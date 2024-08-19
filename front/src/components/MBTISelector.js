import React, { useState } from 'react';
import { sendGetRequest } from "../services";
import styles from '../styles/RecommendModal.module.css';

const MBTISelector = ({ show, onClose, onMBTISelect }) => {
    const [selectedMBTI, setSelectedMBTI] = useState('');

    if (!show) return null;

    const mbtiTypes = [
        'INFP', 'INFJ', 'INTP', 'INTJ',
        'ISFP', 'ISFJ', 'ISTP', 'ISTJ',
        'ENFP', 'ENFJ', 'ENTP', 'ENTJ',
        'ESFP', 'ESFJ', 'ESTP', 'ESTJ'
    ];

    const handleMBTISelect = async () => {
        if (selectedMBTI) {
            try {
                const data = await sendGetRequest({}, `/api/recommend/compatibility/${selectedMBTI}`);
                onMBTISelect(data.compatibilityGroups.soulMate);
            } catch (error) {
                console.error("Error in handleMBTISelect:", error);
            }
        }
    };

    return (
        <div className={styles.modal}>
            <div className={styles.modalContent}>
                <h2>MBTI 선택</h2>
                <select
                    value={selectedMBTI}
                    onChange={(e) => setSelectedMBTI(e.target.value)}
                    className={styles.mbtiSelect}
                >
                    <option value="">MBTI 선택</option>
                    {mbtiTypes.map(mbti => (
                        <option key={mbti} value={mbti}>{mbti}</option>
                    ))}
                </select>
                <button onClick={handleMBTISelect} className={styles.optionButton} disabled={!selectedMBTI}>
                    내 MBTI 기반 추천
                </button>
                <p>자신의 MBTI를 선택하고 추천받으세요!</p>
                <button onClick={onClose} className={styles.closeButton}>닫기</button>
            </div>
        </div>
    );
};

export { MBTISelector };