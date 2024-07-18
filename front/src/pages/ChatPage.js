import React from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';

const ChatPage = () => {
    const location = useLocation();
    const { imageUrl, name, type, tags } = location.state || {};

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox imageUrl={imageUrl} name={name} type={type} tags={tags} />
            </div>
        </main>
    );
};

export {
    ChatPage
};
