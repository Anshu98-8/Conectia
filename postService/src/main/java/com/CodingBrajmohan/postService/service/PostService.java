package com.CodingBrajmohan.postService.service;


import com.CodingBrajmohan.postService.client.ConnectionServiceClient;
import com.CodingBrajmohan.postService.dto.PersonDto;
import com.CodingBrajmohan.postService.dto.PostCreateRequestDto;
import com.CodingBrajmohan.postService.dto.PostDto;
import com.CodingBrajmohan.postService.entity.PostEntity;
import com.CodingBrajmohan.postService.event.PostCreated;
import com.CodingBrajmohan.postService.exception.ResourceNotFoundException;
import com.CodingBrajmohan.postService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final ConnectionServiceClient connectionServiceClient;
    private final KafkaTemplate<Long, PostCreated> postCreatedKafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with id: {}", userId);
        PostEntity post = modelMapper.map(postCreateRequestDto, PostEntity.class);
        post.setUserId(userId);
        post = postRepository.save(post);

        List<PersonDto> personDtoList = connectionServiceClient.getFirstDegreeConnection(userId);

        for(PersonDto person: personDtoList) { // send notification to each connection
            PostCreated postCreated = PostCreated.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .userId(person.getUserId())
                    .ownerUserId(userId)
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic", postCreated);
        }

        return modelMapper.map(post, PostDto.class);

    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID: {}", postId);

        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post not found " +
                "with ID: "+postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all the posts of a user with ID: {}", userId);
        List<PostEntity> postList = postRepository.findByUserId(userId);

        return postList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
