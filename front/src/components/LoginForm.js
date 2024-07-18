import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/LoginPage.module.css';

const LoginForm = () => {
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        // 로그인 로직 추가
        navigate('/main');
    };

    return (
        <div className={styles.loginFormContainer}>
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <h1 className={styles.title}>mzting</h1>
                <div className={styles.inputGroup}>
                    <label htmlFor="userId">ID</label>
                    <input type="text" id="userId" />
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userPassword">Password</label>
                    <input type="password" id="userPassword" />
                </div>
                <button type="submit" className={styles.button}>Login</button>
                <button type="button" className={styles.button}>Sign Up</button>
            </form>
        </div>
    );
};

export {
    LoginForm
};
