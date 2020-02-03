package com.web.curation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.web.curation.model.board.Board;
import com.web.curation.model.board.Comment;
import com.web.curation.model.board.Img;

@Repository
public class BoardDao implements IBoardDao{
	
	@Autowired
	private SqlSession sqlSession;
	private String ns = "boardsql.";

	@Override
	public int addBoard(Board board) throws Exception {
		return sqlSession.insert(ns+"addBoard", board);
	}

	@Override
	public List<Board> getBoardList() throws Exception {
		return sqlSession.selectList(ns+"getBoardList");
	}

	@Override
	public List<Board> findBoardListByUid(int uid) throws Exception {
		return sqlSession.selectList(ns+"findBoardListByUid", uid);
	}

	@Override
	public List<Board> findBoardListByTitle(String title) throws Exception {
		return sqlSession.selectList(ns+"findBoardListByTitle", title);
	}

	@Override
	public List<Board> findBoardListByWriteDate(String startdate, String enddate) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startdate", startdate);
		map.put("enddate", enddate);
		return sqlSession.selectList(ns+"findBoardListByWriteDate", map);
	}

	@Override
	public Board findBoardByBoardId(int boardid) throws Exception {
		return sqlSession.selectOne(ns+"findBoardByBoardId", boardid);
	}

	@Override
	public int deleteBoard(int boardid) throws Exception {
		return sqlSession.delete(ns+"deleteBoard", boardid);
	}

	@Override
	public int updateBoard(Board board) {
		return sqlSession.update(ns+"updateBoard", board);
	}


	@Override
	public int addFavorite(int uid, int boardid) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("uid", uid);
		map.put("boardid", boardid);
		return sqlSession.insert(ns+"addFavorite", map);
	}

	@Override
	public int getFavoriteNum(int boardid) throws Exception {
		return sqlSession.selectOne(ns+"getFavoriteNum", boardid);
	}

	@Override
	public List<Integer> getFavoriteByBoard(int boardid) throws Exception {
		return sqlSession.selectList(ns+"getFavoriteByBoard", boardid);
	}

	@Override
	public List<Integer> getFavoriteByUser(int uid) throws Exception {
		return sqlSession.selectList(ns+"getFavoriteByUser", uid);
	}

	@Override
	public int deleteFavorite(int uid, int boardid) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("uid", uid);
		map.put("boardid", boardid);
		return sqlSession.delete(ns+"deleteFavorite", map);
	}

	@Override
	public int addImg(Img img) throws Exception {
		return sqlSession.insert(ns+"addImg", img);
	}

	@Override
	public List<Img> findBoardImg(int boardid) throws Exception {
		return sqlSession.selectList(ns+"findBoardImg", boardid);
	}

	@Override
	public List<Img> findRepImg(int boardid) throws Exception {
		return sqlSession.selectList(ns+"findRepImg", boardid);
	}

	@Override
	public int deleteImg(int imgid) throws Exception {
		return sqlSession.delete(ns+"deleteImg", imgid);
	}

	@Override
	public int addComment(Comment comment) throws Exception {
		return sqlSession.insert(ns+"addComment", comment);
	}

	@Override
	public List<Comment> findComment(int boardid) throws Exception {
		return sqlSession.selectList(ns+"findComment", boardid);
	}

	@Override
	public int deleteComment(int commentid) throws Exception {
		return sqlSession.delete(ns+"deleteComment", commentid);
	}
}
