package com.jec.module.sysmonitor.service;

import com.googlecode.genericdao.search.Search;
import com.jec.base.entity.NetState;
import com.jec.module.sysmonitor.dao.NetConnectDao;
import com.jec.module.sysmonitor.vo.State;
import com.jec.protocol.command.CommandExecutor;
import com.jec.protocol.command.PduCommand;
import com.jec.protocol.command.Result;
import com.jec.module.sysmonitor.dao.CardDao;
import com.jec.module.sysmonitor.dao.CardViewDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.*;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.processor.Processor;
import com.jec.protocol.unit.BCD;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Service
@Scope
public class NetWorkStateService implements Processor, ApplicationContextAware{

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private CardDao cardDao;

    @Resource
    private CardViewDao cardViewDao;

    @Resource
    private NetConnectDao netConnectDao;

    @Resource
    private NetWorkListenerService netWorkListenerService;

    @Resource
    private DeviceStateService deviceStateService;

    private CommandExecutor ce = new CommandExecutor(false);

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Map<String, State> states = new HashMap<String, State>();

    @PostConstruct
    public void init(){
        netWorkListenerService.addProcessor(PduConstants.CMD_TYPE_SBJS,this);
    }

    @Transactional(readOnly = true)
    public NetUnitState getInitState(int netUnitId){
        NetUnit netUnit =netUnitDao.find(netUnitId);
        if(netUnit == null)
            return null;
        else
            return getInitState(netUnit);
    }

    @Transactional(readOnly = true)
    private NetUnitState getInitState(NetUnit netUnit){

        int netUnitId = netUnit.getId();
        NetUnitState state = new NetUnitState();
        state.setState(getNetUnitState(netUnit));

        Search search = new Search();
        search.addFilterEqual("netUnitId",netUnitId);
        search.addSort("slot",false);

        List<CardView> cards = cardViewDao.search(search);
        int cardNumber = cards.size();

        CardState[] cardStates = new CardState[cardNumber];
        String path;

        lock.readLock().lock();
        for(int i=0; i<cardNumber; i++){
            path = State.buildPath(netUnitId,i+1);
            CardView card = cards.get(i);
            cardStates[i] = new CardState();

            cardStates[i].setId(card.getId());

            if(states.containsKey(path)) {
                int slotState = states.get(path).getState();
                cardStates[i].setState(slotState);
                if(slotState == NetState.CS_FAILURE)
                    state.setState(NetState.US_FAILURE);
            }

            int portNumber = card.getPortCount();
            if(portNumber > 0){
                NetState[] portStates = new NetState[portNumber];
                for(int j=0; j< portNumber; j++){
                    portStates[j] = new NetState();
                    portStates[j].setId(j);
                    path = State.buildPath(netUnitId,i,j);
                    if(states.containsKey(path)) {
                        int portState = states.get(path).getState();
                        portStates[j].setState(portState);
                        if(portState == NetState.PS_FAILURE){
                            cardStates[i].setState(NetState.CS_FAILURE);
                            state.setState(NetState.US_FAILURE);
                        }
                    }
                }
                cardStates[i].setPortState(portStates);
            }
        }
        lock.readLock().unlock();
        state.setCardStates(cardStates);
        return state;
    }

