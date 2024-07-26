import React, { useState } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';

const CommentModal = ({ show, onClose, mbti }) => {
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(0);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (comment.trim() && rating > 0) {
            // 제출 로직 구현
            console.log({ mbti, comment, rating });
            setComment('');
            setRating(0);
            onClose();
        }
    };

    return (
        show ? (
            <div className={styles.modalOverlay}>
                <div className={styles.modal}>
                    <h2 className={styles.modalTitle}>{mbti}에 대한 댓글 및 후기 작성</h2>
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
