import React from 'react';
import typing from '../assets/TypingEllipse.gif';

const TypingIndicator = () => {
    return (
        <div style={{ display: 'flex', alignItems: 'center', marginLeft: '10px' }}>
            <img src={typing} alt="Typing..." style={{ width: '65px', height: '45px', backgroundColor: '#ffffff', borderRadius: '20px', objectFit: 'cover', boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)' }} />
        </div>
    );
};

export { TypingIndicator };
