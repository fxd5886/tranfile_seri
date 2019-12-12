package com.example.tranfile.enumClass;

import sun.security.util.ECUtil;

/**
 * @Author: fanxieding
 * @Description:
 * @Date:10:23 2019/11/15
 **/
public enum EnumLine {
    Nari(1,"eslb","qgw_eslb"),
    YCJQ(2,"ycjq","hubei_enshi_liba"),
    LSHF(3,"lshf","zhejiang_lishui_hefeng"),
    LSLF(4,"lslf","zhejiang_lishui_lifeng"),
    LSXM(5,"lsxm","zhejiang_lishui_xiangmu"),
    NBTN(6,"nbtn","zhejiang_ningbo_tianning"),
    NBTX(7,"nbtx","zhejiang_ningbo_tianxiao"),
    JHNZ(8,"jhnz","zhejiang_jinhua_ningzhu4u59"),
    JHXY(9,"jhxy","zhejiang_jinhua_xiyan4397");

    private Integer id;
    //线路名，bin文件获得
    private String lineName;
    //数据库名
    private String dataName;

    EnumLine(Integer id, String lineName, String dataName) {
        this.id = id;
        this.lineName = lineName;
        this.dataName = dataName;
    }

    public static EnumLine  getById(Integer id){
        for(EnumLine line : EnumLine.values()){
           if(line.getId() == id){
               return line;
           }
        }
        return Nari;
    }

    public static EnumLine  getByLineName(String lineName){
        for(EnumLine line : EnumLine.values()){
            if(line.getLineName().equals(lineName)){
                return line;
            }
        }
        return Nari;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
