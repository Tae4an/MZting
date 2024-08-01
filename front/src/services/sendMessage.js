import axios from "axios";

const API_URL = 'http://localhost:8080';

const sendMessage = async (message,mbti,context) => {
    console.log("Send Message :"+message+"MBTI:"+mbti + "context :" + context)
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
                "mbti" : mbti,
                "context": context
            },
            {
                headers: header
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


export {
    sendMessage,
    sendGetRequest,
    sendPostRequest
}