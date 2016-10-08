package com.cn.server.scanner;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * handler扫描器
 * Created by 1115 on 2016/9/22.
 */
@Component
public class HandlerScaner implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<? extends Object> clazz = bean.getClass();
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> interFace : interfaces) {
            SocketModule socketModule = interFace.getAnnotation(SocketModule.class);
            if (socketModule == null) {
                continue;
            }
            Method[] methods = interFace.getMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    SocketCommand socketCommand = method.getAnnotation(SocketCommand.class);
                    if (socketCommand == null) {
                        continue;
                    }
                    final short cmd = socketCommand.cmd();
                    final short module = socketModule.module();
                    if (InvokerHoler.getInvoker(module, cmd) == null) {
                        InvokerHoler.addInvoker(module, cmd, Invoker.valueOf(method, cmd));
                    } else {
                        System.out.println("重复定义模块：" + module + ",命令：" + cmd);
                    }
                }
            }
        }
        return bean;
    }
}
