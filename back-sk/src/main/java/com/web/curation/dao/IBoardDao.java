package com.web.curation.dao;

import java.util.List;

import com.web.curation.model.board.Board;
import com.web.curation.model.board.Comment;
import com.web.curation.model.board.Img;
import com.web.curation.model.board.Marker;

public interface IBoardDao {
	int addBoard(Board board) throws Exception;
	List<Board> getBoardList() throws Exception;
	List<Board> getBoardList5(String writedate) throws Exception;
	List<Board> findBoardListByUid(int uid) throws Exception;
	List<Board> findBoardListByKeyword(String keyword) throws Exception;
	List<Board> findBoardListByWriteDate(String startdate, String enddate) throws Exception;
	Board findBoardByBoardId(int boardid) throws Exception;
	int deleteBoard(int boardid) throws Exception;
	int updateBoard(Board board) throws Exception;
	int updateFavoriteNum(int boardid, int favoriteNum) throws Exception;
	
	int addFavorite(int uid, int boardid) throws Exception;
    int getFavoriteNum(int boardid) throws Exception;
    List<Integer> getFavoriteByBoard(int boardid) throws Exception;
    List<Integer> getFavoriteByUser(int uid) throws Exception;
    int deleteFavorite(int uid, int boardid) throws Exception;
    
    int addScrap(int uid, int boardid) throws Exception;
    List<Integer> getScrap(int uid) throws Exception;
    int deleteScrap(int uid, int boardid) throws Exception;
    
    int addImg(Img img) throws Exception;
    List<Img> findBoardImg(int boardid) throws Exception;
    int deleteImg(int imgid) throws Exception;
    
    int addComment(Comment comment) throws Exception;
    List<Comment> findComment(int boardid) throws Exception;
    List<Comment> findCommentByUid(int uid) throws Exception;
    List<Comment> findCommentByListener(int listener) throws Exception;
    Comment findCommentByCommentid(int commentid) throws Exception;
    int deleteComment(int commentid) throws Exception;
    
    int addMarker(Marker marker) throws Exception;
    List<Marker> findMarker(int boardid) throws Exception;
    int updateMarker(Marker marker) throws Exception;
    int deleteMarker(int markerid) throws Exception;
    
    List<Board> findBoardByFollow(int following) throws Exception;
}
