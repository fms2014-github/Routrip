package com.web.curation.service;

import java.util.List;

import com.web.curation.model.board.Board;
import com.web.curation.model.board.Comment;
import com.web.curation.model.board.Img;

public interface IBoardService {
	int addBoard(Board board) throws Exception;
	List<Board> getBoardList() throws Exception;
	List<Board> findBoardListByUid(int uid) throws Exception;
	List<Board> findBoardListByTitle(String title) throws Exception;
	List<Board> findBoardListByWriteDate(String startdate, String enddate) throws Exception;
	Board findBoardByBoardId(int boardid) throws Exception;
	int deleteBoard(int boardid) throws Exception;
	int updateBoard(Board board) throws Exception;
	
    int addFavorite(int uid, int boardid) throws Exception;
    int getFavoriteNum(int boardid) throws Exception;
    List<Integer> getFavoriteByBoard(int boardid) throws Exception;
    List<Integer> getFavoriteByUser(int uid) throws Exception;
    int deleteFavorite(int uid, int boardid) throws Exception;

    int addImg(Img img) throws Exception;
    List<Img> findBoardImg(int boardid) throws Exception;
    List<Img> findRepImg(int boardid) throws Exception;
    int deleteImg(int imgid) throws Exception;
    
    int addComment(Comment comment) throws Exception;
    List<Comment> findComment(int boardid) throws Exception;
    int deleteComment(int commentid) throws Exception;
}
