import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';
import 'bootstrap-icons/font/bootstrap-icons.css'; // Bootstrap Icons CSS 포함

const CommentModal = ({ show, onClose, mbti }) => {
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(null); // "좋아요" 또는 "싫어요"로만 구분
    const [comments, setComments] = useState([]);
    const [userActions, setUserActions] = useState({}); // 유저의 좋아요/싫어요 상태 저장

    useEffect(() => {
        if (show) {
            fetchComments();
        }
    }, [show]);

    const fetchComments = async () => {
        const fetchedComments = [
            { id: 1, user: '엠제팅화이팅', comment: '좋아요!', rating: '좋아요', likes: 19, dislikes: 1 },
            { id: 2, user: '프로불편러', comment: '별로에요.', rating: '싫어요', likes: 3, dislikes: 25 },
            { id: 3, user: '슬픈 공대생', comment: '실제로 연예인과 대화한다면 이런 느낌일까..', rating: '좋아요', likes: 30, dislikes: 3 },
            { id: 4, user: '빅빅', comment: '이게 뭐가 재밌다고 그러냐 씹덕들', rating: '싫어요', likes: 2, dislikes: 50 }
        ];
        setComments(fetchedComments);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (comment.trim() && rating) {
            const newComment = { id: comments.length + 1, user: '현재 사용자', comment, rating, likes: 0, dislikes: 0 };
            setComments([...comments, newComment]);
            setComment('');
            setRating(null);
        }
    };

    const handleLike = (id) => {
        const currentAction = userActions[id];

        if (currentAction === 'like') {
            setComments(comments.map(comment => comment.id === id ? { ...comment, likes: comment.likes - 1 } : comment));
            setUserActions({ ...userActions, [id]: null });
        } else if (currentAction === 'dislike') {
            setComments(comments.map(comment => comment.id === id ? { ...comment, likes: comment.likes + 1, dislikes: comment.dislikes - 1 } : comment));
            setUserActions({ ...userActions, [id]: 'like' });
        } else {
            setComments(comments.map(comment => comment.id === id ? { ...comment, likes: comment.likes + 1 } : comment));
            setUserActions({ ...userActions, [id]: 'like' });
        }
    };

    const handleDislike = (id) => {
        const currentAction = userActions[id];

        if (currentAction === 'dislike') {
            setComments(comments.map(comment => comment.id === id ? { ...comment, dislikes: comment.dislikes - 1 } : comment));
            setUserActions({ ...userActions, [id]: null });
        } else if (currentAction === 'like') {
            setComments(comments.map(comment => comment.id === id ? { ...comment, dislikes: comment.dislikes + 1, likes: comment.likes - 1 } : comment));
            setUserActions({ ...userActions, [id]: 'dislike' });
        } else {
            setComments(comments.map(comment => comment.id === id ? { ...comment, dislikes: comment.dislikes + 1 } : comment));
            setUserActions({ ...userActions, [id]: 'dislike' });
        }
    };

    const handleRatingClick = (type) => {
        setRating(type);
    };

    return (
        show ? (
            <div className={styles.modalOverlay}>
                <div className={styles.modal}>
                    <button className={styles.closeButton} onClick={onClose}>×</button>
                    <h2 className={styles.modalTitle}>{mbti}에 대한 댓글 및 후기</h2>
                    <form onSubmit={handleSubmit} className={styles.modalForm}>
                        <div className={styles.textareaAndButtons}>
                            <textarea
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="댓글을 입력하세요"
                                className={styles.modalTextarea}
                            />
                            <button type="submit" className={styles.submitButton}>제출</button>
                        </div>
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={() => handleRatingClick('좋아요')}
                                className={`${styles.ratingButton} ${rating === '좋아요' ? styles.like : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up-fill"></i>
                            </button>
                            <button
                                type="button"
                                onClick={() => handleRatingClick('싫어요')}
                                className={`${styles.ratingButton} ${rating === '싫어요' ? styles.dislike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down-fill"></i>
                            </button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>댓글 및 후기</h3>
                        <div className={styles.commentsContainer}>
                            {comments.map(({ id, user, comment, rating, likes, dislikes }) => (
                                <div key={id} className={styles.comment}>
                                    <p><strong>{user}</strong></p>
                                    <p>{comment}</p>
                                    <p>{rating === '좋아요' ? (
                                            <i className="bi bi-hand-thumbs-up-fill" style={{ color: 'blue' }} />
                                        ) : (
                                            <i className="bi bi-hand-thumbs-down-fill" style={{ color: 'red' }} />
                                        ) }
                                    </p>
                                    <div className={styles.commentActions}>
                                        <button
                                            onClick={() => handleLike(id)}
                                            className={`${styles.actionButton} ${userActions[id] === 'like' ? styles.liked : ''}`}
                                        >
                                            <i className="bi bi-hand-thumbs-up"></i> {likes}
                                        </button>
                                        <button
                                            onClick={() => handleDislike(id)}
                                            className={`${styles.actionButton} ${userActions[id] === 'dislike' ? styles.disliked : ''}`}
                                        >
                                            <i className="bi bi-hand-thumbs-down"></i> {dislikes}
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        ) : null
    );
};

CommentModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    mbti: PropTypes.string.isRequired,
};

export { CommentModal };
