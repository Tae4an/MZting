import React, { useState, useEffect } from "react";
import PropTypes from 'prop-types';
import styles from "../styles/GenerateImageModal.module.css";
import {sendGetRequest, sendPostRequest} from "../services";
import {LoadingSpinner} from "./LoadingSpinner";
import {ImageLogModal} from "./ImageLogModal";

const GenerateImageModal = ({ show, onClose, profileId }) => {
    const [tagList, setTagList] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [currentImage, setCurrentImage] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [logData, setLogData] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [togglePart, setTogglePart] = useState(0); // 0이 이미지 생성, 1이 생성 로그 확인
    const [selectedImage, setSelectedImage] = useState(null);

    useEffect(() => {
        fetchTags();
    }, []);

    useEffect(() => {
        if (togglePart === 1) {
            fetchImageLogs(currentPage);
        }
    }, [togglePart, currentPage]);

    const handleImageClick = (imageUrl) => {
        setSelectedImage(imageUrl);
    };

    const fetchImageLogs = async (page) => {
        try {
            const response = await sendPostRequest({ page, size: 12 }, "api/gnimage/log/all");
            setLogData(response);
        } catch (error) {
            console.error("Failed to fetch image logs:", error);
        }
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
        fetchImageLogs(newPage);
    };

    const requestGenerateImage = async () => {
        setIsLoading(true)
        const requestTags = selectedTags.map(tag => tag.engName);
        const requestData = { tags: requestTags }
        const response = await sendPostRequest(requestData, "api/gnimage/generate")
        setCurrentImage(response.imageUrl)
        setIsLoading(false)
    }

    const fetchTags = async () => {
        const response = await sendGetRequest({}, "api/gnimage/tag")
        const processedTags = response.reduce((acc, item) => {
            if (!acc[item.category]) {
                acc[item.category] = [];
            }
            acc[item.category].push(item);
            return acc;
        }, {});
        setTagList(processedTags)
    }

    const handleTagSelect = (category, tag) => {
        setSelectedTags(prev =>
            prev.some(t => t.category === category && t.korName === tag.korName)
                ? prev.filter(t => !(t.category === category && t.korName === tag.korName))
                : [...prev.filter(t => t.category !== category), { ...tag, category }]
        );
    };

    const handleApplyImage = async (imageUrl) => {
        if(imageUrl != null) {
            const requestData = {
                imageUrl: imageUrl
            }
            const applyResponse = await sendPostRequest(requestData, `api/gnimage/apply/${profileId}`)
            console.log(applyResponse)
            console.log("적용 완료")
            setSelectedImage(null); // 모달 닫기
        } else {
            console.log("안댐, 로직 추가 필요")
        }
    }

    const TagSelectionPart = () => {
        return (
            <div className={styles.tagSelection}>
                <h2>태그 선택</h2>
                {Object.keys(tagList).map((category, index) => (
                    <div key={index}>
                        <h3>{category}</h3>
                        <div className={styles.tagList}>
                            {tagList[category].map((tag, i) => (
                                <button
                                    key={i}
                                    onClick={() => handleTagSelect(category, tag)}
                                    className={selectedTags.some(t => t.category === category && t.korName === tag.korName) ? styles.selectedTag : styles.tag}
                                >
                                    {tag.korName}
                                </button>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        );
    }

    const ImagePreviewPart = ({ isLoading, currentImage, selectedTags, requestGenerateImage, handleApplyImage }) => {
        return (
            <div className={styles.imagePreview}>
                <h3>Generated Image</h3>
                <div className={styles.imageContainer}>
                    {isLoading ? (
                        <div className={styles.spinnerContainer}>
                            <LoadingSpinner/>
                        </div>
                    ) : currentImage ? (
                        <img
                            src={currentImage}
                            alt="Generated"
                            className={styles.generatedImage}
                            onClick={() => handleImageClick(currentImage)}
                        />
                    ) : (
                        <img
                            src="https://firebasestorage.googleapis.com/v0/b/mzting.appspot.com/o/default%2FgenerateDefault.png?alt=media&token=1ba0e005-49d3-43bc-84d3-43574eeccef9"
                            alt="Default"
                            className={styles.defaultImage}
                        />
                    )}
                </div>
                <div className={styles.selectedTags}>
                    <h4 style={{marginBottom: "20px"}}>Selected Tags:</h4>
                    <div className={styles.tagContainer}>
                        {selectedTags.map((tag, index) => (
                            <span key={index} className={styles.selectedTag}>{tag.category}: {tag.korName}</span>
                        ))}
                    </div>
                </div>
                <button onClick={requestGenerateImage} disabled={isLoading || selectedTags.length === 0}>
                    Generate Image
                </button>
                {currentImage && (
                    <button onClick={() => handleApplyImage(currentImage)} className={styles.applyButton}>
                        이미지 적용하기
                    </button>
                )}
            </div>
        );
    };

    const GenerateImagePart = () => {
        return (
            <div className={styles.generateImageContainer}>
                <TagSelectionPart />
                <ImagePreviewPart
                    isLoading={isLoading}
                    currentImage={currentImage}
                    selectedTags={selectedTags}
                    requestGenerateImage={requestGenerateImage}
                    handleApplyImage={handleApplyImage}
                />
            </div>
        );
    }

    const ImageLogPart = () => {
        if (!logData) {
            return <div>Loading...</div>;
        }

        return (
            <div className={styles.imageLogContainer}>
                <h2>생성된 이미지 로그</h2>
                <div className={styles.imageGrid}>
                    {logData.contents.map((log) => (
                        <div key={log.id} className={styles.imageItem}>
                            <img
                                src={log.imageUrl}
                                alt={`Generated at ${log.gnTime}`}
                                className={styles.image}
                                onClick={() => handleImageClick(log.imageUrl)}
                            />
                            <p>{new Date(log.gnTime).toLocaleString()}</p>
                        </div>
                    ))}
                </div>
                <div className={styles.pagination}>
                    <button
                        onClick={() => handlePageChange(currentPage - 1)}
                        disabled={currentPage === 0}
                    >
                        이전
                    </button>
                    <span>{`${currentPage + 1} / ${logData.paginationInfo.totalPages}`}</span>
                    <button
                        onClick={() => handlePageChange(currentPage + 1)}
                        disabled={currentPage === logData.paginationInfo.totalPages - 1}
                    >
                        다음
                    </button>
                </div>
            </div>
        );
    };

    const ButtonArea = () => {
        return (
            <div>
                {togglePart === 0 && (
                    <div>
                        <button onClick={() => setTogglePart(1)} style={{marginLeft: "10px"}}>생성 로그 보기</button>
                    </div>
                )}
                {togglePart === 1 && (
                    <div>
                        <button onClick={() => setTogglePart(0)} style={{marginRight: "10px"}}>이미지 생성하기</button>
                    </div>
                )}
            </div>
        )
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <h2 className={styles.title}>캐릭터 이미지 설정</h2>
                {togglePart === 0 && <GenerateImagePart />}
                {togglePart === 1 && <ImageLogPart />}
                <ButtonArea />
                <ImageLogModal
                    show={selectedImage !== null}
                    onClose={() => setSelectedImage(null)}
                    image={selectedImage}
                    onApply={handleApplyImage}
                />
            </div>
        </div>
    );
};

GenerateImageModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    profileId: PropTypes.string.isRequired,
};

export { GenerateImageModal };