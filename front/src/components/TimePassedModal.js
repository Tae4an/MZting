import React from 'react';
import styles from '../styles/TimePassedModal.module.css';

const TimePassedModal = ({ isOpen, message }) => {
    if (!isOpen) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <p>{message}</p>
                <div className={styles.dots}>
                    <span>.</span>
                    <span>.</span>
                    <span>.</span>
                </div>
            </div>
        </div>
    );
};

export default TimePassedModal;