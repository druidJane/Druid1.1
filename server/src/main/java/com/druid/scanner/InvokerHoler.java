package com.druid.scanner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1115 on 2016/9/22.
 */
public class InvokerHoler {
    /**
     * 命令调用器
     */
    private static Map<Integer, Map<Integer, Invoker>> invokers = new HashMap<>();

    public static void addInvoker(Integer module, Integer cmd, Invoker invoker) {
        Map<Integer, Invoker> map = invokers.get(module);
        if (null == map) {
            map = new HashMap<Integer, Invoker>();
            invokers.put(module,map);
        }
        map.put(cmd, invoker);
    }

    public static Invoker getInvoker(Integer module, Integer cmd) {
        Map<Integer, Invoker> map = invokers.get(module);
        if (map != null) {
            return map.get(cmd);
        }
        return null;
    }
}
