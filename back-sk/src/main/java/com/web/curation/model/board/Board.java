package com.web.curation.model.board;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private int boardid;
	private int uid;
	private String title;
	private String writedate;
	private String tripterm;
	private String keyword;
	private double latitude;
	private double longitude;
	private int level;
	private int unveiled = 1; //true = 1, false = 0
	private List<Comment> comments = new ArrayList<>();
	private List<Img> imgs = new ArrayList<>();
	private int favoriteNum = 0;
	private int commentNum = 0;
	
	
	public Board(int boardid, int uid, String title, String writedate, String tripterm, String keyword, double latitude,
			double longitude, int level, int unveiled, List<Comment> comments, List<Img> imgs, int favoriteNum, int commentNum) {
		super();
		this.boardid = boardid;
		this.uid = uid;
		this.title = title;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.keyword = keyword;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
		this.comments = comments;
		this.imgs = imgs;
		this.favoriteNum = favoriteNum;
		this.commentNum = commentNum;
	}

	public Board(int boardid, int uid, String title, String writedate, String tripterm, String keyword, double latitude,
			double longitude, int level, int unveiled, List<Comment> comments, List<Img> imgs) {
		super();
		this.boardid = boardid;
		this.uid = uid;
		this.title = title;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.keyword = keyword;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
		this.comments = comments;
		this.imgs = imgs;
	}

	public Board(int boardid, int uid, String title, String writedate, String tripterm, String keyword,
			double latitude, double longitude, int level, int unveiled) {
		super();
		this.boardid = boardid;
		this.uid = uid;
		this.title = title;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.keyword = keyword;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
	}

	public Board() {
		super();
	}

	public Board(int uid, String title, String writedate, String tripterm, String keyword, double latitude,
			double longitude, int level, int unveiled) {
		super();
		this.uid = uid;
		this.title = title;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.keyword = keyword;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
	}

	public Board(int uid, String titile, String writedate, String tripterm, double latitude, double longitude,
			int level, int unveiled) {
		super();
		this.uid = uid;
		this.title = titile;
		this.writedate = writedate;
		this.tripterm = tripterm;
		this.latitude = latitude;
		this.longitude = longitude;
		this.level = level;
		this.unveiled = unveiled;
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
}
