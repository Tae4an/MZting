import React from "react";
import styles from '../styles/ResultPage.module.css'

const ResultPage = () => {
    return (
        <div className={styles.container}>
            <div className={styles.content}>
                <div className={styles.mainColumn}>
                    <div className={styles.conversationContainer}>
                        <div className={styles.header}>
                            <div className={styles.backButton}>&lt;</div>
                            <img src="https://cdn.builder.io/api/v1/image/assets/TEMP/ac6c300228a69c666dd5a5766d6b2c3f926f191b74049c3a0e32ea7f07bd0c70?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&" className={styles.profileImage} alt="Profile" />
                            <div className={styles.title}>이지은과의 대화 결과</div>
                        </div>
                        <div className={styles.profileCard}>
                            <div className={styles.profileHeader}>
                                <div className={styles.profileName}>이름 : 이지은</div>
                                <div className={styles.personalityType}>#INFJ</div>
                            </div>
                            <div className={styles.profileDetails}>
                                나이 : 23<br />
                                키 : 165 직업 : 작가<br />
                                취미 : 독서 , 일기, 글쓰기, 음악 감상
                            </div>
                        </div>
                        <div className={styles.actionButton}>대화 로그 보기</div>
                        <div className={styles.scoreCard}>
                            <div className={styles.compatibilityScore}>궁합 점수 : ㅇㅇ</div>
                            <div className={styles.finalScore}>최종 호감도 : ㅇㅇ</div>
                        </div>
                        <div className={styles.reviewButton}>대화 후기 보기</div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export {
    ResultPage
}