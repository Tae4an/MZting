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
    { image: image1, name: "이지은", type: "#INFJ", tags: "#배려심 #사려깊은 #내향적" },
    { image: image2, name: "백지헌", type: "#ENFJ", tags: "#상냥 #공감 #사회적" },
    { image: image3, name: "신세경", type: "#ISFJ", tags: "#친절함 #헌신적 #책임감" },
    { image: image4, name: "고윤정", type: "#ISTP", tags: "#현실적 #실용적 #모험심" },
    { image: image5, name: "한소희", type: "#INFP", tags: "#이상주의 #충실 #내향적" },
    { image: image6, name: "유지민", type: "#ENTP", tags: "#도전적 #논쟁 #호기심" },
];

const MainPage = () => {
    const [scrollWidth, setScrollWidth] = useState('0%');

    const handleScroll = () => {
        const scrollTop = document.documentElement.scrollTop;
        const scrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const scrollPercentage = (scrollTop / scrollHeight) * 100;
        setScrollWidth(`${scrollPercentage}%`);
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
                    <ProfileCard key={index} {...profile} />
                ))}
            </div>
            <div className={styles.scrollIndicator} style={{ '--scroll-width': scrollWidth }} />
        </div>
    );
};

export {
    MainPage
};
