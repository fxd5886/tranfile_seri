package com.example.tranfile.vo;

import com.example.tranfile.util.BytesUtils;
import com.mysql.cj.util.StringUtils;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @ClassName 16进制的报文实体
 * @Description: 本实例中，String类型都为16进制字符串，byte类型都是10进制的short，int等转的
 *   因为报文要控制长度，只能这样转了，服务端接受byte是，直接byte转short，int等就可以
 *   校验位2边都是以10进制计算的
 * @Author fxd
 * @Date 2019/10/22
 **/
@Data
public class Message16 {
    private String bwt;//报文头2  0x6666
    private String zlx;//帧类型1
    private String xlbh;//线路编号2
    private String sbbh;//设备编号2
    private String cjbh;//厂家编号1
    private String sbzt;//设备状态1
    private String bwlx;//报文类型1  0x01
    private String bwzts;//报文总条数1
    private String bwbh;//报文编号1
    private String bwcd;//报文长度2
    private String sjsj;//数据时间6
    private String ylzd;//预留字段8  0x00
    private List<PhiLineSpanResults>bwnr;//报文内容
    private String jyw;//校验位4

    private byte[] bbwt;//报文头2  0x6666
    private byte bzlx;//帧类型1
    private byte[] bxlbh;//线路编号2
    private byte[] bsbbh;//设备编号2
    private byte bcjbh;//厂家编号1
    private byte bsbzt;//设备状态1
    private byte bbwlx;//报文类型1  0x01
    private byte bbwzts;//报文总条数1
    private byte bbwbh;//报文编号1
    private byte[] bbwcd;//报文长度2
    private byte[] bsjsj;//数据时间6
    private byte[] bylzd;//预留字段8  0x00
    private byte[] bjyw;//校验位4

    //默认值先这样处理，5个需要变动的值在set时处理
    public Message16() {
        this.bwt = "6666";
        this.zlx = "1";
        this.sbbh = "1";
        this.cjbh = "1";
        this.sbzt = "0";
        this.bwlx = "1";
        this.ylzd = "0";
        this.bbwt = BytesUtils.shortToBytes(Short.parseShort(this.bwt,16));
        this.bzlx = Byte.parseByte(this.zlx,16);
        this.bxlbh = new byte[2];
        this.bsbbh = BytesUtils.shortToBytes(Short.parseShort(this.sbbh,16));
        this.bcjbh = Byte.parseByte(this.cjbh,16);
        this.bsbzt = Byte.parseByte(this.sbzt,16);
        this.bbwlx = Byte.parseByte(this.bwlx,16);
        this.bbwcd = new byte[2];
        this.bsjsj = new byte[6];
        this.bylzd = BytesUtils.longToBytes(Long.parseLong(this.ylzd,16));
        this.bjyw = new byte[4];
    }

    public void setXlbh(String xlbh) {
        this.xlbh = xlbh;
        this.bxlbh = BytesUtils.shortToBytes(Short.parseShort(this.xlbh,16));
    }

    public void setBwcd(String bwcd) {
        this.bwcd = bwcd;
        this.bbwcd = BytesUtils.shortToBytes(Short.parseShort(this.bwcd,16));
    }

    public void setSjsj(String name) {
        String[] nowDate10s;
        if(name==null){
             nowDate10s = new SimpleDateFormat("yy-MM-dd-HH-mm-ss").format(new Date()).split("-");
        }else {
            String ymd = name.substring(name.lastIndexOf(".")-15,name.lastIndexOf(".")).replace("-","");
            //把当前时间转成16进制的数保存,那边接受的是字符串。其他的那边接收的都是10进制的数字
            nowDate10s = parse(ymd);
        }
        this.sjsj = Integer.toHexString(Integer.parseInt(nowDate10s[0]))+Integer.toHexString(Integer.parseInt(nowDate10s[1]))
                   +Integer.toHexString(Integer.parseInt(nowDate10s[2]))+Integer.toHexString(Integer.parseInt(nowDate10s[3]))
                   +Integer.toHexString(Integer.parseInt(nowDate10s[4]))+Integer.toHexString(Integer.parseInt(nowDate10s[5]));
        byte[] bytes = new byte[6];
        for(int i=0;i<6;i++){
            bytes[i] = Byte.parseByte(Integer.toHexString(Integer.parseInt(nowDate10s[i])),16);
        }
        this.bsjsj = bytes;
    }

