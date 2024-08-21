import React, { useState, useEffect, useRef, PureComponent } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import styles from '../styles/ResultPage.module.css';
import { sendGetRequest } from "../services";
import { CommentModal } from '../components';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';


const ResultPage = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { chatRoomId, profileDetails } = location.state || {};
    const [isLoading, setIsLoading] = useState(true)
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

    const transformScores = (scores) => {
        return scores.map((score, index) => ({
            name: index + 1,  // 인덱스는 0부터 시작하므로 1을 더해줍니다
            likeability: score
        }));
    };

    useEffect(() => {
        const fetchResult = async () => {
            setIsLoading(true)
            try {
                const response = await sendGetRequest({}, `/api/chat/result/${chatRoomId}`);
                setResult(response);
                setIsLoading(false)
            } catch (error) {
                console.error("Error fetching result:", error);
                setIsLoading(false)
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
        <div className={styles.page}>
            <div className={styles.header}>
                <button className={styles.backButton} onClick={() => navigate('/main')}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <h1 className={styles.title}>#{profileDetails.mbti} 프로필 대화 결과</h1>
            </div>

            <div className={styles.profileCard}>
                <img src={profileDetails.image} className={styles.profileImage} alt="Profile"/>
                <div className={styles.profileInfo}>
                    <h2 className={styles.profileName}>{profileDetails.name}</h2>
                    <p className={styles.personalityType}>#{profileDetails.mbti}</p>
                    <p>나이: {profileDetails.age}</p>
                    <p>키: {profileDetails.height}</p>
                    <p>직업: {profileDetails.job}</p>
                    <p>취미: {Array.isArray(profileDetails.characterHobbies) ? profileDetails.characterHobbies.map(item => item.hobby.hobby).join(', ') : profileDetails.characterHobbies}</p>
                </div>
            </div>

            <button className={styles.actionButton}>대화 로그 보기</button>

            {!isLoading && <div className={styles.scoreCard}>
                <h3>최종 호감도: {result ? result.score : "로딩 중..."}</h3>
                <div className={styles.chartContainer}>
                    <h4>대화 중 호감도 변화</h4>
                    <ResponsiveContainer width="100%" height={300}>
                        <LineChart data={transformScores(result.scores)}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <Tooltip />
                            <Line type="monotone" dataKey="likeability" stroke="#ff6b6b" strokeWidth={3} />
                        </LineChart>
                    </ResponsiveContainer>
                </div>
            </div>}

            <div className={styles.summaryCard}>
                <h3>감정 요약</h3>
                <p>{result ? result.summaryFeel : "로딩 중..."}</p>
                <h3>평가 요약</h3>
                <p>{result ? result.summaryEval : "로딩 중..."}</p>
            </div>

            <button className={styles.reviewButton} onClick={openCommentModal}>댓글 보기</button>

            <CommentModal
                show={isCommentModalOpen}
                onClose={closeCommentModal}
                propsData={{type: profileDetails.type, profileId: profileDetails.profileId}}
            />
        </div>
    );
};

export {ResultPage};