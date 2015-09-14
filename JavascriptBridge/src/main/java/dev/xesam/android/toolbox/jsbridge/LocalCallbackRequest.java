package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/14/15.
 */
public class LocalCallbackRequest extends LocalRequest {

    private long callbackId;

    public LocalCallbackRequest(String remoteRequestString) {
        super(remoteRequestString);
    }

    @Override
    protected void parseRemoteExtra(String remoteRequestString, JSONObject jsonObject) throws JSONException {
        callbackId = getRequestId();
    }

    public long getCallbackId() {
        return callbackId;
    }
}
