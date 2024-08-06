import React from 'react';
import styles from '../styles/Layout.module.css';
import { AdColumnLeft, AdColumnRight } from './AdColumn';

const Layout = ({ children }) => {
    return (
        <div className={styles.layout}>
            <AdColumnLeft />
            <AdColumnRight />
            <main className={styles.main}>{children}</main>

        </div>
    );
};

export { Layout };
