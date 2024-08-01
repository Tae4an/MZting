import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage } from '../services/sendMessage';

const ChatPage = () => {
    const location = useLocation();
    const { image, name, type = '', age, height, job, hobbies, tags, description } = location.state || {};
    const mbti = type.replace('#', '');

    const [messages, setMessages] = useState([]);
    const [currentStageIndex, setCurrentStageIndex] = useState(0);
    const [isActualMeeting, setIsActualMeeting] = useState(false);

    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
    }, [location.state]);

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
