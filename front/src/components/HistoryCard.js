import React from 'react';
import styles from '../styles/HistroyCard.module.css'

const HistoryCard = ({ avatarSrc, title, status, actionText }) => {
    return (
        <section className={styles.card}>
            <div className={styles.cardContent}>
                <div className={styles.conversationInfo}>
                    <div className={styles.avatarWrapper}>
                        <img loading="lazy" src={avatarSrc} alt="" className={styles.avatar} />
                    </div>
                    <div className={styles.titleWrapper}>
                        <h2 className={styles.title}>{title}</h2>
                    </div>
                </div>
                <div className={styles.statusWrapper}>
                    <div className={styles.status}>{status}</div>
                </div>
            </div>
            <button className={styles.actionButton}>{actionText}</button>
        </section>
    );
};

export {
    HistoryCard
};