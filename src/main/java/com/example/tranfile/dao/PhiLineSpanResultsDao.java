package com.example.tranfile.dao;

import com.example.tranfile.vo.PhiLineSpanResults;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName PhiLineSpanResultsDao
 * @Description: TODO
 * @Author fxd
 * @Date 2019/10/12
 **/
public interface PhiLineSpanResultsDao {
    /**
     * @MethodName: 批量添加数据
     * @Description: TODO
     * @Param: 
     * @Return: 
     * @Author: fxd
     * @Date: 2019/10/12
    **/
    void batchAdd(@Param("entitys") List<PhiLineSpanResults> list,@Param("dataname")String dataname);

}
