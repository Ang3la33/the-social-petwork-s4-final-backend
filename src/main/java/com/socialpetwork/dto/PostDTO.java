package com.socialpetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDTO {

    @JsonProperty("content")
    private String content;

    @JsonProperty("userId")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
