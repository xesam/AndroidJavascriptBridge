package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public abstract class LocalRequest {

    public static final String REQUEST_ID = "request_id";

    private final String requestString;
    private long requestId;

    protected LocalRequest(String remoteRequestString) {
        requestString = remoteRequestString;
        try {
            JSONObject rawJsonObject = new JSONObject(remoteRequestString);
            requestId = rawJsonObject.getLong(REQUEST_ID);
            parseRemoteExtra(remoteRequestString, rawJsonObject);
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

    protected void parseRemoteExtra(String remoteRequestString, JSONObject jsonObject) throws JSONException {

    }

}
