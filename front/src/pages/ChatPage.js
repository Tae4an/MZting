import React from 'react';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';

const ChatPage = () => {
    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox />
            </div>
        </main>
    );
};

export {
    ChatPage
};