package com.yeying.aimi.protocol.request;/*
Copyright (c) 2007, Sun Microsystems, Inc.
 
All rights reserved.
 
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
 
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
 * Neither the name of Sun Microsystems, Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.yeying.aimi.protoco.path.Result;
import com.yeying.aimi.storage.Cache;
import com.yeying.aimi.storage.EditorStorager;
import com.yeying.aimi.storage.IFileCache;
import com.yeying.aimi.utils.BooleanUtil;
import com.yeying.aimi.utils.Tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * Bug Description:( Android java.net.UnknownHostException: Host is unresolved)
 * <p>
 * 10-21 01:27:28.142: WARN/googleanalytics(1068):
 * java.net.UnknownHostException: Host is unresolved:
 * www.google-analytics.com:80 10-21 01:27:28.142: WARN/googleanalytics(1068):
 * at java.net.Socket.connect(Socket.java:1038) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at
 * org.apache.http.conn.scheme.PlainSocketFactory.
 * connectSocket(PlainSocketFactory.java:119) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at
 * com.google.android.apps.analytics.PipelinedRequester
 * .maybeOpenConnection(Unknown Source) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at
 * com.google.android.apps.analytics.PipelinedRequester.addRequest(Unknown
 * Source) 10-21 01:27:28.142: WARN/googleanalytics(1068): at
 * com.google.android.
 * apps.analytics.NetworkDispatcher$DispatcherThread$AsyncDispatchTask
 * .dispatchSomePendingEvents(Unknown Source) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at com.google.android.apps.analytics.
 * NetworkDispatcher$DispatcherThread$AsyncDispatchTask.run(Unknown Source)
 * 10-21 01:27:28.142: WARN/googleanalytics(1068): at
 * android.os.Handler.handleCallback(Handler.java:587) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at
 * android.os.Handler.dispatchMessage(Handler.java:92) 10-21 01:27:28.142:
 * WARN/googleanalytics(1068): at android.os.Looper.loop(Looper.java:123) 10-21
 * 01:27:28.142: WARN/googleanalytics(1068): at
 * android.os.HandlerThread.run(HandlerThread.java:60)
 * <p>
 * BugFixed By the following solution: I have seen this on a phone as well
 * (while doing USB debugging). The solution was to disable WiFi, and re-enable
 * it.
 *
 * @author sparrow
 */
public final class Request implements Runnable {
    // public static final String HOST="https://localhost:8080/";
//	public static final String HOST = "https://api.kaixin.com/";
//	public static final String APP_KEY = "51522026731817068700eb1b1df40ec3";
//	public static final String APP_SECRET = "22b78a513ffec9bc6b2e439ec779d24e";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String RAW = "RAW";
    public static final String JSON = "json";
    public static final String PATHPRAM = "s";

    public static final String transcode = "transcode";
    public static final String sessionId = "sessionId";
    public static final String content = "content";
    // Special URL for demo purposes
    public static final String CACHE_URL = "cache://";

    public static final String UTF8_CHARSET = "utf-8";

    private static final boolean DEBUG = true;

    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_TRY_TIMES = 5;
    private static final String TAG = Request.class.getSimpleName();
    public static int DEFAULT_CONNECT_TIME_OUT = 20000;
    private Context mContext;
    private Object context = null;
    private String url = null;
    private String method = null;
    private Arg[] httpArgs = null;
    private Arg[] inputArgs = null;
    private PostData multiPart = null;
    private RequestListener listener = null;
    private Thread thread = null;
    private volatile boolean interrupted = false;
    private StringBuffer logBuffer;
    private int totalToSend = 0;
    private int totalToReceive = 0;
    private int sent = 0;
    private int received = 0;
    private int connectTimeout = DEFAULT_CONNECT_TIME_OUT;
    private X509TrustManager xtm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
    private HostnameVerifier hnv = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    private Request(Context ctx) {

        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "100");
        System.setProperty("java.protocol.handler.pkgs",
                "org.apache.harmony.luni.internal.net.www.protocol");
        mContext = ctx;
        SSLContext sslContext = null;

