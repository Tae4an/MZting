import React from 'react';
import styles from '../styles/MainPage.module.css';

const ProfileCard = ({ imageUrl, name, type, tags }) => {
    return (
        <div className={styles.profileCard}>
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