package dev.xesam.android.toolbox.jsbridge;

import android.os.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public abstract class RemoteRequest {
    public static final String REQUEST_ID = "request_id";
    public static final String REQUEST_DATA = "request_data";

    protected long requestId;
    protected JSONObject requestData;

    public RemoteRequest(JSONObject requestData) {
        this.requestId = SystemClock.elapsedRealtime();
        this.requestData = requestData;
    }

    public long getRequestId() {
        return requestId;
    }

    public JSONObject getRequestData() {
        return requestData;
    }

    protected abstract void inflate(JSONObject jsonObject) throws JSONException;

    public String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(REQUEST_ID, requestId);
            jsonObject.put(REQUEST_DATA, requestData);
            inflate(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
