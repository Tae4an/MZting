import React from 'react';
import styles from '../styles/FeedbackBanner.module.css';

const FeedbackBanner = ({ feel, score, evaluation, prevScore }) => {
    const scoreDiff = score - prevScore;
    let sentiment = scoreDiff > 0 ? "↑" : (scoreDiff < 0 ? "↓" : "");

    return (
        <div className={styles.feedbackBanner}>
            <p>
                <span className={styles.evaluation}>{evaluation}</span>
                {' '}
                <span className={styles.feel}>({feel})</span>
                {scoreDiff !== 0 && (
                    <span className={styles.sentiment}> 호감도{sentiment}</span>
                )}
            </p>
        </div>
    );
};

export default FeedbackBanner;