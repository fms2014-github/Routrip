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
	public Alarm alram(Object memberId) throws Exception {//밖에서는 알림 새거가 있는가 없는가 정도만 검사?
		System.out.println(memberId);
		return alarmService.findLatestAlarm((int)memberId);
	}
	
//	@MessageMapping("/info")
//	@SendToUser("/queue/info")
//	public List<Alarm> alram(int memberId) throws Exception {//안 읽은 알림만 반환
//		System.out.println(memberId);
//		return alarmService.getAlarm(memberId);
//	}
}
