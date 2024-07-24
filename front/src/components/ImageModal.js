import React from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/ImageModal.module.css';

const ImageModal = ({ show, onClose, image }) => {
    if (!show) {
        return null;
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <img src={image} alt="Profile" className={styles.image} />
            </div>
        </div>
    );
};

ImageModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    image: PropTypes.string.isRequired,
};

export { ImageModal };
