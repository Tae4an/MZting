import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import styles from '../styles/LoginPage.module.css';
import kakaoIcon from '../assets/icons/kakao.png';
import naverIcon from '../assets/icons/naver.png';
import googleIcon from '../assets/icons/google.png';
import MZting_logo from '../assets/Images/MZting_logo.png';
import { sendPostRequest } from "../services";

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await sendPostRequest({
                username : username,
                password : password
            }, '/api/auth/login')
            console.log(response)
            alert("로그인 성공");
            sessionStorage.setItem('authToken', response.token);

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

    return (
        <div className={styles.loginFormContainer}>
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <img src={MZting_logo} className={styles.loginLogo} alt="Login Logo"/>
                {error && <p className={styles.error}>{error}</p>}
                <div className={styles.inputGroup}>
                    <label
                        htmlFor="userId"
                        style={{
                            fontWeight: "bolder",
                            fontStyle: "normal",
                            fontFamily: "Noto Sans KR, sans-serif",
                        }}
                    >
                        ID
                    </label>
                    <input
                        type="email"
                        id="userId"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        placeholder="이메일 형식으로 작성해주세요!"
                        className={styles.IDInput}
                    />
                </div>
                <div className={styles.inputGroup}>
                    <label
                        htmlFor="userPassword"
                        style={{
                            fontWeight: "bolder",
                            fontStyle: "normal",
                            fontFamily: "Noto Sans KR, sans-serif",
                        }}
                    >
                        Password
                    </label>
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
                <div className={styles.socialLoginContainer}>
                    <button type="button" className={styles.iconButton} onClick={handleGoogleLogin}>
                        <img src={googleIcon} alt="Google" className={styles.iconImage}/>
                    </button>
                    <button type="button" className={styles.iconButton} onClick={handleNaverLogin}>
                        <img src={naverIcon} alt="Naver" className={styles.iconImage}/>
                    </button>
                </div>
            </form>
        </div>
    );
};

export {LoginForm};
