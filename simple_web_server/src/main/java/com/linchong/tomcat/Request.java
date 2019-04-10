package com.linchong.tomcat;

import java.io.IOException;
import java.io.InputStream;

/**
 * @BelongsProject:WebServer
 * @BelongsPackage:com.linchong.tomcat
 * @Author:linchong
 * @CreateTime:2019-04-08 19:07
 * @Description:请求端
 * 该类代表一个Http请求，从负责与客户端通信的Socket中传递来的InputStream对象中
 * 构造这个类的一个实例，调用InputStream对象中一个read方法来获取到HTTP请求的原
 * 始数据。
 *
 * HTTP请求结构：
 *  一个HTTP请求包括四部分：
 *  1.方法-统一资源标识符-协议/版本          POST /example/index.html  HTTP/1.1
 *  2.请求的头部                             Accept: text/plain: text/html
 *                                           Accept-Language:en-gb
 *                                           Connection: keep-Alive
 *                                           Host: localhost
 *                                           User-Agent: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98)
 *                                           Content-Length: 33
 *                                           Content-Type: application/x-www-form-urlencoded
 *                                           Accept-Encoding: gzip,deflate
 * 3.空行(CRLF)
 * 4.主体内容                                lastName=Tom&firstName=Ming
 *
 * 扩展：1.Http1.1中支持7中方法，GET/POST/HEAD/OPTIONS/PUT/DELETE/TRACE
 *       2.每一个头部都是通过CRLF空行来分隔的
 */
public class Request {
    private InputStream input; //客户端传递来的InputStream输入流
    private String uri;         //请求的uri
    public Request(InputStream input) {
        this.input = input;
    }

    /**
     * 从socket中读取数据
     */
    public void parse(){
        StringBuilder request = new StringBuilder(2048);
        int i;
        byte[] buffer = new byte[2048];
        try{
            i = input.read(buffer); //将数据读入到buffer数组中
        }catch (IOException e){
            e.printStackTrace();
            i = -1;
        }
        for (int j = 0; j < i; j++) {
            request.append((char)buffer[j]); //转换
        }
        System.out.println(request.toString());
        uri = parseUri(request.toString());
    }

    /**
     * 截取请求参数，如http://localhost:8080/index.html,截取之后为index.html
     * @param requestString
     * @return
     */
    private String parseUri(String requestString){
        int index1,index2;
        index1 = requestString.indexOf(' ');
        if(index1!=-1){
            index2 = requestString.indexOf(' ',index1+1);
            if(index2 > index1)
                return requestString.substring(index1+1,index2);
        }
        return null;
    }
    public String getUri(){
        return uri;
    }
}
