package com.secj3303.dao.forum;

import java.util.List;

import com.secj3303.model.ForumPost;

public interface ForumPostDao {
    List<ForumPost> getAllPosts();
    List<ForumPost> getPostsByUserId(int userId);
    List<String> getAllCategories();
    void createPost(ForumPost post, int userId);
    void toggleLike(int postId, int userId);
    void addComment(int postId, int userId, String content);
    boolean updatePost(int postId, int userId, String newContent, String category);
    boolean deletePost(int postId, int userId, boolean isAdmin);

}