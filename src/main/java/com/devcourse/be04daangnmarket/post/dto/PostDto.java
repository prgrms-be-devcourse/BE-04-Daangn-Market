package com.devcourse.be04daangnmarket.post.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.devcourse.be04daangnmarket.image.dto.ImageResponse;
import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Status;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PostDto {

	public record CreateRequest(

		@NotBlank(message = "제목은 필수 입니다")
		String title,

		String description,

		@Positive(message = "가격은 양수여야 합니다.")
		int price,

		@NotNull(message = "거래 유형은 필수 입니다.")
		TransactionType transactionType,

		@NotNull(message = "카테고리는 필수 입니다.")
		Category category,

		List<MultipartFile> files
	) {
	}

	public record UpdateRequest(
		@NotBlank(message = "제목은 필수 입니다")
		String title,

		String description,

		@Positive(message = "가격은 양수여야 합니다.")
		int price,

		@NotNull(message = "거래 유형은 필수 입니다.")
		TransactionType transactionType,

		@NotNull(message = "카테고리는 필수 입니다.")
		Category category,

		List<MultipartFile> files
	) {
	}

	public record StatusUpdateRequest(
		@NotNull(message = "게시글 상태값은 필수 입니다.")
		Status status
	) {
	}

	public record BuyerUpdateRequest(
		@NotNull(message = "구매자 정보는 필수 입니다.")
		Long buyerId
	) {
	}

	public record Response(
		Long id,
		Long memberId,
		String userName,
		String title,
		String description,
		int price,
		int views,
		String transactionType,
		String category,
		String status,
		List<ImageResponse> images,
		Long buyerId,
		LocalDateTime createdAt
	) {
	}

}
