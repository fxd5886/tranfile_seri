package com.example.tranfile;

import com.example.tranfile.enumClass.EnumLine;
import com.example.tranfile.util.BytesUtils;
import com.example.tranfile.util.FileOperateDemo;
import com.example.tranfile.util.TruckStreamServer;
import com.example.tranfile.vo.Message16;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
public class ServerService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ServerService.class);
    @Value("${serverFilePath}")
    public   String filePath ;//文件存放的位置
    @Value("${port}")
    public  int port ;
    public  void run(String[] args)  throws IOException{
        ServerSocket server = new ServerSocket(port);
        System.out.println("等待与客户端建立连接...");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        int i=0;
        while (true) {
            Socket socket = server.accept();
            System.out.println(socket.toString());
            fixedThreadPool.execute(new Task(socket));
        }
    }


     class Task implements Runnable {
        private Socket socket;
        public Task(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                handlerSocket();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        /**
         * 跟客户端Socket进行通信
         *
         * @throws IOException
         */


        private void handlerSocket() throws Exception {
            System.out.println("客户端--"+socket.getInetAddress()+"连接完毕");
            DataInputStream dis= null;
            DataOutputStream dos = null;
            FileOutputStream fos= null;
            try{
                boolean flag=true;
                dis=new DataInputStream(socket.getInputStream());
                dos=new DataOutputStream(socket.getOutputStream());
                byte[] head=new byte[2];
                int ti;
                new File(filePath).mkdirs();
                while(flag){
                    ti=dis.read(head);
                    if(BytesUtils.bytesToHex(head).equals("6666")){
                        Message16 message16 = TruckStreamServer.treatDis(dis);
                        message16.setBbwt(head);
                        log.info(message16.toString());
                        int lineId = BytesUtils.bytesToShort(message16.getBxlbh());
                        int cjbh = message16.getBcjbh();
                        String time = message16.getSjsj();
                        time = "20"+time.substring(0,6)+"-"+time.substring(6);
                        String lineName = EnumLine.getById(lineId).getLineName();
                        String filename = lineName+"_iceResults_"+time+".bin";

                        int result = TruckStreamServer.validateMessage(message16);
                        //对南瑞的数据不做时间的校验
                        if(result == 3 && cjbh!=1){
                            //数据过期，中断连接
                            log.info("-------------客户端发送的文件在2小时之前");
                            TruckStreamServer.backToClient(3,dos);
                            dos.flush();
                            flag = false;
                        }else if(result == 2){
                            log.info("-------------报文验证不通过");
                            //发送客户端报文说失败
                            TruckStreamServer.backToClient(1,dos);
                            dos.flush();
                            //flag = false;
                        }else {
                            fos = new FileOutputStream(filePath + filename);
                            TruckStreamServer.treatfos(fos, message16);
                            fos.flush();
                            TruckStreamServer.backToClient(0, dos);
                            dos.flush();
                        }
                        //当文件总条数等于文件编号，说明是最后一个文件
                        if(message16.getBbwbh()==message16.getBbwzts()){
                            flag = false;
                        }
                    }else{
                        flag = false;
                    }

                }
            }
            catch(IOException e){
                e.printStackTrace();
            }finally {
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (socket != null) {
                    try {
                        System.out.println(socket.getPort()+"连接关闭！");
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
