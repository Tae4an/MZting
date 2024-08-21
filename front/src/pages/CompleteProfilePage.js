import React, { useState } from 'react';
import axios from 'axios';
import styles from '../styles/CompleteProfilePage.module.css';
import MZting_logo from '../assets/Images/MZting_logo.png';

const mbtiOptions = [
    "INTJ", "INTP", "ENTJ", "ENTP",
    "INFJ", "INFP", "ENFJ", "ENFP",
    "ISTJ", "ISFJ", "ESTJ", "ESFJ",
    "ISTP", "ISFP", "ESTP", "ESFP"
];

const CompleteProfilePage = () => {
    const [profile, setProfile] = useState({
        age: '',
        gender: '',
        mbti: '',
        nickname: '' // nickname 추가
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProfile({
            ...profile,
            [name]: value
        });
    };

    const generateAgeOptions = () => {
        const options = [];
        for (let i = 1; i <= 100; i++) {
            options.push(<option key={i} value={i}>{i}</option>);
        }
        return options;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = new URLSearchParams(window.location.search).get('token');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            const response = await axios.post('http://localhost:8080/api/auth/complete-profile', profile, config);
            console.log(response.data);
            window.location.href = '/main';
        } catch (error) {
            console.error('Error updating profile', error);
            setError('An unexpected error occurred while updating profile');
        }
    };

    return (
        <div className={styles.completeProfileContainer}>
            <form className={styles.completeProfileForm} onSubmit={handleSubmit}>
                <img src={MZting_logo} className={styles.signupLogo} alt="Signup Logo"/>
                {error && <p className={styles.error}>{error}</p>}
                <div className={styles.inputGroup}>
                    <label htmlFor="nickname" className={styles.font}>Nickname</label>
                    <input
                        type="text"
                        id="nickname"
                        name="nickname"
                        value={profile.nickname}
                        onChange={handleChange}
                        required
                        className={styles.font}
                    />
                </div>
                <div className={styles.selectAge}>
                    <label htmlFor="age" className={styles.font}>Age</label>
                    <select
                        id="age"
                        name="age"
                        value={profile.age}
                        onChange={handleChange}
                        required
                        className={styles.font}
                    >
                        <option value="" disabled>나이 선택</option>
                        {generateAgeOptions()}
                    </select>
                </div>
                <div className={styles.selectGender}>
                    <label htmlFor="gender" className={styles.font}>Gender</label>
                    <select
                        id="gender"
                        name="gender"
                        value={profile.gender}
                        onChange={handleChange}
                        required
                        className={styles.font}
                    >
                        <option value="" disabled>성별 선택</option>
                        <option value="male">남성</option>
                        <option value="female">여성</option>
                    </select>
                </div>
                <div className={styles.selectMBTI}>
                    <label htmlFor="mbti" className={styles.font}>MBTI</label>
                    <select
                        id="mbti"
                        name="mbti"
                        value={profile.mbti}
                        onChange={handleChange}
                        required
                        className={styles.font}
                    >
                        <option value="" disabled>MBTI 선택</option>
                        {mbtiOptions.map((option) => (
                            <option key={option} value={option}>{option}</option>
                        ))}
                    </select>
                </div>
                <button type="submit" className={`${styles.completeProfileButton} ${styles.font}`}>Complete Profile</button>
            </form>
        </div>
    );
};

export { CompleteProfilePage };
