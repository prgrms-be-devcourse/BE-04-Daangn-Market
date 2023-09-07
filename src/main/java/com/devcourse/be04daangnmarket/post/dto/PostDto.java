package com.devcourse.be04daangnmarket.post.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
		Category category,
		List<MultipartFile> files
	) {
	}

	public record UpdateRequest(
		String title,
		String description,
		int price,
		TransactionType transactionType,
		Category category,
		List<MultipartFile> files
	) {
	}

	public record StatusUpdateRequest(
		Status status
	) {
	}

	public record Response(
		Long id,
		Long memberId,
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
