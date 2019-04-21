package com.hong.processors.impl;

import org.springframework.stereotype.Service;

import com.hong.domain.event.EventInMessage;
import com.hong.processors.EventMessageProcessor;

@Service("subscribeEventMessageProcessor")
public class SubscribeEventMessageProcessor implements EventMessageProcessor {

	@Override
	public void onMessage(EventInMessage msg) {
		// 1.检查用户是否已经关注
		// 2.如果用户还未关注，则调用远程接口获取用户信息
		// 3.获取用户信息之前，需要先获取访问令牌
		// 4.调用远程接口
		// 5.存储到数据库

	}

}
