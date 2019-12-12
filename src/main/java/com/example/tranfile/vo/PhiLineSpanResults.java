package com.example.tranfile.vo;

import lombok.Data;

/**
 * @ClassName phiLineSpanResults
 * @Description: TODO
 * @Author fxd
 * @Date 2019/10/11
 **/
@Data
public class PhiLineSpanResults {
    private Short lineSpanId;//档距ID
    private Integer iceThickness;//OPGW覆冰厚度
    private Integer iceThickness100;//OPGW覆冰厚度
    private Integer calcTime;//数据计算时间
    private Float mIceThickness;

    //客户端传输用
    private byte[] blineSpanId;//线路ID
    private byte biceThickness;//OPGW覆冰厚度
    private byte biceThickness100;//OPGW覆冰厚度

    @Override
    public String toString() {
        return "PhiLineSpanResults{" +
                ", lineSpanId=" + lineSpanId +
                ", iceThickness=" + iceThickness +
                ", mIceThickness=" + iceThickness100 +
                ", calcTime=" + calcTime +
                '}';
    }
}
