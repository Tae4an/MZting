import React from 'react';
import styles from '../styles/ProfileDetailModal.module.css';

const ProfileDetailModal = ({ show, onClose, profile }) => {
    if (!show || !profile) {
        return null;
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <div className={styles.profileContainer}>
                    <img src={profile.image} alt={profile.name} className={styles.profileImage} />
                    <div className={styles.profileDetails}>
                        <h2>{profile.name} <span className={styles.profileType}>{profile.type}</span></h2>
                        <p>나이 : {profile.age}</p>
                        <p>키 : {profile.height}</p>
                        <p>직업 : {profile.job}</p>
                        <p>취미 : {profile.hobbies}</p>
                        <div className={styles.profileTags}>{profile.tags}</div>
                        <p className={styles.profileDescription}>{profile.description}</p>
                    </div>
                </div>
                <div className={styles.buttonContainer}>
                    <button className={styles.prevButton}>←</button>
                    <button className={styles.chatButton}>대화 시작하기</button>
                </div>
            </div>
        </div>
    );
};

export default ProfileDetailModal;
