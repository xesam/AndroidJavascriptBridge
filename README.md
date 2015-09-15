#AndroidJavascriptBridge

#JS 调用 Java
##Js 简单调用 Java

这个比较常见，适用于同步，没有回调的情况

示例：

java ：

    @JavascriptInterface
    public void java_fn() {
    }

    webView.addJavascriptInterface(this, "Java");

js：

    Java.java_fn();

##Js 回调
两种形式
    js.call({
        data : {},
        fn:function(){}
    })

    js.call(data, function(){
    })

主要问题 ：

1. 普通对象无法无法直接传递
2. 回调函数无法传递

所以，一种处理方式就是在 js 上下文种保持整个请求，在 java 执行完毕之后，从 java 去主动调用 js 方法，找到原始的请求，并执行正确的回调。
示意图如下：

![js_bridge.png](./js_bridge.png)

一个完整的封装实现：[AndroidJavascriptBridge](https://github.com/xesam/AndroidJavascriptBridge)

#Java 调用 Js

同理，反之即可




