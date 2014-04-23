package com.hwhl.rm.model;

/**
 * 目录项实体类
 * 
 * @author Administrator
 * 
 */
public class Directory {
	private String id;
	private String fatherid;
	private String title;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFatherid() {
		return fatherid;
	}
	public void setFatherid(String fatherid) {
		this.fatherid = fatherid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
