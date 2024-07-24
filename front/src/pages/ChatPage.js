import React, { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox, ProfileDetailModal } from '../components';

const ChatPage = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const { image, name, type, tags } = location.state || {};
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        console.log('ChatPage loaded with state:', { image, name, type, tags });
    }, [location.state]);

    const handleProfileClick = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    const handleStartChat = () => {
        setShowModal(false);
        navigate('/chat', { state: { image, name, type, tags } });
    };

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox
                    image={image}
                    name={name}
                    type={type}
                    tags={tags}
                    onProfileClick={handleProfileClick}
                />
            </div>
            {showModal && (
                <ProfileDetailModal
                    show={showModal}
                    onClose={handleCloseModal}
                    profile={{ image, name, type, tags }}
                    onClick={handleStartChat} // onStartChat을 onClick으로 변경
                />
            )}
        </main>
    );
};

export {
    ChatPage
};
