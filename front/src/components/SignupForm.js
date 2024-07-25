import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/SignupPage.module.css';

const SignupForm = () => {
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        // 회원가입 로직 추가
        navigate('/');
    };

    return (
        <div className={styles.signupFormContainer}>
            <form className={styles.signupForm} onSubmit={handleSubmit}>
                <h1 className={styles.title}>mzting</h1>
                <div className={styles.inputGroup}>
                    <label htmlFor="userId">ID</label>
                    <input type="text" id="userId"/>
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userPassword">Password</label>
                    <input type="password" id="userPassword"/>
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userPassword">Password Check</label>
                    <input type="password" id="userPassword"/>
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userName">Name</label>
                    <input type="name" id="userName"/>
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userNickName">Nick Name</label>
                    <input type="nickname" id="userNickName"/>
                </div>
                <div className={styles.inputGroup}>
                    <label htmlFor="userEmail">Email</label>
                    <input type="email" id="userEmail"/>
                </div>
                <button type="submit" className={styles.button}>Sign Up</button>
            </form>
        </div>
    );
};

export {SignupForm};
