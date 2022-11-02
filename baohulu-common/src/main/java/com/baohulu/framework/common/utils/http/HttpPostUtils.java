package com.baohulu.framework.common.utils.http;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.common.utils.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * http/https post访问工具类
 *
 * @author heqing
 * @date 2018/5/21
 */
public class HttpPostUtils {

    /** HTTP内容类型。*/
    public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";

    /** HTTP内容类型。相当于form表单的形式，提交数据 */
    public static final String CONTENT_TYPE_JSON_URL = "application/json;charset=utf-8";

    /** HTTP内容类型。相当于form表单的形式，提交数据 */
    public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";

    /** 连接管理器 */
    private static PoolingHttpClientConnectionManager pool;

    /** 请求配置 */
    private static RequestConfig requestConfig;

    /** 状态码 */
    private static final int STATUS_CODE = 300;

    static {
        // 初始化连接管理器
        pool = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加到200，实际项目最好从配置文件中读取这个值
        pool.setMaxTotal(200);
        // 设置最大路由
        pool.setDefaultMaxPerRoute(2);
        // 根据默认超时限制初始化requestConfig
        int socketTimeout = 10000;
        int connectTimeout = 10000;
        int connectionRequestTimeout = 10000;
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(
                connectionRequestTimeout).setSocketTimeout(socketTimeout).setConnectTimeout(
                connectTimeout).build();
        // 设置请求超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000)
                .setConnectionRequestTimeout(50000).build();
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                // 设置连接池管理
                .setConnectionManager(pool)
                // 设置请求配置
                .setDefaultRequestConfig(requestConfig)
                // 设置重试次数
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .build();
    }

    public static HttpPost getHttpPost(String httpUrl, String params, String contentType) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            // 设置参数
            if (org.apache.commons.lang3.StringUtils.isNotBlank(params.trim())) {
                StringEntity stringEntity = new StringEntity(params, Network.UTF_8);
                stringEntity.setContentType(contentType);
                httpPost.setEntity(stringEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    /**
     * 发送Post请求
     * @param httpPost 请求内容
     * @return 返回内容
     */
    public static String sendHttpRequest(HttpPost httpPost) {
        CloseableHttpClient httpClient;
        CloseableHttpResponse response = null;
        // 响应内容
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            httpClient = getHttpClient();
            // 配置请求信息
            httpPost.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpPost);
            // 得到响应实例
            HttpEntity entity = response.getEntity();

            // 判断响应状态
            if (response.getStatusLine().getStatusCode() >= STATUS_CODE) {
                throw new Exception( "HTTP Request is not success, Response code is " + response.getStatusLine().getStatusCode());
            }

            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                responseContent = EntityUtils.toString(entity, Network.UTF_8);
                EntityUtils.consume(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static HttpPost getHttpPost(String httpUrl, String paramsJson) {
        return getHttpPost(httpUrl, paramsJson, CONTENT_TYPE_FORM_URL);
    }

    public static HttpPost getHttpPostByJson(String httpUrl, String paramsJson) {
        return getHttpPost(httpUrl, paramsJson, CONTENT_TYPE_JSON_URL);
    }

    public static HttpPost getHttpPostByXml(String httpUrl, String paramsJson) {
        return getHttpPost(httpUrl, paramsJson, CONTENT_TYPE_TEXT_HTML);
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     * @param params 参数(格式:key1=value1&key2=value2)
     */
    public static String sendHttpRequest(String httpUrl, String params) {
        return sendHttpRequest(getHttpPost(httpUrl, params));
    }

    /**
     * 发送 post请求 发送json数据
     * @param httpUrl 地址
     * @param paramsJson 参数(格式 json)
     */
    public static String sendHttpJsonRequest(String httpUrl, String paramsJson) {
        return sendHttpRequest(getHttpPostByJson(httpUrl, paramsJson));
    }

    /**
     * 发送 post请求 发送xml数据
     * @param httpUrl  地址
     * @param paramsXml 参数(格式 Xml)
     *
     */
    public static String sendHttpXmlRequest(String httpUrl, String paramsXml) {
        return sendHttpRequest(getHttpPostByXml(httpUrl, paramsXml));
    }

    /**
     * 发送 post请求
     * @param maps 参数
     */
    public static String sendHttpRequest(String httpUrl, Map<String, String> maps) {
        return sendHttpRequest(httpUrl, StringUtils.mapToHttpString(maps));
    }

    /**
     * 发送 post请求
     * @param httpUrl 地址
     */
    public static String sendHttpRequest(String httpUrl) {
        return sendHttpRequest(httpUrl, "");
    }

}
