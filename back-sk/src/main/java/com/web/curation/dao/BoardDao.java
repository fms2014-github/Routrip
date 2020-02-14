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
import com.web.curation.model.board.Marker;

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
	public List<Board> findBoardListByKeyword(String keyword) throws Exception {
		return sqlSession.selectList(ns+"findBoardListByKeyword", keyword);
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

	@Override
	public int updateFavoriteNum(int boardid, int favoriteNum) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("boardid", boardid);
		map.put("favoriteNum", favoriteNum);
		return sqlSession.update(ns+"updateFavoriteNum", map);
	}

	@Override
	public int addMarker(Marker marker) throws Exception {
		return sqlSession.insert(ns+"addMarker", marker);
	}

	@Override
	public List<Marker> findMarker(int boardid) throws Exception {
		return sqlSession.selectList(ns+"findMarker", boardid);
	}

	@Override
	public int updateMarker(Marker marker) throws Exception {
		return sqlSession.update(ns+"updateMarker", marker);
	}

	@Override
	public int deleteMarker(int markerid) throws Exception {
		return sqlSession.delete(ns+"deleteMarker", markerid);
	}

	@Override
	public Comment findCommentByCommentid(int commentid) throws Exception {
		return sqlSession.selectOne(ns+"findCommentByCommentid", commentid);
	}

	@Override
	public List<Board> findBoardByFollow(int following) throws Exception {
		return sqlSession.selectList(ns+"findBoardByFollow", following);
	}

	@Override
	public int addScrap(int uid, int boardid) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("uid", uid);
		map.put("boardid", boardid);
		return sqlSession.insert(ns+"addScrap", map);
	}

	@Override
	public List<Integer> getScrap(int uid) throws Exception {
		return sqlSession.selectList(ns+"getScrap", uid);
	}

	@Override
	public int deleteScrap(int uid, int boardid) throws Exception {
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("uid", uid);
		map.put("boardid", boardid);
		return sqlSession.delete(ns+"deleteScrap", map);
	}

	@Override
	public List<Comment> findCommentByUid(int uid) throws Exception {
		return sqlSession.selectList(ns+"findCommentByUid", uid);
	}

	@Override
	public List<Comment> findCommentByListener(int listener) throws Exception {
		return sqlSession.selectList(ns+"findCommentByListener", listener);
	}

	@Override
	public List<Board> getBoardList5(String writedate) throws Exception {
		return sqlSession.selectList(ns+"getBoardList5", writedate);
	}

	@Override
	public List<Board> findBoardBest() throws Exception {
		return sqlSession.selectList(ns+"findBoardBest");
	}
}
