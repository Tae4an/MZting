import React from 'react';
import '../styles/LoadingSpinner.module.css';

const LoadingSpinner = () => {
    return (
        <div className="spinner-container">
            <div className="spinner"></div>
        </div>
    );
};

export {
    LoadingSpinner
};