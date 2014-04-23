package com.hwhl.rm.model;

/**
 * 风险实体类
 * 
 * @author Administrator
 * 
 */
public class Risk {
	private String id;
	private String projectId;
	private String pageDetailId;
	private String riskTitle;
	private String riskCode;
	private String riskTypeId;
	private String riskTypeStr;
	private String pageId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getPageDetailId() {
		return pageDetailId;
	}
	public void setPageDetailId(String pageDetailId) {
		this.pageDetailId = pageDetailId;
	}
	public String getRiskTitle() {
		return riskTitle;
	}
	public void setRiskTitle(String riskTitle) {
		this.riskTitle = riskTitle;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getRiskTypeId() {
		return riskTypeId;
	}
	public void setRiskTypeId(String riskTypeId) {
		this.riskTypeId = riskTypeId;
	}
	public String getRiskTypeStr() {
		return riskTypeStr;
	}
	public void setRiskTypeStr(String riskTypeStr) {
		this.riskTypeStr = riskTypeStr;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	

}
