package com.hwhl.rm.model;

/**
 * 项目向量实体类
 * 
 * @author Administrator
 * 
 */
public class ProjectVector {
	private String id;
	private String title;
	private String remark;
	private String theType;
	private String projectId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTheType() {
		return theType;
	}
	public void setTheType(String theType) {
		this.theType = theType;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
