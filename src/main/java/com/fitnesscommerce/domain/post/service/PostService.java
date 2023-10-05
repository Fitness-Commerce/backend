package com.fitnesscommerce.domain.post.service;

import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.domain.post.domain.Post;
import com.fitnesscommerce.domain.post.domain.PostCategory;
import com.fitnesscommerce.domain.post.domain.PostImage;
import com.fitnesscommerce.domain.post.dto.request.PostCreate;
import com.fitnesscommerce.domain.post.dto.request.PostSortFilter;
import com.fitnesscommerce.domain.post.dto.request.PostUpdate;
import com.fitnesscommerce.domain.post.dto.response.CustomPostPageResponse;
import com.fitnesscommerce.domain.post.dto.response.IdResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.exception.PostCategoryNotFound;
import com.fitnesscommerce.domain.post.exception.PostNotFound;
import com.fitnesscommerce.domain.post.repository.PostCategoryRepository;
import com.fitnesscommerce.domain.post.repository.PostImageRepository;
import com.fitnesscommerce.domain.post.repository.PostRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Bean 주입 -> @Autowired 대신 생성자로
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final MemberRepository memberRepository;

    @Value("${file.storage.location}")
    private String fileStorageLocation;

    // 글 작성
    @Transactional
    public IdResponse save(PostCreate postCreate, MemberSession session) throws IOException {

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        PostCategory postCategory = postCategoryRepository.findByTitle(postCreate.getPostCategoryTitle())
                .orElseThrow(PostCategoryNotFound::new);

        Post post = Post.builder()
                .member(member)
                .postCategory(postCategory)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        Post savePost = postRepository.save(post); // 게시글 저장

        if (postCreate.getImages() != null) {
            for (MultipartFile image : postCreate.getImages()) {

                String originalFileName = image.getOriginalFilename();
                String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID() + extension;
                String filePath = fileStorageLocation + "/" + fileName;

                Path targetLocation = Paths.get(filePath);
                Files.copy(image.getInputStream(), targetLocation);

                PostImage postImage = PostImage.builder()
                        .fileName(fileName)
                        .url("http://43.200.32.144/api/item/images" + "/" + fileName)
                        .post(post)
                        .build();

                postImageRepository.save(postImage); //게시글 저장
            }
        }
        return IdResponse.builder()
                .id(savePost.getId())
                .build();
    }

    // 게시글 조회 수
    @Transactional
    public void updateViewCount(Long postId) {
        postRepository.updateViewCount(postId);
    }

    // 게시글 단건 조회
    public PostResponse findOnePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        List<PostImage> postImages = postImageRepository.findByPostId(postId);

        return PostResponse.builder()
                .id(post.getId())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .postCategoryId(post.getPostCategory().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .postImageUrl(postImages.stream().map(PostImage::getUrl).collect(Collectors.toList()))
                .viewCount(post.getViewCount())
                .createdAt(post.getCreated_at())
                .updatedAt(post.getUpdated_at())
                .build();
    }

    // 게시글 전체 조회
    public CustomPostPageResponse getAllPostPaging(PostSortFilter postSortFilter, String search) {

        String[] sortPost = postSortFilter.getOrder().split("_");
        String orderBy = sortPost[0];
        String direction = sortPost[1];


        Sort.Order field = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(field);
        Pageable pageable = PageRequest.of(postSortFilter.getPage()-1, postSortFilter.getSize(),sort);
        Page<Post> postsPage;

        if (search != null) {
            postsPage = postRepository.findByTitleContaining(search, pageable);
        } else {
            postsPage = postRepository.findAll(pageable);
        }

        List<PostResponse> content = postsPage.getContent().stream()
                .map(this::mapPostToResponse)
                .collect(Collectors.toList());

        return new CustomPostPageResponse(postsPage.getTotalPages(), content);
    }

    public PostResponse mapPostToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .memberId(post.getMember().getId())
                .postCategoryId(post.getPostCategory().getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreated_at())
                .updatedAt(post.getUpdated_at())
                .build();
    }

    //게시글 수정
    @Transactional
    public IdResponse updatePost(Long postId, PostUpdate request, MemberSession session) throws IOException {
        // 수정할 Post를 조회합니다.
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

        // 수정할 Post의 새로운 카테고리를 찾습니다.
        PostCategory postCategory = postCategoryRepository.findByTitle(request.getPostCategoryTitle())
                .orElseThrow(PostCategoryNotFound::new);

        // 수정하는 사용자를 찾습니다.
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if (member == post.getMember()) {

            // Post의 속성을 업데이트합니다.
            post.updatePost(postCategory, request.getTitle(), request.getContent());

            // 기존 이미지들을 삭제하고 Post의 이미지 컬렉션을 초기화합니다.
            List<PostImage> byPostId = postImageRepository.findByPostId(post.getId());
            if (byPostId != null) {
                for (PostImage postImage : byPostId) {
                    String fileName = postImage.getFileName();
                    String filePath = fileStorageLocation + "/" + fileName;
                    Path targetLocation = Paths.get(filePath);
                    Files.deleteIfExists(targetLocation);
                    postImageRepository.delete(postImage);
                }
            }

            //새로운 PostImage 생성 및 연결
            if (request.getImages() != null) {
                for (MultipartFile image : request.getImages()) {
                    String originalFileName = image.getOriginalFilename();
                    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String fileName = UUID.randomUUID().toString() + extension;
                    String filePath = fileStorageLocation + "/" + fileName;

                    Path targetLocation = Paths.get(filePath);
                    Files.copy(image.getInputStream(), targetLocation);

                    PostImage postImage = PostImage.builder()
                            .fileName(fileName)
                            .url("http://43.200.32.144:8080/api/post/images" + "/" + fileName)
                            .post(post)
                            .build();

                    postImageRepository.save(postImage);
                }
            }
            return IdResponse.builder()
                    .id(post.getId())
                    .build();
        }else
            throw new RuntimeException("회원이 일치하지 않습니다.");
    }

    
    //게시글 삭제
    @Transactional
    public void deletePost(Long postId, MemberSession session) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if (member == post.getMember()) {
            // 게시글에 연결된 이미지들을 찾습니다.
            List<PostImage> postImages = postImageRepository.findByPostId(post.getId());

            // 이미지 삭제 수행
            for (PostImage postImage : postImages) {
                String fileName = postImage.getFileName();
                String filePath = fileStorageLocation + "/" + fileName;
                Path targetLocation = Paths.get(filePath);
                Files.deleteIfExists(targetLocation);
            }

            // 게시글 삭제
            postRepository.delete(post);

        }else
            throw new RuntimeException("회원이 일치하지 않습니다.");
    }


}
