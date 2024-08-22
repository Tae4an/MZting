import React from 'react';
import styles from '../styles/TimePassedModal.module.css';
import TimePassIcon from "../assets/timepass.gif";

const TimePassedModal = ({ isOpen, message }) => {
    if (!isOpen) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <p>{message}</p>
                <img className={styles.timePassIcon} src={TimePassIcon } alt="Animated GIF"/>
            </div>
        </div>
    );
};

export {TimePassedModal};