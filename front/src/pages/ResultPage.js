import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import styles from '../styles/ResultPage.module.css'
import image2 from '../assets/Images/image2.jpg'
import {sendGetRequest} from "../services";


const chatRoomId = 1;

const ResultPage = () => {
    const navigate = useNavigate();
    const [result, setResult] = useState(null);

    useEffect(() => {
        const fetchResult = async () => {
            try {
                const response = await sendGetRequest({}, `/api/chat/result/${chatRoomId}`)
                setResult(response);
            } catch (error) {
                console.error("Error fetching result:", error);
            }
        };

        fetchResult();
    }, [chatRoomId]);

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
                <div className={styles.compatibilityScore}>궁합 점수 : {result ? result.score : "로딩 중..."}</div>
            </div>
            <div className={styles.summaryCard}>
                <h3>감정 요약</h3>
                <p>{result ? result.summaryFeel : "로딩 중..."}</p>
                <h3>평가 요약</h3>
                <p>{result ? result.summaryEval: "로딩 중..."}</p>
            </div>
            <div className={styles.reviewButton}>댓글 보기</div>
        </div>
    );
}

export {
    ResultPage
}