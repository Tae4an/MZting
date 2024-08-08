import React from 'react';
import PropTypes from 'prop-types';
import styles from '../styles/SituationBanner.module.css';

const SituationBanner = ({ stages, isActualMeeting }) => {
    let description = "지인의 소개로 연락이 닿았습니다. 첫 인사와 함께 만날 약속을 잡아보세요.";

    if (isActualMeeting) {
        description = "약속 장소에서 만났습니다. 서로에 대해 더 알아가는 시간을 가져보세요.";
    } else if (stages.stage1Complete) {
        description = "첫 인사와 약속 잡기가 완료되었습니다. 실제 만남을 기다리고 있습니다.";
    } else if (stages.stage3Complete) {
        description = "소개팅이 마무리되었습니다. 다음 만남에 대해 생각해보세요.";
    }

    return (
        <div className={styles.situationBanner}>
            <p>{description}</p>
        </div>
    );
};

SituationBanner.propTypes = {
    stages: PropTypes.shape({
        stage1Complete: PropTypes.bool.isRequired,
        stage2Complete: PropTypes.bool.isRequired,
        stage3Complete: PropTypes.bool.isRequired,
    }).isRequired,
    isActualMeeting: PropTypes.bool.isRequired,
};

export { SituationBanner };