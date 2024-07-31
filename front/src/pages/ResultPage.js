import React from "react";
import { useNavigate } from "react-router-dom";
import styles from '../styles/ResultPage.module.css'
import image2 from '../assets/Images/image2.jpg'

const ResultPage = () => {
    const navigate = useNavigate();

    return (
        <div className={styles.page}>
            <div className={styles.header}>
                <button className={styles.backButton} onClick={() => navigate(-1)}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <img src={image2} className={styles.profileImage} alt="Profile" />
                <div className={styles.title}>#ENFJ 와의 대화 결과</div>
            </div>
            <div className={styles.profileCard}>
                <div className={styles.profileHeader}>
                    <div className={styles.profileName}>이름 : 백지헌</div>
                    <div className={styles.personalityType}>#ENFJ</div>
                </div>
                <div className={styles.profileDetails}>
                    나이 : 25<br />
                    키 : 170<br />직업 : 마케터<br />
                    취미 : TED 강연 참여, 토론, 연극 보기, 새로운 사람 만나기
                </div>
            </div>
            <div className={styles.actionButton}>대화 로그 보기</div>
            <div className={styles.scoreCard}>
                <div className={styles.compatibilityScore}>궁합 점수 : ㅇㅇ</div>
                <div className={styles.finalScore}>최종 호감도 : ㅇㅇ</div>
            </div>
            <div className={styles.reviewButton}>대화 후기 보기</div>
        </div>
    );
}

export {
    ResultPage
}
