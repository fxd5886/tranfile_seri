/*
package com.example.tranfile.bak;

import com.example.tranfile.seri.SerialPortUtil;
import com.example.tranfile.util.DateUtils;
import com.example.tranfile.util.FileOperateDemo;
import com.example.tranfile.util.TruckStreamClient;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;


@Component
public class ClientServerbak {
    private static final Logger log = LoggerFactory.getLogger(ClientServerbak.class);
    private static final int maxFileLength = 20;
    @Value("${clientFilePath}")
    public String filePaths ;//需要传送的文件目录
    //@Value("${donePath}")
    public  String donePaths ;//文件传送后需要剪切到的位置
    @Value("${serverIp}")
    public  String ip ;
    @Value("${port}")
    public  int port ;
    private String filePath;
    private String donePath;

    SerialPort client;
    boolean flag=true;
    PrintWriter pw = null;
    FileInputStream fis;//此输入流负责读取本机上要传输的文件
    DataOutputStream dos;//此输出流负责向另一台电脑(服务器端)传输数据
    DataInputStream dis;//此输入流负责读取另一台电脑的回应信息

    @Scheduled(cron = "0 0/1  * * * ? ")
    public void ClientStart() throws  NoSuchPortException, PortInUseException, UnsupportedCommOperationException {
        try{
            System.out.println("aaaaa");

            client = SerialPortUtil.openSerialPort("COM3", 115200);
             //client=new Socket(ip,port);
            System.out.println("已连接");
            dos=new DataOutputStream(client.getOutputStream());
            dis=new DataInputStream(client.getInputStream());
            String[] filePathArr = filePaths.split(",");
            String[] donePathArr = donePaths.split(",");
            for(int i=0;i<filePathArr.length;i++){
                this.filePath = filePathArr[i];
                this.donePath = donePathArr[i];
                String newfilePath = filePath + "\\"+DateUtils.getStringByDate(new Date(),"yyyy")
                +"\\"+DateUtils.getStringByDate(new Date(),"MM")+"\\";
                if (!new File(newfilePath).exists())
                    continue;
                transmit(new File(newfilePath));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
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
            if (client != null) {
                client.close();
            }
        }
    }
    public void transmit(File f) throws IOException, InterruptedException {
        File[] files = f.listFiles();
        int fileLength = files.length>maxFileLength?maxFileLength:files.length;
        for(int i=0;i<fileLength;i++){ //一次连接最多只传10个文件
                File f1  = files[i];
                log.info("-------------发送文件的路径是："+(f1.getPath().replace(filePath,"")));//表示这是一个文件的名称);
                TruckStreamClient.getMsgFisByFileClient(i+1,fileLength,f1,dos);
                dos.flush();
                //如果服务端返回正确
                if(TruckStreamClient.backToClient(dis)) {
                    log.info(f1.getName() + "已被顺利推送到服务端");
                    //FileOperateDemo.cutGeneralFile(f1.getAbsolutePath(),donePath);//把文件剪切到新目录
                    FileOperateDemo.cutGeneralFile(f1.getAbsolutePath(),
                            f1.getParent().replace(filePath.substring(0, filePath.length() - 1),
                                    donePath.substring(0, donePath.length() - 1)));//把文件剪切到新目录，层级关系和原来一样
                }else{
                    log.info(f1.getName()+ "传输失败，不做处理，下次再传");
                }
        }
    }
}
*/
