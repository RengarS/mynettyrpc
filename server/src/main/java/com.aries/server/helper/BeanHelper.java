package com.aries.server.helper;


import com.aries.server.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aries on 2017/6/3.
 * Bean助手类
 */
public class BeanHelper {
    /**
     * 定义bean映射（用于存放Bean类与Bean实例的映射关系）
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    public BeanHelper() {
        logger.info("BeanHelp已经启动");
        Set<Class<?>> beanClassSet = ClassHelper.getClassSet();
        for (Class<?> beanClass : beanClassSet) {
            //遍历CLASS_SET,如果某个类被@RpcService注解，把它和它的一个实例放入BEAN_MAP中
            if (beanClass.isAnnotationPresent(RpcService.class)) {
                try {
                    BEAN_MAP.put(beanClass, beanClass.newInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }

            logger.info("IoC容器的Size：" + BEAN_MAP.size());

        }
    }

    /**
     * 获取Bean映射
     *
     * @return
     */
    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    /**
     * 获取Bean实例
     *
     * @param cls
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get com.aries.myFrameWork.bean by class" + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

}
