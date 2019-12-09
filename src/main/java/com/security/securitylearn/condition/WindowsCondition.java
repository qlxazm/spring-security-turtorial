package com.security.securitylearn.condition;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 继承了Conditionfan方法，用于在@Conditional注解上根据条件判断
 * 来动态加载类
 */
@Slf4j
public class WindowsCondition implements Condition {
    private static final String SYSTEM_WINDOWS = "Windows";
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        // 获取容器使用的BeanFactory
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        // 获取类加载器
        ClassLoader classLoader = conditionContext.getClassLoader();
        // 获取Bean定义的注册类
        BeanDefinitionRegistry registry = conditionContext.getRegistry();
        // 获取当前环境信息
        Environment environment = conditionContext.getEnvironment();

        String property = environment.getProperty("os.name");
        if (property.contains(SYSTEM_WINDOWS)) {
            log.info("当前的操作系统是Windows");
            return true;
        }
        return false;
    }
}
