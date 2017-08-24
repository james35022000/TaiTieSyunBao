package jcchen.taitiesyunbao.Entity;

/**
 * Created by JCChen on 2017/7/29.
 */

public class ImageAttr {
    private String ImageUrl;
    private String Provider;
    private String OriginalSite;

    public ImageAttr() {}

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getProvider() {
        return Provider;
    }

    public void setProvider(String Provider) {
        if(Provider.equals("null"))
            Provider = "";
        this.Provider = Provider;
    }

    public String getOriginalSite() {
        return OriginalSite;
    }

    public void setOriginalSite(String OriginalSite) {
        if(OriginalSite.equals("null"))
            OriginalSite = "";
        this.OriginalSite = OriginalSite;
    }
}