    private int getNetUnitState(NetUnit netUnit){

        Result result = ce.open();
        if(!result.isSucceed()) {
            System.err.println(result.getDescription());
            return NetState.UNKNOWN;
        }
        String ip = netUnit.getIp();
        ce.setRemoteAddress(ip, netUnit.getPort());
        ce.setTimeout(2000);

        Search search = new Search();
        search.addFilterEqual("netUnitId", netUnit.getId());
        search.addFilterEqual("type", 1);

        List<Card> cards = cardDao.search(search);
        if(cards.size() == 0)
            return NetState.UNKNOWN;

//        int count = 0;
//        int limit = 3;
        boolean force = true;


        PduCommand command = new PduCommand(netUnit.getId(),cards.get(0).getSlotNumber(), force);
        result = ce.execute(command);

        if(result.isSucceed()) {
            if(command.getResult())
                return NetState.US_NORMAL;
            else
                return NetState.US_OUTLINE;
        }else
            return NetState.US_OUTLINE;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> readNetUnitState(){
        List<NetUnit> netUnits = netUnitDao.findAll();
        Map<Integer,Integer> stateMap = new HashMap<>(netUnits.size());
        List<NetState> state = new ArrayList<>(netUnits.size());

        //获取网元及设备的状态
        for(NetUnit netUnit : netUnits) {
            int netUnitState = getNetUnitState(netUnit);
            NetState netState = new NetState();
            netState.setId(netUnit.getId());
            netState.setState(netUnitState);
            state.add(netState);
            stateMap.put(netUnit.getId(), netUnitState);
        }

        //获取连接状态
        List<NetConnect> topos =  netConnectDao.findAll();
        List<ConnectState> connectStates = new ArrayList<>(netUnits.size());
        for(NetUnit src: netUnits){
            for( NetUnit dest: netUnits)
                if(src != dest) {
                    ConnectState connectState = new ConnectState();
                    connectState.setSrcId(src.getId());
                    connectState.setDestId(dest.getId());
                    connectStates.add(connectState);
                }
        }
        for(NetConnect topo : topos){

            int srcId = topo.getSrcId();
            ConnectState connectState = ConnectState.from(topo);
            if(stateMap.get(srcId) != NetState.US_NORMAL)
                connectState.setState(stateMap.get(srcId));
            else {
                String path = State.buildPath(srcId, topo.getSlot(), topo.getPort());
                lock.readLock().lock();
                if(states.containsKey(path))
                    connectState.setState(states.get(path).getState());
                else
                    connectState.setState(NetState.UNKNOWN);
                lock.readLock().unlock();
            }
            boolean find = false;
            for(ConnectState origin: connectStates){
                if(origin.getSrcId() == srcId && origin.getDestId() == topo.getDestId()){
                    find = true;
                    origin = connectState;
                    break;
                }
            }
            if(!find)
                connectStates.add(connectState);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("netunit", state);
        map.put("connect", connectStates);
        map.put("device", deviceStateService.queryDevice());
        return map;
    }


    @Transactional(readOnly = true)
    public void readCardState(int netUnitId){
        Search search = new Search();
        search.addFilterEqual("netUnitId", netUnitId);
        search.addSort("slot",false);
    }

//    private void setNetUnitState(int netUnit, int state){
//        String path = State.buildPath(netUnit);
//        lock.writeLock().lock();
//        if(states.containsKey(path)){
//            State s = states.get(path);
//            s.setState(state);
//        }else
//            states.put(path,new State(netUnit, state));
//        lock.writeLock().unlock();
//    }

    private  void setCardState(int netUnit, int slot, int state){
        state = NetState.cardStateMap(state);
        String path = State.buildPath(netUnit,slot);
        lock.writeLock().lock();
        if(states.containsKey(path)){
            State s = states.get(path);
            s.setState(state);
        }else
            states.put(path,new State(netUnit, slot,state));
        lock.writeLock().unlock();
    }

    private void setPortState(int netUnit, int slot, int port, int state){
        state = NetState.deviceStateMap(state);
        String path = State.buildPath(netUnit,slot, port);
        lock.writeLock().lock();
        if(states.containsKey(path)){
            State s = states.get(path);
            s.setState(state);
        }else
            states.put(path,new State(netUnit, slot, port, state));
        lock.writeLock().unlock();
    }

    @Override
    public boolean process(PDU pdu) {
        if(pdu.length() != (PduConstants.LENGTH_OF_HEAD + 3))
            return false;

        int type = ProtocolUtils.getCmdConfig(pdu);

        int offset = PduConstants.LENGTH_OF_HEAD;

        int netunit = ProtocolUtils.getSourId(pdu);

        if(type == PduConstants.MONITOR_NET_CARD) {
            offset++;//int type = pdu.getInt8(offset++);
            int slot = pdu.getInt8(offset);
            offset++;
            int state = pdu.getInt8(offset);
            setCardState(netunit, slot, state);
            return true;
        }else if( type == PduConstants.MONITOR_NET_PORT){
            offset++;//int type = pdu.getInt8(offset++);
            int slot = pdu.getInt8(offset);
            offset++;
            int port = pdu.getInt8(offset);
            offset++;
            int state = pdu.getInt8(offset);
            setPortState(netunit, slot, port, state);
            return true;
        }else if (type == PduConstants.MONITOR_NET_DEVICE){
            BCD number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
            offset += PduConstants.LENGTH_OF_BCD;

            int stateCode = pdu.getInt8(offset);
            System.out.println("device:"+number.toString()+",state:"+stateCode);
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeanDefinitionNames();
    }
}
