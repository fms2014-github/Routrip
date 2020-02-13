package com.web.curation.model.board;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.web.curation.model.user.User;

public class Board {
	private int boardid;
	private int uid;
	private String title;
	private String writedate;
	private String tripterm;
	private String keyword;
	private List<String> keywords = new ArrayList<>();
	private double latitude;
	private double longitude;
	private int level;
	private int unveiled = 1; // true = 1, false = 0
	private List<Comment> comments = new ArrayList<>();
	private List<Img> imgs = new ArrayList<>();
	private List<User> favorite = new ArrayList<>();
	private int favoriteNum = 0;
	private int commentNum = 0;
	private List<Marker> markers = new ArrayList<>();
	private User user;
	private String writeday;// ~전
	private String content;

	public Board(int boardid, int uid, String title, String writedate, String tripterm, String keyword,
			List<String> keywords, double latitude, double longitude, int level, int unveiled, List<Comment> comments,
			List<Img> imgs, List<User> favorite, int favoriteNum, int commentNum, List<Marker> markers, User user,
			String writeday, String content) {
		super();
		this.boardid = boardid;
		this.uid = uid;
		this.title = title;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.keyword = keyword;
		this.keywords = keywords;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
		this.comments = comments;
		this.imgs = imgs;
		this.favorite = favorite;
		this.favoriteNum = favoriteNum;
		this.commentNum = commentNum;
		this.markers = markers;
		this.user = user;
		this.writeday = writeday;
		this.content = content;
	}

	public Board() {
		super();
	}

	public int getBoardid() {
		return boardid;
	}

	public void setBoardid(int boardid) {
		this.boardid = boardid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWritedate() {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

			writedate = format2.format(format1.parse(writedate));
		} catch (ParseException e) {
			System.out.println("writedsate 가져오는 중 오류 발생");
		}
		return writedate;
	}

	public void setWritedate(String writedate) {
		this.writedate = writedate;
	}

	public String getTripterm() {
		return tripterm;
	}

	public void setTripterm(String tripterm) {
		this.tripterm = tripterm;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getUnveiled() {
		return unveiled;
	}

	public void setUnveiled(int unveiled) {
		this.unveiled = unveiled;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Img> getImgs() {
		return imgs;
	}

	public void setImgs(List<Img> imgs) {
		this.imgs = imgs;
	}

	public int getFavoriteNum() {
		return favoriteNum;
	}

	public void setFavoriteNum(int favoriteNum) {
		this.favoriteNum = favoriteNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public List<User> getFavorite() {
		return favorite;
	}

	public void setFavorite(List<User> favorite) {
		this.favorite = favorite;
	}

	public List<Marker> getMarkers() {
		return markers;
	}

	public void setMarkers(List<Marker> markers) {
		this.markers = markers;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWriteday() {
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			Date now = format1.parse(format1.format(new Date()));
			Date wd = format1.parse(format1.format(format2.parse(this.writedate)));
			if ((now.getTime() == wd.getTime())) {
				writeday = "오늘";
			} else if ((now.getTime() - wd.getTime()) / (1000 * 60 * 60 * 24) < 31) {
				writeday = ((now.getTime() - wd.getTime()) / (1000 * 60 * 60 * 24)) + "일 전";
			} else if (Integer.parseInt(format1.format(now)) / 10000
					- Integer.parseInt(format1.format(wd)) / 10000 > 0) {
				writeday = (Integer.parseInt(format1.format(now)) / 10000
						- Integer.parseInt(format1.format(wd)) / 10000) + "년 전";
			} else if (Integer.parseInt(format1.format(now)) / 100 - Integer.parseInt(format1.format(wd)) / 100 > 0) {
				writeday = (Integer.parseInt(format1.format(now)) / 100 - Integer.parseInt(format1.format(wd)) / 100)
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

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(String keyword) {
		String[] keywordArray = keyword.split(" ");
		for(String s : keywordArray)
			keywords.add(s);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Board [boardid=" + boardid + ", uid=" + uid + ", title=" + title + ", writedate=" + writedate
				+ ", tripterm=" + tripterm + ", keyword=" + keyword + ", keywords=" + keywords + ", latitude="
				+ latitude + ", longitude=" + longitude + ", level=" + level + ", unveiled=" + unveiled + ", comments="
				+ comments + ", imgs=" + imgs + ", favorite=" + favorite + ", favoriteNum=" + favoriteNum
				+ ", commentNum=" + commentNum + ", markers=" + markers + ", user=" + user + ", writeday=" + writeday
				+ ", content=" + content + "]";
	}
}
