package com.web.curation.model.board;

public class Img {
	private int imgid;
	private int boardid;
	private String src;
	private int rep = 1;//true=1, false=0
	
	public Img(int imgid, int boardid, String src, int rep) {
		super();
		this.imgid = imgid;
		this.boardid = boardid;
		this.src = src;
		this.rep = rep;
	}
	public Img() {
		super();
	}
	public Img(int imgid, int boardid, String src) {
		super();
		this.imgid = imgid;
		this.boardid = boardid;
		this.src = src;
	}
	public int getImgid() {
		return imgid;
	}
	public void setImgid(int imgid) {
		this.imgid = imgid;
	}
	public int getBoardid() {
		return boardid;
	}
	public void setBoardid(int boardid) {
		this.boardid = boardid;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public int getRep() {
		return rep;
	}
	public void setRep(int rep) {
		this.rep = rep;
	}
	@Override
	public String toString() {
		return "Img [imgid=" + imgid + ", boardid=" + boardid + ", src=" + src + ", rep=" + rep + "]";
	}
}
