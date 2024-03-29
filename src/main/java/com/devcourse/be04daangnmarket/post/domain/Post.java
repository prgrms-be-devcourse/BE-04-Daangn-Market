package com.devcourse.be04daangnmarket.post.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.devcourse.be04daangnmarket.comment.domain.Comment;
import com.devcourse.be04daangnmarket.common.constant.Status;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.PostStatus;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@DynamicInsert
@Table(name = "posts")
public class Post extends BaseEntity {
    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int price;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int views;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'FOR_SALE'")
    private PostStatus postStatus;

    private Long buyerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime pullUpAt;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    protected Post() {
    }

    public Post(Long memberId,
                String title,
                String description,
                int price,
                int views,
                TransactionType transactionType,
                Category category,
                PostStatus postStatus,
                LocalDateTime pullUpAt) {
        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.views = views;
        this.transactionType = transactionType;
        this.category = category;
        this.postStatus = postStatus;
        this.status = Status.ALIVE;
        this.pullUpAt = pullUpAt;
    }

    public Post(Long memberId,
                String title,
                String description,
                int price,
                TransactionType transactionType,
                Category category) {
        this.memberId = memberId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.views = 0;
        this.transactionType = transactionType;
        this.category = category;
        this.postStatus = PostStatus.FOR_SALE;
        this.status = Status.ALIVE;
        this.pullUpAt = LocalDateTime.now();
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getViews() {
        return views;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Category getCategory() {
        return category;
    }

    public PostStatus getPostStatus() {
        return postStatus;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPullUpAt() {
        return pullUpAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void update(String title,
                       String description,
                       int price,
                       TransactionType transactionType,
                       Category category) {

        if (title != null) {
            this.title = title;
        }

        if (description != null) {
            this.description = description;
        }

        if (price >= 0) {
            this.price = price;
        }

        if (transactionType != null) {
            this.transactionType = transactionType;
        }

        if (category != null) {
            this.category = category;
        }
    }

    public void updatePostStatus(PostStatus postStatus) {
        this.postStatus = postStatus;
    }

    public void deleteStatus() {
        this.status = Status.DELETED;
    }

    public void updateView() {
        this.views++;
    }

    public void purchased(Long buyerId) {
        this.postStatus = PostStatus.SOLD;
        this.buyerId = buyerId;
    }
}
