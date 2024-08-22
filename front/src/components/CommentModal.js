import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/CommentModal.module.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import { sendPostRequest, sendGetRequest } from "../services";

const CommentModal = ({ show, onClose, propsData, isResult }) => {
    const [comment, setComment] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [isLike, setIsLike] = useState(null);
    const [comments, setComments] = useState([]);
    const [userReactions, setUserReactions] = useState({});
    const [likeViewState, setLikeViewState] = useState(false);
    const [disLikeViewState, setDisLikeViewState] = useState(false);
    const [totalLikeCount, setTotalLikeCount] = useState(0);
    const [totalDislikeCount, setTotalDislikeCount] = useState(0);

    useEffect(() => {
        if(show && !likeViewState && !disLikeViewState) {
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
        if(!show) {
            const resetState = () => {
                setComment('');
                setIsLoading(false);
                setIsLike(null);
                setComments([]);
                setUserReactions({});
                setLikeViewState(false);
                setDisLikeViewState(false);
                setTotalLikeCount(0);
                setTotalDislikeCount(0);
            };
            resetState()
        }
    }, [show, likeViewState, disLikeViewState])

    const fetchComments = async (requestData) => {
        if (propsData?.profileId) {
            const response = await sendGetRequest(requestData, `/api/comments/${propsData.profileId}/list`);
            setComments(response.commentInfos);
            // Initialize user reactions
            const initialReactions = {};
            response.commentInfos.forEach(comment => {
                initialReactions[comment.id] = { liked: false, disliked: false };
            });
            setUserReactions(initialReactions);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (comment.trim() && isLike !== null) {
            await handleRequestComment();
        }
    };

    const handleLike = async () => {
        if(likeViewState) {
            setLikeViewState(false)
            return
        }
        setLikeViewState(true)
        setDisLikeViewState(false)
        const filter = "Like"
        const page = 0
        const size = 20
        const requestData = {
            page : page,
            size : size,
            filter : filter
        }
        await fetchComments(requestData);
    };

    const handleDislike = async () => {
        if(disLikeViewState) {
            setDisLikeViewState(false)
            return
        }
        setDisLikeViewState(true)
        setLikeViewState(false)
        const filter = "Dislike"
        const page = 0
        const size = 20
        const requestData = {
            page : page,
            size : size,
            filter : filter
        }
        await fetchComments(requestData);
    };

    const handleRequestComment = async () => {
        const commentData = {
            content: comment,
            isLike: isLike,
        };
        try {
            await sendPostRequest(commentData, `/api/comments/${propsData.profileId}/create`);
            const filter = "All"
            const page = 0
            const size = 20
            const requestData = {
                page : page,
                size : size,
                filter : filter
            }
            await fetchComments(requestData); // 댓글 목록 새로고침
            setComment('');
            setIsLike(null);
        } catch (e) {
            alert('댓글 저장 실패');
        }
    };

    const toggleLike = async (id) => {
        try {
            const requestData = {
                isFirst : !userReactions[id]?.liked
            };
            console.log(requestData)
            await sendPostRequest(requestData, `/api/comments/${id}/like`);
            setUserReactions(prev => ({
                ...prev,
                [id]: { liked: !prev[id].liked, disliked: prev[id].disliked }
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
            const requestData = {
                isFirst : !userReactions[id]?.disliked
            }
            await sendPostRequest(requestData, `/api/comments/${id}/dislike`);
            setUserReactions(prev => ({
                ...prev,
                [id]: { liked: prev[id].liked, disliked: !prev[id].disliked }
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
                    <h2 className={styles.modalTitle}>#{propsData?.type || ""} 에 대한 댓글 및 후기</h2>
                    <form onSubmit={handleSubmit} className={styles.modalForm}>
                        {isResult && <div className={styles.textareaAndButtons}>
                            <button
                                type="button"
                                onClick={() => { if(isLike) { setIsLike(null) } else { setIsLike(true)}}}
                                className={`${styles.ratingButton} ${isLike === true ? styles.ratinglike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up" style={{color: "black"}}></i>
                            </button>
                            <button
                                type="button"
                                onClick={() => {if(!isLike) { setIsLike(null) } else { setIsLike(false)}}}
                                className={`${styles.ratingButton} ${isLike === false ? styles.ratingdislike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down" style={{color: "black"}}></i>
                            </button>
                            <textarea
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="댓글을 입력하세요"
                                className={styles.modalTextarea}
                            />
                            <button type="submit" className={styles.submitButton}>
                                제출
                            </button>
                        </div>}
                        <div className={styles.modalRating}>
                            <button
                                type="button"
                                onClick={handleLike}
                                className={`${styles.ratingButton} ${likeViewState === true ? styles.ratinglike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-up" style={{color: "black"}}></i>
                                <span style={{color: "black"}}>12</span>
                            </button>
                            <button
                                type="button"
                                onClick={handleDislike}
                                className={`${styles.ratingButton} ${disLikeViewState === true ? styles.ratingdislike : ''}`}
                            >
                                <i className="bi bi-hand-thumbs-down" style={{color: "black"}}></i>
                                <span style={{color: "black"}}>3</span>
                            </button>
                        </div>
                    </form>
                    <div className={styles.commentsSection}>
                        <h3 className={styles.commentsTitle}>해당 프로필에 대한 댓글</h3>
                        <div className={styles.commentsContainer}>
                            {comments.map(({id, username, content, isLike, likeCount, dislikeCount, cwTime}) => (
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
    propsData: PropTypes.shape({
        type: PropTypes.string,
        profileId: PropTypes.number
    }),
    isResult : PropTypes.bool
};

CommentModal.defaultProps = {
    propsData: {
        type: "",
        profileId: null
    }
};

export { CommentModal };