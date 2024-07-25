package com.example.mzting.service;

import com.example.mzting.DTO.CommentDTO;
import com.example.mzting.DTO.LikeDisLikeCountInfo;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final MBTIPostsRepository mbtiPostsRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, MBTIPostsRepository mbtiPostsRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.mbtiPostsRepository = mbtiPostsRepository;
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    private LikeDisLikeCountInfo getLikeAndDislikeCount(Long postId) {
        Optional<MBTIPosts> mbtiPostsOptional = mbtiPostsRepository.findByPostId(postId);
        if (mbtiPostsOptional.isPresent()) {
            MBTIPosts mbtiPosts = mbtiPostsOptional.get();
            return new LikeDisLikeCountInfo(mbtiPosts.getTotalLikeCount(), mbtiPosts.getTotalDislikeCount());
        } else {
            // 결과가 없을 경우 기본값 반환
            return new LikeDisLikeCountInfo(0L, 0L);
        }
    }

    public CommentDTO.GetPostsCommentsResponse getCommentInfoByPostId(Long postId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPostId(postId, pageable);
        List<CommentDTO.CommentInfo> commentInfos = comments.getContent().stream()
                .map(this::convertToCommentInfo)
                .collect(Collectors.toList());

        LikeDisLikeCountInfo likeDisLikeCountInfo = getLikeAndDislikeCount(postId);

        CommentDTO.PaginationInfo paginationInfo = new CommentDTO.PaginationInfo(
                comments.getNumber(),
                comments.getTotalPages(),
                comments.getTotalElements(),
                comments.getSize()
        );

        return new CommentDTO.GetPostsCommentsResponse(
                likeDisLikeCountInfo.getTotalLikeCount(),
                likeDisLikeCountInfo.getTotalDislikeCount(),
                commentInfos,
                paginationInfo
        );
    }

    private CommentDTO.CommentInfo convertToCommentInfo(Comment comment) {
        String username = userRepository.findUsernameById(comment.getUserId());
        return new CommentDTO.CommentInfo(
                comment.getContent(),
                comment.getIsLike(),
                comment.getCw_time(),
                username
        );
    }
}