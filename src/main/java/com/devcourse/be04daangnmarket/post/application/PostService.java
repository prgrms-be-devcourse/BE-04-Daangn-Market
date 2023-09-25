package com.devcourse.be04daangnmarket.post.application;

import com.devcourse.be04daangnmarket.common.aop.lock.DistributedRock;
import com.devcourse.be04daangnmarket.common.image.dto.ImageDto;
import com.devcourse.be04daangnmarket.image.application.ImageService;
import com.devcourse.be04daangnmarket.image.domain.constant.DomainName;
import com.devcourse.be04daangnmarket.member.application.ProfileService;
import com.devcourse.be04daangnmarket.post.domain.Post;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.PostStatus;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;
import com.devcourse.be04daangnmarket.post.repository.PostRepository;
import com.devcourse.be04daangnmarket.post.util.PostConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static com.devcourse.be04daangnmarket.post.exception.ErrorMessage.*;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final ProfileService profileService;
    private final RedisTemplate<String, String> redisTemplate;

    public PostService(PostRepository postRepository,
                       ImageService imageService,
                       ProfileService profileService,
                       RedisTemplate<String, String> redisTemplate) {
        this.postRepository = postRepository;
        this.imageService = imageService;
        this.profileService = profileService;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public PostDto.Response create(Long memberId,
                                   String title,
                                   String description,
                                   int price,
                                   TransactionType transactionType,
                                   Category category,
                                   List<ImageDto.ImageDetail> files) throws IOException {
        Post post = PostConverter.toEntity(
                memberId,
                title,
                description,
                price,
                transactionType,
                category
        );

        postRepository.save(post);

        List<String> imagePaths = imageService.save(files, DomainName.POST, post.getId());
        String username = getUsername(post.getMemberId());

        return PostConverter.toResponse(post, imagePaths, username);
    }

    @DistributedRock
    public PostDto.Response getPost(Long postId, Long userId) {
        Post post = findPostById(postId);

        if (!isViewed(postId, userId)) {
            post.updateView();
        }

        List<String> imagePaths = imageService.getImages(DomainName.POST, postId);
        String username = getUsername(post.getMemberId());

        return PostConverter.toResponse(post, imagePaths, username);
    }

    public Page<PostDto.Response> getAllPost(Pageable pageable) {
        return postRepository.findAll(pageable).map(post -> {
            List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
            String username = getUsername(post.getMemberId());

            return PostConverter.toResponse(post, imagePaths, username);
        });
    }

    public Slice<PostDto.Response> getPostsWithCursorWithFilters(PostDto.FilterRequest request, Pageable pageable) {
        return postRepository.findPostsWithCursorWithFilters(
                        request.id(),
                        request.createdAt(),
                        request.category(),
                        request.memberId(),
                        request.buyerId(),
                        request.keyword(),
                        pageable)
                .map(post -> {
                    List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
                    String username = getUsername(post.getMemberId());

                    return PostConverter.toResponse(post, imagePaths, username);
                });
    }

    public Page<PostDto.Response> getPostByCategory(Category category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable).map(post -> {
            List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
            String username = getUsername(post.getMemberId());

            return PostConverter.toResponse(post, imagePaths, username);
        });
    }

    public Page<PostDto.Response> getPostByMemberId(Long memberId, Pageable pageable) {
        return postRepository.findByMemberId(memberId, pageable).map(post -> {
            List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
            String username = getUsername(post.getMemberId());

            return PostConverter.toResponse(post, imagePaths, username);
        });
    }

    public Page<PostDto.Response> getPostByKeyword(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable).map(post -> {
            List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
            String username = getUsername(post.getMemberId());

            return PostConverter.toResponse(post, imagePaths, username);
        });
    }

    public Page<PostDto.Response> getPostByBuyerId(Long buyerId, Pageable pageable) {
        return postRepository.findByBuyerId(buyerId, pageable).map(post -> {
            List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
            String username = getUsername(post.getMemberId());

            return PostConverter.toResponse(post, imagePaths, username);
        });
    }

    @Transactional
    public PostDto.Response update(Long id, String title, String description, int price,
                                   TransactionType transactionType, Category category, List<ImageDto.ImageDetail> files) {
        Post post = findPostById(id);

        post.update(title, description, price, transactionType, category);
        List<String> imagePaths = imageService.getImages(DomainName.POST, id);

        if (isExistImages(files)) {
            imageService.deleteAllImages(DomainName.POST, id);
            imagePaths = imageService.save(files, DomainName.POST, id);
        }

        String username = getUsername(post.getMemberId());

        return PostConverter.toResponse(post, imagePaths, username);
    }

    private boolean isExistImages(List<ImageDto.ImageDetail> files) {
        return (files == null || !files.isEmpty()) ? false : null;
    }

    @Transactional
    public PostDto.Response updatePostStatus(Long id, PostStatus postStatus) {
        Post post = findPostById(id);
        post.updatePostStatus(postStatus);

        List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
        String username = getUsername(post.getMemberId());

        return PostConverter.toResponse(post, imagePaths, username);
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NOT_FOUND_POST.getMessage()));
    }

    @Transactional
    public void delete(Long id) {
        Post post = findPostById(id);
        post.deleteStatus();

        imageService.deleteAllImages(DomainName.POST, id);
    }

    @Transactional
    public PostDto.Response purchaseProduct(Long id, Long buyerId) {
        Post post = findPostById(id);
        post.purchased(buyerId);

        List<String> imagePaths = imageService.getImages(DomainName.POST, post.getId());
        String username = getUsername(post.getMemberId());

        return PostConverter.toResponse(post, imagePaths, username);
    }

    private String getUsername(Long memberId) {
        return profileService.toProfile(memberId).username();
    }

    private boolean isViewed(Long postId, Long userId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String key = userId.toString();
        String value = postId.toString();

        Set<String> viewedPosts = setOperations.members(key);

        if (viewedPosts.contains(value)) {
            return true;
        }

        setOperations.add(key, value);

        return false;
    }

}
