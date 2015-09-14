package dev.xesam.android.toolbox.jsbridge.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import dev.xesam.android.toolbox.jsbridge.JavascriptBridge;
import dev.xesam.android.toolbox.jsbridge.LocalCallRequest;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.wv);
        webView.getSettings().setJavaScriptEnabled(true);

        final JavascriptBridge javascriptBridge = new JavascriptBridge();
        javascriptBridge.bindWebView(webView);

        javascriptBridge.registerLocalRequestHandler("java_fn1", new LocalCallRequest.RequestHandler<Person>() {

            @Override
            public Person formJson(String requestString) {
                return null;
            }

            @Override
            public void handle(LocalCallRequest localCallRequest, Person data) {

                Toast.makeText(getApplicationContext(), "java_fn1：" + localCallRequest.getRequestId(), Toast.LENGTH_SHORT).show();

                if (localCallRequest.hasCallback()) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", "xesam");
                        jsonObject.put("age", 28);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    javascriptBridge.deliveryRemoteCallback(localCallRequest, "succ", jsonObject);
                }
            }
        });

        webView.loadUrl("file:///android_asset/java_bridge.html");

    }
}

class Person {
    String name;
    String blog;
}