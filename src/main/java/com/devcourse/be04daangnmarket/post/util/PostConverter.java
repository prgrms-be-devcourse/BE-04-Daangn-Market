package com.devcourse.be04daangnmarket.post.util;

import com.devcourse.be04daangnmarket.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;

import java.util.List;

public class PostConverter {
    public static Post toEntity(Long memberId,
                                String title,
                                String description,
                                int price,
                                TransactionType transactionType,
                                Category category) {
        return new Post(
                memberId,
                title,
                description,
                price,
                transactionType,
                category
        );
    }

    public static PostDto.Response toResponse(Post post, List<ImageDto.ImageResponse> images, String username) {
        return new PostDto.Response(
                post.getId(),
                post.getMemberId(),
                username,
                post.getTitle(),
                post.getDescription(),
                post.getPrice(),
                post.getViews(),
                post.getTransactionType().getDescription(),
                post.getCategory().getDescription(),
                post.getPostStatus().getDescription(),
                images,
                post.getBuyerId(),
                post.getCreatedAt()
        );
    }
}
