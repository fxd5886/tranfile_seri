package com.example.tranfile.util;

import com.example.tranfile.enumClass.EnumLine;
import com.example.tranfile.vo.Message16;
import com.example.tranfile.vo.PhiLineSpanResults;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

import java.io.*;
import java.lang.reflect.Member;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName GetMessageByFile
 * @Description: TODO
 * @Author fxd
 * @Date 2019/10/22
 **/
public class TruckStreamClient {

    /**
     * @MethodName: 客户端根据bin文件包装一个报文的输出流
     * @Description: TODO
     * @Param:i 当前编号  fileLength 本次传输总文件个数
     * @Return:
     * @Author: fxd
     * @Date: 2019/10/22
    **/
    public  static void getMsgFisByFileClient(int i,int fileLength,SmbFile file, DataOutputStream dos) throws IOException {
        Message16 message16 = packMsgByFile(i,fileLength,file);
        dos.write(message16.getBbwt());
        dos.write(message16.getBzlx());
        dos.write(message16.getBxlbh());
        dos.write(message16.getBsbbh());
        dos.write(message16.getBcjbh());
        dos.write(message16.getBsbzt());
        dos.write(message16.getBbwlx());
        dos.write(message16.getBbwzts());
        dos.write(message16.getBbwbh()) ;
        dos.write(message16.getBbwcd());
        dos.write(message16.getBsjsj());
        dos.write(message16.getBylzd());
        for(PhiLineSpanResults obj: message16.getBwnr()){
            dos.write(obj.getBlineSpanId());
            dos.write(XORUtils.encrypt(new byte[]{obj.getBiceThickness()}));
            dos.write(XORUtils.encrypt(new byte[]{obj.getBiceThickness100()}));
        }
        dos.write(message16.getBjywByString());
    }

    private static Message16 packMsgByFile(int k, int fileLength, SmbFile file){
        Message16 msg = new Message16();
        List<PhiLineSpanResults> list = new LinkedList<PhiLineSpanResults>();
        //暂定
        String linename = file.getName().split("_")[0];
        msg.setXlbh(EnumLine.getByLineName(linename).getId()+"");
        msg.setSjsj(file.getName());
        msg.setBbwzts((byte) fileLength);
        msg.setBbwbh((byte)k);
        msg.setBwzts(Integer.toHexString(fileLength));
        msg.setBwbh(Integer.toHexString(k));
        try (InputStream is =new SmbFileInputStream(file);
             CppInputStream input = new CppInputStream(is)){
            input.skip(4);
            int num = input.readInt();//走了4个字节
            //一条数据8个字节，报文长度为条数*8
            String bwcd = Integer.toHexString(num*4);
            msg.setBwcd(bwcd);
            for(int i=0;i<num;i++){
                PhiLineSpanResults phiLineSpanResults = new PhiLineSpanResults();
                short lineSpanId = (short)input.readInt();
                phiLineSpanResults.setLineSpanId(lineSpanId);
                phiLineSpanResults.setBlineSpanId(BytesUtils.shortToBytes(lineSpanId));
                //这个地方包括下面，这个值跟报文示例的长度不一样，这里只有2，下面的double只有1，这个具体怎么转换要在商讨
                double iceThickness = input.readDouble();
                int mm = (int)Math.round(iceThickness*1000000/10);
                phiLineSpanResults.setIceThickness(mm/100);
                phiLineSpanResults.setBiceThickness((byte) (mm/100));
                phiLineSpanResults.setIceThickness100(mm%100);
                phiLineSpanResults.setBiceThickness100((byte) (mm%100));
                input.skipBytes(16);
                //if(mm>0) System.out.println("i="+i+",mm="+mm+",bytemm="+(byte) (mm/100)+","+(byte) (mm%100));
                list.add(phiLineSpanResults);
            }
            msg.setBwnr(list);
            System.out.println(num);
            System.out.println(msg);
            input.skipBytes(56);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * @MethodName:
     * @Description:  保证这个方法不是一个阻塞方法
     *                给3次机会，如果还是没有数据，就返回false
     * @Param:
     * @Return:
     * @Author: fxd
     * @Date: 2019/10/23
    **/
    public  static boolean backToClient(DataInputStream dis) throws IOException, InterruptedException {
        for(int i=0;i<3;i++){
            int avlialbe = dis.available();//非阻塞方法
            if(avlialbe == 0){
                Thread.currentThread().sleep(1000);
            }else{
                /*try {
                    dis.skipBytes(11);
                    byte state= dis.readByte() ;
                    dis.skipBytes(6);
                    System.out.println("返回state："+state);
                    return state == 0;//0表示正常
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }*/

                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("E:\\myclient\\eslb_iceResults_20190101-003000.bin");
        DataOutputStream dis = new DataOutputStream(new FileOutputStream("D:\\myclient\\eslb_iceResults_20190101-003000.bin"));
        //getMsgFisByFileClient(file,dis);
    }


}
