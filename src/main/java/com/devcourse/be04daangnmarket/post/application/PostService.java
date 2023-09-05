package com.devcourse.be04daangnmarket.post.application;

import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

import java.util.List;
import java.util.NoSuchElementException;

import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.DomainName;
import com.devcourse.be04daangnmarket.image.dto.ImageResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
@Transactional(readOnly = true)
public class PostService {

	private final PostRepository postRepository;
	private final ImageService imageService;

	public PostService(PostRepository postRepository, ImageService imageService) {
		this.postRepository = postRepository;
		this.imageService = imageService;
	}

	@Transactional
	public PostDto.Response create(Long memberId, String title, String description, int price,
		TransactionType transactionType, Category category, List<MultipartFile> files) throws IOException {
		Post post = new Post(memberId, title, description, price, transactionType, category);
		postRepository.save(post);
		List<ImageResponse> images = imageService.uploadImages(files, DomainName.POST, post.getId());

		return toResponse(post, images);
	}

	@Transactional
	public PostDto.Response getPost(Long id, HttpServletRequest req, HttpServletResponse res) {
		Post post = findPostById(id);
		if (!isViewed(id, req, res)) {
			post.updateView();
			postRepository.save(post);
		}
		List<ImageResponse> images = imageService.getImages(DomainName.POST, id);

		return toResponse(post, images);
	}

	public Page<PostDto.Response> getAllPost(Pageable pageable) {
		List<Post> posts = postRepository.findAll();
		List<PostDto.Response> postResponses = new ArrayList<>();

		for (Post post : posts) {
			List<ImageResponse> images = imageService.getImages(DomainName.POST, post.getId());
			PostDto.Response postResponse = new PostDto.Response(post.getId(), post.getMemberId(), post.getTitle(),
				post.getDescription(),
				post.getPrice(), post.getViews(), post.getTransactionType().name(), post.getCategory().name(),
				post.getStatus().name(), images);
			postResponses.add(postResponse);
		}

		return new PageImpl<>(postResponses, pageable, postResponses.size());
	}

	public Page<PostDto.Response> getPostByCategory(Category category, Pageable pageable) {
		List<Post> posts = postRepository.findByCategory(category, pageable);
		List<PostDto.Response> postResponses = new ArrayList<>();

		for (Post post : posts) {
			List<ImageResponse> images = imageService.getImages(DomainName.POST, post.getId());
			PostDto.Response postResponse = new PostDto.Response(post.getId(), post.getMemberId(), post.getTitle(),
				post.getDescription(),
				post.getPrice(), post.getViews(), post.getTransactionType().name(), post.getCategory().name(),
				post.getStatus().name(), images);
			postResponses.add(postResponse);
		}

		return new PageImpl<>(postResponses, pageable, postResponses.size());
	}

	public Page<PostDto.Response> getPostByMemberId(Long memberId, Pageable pageable) {
		List<Post> posts = postRepository.findByMemberId(memberId, pageable);
		List<PostDto.Response> postResponses = new ArrayList<>();

		for (Post post : posts) {
			List<ImageResponse> images = imageService.getImages(DomainName.POST, post.getId());
			PostDto.Response postResponse = new PostDto.Response(post.getId(), post.getMemberId(), post.getTitle(),
				post.getDescription(),
				post.getPrice(), post.getViews(), post.getTransactionType().name(), post.getCategory().name(),
				post.getStatus().name(), images);
			postResponses.add(postResponse);
		}

		return new PageImpl<>(postResponses, pageable, postResponses.size());
	}

	public Page<PostDto.Response> getPostByKeyword(String keyword, Pageable pageable) {
		List<Post> posts = postRepository.findByTitleContaining(keyword, pageable);
		List<PostDto.Response> postResponses = new ArrayList<>();

		for (Post post : posts) {
			List<ImageResponse> images = imageService.getImages(DomainName.POST, post.getId());
			PostDto.Response postResponse = new PostDto.Response(post.getId(), post.getMemberId(), post.getTitle(),
				post.getDescription(),
				post.getPrice(), post.getViews(), post.getTransactionType().name(), post.getCategory().name(),
				post.getStatus().name(), images);
			postResponses.add(postResponse);
		}

		return new PageImpl<>(postResponses, pageable, postResponses.size());
	}

	@Transactional
	public PostDto.Response update(Long id, String title, String description, int price,
		TransactionType transactionType, Category category, List<MultipartFile> files) {
		Post post = findPostById(id);
		post.update(title, description, price, transactionType, category);

		imageService.deleteAllImages(DomainName.POST, id);
		List<ImageResponse> images = imageService.uploadImages(files, DomainName.POST, id);

		return toResponse(post, images);
	}

	private Post findPostById(Long id) {
		return postRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_POST.getMessage()));
	}

	@Transactional
	public void delete(Long id) {
		postRepository.deleteById(id);
		imageService.deleteAllImages(DomainName.POST, id);
	}

	private PostDto.Response toResponse(Post post, List<ImageResponse> images) {
		return new PostDto.Response(
			post.getId(),
			post.getMemberId(),
			post.getTitle(),
			post.getDescription(),
			post.getPrice(),
			post.getViews(),
			post.getTransactionType().getDescription(),
			post.getCategory().getDescription(),
			post.getStatus().getDescription(),
			images
		);
	}

	private boolean isViewed(Long id, HttpServletRequest req, HttpServletResponse res) {
		String cookieName = Long.toString(id);
		String cookieValue = Long.toString(id);

		Cookie[] cookies = req.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies)
				if (cookie.getName().contains(cookieName))
					return true;
		}

		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(60);
		res.addCookie(cookie);

		return false;
	}
}
