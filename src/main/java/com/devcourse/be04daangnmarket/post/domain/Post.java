package com.devcourse.be04daangnmarket.post.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import com.devcourse.be04daangnmarket.image.Image;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@DynamicInsert
@Table(name = "posts")
public class Post extends BaseEntity {

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
	private Status status;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "posts_id")
	private List<Image> images;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime pullUpAt;

	protected Post() {
	}

	public Post(String title, String description, int price, int views, TransactionType transactionType,
		Category category,
		Status status, LocalDateTime pullUpAt) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.views = views;
		this.transactionType = transactionType;
		this.category = category;
		this.status = status;
		this.pullUpAt = pullUpAt;
	}

	public Post(String title, String description, int price, TransactionType transactionType,
		Category category, List<Image> images) {
		this.title = title;
		this.description = description;
		this.price = price;
		this.views = 0;
		this.transactionType = transactionType;
		this.category = category;
		this.images = images;
		this.status = Status.FOR_SALE;
		this.pullUpAt = LocalDateTime.now();
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

	public Status getStatus() {
		return status;
	}

	public List<Image> getImages() {
		return images;
	}

	public LocalDateTime getPullUpAt() {
		return pullUpAt;
	}

	public void update(String title, String description, int price,
		TransactionType transactionType, Category category) {

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

}
