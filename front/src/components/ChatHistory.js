import React from 'react';
import { sendGetRequest } from '../services';

const ChatHistory = ({ chatRoomId, image, onHistoryLoaded }) => {
    const getChatHistory = async () => {
        try {
            const response = await sendGetRequest({}, `/api/chatroom/entry/${chatRoomId}`);
            console.log("기존 채팅방 이어가기 : ", response);
            if (response && Array.isArray(response)) {
                // 사용자의 첫 메시지를 제외한 나머지 메시지들만 필터링
                const filteredMessages = response.filter((msg, index) => !(index === 0 && msg.role === 'user'));

                const formattedMessages = [];
                filteredMessages.forEach(msg => {
                    // 메시지 내용을 '\n'을 기준으로 나눕니다.
                    const splitContent = msg.content.split('\n').filter(content => content.trim() !== '');

                    splitContent.forEach((content, index) => {
                        formattedMessages.push({
                            content: content,
                            isSent: msg.role === 'user',
                            avatar: msg.role === 'assistant' ? image : null,
                            isLastInGroup: index === splitContent.length - 1,
                            botInfo: msg.role === 'assistant' && index === splitContent.length - 1 ?
                                JSON.parse(msg.botInfo) : null
                        });
                    });
                });

                // 마지막 메시지의 stage 정보
                const lastMessage = filteredMessages[filteredMessages.length - 1];
                const stages = lastMessage && lastMessage.stage ? {
                    stage1Complete: lastMessage.stage >= 1,
                    stage2Complete: lastMessage.stage >= 2,
                    stage3Complete: lastMessage.stage >= 3
                } : null;

                onHistoryLoaded(formattedMessages, stages);
            }
        } catch (error) {
            console.error('Error fetching chat history:', error);
            throw error;
        }
    };

    React.useEffect(() => {
        getChatHistory();
    }, [chatRoomId, image]);

    return null; // This component doesn't render anything
};

export {ChatHistory};