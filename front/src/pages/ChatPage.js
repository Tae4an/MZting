import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import { sendMessage } from '../services';

const ChatPage = () => {
    const location = useLocation();
    const { imageUrl, name, type, tags } = location.state || {
        imageUrl: "https://cdn.builder.io/api/v1/image/assets/TEMP/8f5dd0d11485f72df622564a688aea72f15bc048e514e524c65571bb0142fcc1?apiKey=49c1e3d53b81482bb61cc4f10fd5a261&",
        name: "이지은",
        type:"#ENFP",
        tags: ""
    };
    const [messages, setMessages] = useState([]);

    const handleSendMessage = async (content) => {
        try {
            const newMessage = { content, isSent: true };
            setMessages(prevMessages => [...prevMessages, newMessage]);

            const response = await sendMessage(content);
            const responseMessage = {
                content: response,
                isSent: false,
                avatar: imageUrl,
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
                    imageUrl={imageUrl || 'default_image_url'}
                    name={name || 'Unknown'}
                    type={type || 'Unknown'}
                    tags={tags || []}
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