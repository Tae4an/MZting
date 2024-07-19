import axios from "axios";

const API_URL = 'http://localhost:8080';

const sendMessage = async (message) => {
    try {
        const response = await axios.post(`${API_URL}/ask-claude`,
            { "message": message },
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        console.log(response.data);
        return response.data; // 이미 JSON 객체이므로 파싱할 필요 없음
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

export {
    sendMessage
}