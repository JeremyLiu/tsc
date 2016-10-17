package com.jec.module.inteceptor;

import com.jec.base.annotation.SysLog;
import com.jec.module.sysmanage.dao.UserDao;
import com.jec.module.sysmanage.service.SysLogService;
import com.jec.module.sysmanage.service.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Aspect
@Component
public class SystemLogAspect {

    @Resource
    private UserDao userDao;

    @Resource
    private SysLogService sysLogService;

    //Service层切点
    @Pointcut("@annotation(com.jec.base.annotation.SysLog)")
    public void serviceAspect() {
    }

    //Controller层切点
    @Pointcut("@annotation(com.jec.base.annotation.SysLog)")
    public void controllerAspect() {

    }

    @After("serviceAspect()")
    public void after(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        if(userId != null)
            log(joinPoint, userId);
    }

    public static String getServiceMthodDescription(JoinPoint joinPoint)
            throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    description = method.getAnnotation(SysLog.class).description();
                    break;
                }
            }
        }
        return description;
    }

    private void log(JoinPoint joinPoint, int userId){
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        SysLog sysLog = method.getAnnotation(SysLog.class);
                        String description = sysLog.description();
                        String resourceId = sysLog.action();
                        sysLogService.addLog(userId, resourceId, description);
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
