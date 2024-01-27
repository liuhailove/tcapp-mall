package com.tc.mall.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 专题
 *
 * @author honggang.liu
 */
public class CmsSubject implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 专题主图
     */
    @ApiModelProperty(value = "专题主图")
    private String pic;

    /**
     * 关联产品数量
     */
    @ApiModelProperty(value = "关联产品数量")
    private Integer productCount;

    /**
     * 推荐状态
     */
    private Integer recommendStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 收藏数量
     */
    private Integer collectCount;

    /**
     * 浏览数量
     */
    private Integer readCount;

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 画册图片用逗号分割
     */
    @ApiModelProperty(value = "画册图片用逗号分割")
    private String albumPics;

    /**
     * 描述
     */
    private String description;

    /**
     * 显示状态
     */
    @ApiModelProperty(value = "显示状态：0->不显示；1->显示")
    private Integer showStatus;

    /**
     * 转发数
     */
    @ApiModelProperty(value = "转发数")
    private Integer forwardCount;

    /**
     * 专题分类名称
     */
    @ApiModelProperty(value = "专题分类名称")
    private String categoryName;

    /**
     * 内容
     */
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getRecommendStatus() {
        return recommendStatus;
    }

    public void setRecommendStatus(Integer recommendStatus) {
        this.recommendStatus = recommendStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getAlbumPics() {
        return albumPics;
    }

    public void setAlbumPics(String albumPics) {
        this.albumPics = albumPics;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public Integer getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(Integer forwardCount) {
        this.forwardCount = forwardCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CmsSubject{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", productCount=" + productCount +
                ", recommendStatus=" + recommendStatus +
                ", createTime=" + createTime +
                ", collectCount=" + collectCount +
                ", readCount=" + readCount +
                ", commentCount=" + commentCount +
                ", albumPics='" + albumPics + '\'' +
                ", description='" + description + '\'' +
                ", showStatus=" + showStatus +
                ", forwardCount=" + forwardCount +
                ", categoryName='" + categoryName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
