package com.example.tranfile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import jcifs.smb.*;

/**
 * @ClassName SmbTest
 * @Description: TODO
 * @Author fxd
 * @Date 2019/12/10
 **/
public class SmbTransFile {
    private static NtlmPasswordAuthentication auth;
    static {
        String domainip = "192.168.0.19";
        String username = "administrator";
        String password = "Rd@171020";
        auth = new NtlmPasswordAuthentication(domainip, username, password);
    }

    /**
     * @MethodName:
     * @Description: 从远程把文件复制到本地
     * @Param:
     * @Return:
     * @Author: fxd
     * @Date: 2019/12/10
    **/
    public static void smbGet(String remoteUrl,String localDir){
        InputStream in = null;
        OutputStream out = null;
        try {
            jcifs.smb.SmbFile smbFile = new jcifs.smb.SmbFile(remoteUrl,auth);
            String fileName = smbFile.getName();
            File localFile = new File(localDir+File.separator+fileName);
            in = new BufferedInputStream(new SmbFileInputStream(smbFile));
            out = new BufferedOutputStream(new FileOutputStream(localFile));
            byte []buffer = new byte[1024];
            while((in.read(buffer)) != -1){
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void smbCopy(String remoteUrlSrc,String remoteUrlTar){
        InputStream in = null;
        OutputStream out = null;
        SmbFile smbFileSrc = null;
        try {
            smbFileSrc = new SmbFile(remoteUrlSrc,auth);
            String fileName = smbFileSrc.getName();
            SmbFile smbFileTar = new SmbFile(remoteUrlTar+File.separator+fileName,auth);
            in = new BufferedInputStream(new SmbFileInputStream(smbFileSrc));
            out = new BufferedOutputStream(new SmbFileOutputStream(smbFileTar));
            byte []buffer = new byte[1024];
            while((in.read(buffer)) != -1){
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SmbFile getSmbFile(String path) throws MalformedURLException {
        return new SmbFile(path,auth);
    }
    /**
     * 把本地磁盘中的文件上传到局域网共享文件下
     * @param remoteUrl 共享电脑路径 如：smb//administrator:123456@172.16.10.136/smb
     * @param localFilePath 本地路径 如：D:/
     */
    public static void smbPut(String remoteUrl,String localFilePath){
        InputStream in = null;
        OutputStream out = null;
        try {
            File localFile = new File(localFilePath);
            String fileName = localFile.getName();
            jcifs.smb.SmbFile remoteFile = new jcifs.smb.SmbFile(remoteUrl+"/"+fileName,auth);
            in = new BufferedInputStream(new FileInputStream(localFile));
            out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
            byte []buffer = new byte[1024];
            while((in.read(buffer)) != -1){
                out.write(buffer);
                buffer = new byte[1024];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws MalformedURLException, SmbException {
        //smbGet("smb://192.168.0.19/philine/philishui/hefeng/IceResults/resultbak_bak/iceResults/lshf_iceResults_20191202-083000.bin", "d:/data");
        //smbPut("smb://192.168.0.19/philine/philishui/hefeng/IceResults/resultbak_bak/iceResults/lshf_iceResults_20191202-083000.bin", "d:/data");
        /*smbCut("smb://192.168.0.19/philine/philishui/hefeng/IceResults/" +
                        "resultbak_bak/iceResults/lshf_iceResults_20191202-083000.bin",
                "smb://192.168.0.19/philine/philishui/hefeng/IceResults/" +
                        "resultbak_bak/iceResults/bak");*/
        /*SmbFile fs = new SmbFile("smb://192.168.0.19/philine/philishui/hefeng/IceResults/" +
                "resultbak_bak/iceResults/",auth);
        SmbFile[] f = fs.listFiles();
        System.out.println(f.length);*/
        String filePath = "smb://192.168.0.19/philine/philishui/hefeng/IceResults/resultbak_bak/iceResults/";
        String donePath = filePath+"zbak/";
        SmbFile f = getSmbFile(filePath);
        SmbFile[] files = f.listFiles();
        for(int i=0;i<5;i++) { //一次连接最多只传20个文件
            SmbFile f1 = files[i];
            SmbTransFile.smbCopy(filePath + f1.getName(), donePath);
            f1.delete();
        }

    }
}
