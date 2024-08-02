import axios from "axios";

const API_URL = 'http://localhost:8080';

const sendMessage = async (message, mbti, chatRoomId) => {
    console.log("Send Message :" + message + " MBTI:" + mbti + " ChatRoomId:" + chatRoomId)
    try {
        const header = {
            'Content-Type': 'application/json'
        }

        const authToken = sessionStorage.getItem('authToken');
        if(authToken) {
            header['Authorization'] = `Bearer ${authToken}`;
        }

        const response = await axios.post(`${API_URL}/api/ask-claude`,
            {
                "message": message,
                "mbti": mbti,
                "chatRoomId": chatRoomId
            },
            {
                headers: header
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
        const header = {
            "Accept": "application/json"
        }

        const authToken = sessionStorage.getItem('authToken');
        if(authToken) {
            header['Authorization'] = `Bearer ${authToken}`;
        }

        const response = await axios.get(endpoint, {
            params: data,
            headers : header
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
        const header = {
            'Content-Type': 'application/json'
        }

        const authToken = sessionStorage.getItem('authToken');
        if(authToken) {
            header['Authorization'] = `Bearer ${authToken}`;
        }

        const response = await axios.post(endpoint, data, {
            headers: header
        });
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending message:', error);
        throw error;
    }
};

const initializeChat = async (mbti, chatRoomId) => {
    console.log("Initializing chat for MBTI:" + mbti + " ChatRoomId:" + chatRoomId)
    try {
        const response = await axios.post(`${API_URL}/api/initialize-chat`,
            {
                "mbti": mbti,
                "chatRoomId": chatRoomId
            },
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        );
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error('Error initializing chat:', error);
        throw error;
    }
};


export {
    sendMessage,
    sendGetRequest,
    sendPostRequest,
    initializeChat
}