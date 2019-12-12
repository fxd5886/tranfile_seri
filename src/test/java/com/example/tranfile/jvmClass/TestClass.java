package com.example.tranfile.jvmClass;

import java.math.BigDecimal;

public class TestClass {
    private int m;
    public int incO(){
        return m+1;
    }

    static int i=0;
    static{
        i=1;
    }


    public static void main(String[] args) {

        BigDecimal b = new BigDecimal(0+"."+81);
        System.out.println(b.floatValue());
    }


}
