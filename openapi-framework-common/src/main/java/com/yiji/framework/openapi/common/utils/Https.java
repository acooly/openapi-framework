package com.yiji.framework.openapi.common.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http 客户端工具
 *
 * @author zhangpu
 */
public class Https {
    private static Logger logger = LoggerFactory.getLogger(Https.class);
    /**
     * 默认连接超时时间 10s
     */
    private static final int CONNECTION_TIMEOUT = 10000;
    /**
     * 默认读超时时间 30s
     */
    private static final int SO_TIMEOUT = 30000;
    private static final String defaultCharset = "utf-8";
    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    private static final boolean ENABLE_URL_ENCODE = true;
    private static final boolean INCLUDE_BLANK = false;

    private static Https instance = new Https();
    private final HttpClient httpclient;

    private Https() {
        this(200, 50);
        ShutdownHooks.addShutdownHook(new Runnable() {
            public void run() {
                Https.instance.shutdown();
            }
        }, "HttpUtilShutdownHook");
    }

    private Https(int maxTotal, int maxPerRoute) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        Scheme httpsScheme = this.createHttpsScheme(HTTPS_PORT);
        schemeRegistry.register(httpsScheme);
        schemeRegistry.register(new Scheme("http", HTTP_PORT, PlainSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxPerRoute);
        BasicHttpParams params = new BasicHttpParams();
        params.setParameter("http.protocol.cookie-policy", "ignoreCookies");
        params.setParameter("http.connection.timeout", Integer.valueOf(CONNECTION_TIMEOUT));
        params.setParameter("http.socket.timeout", Integer.valueOf(SO_TIMEOUT));
        this.httpclient = new DefaultHttpClient(cm, params);
    }

