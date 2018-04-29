package com.frank.markdowneditor;

import org.litepal.crud.DataSupport;

public class Article extends DataSupport {
    private int    id;
    private String title;
    private String content;
    private Long   time;

    public Article() { }
    public Article(String title, String content, Long time) {
        this.title   = title;
        this.content = content;
        this.time    = time;
    }
    public Article(int id,String title, String content, Long time) {
        this.id      = id;
        this.title   = title;
        this.content = content;
        this.time    = time;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getTime() { return time; }
    public void setTime(Long time) { this.time = time; }
}
