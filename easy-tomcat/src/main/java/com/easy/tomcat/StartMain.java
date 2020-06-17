package com.easy.tomcat;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author qinhaohao
 * @Date 2020/6/17 3:56 下午
 **/
public class StartMain {

    private Map<String,HttpServlet> servletMap=new HashMap<>();
    public static void main(String[] args)  {
        StartMain startMain = new StartMain();
        try {
            startMain.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void start() throws Exception{
        ServerSocket serverSocket = new ServerSocket(8080);

        loadServlet();
        // 定义一个线程池
        int corePoolSize = 10;
        int maximumPoolSize =50;
        long keepAliveTime = 100L;
        TimeUnit unit = TimeUnit.SECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(50);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();


        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
//        while (true){
//            Socket reqSocket = serverSocket.accept();
//            OutputStream outputStream = reqSocket.getOutputStream();
//            String success="success";
//            outputStream.write((HttpProtocolUtil.getHttpHeader200(success.length())+success).getBytes());
//            outputStream.flush();
//            outputStream.close();
//        }
//        while (true){
//            Socket reqSocket = serverSocket.accept();
//            InputStream inputStream = reqSocket.getInputStream();
//            outInputStream(inputStream);
//            OutputStream outputStream = reqSocket.getOutputStream();
//            String success="success";
//            outputStream.write((HttpProtocolUtil.getHttpHeader200(success.length())+success).getBytes());
//            outputStream.flush();
//            outputStream.close();
//        }

//        while (true){
//            Socket reqSocket = serverSocket.accept();
//            InputStream inputStream = reqSocket.getInputStream();
//            OutputStream outputStream = reqSocket.getOutputStream();
//            Request request = new Request(inputStream);
//            if (request.getUrl().endsWith(".ico")){
//                Response response = new Response(outputStream);
//                response.output(HttpProtocolUtil.getHttpHeader200(0));
//                return;
//            }
//            if (request.getUrl().endsWith(".html")){
//                Response response = new Response(outputStream);
//                response.outputHtml(request.getUrl());
//            }
//            outputStream.flush();
//            outputStream.close();
//        }

//        while (true){
//            Socket reqSocket = serverSocket.accept();
//            InputStream inputStream = reqSocket.getInputStream();
//            OutputStream outputStream = reqSocket.getOutputStream();
//            Request request = new Request(inputStream);
//            Response response = new Response(outputStream);
//            if (request.getUrl().endsWith(".ico")){
//                response.output(HttpProtocolUtil.getHttpHeader200(0));
//                return;
//            }
//            if (request.getUrl().endsWith(".html")){
//                response.outputHtml(request.getUrl());
//            }else {
//                HttpServlet httpServlet = servletMap.get(request.getUrl());
//                if (httpServlet==null){
//                    response.output(HttpProtocolUtil.getHttpHeader404());
//                    return;
//                }
//                httpServlet.service(request,response);
//            }
//        }
        while (true){
            Socket socket = serverSocket.accept();
            RequestProcessor requestProcessor = new RequestProcessor(socket, servletMap);
            threadPoolExecutor.execute(requestProcessor);
        }
    }
    private  void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.selectNodes("//servlet");
            for (Element selectNode : selectNodes) {
                Node servletNameNode = selectNode.selectSingleNode("servlet-name");
                String servletName = servletNameNode.getStringValue();
                Node servletClassNode = selectNode.selectSingleNode("servlet-class");
                String servletClass = servletClassNode.getStringValue();
                Node servletMapping = rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }







    private static void outInputStream(InputStream inputStream) throws IOException {

        long writelen=0;
        StringBuffer stringBuffer=new StringBuffer();
        int byteSize = 1024;
        byte[] buffer=new byte[byteSize];
        int available = inputStream.available();
        while (writelen<available){
           if (writelen+byteSize>available){
               byteSize = (int) (available - writelen);
               buffer=new byte[byteSize];
           }
           inputStream.read(buffer);
            writelen+=byteSize;
           stringBuffer.append(new String(buffer));
        }
        System.out.println(stringBuffer.toString());
    }


}
