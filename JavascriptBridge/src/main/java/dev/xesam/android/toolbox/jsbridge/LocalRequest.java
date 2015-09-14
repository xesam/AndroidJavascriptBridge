package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public abstract class LocalRequest {

    public static final int TYPE_CALL = 1;
    public static final int TYPE_CALLBACK = 2;
    public static final int TYPE_UNKNOWN = 3;

    public static int getType(String remoteRequestString) {
        try {
            JSONObject rawJsonObject = new JSONObject(remoteRequestString);
            return rawJsonObject.getInt(REQUEST_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return TYPE_UNKNOWN;
    }

    public static final String REQUEST_TYPE = "request_type";
    public static final String REQUEST_ID = "request_id";

    private final String requestString;

    private int requestType = TYPE_CALL;
    private long requestId;

    protected LocalRequest(String remoteRequestString) {
        requestString = remoteRequestString;
        try {
            JSONObject rawJsonObject = new JSONObject(remoteRequestString);
            requestType = rawJsonObject.getInt(REQUEST_TYPE);
            requestId = rawJsonObject.getLong(REQUEST_ID);
            parseRemoteExtra(remoteRequestString, rawJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getRequestString() {
        return requestString;
    }

    public int getRequestType() {
        return requestType;
    }

    public long getRequestId() {
        return requestId;
    }

    protected void parseRemoteExtra(String remoteRequestString, JSONObject jsonObject) throws JSONException {

    }

}
