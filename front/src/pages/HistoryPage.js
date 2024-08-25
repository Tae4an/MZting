import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import styles from '../styles/HistoryPage.module.css';
import { sendGetRequest } from "../services";

const HistoryPage = () => {
    const navigate = useNavigate();
    const mainContentRef = useRef(null);
    const [scrollPosition, setScrollPosition] = useState(0);
    const [chatRooms, setChatRooms] = useState([]);
    const [profiles, setProfiles] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchData();
        window.addEventListener('scroll', handleScroll);
        window.addEventListener('resize', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
            window.removeEventListener('resize', handleScroll);
        };
    }, []);

    const fetchData = async () => {
        setLoading(true);
        setError(null);
        try {
            const chatRoomsResponse = await sendGetRequest({}, "/api/chatroom/list/all")
            setChatRooms(chatRoomsResponse);

            const uniqueProfileIds = [...new Set(chatRoomsResponse.map(room => room.profileId))];
            const profilesData = {};

            await Promise.all(uniqueProfileIds.map(async (id) => {
                try {
                    const profileResponse = await axios.get(`/api/profiles/${id}`, {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    });
                    profilesData[id] = profileResponse.data;
                } catch (profileError) {
                    console.error(`프로필 ID ${id}를 가져오는 데 실패했습니다:`, profileError);
                }
            }));

            setProfiles(profilesData);
        } catch (error) {
            console.error('데이터를 불러오는 데 실패했습니다:', error);
            setError('데이터를 불러오는 데 실패했습니다. 다시 시도해 주세요.');
        } finally {
            setLoading(false);
        }
    };

    const handleScroll = () => {
        if (mainContentRef.current) {
            const position = window.pageYOffset;
            setScrollPosition(position);
        }
    };

    const handleActionClick = async (chatRoom) => {
        const profileDetails = profiles[chatRoom.profileId];
        console.log('Profile details:', profileDetails);  // Debugging log

        if (profileDetails) {
            if (chatRoom.result === null) {
                // For "이어서 대화하기" (Continue Conversation)
                try {
                    const response = await axios.get(`/api/chatroom/entry/${chatRoom.id}`, {
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    });

                    navigate(`/chat`, {
                        state: {
                            chatRoomId: chatRoom.id,
                            selectedProfile: {
                                ...profileDetails,
                                image: chatRoom.profileImage,
                            },
                            chatHistory: response.data,
                            isFirst: false
                        }
                    });
                } catch (error) {
                    console.error('채팅 내역을 불러오는 데 실패했습니다:', error);
                    alert('채팅 내역을 불러오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                // For "대화 결과 보기" (View Conversation Results)
                navigate(`/result`, {
                    state: {
                        chatRoomId: chatRoom.id,
                        profileDetails: {
                            ...profileDetails,
                            image: chatRoom.profileImage,
                        }
                    }
                });
            }
        } else {
            console.error(`프로필 ID ${chatRoom.profileId}에 대한 정보를 찾을 수 없습니다.`);
            // 사용자에게 오류 메시지 표시
            alert('프로필 정보를 가져오는 데 실패했습니다. 잠시 후 다시 시도해 주세요.');
        }
    };

    const getStatus = (chatRoom) => {
        return chatRoom.result === null ? "진행중" : "완료";
    };

    const getActionText = (chatRoom) => {
        return chatRoom.result === null ? "이어서 대화하기" : "대화 결과 보기";
    };

    if (loading) return <div>로딩 중...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div ref={mainContentRef} className={styles.page}>
            <header className={styles.Historyheader}>
                <button className={styles.backButton} onClick={() => navigate(-1)}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <h1 className={styles.title}>History</h1>
            </header>
            <div className={styles.content}>
                {chatRooms.map((chatRoom) => (
                    <div key={chatRoom.id} className={styles.card}>
                        <div className={styles.cardContent}>
                            <div className={styles.conversationInfo}>
                                <div className={styles.avatarWrapper}>
                                    <img loading="lazy" src={chatRoom.profileImage} alt="" className={styles.avatar} />
                                </div>
                                <div className={styles.titleWrapper}>
                                    <h2 className={styles.conversationTitle}>{chatRoom.name}</h2>
                                </div>
                            </div>
                            <div className={styles.statusWrapper}>
                                <div className={styles.status}>{getStatus(chatRoom)}</div>
                            </div>
                        </div>
                        <button
                            className={styles.actionButton}
                            onClick={() => handleActionClick(chatRoom)}
                        >
                            {getActionText(chatRoom)}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export {
    HistoryPage
};