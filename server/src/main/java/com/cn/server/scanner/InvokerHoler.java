package com.cn.server.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1115 on 2016/9/22.
 */
public class InvokerHoler {
    /**
     * 命令调用器
     */
    private static Map<Short, Map<Short, Invoker>> invokers = new HashMap<>();

    public static void addInvoker(Short module, Short cmd, Invoker invoker) {
        Map<Short, Invoker> map = invokers.get(module);
        if (null == map) {
            map = new HashMap<Short, Invoker>();
        }
        map.put(cmd, invoker);
    }

    public static Invoker getInvoker(Short module, Short cmd) {
        Map<Short, Invoker> map = invokers.get(module);
        if (map != null) {
            return map.get(cmd);
        }
        return null;
    }
}
