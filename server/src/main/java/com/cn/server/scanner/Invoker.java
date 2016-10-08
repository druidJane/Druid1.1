package com.cn.server.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**命令执行器
 * Created by 1115 on 2016/9/22.
 */
public class Invoker {
    /**
     * 方法
     */
    private Method method;
    /**
     * 目标对象
     */
    private Object target;

    public static Invoker valueOf(Method method,Object target){
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        return invoker;
    }

    public Object invoke(Object... paramValues){
        try {
            return method.invoke(paramValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
