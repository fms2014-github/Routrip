package com.ssafy.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ssafy.vo.Alarm;

@Repository
public class AlarmDAO implements IAlarmDAO {
	
	@Autowired
	private SqlSession sqlsession;

	@Override
	public Alarm findLatestAlarm(int uid) {
		return sqlsession.selectOne("AlarmMapper.findLatestAlarm",uid);
	}

	@Override
	public List<Alarm> getAlarm(int uid) {
		return sqlsession.selectOne("AlarmMapper.getAlarm",uid);
	}
}