    private Scheme createHttpsScheme(int port) {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }}, new SecureRandom());
        } catch (Exception var3) {
            throw Exceptions.unchecked(var3);
        }

        SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return new Scheme("https", port, sf);
    }

    private void shutdown() {
        if (this.httpclient != null) {
            this.httpclient.getConnectionManager().shutdown();
        }

    }

    public static Https getInstance() {
        return instance;
    }

    public String statusInfo() {
        StringBuilder sb = new StringBuilder();
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager) this.httpclient
                .getConnectionManager();
        sb.append("{");
        sb.append("pool.maxTotal:").append(connectionManager.getMaxTotal()).append(",");
        sb.append("pool.maxPerRoute:").append(connectionManager.getDefaultMaxPerRoute()).append(",");
        sb.append("pool.stats:").append(connectionManager.getTotalStats());
        sb.append("}");
        return sb.toString();

    }

    public Https connectTimeout(int connectTimeout) {
        HttpParams params = this.httpclient.getParams();
        params.setParameter("http.connection.timeout", Integer.valueOf(connectTimeout * 1000));
        return this;
    }

    public Https sessionKeeping(boolean keeping) {
        HttpParams params = this.httpclient.getParams();
        if (keeping) {
            params.setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        } else {
            params.setParameter("http.protocol.cookie-policy", "ignoreCookies");

        }
        return this;
    }

    public Https maxTotal(int maxTotal) {
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager) this.httpclient
                .getConnectionManager();
        connectionManager.setMaxTotal(maxTotal);
        return this;
    }

    public Https maxPerRoute(int maxPerRoute) {
        PoolingClientConnectionManager connectionManager = (PoolingClientConnectionManager) this.httpclient
                .getConnectionManager();
        connectionManager.setDefaultMaxPerRoute(maxPerRoute);
        return this;
    }

    public Https readTimeout(int readTimeout) {
        HttpParams params = this.httpclient.getParams();
        params.setParameter("http.socket.timeout", Integer.valueOf(readTimeout * 1000));
        return this;
    }

    /**
     * POST
     *
     * @param url
     * @param dataMap
     * @param headerMap
     * @param enableRedirect
     * @param charset
     * @return
     */
    public HttpResult post(String url, Map<String, ?> dataMap, Map<String, ?> headerMap, boolean enableRedirect,
                           String charset) {
        url = normalizeUrl(url);
        charset = getCharset(charset);
        HttpPost post = buildHttpPost(url, dataMap, charset);
        return execute(url, post, headerMap, enableRedirect, charset);
    }

    public HttpResult post(String url, Map<String, ?> dataMap, String charset) {
        return post(url, dataMap, null, false, charset);
    }

    public HttpResult post(String url, Map<String, ?> dataMap) {
        return post(url, dataMap, null, false, null);
    }

    public HttpResult post(String url, String body, Map<String, ?> headerMap, boolean enableRedirect,
                           ContentType contentType) {
        url = normalizeUrl(url);
        contentType = loadContentType(contentType);
        HttpPost post = buildHttpPost(url, body, contentType);
        return execute(url, post, headerMap, enableRedirect, contentType.getCharset().name());
    }

    public HttpResult post(String url, String body, Map<String, ?> headerMap, boolean enableRedirect, String mimeType,
                           String charset) {
        return post(url, body, headerMap, enableRedirect, ContentType.create(mimeType, charset));
    }

    public HttpResult post(String url, String body, String mimeType, String charset) {
        return post(url, body, null, false, mimeType, charset);
    }

    public HttpResult post(String url, String body, ContentType contentType) {
        return post(url, body, null, false, contentType);
    }

    public HttpResult post(String url, String body) {
        return post(url, body, null, false, null);
    }

    private ContentType loadContentType(ContentType contentType) {
        if (contentType != null) {
            return contentType;
        }
        return ContentType.create(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), defaultCharset);
    }

    /**
     * Get 请求
     *
     * @param url
     * @param dataMap
     * @param headerMap
     * @param charset
     * @param enableUrlEncode
     * @param includeBlank
     * @return
     */
    public HttpResult get(String url, Map<String, String> dataMap, Map<String, String> headerMap, String charset,
                          boolean enableUrlEncode, boolean includeBlank) {
        url = this.normalizeUrl(url);
        charset = getCharset(charset);
        HttpGet get = buildHttpGet(url, dataMap, charset, enableUrlEncode, includeBlank);
        return this.execute(get.getURI().toString(), get, headerMap, false, charset);
    }

    public HttpResult get(String url, Map<String, String> dataMap, Map<String, String> headerMap, String charset) {
        return get(url, dataMap, headerMap, charset, ENABLE_URL_ENCODE, INCLUDE_BLANK);
    }

    public HttpResult get(String url, Map<String, String> dataMap) {
        return get(url, dataMap, null, defaultCharset, ENABLE_URL_ENCODE, INCLUDE_BLANK);
    }

    public HttpResult get(String url) {
        return get(url, null, null, defaultCharset, ENABLE_URL_ENCODE, INCLUDE_BLANK);
    }

    public HttpResult execute(String url, HttpRequestBase request, Map<String, ?> headerMap, boolean enableRedirect,
                              String charset) {
        HttpResult result = new HttpResult();
        charset = getCharset(charset);
        try {
            if (headerMap != null && !headerMap.isEmpty()) {
                for (Entry<String, ?> entry : headerMap.entrySet()) {
                    if (entry.getValue() == null) {
                        request.setHeader(entry.getKey(), "");
                    } else {
                        request.setHeader(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
            loggerRequest(request);
            HttpResponse e1 = this.httpclient.execute(request);
            loggerResponse(e1);

            int statusCode1 = e1.getStatusLine().getStatusCode();
            if (enableRedirect && this.isRedirect(statusCode1)) {
                Header locationHeader = e1.getFirstHeader("Location");
                if (locationHeader != null) {
                    String location = locationHeader.getValue();
                    if (location != null) {
                        logger.debug("redirect request:{}", location);
                        HttpResult redirectResult = this.get(location);
                        logger.debug("redirect result:{}", redirectResult);
                        return redirectResult;
                    }
                }
            }

            result.setBody(EntityUtils.toString(e1.getEntity(), charset));
            result.setHeaders(readHeaders(e1));
            result.setStatus(e1.getStatusLine().getStatusCode());
            logger.debug("http result: result: {}", result);
            return result;
        } catch (IOException ioe) {
            throw Exceptions.unchecked(ioe);
        } catch (Exception var17) {
            if (request != null) {
                request.abort();
            }
            throw var17;
        } finally {
            if (request != null) {
                request.releaseConnection();
            }

        }
    }

    private Map<String, String> readHeaders(HttpResponse httpResponse) {
        Map<String, String> result = Maps.newHashMap();
        List<String> headerValues = null;
        for (Header header : httpResponse.getAllHeaders()) {
            if (Strings.isNotBlank(header.getName())) {
                if (result.containsKey(header.getName())) {
                    headerValues = Lists.newArrayList(Strings.split(result.get(header.getName()), ","));
                    headerValues.add(header.getValue());
                    result.put(header.getName(), Strings.join(headerValues.iterator(), ","));
                } else {
                    result.put(header.getName(), header.getValue());
                }
            }
        }
        return result;
    }

    private boolean isRedirect(int statusCode) {
        return String.valueOf(statusCode).startsWith("3");
    }

    private String getCharset(String charset) {
        if (Strings.isBlank(charset)) {
            return defaultCharset;
        }
        return charset;
    }

    /**
     * 组装HttpPost请求对象
     *
     * @param url
     * @param dataMap
     * @param charset
     * @return
     */
    private HttpPost buildHttpPost(String url, Map<String, ?> dataMap, String charset) {
        HttpPost post = new HttpPost(url);
        StringBuilder sb = new StringBuilder();
        sb.append("请求url:").append(url);
        if (dataMap != null) {
            sb.append("\nparameters：[");
            ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Entry<String, ?> entry : dataMap.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
                if (entry.getValue() == null) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), null));
                } else {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
            }
            try {
                post.setEntity(new UrlEncodedFormEntity(nvps, charset));
            } catch (UnsupportedEncodingException e) {
                logger.error("请求{},编码错误", url, e);
            }
            sb.append("]");
        }
        logger.debug(sb.toString());
        return post;
    }

    private HttpPost buildHttpPost(String url, String body, ContentType contentType) {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(body, contentType));
        return post;
    }

    private HttpGet buildHttpGet(String url, Map<String, ?> dataMap, String charset, boolean enableUrlEncode,
                                 boolean includeBlank) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (dataMap != null && !dataMap.isEmpty()) {
            if (url.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
            String value = null;
            for (Entry<String, ?> entry : dataMap.entrySet()) {
                if (entry.getValue() != null && Strings.isBlank(entry.getValue().toString()) && includeBlank) {
                    sb.append(entry.getKey()).append("=");
                } else {
                    value = enableUrlEncode ? Encodes.urlEncode((String) entry.getValue(), charset)
                            : (String) entry.getValue();
                    sb.append(entry.getKey()).append("=").append(value);
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            url = sb.toString();
        }

        logger.debug("get：{}", url);
        HttpGet get = new HttpGet(url);
        return get;
    }

    private String normalizeUrl(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }

    private void loggerRequest(HttpRequest request) {
        logger.debug(request.getRequestLine().toString());
        for (Header header : request.getAllHeaders()) {
            logger.debug("{}: {}", header.getName(), header.getValue());
        }
    }

    private void loggerResponse(HttpResponse response) {
        logger.debug(response.getStatusLine().toString());
        for (Header header : response.getAllHeaders()) {
            logger.debug("{}: {}", header.getName(), header.getValue());
        }
    }

}
