import React from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/MainPage.module.css';

const ProfileCard = ({ image, name, type, tags, onClick }) => {
    return (
        <button className={styles.profileCard} onClick={onClick}>
            <img src={image} alt={name} className={styles.profileImage} />
            <div className={styles.profileInfo}>
                <div className={styles.profileName}>{name}</div>
                <div className={styles.profileType}>{type}</div>
            </div>
            <div className={styles.profileTags}>{tags}</div>
        </button>
    );
};

ProfileCard.propTypes = {
    image: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    type: PropTypes.string.isRequired,
    tags: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired,
};

export { ProfileCard };
