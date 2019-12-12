package com.example.tranfile.service;

import com.example.tranfile.dao.PhiLineSpanResultsDao;
import com.example.tranfile.enumClass.EnumLine;
import com.example.tranfile.util.BytesUtils;
import com.example.tranfile.util.CppInputStream;
import com.example.tranfile.util.TimeExchange;
import com.example.tranfile.util.TruckStreamServer;
import com.example.tranfile.vo.Message16;
import com.example.tranfile.vo.PhiLineSpanResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName PhiLineSpanResultsService
 * @Description: TODO
 * @Author fxd
 * @Date 2019/10/12
 **/
@Service
public class PhiLineSpanResultsService {
    @Autowired
    private PhiLineSpanResultsDao phiLineSpanResultsDao;

    public void addBatch(List<PhiLineSpanResults> list,String dataname){
        phiLineSpanResultsDao.batchAdd(list,dataname);
    }

    /**
     * @MethodName:
     * @Description: 把文件内容写入到数据库
     * @Param:
     * @Return:
     * @Author: fxd
     * @Date: 2019/10/12
    **/
    public void addDateByFile(File file){
        String name = file.getName();
        String dataname = EnumLine.getByLineName(name.split("_")[0]).getDataName();
        String time = name.substring(name.lastIndexOf(".")-15,name.lastIndexOf(".")).replace("-","");
        Integer caltime = TimeExchange.DateToTimestamp(TimeExchange.StringToDate(time));
        List<PhiLineSpanResults> list = new LinkedList<PhiLineSpanResults>();
        try (InputStream is =new FileInputStream(file);
             DataInputStream input = new DataInputStream(is)){
            byte[] head=new byte[2];
            input.read(head);
            //这个方法没有读头，在入口的时候需要先读头来判断，为了迁就对方就这样写
            Message16 message16 = TruckStreamServer.treatDis(input);
            message16.setBbwt(head);
            List<PhiLineSpanResults> bwnrs = message16.getBwnr();
            for(PhiLineSpanResults phiLineSpanResults:bwnrs){
                BigDecimal b = new BigDecimal(phiLineSpanResults.getIceThickness()+"."+phiLineSpanResults.getBiceThickness100());
                phiLineSpanResults.setMIceThickness(b.floatValue()==0?0:b.floatValue());
                phiLineSpanResults.setCalcTime(caltime);
                list.add(phiLineSpanResults);
            }
            addBatch(list,dataname);
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }



}
