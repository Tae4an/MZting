import React from 'react';
import styles from '../styles/TypingIndicator.module.css';

const TypingIndicator = () => {
    return (
        <div className={styles.typingIndicator}>
            <img src="/path/to/your/loading-indicator.gif" alt="Typing..." />
        </div>
    );
};

export { TypingIndicator };
