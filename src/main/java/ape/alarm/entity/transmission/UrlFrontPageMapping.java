package ape.alarm.entity.transmission;

import org.bklab.quark.util.json.GsonJsonObjectUtil;

public class UrlFrontPageMapping {

    /**
     * 前台页面ID
     */
    private String id;
    /**
     * 前台页面名称
     */
    private String name;
    /**
     * 前台页面URL
     */
    private String url;


    public UrlFrontPageMapping() {

    }

    public String getId() {
        return id;
    }

    public UrlFrontPageMapping setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UrlFrontPageMapping setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UrlFrontPageMapping setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
