import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';
import 'bootstrap-icons/font/bootstrap-icons.css'; // Bootstrap Icons CSS 포함

const CommentModal = ({ show, onClose, mbti }) => {
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(null); // "좋아요" 또는 "싫어요"로만 구분
    const [comments, setComments] = useState([]);

    useEffect(() => {
        if (show) {
            fetchComments();
        }
    }, [show]);

    const fetchComments = async () => {
        const fetchedComments = await sendGetRequest({}, '/api/posts/1/comments');
        const extractedComments = fetchedComments.commentInfos.map(comment => ({
            ...comment,
            likeCount: comment.likeCount || 0,
            dislikeCount: comment.dislikeCount || 0,
            userLiked: comment.userLiked || false,
            userDisliked: comment.userDisliked || false
        }));
        setComments(extractedComments);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (comment.trim() && rating) {
            const newComment = { id: comments.length + 1, user: '현재 사용자', comment, rating };
            setComments([...comments, newComment]);
            setComment('');
            setRating(null);
        }
    };

    const handleLike = () => {
        setRating(true);
    };

    const handleDislike = () => {
        setRating(false);
    };

    const handleRequestComment = async () => {
        const data = {
            userId: 2,
            content: comment,
            isLike: rating,
        };
        try {
            await sendPostRequest(data, '/api/posts/1/comments');
            alert('댓글 저장 완료');
        } catch (e) {
            alert('댓글 저장 실패');
        }
    };

    const toggleLike = (id) => {
        setComments((prevComments) =>
            prevComments.map((c) => {
                if (c.id === id) {
                    if (c.userLiked) {
                        return { ...c, likeCount: c.likeCount - 1, userLiked: false };
                    } else {
                        return {
                            ...c,
                            likeCount: c.likeCount + 1,
                            userLiked: true,
                            userDisliked: false,
                            dislikeCount: c.userDisliked ? c.dislikeCount - 1 : c.dislikeCount
                        };
                    }
                }
                return c;
            })
        );
    };

    const toggleDislike = (id) => {
        setComments((prevComments) =>
            prevComments.map((c) => {
                if (c.id === id) {
                    if (c.userDisliked) {
                        return { ...c, dislikeCount: c.dislikeCount - 1, userDisliked: false };
                    } else {
                        return {
                            ...c,
                            dislikeCount: c.dislikeCount + 1,
                            userDisliked: true,
                            userLiked: false,
                            likeCount: c.userLiked ? c.likeCount - 1 : c.likeCount
                        };
                    }
                }
                return c;
            })
        );
    };

    return (
        show && (
            <div className={styles.modalOverlay}>
                <div className={styles.modal}>
                    <button className={styles.closeButton} onClick={onClose}>
                        ×
                    </button>
                    <h2 className={styles.modalTitle}>{mbti}에 대한 댓글 및 후기</h2>
                    <form onSubmit={handleSubmit} className={styles.modalForm}>
                        <div className={styles.textareaAndButtons}>
                            <textarea
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="댓글을 입력하세요"
                                className={styles.modalTextarea}
                            />
                            <button type="submit" onClick={handleRequestComment} className={styles.submitButton}>
                                제출
                            </button>
                        </div>
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={handleLike}
                                className={`${styles.ratingButton} ${rating === true ? styles.ratinglike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up" style={{ color: "black" }}></i>
                            </button>
                            <button
                                type="button"
                                onClick={handleDislike}
                                className={`${styles.ratingButton} ${rating === false ? styles.ratingdislike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down" style={{ color: "black" }}></i>
                            </button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>댓글 및 후기</h3>
                        <div className={styles.commentsContainer}>
                            {comments.map(({ id, username, content, isLike, likeCount, dislikeCount, userLiked, userDisliked }) => (
                                <div key={id} className={styles.comment}>
                                    <p><strong>{username}</strong></p>
                                    <p>{content}</p>
                                    <p>{isLike === true ? (
                                            <i className="bi bi-hand-thumbs-up-fill" style={{ color: '#4CAF50' }} />
                                        ) : (<i className="bi bi-hand-thumbs-down-fill" style={{ color: '#f44336' }} />
                                        ) }
                                    </p>
                                    <div className={styles.commentActions}>
                                        <button
                                            onClick={() => toggleLike(id)}
                                            className={`${styles.actionButton} ${userLiked ? styles.liked : ''}`}
                                        >
                                            <i className={`bi ${userLiked ? 'bi-hand-thumbs-up-fill' : 'bi-hand-thumbs-up'}`}></i> {likeCount}
                                        </button>
                                        <button
                                            onClick={() => toggleDislike(id)}
                                            className={`${styles.actionButton} ${userDisliked ? styles.disliked : ''}`}
                                        >
                                            <i className={`bi ${userDisliked ? 'bi-hand-thumbs-down-fill' : 'bi-hand-thumbs-down'}`}></i> {dislikeCount}
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        )
    );
};

CommentModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    mbti: PropTypes.string.isRequired,
};

export { CommentModal };
