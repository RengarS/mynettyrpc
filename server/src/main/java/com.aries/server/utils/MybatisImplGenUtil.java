package com.aries.server.utils;

import com.aries.server.helper.BeanHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * create by aries   2018-4-8
 * Mybatis 接口自动生成助手
 */
public class MybatisImplGenUtil implements MethodInterceptor {

    private static Map<Class<?>, Object> BEAN_MAP = BeanHelper.getBeanMap();

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<?> clz) {
        Enhancer enhancer = new Enhancer();
        //将其实现接口设置为DAO接口
        enhancer.setInterfaces(new Class[]{clz});
        enhancer.setCallback(new MybatisImplGenUtil());
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        //打开SqlSession
        SqlSession sqlSession = ((SqlSessionFactory) BEAN_MAP.get(SqlSessionFactory.class)).openSession();
        //方法调用结果
        Object res = null;
        //获取mybatis代理
        Object bean = sqlSession.getMapper(o.getClass().getInterfaces()[0]);

        try {
            //保存方法参数类型
            Class<?>[] paramTypes = new Class[objects.length];
            //获取方法参数类型
            for (int i = 0; i < objects.length; i++) {
                paramTypes[i] = objects[i].getClass();
            }
            //根据方法签名获取接口method
            Method interfaceMethod = bean.getClass().getDeclaredMethod(method.getName(), paramTypes);
            //调用接口
            res = interfaceMethod.invoke(bean, objects);
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            throw new RuntimeException(e.getMessage());
        } finally {
            sqlSession.close();
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    public static <P> P getDAOImpl(Class<?> clz) {
        return (P) MybatisImplGenUtil.newInstance(clz);
    }
}
