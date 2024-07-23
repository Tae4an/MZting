import React from 'react';
import styles from '../styles/MainPage.module.css'; // 스타일 파일 경로 확인

const ProfileCard = ({ image, name, type, tags, onClick }) => {
    return (
        <div className={styles.profileCard} onClick={onClick}>
            <img src={image} alt={name} className={styles.profileImage} />
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
