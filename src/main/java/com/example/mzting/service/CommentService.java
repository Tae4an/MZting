package com.example.mzting.service;

import com.example.mzting.dto.CommentDTO;
import com.example.mzting.dto.LikeDislikeCountInfo;
import com.example.mzting.entity.Comment;
import com.example.mzting.entity.MBTIPosts;
import com.example.mzting.repository.CommentRepository;
import com.example.mzting.repository.MBTIPostsRepository;
import com.example.mzting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 댓글 서비스 클래스
 * 댓글 저장, 조회 및 좋아요/싫어요 개수 관리 등의 기능을 제공
 */
@Service
public class CommentService {

    // 댓글 저장소
    private final CommentRepository commentRepository;
    // 사용자 저장소
    private final UserRepository userRepository;
    // MBTI 게시물 저장소
    private final MBTIPostsRepository mbtiPostsRepository;

    /**
     * CommentService 생성자
     * 필요한 의존성을 주입받아 초기화
     *
     * @param commentRepository 댓글 저장소
     * @param userRepository 사용자 저장소
     * @param mbtiPostsRepository MBTI 게시물 저장소
     */
    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, MBTIPostsRepository mbtiPostsRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.mbtiPostsRepository = mbtiPostsRepository;
    }

    /**
     * 댓글을 저장하는 메서드
     *
     * @param comment 저장할 댓글 객체
     * @return 저장된 댓글 객체
     */
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * 특정 게시물의 좋아요 및 싫어요 개수를 반환하는 메서드
     *
     * @param postId 게시물 ID
     * @return 좋아요 및 싫어요 개수 정보
     */
    private LikeDislikeCountInfo getLikeAndDislikeCount(Long postId) {
        Optional<MBTIPosts> mbtiPostsOptional = mbtiPostsRepository.findByPostId(postId);
        if (mbtiPostsOptional.isPresent()) {
            MBTIPosts mbtiPosts = mbtiPostsOptional.get();
            return new LikeDislikeCountInfo(mbtiPosts.getTotalLikeCount(), mbtiPosts.getTotalDislikeCount());
        } else {
            // 결과가 없을 경우 기본값 반환
            return new LikeDislikeCountInfo(0L, 0L);
        }
    }

    /**
     * 특정 게시물의 댓글 정보를 페이지네이션하여 반환하는 메서드
     *
     * @param profileId 게시물 ID
     * @param pageable 페이지네이션 정보
     * @return 댓글 정보를 포함한 응답 객체
     */
    public CommentDTO.GetPostsCommentsResponse getCommentInfoByProfileId(Long profileId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByProfileId(profileId, pageable);
        List<CommentDTO.CommentInfo> commentInfos = comments.getContent().stream()
                .map(this::convertToCommentInfo)
                .collect(Collectors.toList());

        LikeDislikeCountInfo likeDislikeCountInfo = getLikeAndDislikeCount(profileId);

        CommentDTO.PaginationInfo paginationInfo = new CommentDTO.PaginationInfo(
                comments.getNumber(),
                comments.getTotalPages(),
                comments.getTotalElements(),
                comments.getSize()
        );

        return new CommentDTO.GetPostsCommentsResponse(
                likeDislikeCountInfo.getTotalLikeCount(),
                likeDislikeCountInfo.getTotalDislikeCount(),
                commentInfos,
                paginationInfo
        );
    }

    /**
     * 댓글을 CommentInfo DTO로 변환하는 메서드
     *
     * @param comment 변환할 댓글 객체
     * @return 변환된 CommentInfo 객체
     */
    private CommentDTO.CommentInfo convertToCommentInfo(Comment comment) {
        String username = userRepository.findUsernameById(comment.getUserId());
        return new CommentDTO.CommentInfo(
                comment.getContent(),
                comment.getIsLike(),
                comment.getCwTime(),
                username
        );
    }
}
