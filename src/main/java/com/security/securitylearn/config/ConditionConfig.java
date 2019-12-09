package com.security.securitylearn.config;

import com.security.securitylearn.bean.SystemBean;
import com.security.securitylearn.condition.MacCondition;
import com.security.securitylearn.condition.WindowsCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author qian
 * @date
 */
@Slf4j
@Configuration
public class ConditionConfig {

    @Bean(name = "windowsBean")
    @Conditional({WindowsCondition.class})
    public SystemBean systemWindows() {
        log.info("实例化了windows实体");
        return new SystemBean("Windows系统", "002");
    }

    @Bean(name = "macBean")
    @Conditional({MacCondition.class})
    public SystemBean systemMac() {
        log.info("实例化了mac实体");
        return new SystemBean("Mac系统", "001");
    }
}
