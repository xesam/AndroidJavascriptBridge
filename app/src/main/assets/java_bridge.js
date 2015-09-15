/**
 * Created by xesamguo@gmail.com on 9/10/15.
 */

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

        dispatch_remote_call_request: function (remote_call_request) {
            this._remote_callback[remote_call_request.request_id] = remote_call_request;
            JavaBridge.onReceiveCallRequest(remote_call_request.getJsonString());
        },

        dispatch_remote_callback_request: function (remote_callback_request) {
            JavaBridge.onReceiveCallbackRequest(remote_callback_request.getJsonString());
        },

        dispatch_local_call_request: function (local_call_request) {
            var call = this._local_handlers[local_call_request.request_method] || window[local_call_request.request_method];
            if (call) {
                setTimeout(function () {
                    call(local_call_request.request_data, !!local_call_request.callback_id ? local_call_request.callback_id : undefined);
                }, 0);
            }
        },

        dispatch_local_callback_request: function (local_callback_request) {
            var _this = this;
            var callback_id = local_callback_request.callback_id;
            var cache_request = this._remote_callback[callback_id];
            if (cache_request) {
                var callback;
                if (local_callback_request.callback_method) {
                    callback = cache_request.data[local_callback_request.callback_method];
                } else {
                    callback = cache_request.data.extra_callback;
                }
                if (callback) {
                    setTimeout(function () {
                        callback(local_callback_request.request_data);
                        delete _this._remote_callback[callback_id];
                    }, 0);
                } else {
                    delete _this._remote_callback[callback_id];
                }
            }
        }
    };

    /******************** Remote Call ************************/

    function RemoteRequest() {
        this.request_id = new Date().getTime();
        this.getJsonString = function () {
            return JSON.stringify(this.data);
        };
    }

    function RemoteCallRequest(remote_method, remote_data, has_data_callback, extra_callback) {
        RemoteRequest.call(this);
        this.data = remote_data;
        this.data['request_id'] = this.request_id;
        this.data['request_method'] = remote_method;
        this.data['extra_callback'] = extra_callback;
        this.data['has_callback'] = has_data_callback || !!extra_callback;
    }

    function RemoteCallbackRequest(remote_callback_id, remote_data) {
        RemoteRequest.call(this);
        this.data = {};
        this.data['request_id'] = this.request_id;
        this.data['callback_id'] = remote_callback_id;
        this.data['callback_data'] = remote_data;
    }

    /******************** Local Call ************************/

    function LocalRequest(request) {
        this.request_id = request['request_id'];
        this.request_data = request['request_data'];
        this.callback_id = request['callback_id'];
    }

    function LocalCallRequest(request) {
        LocalRequest.call(this, request);
        this.request_method = request['request_method'];
    }

    function LocalCallbackRequest(request) {
        LocalCallbackRequest.call(this, request);
        this.callback_method = request['callback_method'];
    }

    var _dispatcher = new Dispatcher();

    return {

        register_local_request_handler: function (request_method, request_handler) {
            _dispatcher.register_local_request_handler(request_method, request_handler);
        },

        invoke_remote_call: function (remote_method, remote_data, has_data_callback, extra_callback) {
            var remoteRequest = new RemoteCallRequest(remote_method, remote_data, !!has_data_callback, extra_callback);
            _dispatcher.dispatch_remote_call_request(remoteRequest);
        },

        invoke_remote_callback: function (remote_callback_id, remote_data) {
            var remoteRequest = new RemoteCallbackRequest(remote_callback_id, remote_data);
            _dispatcher.dispatch_remote_callback_request(remoteRequest);
        },

        on_receive_request: function (local_request_string) {
            var request = JSON.parse(local_request_string);
            if (request['request_method']) {
                log("dispatch_local_call_request");
                _dispatcher.dispatch_local_call_request(new LocalCallRequest(request));
            } else {
                _dispatcher.dispatch_local_callback_request(new LocalCallbackRequest(request));
                log("dispatch_local_callback_request");
            }
        }
    }
})();