import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/SettingsPage.module.css';

const SettingsPage = () => {
    const navigate = useNavigate();
    const mainContentRef = useRef(null);
    const [scrollPosition, setScrollPosition] = useState(0);

    const handleLogout = () => {
        // 로그아웃 로직을 추가할 곳
        alert('로그아웃 되었습니다.');
        navigate('/');
    };

    const handleScroll = () => {
        const totalScrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const currentScrollPosition = window.scrollY;
        const maxScrollIndicatorPosition = window.innerHeight - 129;

        let scrollIndicatorPosition = (currentScrollPosition / totalScrollHeight) * maxScrollIndicatorPosition;

        if (scrollIndicatorPosition > maxScrollIndicatorPosition) {
            scrollIndicatorPosition = maxScrollIndicatorPosition;
        }

        setScrollPosition(scrollIndicatorPosition);
    };

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        window.addEventListener('resize', handleScroll);

        return () => {
            window.removeEventListener('scroll', handleScroll);
            window.removeEventListener('resize', handleScroll);
        };
    }, []);

    return (
        <div ref={mainContentRef} className={styles.settingsContainer}>
            <header className={styles.settingsHeader}>
                <button className={styles.backButton} onClick={() => navigate(-1)}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <h1 className={styles.settingsTitle}>Settings</h1>
            </header>
            <div className={styles.settingsContent}>
                <button onClick={handleLogout} className={styles.logoutButton}>
                    로그아웃
                </button>
            </div>
            <div
                className={styles.scrollIndicator}
                style={{
                    top: `${scrollPosition}px`,
                    left: `${mainContentRef.current ? mainContentRef.current.getBoundingClientRect().right - 19 : 0}px`
                }}
            />
        </div>
    );
};

export { SettingsPage };