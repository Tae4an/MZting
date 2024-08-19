import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { sendPostRequest, sendGetRequest } from "../services";
import styles from '../styles/QuestionnaireRecommendation.module.css';
import { ProfileCard } from "./ProfileCard";
import { ProfileDetailModal } from "./ProfileDetailModal";

const QUESTIONS_PER_PAGE = 4;

function importAll(r) {
    let images = {};
    r.keys().forEach((item) => { images[item.replace('./', '')] = r(item); });
    return images;
}

const images = importAll(require.context('../assets/Images', false, /\.(png|jpe?g|svg)$/));

const transformProfileData = (data) => {
    return data.map((profile, index) => ({
        id: profile.profileId,
        image: images[profile.characterImage] || images[`image${index + 1}.jpg`],
        name: profile.name,
        type: `#${profile.mbti}`,
        tags: profile.characterKeywords.map(keyword => keyword.keyword.keyword).join(' '),
        age: profile.age,
        height: profile.height,
        job: profile.job,
        hobbies: profile.characterHobbies.map(hobby => hobby.hobby.hobby).join(', '),
        description: profile.description
    }));
};

const QuestionnaireRecommendation = ({ show, onClose }) => {
    const [isLoading, setIsLoading] = useState(false);
    const [choice, setChoice] = useState([]);
    const [recommend, setRecommend] = useState(null);
    const [profileData, setProfileData] = useState([]);
    const [selectedProfile, setSelectedProfile] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        if (show) {
            handleSelectChoice();
            fetchProfileData();
        } else {
            setChoice([]);
            setRecommend(null);
            setProfileData([]);
            setSelectedProfile(null);
        }
    }, [show]);

    const handleSelectChoice = async () => {
        try {
            setIsLoading(true);
            const data = await sendGetRequest({}, "/api/question");
            setChoice(data);
        } catch (error) {
            console.error("Error in handleSelectChoice:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleChoiceComplete = async (completeAnswers) => {
        try {
            setIsLoading(true);
            const mbti = await sendPostRequest(completeAnswers, "/api/question/submit");
            const data = await sendGetRequest({}, `/api/recommend/compatibility/${mbti}`);
            setRecommend(data.compatibilityGroups.soulMate);
        } catch (error) {
            console.error("Error in handleChoiceComplete:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const fetchProfileData = async () => {
        try {
            const pData = await sendGetRequest({}, "/api/profiles");
            setProfileData(transformProfileData(pData));
        } catch (error) {
            console.error("Error fetching profile data:", error);
        }
    };

    const handleProfileClick = (profile) => {
        setSelectedProfile(profile);
    };

    const handleProfileClose = () => {
        setSelectedProfile(null);
    };

    const handleChatClick = async (profile) => {
        const chatRoomId = await sendGetRequest({}, `/api/chatroom/create/${profile.id}`);
        navigate('/chat', {
            state: {
                selectedProfile: profile,
                chatRoomId,
                isFirst: true
            }
        });
    };

    if (!show) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <ModalHeader onClose={onClose} />
                <ModalBody
                    choice={choice}
                    handleChoiceComplete={handleChoiceComplete}
                    recommend={recommend}
                    profileData={profileData}
                    isLoading={isLoading}
                    onProfileClick={handleProfileClick}
                />
                {selectedProfile && (
                    <ProfileDetailModal
                        show={!!selectedProfile}
                        onClose={handleProfileClose}
                        profile={selectedProfile}
                        onClick={() => handleChatClick(selectedProfile)}
                        showChatButton={true}
                    />
                )}
            </div>
        </div>
    );
};

const ModalHeader = ({ onClose }) => (
    <div className={styles.modalHeader}>
        <h2>선택지 MBTI 추천</h2>
        <button className={styles.closeButton} onClick={onClose}>×</button>
    </div>
);

const ModalBody = ({ choice, handleChoiceComplete, recommend, profileData, isLoading, onProfileClick }) => (
    <div className={styles.modalBody}>
        {isLoading ? (
            <div>로딩 중...</div>
        ) : recommend ? (
            <TempCharacterView recommend={recommend} profileData={profileData} onProfileClick={onProfileClick} />
        ) : (
            <TempChoiceView choice={choice} onComplete={handleChoiceComplete} />
        )}
    </div>
);

const TempCharacterView = ({ recommend, profileData, onProfileClick }) => (
    <div className={styles.tempCharacterView}>
        {recommend.map((mbti, index) => {
            const profile = profileData.find(p => p.type.toLowerCase() === "#" + mbti.toLowerCase());
            return profile ? (
                <ProfileCard
                    key={index}
                    image={profile.image}
                    name={profile.name}
                    onClick={() => onProfileClick(profile)}
                    tags={profile.tags}
                    type={profile.type}
                />
            ) : null;
        })}
    </div>
);

const TempChoiceView = ({ choice, onComplete }) => {
    const [currentPage, setCurrentPage] = useState(0);
    const [answers, setAnswers] = useState([]);

    const totalPages = Math.ceil(choice.length / QUESTIONS_PER_PAGE);

    if (!choice || choice.length === 0) {
        return <div>질문을 불러오는 중입니다...</div>;
    }

    const handleAnswer = (questionId, option) => {
        setAnswers(prev => {
            const existingIndex = prev.findIndex(a => a.questionId === questionId);
            if (existingIndex !== -1) {
                return prev.map((a) =>
                    a.questionId === questionId ? { questionId, option } : a
                );
            } else {
                return [...prev, { questionId, option }];
            }
        });
    };

    const handleNextPage = () => {
        if (currentPage < totalPages - 1) {
            setCurrentPage(currentPage + 1);
        } else {
            onComplete(answers);
        }
    };

    const currentQuestions = choice.slice(currentPage * QUESTIONS_PER_PAGE, (currentPage + 1) * QUESTIONS_PER_PAGE);
    const isPageComplete = currentQuestions.every(q => answers.some(a => a.questionId === q.id));

    return (
        <div className={styles.choiceView}>
            <h3>페이지 {currentPage + 1} / {totalPages}</h3>
            {currentQuestions.map((question) => (
                <QuestionBlock
                    key={question.id}
                    question={question}
                    handleAnswer={handleAnswer}
                    selectedOption={answers.find(a => a.questionId === question.id)?.option}
                />
            ))}
            <button
                onClick={handleNextPage}
                disabled={!isPageComplete}
                className={styles.nextButton}
            >
                {currentPage === totalPages - 1 ? '완료' : '다음'}
            </button>
        </div>
    );
};

const QuestionBlock = ({ question, handleAnswer, selectedOption }) => (
    <div className={styles.questionBlock}>
        <p>{question.question}</p>
        <div className={styles.optionButtons}>
            <button
                onClick={() => handleAnswer(question.id, 'A')}
                className={`${styles.optionButton} ${selectedOption === 'A' ? styles.selected : ''}`}
            >
                {question.optionA}
            </button>
            <button
                onClick={() => handleAnswer(question.id, 'B')}
                className={`${styles.optionButton} ${selectedOption === 'B' ? styles.selected : ''}`}
            >
                {question.optionB}
            </button>
        </div>
    </div>
);

export { QuestionnaireRecommendation };