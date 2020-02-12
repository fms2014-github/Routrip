package com.ssafy.dao;

import java.util.List;

import com.ssafy.vo.Alarm;

public interface IAlarmDAO {
	public Alarm findLatestAlarm(int uid);
	List<Alarm> getAlarm(int uid);
}
