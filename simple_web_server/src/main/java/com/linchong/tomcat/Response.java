package com.linchong.tomcat;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;

import java.io.*;

/**
 * @BelongsProject:WebServer
 * @BelongsPackage:com.linchong.tomcat
 * @Author:linchong
 * @CreateTime:2019-04-08 19:08
 * @Description:响应端,代表一个HTTP响应
 * Http响应体结构：
 * 一个Http响应结构包括四部分：
 *    1.协议/版本-状态标识符-状态码        HTTP/1.1 200 OK
 *    2.响应头部                           Server:Microsoft-IIS/4.0
 *                                         Date: Mon,5 Jan 2019 14:12:12 GMT
 *                                         Content-Type: text/html
 *                                         Last-Modified: Mon,5 Jan 2019 14:12:02 GMT
 *                                         Content-Length: 112
 *   3.空行(CRLF)
 *   4.主体内容                             <!DOCTYPE html>
 *                                          <html lang="en">
 *                                          <head>
 *                                          <meta charset="UTF-8">
 *                                          <title>测试静态资源</title>
 *                                          </head>
 *                                          <body>
 *                                              这是测试文件！！！
 *                                          </body>
 *                                           </html>
 *
 */
public class Response {
    private static final int BUFFER_SIZE = 1024;
    private Request request;
    private OutputStream output;

    public Response(OutputStream output){
        this.output = output;
    }

    public void setRequest(Request request){
        this.request = request;
    }

    public void sendStaticResource() throws IOException{
        //字节数组，传递读取数据
        byte[] bytes = new byte[BUFFER_SIZE];
        //读取访问地址请求的文件,由请求去指定路径下查找文件
        File file = new File(HttpServer.WEB_ROOT,request.getUri());
        try(FileInputStream fis = new FileInputStream(file)){
            if(file.exists()){
                //如果请求的路径下存在目标文件，添加响应的头
                StringBuilder heads = new StringBuilder("HTTP/1.1 200 OK\r\n");
                heads.append("Content-Type: text/html\r\n");
                //头部
                StringBuilder body = new StringBuilder();
                //读取相应主体
                int len;
                while((len = fis.read(bytes,0,BUFFER_SIZE))!=-1){
                    body.append(new String(bytes,0,len));
                }
                //添加Content-Length
                heads.append(String.format("Content-length: %d\n",body.toString().getBytes().length));
                heads.append("\r\n");
                output.write(heads.toString().getBytes());
                output.write(body.toString().getBytes());
            }else{
                response404(output);
            }
        }catch (FileNotFoundException e){
            response404(output);
        }
    }

    private void response404(OutputStream output) throws IOException{
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 404 File Not Found\r\n");
        response.append("Content-Type: text/html\r\n");
        response.append("Content-length: 23");
        response.append("\r\n");
        response.append("<h1>File Not Found</h1>");
        output.write(response.toString().getBytes());
    }


}
