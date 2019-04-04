package com.hong.weixin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import com.hong.domain.InMessage;
import com.hong.weixin.service.JsonRedisSerializer;
import antlr.debug.MessageEvent;
import antlr.debug.TraceEvent;

@SpringBootApplication
public class SubscribeApplication {
	
	// 相当于Spring的XML配置方式中的<bean>元素
	@Bean
	public RedisTemplate<String, InMessage> inMessageTemplate(//
			@Autowired RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, InMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		//设置序列化程序
//		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new JsonRedisSerializer());
		
		return template;
	}
	@Bean
	public MessageListenerAdapter messageListener(
			@Autowired RedisTemplate<String, InMessage> inMessageTemplate) {
		
		MessageListenerAdapter adapter = new MessageListenerAdapter();
		//共用模板里面的序列化程序
		adapter.setSerializer(inMessageTemplate.getValueSerializer());
		//设置消息处理程序的代理对象
		adapter.setDelegate(this);
		//设置代理对象里面哪个方法用于处理消息，设置方法名
		adapter.setDefaultListenerMethod("handle");
		return adapter;
		
	}
	
	@Bean
	public RedisMessageListenerContainer messageListenerContainer(
			@Autowired RedisConnectionFactory redisConnectionFactory,
			@Autowired MessageListener l) {
		
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		//给容器增加监听器
		MessageListener messageListener = new MessageListener() {
			
			@Override
			public void onMessage(Message message, byte[] pattern) {
				// TODO Auto-generated method stub
				
			}
		};
		
		List<Topic> topics = new ArrayList<>();
		//支持*通配符，监听多个通道
		//topic.add(new ChannelTopic("hongzw_*"));
		//监听具体某个通道
		topics.add(new ChannelTopic("hongzw_event"));
		container.addMessageListener(l, topics);
		
		return container;
	}
	public void handle(InMessage msg) {
		System.out.println("在这里处理收到的消息："+msg);
	}
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(SubscribeApplication.class, args);
		System.out.println("Spring Boot应用启动成功");
		// 要让程序等待 不要退出
		CountDownLatch countDownLatch = new CountDownLatch(1);
		countDownLatch.await();
	}

}
