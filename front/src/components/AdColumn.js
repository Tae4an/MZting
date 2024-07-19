import React from 'react';
import styles from '../styles/AdColumn.module.css';

const AdColumn = () => {
    return (
        <aside className={styles.adColumn}>
            <div className={styles.adContent}>광고</div>
        </aside>
    );
};

export {
    AdColumn
};