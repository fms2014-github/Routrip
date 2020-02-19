package com.web.curation.model.user;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class StreamDataSet {
	private User user;
	private SseEmitter sseEmitter;
	public StreamDataSet(User user, SseEmitter sseEmitter) {
		super();
		this.user = user;
		this.sseEmitter = sseEmitter;
	}
	public StreamDataSet() {
		super();
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public SseEmitter getSseEmitter() {
		return sseEmitter;
	}
	public void setSseEmitter(SseEmitter sseEmitter) {
		this.sseEmitter = sseEmitter;
	}
}
