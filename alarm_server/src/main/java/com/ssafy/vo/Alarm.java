package com.ssafy.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alarm {
	private int alarmid;
	private int uid;
	private int boardid;
	private int commentid;
	private int follow;
	private int alarmtype;
	private String nickname;
	private String time;
	private String alarmtime;

	public Alarm(int alarmid, int uid, int boardid, int commentid, int follow, int alarmtype, String nickname, String time) {
		super();
		this.alarmid = alarmid;
		this.uid = uid;
		this.boardid = boardid;
		this.commentid = commentid;
		this.follow = follow;
		this.alarmtype = alarmtype;
		this.nickname = nickname;
		this.time = time;
	}
	public Alarm() {
		super();
	}
	public int getAlarmid() {
		return alarmid;
	}
	public void setAlarmid(int alarmid) {
		this.alarmid = alarmid;
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
	public int getCommentid() {
		return commentid;
	}
	public void setCommentid(int commentid) {
		this.commentid = commentid;
	}
	public int getFollow() {
		return follow;
	}
	public void setFollow(int follow) {
		this.follow = follow;
	}
	public int getAlarmtype() {
		return alarmtype;
	}
	public void setAlarmtype(int alarmtype) {
		this.alarmtype = alarmtype;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAlarmtime() {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHH");
			SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now1 = format1.parse(format1.format(new Date()));
			Date now2 = format2.parse(format2.format(new Date()));
			Date wd1 = format1.parse(format1.format(format3.parse(this.time)));
			Date wd2 = format2.parse(format2.format(format3.parse(this.time)));
			if ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60) < 24) {
				if ((now2.getTime() - wd2.getTime()) / 1000 < 60) {
					alarmtime = ((now2.getTime() - wd2.getTime()) / 1000) + "초 전";
				} else if ((now2.getTime() - wd2.getTime()) / (1000 * 60) < 60) {
					alarmtime = ((now2.getTime() - wd2.getTime()) / (1000 * 60)) + "분 전";
				} else if ((now2.getTime() - wd2.getTime()) / (1000 * 60 * 60) < 24) {
					alarmtime = ((now2.getTime() - wd2.getTime()) / (1000 * 60 * 60)) + "시간 전";
				}
			} else if ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60 * 24) < 31) {
				alarmtime = ((now1.getTime() - wd1.getTime()) / (1000 * 60 * 60 * 24)) + "일 전";
			} else if (Integer.parseInt(format1.format(now1)) / 1000000
					- Integer.parseInt(format1.format(wd1)) / 1000000 > 0) {
				alarmtime = (Integer.parseInt(format1.format(now1)) / 1000000
						- Integer.parseInt(format1.format(wd1)) / 1000000) + "년 전";
			} else if (Integer.parseInt(format1.format(now1)) / 10000 - Integer.parseInt(format1.format(wd1)) / 10000 > 0) {
				alarmtime = (Integer.parseInt(format1.format(now1)) / 10000 - Integer.parseInt(format1.format(wd1)) / 10000)
						+ "달 전";
			}
		} catch (Exception e) {
			System.out.println("writeday 가져오는 동안 오류 발생");
		}
		return alarmtime;
	}
	public void setAlarmtime(String alarmtime) {
		this.alarmtime = alarmtime;
	}
}
