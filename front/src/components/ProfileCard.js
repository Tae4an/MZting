import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/MainPage.module.css';

const ProfileCard = ({ imageUrl, name, type, tags }) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate('/chat');
    };

    return (
        <div className={styles.profileCard} onClick={handleClick}>
            <img src={imageUrl} alt={`Profile of ${name}`} className={styles.profileImage} />
            <div className={styles.profileInfo}>
                <div className={styles.profileName}>{name}</div>
                <div className={styles.profileType}>{type}</div>
            </div>
            <div className={styles.profileTags}>{tags}</div>
        </div>
    );
};

export {
    ProfileCard
};
