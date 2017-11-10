package com.aries.server.helper;


import com.aries.server.annotation.Bean;
import com.aries.server.annotation.Service;
import com.aries.server.utils.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
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
        Set<Class<?>> beanClassSet = ClassHelper.getClasseSet();
        for (Class<?> beanClass : beanClassSet) {

            if (beanClass.isAnnotationPresent(Service.class)) {

                Object obj = ReflectionUtil.newInstance(beanClass);
                BEAN_MAP.put(beanClass, obj);
                logger.info(beanClass.getName() + "已经注入到容器");
                //面向接口的支持
                if (beanClass.getInterfaces().length != 0) {
                    Arrays.
                            stream(beanClass.getInterfaces()).
                            parallel().
                            forEach(
                                    ImplInterface -> {
                                        BEAN_MAP.put(
                                                ImplInterface, ReflectionUtil.newInstance(beanClass)
                                        );
                                        logger.info(ImplInterface.getName() + "已经注入到容器");
                                    }
                            );
                }
                //多态的支持
                if (beanClass.getSuperclass() != null && beanClass.getSuperclass() != Object.class) {
                    BEAN_MAP.put(beanClass.getSuperclass(), ReflectionUtil.newInstance(beanClass));
                    logger.info(beanClass.getSuperclass().getName() + "已经注入到容器");
                }
                //@Bean注解的支持
                Arrays.stream(beanClass.getDeclaredMethods()).
                        parallel().
                        filter(method -> method.isAnnotationPresent(Bean.class)).
                        forEach(method -> {
                            BEAN_MAP.put(
                                    method.getReturnType(), ReflectionUtil.invokeMethod(
                                            BEAN_MAP.get(beanClass), method
                                    )
                            );

                            logger.info(beanClass.getName() + "." + method.getName() + "的返回值已经注入到容器（被@Bean注解）");
                        });

            }
        }

        logger.info("IoC容器的Size：" + BEAN_MAP.size());
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
