package com.hwhl.rm.model;

/**
 * 项目矩阵实体类
 * 
 * @author Administrator
 * 
 */
public class ProjectMatrix {
	private String  id;
	private String  projectId ;
	private String matrix_title ;
	private String matrix_x ;
	private String matrix_y ;
	private String fatherid_matrix ;
	private String xIndex ;
	private String yIndex ;
	private String Color ;
	private String levelType;
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
	public String getMatrix_title() {
		return matrix_title;
	}
	public void setMatrix_title(String matrix_title) {
		this.matrix_title = matrix_title;
	}
	public String getMatrix_x() {
		return matrix_x;
	}
	public void setMatrix_x(String matrix_x) {
		this.matrix_x = matrix_x;
	}
	public String getMatrix_y() {
		return matrix_y;
	}
	public void setMatrix_y(String matrix_y) {
		this.matrix_y = matrix_y;
	}
	public String getFatherid_matrix() {
		return fatherid_matrix;
	}
	public void setFatherid_matrix(String fatherid_matrix) {
		this.fatherid_matrix = fatherid_matrix;
	}
	public String getxIndex() {
		return xIndex;
	}
	public void setxIndex(String xIndex) {
		this.xIndex = xIndex;
	}
	public String getyIndex() {
		return yIndex;
	}
	public void setyIndex(String yIndex) {
		this.yIndex = yIndex;
	}
	public String getColor() {
		return Color;
	}
	public void setColor(String color) {
		Color = color;
	}
	public String getLevelType() {
		return levelType;
	}
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}



}
