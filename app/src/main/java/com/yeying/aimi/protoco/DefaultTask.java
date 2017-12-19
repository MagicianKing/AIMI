/** * */package com.yeying.aimi.protoco;import android.app.Activity;import android.content.Context;import android.os.Bundle;import android.os.Handler;import android.os.Message;import com.yeying.aimi.protoco.path.Result;import com.yeying.aimi.protocol.request.Arg;import com.yeying.aimi.protocol.request.Request;import com.yeying.aimi.protocol.request.RequestListener;import com.yeying.aimi.protocol.request.Response;import com.yeying.aimi.utils.BooleanUtil;import com.yeying.aimi.utils.JSONUtil;import com.yeying.aimi.utils.Tools;import com.yeying.aimi.utils.Utils;import java.net.HttpURLConnection;/** * @author sparrow */public class DefaultTask {    private static final int EVENT_ON_PROGRESS = 0;    // private static final int EVENT_WRITE_PROGRESS = 1;    // private static final int EVENT_ON_EXCEPTION = 3;    private static final int EVENT_ON_ERROR = 2;    private static final int EVENT_ON_DONE = 4;    private static final int EVENT_ON_FINISH = 5;    private static final int connectTimeout = 10000;    private static final String HOST = Tools.normal_web;    private static final String HOST_OMIT = Tools.BEGIN_URL;// 生产环境地址  https 端口10031    //private static final String HOST = "https://api.ipagat.com:10031/hi8_mobile_web/servlet/mainDes.cl";//	private static final String HOST = "https://api.ipagat.com:10031/hi8_mobile_web/servlet/main.cl";//	private static final String HOST_OMIT = "https://api.ipagat.com:10031";    //线下测试 https: 端口10031//	private static final String HOST = "https://hexinjingu.ipagat.com:10031/hi8_mobile_web/servlet/main.cl";//	private static final String HOST_OMIT = "https://hexinjingu.ipagat.com:10031";// 外网地址 	端口10030//	private static final String HOST = "http://124.193.122.18:10030/hi8_mobile_web/servlet/main.cl";//	private static final String HOST_OMIT = "http://124.193.122.18:10030";    //加密//	private static final String HOST = "http://124.193.122.18:10030/hi8_mobile_web//servlet/mainDes.cl";//	private static final String HOST_OMIT = "http://124.193.122.18:10030";//	// 内网地址//	private static final String HOST = "http://192.168.1.90:10030/hi8_mobile_web/servlet/main.cl";//	private static final String HOST_OMIT = "http://192.168.1.90:10030";    // 内网地址//	private static final String HOST = "http://192.168.1.90:10030/hi8_mobile_web/servlet/main.cl";//	private static final String HOST_OMIT = "http://192.168.1.90:10030";    //外网123.57.36.131   124.193.122.18    // 调试用//	private static final String HOST = "http://192.168.1.121:8080/hi8_mobile_web/servlet/main.cl";  //	private static final String HOST_OMIT = "http://192.168.1.121:8080";    // 线上环境接口地址    // private static final String HOST =    // "http://87ByVKofmb.agan365.com/index.php";    // private static final String HOST_OMIT = "http://cp.sjzztz.com";    private static final String HOST_SHA_HAO = "http://211.147.5.46:9080/";    private static final String HOST_SHA_HAO_SSL = "https://211.147.5.46:8443/";    // 新测试接口地    private static final InternalHandler sInternalHandler = new InternalHandler();    public TaskConfig mTaskConfig;    Context mctx;    private Request mHttpHandler;    private DefaultTask mTask;    private RequestListener mRequestListener = new RequestListener() {        // all the request listener will be execute in another thread,        // so will use the handler.        @Override        public void readProgress(Object context, int bytes, int total) {            // notify the UI            sInternalHandler.obtainMessage(                    EVENT_ON_PROGRESS,                    new DefaultTaskResult<RWProgress>(mTask, new RWProgress(                            true, total, bytes))).sendToTarget();        }        @Override        public void writeProgress(Object context, int bytes, int total) {            // notify the UI            sInternalHandler.obtainMessage(                    EVENT_ON_PROGRESS,                    new DefaultTaskResult<RWProgress>(mTask, new RWProgress(                            false, total, bytes))).sendToTarget();        }        @Override        public void done(Object context, Response result) throws Exception {            try {                Exception ex = result.getException();                if (ex == null) {                    if (result.getCode() != HttpURLConnection.HTTP_OK) {                        if (result != null && result.getResult() != null) {                            // defaultError.unSerializeError(result.getResult());                            DefaultError error = new DefaultError();                            error.isException = false;                            sInternalHandler.obtainMessage(                                    EVENT_ON_ERROR,                                    new DefaultTaskResult<DefaultError>(mTask,                                            error)).sendToTarget();                        }                    } else {                        IProtocol response = (IProtocol) context;                        if (result != null) {                            response.unSerialize(result.getResult());                            sInternalHandler.obtainMessage(                                    EVENT_ON_DONE,                                    new DefaultTaskResult<IProtocol>(mTask,                                            response)).sendToTarget();                        }                    }                } else {                    DefaultError exception = new DefaultError();                    exception.isException = true;                    exception.error = result.getContent();// result.getContent();                    sInternalHandler.obtainMessage(                            EVENT_ON_ERROR,                            new DefaultTaskResult<DefaultError>(mTask,                                    exception)).sendToTarget();                    ex.printStackTrace();                    // retry();                }            } catch (Exception e) {                e.printStackTrace();                DefaultError error = new DefaultError();                error.isException = false;                sInternalHandler.obtainMessage(EVENT_ON_ERROR,                        new DefaultTaskResult<DefaultError>(mTask, error))                        .sendToTarget();            } finally {                sInternalHandler.obtainMessage(EVENT_ON_FINISH,                        new DefaultTaskResult<String>(mTask, (String) null))                        .sendToTarget();            }        }    };    public DefaultTask() {        super();        mTaskConfig = new TaskConfig();        mTask = this;    }    ;    // notify the UI    public void preExecute() {        if (mctx instanceof Activity) {           /* if (mctx instanceof NewRegisterActivity) {                return;            }*/            if (!Utils.isNetWorking(mctx)) {                // PromptUtil.showToast((Activity)mctx,                // R.string.network_no_setting);            }        }    }    // notify the UI    public void onProgress(RWProgress obj) {    }    ;    // notify the UI    public void onError(DefaultError obj) {    }    ;    // notify the UI    // public void onException(String ojb){    //    // }    // process the reuslt in the UI    public void onOk(IProtocol protocol) {    }    // notify the UI    public void postExecute() {        //mRequestListener = null;    }    public void cancel() {        //Thread.currentThread().interrupt();        if (mHttpHandler != null)            mHttpHandler.cancel();    }    public final DefaultTask execute(Context ctx, String uri, Bundle bundle) {        // mNetStateChecker=new NetStatChecher(ctx);        mctx = ctx;        // preExecute();        String url = (mTaskConfig.getHost()) + uri;        Tools.i("URL:" + url);        Arg[] httpArgs = new Arg[5];        httpArgs[0] = new Arg(Arg.CONNECTION,                mTaskConfig.isKeepalive ? "Keep-Alive" : "Close");        httpArgs[1] = new Arg(Arg.CHARSET, "UTF-8");        httpArgs[2] = new Arg(Arg.USER_AGENT, System.getProperties()                .getProperty("http.agent") + " AndroidSDK");        httpArgs[3] = new Arg("Accept", "*/*");        httpArgs[4] = new Arg("cache",                BooleanUtil.toString(mTaskConfig.isCache));        // httpArgs[5]=new Arg(Arg.CONTENT_TYPE,"application/json");        if (mTaskConfig.isGet) {            mHttpHandler = Request.get(ctx, url, JSONUtil.bundle2Args(bundle),                    httpArgs, mRequestListener, null);        } else {            // postData not null the serialize            // if(upload!=null){            //            // httpHandler= Request.post(ctx, url, null, httpArgs,            // defaultListener, upload.getData(), resp);            // }            // else            {                mHttpHandler = Request.post(ctx, url,                        JSONUtil.bundle2Args(bundle), httpArgs,                        mRequestListener, null, null);            }        }        return this;    }    public final DefaultTask execute(Context ctx, IProtocol protocol) {        // preExecute();        //        // String url = (mTaskConfig.getHost()) + protocol.uri();        // int argsCount = mTaskConfig.isJson ? 6 : 5;        // Arg[] httpArgs = new Arg[argsCount];        //        // httpArgs[0] = new Arg(Arg.CONNECTION, mTaskConfig.isKeepalive ?        // "Keep-Alive" : "Close");        // httpArgs[1] = new Arg(Arg.CHARSET, "UTF-8");        // httpArgs[2] = new Arg(Arg.USER_AGENT,        // System.getProperties().getProperty("http.agent") + " AndroidSDK");        // httpArgs[3] = new Arg("Accept", "*/*");        // httpArgs[4] = new Arg("cache",        // BooleanUtil.toString(mTaskConfig.isCache));        // if (mTaskConfig.isJson)        // httpArgs[5] = new Arg(Arg.CONTENT_TYPE, Result.JSON_CONTENT_TYPE);        //        // if (mTaskConfig.isGet) {        // mHttpHandler = Request.get(ctx, url, protocol.serialize(), httpArgs,        // mRequestListener, protocol);        // } else {        // // postData not null the serialize        //        // // {        // mHttpHandler = Request.post(ctx, url, protocol.serialize(), httpArgs,        // mRequestListener, null, protocol);        // // }        //        // }        return execute(ctx, protocol, Request.DEFAULT_CONNECT_TIME_OUT);    }    public final DefaultTask execute(Context ctx, IProtocol protocol,                                     int connectTimeout) {        connectTimeout = this.connectTimeout;        mctx = ctx;        preExecute();        String url = (mTaskConfig.getHost()) + protocol.uri();        int argsCount = mTaskConfig.isJson ? 6 : 5;        Arg[] httpArgs = new Arg[argsCount];        httpArgs[0] = new Arg(Arg.CONNECTION,                mTaskConfig.isKeepalive ? "Keep-Alive" : "Close");        httpArgs[1] = new Arg(Arg.CHARSET, "UTF-8");        httpArgs[2] = new Arg(Arg.USER_AGENT, System.getProperties()                .getProperty("http.agent") + " AndroidSDK");        httpArgs[3] = new Arg("Accept", "*/*");        httpArgs[4] = new Arg("cache",                BooleanUtil.toString(mTaskConfig.isCache));        if (mTaskConfig.isJson)            httpArgs[5] = new Arg(Arg.CONTENT_TYPE, Result.JSON_CONTENT_TYPE);        if (mTaskConfig.isGet) {            mHttpHandler = Request.get(ctx, url, protocol.serialize(),                    httpArgs, mRequestListener, protocol, connectTimeout);        } else {            mHttpHandler = Request.post(ctx, url, protocol.serialize(),                    httpArgs, mRequestListener, null, protocol, connectTimeout);        }        return this;    }    public static enum HostEnum {        HostNormal, HostInfo, HostShaHao, HostOmit    }    public static class DefaultError {        public int code;        public String error;        public boolean isException;    }    public static class TaskConfig {        public boolean isGet;        public boolean isCache;        public boolean isKeepalive;        public boolean isJson;        public HostEnum hostType;        // host        private String host;        // public boolean isSsl;        // public boolean isInfoHost;        // public boolean isShaHao;        public TaskConfig() {            super();            // host=HOST;            hostType = HostEnum.HostNormal;            isGet = false;            // isSsl=false;            isCache = false;            isKeepalive = false;            isJson = false;        }        public String getHost() {            if (hostType == HostEnum.HostNormal) {                host = HOST;                // host = HOST_SSL;                // } else if (hostType == HostEnum.HostInfo) {                // host = HOST_INFO;                // host = HOST_INFO_SSL;            } else if (hostType == HostEnum.HostShaHao) {                host = HOST_SHA_HAO;                host = HOST_SHA_HAO_SSL;            } else if (hostType == HostEnum.HostOmit) {                host = HOST_OMIT;            }            return host;        }    }    static class InternalHandler extends Handler {        @Override        public void handleMessage(Message msg) {            DefaultTaskResult result = (DefaultTaskResult) msg.obj;            switch (msg.what) {                case EVENT_ON_PROGRESS:                    // result.mTask.on                {                    RWProgress rwProgress = (RWProgress) result.mData;                    result.mTask.onProgress(rwProgress);                }                break;                case EVENT_ON_ERROR: {                    result.mTask.onError((DefaultError) result.mData);                }                break;                case EVENT_ON_FINISH: {                    result.mTask.postExecute();                }                break;                case EVENT_ON_DONE: {                    // IProtocol protocol=(IProtocol)result.mData[0];                    try {                        result.mTask.onOk((IProtocol) result.mData);                    } catch (Exception e) {                        e.printStackTrace();                    }                }                break;                default:                    try {                        super.handleMessage(msg);                    } catch (Exception e) {                        e.printStackTrace();                    }                    break;            }        }    }    static class DefaultTaskResult<Data> {        public DefaultTask mTask;        public Data mData;        public DefaultTaskResult(DefaultTask mTask, Data mData) {            super();            this.mTask = mTask;            this.mData = mData;        }    }    protected class RWProgress {        boolean isRead;        int total;        int bytes;        public RWProgress(boolean isRead, int total, int bytes) {            super();            this.isRead = isRead;            this.total = total;            this.bytes = bytes;        }        public boolean isRead() {            return isRead;        }        public int getTotal() {            return total;        }        public int getBytes() {            return bytes;        }    }}