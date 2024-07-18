import React from 'react';
import styles from '../styles/ChatBox.module.css';

const ChatBox = () => (
    <section className={styles.chatContainer}>
        <header className={styles.chatHeader}>
            <div className={styles.userInfo}>
                <span className={styles.navArrow}>&lt;</span>
                <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/8f5dd0d11485f72df622564a688aea72f15bc048e514e524c65571bb0142fcc1?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&" alt="User avatar" className={styles.avatar} />
                <span className={styles.userName}>이지은</span>
                <span className={styles.navArrow}>&gt;</span>
            </div>
            <div className={styles.statusIndicator} />
        </header>
        <p className={styles.situationDescription}>
            상황 설명 상황 설명 설명 상황 설명 상황 끄어엄 끄어어엄<br />
            (ex 당신은 주선자의 소개를 통해 연락이 닿았습니다.)<br />
        </p>
        <div className={styles.messageContainer}>
            <ChatBubble
                content="내용내용내용내용내용"
                isSent={false}
                avatar="https://cdn.builder.io/api/v1/image/assets/TEMP/8f5dd0d11485f72df622564a688aea72f15bc048e514e524c65571bb0142fcc1?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&"
            />
            <ChatBubble
                content="대화내용"
                isSent={false}
                avatar="https://cdn.builder.io/api/v1/image/assets/TEMP/8f5dd0d11485f72df622564a688aea72f15bc048e514e524c65571bb0142fcc1?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&"
            />
            <ChatBubble
                content="내용대화"
                isSent={true}
            />
        </div>
    </section>
);

const ChatBubble = ({ content, isSent, avatar }) => (
    <div className={isSent ? styles.messageSent : styles.receivedMessage}>
        {!isSent && (
            <img src={avatar} alt="Message avatar" className={styles.messageAvatar} />
        )}
        <div className={styles.message}>{content}</div>
    </div>
);

export {
    ChatBox
};