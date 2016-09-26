package com.jec.module.sysconfig.service;

import com.jec.module.sysconfig.command.CardTypeCommand;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.command.Command;
import com.jec.protocol.command.Result;
import com.jec.protocol.pdu.PduConstants;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jeremyliu on 8/6/16.
 */
@Service
public class CardDownloadService extends DownloadService {

    @Transactional(readOnly = true)
    public Response downloadCard(NetUnit netUnit){

        int netUnitId = netUnit.getId();

        if(!netStateService.isOnline(netUnitId))
            return Response.Builder(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");

        List<Card> cards = cardDao.getCardByNetunit(netUnitId);
        Response result = Response.Builder(Response.STATUS_PARAM_ERROR);

        if(cards.size() == 0)
            return result;

        Card mainCard = cardDao.getMainCard(netUnitId);
        if(mainCard == null)
            return result;

        Command command;
        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());
        int netId = netUnit.getNetId();
        int mainSlot = mainCard.getSlotNumber();
        result.status(Response.STATUS_SUCCESS);
        String desc = netUnit.getName() + "板卡类型配置失败槽位:";

        for(Card card : cards){
            if(card.getType() != PduConstants.CARD_TYPE_NUL){
                command = new CardTypeCommand(netId, mainSlot, card.getSlotNumber(), card.getType());
                command.setNeedResponse(true);
                Result res= executor.execute(command);
                if(!res.isSucceed()) {
                    result.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += card.getSlotNumber() + ",";
                }
            }
        }

        if((Integer)result.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0, desc.length() - 1);
            result.message(desc);
        }else
            result.data(true);

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public Response download(int netUnitId){
        NetUnit netUnit = netUnitDao.find(netUnitId);

        if(netUnit == null)
            return Response.Builder(Response.STATUS_PARAM_ERROR);
        else {
            synchronized (executor) {
                return downloadCard(netUnit);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Response download(){
        List<NetUnit> netUnits = netUnitDao.findAll();
        Response response = Response.Builder();
        String desc = "";
        synchronized (executor) {
            for (NetUnit netUnit : netUnits) {
                Response res = downloadCard(netUnit);
                if ((Integer) res.get("status") != Response.STATUS_SUCCESS) {
                    response.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += res.get("message") + ";";
                }
            }
        }
        if((Integer)response.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0,desc.length() - 1);
            response.message(desc);
        }

        return response;
    }

}
