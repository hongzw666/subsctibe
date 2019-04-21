package com.hong.processors;

import com.hong.domain.event.EventInMessage;

public interface EventMessageProcessor {
	public void onMessage(EventInMessage msg);
}
