import React, { useState } from 'react';
import axios from 'axios';

const mbtiOptions = [
    "ISTJ", "ISFJ", "INFJ", "INTJ",
    "ISTP", "ISFP", "INFP", "INTP",
    "ESTP", "ESFP", "ENFP", "ENTP",
    "ESTJ", "ESFJ", "ENFJ", "ENTJ"
];

const CompleteProfile = () => {
    const [profile, setProfile] = useState({
        age: '',
        gender: '',
        mbti: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setProfile({
            ...profile,
            [name]: value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const token = new URLSearchParams(window.location.search).get('token');
            const config = {
                headers: { Authorization: `Bearer ${token}` }
            };
            const response = await axios.post('http://localhost:8080/api/auth/complete-profile', profile, config);
            console.log(response.data);
            window.location.href = '/main';
        } catch (error) {
            console.error('Error updating profile', error);
            alert('Error updating profile');
        }
    };

    return (
        <div>
            <h1>Complete Your Profile</h1>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Age:</label>
                    <input
                        type="number"
                        name="age"
                        value={profile.age}
                        onChange={handleChange}
                        min="0"
                    />
                </div>
                <div>
                    <label>Gender:</label>
                    <select
                        name="gender"
                        value={profile.gender}
                        onChange={handleChange}
                    >
                        <option value="">Select Gender</option>
                        <option value="man">Man</option>
                        <option value="woman">Woman</option>
                    </select>
                </div>
                <div>
                    <label>MBTI:</label>
                    <select
                        name="mbti"
                        value={profile.mbti}
                        onChange={handleChange}
                    >
                        <option value="">Select MBTI</option>
                        {mbtiOptions.map((option) => (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        ))}
                    </select>
                </div>
                <button type="submit">Submit</button>
            </form>
        </div>
    );
};

export {
    CompleteProfile
}
