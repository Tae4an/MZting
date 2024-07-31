import React, { useState, useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { ProfileDetailModal, TypingIndicator } from '../components';
import styles from '../styles/ChatBox.module.css';
import ChoiceModal from './ChoiceModal';
import FeedbackBanner from "./FeedbackBanner";

const ChatBox = ({
                     image,
                     name,
                     profileDetails,
                     messages,
                     onSendMessage,
                     isActualMeeting
                 }) => {
    const [showProfileModal, setShowProfileModal] = useState(false);
    const [inputMessage, setInputMessage] = useState('');
    const [isTyping, setIsTyping] = useState(false);
    const navigate = useNavigate();
    const messagesEndRef = useRef(null);
    const [prevScore, setPrevScore] = useState(0);



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
            setIsTyping(true); // TypingIndicator 표시 시작
        }
    };

    useEffect(() => {
        const lastMessage = messages[messages.length - 1];
        if (lastMessage && !lastMessage.isSent) {
            setIsTyping(false); // AI 응답이 오면 TypingIndicator 중지
        }
    }, [messages]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages, isTyping]);

    return (
        <section className={styles.chatContainer}>
            <header className={styles.chatHeader}>
                <button className={styles.backButton} onClick={handleBackClick}>&lt;</button>
                <img src={image} alt={name} className={styles.avatar} onClick={handleProfileClick}/>
                <span className={styles.userName} onClick={handleProfileClick}>{name}</span>
            </header>
            <div className={styles.situationDescription}>
                {isActualMeeting
                    ? "약속 장소에서 만난 상황입니다. 자연스럽게 대화를 이어가세요."
                    : "상황 설명: 간략한 상황에 대한 설명 또는 미션 부여 (예: 당신은 주선자의 소개를 통해 연락이 닿았습니다.)"}
            </div>
            <div className={styles.messageContainer}>
                {messages && messages.map((message, index) => {
                    if (!message.isSent && message.content && typeof message.content === 'object') {
                        const currentScore = message.content.score || 0;

                        return (
                            <React.Fragment key={index}>
                                <ChatBubble
                                    content={message.content.text}
                                    isSent={message.isSent}
                                    avatar={message.isSent ? null : image}
                                />
                                <FeedbackBanner
                                    feel={message.content.feel}
                                    score={currentScore}
                                    evaluation={message.content.evaluation}
                                    prevScore={prevScore}
                                />
                            </React.Fragment>
                        );

                        // 점수 업데이트
                        setPrevScore(currentScore);
                    } else {
                        return (
                            <ChatBubble
                                key={index}
                                content={message.content}
                                isSent={message.isSent}
                                avatar={message.isSent ? null : image}
                            />
                        );
                    }
                })}
                {isTyping && (
                    <div className={styles.messageWrapper} style={{justifyContent: 'flex-start'}}>
                        <img src={image} alt="Avatar" className={styles.messageAvatar}/>
                        <TypingIndicator/>
                    </div>
                )}
                <div ref={messagesEndRef}/>
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
        </section>
    );
};

const ChatBubble = ({content, isSent, avatar}) => (
    <div className={`${styles.messageWrapper} ${isSent ? styles.sentMessage : styles.receivedMessage}`}>
        {!isSent && <img src={avatar} alt="Avatar" className={styles.messageAvatar}/>}
        <div className={styles.messageBubble}>
            <div className={styles.messageText}>
                {typeof content === 'string' ? content : (content.text || '')}
            </div>
        </div>
    </div>
);

ChatBox.propTypes = {
    image: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    profileDetails: PropTypes.object.isRequired,
    messages: PropTypes.array.isRequired,
    onSendMessage: PropTypes.func.isRequired,
};

export {ChatBox};
