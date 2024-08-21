import React, { useState, useEffect } from 'react';
import styles from '../styles/IntroductionModal.module.css';

const IntroductionModal = ({ isOpen, onClose, profileDetails }) => {
    const [displayedText, setDisplayedText] = useState([]);
    const [stepIndex, setStepIndex] = useState(0);
    const [charIndex, setCharIndex] = useState(0);
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
        if (isOpen && stepIndex < introSteps.length) {
            const currentStep = introSteps[stepIndex];
            if (charIndex < currentStep.length) {
                const timeoutId = setTimeout(() => {
                    const nextChar = currentStep[charIndex];
                    let styledChar = nextChar;

                    const nameIndex = currentStep.indexOf(profileDetails.name);
                    const ageIndex = currentStep.indexOf(profileDetails.age.toString());
                    const jobIndex = currentStep.indexOf(profileDetails.job);
                    const hobbiesIndex = currentStep.indexOf(profileDetails.hobbies);

                    if (nameIndex !== -1 && charIndex >= nameIndex && charIndex < nameIndex + profileDetails.name.length) {
                        styledChar = <span className={styles.highlight} key={`name-${charIndex}`}>{nextChar}</span>;
                    } else if (ageIndex !== -1 && charIndex >= ageIndex && charIndex < ageIndex + profileDetails.age.toString().length) {
                        styledChar = <span className={styles.highlight} key={`age-${charIndex}`}>{nextChar}</span>;
                    } else if (jobIndex !== -1 && charIndex >= jobIndex && charIndex < jobIndex + profileDetails.job.length) {
                        styledChar = <span className={styles.highlight} key={`job-${charIndex}`}>{nextChar}</span>;
                    } else if (hobbiesIndex !== -1 && charIndex >= hobbiesIndex && charIndex < hobbiesIndex + profileDetails.hobbies.length) {
                        styledChar = <span className={styles.highlight} key={`hobbies-${charIndex}`}>{nextChar}</span>;
                    }

                    setDisplayedText(prev => [...prev, styledChar]);
                    setCharIndex(charIndex + 1);
                }, 50); // 타이핑 속도 조정
                return () => clearTimeout(timeoutId);
            } else if (stepIndex < introSteps.length - 1) {
                const timeoutId = setTimeout(() => {
                    setDisplayedText(prev => [...prev, <br key={`br-${stepIndex}`} />]); // 다음 문장 전에 줄 바꿈
                    setStepIndex(stepIndex + 1);
                    setCharIndex(0);
                }, 1000); // 다음 문장으로 넘어가기 전에 대기 시간
                return () => clearTimeout(timeoutId);
            }
        }
    }, [isOpen, stepIndex, charIndex, introSteps, profileDetails]);

    const handleClose = () => {
        setIsClosing(true);
        setTimeout(() => {
            onClose();
            setIsClosing(false);
            setDisplayedText([]); // 모달이 닫힐 때 텍스트 초기화
            setStepIndex(0);
            setCharIndex(0);
        }, 500);
    };

    if (!isOpen && !isClosing) return null;

    return (
        <div className={`${styles.modalOverlay} ${isOpen ? styles.visible : ''} ${isClosing ? styles.closing : ''}`}>
            <div className={`${styles.modalContent} ${isOpen ? styles.visible : ''} ${isClosing ? styles.closing : ''}`}>
                <div className={styles.messageBox}>
                    <div className={styles.messageItem}>
                        {displayedText}
                    </div>
                </div>
                {stepIndex === introSteps.length - 1 && charIndex === introSteps[stepIndex].length && (
                    <button onClick={handleClose} className={styles.closeButton}>
                        소개팅 시작하기
                    </button>
                )}
            </div>
        </div>
    );
};

export {IntroductionModal};
