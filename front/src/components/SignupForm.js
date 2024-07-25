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
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        // 회원가입 로직 추가
        navigate('/');
        if (password !== passwordCheck) {
            return;
        }

        try {
            const response = await axios.post('/api/auth/register', {
                username: userId,
                password,
                name,
                nickname,
                email
            });
            alert('Registration successful');
            navigate('/');
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
                <h1 className={styles.title}>mzting</h1>
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
                <button type="submit" className={styles.button}>Sign Up</button>
            </form>
        </div>
    );
};

export { SignupForm };
