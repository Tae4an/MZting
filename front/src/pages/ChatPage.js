import React, { useEffect,useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage } from '../services';


const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};
    const mbti = type.replace('#', ''); // type에서 #을 제거하여 mbti 추출


    useEffect(() => {
        console.log('ChatPage loaded with state:', location.state);
    }, [location.state]);

    const [messages, setMessages] = useState([]);

    const handleSendMessage = async (content) => {
        try {
            const newMessage = { content, isSent: true };
            setMessages(prevMessages => [...prevMessages, newMessage]);

            const response = await sendMessage(content,mbti);
            const responseMessage = {
                content: response,
                isSent: false,
                avatar: image,
            };
            setMessages(prevMessages => [...prevMessages, responseMessage]);
        } catch (error) {
            console.error('Error sending message:', error);
        }
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
                />
            </div>
        </main>
    );
};

export {
    ChatPage
};
