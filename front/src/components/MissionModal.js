import React from 'react';
import styles from '../styles/MissionModal.module.css' // CSS 파일을 import 합니다

const MissionModal = ({ chatCount, onClose }) => {
    const missions = [
        { id: 1, description: "약속 장소 정하기", requiredCount: 10 },
        { id: 2, description: "약속 장소 도착 후 대화 마무리하기", requiredCount: 20 },
        { id: 3, description: "애프터 결정하기", requiredCount: 30 },
    ];

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <h2 className={styles.modalTitle}>미션 리스트</h2>
                <div className={styles.missionList}>
                    {missions.map((mission, index) => {
                        const isCompleted = chatCount >= mission.requiredCount;
                        return (
                            <div key={mission.id} className={styles.missionItem}>
                                <div className={styles.missionNumber}>{index + 1}</div>
                                <span className={styles.missionDescription}>
                                    {mission.description}
                                </span>
                                <div className={`${styles.checkmark} ${isCompleted ? styles.completed : ''}`}>
                                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                                        <polyline points="20 6 9 17 4 12"></polyline>
                                    </svg>
                                </div>
                            </div>
                        );
                    })}
                </div>
                <button onClick={onClose} className={styles.closeModalButton}>
                    닫기
                </button>
            </div>
        </div>
    );
};

export {
    MissionModal
}