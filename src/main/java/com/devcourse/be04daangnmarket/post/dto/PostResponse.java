package com.devcourse.be04daangnmarket.post.dto;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;

public record PostResponse(

	Long id,
	String title,
	String description,
	int price,
	int views,
	TransactionType transactionType,
	Category category,
	Status status

) {
}
