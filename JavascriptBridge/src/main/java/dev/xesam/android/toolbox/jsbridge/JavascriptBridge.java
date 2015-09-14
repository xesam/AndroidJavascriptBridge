package dev.xesam.android.toolbox.jsbridge;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

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
    public void onReceiveRequest(String requestString) {
        log(requestString);
        int requestType = LocalRequest.getType(requestString);
        switch (requestType) {
            case LocalRequest.TYPE_CALL:
                LocalCallRequest localCallRequest = new LocalCallRequest(requestString);
                mDispatcher.dispatchLocalRequest(localCallRequest);
                break;
            case LocalRequest.TYPE_CALLBACK:
                LocalCallbackRequest localCallbackRequest = new LocalCallbackRequest(requestString);
                mDispatcher.dispatchLocalRequest(localCallbackRequest);
                break;
        }
    }

    public void invokeRemoteCall(RemoteCallRequest remoteRequest) {
        mDispatcher.dispatchRemoteRequest(remoteRequest);
    }

    public void invokeRemoteCallback(RemoteCallbackRequest remoteRequest) {
        mDispatcher.dispatchRemoteRequest(remoteRequest);
    }
}
