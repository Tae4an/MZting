import React from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/PreviousChatsModal.module.css';

const PreviousChatsModal = ({ show, onClose, chats, mbti }) => {
    if (!show) {
        return null;
    }

    return (
        <div className={styles.PreviousChatsModalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <h2>{mbti}의 이전 채팅방</h2>
                <div className={styles.chatList}>
                    {chats.length > 0 ? (
                        chats.map((chat, index) => (
                            <button key={index} className={styles.chatItemButton}>
                                <p className={styles.chatName}><strong>채팅방 이름:</strong> {chat.name}</p>
                                <p className={styles.chatLastMessage}><strong>마지막 메시지:</strong> {chat.lastMessage}</p>
                            </button>
                        ))
                    ) : (
                        <p>이전 채팅방이 없습니다.</p>
                    )}
                </div>
            </div>
        </div>
    );
};

PreviousChatsModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    chats: PropTypes.arrayOf(
        PropTypes.shape({
            name: PropTypes.string.isRequired,
            lastMessage: PropTypes.string.isRequired,
        })
    ).isRequired,
    mbti: PropTypes.string.isRequired, // 추가: mbti prop 정의
};

export {
    PreviousChatsModal
};
