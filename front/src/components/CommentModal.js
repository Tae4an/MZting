import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';

const CommentModal = ({ show, onClose, mbti }) => {
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(0);
    const [comments, setComments] = useState([]);

    useEffect(() => {
        if (show) {
            // 댓글 및 후기 데이터를 가져오는 API 호출 로직 추가 (예: fetchComments())
            fetchComments();
        }
    }, [show]);

    const fetchComments = async () => {
        // 실제 API 호출로 데이터를 가져오는 로직을 구현
        const fetchedComments = [
            { id: 1, user: 'User1', comment: '좋아요!', rating: 5 },
            { id: 2, user: 'User2', comment: '별로에요.', rating: 2 },
        ];
        setComments(fetchedComments);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (comment.trim() && rating > 0) {
            // 제출 로직 구현
            console.log({ mbti, comment, rating });
            setComments([...comments, { user: '현재 사용자', comment, rating }]);
            setComment('');
            setRating(0);
        }
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
                            <label>
                                평점:
                                <select value={rating} onChange={(e) => setRating(Number(e.target.value))}>
                                    <option value="0">선택하세요</option>
                                    {[1, 2, 3, 4, 5].map(num => (
                                        <option key={num} value={num}>{num}</option>
                                    ))}
                                </select>
                            </label>
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
                                <p>평점: {rating}</p>
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
