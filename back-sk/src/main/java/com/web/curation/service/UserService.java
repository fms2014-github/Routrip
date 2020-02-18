package com.web.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.curation.dao.UserDao;
import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.User;

@Service
public class UserService implements IUserService{

	@Autowired
	private UserDao userDao;

	@Override
	public User findUserByEmail(String email, int loginApi) throws Exception {
		return userDao.findUserByEmail(email, loginApi);
	}

	@Override
	public User findUserByEmailAndPassword(String email, String password) throws Exception {
		return userDao.findUserByEmailAndPassword(email, password);
	}

	@Override
	public int addUser(User user) throws Exception {
		return userDao.addUser(user);
	}

//	@Override
//	public int changePw(User user) throws Exception {
//		return userDao.changePw(user);
//	}

	@Override
	public String findPw(User user) throws Exception {
		return userDao.findPw(user);
	}
	
//	@Override
//	public int updateProfileImg(User user) throws Exception {
//		return userDao.updateProfileImg(user);
//	}

	@Override
	public int addFollow(int following, int follower) throws Exception {
		return userDao.addFollow(following, follower);
	}

	@Override
	public List<User> getUserList() throws Exception {
		return userDao.getUserList();
	}

	@Override
	public int deleteFollow(int following, int follower) throws Exception {
		return userDao.deleteFollow(following, follower);
	}

	@Override
	public List<Integer> getFollow(int following) throws Exception {
		return userDao.getFollow(following);
	}

	@Override
	public User findUserByUid(int uid) throws Exception {
		return userDao.findUserByUid(uid);
	}

	@Override
	public List<Integer> getFollower(int follower) throws Exception {
		return userDao.getFollower(follower);
	}

	@Override
	public int deleteUser(int uid) throws Exception {
		return userDao.deleteUser(uid);
	}

	@Override
	public int updateUserKey(int uid) throws Exception {
		return userDao.updateUserKey(uid);
	}

	@Override
	public int changeUserKey(User user) throws Exception {
		return userDao.changeUserKey(user);
	}

	@Override
	public List<String> findEmail(User user) throws Exception {
		return userDao.findEmail(user);
	}

	@Override
	public int addBlackList(int uid, String exp, String jwt) throws Exception {
		return userDao.addBlackList(uid, exp, jwt);
	}

	@Override
	public int findBlackList(int uid, String exp) throws Exception {
		return userDao.findBlackList(uid, exp);
	}

	@Override
	public int deleteBlackList() throws Exception {
		return userDao.deleteBlackList();
	}

	@Override
	public int updateProfile(User user) throws Exception {
		return userDao.updateProfile(user);
	}

	@Override
	public List<String> findBlackListByUid(int uid) throws Exception {
		return userDao.findBlackListByUid(uid);
	}

	@Override
	public User findUserByUserId(String userid, int loginApi) throws Exception {
		return userDao.findUserByUserId(userid, loginApi);
	}

	@Override
	public User findUserSimple(int uid) throws Exception {
		return userDao.findUserSimple(uid);
	}

	@Override
	public int addAlarm(Alarm alarm) throws Exception {
		return userDao.addAlarm(alarm);
	}

	@Override
	public List<Alarm> getAlarm(int uid) throws Exception {
		return userDao.getAlarm(uid);
	}

	@Override
	public int deleteAlarm(int alarmid) throws Exception {
		return userDao.deleteAlarm(alarmid);
	}

	@Override
	public int deleteAlarmAll(int uid) throws Exception {
		return userDao.deleteAlarmAll(uid);
	}

	@Override
	public int deleteUserNoJoin(int uid) throws Exception {
		return userDao.deleteUserNoJoin(uid);
	}

	@Override
	public User findUserNoJoin(String email, int loginApi) throws Exception {
		return userDao.findUserNoJoin(email, loginApi);
	}

	@Override
	public int updateAlarm(int uid) throws Exception {
		return userDao.updateAlarm(uid);
	}

	@Override
	public List<User> findUserByLoginApi(int loginApi) throws Exception {
		return userDao.findUserByLoginApi(loginApi);
	}

	@Override
	public List<Alarm> getAlarmNoRead(int uid) throws Exception {
		return userDao.getAlarmNoRead(uid);
	}

	@Override
	public int updateAlarmByAlarmId(int alarmid) throws Exception {
		return userDao.updateAlarmByAlarmId(alarmid);
	}
}
