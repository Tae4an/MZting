import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import styles from '../styles/ResultPage.module.css';
import image2 from '../assets/Images/image2.jpg';
import { sendGetRequest } from "../services";
import { CommentModal } from '../components/CommentModal';

const chatRoomId = 1;

const ResultPage = () => {
    const navigate = useNavigate();
    const mainContentRef = useRef(null);
    const [result, setResult] = useState(null);
    const [scrollPosition, setScrollPosition] = useState(0);
    const [isCommentModalOpen, setCommentModalOpen] = useState(false);

    const handleScroll = () => {
        const totalScrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const currentScrollPosition = window.scrollY;
        const maxScrollIndicatorPosition = window.innerHeight - 129;

        let scrollIndicatorPosition = (currentScrollPosition / totalScrollHeight) * maxScrollIndicatorPosition;

        if (scrollIndicatorPosition > maxScrollIndicatorPosition) {
            scrollIndicatorPosition = maxScrollIndicatorPosition;
        }

        setScrollPosition(scrollIndicatorPosition);
    };

    useEffect(() => {
        const fetchResult = async () => {
            try {
                const response = await sendGetRequest({}, `/api/chat/result/${chatRoomId}`);
                setResult(response);
            } catch (error) {
                console.error("Error fetching result:", error);
            }
        };

        fetchResult();

        window.addEventListener('scroll', handleScroll);
        window.addEventListener('resize', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
            window.removeEventListener('resize', handleScroll);
        };
    }, []);

    const openCommentModal = () => {
        setCommentModalOpen(true);
    };

    const closeCommentModal = () => {
        setCommentModalOpen(false);
    };

    return (
        <div ref={mainContentRef} className={styles.page}>
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
                <div className={styles.compatibilityScore}>호감도 : {result ? result.score : "로딩 중..."}</div>
            </div>
            <div className={styles.summaryCard}>
                <h3 style={{ fontStyle: "italic" }}>감정 요약</h3>
                <p>{result ? result.summaryFeel : "로딩 중..."}</p>
                <h3 style={{ fontStyle: "italic" }}>평가 요약</h3>
                <p>{result ? result.summaryEval : "로딩 중..."}</p>
            </div>
            <div className={styles.reviewButton} onClick={openCommentModal}>댓글 보기</div>
            <div
                className={styles.scrollIndicator}
                style={{
                    top: `${scrollPosition}px`,
                    left: `${mainContentRef.current ? mainContentRef.current.getBoundingClientRect().right - 19 : 0}px`
                }}
            />
            <CommentModal show={isCommentModalOpen} onClose={closeCommentModal} mbti="ENFJ" />
        </div>
    );
};

export {
    ResultPage
};
