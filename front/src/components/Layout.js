import React from 'react';
import styles from '../styles/Layout.module.css';
import { AdColumn } from './';

const Layout = ({ children }) => {
    return (
        <div className={styles.layout}>
            <AdColumn />
            <main className={styles.main}>{children}</main>
            <AdColumn />
        </div>
    );
};

export {
    Layout
};