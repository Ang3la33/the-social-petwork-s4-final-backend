package com.socialpetwork.comment;

import com.socialpetwork.post.Post;
import com.socialpetwork.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "posted_at", nullable = false)
    private LocalDateTime postedAt;

    public Comment() {
        // Default constructor
    }

    public Comment(Long id, String content, User user, Post post, LocalDateTime postedAt) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.post = post;
        this.postedAt = postedAt != null ? postedAt : LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.postedAt == null) {
            this.postedAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(LocalDateTime postedAt) {
        this.postedAt = postedAt;
    }
}
