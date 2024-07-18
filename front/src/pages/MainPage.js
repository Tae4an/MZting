import React from 'react';
import styles from '../styles/MainPage.module.css';
import { ProfileCard } from "../components";

const profileData = [
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/fa86ed5ef2b4cb6800a78b2048d130102bec6527b5c83b3fca46c9be6be09329?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "이지은", type: "#INFJ", tags: "#배려심 #사려깊은 #내향적" },
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/788cc6ebe48f2d50acc5da79e2e7495ddceed32d6d89cba25f64f8a2529e5bb4?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "백지헌", type: "#ENFJ", tags: "#상냥 #공감 #사회적" },
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/deec3b2e6bf13ea1c35d6ad7cd7b8658c79406cb26e9a178580b114d5bc7eb05?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "신세경", type: "#ISFJ", tags: "#친절함 #헌신적 #책임감" },
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/a000f5467aec7856cd1a3f0041771393b024753bad4f0c55c931ef429735a3af?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "고윤정", type: "#ISTP", tags: "#현실적 #실용적 #모험심" },
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/ba2b63d5dffa7b329c46671cc7a4a8f16b501d9252ee31a9574ca5a6617157e0?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "한소희", type: "#INFP", tags: "#이상주의 #충실 #내향적" },
    { imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/c79c10d46a46692d66aa293b41c1a78c67f1a0948c8433cf6620819ee7a0ed9a?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&", name: "유지민", type: "#ENTP", tags: "#도전적 #논쟁 #호기심" },
];

const MainPage = () => {
    return (
        <div className={styles.mainContent}>
            <header className={styles.header}>
                <h1 className={styles.title}>추천</h1>
                <div className={styles.iconContainer}>
                    <div className={styles.icon} />
                </div>
            </header>
            <hr className={styles.divider} />
            <div className={styles.profileGrid}>
                {profileData.map((profile, index) => (
                    <ProfileCard key={index} {...profile} />
                ))}
            </div>
            <div className={styles.scrollIndicator} />
        </div>
    );
};

export {
    MainPage
};