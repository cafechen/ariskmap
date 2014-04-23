package com.hwhl.rm.model;

public class RiskRelation {
	private String id;
	private String projectid;
	private String riskFrom;
	private String riskTo;
	private String relationRemark;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectid() {
		return projectid;
	}
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}
	public String getRiskFrom() {
		return riskFrom;
	}
	public void setRiskFrom(String riskFrom) {
		this.riskFrom = riskFrom;
	}
	public String getRiskTo() {
		return riskTo;
	}
	public void setRiskTo(String riskTo) {
		this.riskTo = riskTo;
	}
	public String getRelationRemark() {
		return relationRemark;
	}
	public void setRelationRemark(String relationRemark) {
		this.relationRemark = relationRemark;
	}
	

}
