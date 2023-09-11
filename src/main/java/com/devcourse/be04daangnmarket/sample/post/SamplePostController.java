package com.devcourse.be04daangnmarket.sample.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.devcourse.be04daangnmarket.post.domain.constant.Category;
import com.devcourse.be04daangnmarket.post.domain.constant.TransactionType;

@Controller
public class SamplePostController {

	@GetMapping("/sample/create/posts")
	public String sampleMain(Model model){
		model.addAttribute("categories", Category.values());
		model.addAttribute("transactionTypes", TransactionType.values());

		return "/sample/sampleCreatePost";
	}

}
