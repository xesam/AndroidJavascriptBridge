package dev.xesam.android.toolbox.jsbridge;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONObject;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public class JavascriptBridge {

    public static final String JAVA_BRIDGE = "JavaBridge";

    private Dispatcher mDispatcher;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public JavascriptBridge(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, JavascriptBridge.JAVA_BRIDGE);
        mDispatcher = new Dispatcher(webView);
    }

    private void log(String string) {
        Log.d(this.getClass().getSimpleName(), string == null ? "null" : string);
    }

    public void registerLocalRequestHandler(String requestMethod, LocalCallRequest.RequestHandler<?> requestHandler) {
        mDispatcher.registerLocalRequestHandler(requestMethod, requestHandler);
    }

    @JavascriptInterface
    public void onReceiveCallRequest(String requestString) {
        log("onReceiveCallRequest:" + requestString);
        LocalCallRequest localCallRequest = new LocalCallRequest(requestString);
        mDispatcher.dispatchLocalRequest(localCallRequest);
    }

    @JavascriptInterface
    public void onReceiveCallbackRequest(String requestString) {
        log("onReceiveCallbackRequest:" + requestString);
        log(requestString);
        LocalCallbackRequest localCallbackRequest = new LocalCallbackRequest(requestString);
        mDispatcher.dispatchLocalRequest(localCallbackRequest);
    }

    public void invokeRemoteCall(RemoteCallRequest remoteRequest) {
        mDispatcher.dispatchRemoteRequest(remoteRequest);
    }

    public void invokeRemoteCallback(RemoteCallbackRequest remoteRequest) {
        mDispatcher.dispatchRemoteRequest(remoteRequest);
    }

    /**
     * extra_callback
     */
    public void deliveryRemoteCallback(LocalCallRequest localCallRequest, JSONObject requestData) {
        RemoteCallbackRequest remoteRequest = new RemoteCallbackRequest(localCallRequest.getCallbackId(), null, requestData);
        invokeRemoteCallback(remoteRequest);
    }

    public void deliveryRemoteCallback(LocalCallRequest localCallRequest, String callbackMethod, JSONObject requestData) {
        RemoteCallbackRequest remoteRequest = new RemoteCallbackRequest(localCallRequest.getCallbackId(), callbackMethod, requestData);
        invokeRemoteCallback(remoteRequest);
    }
}
