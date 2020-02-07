package com.web.curation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.web.curation.model.user.User;

@Repository//("userDaoImpl")
public class UserDao implements IUserDao{
    @Autowired
    private SqlSession sqlSession;
    private String ns = "usersql.";

    @Override
	public User findUserByEmail(String email, int loginApi) throws Exception {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("email", email);
    	map.put("loginApi", String.valueOf(loginApi));
		return sqlSession.selectOne(ns+"findUserByEmail", map);
	}

    @Override
	public User findUserByEmailAndPassword(String email, String password) throws Exception {
		User user = new User(email, password);
        return sqlSession.selectOne(ns+"findUserByEmailAndPassword", user);
	}

    @Override
	public int addUser(User user) throws Exception {
		return sqlSession.insert(ns+"addUser", user);
	}
	
//	@Override
//	public int changePw(User user) throws Exception {
//		return sqlSession.update(ns+"changePw", user);
//	}
	
	@Override
	public String findPw(User user) throws Exception {
		return sqlSession.selectOne(ns+"findPw", user);
	}

//	@Override
//	public int updateProfileImg(User user) throws Exception {
//		return sqlSession.update(ns+"updateProfileImg", user);
//	}

	@Override
	public int addFollow(int following, int follower) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("following", following);
		map.put("follower", follower);
		return sqlSession.insert(ns+"addFollow", map);
	}

	@Override
	public List<User> getUserList() throws Exception {
		return sqlSession.selectList(ns+"getUserList");
	}

	@Override
	public int deleteFollow(int following, int follower) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("following", following);
		map.put("follower", follower);
		return sqlSession.delete(ns+"deleteFollow", map);
	}

	@Override
	public List<Integer> getFollow(int following) throws Exception {
		return sqlSession.selectList(ns+"getFollow", following);
	}

	@Override
	public User findUserByUid(int uid) throws Exception {
		return sqlSession.selectOne(ns+"findUserByUid", uid);
	}

	@Override
	public List<Integer> getFollower(int follower) throws Exception {
		return sqlSession.selectList(ns+"getFollower", follower);
	}

	@Override
	public int deleteUser(int uid) throws Exception {
		return sqlSession.delete(ns+"deleteUser", uid);
	}

	@Override
	public int updateUserKey(int uid) throws Exception {
		return sqlSession.update(ns+"updateUserKey", uid);
	}

	@Override
	public int changeUserKey(User user) throws Exception {
		return sqlSession.update(ns+"changeUserKey", user);
	}

	@Override
	public List<String> findEmail(User user) throws Exception {
		return sqlSession.selectList(ns+"findEmail", user);
	}


	@Override
	public int addBlackList(int uid, String exp, String jwt) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("exp", exp);
		map.put("jwt", jwt);
		return sqlSession.insert(ns+"addBlackList", map);
	}

	@Override
	public int findBlackList(int uid, String exp) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", String.valueOf(uid));
		map.put("exp", exp);
		return sqlSession.selectOne(ns+"findBlackList", map);
	}

	@Override
	public int deleteBlackList() throws Exception {
		return sqlSession.delete(ns+"deleteBlackList");
	}

	@Override
	public int updateProfile(User user) throws Exception {
		return sqlSession.update(ns+"updateProfile", user);
	}

	@Override
	public List<String> findBlackListByUid(int uid) throws Exception {
		return sqlSession.selectList(ns+"findBlackListByUid", uid);
	}

	@Override
	public User findUserByUserId(String userid, int loginApi) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("loginApi", String.valueOf(loginApi));
		return sqlSession.selectOne(ns+"findUserByUserId", map);
	}

	@Override
	public User findUserSimple(int uid) throws Exception {
		return sqlSession.selectOne(ns+"findUserSimple", uid);
	}
}
