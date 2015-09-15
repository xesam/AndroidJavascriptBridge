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

    public JavascriptBridge() {

    }

    private void log(String string) {
        Log.e(this.getClass().getSimpleName(), string == null ? "null" : string);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public void bindWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, JavascriptBridge.JAVA_BRIDGE);
        mDispatcher = new Dispatcher(webView);
    }

    public void registerLocalRequestHandler(String requestMethod, LocalCallRequest.RequestHandler<?> requestHandler) {
        mDispatcher.registerLocalRequestHandler(requestMethod, requestHandler);
    }

    @JavascriptInterface
    public void onReceiveCallRequest(String requestString) {
        log(requestString);
        LocalCallRequest localCallRequest = new LocalCallRequest(requestString);
        mDispatcher.dispatchLocalRequest(localCallRequest);
    }

    @JavascriptInterface
    public void onReceiveCallbackRequest(String requestString) {
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

    public void deliveryRemoteCallback(LocalCallRequest localCallRequest, String callbackMethod, JSONObject requestData) {
        RemoteCallbackRequest remoteRequest = new RemoteCallbackRequest(localCallRequest.getCallbackId(), callbackMethod, requestData);
        invokeRemoteCallback(remoteRequest);
    }
}
