import React, { useState, useEffect, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import styles from '../styles/ResultPage.module.css';
import { sendGetRequest } from "../services";
import { CommentModal } from '../components';

const ResultPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { chatRoomId, profileDetails } = location.state || {};
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
    }, [chatRoomId]);

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
                <img src={profileDetails.image} className={styles.profileImage} alt="Profile" />
                <div className={styles.title}>#{profileDetails.type} 와의 대화 결과</div>
            </div>
            <div className={styles.profileCard}>
                <div className={styles.profileHeader}>
                    <div className={styles.profileName}>이름 : {profileDetails.name}</div>
                    <div className={styles.personalityType}>#{profileDetails.type}</div>
                </div>
                <div className={styles.profileDetails}>
                    나이 : {profileDetails.age}<br />
                    키 : {profileDetails.height}<br />
                    직업 : {profileDetails.job}<br />
                    취미 : {Array.isArray(profileDetails.hobbies) ? profileDetails.hobbies.join(', ') : profileDetails.hobbies}
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
            <CommentModal
                show={isCommentModalOpen}
                onClose={closeCommentModal}
                propsData={{ type: profileDetails.type, profileId: chatRoomId }}
            />
        </div>
    );
};

export { ResultPage };