package com.secj3303.model;


import javax.persistence.*;



@Entity
@Table(name = "post_likes")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    private ForumPost post;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User users;
    
    @Column(name = "liked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date likedAt;

    // Constructors
    public PostLike() {
        this.likedAt = new java.util.Date();
    }
    
    public PostLike(ForumPost post, User users) {
        this.post = post;
        this.users = users;
        this.likedAt = new java.util.Date();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
    
    public User getUsers() { return users; }
    public void setUsers(User users) { this.users = users; }
    
    public java.util.Date getLikedAt() { return likedAt; }
    public void setLikedAt(java.util.Date likedAt) { this.likedAt = likedAt; }
}