import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { sendPostRequest, sendGetRequest } from "../services";

const CommentModal = ({ show, onClose, propsData }) => {
    const [comment, setComment] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [rating, setRating] = useState(null);
    const [comments, setComments] = useState([]);
    const [userReactions, setUserReactions] = useState({});

    useEffect(() => {
        if (show) {
            const filter = "All"
            const page = 0
            const size = 20
            const requestData = {
                page : page,
                size : size,
                filter : filter
            }
            console.log(propsData)
            fetchComments(requestData);
        }
    }, [show]);

    const fetchComments = async (requestData) => {
        const response = await sendGetRequest({}, `/api/comments/${propsData.profileId}/list`);
        setComments(response.commentInfos);
        // Initialize user reactions
        const initialReactions = {};
        response.commentInfos.forEach(comment => {
            initialReactions[comment.id] = { liked: false, disliked: false };
        });
        setUserReactions(initialReactions);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (comment.trim() && rating !== null) {
            await handleRequestComment();
        }
    };

    const handleLike = async () => {
        setIsLoading(true)
        const filter = "Like"
        const page = 0
        const size = 20
        const requestData = {
            page : page,
            size : size,
            filter : filter
        }
        console.log(requestData)
        const response = await sendGetRequest(requestData, `/api/comments/${propsData.profileId}/list`); // 좋아요 / 싫어요 필터 적용 요청
        setComments(response.commentInfos);
        setIsLoading(false)
    };

    const handleDislike = async () => {
        setIsLoading(true)
        const filter = "Dislike"
        const page = 0
        const size = 20
        const requestData = {
            page : page,
            size : size,
            filter : filter
        }
        console.log(requestData)
        const response = await sendGetRequest(requestData, `/api/comments/${propsData.profileId}/list`); // 좋아요 / 싫어요 필터 적용 요청
        setComments(response.commentInfos);
        setIsLoading(false)
    };

    const handleRequestComment = async () => {
        const commentData = {
            userId: 2, // 실제 사용자 ID로 변경 필요
            content: comment,
            isLike: rating,
        };
        try {
            await sendPostRequest(commentData, `/api/comments/${propsData.profileId}/create`);
            await fetchComments(); // 댓글 목록 새로고침
            setComment('');
            setRating(null);
        } catch (e) {
            alert('댓글 저장 실패');
        }
    };

    const toggleLike = async (id) => {
        try {
            await sendPostRequest({}, `/api/comments/${id}/like`);
            setUserReactions(prev => ({
                ...prev,
                [id]: { liked: !prev[id].liked, disliked: false }
            }));
            setComments(prev =>
                prev.map(comment =>
                    comment.id === id
                        ? { ...comment,
                            likeCount: comment.likeCount + (userReactions[id].liked ? -1 : 1),
                            dislikeCount: comment.dislikeCount - (userReactions[id].disliked ? 1 : 0)
                        }
                        : comment
                )
            );
        } catch (e) {
            alert('좋아요 처리 실패');
        }
    };

    const toggleDislike = async (id) => {
        try {
            await sendPostRequest({}, `/api/comments/${id}/dislike`);
            setUserReactions(prev => ({
                ...prev,
                [id]: { liked: false, disliked: !prev[id].disliked }
            }));
            setComments(prev =>
                prev.map(comment =>
                    comment.id === id
                        ? { ...comment,
                            dislikeCount: comment.dislikeCount + (userReactions[id].disliked ? -1 : 1),
                            likeCount: comment.likeCount - (userReactions[id].liked ? 1 : 0)
                        }
                        : comment
                )
            );
        } catch (e) {
            alert('싫어요 처리 실패');
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleString('ko-KR', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    return (
        show && (
            <div className={styles.CommentModalOverlay}>
                <div className={styles.modal}>
                    <button className={styles.closeButton} onClick={onClose}>
                        ×
                    </button>
                    <h2 className={styles.modalTitle}>{propsData.type}에 대한 댓글 및 후기</h2>
                    <form onSubmit={handleSubmit} className={styles.modalForm}>
                        <div className={styles.textareaAndButtons}>
                        <textarea
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            placeholder="댓글을 입력하세요"
                            className={styles.modalTextarea}
                        />
                            <button type="submit" className={styles.submitButton}>
                                제출
                            </button>
                        </div>
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={() => handleLike}
                                className={`${styles.ratingButton} ${rating === true ? styles.ratinglike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up" style={{ color: "black" }}></i>
                            </button>
                            <button
                                type="button"
                                onClick={() => handleDislike}
                                className={`${styles.ratingButton} ${rating === false ? styles.ratingdislike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down" style={{ color: "black" }}></i>
                            </button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>해당 프로필에 대한 댓글</h3>
                        <div className={styles.commentsContainer}>
                            {comments.map(({ id, username, content, isLike, likeCount, dislikeCount, cwTime }) => (
                                <div key={id} className={styles.comment}>
                                    <p><strong>{username}</strong> <span
                                        className={styles.commentTime}>{formatDate(cwTime)}</span></p>
                                    <p>{content}</p>
                                    <p>{isLike ? (
                                        <i className="bi bi-hand-thumbs-up-fill" style={{color: '#4CAF50'}}/>
                                    ) : (
                                        <i className="bi bi-hand-thumbs-down-fill" style={{color: '#f44336'}}/>
                                    )}
                                    </p>
                                    <div className={styles.commentActions}>
                                        <button
                                            onClick={() => toggleLike(id)}
                                            className={`${styles.actionButton} ${userReactions[id]?.liked ? styles.liked : ''}`}
                                        >
                                            <i className={`bi ${userReactions[id]?.liked ? 'bi-hand-thumbs-up-fill' : 'bi-hand-thumbs-up'}`}></i> {likeCount}
                                        </button>
                                        <button
                                            onClick={() => toggleDislike(id)}
                                            className={`${styles.actionButton} ${userReactions[id]?.disliked ? styles.disliked : ''}`}
                                        >
                                            <i className={`bi ${userReactions[id]?.disliked ? 'bi-hand-thumbs-down-fill' : 'bi-hand-thumbs-down'}`}></i> {dislikeCount}
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
    propsData: PropTypes.object.isRequired,
};

export { CommentModal };