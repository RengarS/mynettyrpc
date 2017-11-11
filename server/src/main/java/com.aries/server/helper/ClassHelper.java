package com.aries.server.helper;


import com.aries.server.AriesRpcFrameWork;
import com.aries.server.utils.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        String basePackage = AriesRpcFrameWork.getPackageName();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
        System.out.println(CLASS_SET.size());
    }

    /**
     * 获取应用包名下所有类
     */
    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }
}
