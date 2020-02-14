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
	public Alarm alram(int memberId) throws Exception {//밖에서는 알림 새거가 있는가 없는가 정도만 검사?
		return alarmService.findLatestAlarm(memberId);
	}
	
	@MessageMapping("/infolist")
	@SendToUser("/queue/infolist")
	public List<Alarm> alramlist(String memberId) throws Exception {//infolist 는 그냥 알림 눌렀을 때 다 반환해주는 걸로 할까
		//아니면 봤다 안봤다 칼럼을 만들어서 안본것만 반환?
		return alarmService.getAlarm(Integer.parseInt(memberId));
	}
}
