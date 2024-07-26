import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';

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
        const fetchedComments = [
            { id: 1, user: 'User1', comment: '좋아요!', rating: '좋아요' },
            { id: 2, user: 'User2', comment: '별로에요.', rating: '싫어요' },
            { id: 3, user: '슬픈 공대생', comment: '실제로 연예인과 대화한다면 이런 느낌일까..', rating: '좋아요' }
        ];
        setComments(fetchedComments);
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
        setRating('좋아요');
    };

    const handleDislike = () => {
        setRating('싫어요');
    };

    return (
        show ? (
            <div className={styles.modalOverlay}>
                <div className={styles.modal}>
                    <button className={styles.closeButton} onClick={onClose}>×</button>
                    <h2 className={styles.modalTitle}>{mbti}에 대한 댓글 및 후기</h2>
                    <form onSubmit={handleSubmit} className={styles.modalForm}>
                        <textarea
                            value={comment}
                            onChange={(e) => setComment(e.target.value)}
                            placeholder="댓글을 입력하세요"
                            className={styles.modalTextarea}
                        />
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={handleLike}
                                className={`${styles.ratingButton} ${rating === '좋아요' ? styles.selected : ''}`}
                            >
                                좋아요
                            </button>
                            <button
                                type="button"
                                onClick={handleDislike}
                                className={`${styles.ratingButton} ${rating === '싫어요' ? styles.selected : ''}`}
                            >
                                싫어요
                            </button>
                        </div>
                        <div className={styles.modalButtons}>
                            <button type="submit" className={styles.submitButton}>제출</button>
                            <button type="button" onClick={onClose} className={styles.cancelButton}>취소</button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>댓글 및 후기</h3>
                        {comments.map(({ id, user, comment, rating }) => (
                            <div key={id} className={styles.comment}>
                                <p><strong>{user}</strong></p>
                                <p>{comment}</p>
                                <p>{rating === '좋아요' ? '👍 좋아요' : '👎 싫어요'}</p>
                            </div>
                        ))}
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
