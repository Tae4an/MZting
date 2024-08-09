import React, { useState, useEffect } from 'react';
import styles from '../styles/IntroductionModal.module.css';

const IntroductionModal = ({ isOpen, onClose, profileDetails }) => {
    const [displayedSteps, setDisplayedSteps] = useState([]);
    const [isClosing, setIsClosing] = useState(false);
    const introSteps = [
        "안녕! 오늘 소개시켜줄 사람이 있어.",
        `이름은 ${profileDetails.name}이야.`,
        `${profileDetails.age}살이고, ${profileDetails.job}로 일하고 있어.`,
        `취미는 ${profileDetails.hobbies}이래.`,
        "정말 좋은 사람이니까 잘 해봐!",
        "그럼 행운을 빌어!"
    ];

    useEffect(() => {
        if (isOpen && displayedSteps.length < introSteps.length) {
            const timer = setTimeout(() => {
                setDisplayedSteps(prev => [...prev, introSteps[prev.length]]);
            }, 2000);
            return () => clearTimeout(timer);
        }
    }, [isOpen, displayedSteps, introSteps]);

    const handleClose = () => {
        setIsClosing(true);
        setTimeout(() => {
            onClose();
            setIsClosing(false);
        }, 500); // 애니메이션 지속 시간과 일치
    };

    if (!isOpen && !isClosing) return null;

    return (
        <div className={`${styles.modalOverlay} ${isOpen ? styles.visible : ''} ${isClosing ? styles.closing : ''}`}>
            <div className={`${styles.modalContent} ${isOpen ? styles.visible : ''} ${isClosing ? styles.closing : ''}`}>
                <div className={styles.messageBox}>
                    {introSteps.map((step, index) => (
                        <div
                            key={index}
                            className={`${styles.messageItem} ${index < displayedSteps.length ? styles.visible : ''}`}
                        >
                            {step}
                        </div>
                    ))}
                </div>
                {displayedSteps.length === introSteps.length && (
                    <button onClick={handleClose} className={styles.closeButton}>
                        소개팅 시작하기
                    </button>
                )}
            </div>
        </div>
    );
};

export default IntroductionModal;