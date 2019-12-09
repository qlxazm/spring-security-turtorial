package com.security.securitylearn.condition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class MacCondition implements Condition {
    private static final String MAC = "Mac";
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 获取当前环境信息
        Environment environment = conditionContext.getEnvironment();
        String property = environment.getProperty("os.name");
        if (property.contains(MAC)) {
            log.info("当前的操作系统是Mac Os");
            return true;
        }
        return false;
    }
}
