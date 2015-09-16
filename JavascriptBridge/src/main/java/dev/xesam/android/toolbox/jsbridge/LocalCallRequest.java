package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/14/15.
 */
public class LocalCallRequest extends LocalRequest {

    public interface RequestHandler {
        void handle(LocalCallRequest localCallRequest);
    }

    public static final String REQUEST_METHOD = "request_method";
    public static final String HAS_CALLBACK = "has_callback";

    private String requestMethod;
    private boolean callback;

    public LocalCallRequest(String remoteRequestString) {
        super(remoteRequestString);
    }

    @Override
    protected void parseRequestData(String remoteRequestString, JSONObject jsonObject) throws JSONException {
        requestMethod = jsonObject.getString(REQUEST_METHOD);
        callback = jsonObject.getBoolean(HAS_CALLBACK);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public boolean hasCallback() {
        return callback;
    }

    public long getCallbackId() {
        return getRequestId();
    }
}
