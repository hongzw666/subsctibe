package com.hong.weixin.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.fasterxml.jackson.core.io.DataOutputAsStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hong.domain.InMessage;

public class JsonRedisSerializer extends Jackson2JsonRedisSerializer<Object>{
	private ObjectMapper objectMapper = new ObjectMapper();
	
	public JsonRedisSerializer() {
		super(Object.class);
	}
	
	//序列化调用的方法，把对象装换为byte[]
	@Override
	public byte[] serialize(Object t) throws SerializationException {
		try {
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();//把数据输出到一个字节数组
			DataOutputStream out = new DataOutputStream(baos);		//把输出流封装成数据输出流
			String className = t.getClass().getName(); //获取类名
			byte[] classNameBytes = className.getBytes("utf-8");
			out.writeInt(classNameBytes.length);//先把类名的长度写出去
			out.write(classNameBytes);//把类名装换得到的字节数组写出
			//使用原本父类的方法，负责把对象装换为字节数组
			byte[] serialize = super.serialize(t);
			out.write(serialize);
			//得到结果数组
			byte[] result = baos.toByteArray();
			return result;
		} catch (Exception e) {
			throw new SerializationException("序列化对象出错"+e.getLocalizedMessage(),e);
		}
	}
//	反序列化调用的方法，把byte[]装换为对象
	@Override
	public InMessage deserialize(byte[] bytes) throws SerializationException {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		DataInputStream inputStream = new DataInputStream(bais);
		//在写的时候，下吧类名的长度传入，此时要先得到类名的长度，再根据类名的长度类读取类名
		try {
			int length = inputStream.readInt();
			byte[] classNameBytes = new  byte[length];
			//把直接数组填满才返回
			inputStream.readFully(classNameBytes);
			//把读取到的字节数组，装换为类名
			String className = new String(classNameBytes,"utf-8");
			//通过类名，加载类对象
			@SuppressWarnings("unchecked")
			Class<? extends InMessage> cla = (Class<? extends InMessage>) Class.forName(className);
			//length+4 表示类名长度加上int长度  ，int的长度是占四个字节的
			return this.objectMapper.readValue(Arrays.copyOfRange(bytes, length+4, bytes.length), cla);
		} catch (Exception e) {
			throw new SerializationException("反序列化对象出错"+e.getLocalizedMessage(),e);
		}
		
//		return super.deserialize(bytes);
	}

	public ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
}
