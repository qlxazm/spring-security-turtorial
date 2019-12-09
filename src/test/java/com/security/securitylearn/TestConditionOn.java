package com.security.securitylearn;

import com.security.securitylearn.bean.SystemBean;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestConditionOn {
    @Autowired(required = false)
    @Qualifier("windowsBean")
    private SystemBean windows;

    @Autowired(required = false)
    @Qualifier("macBean")
    private SystemBean mac;

    @Test
    public void test() {
        if (windows != null) {
            log.info("这是一个windows系统: {}", windows.toString());
        }
        if (mac != null) {
            log.info("这是一个mac系统: {}", mac.toString());
        }
    }
}
