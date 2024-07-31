import React, { useEffect, useState } from 'react';
import { sendPostRequest, sendGetRequest } from "../services";
import styles from '../styles/RecommendModal.module.css';
import { ProfileCard } from "./ProfileCard";

// Constants
const QUESTIONS_PER_PAGE = 4;

// Utility functions
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

// Main component
const RecommendModal = ({ show, onClose }) => {
    const [option, setOption] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [choice, setChoice] = useState([]);
    const [recommend, setRecommend] = useState(null);
    const [profileData, setProfileData] = useState([]);

    const handleChoiceComplete = async (completeAnswers) => {
        try {
            setIsLoading(true);
            const mbti = await sendPostRequest(completeAnswers, "/api/question/submit");
            const data = await sendGetRequest({}, `/api/recommend/compatibility/${mbti}`);
            setRecommend(data.compatibilityGroups.soulMate);
            setOption(false);
        } catch (error) {
            console.error("Error in handleChoiceComplete:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleMyMBTI = async () => {
        try {
            setIsLoading(true);
            const data = await sendGetRequest({}, "/api/recommend/compatibility/INFP");
            setRecommend(data.compatibilityGroups.soulMate);
            setOption(false); // true 대신 false로 변경
        } catch (error) {
            console.error("Error in handleMyMBTI:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSelectChoice = async () => {
        try {
            setIsLoading(true);
            const data = await sendGetRequest({}, "/api/question");
            console.log(data)
            setChoice(data);
            setOption(true);
        } catch (error) {
            console.error("Error in handleSelectChoice:", error);
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        if (!show) {
            setOption(null);
            setIsLoading(false);
            setChoice([]);
            setRecommend(null);
            setProfileData([]);
        } else {
            fetchProfileData();
        }
    }, [show]);

    const fetchProfileData = async () => {
        try {
            const pData = await sendGetRequest({}, "/api/profiles");
            setProfileData(transformProfileData(pData));
        } catch (error) {
            console.error("Error fetching profile data:", error);
        }
    };

    if (!show) return null;

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modal}>
                <ModalHeader onClose={onClose} />
                <ModalBody
                    option={option}
                    handleMyMBTI={handleMyMBTI}
                    handleSelectChoice={handleSelectChoice}
                    choice={choice}
                    handleChoiceComplete={handleChoiceComplete}
                    recommend={recommend}
                    profileData={profileData}
                    isLoading={isLoading}
                />
            </div>
        </div>
    );
};

// Sub-components
const ModalHeader = ({ onClose }) => (
    <div className={styles.modalHeader}>
        <h2>추천</h2>
        <button className={styles.closeButton} onClick={onClose}>X</button>
    </div>
);

const ModalBody = ({ option, handleMyMBTI, handleSelectChoice, choice, handleChoiceComplete, recommend, profileData, isLoading }) => (
    <div className={styles.modalBody}>
        {option === null ? (
            <>
                <p>옵션 선택</p>
                <OptionSelection handleMyMBTI={handleMyMBTI} handleSelectChoice={handleSelectChoice} />
            </>
        ) : option === true ? (
            <TempChoiceView choice={choice} onComplete={handleChoiceComplete} />
        ) : isLoading ? (
            <div>로딩 중...</div>
        ) : recommend && profileData.length !== 0 ? (
            <TempCharacterView recommend={recommend} profileData={profileData} />
        ) : null}
    </div>
);

const OptionSelection = ({ handleMyMBTI, handleSelectChoice }) => (
    <>
        <div>
            <button onClick={handleMyMBTI}>내 MBTI 기반 추천</button>
            <p>자신의 MBTI를 아는 사람을 위한 선택!</p>
        </div>
        <div>
            <button onClick={handleSelectChoice}>선택지를 통한 추천</button>
            <p>자신의 MBTI를 모르는 사람을 위한 선택!</p>
        </div>
    </>
);

const TempCharacterView = ({ recommend, profileData }) => (
    <div style={{ flexDirection: "row" }}>
        {recommend.map((mbti, index) => {
            const profile = profileData.find(p => p.type.toLowerCase() === "#" + mbti.toLowerCase());
            return profile ? (
                <ProfileCard
                    key={index}
                    image={profile.image}
                    name={profile.name}
                    onClick={() => console.log("클릭")}
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

export { RecommendModal };