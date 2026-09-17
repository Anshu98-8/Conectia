package com.CodingBrajmohan.postService.controller;

import com.CodingBrajmohan.postService.auth.AuthContextHolder;
import com.CodingBrajmohan.postService.dto.PostCreateRequestDto;
import com.CodingBrajmohan.postService.dto.PostDto;
import com.CodingBrajmohan.postService.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(@RequestPart("post") PostCreateRequestDto postCreateRequestDto,
                                              @RequestPart("file") MultipartFile file) {
        PostDto postDto = postService.createPost(postCreateRequestDto, file);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }


}
