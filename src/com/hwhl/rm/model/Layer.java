package com.hwhl.rm.model;

/**
 * Created by steven on 2014/4/24.
 */
public class Layer {
    private String id;
    private String layerName;
    private String visible;
    private String projectId;
    private String pageIndex;
    private String belongPage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getBelongPage() {
        return belongPage;
    }

    public void setBelongPage(String belongPage) {
        this.belongPage = belongPage;
    }
}
