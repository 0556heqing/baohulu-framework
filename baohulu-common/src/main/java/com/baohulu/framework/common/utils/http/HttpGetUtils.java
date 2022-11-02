package com.baohulu.framework.common.utils.http;

import com.baohulu.framework.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * http/https get访问工具类
 *
 * @author heqing
 * @date 2018/5/14
 */
public class HttpGetUtils {

    /**
     * 发送get请求
     * @param url 请求路径加参数
     * @return 服务端返回的数据
     */
    public static String sendHttpRequest(String url) {
        StringBuilder response = new StringBuilder();

        BufferedReader buffReader = null;
        InputStreamReader inReader = null;
        InputStream in = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            conn.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = conn.getInputStream();
            inReader = new InputStreamReader(in);
            buffReader = new BufferedReader(inReader);
            String line;
            while ((line = buffReader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 使用finally块来关闭输入流
            try {
                if(in != null) {
                    in.close();
                }
                if(inReader != null) {
                    inReader.close();
                }
                if (buffReader != null) {
                    buffReader.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return response.toString();
    }

    /**
     * 发送get请求
     * @param url 请求路径
     * @param param 请求参数
     * @return 服务端返回的数据
     */
    public static String sendHttpRequest(String url, String param){
        String urlNameString = url + "?" + param;
        return sendHttpRequest(urlNameString);
    }

    /**
     * 发送 get请求
     * @param maps 参数
     */
    public static String sendHttpRequest(String httpUrl, Map<String, String> maps) {
        return sendHttpRequest(httpUrl, StringUtils.mapToHttpString(maps));
    }

}
