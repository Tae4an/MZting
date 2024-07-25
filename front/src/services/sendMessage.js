import axios from "axios";

const API_URL = 'http://localhost:8080';

const sendMessage = async (message) => {
    const mbti = "ENTP"
    try {
        const response = await axios.post(`${API_URL}/api/ask-claude`,
            {
                "message": message,
                "mbti" : mbti
            },
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

const sendGetRequest = async (data, endpoint) => {
    try {
        const response = await axios.get(endpoint, {
            params: data,
            headers : {
                "Accept": "application/json"
            }
        })
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

export {
    sendMessage,
    sendGetRequest
}