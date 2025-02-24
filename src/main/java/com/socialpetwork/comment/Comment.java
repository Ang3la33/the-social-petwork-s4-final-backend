package com.socialpetwork.comment;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Primary Key

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    // Corrected Constructor
    public Comment(Long id, String content, Long userId, Long postId, LocalDateTime postedAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.postId = postId;
        this.postedAt = (postedAt != null) ? postedAt : LocalDateTime.now();
    }

    // No-arg constructor
    public Comment() {

    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getPostedAt() { return postedAt; }
    public void setPostedAt(LocalDateTime postedAt) { this.postedAt = postedAt; }

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
}
