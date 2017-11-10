package com.aries.server.helper;


import com.aries.server.AriesRpcFrameWork;
import com.aries.server.annotation.Service;
import com.aries.server.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/3.
 * 类操作助手类
 */
public final class ClassHelper {
    /**
     * 定义类集合（用于存放所有被加载的类）
     */
    private static Set<Class<?>> CLASS_SET = null;
    private static final Logger logger = LoggerFactory.getLogger(ClassHelper.class);


    public ClassHelper() {
        logger.info("ClassHelper已经启动");
        String basePackage = AriesRpcFrameWork.getClassPath();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    /**
     * 获取应用包名下所有类
     */
    public static Set<Class<?>> getClasseSet() {
        return CLASS_SET;
    }

    /**
     * 获取应用包下所有service类
     *
     * @return
     */
    public static Set<Class<?>> getServiceClassSet() {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                classSet.add(cls);
            }
        }
        return classSet;
    }

}
