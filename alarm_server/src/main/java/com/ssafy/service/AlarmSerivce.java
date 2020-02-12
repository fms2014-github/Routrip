package com.ssafy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.dao.AlarmDAO;
import com.ssafy.vo.Alarm;

@Service
public class AlarmSerivce implements IAlarmService {

	@Autowired
	private AlarmDAO alarmdao;
	
	@Override
	public Alarm findLatestAlarm(int uid) {
		return alarmdao.findLatestAlarm(uid);
	}

	@Override
	public List<Alarm> getAlarm(int uid) {
		return alarmdao.getAlarm(uid);
	}

}
