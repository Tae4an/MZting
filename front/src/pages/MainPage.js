import React, { useEffect, useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/MainPage.module.css';
import { ProfileCard, ProfileDetailModal } from "../components";

// 이미지 동적 import 함수
function importAll(r) {
    let images = {};
    r.keys().forEach((item) => { images[item.replace('./','')] = r(item); });
    return images;
}

// 이미지를 동적으로 import
const images = importAll(require.context('../assets/Images', false, /\.(png|jpe?g|svg)$/));

const profileData = [
    { id: 1, image: images['image1.jpg'], name: "이지은", type: "#INFJ", tags: "#배려심 #사려깊은 #내향적", age: 23, height: 165, job: "작가", hobbies: "독서, 일기, 글쓰기, 음악 감상", description: "내성적이지만 매우 사려 깊고 배려심 많은 사람이다. 그녀는 혼자만의 시간을 즐기며, 깊이 있는 대화를 좋아한다. 타인을 돕는 것에서 큰 보람을 느끼며, 진실된 관계를 중요시한다. 친절하고 따듯한 성격으로 많은 사람들에게 좋은 인상을 주는 그녀는, 항상 주변 사람들을 생각하며 그들의 감정을 이해하려고 노력한다." },
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
    { id: 13, image: images['image13.jpg'], name: "안유진", type: "#ESTP", tags: "#명석한두뇌 #직관력 #느긋함" },
    { id: 14, image: images['image14.jpg'], name: "김민지", type: "#ESTJ", tags: "#체계적 #규칙준수 #목표" },
    { id: 15, image: images['image15.jpg'], name: "이나경", type: "#INTP", tags: "#혁신적 #논리적 #잠재력" },
    { id: 16, image: images['image16.jpg'], name: "김가을", type: "#ISTJ", tags: "#사실근거 #사고형 #책임감" },
    { id: 17, image: images['imageR.jpg'], name: "???", type: "#????", tags: "#모든 것이 랜덤입니다!", age: "???", height: "???", job: "???", hobbies: "???", description: "선택 장애가 온 당신! 모든 것을 랜덤에 맡겨보는 건 어떨까요?" },
];

const MainPage = () => {
    const [scrollPosition, setScrollPosition] = useState(0);
    const [showModal, setShowModal] = useState(false);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const mainContentRef = useRef(null);
    const navigate = useNavigate();

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

    const handleProfileClick = (profile) => {
        setSelectedProfile(profile);
        setShowModal(true);
    };

    const handleStartChat = () => {
        setShowModal(false);
        navigate('/chat', { state: selectedProfile });
    };

    return (
        <div ref={mainContentRef} className={styles.mainContent}>
            <header className={styles.header}>
                <div className={styles.mainTitleContainer}>
                    <h1 className={styles.mainTitle}>mzting</h1>
                    <div className={styles.iconContainer}>
                        <div className={styles.icon} />
                        <div className={styles.icon} />
                    </div>
                </div>
                <h2 className={styles.title}>추천</h2>
            </header>
            <hr className={styles.divider} />
            <div className={styles.profileGrid}>
                {profileData.map((profile) => (
                    <ProfileCard key={profile.id} {...profile} onClick={() => handleProfileClick(profile)} />
                ))}
            </div>
            {showModal && (
                <ProfileDetailModal
                    show={showModal}
                    onClose={() => setShowModal(false)}
                    profile={selectedProfile}
                    onClick={handleStartChat}
                    showChatButton={true}
                />
            )}
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
    MainPage
};
