import React from 'react';
import { HistoryCard } from './HistoryCard';
import styles from '../styles/HistoryList.module.css'

const HistoryList = ({ conversations }) => {
    return (
        <main className={styles.conversationList}>
            <header className={styles.header}>
                <button className={styles.backButton}>&lt;</button>
                <h1 className={styles.title}>내 대화</h1>
            </header>
            {conversations.map((conversation, index) => (
                <HistoryCard
                    key={index}
                    avatarSrc={conversation.avatarSrc}
                    title={conversation.title}
                    status={conversation.status}
                    actionText={conversation.actionText}
                />
            ))}
        </main>
    );
};

export {
    HistoryList
};