        try {
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager[] xtmArray = new X509TrustManager[]{xtm};
            sslContext.init(null, xtmArray, new java.security.SecureRandom());
        } catch (GeneralSecurityException gse) {
        }
        if (sslContext != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
                    .getSocketFactory());
        }

        HttpsURLConnection.setDefaultHostnameVerifier(hnv);
        // logBuffer=new StringBuffer();
    }

    public static Response get(final Context ctx, final String url,
                               final Arg[] inputArgs, final Arg[] httpArgs,
                               final RequestListener listener) throws IOException {

        return sync(ctx, GET, url, inputArgs, httpArgs, listener, null);
    }

    public static Response post(final Context ctx, final String url,
                                final Arg[] inputArgs, final Arg[] httpArgs,
                                final RequestListener listener, final PostData multiPart)
            throws IOException {

        return sync(ctx, POST, url, inputArgs, httpArgs, listener, multiPart);
    }

    private static Response sync(final Context ctx, final String method,
                                 final String url, final Arg[] inputArgs, final Arg[] httpArgs,
                                 final RequestListener listener, final PostData multiPart)
            throws IOException {

        final Request request = new Request(ctx);
        request.method = method;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;
        request.listener = listener;

        final Response response = new Response();
        request.doHTTP(response);
        return response;
    }

    public static Request get(final Context ctx, final String url,
                              final Arg[] inputArgs, final Arg[] httpArgs,
                              final RequestListener listener, final Object context) {

        return get(ctx, url, inputArgs, httpArgs, listener, context, DEFAULT_CONNECT_TIME_OUT);
    }

    public static Request get(final Context ctx, final String url,
                              final Arg[] inputArgs, final Arg[] httpArgs,
                              final RequestListener listener, final Object context, int connectTimeOut) {
        return async(ctx, GET, url, inputArgs, httpArgs, listener, null,
                context, connectTimeOut);
    }

    public static Request post(final Context ctx, final String url,
                               final Arg[] inputArgs, final Arg[] httpArgs,
                               final RequestListener listener, final PostData multiPart,
                               final Object context) {

        return post(ctx, url, inputArgs, httpArgs, listener, multiPart, context, DEFAULT_CONNECT_TIME_OUT);
    }

    public static Request post(final Context ctx, final String url,
                               final Arg[] inputArgs, final Arg[] httpArgs,
                               final RequestListener listener, final PostData multiPart,
                               final Object context, int connectTimeOut) {
        return async(ctx, POST, url, inputArgs, httpArgs, listener, multiPart,
                context, connectTimeOut);
    }

    private static Request async(final Context ctx, final String method,
                                 final String url, final Arg[] inputArgs, final Arg[] httpArgs,
                                 final RequestListener listener, final PostData multiPart,
                                 final Object context, int connectTimeOut) {
        final Request request = new Request(ctx);
        request.method = method;

        request.context = context;
        request.listener = listener;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;
        request.connectTimeout = connectTimeOut;
        // TODO: implement more sophisticated pooling, queuing and scheduling
        // strategies
        // Must to be done!
        RequestService.getInstance().addTask(request);
        /*
		 * request.thread = new Thread(request); request.thread.start();
		 */
        return request;
    }

    private static String unquote(final String str) {
        if (str.startsWith("\"") && str.endsWith("\"") || str.startsWith("'")
                && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    // TODO: verify correctness
    private static String encode(final String str)
            throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        final byte[] buf = str.getBytes("utf-8");

        final StringBuffer sbuf = new StringBuffer(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            final byte ch = buf[i];
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')
                    || (ch >= '0' && ch <= '9')
                    || (ch == '-' || ch == '_' || ch == '.' || ch == '~')) {
                sbuf.append((char) ch);
            } else if (ch == ' ') {
                sbuf.append('+');
            } else {
                sbuf.append('%');
                sbuf.append(Integer.toHexString(ch & 0xff));
            }
        }
        return sbuf.toString();
    }

    public static void main(String[] args) {
        HttpURLConnection conn = null;
        URLConnection urlConnection = null;
        OutputStream os = null;
        InputStream is = null;
        // HttpConnection conn = null;
        try {

            URL url = new URL("http://www.agan365.com/src_agan_dist/img/agan2015/index/20150625/rice.jpg");
            urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(20000);
            urlConnection.setReadTimeout(20000);
            // urlConnection.setr
            conn = (HttpURLConnection) urlConnection;
            conn.setAllowUserInteraction(false);

            conn.setDoOutput(true);
            conn.setDoInput(true);
            // conn.setDoOutput(true);
            conn.setUseCaches(false);
            // 1M
            // conn.setChunkedStreamingMode(1024*1024);
            // HttpURLConnection.setFollowRedirects(true);
            // set the params
            conn.setRequestMethod("GET");
            // KXLog.d(TAG, "Http.Method :%s", method);


            // boolean Tools.enableLog;
            // log the


            // connect

            conn.connect();


//			copyResponseHeaders(conn, response);

            int responseCode = conn.getResponseCode();

            System.out.println(responseCode);

        } catch (Exception ex) {
            ex.printStackTrace();

//			toast(R.string.net_error);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public void cancel() {
        interrupted = true;

        // TODO: maybe wait a little to give the thread an opportunity to return
        // cleanly
        // #if CLDC!="1.0"
        if (thread != null) {
            thread.interrupt();
        }
        // #endif
    }

    private void toast(int resid) {
        Toast.makeText(mContext, resid, Toast.LENGTH_SHORT).show();
    }

    public void run() {
        final Response response = new Response();

        try {
            if (url.equals(CACHE_URL)) {
                doCache(response);
            } else {
                doHTTP(response);
//				 if(multiPart==null)
//				{
//					// when contains the multipart don't resend
//					int tryTimes = 0;
//					while (response.getCode() == -1) {
//						// retry
//						// TKLog.d(TAG, "How that happen?retry. :%d",tryTimes);
//						tryTimes++;
//						if (tryTimes > MAX_TRY_TIMES)
//							break;
//						Tools.e("Request.java : 执行doHTTP方法------>>>>> 2");
//						doHTTP(response);
//					}
//				}
            }
        } catch (Exception ex) {
            response.ex = ex;// Kuix.getMessage("network_error");//ex;
            ex.printStackTrace();
            Tools.i("Request.doHttp exception:" + ex.getMessage());
//			toast(R.string.net_error);

        } finally {

            if (listener != null) {
                try {
                    listener.done(context, response);
                } catch (Throwable th) {
                    // Kuix.alert(th);
                    if (DEBUG) {
                        // System.err.println("Uncaught throwable in listener: ");
                        th.printStackTrace();
                    }
                }
            }
        }
    }

    // data may be large, send in chunks while reporting progress and checking
    // for interruption
    private void write(final OutputStream os, final byte[] data)
            throws IOException {

        if (interrupted) {
            return;
        }

        // optimization if a small amount of data is being sent
        if (data.length <= BUFFER_SIZE) {
            os.write(data);
            os.flush();
            // Tools.hex(data, 0, data.length);
            // Log.d(this.getClass().getName(),new String(data,"utf-8"));
            sent += data.length;
            if (listener != null) {
                try {
                    listener.writeProgress(context, sent, totalToSend);
                } catch (Throwable th) {
                    if (DEBUG) {
                        System.err.println("Uncaught throwable in listener: ");
                        th.printStackTrace();
                    }
                }
            }
        } else {
            int offset = 0;
            int length = 0;
            do {
                length = Math.min(BUFFER_SIZE, data.length - offset);
                if (length > 0) {
                    os.write(data, offset, length);
                    os.flush();
                    // Tools.hex(data, offset, length);
                    // Log.d(this.getClass().getName(),new
                    // String(data,offset,length,"utf-8"));
                    offset += length;
                    sent += length;
                    if (listener != null) {
                        try {
                            listener.writeProgress(context, sent, totalToSend);
                        } catch (Throwable th) {
                            if (DEBUG) {
                                System.err
                                        .println("Uncaught throwable in listener: ");
                                th.printStackTrace();
                            }
                        }
                    }
                }
            } while (!interrupted && length > 0);
        }
    }

    public URLConnection getConnection(URL url) throws IOException {
        // boolean

        boolean custom = false;
        // custom=multiPart!=null;
        {
            URLConnection httpsURLConn = null;
            ConnectivityManager connMgr = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = connMgr.getActiveNetworkInfo();


            // @ 关闭代理方式
            httpsURLConn = url.openConnection();

//			if (null != netInfo && ConnectivityManager.TYPE_WIFI == netInfo.getType()) {
//				httpsURLConn = url.openConnection();
//			} else {
//				String proxyHost = android.net.Proxy.getDefaultHost();
//				if (null == proxyHost) {
//					Log.e("Request: ", "null == proxyHost  ----> line 427");
//					httpsURLConn = url.openConnection();
//				} else {
//					Log.e("Request: ", "null != proxyHost  ----> line 430");
//					java.net.Proxy p = new java.net.Proxy(
//							java.net.Proxy.Type.HTTP, new InetSocketAddress(
//									android.net.Proxy.getDefaultHost(),
//									android.net.Proxy.getDefaultPort()));
//					httpsURLConn = url.openConnection(p);
//				}
//			}


            return httpsURLConn;
        }
		/**/
    }

    private void doHTTP(final Response response) throws IOException {
        // logBuffer.delete(0, logBuffer.length());
        final StringBuffer args = new StringBuffer();
        boolean isCache = false;
        if (httpArgs != null) {
            if (httpArgs.length > 0) {
                for (int i = 0; i < httpArgs.length; i++) {
                    if ("cache".equals(httpArgs[i].getKey())) {
                        isCache = BooleanUtil.parseBoolean(httpArgs[i]
                                .getValue());
                    }

                }
            }
        }
        if (inputArgs != null) {
            if (inputArgs.length > 0) {
                // when method is POST

                for (int i = 0; i < inputArgs.length; i++) {
                    if (inputArgs[i] != null) {

                        if (inputArgs[i].getKey().equals(PATHPRAM)) {
                            url = (new StringBuffer().append(url).append("?").append(inputArgs[i].getKey()).append("=").append(inputArgs[i].getValue())).toString();
                            continue;
                        }
                        if (inputArgs[i].getKey().equals(RAW)) {
                            args.append(inputArgs[i].getValue());
                        } else {
                            if (inputArgs[i].isEncode()) {
                                args.append(URLEncoder.encode(
                                        inputArgs[i].getKey(), "utf-8"));
                                args.append(("="));
                                if (inputArgs[i].getValue() != null)
                                    args.append(URLEncoder.encode(
                                            inputArgs[i].getValue(), "utf-8"));
                            } else {
                                args.append((inputArgs[i].getKey()));
                                args.append(("="));
                                if (inputArgs[i].getValue() != null)
                                    args.append((inputArgs[i].getValue()));
                            }
                            if (i + 1 < inputArgs.length
                                    && inputArgs[i + 1] != null) {
                                args.append("&");
                            }
                        }
                    }
                }

            }
        }

        final StringBuffer location = new StringBuffer(url);
        final String params = /* URLEncoder.encode */(args.toString());
        if (GET.equals(method) && args.length() > 0) {
            location.append("?");
            location.append(params);
        }

        final String requestQuery = location.toString();
        log(requestQuery);
		/*
		 * log(requestQuery); if (POST.equals(method)) { log(params); }
		 */

        /**
         *
         * location.toString as key.
         *
         */
        boolean shouldInCache = false;
        if (isCache) {
            String key = Tools.genMd5(requestQuery);
            Cache cache = new Cache(key);
            EditorStorager.getInstance(mContext).loadStorage(cache);
            if (cache.isValid()) {
                shouldInCache = true;
                log(cache.getIdentifer() + "@cache " + cache.content);
                inputArgs = new Arg[1];
                inputArgs[0] = new Arg("cache", cache.content);
            }
        }

        if (shouldInCache) {
            doCache(response);
        } else {
            HttpURLConnection conn = null;
            URLConnection urlConnection = null;
            OutputStream os = null;
            InputStream is = null;
            // HttpConnection conn = null;
            try {
                URL url = new URL(requestQuery);
                urlConnection = getConnection(url);
                urlConnection.setConnectTimeout(connectTimeout);
                urlConnection.setReadTimeout(20000);
                // urlConnection.setr
                conn = (HttpURLConnection) urlConnection;
                conn.setAllowUserInteraction(false);
                if (POST.equals(method)) {
                    totalToSend = 0;
                    totalToReceive = 0;
                    sent = 0;
                    received = 0;

                }
                if (POST.equals(method)) {
                    conn.setDoOutput(true);
                }
                conn.setDoInput(true);
                // conn.setDoOutput(true);
                conn.setUseCaches(false);
                // 1M
                // conn.setChunkedStreamingMode(1024*1024);
                // HttpURLConnection.setFollowRedirects(true);
                // set the params
                conn.setRequestMethod(method);
                // KXLog.d(TAG, "Http.Method :%s", method);

                if (httpArgs != null) {
                    for (int i = 0; i < httpArgs.length; i++) {
                        if (httpArgs[i] != null) {
                            final String value = httpArgs[i].getValue();
                            if (value != null) {
                                // conn.getre
                                conn.setRequestProperty(httpArgs[i].getKey(),
                                        value);
                                // Tools.i(" "+httpArgs[i].getKey()+" :"+value);
                                // KXLog.d(TAG, "%s: %s",
                                // httpArgs[i].getKey(),value);
                            }
                        }
                    }
                }
                if (POST.equals(method)) {
                    if (multiPart != null) {
                        // conn.setFixedLengthStreamingMode(1000*1024);
                        long sizeToSend = multiPart.getContentLength();
                        // conn.setRequestProperty(Arg.CONTENT_LENGTH,
                        // String.valueOf(sizeToSend));

                        conn.setRequestProperty("Transfer-Encoding", "chunked");
                        // conn.setChunkedStreamingMode(1000*1024);
                        // conn.setre
                        conn.setRequestProperty(
                                Arg.CONTENT_TYPE,
                                "multipart/form-data;boundary="
                                        + multiPart.getBoundary());
                    } else if (inputArgs != null) {
                        final byte[] argBytes = args.toString().getBytes();
                        // // totalToSend = argBytes.length;
                        long sizeToSend = argBytes.length;
                        conn.setRequestProperty(Arg.CONTENT_LENGTH,
                                String.valueOf(sizeToSend));
                        //
                    }
                }

                // boolean Tools.enableLog;
                // log the

                if (interrupted) {
                    return;
                }
                // connect
                conn.connect();
                if (POST.equals(method)) {

                    try {

                        os = conn.getOutputStream();
                        writePostData(params, os);
                    } catch (Exception ex) {

                        ex.printStackTrace();
                    } finally {

                        if (os != null) {
                            try {
                                os.flush();
                                os.close();
                            } catch (IOException ignore) {
                            }
                        }

                    }
                }

                if (interrupted) {
                    return;
                }

                copyResponseHeaders(conn, response);

                response.responseCode = conn.getResponseCode();

                if (interrupted) {
                    return;
                }

                processContentType(conn, response);
                is = getInputStream(conn);

                readResponse(is, response, requestQuery, isCache);

            } catch (Exception ex) {
                ex.printStackTrace();
                response.ex = ex;
//				toast(R.string.net_error);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException ignore) {
                    }
                }

                if (conn != null) {
                    conn.disconnect();
                }
            }

        }

    }

    /**
     * Return cached data (use instead of doHTTP())
     **/
    private void doCache(final Response response) throws IOException {

        String content = null;
        if ((inputArgs != null) && (inputArgs.length > 0)
                && (inputArgs[0] != null) && (inputArgs[0].getValue() != null)) {
            content = inputArgs[0].getValue();
        } else {
            throw new IOException("Invalid demo args");
        }
        // Tools.i("cache:"+content);
        response.charset = UTF8_CHARSET;
        response.contentType = Result.TEXT_XML_CONTENT_TYPE;// Result.JSON_CONTENT_TYPE;//
        // "text/javascript";
        response.responseCode = HttpURLConnection.HTTP_OK;

        response.result = Result.fromContent(content, response.contentType);
    }

    private void writePostData(final String args, final OutputStream os)
            throws IOException {
        if (multiPart != null) {
            final byte[] multipartBoundaryBits = multiPart.getBoundary()
                    .getBytes();
            final byte[] newline = "\r\n".getBytes();
            final byte[] dashdash = "--".getBytes();
            totalToSend = 0;
            // estimate totalBytesToSend
            final Part[] parts = multiPart.getParts();
            for (int i = 0; i < parts.length; i++) {
                final Arg[] headers = parts[i].getHeaders();
                for (int j = 0; j < headers.length; j++) {
                    totalToSend += headers[j].getKey().getBytes().length;
                    totalToSend += headers[j].getValue().getBytes().length;
                    totalToSend += multipartBoundaryBits.length
                            + dashdash.length + 3 * newline.length;
                }
                totalToSend += parts[i].getDataSize();
            }
            // closing boundary marker
            totalToSend += multipartBoundaryBits.length + 2 * dashdash.length
                    + 2 * newline.length;

            for (int i = 0; i < parts.length && !interrupted; i++) {
                write(os, newline);
                write(os, dashdash);
                write(os, multipartBoundaryBits);
                write(os, newline);

                boolean wroteAtleastOneHeader = false;
                final Arg[] headers = parts[i].getHeaders();
                for (int j = 0; j < headers.length; j++) {
                    Log.d(this.getClass().getName(), (headers[j].getKey()
                            + ": " + headers[j].getValue()));
                    write(os, (headers[j].getKey() + ": " + headers[j]
                            .getValue()).getBytes());
                    write(os, newline);
                    wroteAtleastOneHeader = true;
                }
                if (wroteAtleastOneHeader) {
                    write(os, newline);
                }
                if (parts[i].isBytes())
                    write(os, parts[i].getData());
                else {

                    byte[] data = new byte[BUFFER_SIZE];
                    int nReadLength = 0;
                    InputStream is = parts[i].getStream();
                    while ((nReadLength = is.read(data)) != -1) {
                        write(os, data);
                        // os.write(data, 0, nReadLength);
                    }
                    is.close();

                }
            }

            // closing boundary marker
            write(os, newline);
            write(os, dashdash);
            write(os, multipartBoundaryBits);
            write(os, dashdash);
            write(os, newline);
        } else if (inputArgs != null) {
            final byte[] argBytes = args.toString().getBytes();
            totalToSend = argBytes.length;
            write(os, argBytes);
        } else {
            throw new IOException(
                    "No data to POST - either input args or multipart must be non-null");
        }
    }

    private void readIntoCache(final InputStream is, String filename)
            throws IOException {
        final byte[] cbuf = new byte[BUFFER_SIZE];
        IFileCache cache = null;

        try {
            cache = (IFileCache) context;// new
            // ImageCache(mContext,filename,false);
            cache.open();

            int nread = 0;
            while ((nread = is.read(cbuf)) > 0 && !interrupted) {

                cache.write(cbuf, 0, nread);

                received += nread;
                if (listener != null) {
                    try {
                        listener.readProgress(context, received, totalToReceive);
                    } catch (Throwable th) {
                        if (DEBUG) {
                            System.err
                                    .println("Uncaught throwable in listener: ");
                            th.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Request.readIntoCache ", e.getMessage());
//			e.printStackTrace();
        } finally {

            if (cache != null) {
                cache.close();
            }

        }

    }

    private InputStream getInputStream(final HttpURLConnection conn)
            throws IOException {
        InputStream is = null;
        int code = conn.getResponseCode();
        if (code >= 400) {

            String message = conn.getResponseMessage();
            // TKLog.d(TAG, "getInputStream(code=%d,%s)", code,message);
            is = conn.getErrorStream();

        } else {
            is = conn.getInputStream();
        }

        return is;

    }

    private String readStream(final InputStream is) {

        final byte[] cbuf = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bos = null;

        try {

            // Bug fixed ,when the server turns error code as 4xx or 5xx
            // java.io.FileNoutFoundException.
            //

            bos = new ByteArrayOutputStream();
            int nread = 0;
            if (is != null) {
                while ((nread = is.read(cbuf)) > 0 && !interrupted) {

                    bos.write(cbuf, 0, nread);
//					// Tools.hex(cbuf, 0, nread);
//					received += nread;
//					if (listener != null) {
//						try {
//							listener.readProgress(context, received, totalToReceive);
//						} catch (Throwable th) {
//							if (DEBUG) {
//								System.err.println("Uncaught throwable in listener: ");
//								th.printStackTrace();
//							}
//						}
//					}
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (interrupted) {
            return "";
        }

        /**
         * Purpose: Fixed the charset problem Author: sparrow Date: 2011-03-21
         * Before Modification:
         *
         * // final String content = bos.toString().trim();
         *
         *
         * Assign Name: Fixed Charset
         *
         */

        // //#if Assign Name is [Fixed Charset]
        byte[] bytes = bos.toByteArray();
        String content = null;
        try {
            content = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }

    private void readResponse(final InputStream is, final Response response,
                              final String cacheKey, boolean clearCache) throws IOException {
        log("content-type:" + response.contentType);
        // application/octet-stream/video/mp4
        if (response.contentType.startsWith("image")
                // application/octet-stream
                || response.contentType.startsWith("application/octet-stream")
                || response.contentType.contains("x-zip-compressed")
                || response.contentType.contains("video")
                || response.contentType.contains("JPEG")
                || response.contentType.contains("jpeg")
                || response.contentType.contains("PNG")
                || response.contentType.contains("png")
                || response.contentType.contains("jpg") || response.contentType.contains("JPG") || response.contentType.contains("png") || response.contentType.contains("PNG")
                ) {
            if (response.getCode() == HttpURLConnection.HTTP_OK
                    || response.getCode() == HttpURLConnection.HTTP_PARTIAL) {
                // KXLog.d(TAG, "Reading files....");
                Tools.i("Reading files");
                readIntoCache(is, cacheKey);

            } else {
                final String content = readStream(is);
                // logBuffer.append("reading file error:").append(content);
                // KXLog.d(TAG, "Reading  files error:%s",content);
                response.resp = content;
                log("Response:\n" + response.resp);
            }

        } else {

            final String content = readStream(is);

            if (response.getCode() == HttpURLConnection.HTTP_OK && clearCache) {
                String key = Tools.genMd5(cacheKey);
                Cache cache = new Cache(key, content);
                // log("Update Cache:"+cache.getIdentifer());
                EditorStorager.getInstance(mContext).updateStorage(cache);
                // KXLog.d(TAG, "update %s@cache(%s)", key,content);

            }
            response.resp = content;
            log("Response:\n" + response.resp);
            if (content != null && !content.equals("")) {
                // response.result.resp
                response.result = Result.fromContent(content,
                        response.contentType);

            }
        }

    }

    public void log(String msg) {
        // KXLog.d(TAG,msg);
        Tools.i(msg);
    }

    private void copyResponseHeaders(final HttpURLConnection conn,
                                     final Response response) throws IOException {

        // pass 1 - count the number of headers
        int headerCount = 0;
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                headerCount++;
            }
        }
        Map<String, List<String>> headers = conn.getHeaderFields();

        // for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        // log(entry.getKey() + " =" + entry.getValue());
        // // //
        // logBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        // }
        response.headers = new Arg[headerCount];

        // pass 2 - now copy the headers
        // for (int i = 0; i < Short.MAX_VALUE; i++) {
        // final String key = conn.getHeaderFieldKey(i);
        // final String val = conn.getHeaderField(i);
        // if (key == null || val == null) {
        // break;
        // } else {
        // response.headers[i] = new Arg(key, val);
        // if(Tools.enableLog){
        // //KXLog.d(TAG, "%s-%s", key,val);
        // logBuffer.append(key).append("=").append(val).append("\n");
        // }
        // }
        // }
        totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
        // Log.i("ViaStore","copyResponseHeaders Total To Received getHeaderFieldInt CONTENT_LENGTH:"
        // + totalToReceive);
        // BugFixe by sparrow 2012-06-25
        if (totalToReceive == 0) {
            // checking the content-range
            String contentRange = conn.getHeaderField(Arg.CONTENT_RANGE);
            // content-range =[bytes 0-4709061/4709062]
            // Log.i("ViaStore","copyResponseHeaders contentRange getHeaderField CONTENT_RANGE:"
            // + contentRange);
            // bytes 0-2914528/2914529
            if (contentRange != null) {
                int pos = contentRange.indexOf('/');
                // Log.i("ViaStore","indexOf('/'):" + pos);
                if (pos > 0) {

                    String rangeTotal = contentRange.substring(pos + 1).trim();
                    // Log.i("ViaStore","copyResponseHeaders rangeTotal:" +
                    // rangeTotal);
                    if (rangeTotal != null) {

                        totalToReceive = Integer.parseInt(rangeTotal);
                    }

                }
            }
        }
        // Tools.i("copyResponseHeaders Total To Received:"+totalToReceive);
        // String server=conn.getHeaderField(Arg.SERVER);
        // if(Tools.enableLog){
        // //KXLog.d(TAG, "%s: %d", Arg.CONTENT_LENGTH,totalToReceive);
        // //logBuffer.append(Arg.CONTENT_LENGTH).append("=").append(totalToReceive).append("\n");
        // //logBuffer.append(Arg.SERVER).append("=").append(server).append("\n");
        // }
    }

    private void processContentType(final HttpURLConnection conn,
                                    final Response response) throws IOException {

        response.contentType = conn.getHeaderField(Arg.CONTENT_TYPE);

        if (response.contentType == null) {
            // assume UTF-8 and XML if not specified
//			response.contentType = Result.APPLICATION_XML_CONTENT_TYPE;
            response.contentType = "application/octet-stream";
            response.charset = UTF8_CHARSET;
            return;
        }
        final int semi = response.contentType.indexOf(';');
        if (semi >= 0) {
            response.charset = response.contentType.substring(semi + 1).trim();
            final int eq = response.charset.indexOf('=');
            if (eq < 0) {
                throw new IOException("Missing charset value: "
                        + response.charset);
            }
            response.charset = unquote(response.charset.substring(eq + 1)
                    .trim());
            response.contentType = response.contentType.substring(0, semi)
                    .trim();
        }

        /**
         *
         * When the UTF
         */
        if (response.charset != null
                && !UTF8_CHARSET.equals(response.charset.toLowerCase())) {
            throw new IOException("Unsupported charset: " + response.charset);
        }
    }
}
