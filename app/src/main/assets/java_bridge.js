/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */

JavaBridge = {
    onReceiveRequest: function (data) {
        console.log(data);
        data = JSON.parse(data);
        var s = '{"request_id":350863710,"request_data":{"name":"xesam","age":28},"_callback_id":' + data['request_id'] + ',"_callback_method":"succ"}';
        bridge.on_receive_request(s);
    }
};

window.bridge = (function () {

    function Dispatcher() {
        this._remote_callback = {};
        this._local_handlers = {};
    }

    Dispatcher.prototype = {
        constructor: Dispatcher,

        register_local_request_handler: function (request_method, request_handler) {
            this._local_handlers[request_method] = request_handler;
        },

        dispatch_remote_request: function (remote_request) {
            this._remote_callback[remote_request.request_id] = remote_request;
            console.log(remote_request.getJsonString());
            JavaBridge.onReceiveRequest(remote_request.getJsonString());
        },

        dispatch_local_request: function (local_request) {
            var _this = this;
            if (local_request.is_call()) {
                var call = this._local_handlers[local_request.request_method] || window[local_request.request_method];
                if (call) {
                    setTimeout(function () {
                        call(local_request.request_data);
                    }, 0);
                }
            } else if (local_request.is_callback()) {
                var cache_request = this._remote_callback[local_request.callback_id];
                console.log(cache_request);
                if (cache_request) {
                    console.log(cache_request['data']);
                    console.log(local_request);
                    var callback = cache_request['data'][local_request.callback_method];
                    console.log(callback);
                    if (callback) {
                        setTimeout(function () {
                            callback(local_request.request_data);
                            delete _this._remote_callback[local_request.callback_id];
                        }, 0);
                    }
                }

            }
        }
    };

    /********************************************/

    function RemoteRequest(remote_data) {
        this.request_id = new Date().getTime();
        this.data = remote_data;
        this.data['request_id'] = this.request_id;
        this.getJsonString = function () {
            return JSON.stringify(this.data);
        };
    }

    RemoteRequest.TYPE_CALL = 1;
    RemoteRequest.TYPE_CALLBACK = 1;

    function RemoteCallRequest(remote_method, remote_data, has_data_callback, extra_callback) {
        RemoteRequest.call(this, remote_data);

        this.data['request_type'] = RemoteRequest.TYPE_CALL;
        this.data['request_method'] = remote_method;
        this.data['extra_callback'] = extra_callback;
        this.data['has_callback'] = has_data_callback || !!extra_callback;
    }

    RemoteCallRequest.prototype = RemoteRequest.prototype;

    function RemoteCallbackRequest(remote_data, remote_callback_id) {
        RemoteRequest.call(this, remote_data);

        this.data['request_type'] = RemoteRequest.TYPE_CALLBACK;
        this.data['remote_callback_id'] = remote_callback_id;
    }

    /********************************************/

    function LocalRequest(request_string) {
        console.log('LocalRequest:', request_string);
        var request = JSON.parse(request_string);
        this.request_id = request['request_id'];

        this.request_method = request['request_method'];
        this.request_data = request['request_data'];

        this.callback_id = request['_callback_id'];
        this.callback_method = request['_callback_method'];
    }

    LocalRequest.prototype = {
        constructor: LocalRequest,
        is_call: function () {
            return !!this.request_method;
        },

        is_callback: function () {
            return !!this.callback_id;
        }
    };

    var _dispatcher = new Dispatcher();

    return {

        register_local_request_handler: _dispatcher.register_local_request_handler,

        invoke_remote_call: function (remote_method, remote_data, has_data_callback, extra_callback) {
            var remoteRequest = new RemoteCallRequest(remote_method, remote_data, has_data_callback, extra_callback);
            _dispatcher.dispatch_remote_request(remoteRequest);
        },

        invoke_remote_callback: function (remote_data, remote_callback_id) {
            var remoteRequest = new RemoteCallbackRequest(remote_data, remote_callback_id);
            _dispatcher.dispatch_remote_request(remoteRequest);
        },

        on_receive_request: function (local_request_string) {
            var local_request = new LocalRequest(local_request_string);
            _dispatcher.dispatch_local_request(local_request);
        }
    }
})();