import React from 'react';
import styles from '../styles/AdColumn.module.css';

const AdColumnLeft = () => (
    <div className={styles.adColumnLeft}>
        <div className={styles.adContent}>광고</div>
    </div>
);

const AdColumnRight = () => (
    <div className={styles.adColumnRight}>
        <div className={styles.adContent}>광고</div>
    </div>
);

export { AdColumnLeft, AdColumnRight };
