import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { ProfileDetailModal } from "../components/ProfileDetailModal";

const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, tags } = location.state || {};
    const [showModal, setShowModal] = useState(false);

    const handleProfileClick = () => {
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setShowModal(false);
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
                    profile={{ image: image, name, type, tags }}
                />
            )}
        </main>
    );
};

export { ChatPage };
