package com.hwhl.rm.model;


/**
 * 项目向量明细实体类
 * 
 * @author Administrator
 * 
 */
public class ProjectVectorDetail {
	private String id;
	private String fatherid;
	private String levelTitle;
	private String score;
	private String remarkTitle;
	private String remarkContent;
	private String theType;
	private String projectId;
	private String sort;

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

	public String getLevelTitle() {
		return levelTitle;
	}

	public void setLevelTitle(String levelTitle) {
		this.levelTitle = levelTitle;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRemarkTitle() {
		return remarkTitle;
	}

	public void setRemarkTitle(String remarkTitle) {
		this.remarkTitle = remarkTitle;
	}

	public String getRemarkContent() {
		return remarkContent;
	}

	public void setRemarkContent(String remarkContent) {
		this.remarkContent = remarkContent;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

}
