import React from 'react';
import { useLocation } from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';

const ChatPage = () => {
    const location = useLocation();
    const { image, name, type, age, height, job, hobbies, tags, description } = location.state || {};

    return (
        <main className={styles.mainContainer}>
            <div className={styles.contentWrapper}>
                <ChatBox
                    image={image}
                    name={name}
                    type={type}
                    age={age}
                    height={height}
                    job={job}
                    hobbies={hobbies}
                    tags={tags}
                    description={description}
                />
            </div>
        </main>
    );
};

export {
    ChatPage
};
