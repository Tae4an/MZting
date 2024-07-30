import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage } from '../services/sendMessage';

// 장소 키워드 정의
const placeKeywords = {
    '메신저': ['메신저', '카카오톡', '라인', '텔레그램', '온라인', '채팅'],
    '카페': ['카페', '커피숍', '스타벅스', '커피', '디저트', '브런치'],
    '레스토랑': ['레스토랑', '식당', '저녁식사', '맛집', '음식점', '다이닝'],
    '영화관': ['영화관', '극장', '시네마', 'CGV', '메가박스', '롯데시네마', '영화'],
    '공원': ['공원', '산책', '피크닉', '야외', '자연', '벤치'],
    '전시회': ['전시회', '갤러리', '미술관', '박물관', '작품', '전시']
};

// 장소 감지 함수
function detectPlace(content) {
    const lowercaseContent = content.toLowerCase();
    for (const [place, keywords] of Object.entries(placeKeywords)) {
        if (keywords.some(keyword => lowercaseContent.includes(keyword.toLowerCase()))) {
            return place;
        }
    }
    return null;
}

// 대화 단계에 따른 컨텍스트 생성 함수
function getContextualPrompt(stage, selectedPlace) {
    switch (stage) {
        case 'INITIAL':
            return '첫 만남 장소를 정하는 대화를 이어가주세요.';
        case 'PLACE_SELECTED':
            return `${selectedPlace}에서 만나기로 했습니다. 구체적인 시간과 세부 사항을 정해보세요.`;
        case 'DETAILS_SET':
            return '만남 준비와 기대감에 대해 이야기해보세요.';
        default:
            return '자연스럽게 대화를 이어가세요.';
    }
}

// 대화 진행에 따른 상태 업데이트 함수
function updateConversationStage(currentStage, message) {
    if (currentStage === 'PLACE_SELECTED' && message.toLowerCase().includes('시간')) {
        return 'DETAILS_SET';
    }
    return currentStage;
}

const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};
    const mbti = type.replace('#', '');

    const [messages, setMessages] = useState([]);
    const [choices, setChoices] = useState([]);
    const [showChoiceModal, setShowChoiceModal] = useState(false);
    const [selectedPlace, setSelectedPlace] = useState(null);
    const [conversationStage, setConversationStage] = useState('INITIAL');

    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
    }, [location.state]);

    const handleSendMessage = async (content) => {
        try {
            // 새 메시지 추가
            const newMessage = { content, isSent: true };
            setMessages(prevMessages => [...prevMessages, newMessage]);

            // 사용자 메시지에서 장소 감지
            const userSelectedPlace = detectPlace(content);
            if (userSelectedPlace && !selectedPlace) {
                setSelectedPlace(userSelectedPlace);
                setConversationStage('PLACE_SELECTED');
            }

            // 현재 대화 단계에 따른 컨텍스트 생성
            const contextualPrompt = getContextualPrompt(conversationStage, selectedPlace);

            // 메시지 전송 및 응답 받기
            const response = await sendMessage(content, mbti, contextualPrompt);

            if (response.claudeResponse) {
                const responseMessage = {
                    content: response.claudeResponse,
                    isSent: false,
                    avatar: image,
                };
                setMessages(prevMessages => [...prevMessages, responseMessage]);

                // 챗봇 응답에서 장소 감지
                const botSelectedPlace = detectPlace(response.claudeResponse.text);
                if (botSelectedPlace && !selectedPlace) {
                    setSelectedPlace(botSelectedPlace);
                    setConversationStage('PLACE_SELECTED');
                }

                // 대화 진행에 따른 상태 업데이트
                setConversationStage(prevStage =>
                    updateConversationStage(prevStage, response.claudeResponse.text)
                );
            }

            // 장소가 선택되지 않은 경우에만 선택지 모달 표시
            if (!selectedPlace && response.choices && Object.keys(response.choices).length > 0) {
                const triggerType = Object.keys(response.choices)[0];
                const triggerChoices = response.choices[triggerType];
                setChoices(triggerChoices);
                setShowChoiceModal(true);
            }
        } catch (error) {
            console.error('Error sending message:', error);
        }
    };

    const handleChoiceSelect = (choice) => {
        setSelectedPlace(choice);
        setConversationStage('PLACE_SELECTED');
        handleSendMessage(`${choice}에서 만나는 게 어떨까요?`);
        setShowChoiceModal(false);
    };

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox
                    image={image}
                    name={name}
                    profileDetails={{ image, name, type, age, height, job, hobbies, tags, description }}
                    messages={messages}
                    onSendMessage={handleSendMessage}
                    choices={choices}
                    showChoiceModal={showChoiceModal}
                    onChoiceSelect={handleChoiceSelect}
                    onCloseChoiceModal={() => setShowChoiceModal(false)}
                />
            </div>
        </main>
    );
};

export { ChatPage };