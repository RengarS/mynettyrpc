package com.aries.server.utils;

import com.aries.server.domain.RpcRequest1;
import com.aries.server.domain.RpcResponse1;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;


/**
 * @author aries
 */
public class SerializableUtils<T> {
    private static final RuntimeSchema<RpcRequest1> REQUEST_RUNTIME_SCHEMA = RuntimeSchema.createFrom(RpcRequest1.class);
    private static final RuntimeSchema<RpcResponse1> RESPONSE_RUNTIME_SCHEMA = RuntimeSchema.createFrom(RpcResponse1.class);

    /**
     * 将字节数组反序列化成对象
     *
     * @param bytes 字节数组
     * @return 反序列化后的对象
     */
    public static <T> T UnSerializableObject(byte[] bytes, Class<?> clz) {
        if (clz == RpcRequest1.class) {
            RpcRequest1 request = REQUEST_RUNTIME_SCHEMA.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, request, REQUEST_RUNTIME_SCHEMA);
            return (T) request;
        } else if (clz == RpcResponse1.class) {
            RpcResponse1 response = RESPONSE_RUNTIME_SCHEMA.newMessage();
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
        if (clz == RpcRequest1.class) {
            runtimeSchema = REQUEST_RUNTIME_SCHEMA;
        } else if (clz == RpcResponse1.class) {
            runtimeSchema = RESPONSE_RUNTIME_SCHEMA;
        }
        byte[] bytes = ProtobufIOUtil.toByteArray(toBeSerial, runtimeSchema, LinkedBuffer.allocate(LinkedBuffer
                .DEFAULT_BUFFER_SIZE));
        return bytes;
    }
}

