package com.devcourse.be04daangnmarket.post.domain;

import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import com.devcourse.be04daangnmarket.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;

@Entity
@DynamicInsert
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
	private Status status;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime pullUpAt;

	protected Post() {
	}

	private Post(String title, String description, int price, int views, TransactionType transactionType,
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

	public LocalDateTime getPullUpAt() {
		return pullUpAt;
	}

	public void update(String title, String description, int price, int views,
		TransactionType transactionType, Category category, Status status) {

		if (title != null) {
			this.title = title;
		}
		if (description != null) {
			this.description = description;
		}
		if(price>=0){
			this.price = price;
		}
		if(views >=0){
			this.views = views;
		}
		if (transactionType != null) {
			this.transactionType = transactionType;
		}
		if (category != null) {
			this.category = category;
		}
		if (status != null) {
			this.status = status;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String title;
		private String description;
		private int price;
		private int views;
		private TransactionType transactionType;
		private Category category;
		private Status status;
		private LocalDateTime pullUpAt;

		private Builder() {
		}

		public Builder title(String title) {
			this.title = title;

			return this;
		}

		public Builder description(String description) {
			this.description = description;

			return this;
		}

		public Builder price(int price) {
			this.price = price;

			return this;
		}

		public Builder views(int views) {
			this.views = views;

			return this;
		}

		public Builder transactionType(TransactionType transactionType) {
			this.transactionType = transactionType;

			return this;
		}

		public Builder category(Category category) {
			this.category = category;

			return this;
		}

		public Builder status(Status status) {
			this.status = status;

			return this;
		}

		public Builder pullUpAt(LocalDateTime pullUpAt) {
			this.pullUpAt = pullUpAt;

			return this;
		}

		public Post build() {
			return new Post(title, description, price, views, transactionType, category, status, pullUpAt);
		}
	}

}
