package com.example.tranfile;

import com.example.tranfile.seri.SerialPortUtil;
import com.example.tranfile.util.DateUtils;
import com.example.tranfile.util.FileOperateDemo;
import com.example.tranfile.util.SmbTransFile;
import com.example.tranfile.util.TruckStreamClient;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ClientServer
 * @Description: TODO
 * @Author fxd
 * @Date 2019/10/22
 **/
@Component
public class ClientServer {
    private static final Logger log = LoggerFactory.getLogger(ClientServer.class);
    private static final int maxFileLength = 20;
    @Value("${clientFilePath}")
    public String filePaths ;//文件目录
    @Value("${remotePath}")//存放远程bin文件主机ip
    public  String remotePath ;
    @Value("${suffixPath}")//路径后缀
    public  String suffixPath ;
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
            System.out.println("bbbbbb");

            client = SerialPortUtil.openSerialPort("COM3", 115200);
             //client=new Socket(ip,port);
            System.out.println("已连接");
            dos=new DataOutputStream(client.getOutputStream());
            dis=new DataInputStream(client.getInputStream());
            String[] filePathArr = filePaths.split(",");
            for(int i=0;i<filePathArr.length;i++){
                this.filePath = remotePath+filePathArr[i]+suffixPath;
                this.donePath = this.filePath+"zbak/";
                transmit(SmbTransFile.getSmbFile(filePath));
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
    public void transmit(SmbFile f) throws IOException, InterruptedException {
        SmbFile[] files = f.listFiles();
        int fileLength = files.length>maxFileLength?maxFileLength:files.length;
        for(int i=0;i<fileLength;i++){ //一次连接最多只传20个文件
            SmbFile f1  = files[i];
            if(f1.isDirectory()){
                continue;
            }
                log.info("-------------发送文件的路径是："+(f1.getPath().replace(filePath,"")));//表示这是一个文件的名称);
                TruckStreamClient.getMsgFisByFileClient(i+1,fileLength,f1,dos);
                dos.flush();
                //如果服务端返回正确
                if(TruckStreamClient.backToClient(dis)) {
                    log.info(f1.getName() + "已被顺利推送到服务端");
                    //FileOperateDemo.cutGeneralFile(f1.getAbsolutePath(),donePath);//把文件剪切到新目录
                    SmbTransFile.smbCopy(this.filePath+f1.getName(),this.donePath);
                    f1.delete();
                }else{
                    log.info(f1.getName()+ "传输失败，不做处理，下次再传");
                }
        }
    }

}
