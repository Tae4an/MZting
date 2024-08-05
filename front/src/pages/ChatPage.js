import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage } from '../services/sendMessage';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};
    const mbti = type.replace('#', '');

    const [messages, setMessages] = useState([]);
    const [stages, setStages] = useState({
        stage1Complete: false,
        stage2Complete: false,
        stage3Complete: false
    });
    const [isActualMeeting, setIsActualMeeting] = useState(false);
    const [claudeResponse, setClaudeResponse] = useState(null);

    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
        sendInitialMessage();
    }, []);

    useEffect(() => {
        if (claudeResponse) {
            updateStages(claudeResponse);
        }
    }, [claudeResponse]);

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
            const response = await sendMessage(initialMessageContent, mbti);

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
                setClaudeResponse(response.claudeResponse);
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
                setClaudeResponse(response.claudeResponse);
            }
        } catch (error) {
            console.error('Error sending message:', error);
        }
    };

    const updateStages = (claudeResponse) => {
        setStages(prevStages => {
            const newStages = {
                stage1Complete: claudeResponse.stage1Complete,
                stage2Complete: claudeResponse.stage2Complete,
                stage3Complete: claudeResponse.stage3Complete
            };

            if (newStages.stage1Complete && !prevStages.stage1Complete) {
                toast.success("스테이지 1 완료: 첫 인사와 약속 잡기!");
            }
            if (newStages.stage2Complete && !prevStages.stage2Complete) {
                toast.success("스테이지 2 완료: 실제 만남에서의 대화!");
                setIsActualMeeting(true);
            }
            if (newStages.stage3Complete && !prevStages.stage3Complete) {
                toast.success("스테이지 3 완료: 만남 후 애프터 여부 결정!");
            }

            return newStages;
        });
    };

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                {stages.stage1Complete && (
                    <div className={styles.stageBanner}>
                        스테이지 1 완료: 첫 인사와 약속 잡기
                    </div>
                )}
                {stages.stage2Complete && (
                    <div className={styles.stageBanner}>
                        스테이지 2 완료: 실제 만남에서의 대화
                    </div>
                )}
                {stages.stage3Complete && (
                    <div className={styles.stageBanner}>
                        스테이지 3 완료: 만남 후 애프터 여부 결정
                    </div>
                )}
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
            <ToastContainer position="top-right" autoClose={3000} />
        </main>
    );
};

export { ChatPage };