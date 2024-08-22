import React from 'react';
import adImage from '../assets/Images/광고1.png';
import styles from '../styles/AdColumn.module.css';

const AdColumnLeft = () => (
    <div className={styles.adColumnLeft}>
        <img
            src={adImage}
            alt="MZting 광고"
            className={styles.adImage}
        />
    </div>
);

const AdColumnRight = () => (
    <div className={styles.adColumnRight}>
        <img
            src={adImage}
            alt="MZting 광고"
            className={styles.adImage}
        />
    </div>
);

export { AdColumnLeft, AdColumnRight };