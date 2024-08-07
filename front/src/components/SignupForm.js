import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from '../styles/SignupPage.module.css';

const SignupForm = () => {
    const [userId, setUserId] = useState('');
    const [password, setPassword] = useState('');
    const [passwordCheck, setPasswordCheck] = useState('');
    const [name, setName] = useState('');
    const [nickname, setNickname] = useState('');
    const [email, setEmail] = useState('');
    const [mbti, setMbti] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (password !== passwordCheck) {
            setError('Passwords do not match');
            return;
        }

        try {
            const response = await axios.post('/api/auth/register', {
                username: userId,
                password,
                name,
                nickname,
                email,
                mbti,
            });
            alert('Registration successful');
            navigate('/login');
        } catch (error) {
            if (error.response) {
                setError(error.response.data);
            } else {
                setError('An unexpected error occurred');
            }
        }
    };

    return (
        <div className={styles.signupFormContainer}>
            <form className={styles.signupForm} onSubmit={handleSubmit}>
                <h1 className={styles.signupTitle}>mzting</h1>
                {error && <p className={styles.error}>{error}</p>}
                <div className={styles.inputGroup}>
                    <label htmlFor="userId">ID</label>
                    <input
                        type="text"
                        id="userId"
                        value={userId}
                        onChange={(e) => setUserId(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userPassword">Password</label>
                    <input
                        type="password"
                        id="userPassword"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userPasswordCheck">Password Check</label>
                    <input
                        type="password"
                        id="userPasswordCheck"
                        value={passwordCheck}
                        onChange={(e) => setPasswordCheck(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userName">Name</label>
                    <input
                        type="text"
                        id="userName"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userNickName">Nick Name</label>
                    <input
                        type="text"
                        id="userNickName"
                        value={nickname}
                        onChange={(e) => setNickname(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userEmail">Email</label>
                    <input
                        type="email"
                        id="userEmail"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </div>
                <div className={styles.selectMBTI}>
                    <label htmlFor="userMbti">MBTI</label>
                    <select
                        id="userMbti"
                        value={mbti}
                        onChange={(e) => setMbti(e.target.value)}
                        required
                    >
                        <option value="" disabled>MBTI 선택</option>
                        <option value="INTJ">INTJ</option>
                        <option value="INTP">INTP</option>
                        <option value="ENTJ">ENTJ</option>
                        <option value="ENTP">ENTP</option>
                        <option value="INFJ">INFJ</option>
                        <option value="INFP">INFP</option>
                        <option value="ENFJ">ENFJ</option>
                        <option value="ENFP">ENFP</option>
                        <option value="ISTJ">ISTJ</option>
                        <option value="ISFJ">ISFJ</option>
                        <option value="ESTJ">ESTJ</option>
                        <option value="ESFJ">ESFJ</option>
                        <option value="ISTP">ISTP</option>
                        <option value="ISFP">ISFP</option>
                        <option value="ESTP">ESTP</option>
                        <option value="ESFP">ESFP</option>
                    </select>
                </div>
                <button type="submit" className={styles.signupButton}>Sign Up</button>
            </form>
        </div>
    );
};

export { SignupForm };
