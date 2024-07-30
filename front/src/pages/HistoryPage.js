import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from "react-router-dom";
import styles from '../styles/HistoryPage.module.css';

const HistoryPage = () => {
    const navigate = useNavigate();
    const mainContentRef = useRef(null);
    const [scrollPosition, setScrollPosition] = useState(0);

    const conversations = [
        {
            avatarSrc: "https://cdn.builder.io/api/v1/image/assets/TEMP/f312af922c0a98448cf4a4b68cf50ce014690f6158cf485c528a8307c494fe5f?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&",
            title: "#INFJ와의 대화",
            status: "진행중",
            actionText: "이어서 대화 하기"
        },
        {
            avatarSrc: "https://cdn.builder.io/api/v1/image/assets/TEMP/f312af922c0a98448cf4a4b68cf50ce014690f6158cf485c528a8307c494fe5f?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&",
            title: "#ENFJ와의 대화",
            status: "완료",
            actionText: "대화 기록 보기"
        }
    ];

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
        <div ref={mainContentRef} className={styles.page}>
            <header className={styles.header}>
                <button className={styles.backButton} onClick={() => navigate(-1)}>
                    <i className="bi bi-arrow-left"></i>
                </button>
                <h1 className={styles.title}>History</h1>
            </header>
            <div className={styles.content}>
                {conversations.map((conversation, index) => (
                    <div key={index} className={styles.card}>
                        <div className={styles.cardContent}>
                            <div className={styles.conversationInfo}>
                                <div className={styles.avatarWrapper}>
                                    <img loading="lazy" src={conversation.avatarSrc} alt="" className={styles.avatar} />
                                </div>
                                <div className={styles.titleWrapper}>
                                    <h2 className={styles.conversationTitle}>{conversation.title}</h2>
                                </div>
                            </div>
                            <div className={styles.statusWrapper}>
                                <div className={styles.status}>{conversation.status}</div>
                            </div>
                        </div>
                        <button className={styles.actionButton}>{conversation.actionText}</button>
                    </div>
                ))}
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

export {
    HistoryPage
};
