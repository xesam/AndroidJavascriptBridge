package dev.xesam.android.toolbox.jsbridge.demo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import dev.xesam.android.toolbox.jsbridge.JavascriptBridge;
import dev.xesam.android.toolbox.jsbridge.LocalCallRequest;
import dev.xesam.android.toolbox.jsbridge.LocalCallbackRequest;
import dev.xesam.android.toolbox.jsbridge.RemoteCallRequest;

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
                Gson gson = new Gson();
                return gson.fromJson(requestString, Person.class);
            }

            @Override
            public void handle(LocalCallRequest localCallRequest, Person data) {
                Toast.makeText(getApplicationContext(), data.name, Toast.LENGTH_SHORT).show();
            }
        });

        javascriptBridge.registerLocalRequestHandler("java_fn2", new LocalCallRequest.RequestHandler<Person>() {

            @Override
            public Person formJson(String requestString) {
                return null;
            }

            @Override
            public void handle(LocalCallRequest localCallRequest, Person data) {

                Toast.makeText(getApplicationContext(), "java_fn2：" + localCallRequest.getRequestId(), Toast.LENGTH_SHORT).show();

                if (localCallRequest.hasCallback()) {
                    javascriptBridge.deliveryRemoteCallback(localCallRequest, "succ", getData());
                }
            }
        });

        javascriptBridge.registerLocalRequestHandler("java_fn3", new LocalCallRequest.RequestHandler<Person>() {

            @Override
            public Person formJson(String requestString) {
                Gson gson = new Gson();
                return gson.fromJson(requestString, Person.class);
            }

            @Override
            public void handle(LocalCallRequest localCallRequest, Person data) {

                Toast.makeText(getApplicationContext(), "java_fn3：" + data.name, Toast.LENGTH_SHORT).show();

                if (localCallRequest.hasCallback()) {
                    javascriptBridge.deliveryRemoteCallback(localCallRequest, null, getData());
                }
            }
        });

        webView.loadUrl("file:///android_asset/java_bridge.html");

        findViewById(R.id.js_fn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteCallRequest remoteCallRequest = new RemoteCallRequest("js_fn1", getData(), null);
                javascriptBridge.invokeRemoteCall(remoteCallRequest);

            }
        });

        findViewById(R.id.js_fn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteCallRequest remoteCallRequest = new RemoteCallRequest("js_fn2", getData(), new RemoteCallRequest.RemoteRequestListener() {
                    @Override
                    public void handle(LocalCallbackRequest localRequest) {
                        Toast.makeText(getApplicationContext(), localRequest.getRequestString(), Toast.LENGTH_SHORT).show();
                    }
                });
                javascriptBridge.invokeRemoteCall(remoteCallRequest);
            }
        });

        findViewById(R.id.js_fn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoteCallRequest remoteCallRequest = new RemoteCallRequest("js_fn3", getData(), null);
                javascriptBridge.invokeRemoteCall(remoteCallRequest);

            }
        });

    }

    public JSONObject getData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "xesam");
            jsonObject.put("blog", "https://github.com/xesam");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}

class Person {
    String name;
    String blog;

    public Person(String name, String blog) {
        this.name = name;
        this.blog = blog;
    }
}