package com.example.tranfile.util;

import com.example.tranfile.vo.Message16;
import com.example.tranfile.vo.PhiLineSpanResults;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName TruckStreamServer
 * @Description: TODO
 * @Author fxd
 * return int 1正常 2检验位检验失败 3 数据超时
 * @Date 2019/10/23
 **/
public class TruckStreamServer {

    private static final int RETURN_1 = 1;
    private static final int RETURN_2 = 2;
    private static final int RETURN_3 = 3;

    //根据客户端的输入字节流，生成一个实体
    public static Message16 treatDis(DataInputStream dis) throws Exception {
        Message16 message16 = new Message16();
        message16.setBzlx(dis.readByte());
        dis.read(message16.getBxlbh());
        message16.setXlbh(Integer.toHexString(BytesUtils.bytesToShort(message16.getBxlbh())));
        dis.read(message16.getBsbbh());
        message16.setBcjbh(dis.readByte());
        message16.setBsbzt(dis.readByte());
        message16.setBbwlx(dis.readByte());
        message16.setBbwzts(dis.readByte());
        message16.setBbwbh(dis.readByte());
        dis.read(message16.getBbwcd());
        message16.setBwcd(Integer.toHexString(BytesUtils.bytesToShort(message16.getBbwcd())));
        dis.read(message16.getBsjsj());
        dis.read(message16.getBylzd());
        Integer bwcd = BytesUtils.bytesToShort(message16.getBbwcd())/4;
        List<PhiLineSpanResults> bwnr=new LinkedList<>();
        for(int i=0;i<bwcd;i++){
            PhiLineSpanResults obj = new PhiLineSpanResults();
            byte[] bytes2 = new byte[2];
            byte[] thick = new byte[1];
            byte[] thick100 = new byte[1];
            dis.read(bytes2,0,2);
            obj.setBlineSpanId(bytes2);
            obj.setLineSpanId((short)BytesUtils.bytesToShort(bytes2));
            dis.read(thick,0,1);
            byte[] thick_decode = XORUtils.encrypt(thick);
            obj.setBiceThickness(thick_decode[0]);
            obj.setIceThickness((int)thick_decode[0]);
            dis.read(thick100,0,1);
            byte[] thick100_decode = XORUtils.encrypt(thick100);
            obj.setBiceThickness100(thick100_decode[0]);
            obj.setIceThickness100((int)thick100_decode[0]);
            bwnr.add(obj);
        }
        message16.setBwnr(bwnr);
        dis.read(message16.getBjyw(),0,4);
        System.out.println(message16.getJywByBytes()+","+BytesUtils.bytesToInt(message16.getBjyw()));
        return message16;
    }
    /**
     * @MethodName:
     * @Description: 判断实体是否合法
     * @Param:
     * @Return: 1 合法 2总值校验不相等 3超时
     * @Author: fxd
     * @Date: 2019/10/26
    **/
    public static int validateMessage(Message16 message16) throws Exception {
        //由服务端接受的所有字段的和与由客户端计算的所有字段的和就是bjyw值比对
        if(message16.getJywByBytes()!=(BytesUtils.bytesToInt(message16.getBjyw()))){
            return RETURN_2;
        }
        System.out.println(message16.getSjsj());
        long lsjsj = DateUtils.getDateByString("20"+message16.getSjsj(),"yyyyMMddHHmmss").getTime() ;
        long now = (new Date()).getTime();
        //数据超过2个小时返回3 超时
        if((now-lsjsj)>2*60*60*1000){
            return RETURN_3;
        }
        return RETURN_1;
    }

    /**
     * @MethodName:
     * @Description: 根据实体，生成服务端文件
     * @Param:
     * @Return:
     * @Author: fxd
     * @Date: 2019/10/23
    **/
    public static void treatfos(FileOutputStream fos, Message16 message16) throws IOException {
        fos.write(message16.getBbwt());
        fos.write(message16.getBzlx());
        fos.write(message16.getBxlbh());
        fos.write(message16.getBsbbh());
        fos.write(message16.getBcjbh());
        fos.write(message16.getBsbzt());
        fos.write(message16.getBbwlx());
        fos.write(message16.getBbwzts());
        fos.write(message16.getBbwbh()) ;
        fos.write(message16.getBbwcd());
        fos.write(message16.getBsjsj());
        fos.write(message16.getBylzd());
        for(PhiLineSpanResults obj: message16.getBwnr()){
            fos.write(obj.getBlineSpanId());
            fos.write(XORUtils.encrypt(new byte[]{obj.getBiceThickness()}));
            fos.write(XORUtils.encrypt(new byte[]{obj.getBiceThickness100()}));
        }
        fos.write(message16.getBjyw());
    }

    public  static void backToClient(Integer  status, DataOutputStream dos) throws IOException {
        Message16 message16 = new Message16();
        dos.write(message16.getBbwt());
        dos.write(message16.getBzlx());
        dos.write(message16.getBxlbh());
        dos.write(message16.getBsbbh());
        dos.write(message16.getBcjbh());
        dos.write(message16.getBbwlx());
        dos.write(message16.getBbwcd());
        dos.write(status.byteValue()) ;
        message16.setSjsj(null);
        dos.write(message16.getBsjsj());
    }

    public static void main(String[] args) throws Exception {
        FileInputStream input = new FileInputStream("E:\\myserver\\eslb_iceResults_20190101-003000.bin");
        //treatDis(new DataInputStream(input));
        byte[] b = new byte[700];
        input.read(b);
        System.out.println(BytesUtils.bytesToHex(b));
    }
}
