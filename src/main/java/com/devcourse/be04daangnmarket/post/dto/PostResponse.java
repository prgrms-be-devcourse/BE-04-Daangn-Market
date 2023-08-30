package com.devcourse.be04daangnmarket.post.dto;

import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;

public record PostResponse(

	Long id,
	String title,
	int price,
	int views,
	TransactionType transactionType,
	Status status

) {
}
