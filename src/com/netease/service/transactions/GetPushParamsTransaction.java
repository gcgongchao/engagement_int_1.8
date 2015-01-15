package com.netease.service.transactions;


import com.google.gson.JsonElement;
import com.netease.common.http.THttpRequest;
import com.netease.engagement.app.EngagementApp;
import com.netease.service.preferMgr.EgmPrefHelper;
import com.netease.service.protocol.EgmProtocol;
import com.netease.service.protocol.EgmServiceCode;
import com.netease.service.protocol.meta.PushParamsInfo;

/**
 * 获取push所需的参数
 * @author Byron(hzchenlk&corp.netease.com)
 * @version 1.0
 */
public class GetPushParamsTransaction extends EgmBaseTransaction {
    public GetPushParamsTransaction(){
        super(TRANSACTION_TYPE_GET_PUSH_PARAMS);
    }
    
    @Override
    public void onTransact() {
        THttpRequest request = EgmProtocol.getInstance().createGetPushParamsRequest();
        sendRequest(request);
    }

    @Override
    public void onEgmTransactionSuccess(int code, Object obj){
        super.onEgmTransactionSuccess(code, obj);
        
        PushParamsInfo info = null;
        if (obj != null && obj instanceof JsonElement) {
            info = PushParamsInfo.fromJson((JsonElement)obj);
        }
        
        if (info != null) {
            savePushParamsInfo(info);
            notifyMessage(EgmServiceCode.TRANSACTION_SUCCESS, info);
        } 
        else {
            notifyDataParseError();
        }
    }
    
    /** 保存信息 */
    private void savePushParamsInfo(PushParamsInfo info){
        if(info == null)
            return;
        
        EgmPrefHelper.putNonce(EngagementApp.getAppInstance(), info.nonce);
        EgmPrefHelper.putExpire(EngagementApp.getAppInstance(), info.expire);
        EgmPrefHelper.putSignature(EngagementApp.getAppInstance(), info.signature);
    }
}
