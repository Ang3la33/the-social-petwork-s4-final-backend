package com.socialpetwork.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialpetwork.auth.jwt.JwtUtil;
import com.socialpetwork.dto.CommentDTO;
import com.socialpetwork.entity.Comment;
import com.socialpetwork.entity.Post;
import com.socialpetwork.entity.User;
import com.socialpetwork.entity.UserType;
import com.socialpetwork.repository.PostRepository;
import com.socialpetwork.repository.UserRepository;
import com.socialpetwork.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private JwtUtil jwtUtil;

    private User createSampleUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test Name");
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setType(UserType.USER); // Assuming UserType.USER exists.
        user.setAbout("About test user");
        user.setBirthday("2000-01-01");
        user.setAvatarUrl("http://avatar.example.com/testuser.png");
        return user;
    }

    private Post createSamplePost() {
        Post post = new Post();
        post.setId(1L);
        post.setContent("This is a test post");
        post.setUser(createSampleUser());
        post.setCreatedAt(LocalDateTime.now());
        post.setImageUrl("http://image.example.com/testpost.png");
        return post;
    }

    private Comment createSampleComment() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("This is a test comment");
        comment.setUser(createSampleUser());
        comment.setPost(createSamplePost());
        comment.setPostedAt(LocalDateTime.now());
        return comment;
    }

    @Test
    public void testGetAllComments_success() throws Exception {
        Comment comment = createSampleComment();
        when(commentService.findAllComments()).thenReturn(List.of(comment));

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(comment.getId()))
                .andExpect(jsonPath("$[0].content").value("This is a test comment"));
    }

    @Test
    public void testGetCommentById_found() throws Exception {
        Comment comment = createSampleComment();
        when(commentService.findCommentById(1L)).thenReturn(comment);

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.content").value("This is a test comment"));
    }

    @Test
    public void testGetCommentById_notFound() throws Exception {
        when(commentService.findCommentById(1L)).thenThrow(new RuntimeException("Comment not found"));

        mockMvc.perform(get("/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommentsByUserId_found() throws Exception {
        Comment comment = createSampleComment();
        User user = createSampleUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentService.findCommentsByUser(user)).thenReturn(List.of(comment));

        mockMvc.perform(get("/comments/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(comment.getId()))
                .andExpect(jsonPath("$[0].content").value("This is a test comment"));
    }

    @Test
    public void testGetCommentsByUserId_notFound() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/comments/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommentsByPostId_found() throws Exception {
        Comment comment = createSampleComment();
        Post post = createSamplePost();
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentService.findCommentsByPost(post)).thenReturn(List.of(comment));

        mockMvc.perform(get("/comments/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(comment.getId()))
                .andExpect(jsonPath("$[0].content").value("This is a test comment"))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    public void testGetCommentsByPostId_notFound() throws Exception {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/comments/post/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateComment_success() throws Exception {
        Comment commentInput = new Comment();
        commentInput.setContent("This is a test comment");
        User user = createSampleUser();
        Post post = createSamplePost();
        commentInput.setUser(user);
        commentInput.setPost(post);

        Comment savedComment = createSampleComment();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentService.createComment(any(Comment.class))).thenReturn(savedComment);

        String json = objectMapper.writeValueAsString(commentInput);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedComment.getId()))
                .andExpect(jsonPath("$.content").value("This is a test comment"));
    }
}


