package com.devcourse.be04daangnmarket.post.view;

import com.devcourse.be04daangnmarket.post.domain.Category;
import com.devcourse.be04daangnmarket.post.domain.TransactionType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PostController {

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
}
