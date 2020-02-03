package com.web.curation.model.board;

public class Comment {
	private int commentid;
	private int uid;
	private int boardid;
	private String contents;
	private String writedate;
	private int listener = 0;
	
	public Comment(int commentid, int uid, int boardid, String contents, String writedate, int listener) {
		super();
		this.commentid = commentid;
		this.uid = uid;
		this.boardid = boardid;
		this.contents = contents;
		this.writedate = writedate;
		this.listener = listener;
	}

	public Comment() {
		super();
	}

	public int getCommentid() {
		return commentid;
	}

	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getBoardid() {
		return boardid;
	}

	public void setBoardid(int boardid) {
		this.boardid = boardid;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getWritedate() {
		return writedate;
	}

	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}

	public int getListener() {
		return listener;
	}

	public void setListener(int listener) {
		this.listener = listener;
	}
}
