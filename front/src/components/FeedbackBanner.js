import React from 'react';
import styles from '../styles/FeedbackBanner.module.css';

const FeedbackBanner = ({ feel, evaluation,  chatDiff }) => {
    let sentiment = chatDiff > 0 ? "↑" : (chatDiff < 0 ? "↓" : "");

    return (
        <div className={styles.feedbackBanner}>
            <p>
                <span className={styles.evaluation}>{evaluation}</span>
                {' '}
                <span className={styles.feel}>({feel})</span>
                {chatDiff !== 0 && (
                    <span className={styles.sentiment}> 호감도{sentiment}</span>
                )}
            </p>
        </div>
    );
};

export { FeedbackBanner };