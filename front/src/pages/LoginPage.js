import React from 'react';
import styles from '../styles/LoginPage.module.css';
import { LoginForm } from '../components';

const LoginPage = () => {
    return (
        <div className={styles.loginPage}>
            <LoginForm />
        </div>
    );
};

export { LoginPage };