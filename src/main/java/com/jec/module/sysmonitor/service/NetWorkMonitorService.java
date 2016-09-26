package com.jec.module.sysmonitor.service;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysmonitor.dao.*;
import com.jec.module.sysmonitor.entity.*;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;
import com.jec.module.sysmonitor.vo.CardConfig;
import com.jec.module.sysmonitor.vo.Device;
import com.jec.module.sysmonitor.vo.NetTopo;
import com.jec.protocol.pdu.PduConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jeremyliu on 5/9/16.
 */

@Service
public class NetWorkMonitorService {

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private CardDao cardDao;

    @Resource
    private CardViewDao cardViewDao;

    @Resource
    private TerminalDeviceViewDao terminalDeviceViewDao;

    @Resource
    private CardTypeDao cardTypeDao;

    @Resource
    private TerminalDeviceDao terminalDeviceDao;

    @Resource
    private NetConnectDao netConnectDao;


    @Transactional
    public int addNetUnit(String name, String ip, int port, int slotCount){
        try {
            NetUnit netUnit = new NetUnit();
            netUnit.setIp(ip);
            netUnit.setName(name);
            netUnit.setPort(port);
            netUnit.setCardCount(slotCount);

            int netId = NetUnit.getIdFromIp(ip);
            if(netUnitDao.getNetUnitByNetId(netId) != null)
                return -2;

            netUnit.setNetId(netId);
            netUnitDao.save(netUnit);

            Card[] cards = new Card[slotCount];

            for (int i = 0; i < slotCount; i++) {
                cards[i] = new Card();
                cards[i].setNetUnitId(netUnit.getId());
                cards[i].setSlotNumber(i+1);
            }
            cardDao.save(cards);

            return netUnit.getId();
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Transactional
    public int modifyNetUnit(int id,String name, String ip, int port, int slotCount){
        NetUnit netUnit = netUnitDao.find(id);
        if(netUnit == null)
            return -1;

        int newId = NetUnit.getIdFromIp(ip);

        //检查新的ip地址是否和其他的冲突
        if(newId!=id && netUnitDao.getNetUnitByNetId(newId) != null)
            return -2;

        netUnit.setName(name);
        netUnit.setPort(port);
        netUnit.setNetId(newId);
        netUnit.setIp(ip);

        //如果当前板卡数量比之前的数量少
        int diff = slotCount - netUnit.getCardCount();
        if(diff < 0){

            //查询板卡号大于slotCount的光传输板
            Search search = new Search(Card.class);
            search.addFilterEqual("netUnitId", newId);
            search.addFilterEqual("type",PduConstants.CARD_TYPE_OTB);
            search.addFilterGreaterThan("slotNumber", slotCount);

            List<Card> cards = cardDao.search(search);
            List<Integer> cardSlots = new ArrayList<>(cards.size());

            for(Card card : cards){
                cardSlots.add(card.getSlotNumber());
            }
            //删除连接信息
            netConnectDao.removeConnect(newId, cardSlots);

            //TODO:删除终端设备

            //删除板卡
            search.removeFiltersOnProperty("type");
            cards = cardDao.search(search);
            cardDao.remove(cards.toArray(new Card[cards.size()]));

        }else{
            Card[] cards = new Card[diff];

            for (int i = 0; i < diff; i++) {
                cards[i] = new Card();
                cards[i].setNetUnitId(netUnit.getId());
                cards[i].setSlotNumber(i+1+netUnit.getCardCount());
            }
            cardDao.save(cards);
            netUnit.setCardCount(slotCount);
        }
        return newId;
    }

    @Transactional
    public boolean saveNetConnect(int srcId, int destId, int slot, int port){
        NetUnit src = netUnitDao.find(srcId);
        if(src == null)
            return false;
        NetUnit dest = netUnitDao.find(destId);
        if(dest == null)
            return false;

        //判断是否配置光传输板
        Search search = new Search(CardView.class);
        search.addFilterEqual("netUnitId", destId);
        search.addFilterEqual("slot", slot);
        search.addFilterEqual("code", PduConstants.CARD_TYPE_OTB);
        search.addFilterGreaterOrEqual("portCount", port);

        if(cardViewDao.searchUnique(search) == null)
            return false;

        NetConnectId netConnectId = new NetConnectId(srcId,destId);

        NetConnect netConnect = netConnectDao.find(netConnectId);

        if(netConnect == null) {
            netConnect = new NetConnect();
            netConnect.setSrcId(srcId);
            netConnect.setDestId(destId);
            netConnect.setSlot(slot);
            netConnect.setPort(port);
        }else{
            netConnect.setPort(port);
            netConnect.setSlot(slot);
        }

        netConnectDao.save(netConnect);
        return true;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getNetUnitAndDevice(){
        List<NetUnit> netUnits = netUnitDao.findAll();
        List<NetConnect> connect =  netConnectDao.findAll();
        List<NetTopo> topo = new ArrayList<NetTopo>();

        for(NetUnit netUnit : netUnits){
            NetTopo netTopo = new NetTopo();
            netTopo.setName(netUnit.getName());
            netTopo.setId(netUnit.getId());

            Search search = new Search();
            search.addFilterEqual("netUnitId",netUnit.getId());

            List<TerminalDeviceView> terminals = terminalDeviceViewDao.search(search);
            List<Device> devices = new ArrayList<Device>();
            for(TerminalDeviceView terminal : terminals){
                Device device = new Device(terminal);
                devices.add(device);
            }
            netTopo.setDevices(devices);
            topo.add(netTopo);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("device",topo);
        map.put("connect", connect);
        return map;
    }

    @Transactional(readOnly = true)
    public List<NetUnit> getAllNetUnit(){
        return netUnitDao.findAll();
    }

    @Transactional(readOnly = true)
    public NetUnit getNetUnit(int id){
        return netUnitDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<CardConfig> getCardByNetUnit(int netUnitId){
        Search search = new Search();
        search.addFilterEqual("netUnitId", netUnitId);
        search.addSort("slot",false);
        List<CardView> cardViews = cardViewDao.search(search);
        List<CardConfig> cardConfigs = new ArrayList<>(cardViews.size());

        for(CardView cardView: cardViews) {
            CardConfig cardConfig = new CardConfig(cardView);
            search.clear();
            search.addFilterEqual("cardId", cardView.getId());
            List<TerminalDevice> devices = terminalDeviceDao.search(search);
            cardConfig.setDevices(devices.toArray(new TerminalDevice[devices.size()]));
            cardConfigs.add(cardConfig);
        }
        return cardConfigs;
    }

    @Transactional
    public boolean modifyCard(List<CardConfig> cardConfigs){

        Map<Integer, CardType> cardTypeMap = getCardTypeMap();

        List<Card> cards = new ArrayList<>();
        List<TerminalDevice> terminalDevices = new ArrayList<>();
        List<Integer> cardIds = new ArrayList<>();
        Set<Integer> portSet = new HashSet<>();

        for(CardConfig cardConfig : cardConfigs){
            TerminalDevice[] devices = cardConfig.getDevices();
            int cardPort = cardTypeMap.get(cardConfig.getType()).getPortCount();
            if(!cardTypeMap.containsKey(cardConfig.getType())
                    || cardPort < devices.length)
                continue;

            Card card = new Card();
            card.setId(cardConfig.getId());
            card.setType(cardConfig.getType());
            cards.add(card);

            portSet.clear();
            for(TerminalDevice device: devices){
                device.setCardId(cardConfig.getId());
                int port = device.getCardPort();
                if(port < cardPort &&
                        device.getName().length()>0 && device.getName().length()<60
                        && !portSet.contains(port)) {
                    terminalDevices.add(device);
                    portSet.add(port);
                }
            }
            if(devices.length>0)
                cardIds.add(cardConfig.getId());
        }
        try {
            if(cards.size()>0)
                cardDao.save(cards.toArray(new Card[cards.size()]));
            terminalDeviceDao.removeDeviceByCard( cardIds.toArray(new Integer[cardIds.size()]));
            terminalDeviceDao.save(terminalDevices.toArray(new TerminalDevice[terminalDevices.size()]));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }


    @Transactional
    public boolean modifyCard(int id,int type){
        Card card = cardDao.find(id);
        if(card == null)
            return false;
        CardType cardType = cardTypeDao.find(type);
        if(cardType == null)
            return false;
        card.setType(type);

//        cardDao.save(card);
        return true;
    }

    @Transactional
    public boolean removeNetUnit(int netUnitId){

        try {
            terminalDeviceDao.removeDeviceByNetUnit(netUnitId);
            cardDao.removeByNetUnit(netUnitId);
            netConnectDao.removeConnect(netUnitId);
            netUnitDao.removeById(netUnitId);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Transactional
    public List<TerminalDevice> getDeviceByCard(int cardId){
        Search search = new Search(TerminalDevice.class);
        search.addFilterEqual("cardId", cardId);
        return terminalDeviceDao.search(search);
    }

    @Transactional
    public List<CardType> getAllCardType(){
        return cardTypeDao.findAll();
    }

    @Transactional
    public Map<Integer, CardType> getCardTypeMap(){
        List<CardType> cardTypes = cardTypeDao.findAll();
        Map<Integer, CardType> map= new HashMap<Integer, CardType>();
        for(CardType cardType : cardTypes)
            map.put(cardType.getCode(), cardType);

        return map;
    }

    @Transactional
    public List<CardView> getNetUnitCardSlot(int netunit, int type){
        Search search = new Search(CardView.class);
        search.addFilterEqual("netUnitId", netunit);
        if(type>=0)
            search.addFilterEqual("code", type);
        return cardViewDao.search(search);
    }
}
