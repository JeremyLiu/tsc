package com.jec.module.sysmonitor.service;

import com.googlecode.genericdao.search.Search;
import com.jec.base.entity.NetState;
import com.jec.module.sysmonitor.dao.NetConnectDao;
import com.jec.module.sysmonitor.vo.TopoState;
import com.jec.protocol.command.CommandExecutor;
import com.jec.protocol.command.PduCommand;
import com.jec.protocol.command.Result;
import com.jec.module.sysmonitor.dao.CardDao;
import com.jec.module.sysmonitor.dao.CardViewDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.*;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.protocol.command.SlotCommand;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.processor.Processor;
import com.jec.protocol.unit.BCD;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
public class NetWorkStateService extends Thread implements Processor{

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

    private Map<String, TopoState> states = new HashMap<>();

    private Map<Integer,Integer> netUnitStates = new HashMap<>();

    private boolean exit = false;

    private List<NetUnit> netUnits = new ArrayList<>();

    @PostConstruct
    public void init(){
        netWorkListenerService.addProcessor(PduConstants.CMD_TYPE_SBJS,this);
        this.start();
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
        Integer netUnitState = netUnitStates.get(netUnitId);
        if(netUnitState != null)
            state.setState(netUnitState);

        Search search = new Search();
        search.addFilterEqual("netUnitId",netUnitId);
        search.addSort("slot",false);

        List<CardView> cards = cardViewDao.search(search);
        int cardNumber = cards.size();

        CardState[] cardStates = new CardState[cardNumber];
        String path;

        lock.readLock().lock();
        for(int i=0; i<cardNumber; i++){
            path = TopoState.buildPath(netUnitId,i+1);
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
                    path = TopoState.buildPath(netUnitId,i+1,j);
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
//        Map<Integer,Integer> stateMap = new HashMap<>(netUnits.size());
        List<NetState> state = new ArrayList<>(netUnits.size());

        //获取网元及设备的状态
        for(NetUnit netUnit : netUnits) {
            NetState netState = new NetState();
            netState.setId(netUnit.getId());
            if(netUnitStates.containsKey(netUnit.getId()))
                netState.setState(netUnitStates.get(netState.getId()));
            state.add(netState);
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

            int destId = topo.getDestId();
            ConnectState connectState = ConnectState.from(topo);
            Integer netState = netUnitStates.get(topo.getSrcId());
            if(netState == null)
                netState = NetState.UNKNOWN;
            if(netState != NetState.US_NORMAL)
                connectState.setState(netState);
            else {
                String path = TopoState.buildPath(destId, topo.getSlot(), topo.getPort());
                lock.readLock().lock();
                if(states.containsKey(path))
                    connectState.setState(states.get(path).getState());
                else
                    connectState.setState(NetState.UNKNOWN);
                lock.readLock().unlock();
            }
            boolean find = false;
            for(ConnectState origin: connectStates){
                if(origin.getSrcId() == topo.getSrcId() && origin.getDestId() == topo.getDestId()){
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

    @Transactional(readOnly = true)
    public void refreshCardState(NetUnit netUnit){
        CommandExecutor ce = new CommandExecutor();
        Search search = new Search(Card.class);
        SlotCommand slotCommand = new SlotCommand(0,0,0,0);
        ce.setRemoteAddress(netUnit.getIp(), netUnit.getPort());

        search.addFilterEqual("netUnitId", netUnit.getId());
        search.addFilterNotEqual("type", PduConstants.CARD_TYPE_NUL);

        int mainSlot = -1;

        List<Card> cards = cardDao.search(search);
        for(Card card : cards){
            if(card.getType() == PduConstants.CARD_TYPE_MCB){
                mainSlot = card.getSlotNumber();
                break;
            }
        }
        slotCommand.setNetunit(netUnit.getNetId(),mainSlot);
        for(Card card : cards){
            slotCommand.setSlot(card.getSlotNumber(), card.getType());
            ce.execute(slotCommand);
            try{
                Thread.sleep(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public boolean isOnline(int netUnit){
        int state = netUnitStates.get(netUnit);
        return  state== NetState.US_NORMAL || state ==NetState.US_FAILURE;
    }

    @Transactional(readOnly = true)
    private  void setCardState(int netUnitId, int slot, int state){

        NetUnit netUnit = netUnitDao.getNetUnitByNetId(netUnitId);
        if(netUnit == null)
            return;

        state = NetState.cardStateMap(state);
        String path = TopoState.buildPath(netUnit.getId(),slot);
        lock.writeLock().lock();
        if(states.containsKey(path)){
            TopoState s = states.get(path);
            s.setState(state);
        }else
            states.put(path,new TopoState(netUnit.getId(), slot,state));
        lock.writeLock().unlock();
    }

    @Transactional(readOnly = true)
    private void setPortState(int netUnitId, int slot, int port, int state){
        NetUnit netUnit = netUnitDao.getNetUnitByNetId(netUnitId);
        if(netUnit == null)
            return;

        state = NetState.portStateMap(state);
        String path = TopoState.buildPath(netUnit.getId() ,slot, port);
        lock.writeLock().lock();
        if(states.containsKey(path)){
            TopoState s = states.get(path);
            s.setState(state);
        }else
            states.put(path,new TopoState(netUnit.getId(), slot, port, state));
        lock.writeLock().unlock();
    }

    @Override
    public boolean process(PDU pdu) {


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
            deviceStateService.setDeviceState(number.toString(), stateCode);
            System.out.println("device:"+number.toString()+",state:"+stateCode);
        }
        return false;
    }

    @Override
    public void run(){
        TransactionSynchronizationManager.initSynchronization();
        while(!exit) {
            try {
                Thread.sleep(5000);
                List<NetUnit> netUnits = netUnitDao.findAll();

                //获取网元及设备的状态
                for(NetUnit netUnit : netUnits) {
                    int netUnitState = getNetUnitState(netUnit);
                    int netUnitId = netUnit.getId();
                    Integer curState = netUnitStates.get(netUnitId);
                    if( (netUnitState == NetState.US_NORMAL || netUnitState == NetState.US_FAILURE)
                            && (curState == null || curState == NetState.US_OUTLINE
                            || curState == NetState.UNKNOWN)){
                        refreshCardState(netUnit);
                        deviceStateService.refreshState(netUnit);
                    }
                    netUnitStates.put(netUnitId, netUnitState);
                }
            }catch (Exception ie){
                ie.printStackTrace();
            }
        }
    }

    @Transactional(readOnly = true)
    public void refreshAll(){
        List<NetUnit> netUnits = netUnitDao.findAll();
        //获取网元及设备的状态
        for(NetUnit netUnit : netUnits) {
            if(isOnline(netUnit.getId())) {
                refreshCardState(netUnit);
                deviceStateService.refreshState(netUnit);
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean refresh(int netunit){
        NetUnit netUnit = netUnitDao.find(netunit);
        if(netUnit == null)
            return false;
        refreshCardState(netUnit);
        deviceStateService.refreshState(netUnit);
        return true;
    }
}
