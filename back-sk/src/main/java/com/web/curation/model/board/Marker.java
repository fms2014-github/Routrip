package com.web.curation.model.board;

public class Marker {
	private int markerid;
	private int boardid;
	private String latitude;
	private String longitude;
	private String remark;
	private String overlaytype;
	private double radius;
	private double rx;
	private double ry;
	
	public Marker(int markerid, int boardid, String latitude, String longitude, String remark, String overlaytype,
			double radius, double rx, double ry) {
		super();
		this.markerid = markerid;
		this.boardid = boardid;
		this.latitude = latitude;
		this.longitude = longitude;
		this.remark = remark;
		this.overlaytype = overlaytype;
		this.radius = radius;
		this.rx = rx;
		this.ry = ry;
	}
	public Marker() {
		super();
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
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getRx() {
		return rx;
	}
	public void setRx(double rx) {
		this.rx = rx;
	}
	public double getRy() {
		return ry;
	}
	public void setRy(double ry) {
		this.ry = ry;
	}
	@Override
	public String toString() {
		return "Marker [markerid=" + markerid + ", boardid=" + boardid + ", latitude=" + latitude + ", longitude="
				+ longitude + ", remark=" + remark + ", overlaytype=" + overlaytype + ", radius=" + radius + ", rx="
				+ rx + ", ry=" + ry + "]";
	}
}
