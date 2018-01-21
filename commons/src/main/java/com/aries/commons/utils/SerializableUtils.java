package com.aries.commons.utils;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author aries
 */
public class SerializableUtils {
    private static final RuntimeSchema<Object> REQUEST_RUNTIME_SCHEMA = RuntimeSchema.createFrom(Object.class);
    private static final RuntimeSchema<Object> RESPONSE_RUNTIME_SCHEMA = RuntimeSchema.createFrom(Object.class);

    private static ConcurrentHashMap<Class, RuntimeSchema> SCHEMA_MAP;

    private static RuntimeSchema getRuntimeSchema(Class<?> clz) {
        RuntimeSchema schema = SCHEMA_MAP.get(clz);
        if (schema != null) {
            return schema;
        } else {
            RuntimeSchema schema1 = RuntimeSchema.createFrom(clz.getClass());
            SCHEMA_MAP.put(clz, schema1);
            return schema1;
        }
    }

    /**
     * 将字节数组反序列化成对象
     *
     * @param bytes 字节数组
     * @return 反序列化后的对象
     */
    public static <T> T UnSerializableObject(byte[] bytes, Class<?> clz) {
        if (clz == Object.class) {
            Object request = REQUEST_RUNTIME_SCHEMA.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, request, REQUEST_RUNTIME_SCHEMA);
            return (T) request;
        } else if (clz == Object.class) {
            Object response = RESPONSE_RUNTIME_SCHEMA.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, response, RESPONSE_RUNTIME_SCHEMA);
            return (T) response;
        }
        return null;
    }

    /**
     * 将对象序列化成byte数组
     *
     * @param toBeSerial 需要序列化的对象
     * @return 对象序列化后的字节数组
     */
    public static <T> byte[] SerializableObject(T toBeSerial, Class<?> clz) {
        RuntimeSchema runtimeSchema = null;
        if (clz == Object.class) {
            runtimeSchema = REQUEST_RUNTIME_SCHEMA;
        } else if (clz == Object.class) {
            runtimeSchema = RESPONSE_RUNTIME_SCHEMA;
        }
        byte[] bytes = ProtobufIOUtil.toByteArray(toBeSerial, runtimeSchema, LinkedBuffer.allocate(LinkedBuffer
                .DEFAULT_BUFFER_SIZE));
        return bytes;
    }
}

