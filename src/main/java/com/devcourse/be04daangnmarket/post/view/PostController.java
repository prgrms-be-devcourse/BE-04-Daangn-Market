package com.devcourse.be04daangnmarket.post.view;

import com.devcourse.be04daangnmarket.common.auth.User;
import com.devcourse.be04daangnmarket.post.application.PostService;
import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;
import com.devcourse.be04daangnmarket.post.dto.PostDto;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String getAllPosts() {
        return "main";
    }

    @GetMapping(value = "/posts/new")
    public String newPost(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("transactionTypes", TransactionType.values());

        return "newPost";
    }

    @GetMapping(value = "/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);

        return "post";
    }

    @GetMapping(value = "/posts/update/{id}")
    public String updatePost(@PathVariable Long id,
                             @AuthenticationPrincipal User user,
                             Model model) {
        PostDto.Response post = postService.getPost(id, user.getId());

        model.addAttribute("post", post);
        model.addAttribute("categories", Category.values());
        model.addAttribute("transactionTypes", TransactionType.values());

        return "updatePost";
    }

    @GetMapping(value = "/posts/category")
    public String postsByCategory(Model model) {
        model.addAttribute("categories", Category.values());

        return "categoryPosts";
    }

    @GetMapping("/posts/search")
    public String postsByKeyword(Model model) {

        return "searchPosts";
    }

    @GetMapping("/posts/{id}/communication")
    public String communicationMembers(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);

        return "communicationList";
    }

    @GetMapping("/posts/{id}/comments")
    public String comments(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);

        return "comments";
    }
}
