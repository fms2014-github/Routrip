package com.ssafy.service;

import java.util.List;

import com.ssafy.vo.Alarm;

public interface IAlarmService {
	public Alarm findLatestAlarm(int uid);
	List<Alarm> getAlarm(int uid);
}
