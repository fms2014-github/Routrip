package com.web.curation.service;

import java.util.List;

import com.web.curation.model.user.Alarm;
import com.web.curation.model.user.User;

public interface IUserService {
	User findUserByEmail(String email, int loginApi) throws Exception;
	User findUserNoJoin(String email, int loginApi) throws Exception;
    User findUserByEmailAndPassword(String email, String password) throws Exception;
    int addUser(User user) throws Exception;
    int updateProfile(User user) throws Exception;
    String findPw(User user) throws Exception;
    List<String> findEmail(User user) throws Exception;
    List<User> getUserList() throws Exception;
    User findUserByUid(int uid) throws Exception;
    int deleteUser(int uid) throws Exception;
    int updateUserKey(int uid) throws Exception;
    int changeUserKey(User user) throws Exception;
    User findUserByUserId(String userid, int loginApi) throws Exception;
    User findUserSimple(int uid) throws Exception;
    int deleteUserNoJoin(int uid) throws Exception;
    
    int addFollow(int following, int follower) throws Exception;
    int deleteFollow(int following, int follower) throws Exception;
    List<Integer> getFollow(int following) throws Exception;
    List<Integer> getFollower(int follower) throws Exception;
    
    int addBlackList(int uid, String exp, String jwt) throws Exception;
    int findBlackList(int uid, String exp) throws Exception;
    List<String> findBlackListByUid(int uid) throws Exception;
    int deleteBlackList() throws Exception;
    
    int addAlarm(Alarm alarm) throws Exception;
    List<Alarm> getAlarm(int uid) throws Exception;
    int deleteAlarm(int alarmid) throws Exception;
    int deleteAlarmAll(int uid) throws Exception;
}
