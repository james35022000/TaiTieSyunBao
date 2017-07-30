package jcchen.taitiesyunbao;

import java.util.Vector;

/**
 * Created by JCChen on 2017/7/27.
 */

public class StoreComment {
    private String UserName;
    private int Resource;
    private String Comment;
    private String Rate;
    private String UserPicUrl;
    private String Time;
    private Vector<String> ImgUrl;

    public StoreComment() {}

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public int getResource() {
        return Resource;
    }

    public void setResource(int Resource) {
        this.Resource = Resource;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        if(Comment.equals("null"))
            this.Comment = "";
        else
            this.Comment = Comment;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String Rate) {
        this.Rate = Rate;
    }

    public String getUserPicUrl() {
        return UserPicUrl;
    }

    public void setUserPicUrl(String UserPicUrl) {
        this.UserPicUrl = UserPicUrl;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public Vector<String> getImgUrl() {
        if(ImgUrl == null)
            ImgUrl = new Vector<>();
        return getImgUrl();
    }

    public void setImgUrl(Vector<String> ImgUrl) {
        this.ImgUrl = ImgUrl;
    }
}
