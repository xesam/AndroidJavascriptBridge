package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public abstract class LocalRequest {

    public static final String REQUEST_ID = "request_id";
    public static final String REQUEST_DATA = "request_data";

    private final String requestString;
    private long requestId;
    private JSONObject requestData;

    protected LocalRequest(String remoteRequestString) {
        requestString = remoteRequestString;
        try {
            JSONObject requestData = new JSONObject(remoteRequestString);
            requestId = requestData.getLong(REQUEST_ID);
            parseRequestData(remoteRequestString, requestData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRequestString() {
        return requestString;
    }

    public long getRequestId() {
        return requestId;
    }

    public JSONObject getRequestData() {
        return requestData;
    }

    protected void parseRequestData(String requestString, JSONObject jsonObject) throws JSONException {

    }

}
