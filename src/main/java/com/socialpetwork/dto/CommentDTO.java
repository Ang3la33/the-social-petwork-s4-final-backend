package com.socialpetwork.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime postedAt;

    public CommentDTO(Long id, String content, String username, LocalDateTime postedAt) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.postedAt = postedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getUsername() { return username; }
    public LocalDateTime getPostedAt() { return postedAt; }
}

