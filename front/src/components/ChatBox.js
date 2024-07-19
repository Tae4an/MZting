import React, { useState } from 'react';
import styles from '../styles/ChatBox.module.css';

const ChatBox = ({ imageUrl, name, type, tags = [], messages, onSendMessage }) => {
    const [inputMessage, setInputMessage] = useState('');

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
                <div className={styles.userInfo}>
                    <span className={styles.navArrow}>&lt;</span>
                    <img src={imageUrl} alt="User avatar" className={styles.avatar} />
                    <span className={styles.userName}>{name || 'Unknown'}</span>
                    <span className={styles.navArrow}>&gt;</span>
                </div>
                <div className={styles.statusIndicator} />
            </header>
            <p className={styles.situationDescription}>
                상황 설명: 간략한 상황에 대한 설명 또는 미션 부여<br />
                (예: 당신은 주선자의 소개를 통해 연락이 닿았습니다.)<br />
            </p>
            <div className={styles.messageContainer}>
                {messages && messages.map((message, index) => (
                    <ChatBubble
                        key={index}
                        content={message.content}
                        isSent={message.isSent}
                        avatar={message.avatar}
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
        </section>
    );
};

const ChatBubble = ({ content, isSent, avatar }) => (
    <div className={`${styles.messageWrapper} ${isSent ? styles.sentMessage : styles.receivedMessage}`}>
        {!isSent && <img src={avatar} alt="Avatar" className={styles.messageAvatar} />}
        <div className={`${styles.messageBubble} ${isSent ? styles.sentMessage : styles.receivedMessage}`}>
            <div className={styles.messageText}>{isSent ? content : <p>text : {content.text}</p>}</div>
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

export {
    ChatBox
};