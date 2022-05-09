package ape.alarm.entity.transmission;

import org.bklab.quark.util.json.GsonJsonObjectUtil;

public class UrlMicroServiceMapping {

    /**
     * 前台页面ID
     */
    private String fpId;
    /**
     * 前台微服务页面名称
     */
    private String fpName;
    /**
     * 步骤名称
     */
    private String msName;
    /**
     * 前台微服务ID
     */
    private String msId;
    /**
     * URL
     */
    private String url;


    public UrlMicroServiceMapping() {

    }

    public String getFpId() {
        return fpId;
    }

    public UrlMicroServiceMapping setFpId(String fpId) {
        this.fpId = fpId;
        return this;
    }

    public String getFpName() {
        return fpName;
    }

    public UrlMicroServiceMapping setFpName(String fpName) {
        this.fpName = fpName;
        return this;
    }

    public String getMsName() {
        return msName;
    }

    public UrlMicroServiceMapping setMsName(String msName) {
        this.msName = msName;
        return this;
    }

    public String getMsId() {
        return msId;
    }

    public UrlMicroServiceMapping setMsId(String msId) {
        this.msId = msId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public UrlMicroServiceMapping setUrl(String url) {
        this.url = url;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
