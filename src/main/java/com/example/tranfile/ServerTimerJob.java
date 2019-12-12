package com.example.tranfile;

import com.example.tranfile.service.PhiLineSpanResultsService;
import com.example.tranfile.util.DateUtils;
import com.example.tranfile.util.FileOperateDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @ClassName ServerTimerJob
 * @Description: 服务端执行定时任务
 * @Author fxd
 * @Date 2019/10/12
 **/
//@Component
public class ServerTimerJob {
    //io密集型任务线程池数量设为cpu*2
    private final static int THREAD_NUM = Runtime.getRuntime().availableProcessors()*2;
    private final static ThreadPoolExecutor pool = new ThreadPoolExecutor(THREAD_NUM,THREAD_NUM,3,
            TimeUnit.SECONDS,new LinkedBlockingQueue<>(500));

    @Autowired
    PhiLineSpanResultsService phiLineSpanResultsService;

    @Value("${serverDonePath}")
    private String serverDonePath ;//服务端处理完文件后存放的新地址
    @Value("${serverFilePath}")
    private String serverFilePath ;//服务端文件目录

    //@Scheduled(cron = "0 0/1  * * * ? ")
    private void execute() throws IOException {
        File f = new File(serverFilePath);
        transmit(f);
    }

    public void transmit(File f)throws IOException{
        byte b[];
        String ts;
        int ti;
        for(File f1:f.listFiles()){ //首先通过if语句判断f1是文件还是文件夹
            //一个文件由一个线程执行入库，效果如何待验证
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    phiLineSpanResultsService.addDateByFile(f1);
                    try {
                        String filename = f1.getName().split("_")[0];
                        String year = DateUtils.getStringByDate(new Date(),"yyyy");
                        String month = DateUtils.getStringByDate(new Date(),"MM");
                        FileOperateDemo.cutGeneralFile(f1.getAbsolutePath(),f1.getParent().
                            replace(serverFilePath.substring(0,serverFilePath.length()-1),
                            serverDonePath.substring(0,serverDonePath.length()-1))
                        +"/"+filename+"/"+year+"/"+month+"/");//把文件剪切到新目录，新路径/文件名/年/月/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        String aa = "d:\\data\\";
        String bb = "e:\\data\\";
        String parent = "e:\\data\\";
        System.out.println(parent.replace(bb,aa));
    }
}
