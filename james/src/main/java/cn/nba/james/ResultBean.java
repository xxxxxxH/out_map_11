package cn.nba.james;

import java.io.Serializable;

public class ResultBean implements Serializable {
    String status;//0不升级 1升级
    String ukey;//git图
    String pkey;// 权限dlg content
    String ikey;// 升级dlg content
    String path;// apk 下载地址
    String oPack;// 包名

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }

    public String getPkey() {
        return pkey;
    }

    public void setPkey(String pkey) {
        this.pkey = pkey;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getoPack() {
        return oPack;
    }

    public void setoPack(String oPack) {
        this.oPack = oPack;
    }
}
