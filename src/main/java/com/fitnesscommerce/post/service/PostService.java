package com.fitnesscommerce.post.service;

import com.fitnesscommerce.post.domain.Member;
import com.fitnesscommerce.post.domain.Post;
import com.fitnesscommerce.post.domain.PostCategory;
import com.fitnesscommerce.post.domain.PostImage;
import com.fitnesscommerce.post.dto.request.PostCreate;
import com.fitnesscommerce.post.dto.request.PostUpdate;
import com.fitnesscommerce.post.dto.response.PostRes;
import com.fitnesscommerce.post.repository.MemberRepository;
import com.fitnesscommerce.post.repository.PostCategoryRepository;
import com.fitnesscommerce.post.repository.PostImageRepository;
import com.fitnesscommerce.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor // Bean 주입 -> @Autowired 대신 생성자로
public class PostService {

    private final PostRepository postRepository;

    private final PostImageRepository postImageRepository;

    private final PostCategoryRepository postCategoryRepository;

    private final MemberRepository memberRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    // 글 작성
    @Transactional
    public Post savePost(PostCreate postCreate) {
        Member member = memberRepository.findById(postCreate.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다!"));

        PostCategory postCategory = postCategoryRepository.findById(postCreate.getPostCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다!"));

        Post post = Post.builder()
                .member(member)
                .postCategory(postCategory)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .viewCount(0)
                .build();

        Post savedPost = postRepository.save(post);

        //이미지가 null값이 아닐때 처리
        if(postCreate.getImages() != null){  //객체의 getImages() 메서드를 호출하여 이미지 리스트를 가져옵니다.
            List<MultipartFile> images = postCreate.getImages();
            List<PostImage> savedImages = saveImages(images); //savedImages는 PostImage 엔티티의 리스트입니다.
            savedPost.getPostImages().addAll(savedImages); //savedPost의 이미지 리스트(postImages)에 앞서 저장한 이미지들(savedImages)을 추가합니다. 이를 통해 게시글과 이미지들 간의 연결을 만들어줍니다.
        }
        return savedPost;
    }


    @Transactional
    public List<PostImage> saveImages(List<MultipartFile> images) {
        List<PostImage> postImages = new ArrayList<>();

        for (MultipartFile image : images) {
            String fileName = image.getOriginalFilename();
            String filePath = fileStorageLocation + "/" + fileName;

            try {
                Path targetLocation = Paths.get(filePath);
                Files.copy(image.getInputStream(), targetLocation);

                PostImage postImage = PostImage.builder()
                        .fileName(fileName)
                        .url("http://localhost:8080/api/post/images" + "/" + fileName)
                        .build();

                postImages.add(postImage);
            } catch (IOException e) {
                e.printStackTrace();
                // 이미지 저장 실패에 대한 로직 처리
            }
        }
        return postImageRepository.saveAll(postImages);
    }

    // 게시글 단건 조회
    @Transactional
    public PostRes findOnePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        return PostRes.builder()
                .id(post.getId())
                .memberId(post.getMember().getId())
                .postCategoryId(post.getPostCategory().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .url(post.getPostImages().stream().map(PostImage::getUrl).collect(Collectors.toList()))
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    // 게시글 전체 조회
    @Transactional
    public List<PostRes> findAllPost(){
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            return PostRes.builder()
                    .id(post.getId())
                    .memberId(post.getMember().getId())
                    .postCategoryId(post.getPostCategory().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .url(post.getPostImages().stream().map(PostImage::getUrl).collect(Collectors.toList()))
                    .viewCount(post.getViewCount())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    // 게시글 조회 수
    @Transactional
    public void updateViewCount(Long postId){
        postRepository.updateViewCount(postId);
    }

    //게시글 수정
    @Transactional
    public Post updatePost(Long postId, PostUpdate postUpdate) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다!"));

        PostCategory postCategory = postCategoryRepository.findById(postUpdate.getPostCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다!"));

        List<PostImage> newImages = new ArrayList<>();

        //기존 PostImages 삭제
        List<PostImage> oldPostImages = post.getPostImages();
        if(!oldPostImages.isEmpty()){
            deletePostImageFiles(oldPostImages); // 이미지 파일 삭제

            postImageRepository.deleteAll(oldPostImages); // 이미지 레코드 삭제
            post.getPostImages().clear(); // post 객체에서 이미지 리스트 비우기
        }

        //새로운 PostImage 생성 및 연결
        List<MultipartFile> images = postUpdate.getImages();
        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = image.getOriginalFilename();
                String filePath = fileStorageLocation + "/" + fileName;

                try {
                    Path targetLocation = Paths.get(filePath);
                    Files.copy(image.getInputStream(), targetLocation);

                    PostImage newPostImage = PostImage.builder()
                            .fileName(fileName)
                            .url("http://localhost:8080/api/post/images" + "/" + fileName)
                            .build();
                    newImages.add(newPostImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 이미지 저장 실패에 대한 로직 처리
                }
            }
        }
        // Post의 속성들 업데이트
        post.changePost(
                postCategory,
                postUpdate.getTitle(),
                postUpdate.getContent(),
                newImages
        );

        return postRepository.save(post);
    }

    private void deletePostImageFiles(List<PostImage> itemImages) {
        for (PostImage image : itemImages) {
            String fileName = image.getFileName();
            String filePath = fileStorageLocation + "/" + fileName;

            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 삭제 실패에 대한 로직 처리
            }
        }

    }


    //게시글 삭제
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // 게시글에 연결된 이미지들을 찾습니다.
        List<PostImage> postImages = post.getPostImages();

        // 이미지 삭제 수행
        if (!postImages.isEmpty()) {
            deletePostImageFiles(postImages); // 이미지 파일 삭제
            postImageRepository.deleteAll(postImages); // 이미지 레코드 삭제
        }

        // 게시글 삭제
        postRepository.delete(post);
    }
}

