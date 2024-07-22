import React, { useEffect, useState } from 'react';
import styles from '../styles/MainPage.module.css';
import { ProfileCard } from "../components";

// 로컬 이미지 import
import image1 from '../assets/image1.jpg';
import image2 from '../assets/image2.jpg';
import image3 from '../assets/image3.jpg';
import image4 from '../assets/image4.jpg';
import image5 from '../assets/image5.jpg';
import image6 from '../assets/image6.jpg';

const profileData = [
    { id: 1, image: image1, name: "이지은", type: "#INFJ", tags: "#배려심 #사려깊은 #내향적" },
    { id: 2, image: image2, name: "백지헌", type: "#ENFJ", tags: "#상냥 #공감 #사회적" },
    { id: 3, image: image3, name: "신세경", type: "#ISFJ", tags: "#친절함 #헌신적 #책임감" },
    { id: 4, image: image4, name: "고윤정", type: "#ISTP", tags: "#현실적 #실용적 #모험심" },
    { id: 5, image: image5, name: "한소희", type: "#INFP", tags: "#이상주의 #충실 #내향적" },
    { id: 6, image: image6, name: "유지민", type: "#ENTP", tags: "#도전적 #논쟁 #호기심" },
];

const MainPage = () => {
    const [scrollPosition, setScrollPosition] = useState(0);

    const handleScroll = () => {
        const totalScrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const currentScrollPosition = window.scrollY;
        const scrollIndicatorPosition = (currentScrollPosition / totalScrollHeight) * (document.documentElement.clientHeight - 129);
        setScrollPosition(scrollIndicatorPosition)
    };

    useEffect(() => {
        window.addEventListener('scroll', handleScroll);
        return () => window.removeEventListener('scroll', handleScroll);
    }, []);

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
                    <ProfileCard key={profile.id} {...profile} />
                ))}
            </div>
            <div className={styles.scrollIndicator} style={{ position: 'fixed', top: scrollPosition}}/>
        </div>
    );
};

export {
    MainPage
};