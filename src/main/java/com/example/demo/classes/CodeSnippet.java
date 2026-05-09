package com.example.demo.classes;

import java.util.Date;

public class CodeSnippet {
    private int cid;
    private String title;
    private String code;
    private String runtime;
    private String language;
    private String createdBy;
    private String dateCreated;
    private int uid;

    public CodeSnippet(int cid, String title, String code, String runtime, String language, String createdBy, String dateCreated, int uid) {
        this.cid = cid;
        this.title = title;
        this.code = code;
        this.runtime = runtime;
        this.language = language;
        this.createdBy = createdBy;
        this.dateCreated = dateCreated;
        this.uid = uid;
    }

    public int getCid() {
        return cid;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getLanguage() {
        return language;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public int getUid() {
        return uid;
    }
}
