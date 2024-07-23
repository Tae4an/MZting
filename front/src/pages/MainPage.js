import React, { useEffect, useState } from 'react';
import styles from '../styles/MainPage.module.css';
import { ProfileCard } from "../components";

// 이미지 동적 import 함수
function importAll(r) {
    let images = {};
    r.keys().forEach((item) => { images[item.replace('./','')] = r(item); });
    return images;
}

// 이미지를 동적으로 import
const images = importAll(require.context('../assets', false, /\.(png|jpe?g|svg)$/));

const profileData = [
    { id: 1, image: images['image1.jpg'], name: "이지은", type: "#INFJ", tags: "#배려심 #사려깊은 #내향적" },
    { id: 2, image: images['image2.jpg'], name: "백지헌", type: "#ENFJ", tags: "#상냥 #공감 #사회적" },
    { id: 3, image: images['image3.jpg'], name: "신세경", type: "#ISFJ", tags: "#친절함 #헌신적 #책임감" },
    { id: 4, image: images['image4.jpg'], name: "고윤정", type: "#ISTP", tags: "#현실적 #실용적 #모험심" },
    { id: 5, image: images['image5.jpg'], name: "한소희", type: "#INFP", tags: "#이상주의 #충실 #내향적" },
    { id: 6, image: images['image6.jpg'], name: "유지민", type: "#ENTP", tags: "#도전적 #논쟁 #호기심" },
    { id: 7, image: images['image7.jpg'], name: "김유정", type: "#INTJ", tags: "#상상력 #철두철미 #독립적" },
    { id: 8, image: images['image8.jpg'], name: "조미연", type: "#ENFP", tags: "#창의적 #상상력 #웃음많음" },
    { id: 9, image: images['image9.jpg'], name: "설윤아", type: "#ISFP", tags: "#온화함 #겸손함 #도전적" },
    { id: 10, image: images['image10.jpg'], name: "전희진", type: "#ENTJ", tags: "#강한의지 #통솔력 #활동적" },
    { id: 11, image: images['image11.jpg'], name: "강혜원", type: "#ESFP", tags: "#즉흥적 #개방적 #에너지" },
    { id: 12, image: images['image12.jpg'], name: "이채영", type: "#ESFJ", tags: "#지적인도전 #친절함 #공감능력" },
    { id: 13, image: images['image13.jpg'], name: "박경리", type: "#ESTP", tags: "#명석한두뇌 #직관력 #느긋함" },
    { id: 14, image: images['image14.jpg'], name: "김민지", type: "#ESTJ", tags: "#체계적 #규칙준수 #목표" },
    { id: 15, image: images['image15.jpg'], name: "이나경", type: "#INTP", tags: "#혁신적 #논리적 #잠재력" },
    { id: 16, image: images['image16.jpg'], name: "김가을", type: "#ISTJ", tags: "#사실근거 #사고형 #책임감" },
    { id: 17, image: images['imageR.jpg'], name: "???", type: "#????", tags: "#모든 것이 랜덤입니다!" },
];

const MainPage = () => {
    const [scrollPosition, setScrollPosition] = useState(0);

    const handleScroll = () => {
        const totalScrollHeight = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        const currentScrollPosition = window.scrollY;
        const maxScrollIndicatorPosition = document.documentElement.clientHeight - 129; // 129는 인디케이터의 height
        const headerHeight = 183; // 헤더 높이 (px 단위로 정확한 값을 설정하세요)

        let scrollIndicatorPosition = (currentScrollPosition / totalScrollHeight) * maxScrollIndicatorPosition;

        if (scrollIndicatorPosition < headerHeight) {
            scrollIndicatorPosition = headerHeight;
        } else if (scrollIndicatorPosition > maxScrollIndicatorPosition) {
            scrollIndicatorPosition = maxScrollIndicatorPosition;
        }

        setScrollPosition(scrollIndicatorPosition);
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
                {profileData.map((profile) => (
                    <ProfileCard key={profile.id} {...profile} />
                ))}
            </div>
            <div className={styles.scrollIndicator} style={{ top: scrollPosition }}/>
        </div>
    );
};

export {
    MainPage
};
