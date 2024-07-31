import React from 'react';
import styles from '../styles/RecommendModal.module.css';

const RecommendModal = ({ show, onClose }) => {
    if (!show) {
        return null;
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <div className={styles.modalHeader}>
                    <h2>추천</h2>
                    <button className={styles.closeButton} onClick={onClose}>X</button>
                </div>
                <div className={styles.modalBody}>
                    <p>추천 내용을 여기에 추가하세요.</p>
                </div>
            </div>
        </div>
    );
};

export { RecommendModal };
