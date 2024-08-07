import React from 'react';
import spinner from '../assets/LoadingSpinner.gif'; // GIF 파일 경로에 맞게 수정

const LoadingSpinner = () => {
    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
            <img src={spinner} alt="Loading..." />
        </div>
    );
};

export { LoadingSpinner };
