package com.web.curation.model.board;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.web.curation.model.user.User;

public class Comment {
	private int commentid;
	private int uid;
	private int boardid;
	private String contents;
	private String writedate;
	private int listener = 0;
	private User user;
	private String writeday;// ~전
	private List<Comment> comments = new ArrayList<>();

	public Comment(int commentid, int uid, int boardid, String contents, String writedate, int listener, User user,
			String writeday) {
		super();
		this.commentid = commentid;
		this.uid = uid;
		this.boardid = boardid;
		this.contents = contents;
		this.writedate = writedate;
		this.listener = listener;
		this.user = user;
		this.writeday = writeday;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWriteday() {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHH");
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now1 = format1.parse(format1.format(new Date()));
			Date now2 = format2.parse(format2.format(new Date()));
			Date wd1 = format1.parse(format1.format(format3.parse(this.writedate)));
			Date wd2 = format2.parse(format2.format(format3.parse(this.writedate)));
			if ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60) < 24) {
				if ((now2.getTime() - wd2.getTime()) / 1000 >= 0 && (now2.getTime() - wd2.getTime()) / 1000 < 60) {
					writeday = ((now2.getTime() - wd2.getTime()) / 1000) + "초 전";
				} else if ((now2.getTime() - wd2.getTime()) / (1000 * 60) > 0 && (now2.getTime() - wd2.getTime()) / (1000 * 60) < 60) {
					writeday = ((now2.getTime() - wd2.getTime()) / (1000 * 60)) + "분 전";
				} else {
					writeday = ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60)) + "시간 전";
				}
			} else if ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60 * 24) < 31) {
				writeday = ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60 * 24)) + "일 전";
			} else if (Integer.parseInt(format1.format(now1)) / 1000000
					- Integer.parseInt(format1.format(wd1)) / 1000000 > 0) {
				writeday = (Integer.parseInt(format1.format(now1)) / 1000000
						- Integer.parseInt(format1.format(wd1)) / 1000000) + "년 전";
			} else if (Integer.parseInt(format1.format(now1)) / 10000 - Integer.parseInt(format1.format(wd1)) / 10000 > 0) {
				writeday = (Integer.parseInt(format1.format(now1)) / 10000 - Integer.parseInt(format1.format(wd1)) / 10000)
						+ "달 전";
			}
		} catch (Exception e) {
			System.out.println("writeday 가져오는 동안 오류 발생");
		}

		return writeday;
	}

	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setComment(Comment comment) {
		this.comments.add(comment);
	}

	@Override
	public String toString() {
		return "Comment [commentid=" + commentid + ", uid=" + uid + ", boardid=" + boardid + ", contents=" + contents
				+ ", writedate=" + writedate + ", listener=" + listener + ", user=" + user + ", writeday=" + writeday
				+ ", comments=" + comments + "]";
	}
}
