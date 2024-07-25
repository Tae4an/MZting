import React from 'react';
import styles from '../styles/SignupPage.module.css';
import { SignupForm } from '../components';

const SignupPage = () => {
    return (
        <div className={styles.signupPage}>
            <SignupForm />
        </div>
    )
}

export { SignupPage };