    public String getSjsj(){
        byte[] bytes = this.bsjsj;
        String sjsj="";
        for(byte b:bytes){
            int aa = Integer.parseInt(BytesUtils.byteToHex(b),16);
            if(aa<10){
                sjsj = sjsj+"0"+aa;
            }else {
                sjsj = sjsj + aa;
            }
        }
        return sjsj;
    }

    public void setJyw(String jyw) {
        this.jyw = jyw;
        this.bjyw = BytesUtils.intToBytes(Integer.parseInt(this.jyw,16));
    }

    public String getJywByString(){

        int total = Integer.parseInt(this.bwt, 16)+Integer.parseInt(this.bwzts, 16)
                +Integer.parseInt(this.bwbh, 16)+Integer.parseInt(this.bwcd, 16)
                +Integer.parseInt(this.cjbh, 16)
                +Integer.parseInt(this.sbbh, 16)+Integer.parseInt(this.sbzt, 16)
                +Integer.parseInt(this.xlbh, 16)+Integer.parseInt(this.zlx, 16)
                +Integer.parseInt(this.bwlx, 16);
        for(PhiLineSpanResults obj:this.bwnr){
            total += obj.getLineSpanId()+obj.getIceThickness()+obj.getIceThickness100();
        }
        //把报文内容的值要加上，这个要在
        return Integer.toHexString(total);
    }

    public int getJywByBytes(){

        int total = BytesUtils.bytesToShort(this.bbwt)+this.bbwzts
                +this.bbwbh+BytesUtils.bytesToShort(this.bbwcd)+this.bcjbh
                +BytesUtils.bytesToShort(this.bsbbh)+this.bsbzt+BytesUtils.bytesToShort(this.bxlbh)
                +this.bzlx+this.bbwlx;
        for(PhiLineSpanResults obj:this.bwnr){
            total += BytesUtils.bytesToShort(obj.getBlineSpanId())
                    +(int)obj.getBiceThickness() +(int)obj.getBiceThickness100();
        }
        //把报文内容的值要加上，这个要在
        return total;
    }


    public byte[] getBjywByString() {
        return BytesUtils.intToBytes(Integer.parseInt(getJywByString(),16));
    }

    public static void main(String[] args) {
        Message16 obj = new Message16();
        obj.setSjsj("asdfas20190101-003000.bin");
        String time = obj.getSjsj();
        String time1 = "20"+time.substring(0,6)+"-"+time.substring(6);
        System.out.println(time1);
    }
    private String[] parse(String ymd){
        String[] strings = new String[6];
        strings[0] = ymd.substring(2,4);
        strings[1] = ymd.substring(4,6);
        strings[2] = ymd.substring(6,8);
        strings[3] = ymd.substring(8,10);
        strings[4] = ymd.substring(10,12);
        strings[5] = ymd.substring(12,14);

        return  strings;
    }

    @Override
    public String toString() {
        return "Message16{" +
                "bbwt=" + BytesUtils.bytesToHex(bbwt) +
                ", bzlx=" + bzlx +
                ", bxlbh=" + BytesUtils.bytesToHex(bxlbh) +
                ", bsbbh=" + BytesUtils.bytesToHex(bsbbh) +
                ", bcjbh=" + bcjbh +
                ", bsbzt=" + bsbzt +
                ", bbwlx=" + bbwlx +
                ", bbwzts=" + bbwzts +
                ", bbwbh=" + bbwbh +
                ", bbwcd=" + BytesUtils.bytesToHex(bbwcd) +
                ", bsjsj=" + BytesUtils.bytesToHex(bsjsj) +
                ", bylzd=" + BytesUtils.bytesToHex(bylzd) +
                ", bjyw=" + BytesUtils.bytesToHex(bjyw) +
                '}';
    }
}
