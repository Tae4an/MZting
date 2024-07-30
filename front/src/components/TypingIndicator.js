import React from 'react';
import typing from '../assets/TypingEllipse.gif';

const TypingIndicator = () => {
    return (
        <div style={{ display: 'flex', alignItems: 'center', marginLeft: '10px' }}>
            <img src={typing} alt="Typing..." style={{ width: '65px', height: '45px', backgroundColor: '#3D3D56', borderRadius: '20px', objectFit: 'cover' }} />
        </div>
    );
};

export { TypingIndicator };
