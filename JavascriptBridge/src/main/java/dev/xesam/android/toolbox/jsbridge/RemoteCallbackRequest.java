package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/14/15.
 */
public class RemoteCallbackRequest extends RemoteRequest {

    public static final String CALLBACK_ID = "callback_id";
    public static final String CALLBACK_METHOD = "callback_method";

    private long callbackId;
    private String callbackMethod;

    public RemoteCallbackRequest(long callbackId, String callbackMethod, JSONObject requestData, RemoteCallRequest.RemoteRequestListener remoteRequestListener) {
        super(requestData);
        this.callbackId = callbackId;
        this.callbackMethod = callbackMethod;
    }

    public long getCallbackId() {
        return callbackId;
    }

    public String getCallbackMethod() {
        return callbackMethod;
    }

    @Override
    protected void inflate(JSONObject jsonObject) throws JSONException {
        jsonObject.put(CALLBACK_ID, callbackId);
        jsonObject.put(CALLBACK_METHOD, callbackMethod);
    }
}
