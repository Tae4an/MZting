import React from 'react';
import { HistoryList } from "../components";
import { AdColumn } from '../components';
import styles from '../styles/HistoryListPage.module.css';

const HistoryListPage = () => {
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

    return (
        <div className={styles.page}>
            <div className={styles.content}>
                <div className={styles.adColumn}>
                    <AdColumn />
                </div>
                <div className={styles.mainColumn}>
                    <HistoryList conversations={conversations} />
                </div>
                <div className={styles.adColumn}>
                    <AdColumn />
                </div>
            </div>
        </div>
    );
};

export {
    HistoryListPage
};