package com.fitnesscommerce.domain.post.service;

import com.fitnesscommerce.domain.member.domain.Member;
import com.fitnesscommerce.domain.member.exception.IdNotFound;
import com.fitnesscommerce.domain.member.repository.MemberRepository;
import com.fitnesscommerce.domain.post.domain.Post;
import com.fitnesscommerce.domain.post.domain.PostComment;
import com.fitnesscommerce.domain.post.dto.request.PostCommentCreate;
import com.fitnesscommerce.domain.post.dto.request.PostCommentUpdate;
import com.fitnesscommerce.domain.post.dto.response.PostCommentResponse;
import com.fitnesscommerce.domain.post.dto.response.PostResponse;
import com.fitnesscommerce.domain.post.exception.PostCommentNotFound;
import com.fitnesscommerce.domain.post.exception.PostNotFound;
import com.fitnesscommerce.domain.post.repository.PostCommentRepository;
import com.fitnesscommerce.domain.post.repository.PostRepository;
import com.fitnesscommerce.global.config.data.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    // 게시글 댓글 작성
    @Transactional
    public PostCommentResponse saveComment(Long postId, PostCommentCreate postCommentCreate, MemberSession session){
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        PostComment postComment = PostComment.builder()
                .post(post)
                .content(postCommentCreate.getContent())
                .member(member)
                .build();

        postCommentRepository.save(postComment);

        PostCommentResponse postCommentResponse = PostCommentResponse.builder()
                .postId(post.getId())
                .nickname(member.getNickname())
                .content(postComment.getContent())
                .build();

        return postCommentResponse;
    }

    // 게시글 댓글 조회
    /*public PostCommentResponse getPostCommentPaging(Long postId, int page, int size, String orderBy, String direction) {

        Sort.Order order = new Sort.Order(Sort.Direction.fromString(direction), orderBy);
        Sort sort = Sort.by(order);

        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<PostComment> postsCommentPage = postCommentRepository.findByPostId(postId, pageable);

        List<PostCommentResponse> content = postsCommentPage.getContent().stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());

        return new PostCommentResponse(postsCommentPage.getTotalPages(), content);
    }*/

    private PostCommentResponse mapToCommentResponse(PostComment comment) {
        return PostCommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreated_at())
                .updatedAt(comment.getUpdated_at())
                .build();
    }

    // 게시글 댓글 수정
    @Transactional
    public Long updateComment(Long postId, Long commentId, PostCommentUpdate postCommentUpdate, MemberSession session) {
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(PostCommentNotFound::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

        if (member == postComment.getMember()){
            postComment.updateComment(postCommentUpdate.getContent());
        }else
            throw new RuntimeException("회원이 아닙니다.");

        return postComment.getId();
    }

    // 게시글 댓글 삭제

    @Transactional
    public void deleteComment(Long postId, Long commentId, MemberSession session){
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(PostCommentNotFound::new);
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        Member member = memberRepository.findById(session.id)
                .orElseThrow(IdNotFound::new);

       if(member == postComment.getMember()){
           post.removePostComment(postComment);
       }else
           throw new RuntimeException("회원이 아닙니다.");

        postCommentRepository.delete(postComment);
    }


}
