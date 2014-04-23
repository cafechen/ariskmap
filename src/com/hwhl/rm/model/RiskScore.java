package com.hwhl.rm.model;

/**
 * 风险实体类
 * 
 * @author Administrator
 * 
 */
public class RiskScore {

	private String id;
	private String riskid;
	private String scoreVectorId;
	private String scoreBefore;
	private String scoreEnd;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getScoreVectorId() {
		return scoreVectorId;
	}

	public void setScoreVectorId(String scoreVectorId) {
		this.scoreVectorId = scoreVectorId;
	}

	public String getScoreBefore() {
		return scoreBefore;
	}

	public void setScoreBefore(String scoreBefore) {
		this.scoreBefore = scoreBefore;
	}

	public String getScoreEnd() {
		return scoreEnd;
	}

	public void setScoreEnd(String scoreEnd) {
		this.scoreEnd = scoreEnd;
	}

}
