package com.gad;

import com.gad.handler.PlanNoticeHandler;
import com.gad.handler.ResultHandlerTask;
import com.gad.service.CpBizService;
import com.gad.service.CpxzsBizService;
import com.gad.service.NoticeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {


    @Value("${user-name}")
    private String userName;

    @Value("${user-pwd}")
    private String pwd;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 设置定时任务

        Timer timer = new Timer("result-task");
        //一小时查看一次运行结果
        timer.schedule(new ResultHandlerTask(), 1000, 1000 * 60 * 3);

        //在容器加载完毕后获取dao层来操作数据库
        CpBizService bean = event.getApplicationContext().getBean(CpBizService.class);
        CpxzsBizService cpxzsBizService = event.getApplicationContext().getBean(CpxzsBizService.class);
        NoticeService noticeService = event.getApplicationContext().getBean(NoticeService.class);
        bean.login(userName, pwd);
        

    }
}