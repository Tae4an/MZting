import React from 'react';
import PropTypes from 'prop-types';
import { useNavigate } from 'react-router-dom';
import styles from '../styles/ChatBox.module.css';

const ChatBox = ({ image, name }) => {
    const navigate = useNavigate();

    const handleBackClick = () => {
        navigate(-1); // 이전 페이지로 이동
    };

    return (
        <section className={styles.chatContainer}>
            <header className={styles.chatHeader}>
                <div className={styles.userInfo}>
                    <button
                        className={styles.backbutton}
                        onClick={handleBackClick}
                        onKeyDown={(e) => { if (e.key === 'Enter') handleBackClick(); }}
                        aria-label="Go back"
                    >
                        &lt;
                    </button>
                    <img src={image} alt="User avatar" className={styles.avatar} />
                    <span className={styles.userName}>{name}</span>
                </div>
                <div className={styles.statusIndicator} />
            </header>
            <p className={styles.situationDescription}>
                상황 설명 상황 설명 설명 상황 설명 상황 끄어엄 끄어어엄<br />
                (ex 당신은 주선자의 소개를 통해 연락이 닿았습니다.)<br />
            </p>
            <div className={styles.messageContainer}>
                <div className={styles.receivedMessage}>
                    <img src={image} alt="Message avatar" className={styles.messageAvatar} />
                    <div className={styles.message}>안녕하세요 이번에 소개받게 된 {name}(이)라고 합니다~</div>
                </div>
                <div className={styles.receivedMessage}>
                    <img src={image} alt="Message avatar" className={styles.messageAvatar} />
                    <div className={styles.message}>대화내용</div>
                </div>
                <div className={styles.messageSent}>내용대화</div>
            </div>
        </section>
    );
};

ChatBox.propTypes = {
    image: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
};

export {
    ChatBox
};
