import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from '../styles/LoginPage.module.css';
import kakaoIcon from '../assets/icons/kakao.png';
import naverIcon from '../assets/icons/naver.png';
import googleIcon from '../assets/icons/google.png';

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post('/api/auth/login', {
                username,
                password
            });
            alert(response.data);
            navigate('/main');
        } catch (error) {
            if (error.response) {
                setError(error.response.data);
            } else {
                setError('An unexpected error occurred');
            }
        }
    };

    const handleSignUpClick = () => {
        navigate('/signup');
    };

    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    const handleNaverLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
    };

    const handleKakaoLogin = () => {
        // 기능 미구현
        alert('카카오 로그인 기능은 아직 구현되지 않았습니다.');
    };

    return (
        <div className={styles.loginFormContainer}>
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <h1 className={styles.title}>mzting</h1>
                {error && <p className={styles.error}>{error}</p>}
                <div className={styles.inputGroup}>
                    <label htmlFor="userId">ID</label>
                    <input
                        type="text"
                        id="userId"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
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
                <button type="submit" className={styles.button}>Login</button>
                <button type="button" className={styles.button} onClick={handleSignUpClick}>Sign Up</button>
                <br/>
                <div className={styles.socialLoginContainer}>
                    <button type="button" className={styles.iconButton} onClick={handleGoogleLogin}>
                        <img src={googleIcon} alt="Google" className={styles.iconImage} />
                    </button>
                    <button type="button" className={styles.iconButton} onClick={handleNaverLogin}>
                        <img src={naverIcon} alt="Naver" className={styles.iconImage} />
                    </button>
                    <button type="button" className={styles.iconButton} onClick={handleKakaoLogin}>
                        <img src={kakaoIcon} alt="Kakao" className={styles.iconImage} />
                    </button>
                </div>
            </form>
        </div>
    );
};

export { LoginForm };
