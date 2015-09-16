package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/14/15.
 */
public class LocalCallbackRequest extends LocalRequest {

    public static final String CALLBACK_ID = "callback_id";
    public static final String CALLBACK_DATA = "callback_data";
    
    private long callbackId;
    private JSONObject callbackData;

    public LocalCallbackRequest(String remoteRequestString) {
        super(remoteRequestString);
    }

    @Override
    protected void parseRequestData(String remoteRequestString, JSONObject jsonObject) throws JSONException {
        callbackId = jsonObject.getLong(CALLBACK_ID);
        callbackData = jsonObject.getJSONObject(CALLBACK_DATA);
    }

    public long getCallbackId() {
        return callbackId;
    }

    public JSONObject getCallbackData() {
        return callbackData;
    }
}
