import axios from "axios";

const API_URL = 'http://localhost:8080';

const getToken = () => {
    return localStorage.getItem('authToken') || sessionStorage.getItem('authToken');
};

const sendMessage = async (message, mbti, context) => {
    console.log("Send Message :" + message + "MBTI:" + mbti + "context :" + context)
    try {
        const token = getToken();
        const response = await axios.post(`${API_URL}/api/ask-claude`,
            {
                "message": message,
                "mbti": mbti,
                "chatRoomId" : 1,
            },
            {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            }
        );
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

const sendGetRequest = async (data, endpoint) => {
    try {
        const token = getToken();
        const response = await axios.get(endpoint, {
            params: data,
            headers: {
                "Accept": "application/json",
                'Authorization': `Bearer ${token}`
            }
        })
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

const sendPostRequest = async (data, endpoint) => {
    try {
        const token = getToken();
        const response = await axios.post(endpoint, data, {
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${token}`
            }
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

export {
    sendMessage,
    sendGetRequest,
    sendPostRequest
}