package dev.xesam.android.toolbox.jsbridge;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */
public class Dispatcher {

    long sequence = 0;
    private WebView mWebView;
    private Handler invokeHandler = new Handler(Looper.getMainLooper());

    public Dispatcher(WebView webView) {
        mWebView = webView;
    }

    private Map<Long, RemoteCallRequest.RemoteRequestListener> remoteRequestListeners = new HashMap<>();

    private Map<String, LocalCallRequest.RequestHandler<?>> handlers = new HashMap<>();

    public void registerLocalRequestHandler(String requestMethod, LocalCallRequest.RequestHandler<?> requestHandler) {
        handlers.put(requestMethod, requestHandler);
    }

    public void dispatchLocalRequest(LocalCallRequest localRequest) {
        String requestMethod = localRequest.getRequestMethod();
        if (handlers.containsKey(requestMethod)) {
            LocalCallRequest.RequestHandler handler = handlers.get(requestMethod);
            handler.handle(localRequest, handler.formJson(localRequest.getRequestString()));
        }
    }

    public void dispatchLocalRequest(LocalCallbackRequest localRequest) {
        RemoteCallRequest.RemoteRequestListener remoteRequestListener = remoteRequestListeners.get(localRequest.getCallbackId());
        if (remoteRequestListener != null) {
            remoteRequestListener.handle(localRequest);
            remoteRequestListeners.remove(localRequest.getCallbackId());
        }
    }

    public void dispatchRemoteRequest(RemoteCallRequest remoteRequest) {
        sequence++;
        if (remoteRequest.getRemoteRequestListener() != null) {
            remoteRequestListeners.put(sequence, remoteRequest.getRemoteRequestListener());
        }
        evaluateJavascript(remoteRequest);
    }

    public void dispatchRemoteRequest(RemoteCallbackRequest remoteRequest) {
        evaluateJavascript(remoteRequest);
    }

    private void evaluateJavascript(final RemoteRequest remoteRequest) {
        invokeHandler.post(new Runnable() {
            @Override
            public void run() {

                String script = String.format(Locale.getDefault(), "window.bridge.on_receive_request('%s')", remoteRequest.toJSONString());
                Log.e("script", script);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebView.evaluateJavascript(script, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Toast.makeText(mWebView.getContext(), value, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    mWebView.loadUrl("javascript:" + script);
                }

            }
        });
    }
}
