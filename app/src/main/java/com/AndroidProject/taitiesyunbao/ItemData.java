package com.AndroidProject.taitiesyunbao;

/**
 * Created by 石家 on 2017/6/11.
 */

public class ItemData {
    private String title;
    private String content;
    private Integer img_id;

    public ItemData(String title, String content, Integer img_id){

        this.title = title;
        this.content = content;
        this.img_id = img_id;

    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getImgID() {
        return img_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg_id(Integer img_id) {
        this.img_id = img_id;
    }
}
