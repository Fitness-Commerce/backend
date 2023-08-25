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

    private final String fileStorageLocation = "D:/sideproject/src/main/resources/static/file";

    // 글 작성
    @Transactional
    public Post savePost(PostCreate postCreate) {
        Member member = memberRepository.findById(postCreate.getMemberId()).orElse(null);
        PostCategory postCategory = postCategoryRepository.findById(postCreate.getPostCategoryId()).orElse(null);

        Post post = Post.builder()
                .member(member)
                .postCategory(postCategory)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .viewCount(0L)
                .build();

        Post savedPost = postRepository.save(post);

        List<PostImage> postImages = saveImages(postCreate.getImages(), savedPost); // post 엔티티 전달

        // 각 PostImage 엔티티에 대해 Post 엔티티 설정
        for (PostImage postImage : postImages) {
            postImage.setPost(savedPost);
        }

        return savedPost;
    }


    @Transactional
    public List<PostImage> saveImages(List<MultipartFile> images, Post post) {
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
                        .post(post) // Post 엔티티 설정
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
    public PostRes findOnePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No Post found with id: " + id));

        List<PostImage> postImages = postImageRepository.findByPost_Id(id);
        List<String> imageUrls = postImages.stream()
                .map(PostImage::getUrl)
                .collect(Collectors.toList());

        PostRes response = PostRes.builder()
                .id(post.getId())
                .memberId(post.getMember().getId())
                .postCategoryId(post.getPostCategory().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .url(imageUrls) // 이미지 URL 리스트 추가
                .build();

        return response;
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
                    // .postImageId() 처리는 별도로 해주어야 합니다.
                    // 예를 들어, post.getPostImages()가 이미지의 List를 반환한다면,
                    // 그 중 첫 번째 이미지의 ID나 원하는 이미지의 ID를 선택할 수 있습니다.
                    .title(post.getTitle())
                    .content(post.getContent())
                    .viewCount(post.getViewCount())
                    .build();
        }).collect(Collectors.toList());
    }

    //게시글 수정
    @Transactional
    public Post updatePost(Long postId, Long postCategoryId, String title, String content, List<MultipartFile> postImages) {
        Post postToUpdate = postRepository.findById(postId).orElse(null);

        if (postToUpdate == null) {
            throw new EntityNotFoundException("Post not found with id: " + postId);
        }

        postToUpdate.changePost(title, content, null); // 이미지 업데이트는 아래에서 처리

        if (postImages != null) {
            List<PostImage> newImages = saveImages(postImages, postToUpdate);
        }

        //List<PostImage> newImages = saveImages(postImages, postToUpdate);
        //postToUpdate.changePost(title, content, newImages);

        return postRepository.save(postToUpdate);
    }


    //게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        // 게시글에 연결된 이미지들을 찾습니다.
        List<PostImage> postImages = postImageRepository.findByPost_Id(id);

        // 이미지 삭제 수행
        for (PostImage postImage : postImages) {
            deleteImage(postImage);
        }

        // 게시글 삭제
        postRepository.delete(post);
    }
    private void deleteImage(PostImage postImage) {
        try {
            // 이미지 파일 삭제
            String fileName = postImage.getFileName();
            String filePath = fileStorageLocation + "/" + fileName;
            Path targetPath = Paths.get(filePath);
            Files.deleteIfExists(targetPath);

            // 이미지 엔티티 삭제
            postImageRepository.delete(postImage);
        } catch (IOException e) {
            e.printStackTrace();
            // 이미지 파일 삭제 실패에 대한 예외 처리
        }
    }
}

