package com.linchong.tomcat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @BelongsProject:WebServer
 * @BelongsPackage:com.linchong.tomcat
 * @Author:linchong
 * @CreateTime:2019-04-08 19:07
 * @Description:服务端
 */
public class HttpServer {
    //静态资源访问路径：存放静态资源
    static final File WEB_ROOT = new File("G:/resources/resource-tomcat");
    //终止命令
    private static final String SHUTDOWN_COMMAND="shutdown";
    //是否终止，用来关闭服务器
    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.await();
    }

    private void await(){
        try(ServerSocket serverSocket = new ServerSocket(8080)){
            //服务器处理请求
            serverProcess(serverSocket);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //模拟处理器处理请求过程
    private void serverProcess(ServerSocket serverSocket) {
        while(!shutdown){
            //从服务器端获取socket实例
            try(Socket socket = serverSocket.accept()){
                //System.out.println(socket.hashCode());
                //获取socket中inputStream
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                Request request = new Request(input);
                request.parse();
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();
                //System.out.println("请求为："+request.getUri());
                int index = request.getUri().lastIndexOf("/");
                shutdown = request.getUri().substring(index+1).equals(SHUTDOWN_COMMAND);
            }catch (IOException e){
                    e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
