package dev.xesam.android.toolbox.jsbridge;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/14/15.
 */
public class RemoteCallRequest extends RemoteRequest {

    public interface RemoteRequestListener {
        void handle(LocalCallbackRequest localRequest);
    }

    public static final String REQUEST_METHOD = "request_method";

    private String requestMethod;
    private RemoteRequestListener remoteRequestListener;

    public RemoteCallRequest(String requestMethod, JSONObject requestData, RemoteRequestListener remoteRequestListener) {
        super(requestData);
        this.requestMethod = requestMethod;
        this.remoteRequestListener = remoteRequestListener;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public RemoteRequestListener getRemoteRequestListener() {
        return remoteRequestListener;
    }

    @Override
    protected void inflate(JSONObject jsonObject) throws JSONException {
        jsonObject.put(REQUEST_METHOD, requestMethod);
        jsonObject.put(REQUEST_DATA, requestData);
    }
}
