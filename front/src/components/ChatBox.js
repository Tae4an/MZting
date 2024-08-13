import React, { useState, useEffect, useRef } from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import { ProfileDetailModal, TypingIndicator, FeedbackBanner, SituationBanner } from '../components';
import styles from '../styles/ChatBox.module.css';

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
    const [isTyping, setIsTyping] = useState(true);  // 초기값을 true로 설정
    const navigate = useNavigate();
    const messagesEndRef = useRef(null);

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

    const textareaRef = useRef(null);

    const adjustTextareaHeight = () => {
        const textarea = textareaRef.current;
        if (textarea) {
            textarea.style.height = 'auto';
            textarea.style.height = `${Math.min(textarea.scrollHeight, 120)}px`;
        }
    };

    const handleInputChange = (e) => {
        setInputMessage(e.target.value);
        adjustTextareaHeight();
    };

    const handleKeyPress = (e) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSubmit(e);
        }
    };

    useEffect(() => {
        if (messages.length > 0) {
            const lastMessage = messages[messages.length - 1];
            if (!lastMessage.isSent) {
                setIsTyping(false);
            }
        }
    }, [messages]);

    const scrollToBottom = () => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    };

    useEffect(() => {
        scrollToBottom();
    }, [messages, isTyping]);

    return (
        <div className={styles.chatContainer}>
            <header className={styles.chatHeader}>
                <button className={styles.backButton} onClick={handleBackClick}>&lt;</button>
                <img src={image} alt={name} className={styles.avatar} onClick={handleProfileClick}/>
                <span className={styles.userName} onClick={handleProfileClick}>{name}</span>
            </header>
            <SituationBanner stages={stages} isActualMeeting={isActualMeeting}/>
            <div className={styles.messageContainer}>
                {messages.map((message, index) => {
                    const showAvatar = !message.isSent && (index === 0 || messages[index - 1].isSent);
                    return (
                        <React.Fragment key={index}>
                            <ChatBubble
                                content={typeof message.content === 'string' ? message.content : message.content.text}
                                isSent={message.isSent}
                                avatar={image}
                                showAvatar={showAvatar}
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
                    );
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
                <textarea
                    ref={textareaRef}
                    value={inputMessage}
                    onChange={handleInputChange}
                    onInput={adjustTextareaHeight}
                    onKeyPress={handleKeyPress}
                    placeholder="메시지를 입력하세요"
                    className={styles.inputField}
                    rows={1}
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
        </div>
    );
};

const ChatBubble = ({content, isSent, avatar, showAvatar}) => (
    <div className={`${styles.messageWrapper} ${isSent ? styles.sentMessage : styles.receivedMessage}`}>
        {!isSent && showAvatar && (
            <div className={styles.avatarContainer}>
                <img src={avatar} alt="Avatar" className={styles.messageAvatar}/>
            </div>
        )}
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

export { ChatBox };
