package com.devcourse.be04daangnmarket.post.dto;

import java.util.List;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;

public class PostDto {

	public record CreateRequest(
		String title,
		String description,
		int price,
		TransactionType transactionType,
		Category category
	) {
	}

	public record UpdateRequest(
		String title,
		String description,
		int price,
		TransactionType transactionType,
		Category category
	) {
	}

	public record Response(
		Long id,
		String title,
		String description,
		int price,
		int views,
		String transactionType,
		String category,
		String status,
		List<ImageResponse> images
	) {
	}

}
