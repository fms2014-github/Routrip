package com.ssafy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.ssafy.service.AlarmSerivce;
import com.ssafy.vo.Alarm;

@Controller
public class GreetingController {

	@Autowired
	private AlarmSerivce alarmService;

	@MessageMapping("/info")
	@SendToUser("/queue/info")
	public Alarm alram(String message) throws Exception {
		return alarmService.findLatestAlarm(Integer.parseInt(message));
	}
	
	@MessageMapping("/infoes")
	@SendToUser("/topic/info")
	public List<Alarm> alramlist(String message) throws Exception {
		return alarmService.getAlarm(Integer.parseInt(message));
	}
}
