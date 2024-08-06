import React, { useState, useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { ProfileDetailModal, TypingIndicator } from '../components';
import styles from '../styles/ChatBox.module.css';
import FeedbackBanner from "./FeedbackBanner";
import SituationBanner from "./SituationBanner";


const ChatBox = ({
                     image,
                     name,
                     profileDetails,
                     messages,
                     onSendMessage,
                     stages,
                     isActualMeeting,
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
            <SituationBanner stages={stages} isActualMeeting={isActualMeeting} />
            <div className={styles.messageContainer}>
                {messages.map((message, index) => (
                    <React.Fragment key={index}>
                        <ChatBubble
                            content={typeof message.content === 'string' ? message.content : message.content.text}
                            isSent={message.isSent}
                            avatar={message.isSent ? null : image}
                        />
                        {!message.isSent && message.isLastInGroup && message.content.feel && (
                            <FeedbackBanner
                                feel={message.content.feel}
                                score={message.content.score}
                                evaluation={message.content.evaluation}
                                prevScore={index > 0 ? (messages[index - 1].content.score || 0) : 0}
                            />
                        )}
                    </React.Fragment>
                ))}
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
    stages: PropTypes.shape({
        stage1Complete: PropTypes.bool.isRequired,
        stage2Complete: PropTypes.bool.isRequired,
        stage3Complete: PropTypes.bool.isRequired,
    }).isRequired,
    isActualMeeting: PropTypes.bool.isRequired
};

export {ChatBox};
