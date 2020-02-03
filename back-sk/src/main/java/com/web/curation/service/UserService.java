package com.web.curation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.curation.dao.UserDao;
import com.web.curation.model.user.User;

@Service//("userService")
public class UserService implements IUserService{

	@Autowired
	private UserDao userDao;

	@Override
	public User findUserByEmail(String email) throws Exception {
		System.out.println("service   "+email);
		return userDao.findUserByEmail(email);
	}

	@Override
	public User findUserByEmailAndPassword(String email, String password) throws Exception {
		return userDao.findUserByEmailAndPassword(email, password);
	}

	@Override
	public int addUser(User user) throws Exception {
		return userDao.addUser(user);
	}

	@Override
	public int changePw(User user) throws Exception {
		return userDao.changePw(user);
	}

	@Override
	public String findPw(User user) throws Exception {
		return userDao.findPw(user);
	}
	
	@Override
	public int updateProfileImg(User user) throws Exception {
		return userDao.updateProfileImg(user);
	}

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
}
