import React from 'react';
import PropTypes from 'prop-types';
import styles from "../styles/ImageLogModal.module.css";

const ImageLogModal = ({ show, onClose, image, onApply }) => {
    if (!show) {
        return null;
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <img src={image} alt="Selected" className={styles.modalImage} />
                <button onClick={() => onApply(image)} className={styles.applyButton}>
                    이미지 적용하기
                </button>
            </div>
        </div>
    );
};

ImageLogModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    image: PropTypes.string,
    onApply: PropTypes.func.isRequired,
};

export { ImageLogModal };