import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import {sendGetRequest, sendMessage, sendPostRequest} from '../services';

const ChatPage = () => {
    const location = useLocation();
    const { selectedProfile : { image, name, type, age, height, job, hobbies, tags, description }, chatRoomId, isFirst } = location.state || {};
    const mbti = type.replace('#', '');

    const [messages, setMessages] = useState([]);
    const [currentStageIndex, setCurrentStageIndex] = useState(0);
    const [isActualMeeting, setIsActualMeeting] = useState(false);

    useEffect(() => {
        if(isFirst) { // 채팅방 새로 생성 시
            console.log('ChatPage loaded with state:', location.state);
            sendInitialMessage();
        } else { // 기존 채팅방 이어가기
            const getChatHistory = async () => { // 기존 채팅방 정보 가져오기
                const response = sendGetRequest({}, `/api/chatroom/entry/${chatRoomId}`)
                console.log("기존 채팅방 이어가기 : ", response)
            }
        }

    }, []);

    const sendInitialMessage = async () => {
        const initialContext = {
            name,
            type,
            age,
            height,
            job,
            hobbies: Array.isArray(hobbies) ? hobbies.join(', ') : hobbies,
            tags: Array.isArray(tags) ? tags.join(', ') : tags,
            description
        };

        const initialMessageContent = `안녕하세요. 소개팅 상대방의 정보입니다: ${JSON.stringify(initialContext)}`;

        try {
            const requestData = {
                message : initialMessageContent,
                mbti : mbti,
                chatRoomId : chatRoomId
            }
            console.log(requestData);
            const response = await sendPostRequest(requestData, "api/ask-claude")

            if (response.claudeResponse && response.claudeResponse.text) {
                const responseMessage = {
                    content: {
                        text: response.claudeResponse.text,
                        feel: response.claudeResponse.feel,
                        score: response.claudeResponse.score,
                        evaluation: response.claudeResponse.evaluation
                    },
                    isSent: false,
                    avatar: image,
                };
                setMessages([responseMessage]);
            }
        } catch (error) {
            console.error('Error sending initial message:', error);
        }
    };

    const handleSendMessage = async (content) => {
        try {
            const newMessage = { content, isSent: true };
            setMessages(prevMessages => [...prevMessages, newMessage]);

            const response = await sendMessage(content, mbti);

            if (response.claudeResponse && response.claudeResponse.text) {
                const responseMessage = {
                    content: {
                        text: response.claudeResponse.text,
                        feel: response.claudeResponse.feel,
                        score: response.claudeResponse.score,
                        evaluation: response.claudeResponse.evaluation
                    },
                    isSent: false,
                    avatar: image,
                };
                setMessages(prevMessages => [...prevMessages, responseMessage]);
            }
        } catch (error) {
            console.error('Error sending message:', error);
        }
    };

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                {isActualMeeting && (
                    <div className={styles.actualMeetingBanner}>
                        실제 만남 진행 중
                    </div>
                )}
                <ChatBox
                    image={image}
                    name={name}
                    profileDetails={{ image, name, type, age, height, job, hobbies, tags, description }}
                    messages={messages}
                    onSendMessage={handleSendMessage}
                />
            </div>
        </main>
    );
};

export { ChatPage };