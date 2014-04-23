package com.hwhl.rm.model;

/**
 * 项目实体类
 * 
 * @author Administrator
 * 
 */
public class Project {
	private String id;
	private String fatherId;
	private String belong_department;
	private String title;
	private String isUpload;
	private String AddDate;
	private String isComplete;
	private String show_card;
	private String show_hot;
	private String show_chengben;
	private String show_static;
	private String show_after;
	private String projectid;
	private String remark;
	private String show_sort;
	private String huobi;
//	private String bindArgs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFatherId() {
		return fatherId;
	}

	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	public String getBelong_department() {
		return belong_department;
	}

	public void setBelong_department(String belong_department) {
		this.belong_department = belong_department;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getAddDate() {
		return AddDate;
	}

	public void setAddDate(String addDate) {
		AddDate = addDate;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getShow_card() {
		return show_card;
	}

	public void setShow_card(String show_card) {
		this.show_card = show_card;
	}

	public String getShow_hot() {
		return show_hot;
	}

	public void setShow_hot(String show_hot) {
		this.show_hot = show_hot;
	}

	public String getShow_chengben() {
		return show_chengben;
	}

	public void setShow_chengben(String show_chengben) {
		this.show_chengben = show_chengben;
	}

	public String getShow_static() {
		return show_static;
	}

	public void setShow_static(String show_static) {
		this.show_static = show_static;
	}

	public String getShow_after() {
		return show_after;
	}

	public void setShow_after(String show_after) {
		this.show_after = show_after;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShow_sort() {
		return show_sort;
	}

	public void setShow_sort(String show_sort) {
		this.show_sort = show_sort;
	}

	public String getHuobi() {
		return huobi;
	}

	public void setHuobi(String huobi) {
		this.huobi = huobi;
	}
	
}
