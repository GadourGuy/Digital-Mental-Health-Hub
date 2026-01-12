package com.secj3303.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int likeID;

    // IMPORTANT: Must be named 'users' here too
    @ManyToOne
    @JoinColumn(name = "userID")
    private User users;

    @ManyToOne
    @JoinColumn(name = "postID")
    private ForumPost post;

    public PostLike() {}

    public PostLike(User users, ForumPost post) {
        this.users = users;
        this.post = post;
    }

    public int getLikeID() { return likeID; }
    public void setLikeID(int likeID) { this.likeID = likeID; }
    public User getUsers() { return users; }
    public void setUsers(User users) { this.users = users; }
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
}