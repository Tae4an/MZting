import React, {useState, useEffect, useCallback} from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage, initializeChat } from '../services/sendMessage';


const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};
    const mbti = type.replace('#', '');

    const [messages, setMessages] = useState([]);
    const [currentStageIndex, setCurrentStageIndex] = useState(0);
    const [isActualMeeting, setIsActualMeeting] = useState(false);
    const [chatRoomId, setChatRoomId] = useState(null);
    const [isInitialized, setIsInitialized] = useState(false);


    const initChat = useCallback(async () => {
        if (isInitialized) return;

        const newChatRoomId = Date.now();
        setChatRoomId(newChatRoomId);

        try {
            console.log('Initializing chat with the following details:');
            console.log('MBTI:', mbti);
            console.log('ChatRoomId:', newChatRoomId);
            console.log('Profile Details:', {
                name, type, age, height, job, hobbies, tags, description
            });

            const data = await initializeChat(mbti, newChatRoomId);
            console.log('Initialization response:', data);

            if (data.claudeResponse) {
                console.log('Claude\'s initial response:', data.claudeResponse);
                setMessages(prevMessages => [...prevMessages, {
                    content: data.claudeResponse.text,
                    isSent: false,
                    avatar: image,
                }]);
            }

            setIsInitialized(true);
        } catch (error) {
            console.error('Error initializing chat:', error);
        }
    }, []);

    useEffect(() => {
        initChat();
    }, [initChat]);

    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
    }, [location.state]);

    const handleSendMessage = async (content) => {
        try {
            const newMessage = { content, isSent: true };
            setMessages(prevMessages => [...prevMessages, newMessage]);

            const response = await sendMessage(content, mbti, chatRoomId);

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
                    chatRoomId={chatRoomId}
                />
            </div>
        </main>
    );
};

export { ChatPage };