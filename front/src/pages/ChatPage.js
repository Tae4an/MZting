import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox, ProfileDetailModal } from '../components';

const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};
    const [showModal, setShowModal] = useState(false);

    const handleProfileClick = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
    }, [location.state]);

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox
                    image={image}
                    name={name}
                    onProfileClick={handleProfileClick} // 핸들러 전달
                />
            </div>
            {showModal && (
                <ProfileDetailModal
                    show={showModal}
                    onClose={handleCloseModal}
                    profile={{ image, name, type, age, height, job, hobbies, tags, description }}
                    showChatButton={false} // "대화 시작하기" 버튼 숨기기
                />
            )}
        </main>
    );
};

export {
    ChatPage
};
