import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/MainPage.module.css';
import { ProfileCard, ProfileDetailModal, LoadingSpinner, QuestionnaireRecommendation } from "../components";
import { sendGetRequest } from "../services/sendMessage";
import 'bootstrap-icons/font/bootstrap-icons.css';

// 이미지 동적 import 함수
function importAll(r) {
    let images = {};
    r.keys().forEach((item) => { images[item.replace('./','')] = r(item); });
    return images;
}

// 이미지를 동적으로 import
const images = importAll(require.context('../assets/Images', false, /\.(png|jpe?g|svg)$/));

const MainPage = () => {
    const [scrollPosition, setScrollPosition] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [profileData, setProfileData] = useState([]);
    const [showQuestionnaireModal, setShowQuestionnaireModal] = useState(false);
    const [recommendedMBTIs, setRecommendedMBTIs] = useState([]);
    const mainContentRef = useRef(null);
    const navigate = useNavigate();
    const [isMbtiSorted, setIsMbtiSorted] = useState(false);
    const [originalProfileOrder, setOriginalProfileOrder] = useState([]);

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

    const handleMyMBTIClick = async () => {
        if (isMbtiSorted) {
            // 원래 정렬 상태로 돌아가기
            setProfileData([...originalProfileOrder]);
            setIsMbtiSorted(false);
        } else {
            try {
                setIsLoading(true);
                const data = await sendGetRequest({}, "/api/recommend/compatibility/INFJ");  // INFJ로 하드코딩
                const { soulMate, good, worst } = data.compatibilityGroups;

                // 현재 프로필 순서 저장
                setOriginalProfileOrder([...profileData]);

                // 프로필 데이터 재정렬
                const sortedProfiles = [...profileData].sort((a, b) => {
                    const aType = a.type.replace('#', '').toLowerCase();
                    const bType = b.type.replace('#', '').toLowerCase();

                    // 호환성 순위를 가져오는 도우미 함수
                    const getCompatibilityRank = (mbti) => {
                        if (soulMate.includes(mbti)) return 0;
                        if (good.includes(mbti)) return 1;
                        if (worst.includes(mbti)) return 3;
                        return 2; // 어느 목록에도 없는 유형의 경우
                    };

                    const aRank = getCompatibilityRank(aType);
                    const bRank = getCompatibilityRank(bType);

                    return aRank - bRank;
                });

                setProfileData(sortedProfiles);
                setRecommendedMBTIs([...soulMate, ...good]); // 추천 MBTI 업데이트
                setIsMbtiSorted(true);
            } catch (error) {
                console.error("handleMyMBTI에서 오류 발생:", error);
            } finally {
                setIsLoading(false);
            }
        }
    };

    const handleQuestionnaireClick = () => {
        setShowQuestionnaireModal(true);
    };

    const handleRecommendationComplete = (recommendation) => {
        setRecommendedMBTIs(recommendation);

        // 프로필 데이터 재정렬
        const sortedProfiles = [...profileData].sort((a, b) => {
            const aRecommended = recommendation.includes(a.type.replace('#', ''));
            const bRecommended = recommendation.includes(b.type.replace('#', ''));
            if (aRecommended && !bRecommended) return -1;
            if (!aRecommended && bRecommended) return 1;
            return 0;
        });

        setProfileData(sortedProfiles);
        setShowQuestionnaireModal(false);
    };

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        window.addEventListener('resize', handleScroll);

        const extractProfile = async () => {
            try {
                const data = await sendGetRequest({}, "/api/profiles");
                const transformedData = transformProfileData(data);
                setProfileData(transformedData);

                setIsLoading(false);
            } catch (error) {
                console.error("Failed to fetch profiles", error);
                setIsLoading(false);
            }
        }

        extractProfile()

        return () => {
            window.removeEventListener('scroll', handleScroll);
            window.removeEventListener('resize', handleScroll);
        };
    }, []);

    const loadChatRoomData = async (profileId) => {
        try {
            const chatRoomData = await sendGetRequest({}, `/api/chatroom/list/${profileId}`);
            console.log(chatRoomData);
            return chatRoomData;
        } catch (error) {
            console.error("채팅방 데이터를 불러오는 데 실패했습니다.", error);
            return [];
        }
    };

    const handleProfileClick = (profile) => {
        setSelectedProfile(profile);
        setShowModal(true);
    };

    const handleStartChat = async () => {
        const chatRoomId = await sendGetRequest({}, `/api/chatroom/create/${selectedProfile.id}`);
        const isFirst = true;
        console.log(chatRoomId);
        navigate('/chat', {
            state: {
                selectedProfile,
                chatRoomId,
                isFirst
            }
        });
    };

    const handleHistoryClick = () => {
        navigate('/history');
    };

    const handleSettingsClick = () => {
        navigate('/settings');
    };

    return (
        <div ref={mainContentRef} className={styles.mainContent}>
            <header className={styles.header}>
                <div className={styles.mainTitleContainer}>
                    <h1 className={styles.mainTitle}>mzting</h1>
                    <div className={styles.iconContainer}>
                        <button className={styles.iconButton} onClick={handleHistoryClick}>
                            <i className="bi bi-chat-dots-fill"></i>
                        </button>
                        <button className={styles.iconButton} onClick={handleSettingsClick}>
                            <i className="bi bi-gear-fill"></i>
                        </button>
                    </div>
                </div>
                <div className={styles.subTitleContainer}>
                    <button
                        className={`${styles.subtitleButton} ${isMbtiSorted ? styles.activeMbtiButton : ''}`}
                        onClick={handleMyMBTIClick}
                    >
                        {isMbtiSorted ? '자동추천' : '자동추천'}
                    </button>
                    <button className={styles.subtitleButton} onClick={handleQuestionnaireClick}>선택지 MBTI 추천</button>
                </div>
            </header>
            {isLoading ? (
                <LoadingSpinner />
            ) : (
                <div className={styles.profileGrid}>
                    {profileData.map((profile) => (
                        <ProfileCard
                            key={profile.id}
                            {...profile}
                            onClick={() => handleProfileClick(profile)}
                            isRecommended={recommendedMBTIs.includes(profile.type.replace('#', ''))}
                        />
                    ))}
                </div>
            )}
            {showModal && (
                <ProfileDetailModal
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    profile={selectedProfile}
                    onClick={handleStartChat}
                    showChatButton={true}
                    loadChatRoomData={loadChatRoomData}
                />
            )}
            <QuestionnaireRecommendation
                show={showQuestionnaireModal}
                onClose={() => setShowQuestionnaireModal(false)}
                onRecommendationComplete={handleRecommendationComplete}
            />
            <div
                className={styles.scrollIndicator}
                style={{
                    top: `${scrollPosition}px`,
                    left: `${mainContentRef.current ? mainContentRef.current.getBoundingClientRect().right - 19 : 0}px`
                }}
            />
        </div>
    );
};

const transformProfileData = (data) => {
    return data.map((profile, index) => ({
        id: profile.profileId,
        image: images[profile.characterImage] || images[`image${index + 1}.jpg`],
        name: profile.name,
        type: `#${profile.mbti}`,
        tags: profile.characterKeywords.map(keyword => keyword.keyword.keyword).join(' '),
        age: profile.age,
        height: profile.height,
        job: profile.job,
        hobbies: profile.characterHobbies.map(hobby => hobby.hobby.hobby).join(', '),
        description: profile.description
    }));
};

export { MainPage };