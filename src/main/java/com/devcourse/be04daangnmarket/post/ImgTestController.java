package com.devcourse.be04daangnmarket.post;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller//("api/test/posts")
public class ImgTestController {

	@GetMapping(value = "/create-img")
	public String test(){
		System.out.println("ddd");
		return "/test/postUploadImg";
	}

	@GetMapping(value = "/update-img")
	public String test2(){
		System.out.println("kkk");
		return "/test/putUploadImg";
	}

	@PostMapping(value = "/create-img")
	@ResponseBody
	public String createPost(ImgTestController.TestDto testDto){
		return "nice";
	}

	@PutMapping(value = "/update-img")
	@ResponseBody
	public String updatePost(ImgTestController.TestDto testDto) {
		return "nice";
	}

	record TestDto (String name, List<MultipartFile> file){

	}

}
