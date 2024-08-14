import React, { useState, useEffect } from 'react';
import {useLocation, useNavigate} from 'react-router-dom';
import styles from '../styles/ChatPage.module.css';
import { ChatBox } from '../components';
import {sendGetRequest, sendMessage, sendPostRequest} from '../services';
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { TimePassedModal } from "../components/TimePassedModal";
import IntroductionModal from '../components/IntroductionModal';

const ChatPage = () => {
    const location = useLocation();
    const state = location.state || {};
    const navigate = useNavigate();


    const selectedProfile = state.selectedProfile || state;
    const {
        image,
        name,
        type,
        age,
        height,
        job,
        hobbies,
        tags,
        description
    } = selectedProfile;

    const chatRoomId = state.chatRoomId;
    const isFirst = state.isFirst || false;

    const mbti = type ? type.replace('#', '') : '';

    const [messages, setMessages] = useState([]);
    const [stages, setStages] = useState({
        stage1Complete: false,
        stage2Complete: false,
        stage3Complete: false
    });
    const [isActualMeeting, setIsActualMeeting] = useState(false);
    const [claudeResponse, setClaudeResponse] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [backgroundChanged, setBackgroundChanged] = useState(false);
    const [stageToComplete, setStageToComplete] = useState(null);
    const [isIntroModalOpen, setIsIntroModalOpen] = useState(true);

    useEffect(() => {
        if(isFirst) {
            setIsIntroModalOpen(true);
        } else {
            getChatHistory();
        }
    }, [isFirst]);

    const getChatHistory = async () => {
        try {
            const response = await sendGetRequest({}, `/api/chatroom/entry/${chatRoomId}`);
            console.log("기존 채팅방 이어가기 : ", response);
            // 여기서 채팅 히스토리를 처리하고 상태를 업데이트합니다.
        } catch (error) {
            console.error('Error fetching chat history:', error);
        }
    };

    useEffect(() => {
        if (claudeResponse) {
            updateStages(claudeResponse);
        }
    }, [claudeResponse]);

    useEffect(() => {
        if (stageToComplete !== null) {
            const timer = setTimeout(() => {
                handleModalDisplay(stageToComplete);
            }, 5000);

            return () => clearTimeout(timer);
        }
    }, [stageToComplete]);

    useEffect(() => {
        if (stages.stage3Complete) {
            const timer = setTimeout(() => {
                navigate('/result', {
                    state: {
                        chatRoomId: chatRoomId,
                        profileDetails: selectedProfile
                    }
                });
            }, 3000);

            return () => clearTimeout(timer);
        }
    }, [stages.stage3Complete]);

    const handleCloseIntroModal = () => {
        setIsIntroModalOpen(false);
        sendInitialMessage();
    };

    const handleModalDisplay = (stage) => {
        let modalMessage;

        switch(stage) {
            case 1:
                modalMessage = "약속 날짜까지 시간이 흘렀습니다...";
                break;
            case 2:
                modalMessage = "대화를 마무리하고 있습니다...";
                break;
            case 3:
                modalMessage = "소개팅이 끝나고 있습니다...";
                break;
            default:
                return;
        }

        setIsModalOpen(true);

        const timer = setTimeout(() => {
            setIsModalOpen(false);
            if (stage === 1) setIsActualMeeting(true);

            setTimeout(() => {
                setBackgroundChanged(true);
            }, 500);
        }, 3000);

        return () => clearTimeout(timer);
    };

    const updateStages = (claudeResponse) => {
        setStages(prevStages => {
            const newStages = {
                stage1Complete: claudeResponse.stage1Complete,
                stage2Complete: claudeResponse.stage2Complete,
                stage3Complete: claudeResponse.stage3Complete
            };

            if (newStages.stage1Complete && !prevStages.stage1Complete) {
                toast.success("미션 완료: 성공적으로 약속을 잡았습니다!", {
                    position: "top-center",
                    autoClose: 5000,
                });
                setStageToComplete(1);
            } else if (newStages.stage2Complete && !prevStages.stage2Complete) {
                toast.success("미션 완료: 실제 만남에서 대화를 나눴습니다!", {
                    position: "top-center",
                    autoClose: 5000,
                });
                setStageToComplete(2);
            } else if (newStages.stage3Complete && !prevStages.stage3Complete) {
                toast.success("미션 완료: 만남 후 애프터 여부를 결정했습니다!", {
                    position: "top-center",
                    autoClose: 5000,
                });
                setStageToComplete(3);
            }

            return newStages;
        });
    };

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

            const processingData = async () => {
                const requestData = {
                    message : content,
                    mbti : mbti,
                    chatRoomId : chatRoomId
                }

                console.log(requestData)

                const response = await sendPostRequest(requestData, "/api/ask-claude")

                // Claude의 응답을 \n을 기준으로 여러 개의 메시지로 나누기
                const splitMessages = response.claudeResponse.text.split('\n').filter(msg => msg.trim() !== '');
                console.log(splitMessages)

                return {
                    ...response,
                    claudeResponse: {
                        ...response.claudeResponse,
                        messages: splitMessages
                    }
                };
            }

            const response = await processingData()

            if (response.claudeResponse && response.claudeResponse.messages) {
                const totalMessages = response.claudeResponse.messages.length;

                for (let index = 0; index < totalMessages; index++) {
                    const message = response.claudeResponse.messages[index];
                    const isLastMessage = index === totalMessages - 1;

                    await new Promise(resolve => setTimeout(resolve, 2000)); // 각 메시지마다 0.5초의 간격

                    const responseMessage = {
                        content: {
                            text: message,
                            feel: isLastMessage ? response.claudeResponse.feel : null,
                            score: isLastMessage ? response.claudeResponse.score : null,
                            evaluation: isLastMessage ? response.claudeResponse.evaluation : null
                        },
                        isSent: false,
                        avatar: image,
                        isLastInGroup: isLastMessage
                    };

                    setMessages(prevMessages => [...prevMessages, responseMessage]);

                    if (isLastMessage) {
                        setClaudeResponse(response.claudeResponse);
                    }
                }
            }
        } catch (error) {
            console.error('Error sending message:', error);
        }
    };

    return (
        <main className={`${styles.mainContainer} ${backgroundChanged ? styles.backgroundChanged : ''}`}>
            <div className={styles.contentWrapper}>
                <ChatBox
                    image={image}
                    name={name}
                    profileDetails={{image, name, type, age, height, job, hobbies, tags, description}}
                    messages={messages}
                    onSendMessage={handleSendMessage}
                    stages={stages}
                    isActualMeeting={isActualMeeting}
                />
            </div>
            <IntroductionModal
                isOpen={isIntroModalOpen}
                onClose={handleCloseIntroModal}
                profileDetails={selectedProfile}
            />
            <TimePassedModal
                isOpen={isModalOpen}
                message={stageToComplete === 1 ? "약속 날짜까지 시간이 흐르고.." :
                    stageToComplete === 2 ? "대화를 마무리하고 있습니다..." :
                        "소개팅이 끝나고 있습니다..."}
            />
            <ToastContainer
                position="top-center"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />
        </main>
    );
};

export { ChatPage };