package com.kafka.demo.self.serializer;

import com.kafka.demo.entity.User;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class UserSerializer implements Serializer<User> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // noting to do
    }

    @Override
    public byte[] serialize(String topic, User data) {
        try {
            if (data==null) return null;
            Integer userId = data.getUserId();
            String username = data.getUsername();
            int length=0;
            byte[] bytes=null;
            if (null!=username){
                bytes=username.getBytes(StandardCharsets.UTF_8);
                length=bytes.length;
            }
            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + length);
            buffer.putInt(userId);
            buffer.putInt(length);
            buffer.put(bytes);
            return buffer.array();
        }catch (Exception e){
            throw new SerializationException("序列化数据异常");
        }
    }

    @Override
    public void close() {
        // noting to do
    }
}
