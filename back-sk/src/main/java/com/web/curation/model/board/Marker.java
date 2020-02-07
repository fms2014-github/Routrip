package com.web.curation.model.board;

public class Marker {
	private int markerid;
	private int boardid;
	private String latitude;
	private String longitude;
	private String remark;
	private String overlaytype;
	private int radius;
	
	public Marker(int markerid, int boardid, String latitude, String longitude, String remark, String overlaytype,
			int radius) {
		super();
		this.markerid = markerid;
		this.boardid = boardid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.remark = remark;
		this.overlaytype = overlaytype;
		this.radius = radius;
	}
	public Marker() {
		super();
	}
	public Marker(int markerid, int boardid, String latitude, String longitude, String remark, String overlaytype) {
		super();
		this.markerid = markerid;
		this.boardid = boardid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.remark = remark;
		this.overlaytype = overlaytype;
	}
	public int getMarkerid() {
		return markerid;
	}
	public void setMarkerid(int markerid) {
		this.markerid = markerid;
	}
	public int getBoardid() {
		return boardid;
	}
	public void setBoardid(int boardid) {
		this.boardid = boardid;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOverlaytype() {
		return overlaytype;
	}
	public void setOverlaytype(String overlaytype) {
		this.overlaytype = overlaytype;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
}
