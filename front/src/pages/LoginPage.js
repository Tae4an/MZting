import React, {useEffect} from 'react';
import styles from '../styles/LoginPage.module.css';
import { LoginForm } from '../components';
import { getToken } from "../services";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
    const navigate = useNavigate();

    useEffect(() => {
        const token = getToken()
        if(token !== null) {
            navigate("/main")
        }
    }, []);

    return (
        <div className={styles.loginPage}>
            <LoginForm />
        </div>
    );
};

export { LoginPage };