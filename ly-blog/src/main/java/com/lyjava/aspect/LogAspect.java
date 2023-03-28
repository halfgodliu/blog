package com.lyjava.aspect;

import com.alibaba.fastjson.JSON;
import com.lyjava.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用来打印接口日志的通知类
 */
@Component
@Aspect//交给AspectJ的框架去识别切面类
@Slf4j
public class LogAspect {

    //后面通知的值就可以直接写pt()，在加了@SystemLog注解的方法上面添加通知
    @Pointcut("@annotation(com.lyjava.annotation.SystemLog)")
    public void pt(){}

    /**
     * 用法有点类似过滤器
     * 通知方法里面必须调用joinPoint.proceed()方法，类似过滤器
     * @param joinPoint 包含了被增强方法
     * @return 需要返回被增强方法的返回值
     */
    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            //前切功能，在被增强方法执行前调用自定义的方法
            handleBefore(joinPoint);

            //必须要调用，否则被增强方法就不会执行了，返回值就是被增强方法的返回值
            result = joinPoint.proceed();

            //后切功能，在被增强方法执行后调用自定义的方法
            handleAfter(result);
        } finally {
            log.info("=======End=======" + System.lineSeparator());//后面的那个是换行符
        }
        //返回
        return result;
    }

    /**
     * 在环绕通知里面调用，代替前置通知
     * @param joinPoint
     */
    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //获取请求对象
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //获取被增强方法上的注解对象（包含了描述信息）
        SystemLog systemLog = getSystemLog(joinPoint);

        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}",JSON.toJSONString(joinPoint.getArgs()));
    }

    /**
     * 获取被增强方法上的注解对象
     * @param joinPoint
     * @return
     */
    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        //获取切面对象，包括被增强方法的整个内容（包括上面的注解）
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //从切面对象里面获取被增强方法，然后在获取其上的（SystemLog）注解
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);
        //返回被增强方法上的注解对象
        return systemLog;
    }

    /**
     * 在环绕通知里面调用，代替后置通知
     * @param result
     */
    private void handleAfter(Object result) {
        // 打印出参
        log.info("Response       : {}", JSON.toJSONString(result));
    }
}
