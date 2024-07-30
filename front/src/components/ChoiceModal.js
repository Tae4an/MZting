import React from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/ChoiceModal.module.css';

const ChoiceModal = ({ show, choices, onChoiceSelect, onClose }) => {
    if (!show) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <h2>선택지</h2>
                <ul>
                    {choices.map((choice, index) => (
                        <li key={index}>
                            <button onClick={() => onChoiceSelect(choice)}>{choice}</button>
                        </li>
                    ))}
                </ul>
                <button onClick={onClose}>닫기</button>
            </div>
        </div>
    );
};

ChoiceModal.propTypes = {
    show: PropTypes.bool.isRequired,
    choices: PropTypes.array.isRequired,
    onChoiceSelect: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired
};

export default ChoiceModal;