import React, { useState, useEffect } from 'react';
import { sendGetRequest, sendPostRequest } from '../services'
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';
import 'bootstrap-icons/font/bootstrap-icons.css'; // Bootstrap Icons CSS 포함

// const fetchedComments = [
//     { id: 1, user: 'User1', comment: '좋아요!', rating: '좋아요' },
//     { id: 2, user: 'User2', comment: '별로에요.', rating: '싫어요' },
//     { id: 3, user: '슬픈 공대생', comment: '실제로 연예인과 대화한다면 이런 느낌일까..', rating: '좋아요' }
// ];

const CommentModal = ({ show, onClose, mbti }) => {
    const [comment, setComment] = useState('');
    const [rating, setRating] = useState(true); // "좋아요" 또는 "싫어요"로만 구분
    const [comments, setComments] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if (show) {
            fetchComments();
        }
    }, [show]);

    const fetchComments = async () => {
        const fetchedComments = await sendGetRequest({}, '/api/posts/1/comments')
        const extractedComments = fetchedComments.commentInfos
        console.log(extractedComments)
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
            userId : 2,
            content : comment,
            isLike : rating
        }
        try {
            await sendPostRequest(data, "/api/posts/1/comments")
            alert("댓글 저장 완료");
        } catch (e) {
            alert("댓글 저장 실패");
        }
    }

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
                            <button type="submit" onClick={handleRequestComment} className={styles.submitButton}>제출</button>
                        </div>
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={handleLike}
                                className={`${styles.ratingButton} ${rating === '좋아요' ? styles.selected : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up-fill"></i> {/* 좋아요 아이콘 */}
                            </button>
                            <button
                                type="button"
                                onClick={handleDislike}
                                className={`${styles.ratingButton} ${rating === '싫어요' ? styles.selected : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down-fill"></i> {/* 싫어요 아이콘 */}
                            </button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>댓글 및 후기</h3>
                        <div className={styles.commentsContainer}>
                            {comments.map(({ id, username, content, isLike }) => (
                                <div key={id} className={styles.comment}>
                                    <p><strong>{username}</strong></p>
                                    <p>{content}</p>
                                    <p>{isLike === true ? '👍 좋아요' : '👎 싫어요'}</p>
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
