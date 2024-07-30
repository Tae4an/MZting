import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { ProfileDetailModal } from '../components';
import ChoiceModal from './ChoiceModal';
import styles from '../styles/ChatBox.module.css';

const ChatBox = ({
                     image,
                     name,
                     profileDetails,
                     messages,
                     onSendMessage,
                     choices,
                     showChoiceModal,
                     onChoiceSelect,
                     onCloseChoiceModal
                 }) => {
    const [showProfileModal, setShowProfileModal] = useState(false);
    const [inputMessage, setInputMessage] = useState('');
    const navigate = useNavigate();

    const handleBackClick = () => {
        navigate(-1);
    };

    const handleProfileClick = () => {
        setShowProfileModal(true);
    };

    const handleCloseProfileModal = () => {
        setShowProfileModal(false);
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (inputMessage.trim()) {
            onSendMessage(inputMessage);
            setInputMessage('');
        }
    };

    return (
        <section className={styles.chatContainer}>
            <header className={styles.chatHeader}>
                <button className={styles.backButton} onClick={handleBackClick}>&lt;</button>
                <img src={image} alt={name} className={styles.avatar} onClick={handleProfileClick} />
                <span className={styles.userName} onClick={handleProfileClick}>{name}</span>
            </header>
            <div className={styles.situationDescription}>
                상황 설명: 간략한 상황에 대한 설명 또는 미션 부여<br />
                (예: 당신은 주선자의 소개를 통해 연락이 닿았습니다.)
            </div>
            <div className={styles.messageContainer}>
                {messages && messages.map((message, index) => (
                    <ChatBubble
                        key={index}
                        content={message.content}
                        isSent={message.isSent}
                        avatar={message.isSent ? null : image}
                    />
                ))}
            </div>
            <form onSubmit={handleSubmit} className={styles.inputArea}>
                <input
                    type="text"
                    value={inputMessage}
                    onChange={(e) => setInputMessage(e.target.value)}
                    placeholder="메시지를 입력하세요"
                    className={styles.inputField}
                />
                <button type="submit" className={styles.sendButton}>전송</button>
            </form>
            {showProfileModal && (
                <ProfileDetailModal
                    show={showProfileModal}
                    onClose={handleCloseProfileModal}
                    profile={profileDetails}
                    showChatButton={false}
                />
            )}
            <ChoiceModal
                show={showChoiceModal}
                choices={choices}
                onChoiceSelect={onChoiceSelect}
                onClose={onCloseChoiceModal}
            />
        </section>
    );
};

const ChatBubble = ({ content, isSent, avatar }) => (
    <div className={`${styles.messageWrapper} ${isSent ? styles.sentMessage : styles.receivedMessage}`}>
        {!isSent && <img src={avatar} alt="Avatar" className={styles.messageAvatar} />}
        <div className={styles.messageBubble}>
            <div className={styles.messageText}>{isSent ? content : content.text}</div>
            {!isSent && (
                <div className={styles.messageInfo}>
                    <p>Feel: {content.feel}</p>
                    <p>Evaluation: {content.evaluation}</p>
                    <p>Score: {content.score}</p>
                </div>
            )}
        </div>
    </div>
);

ChatBox.propTypes = {
    image: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    profileDetails: PropTypes.object.isRequired,
    messages: PropTypes.array.isRequired,
    onSendMessage: PropTypes.func.isRequired,
    choices: PropTypes.array,
    showChoiceModal: PropTypes.bool,
    onChoiceSelect: PropTypes.func,
    onCloseChoiceModal: PropTypes.func
};

export { ChatBox };