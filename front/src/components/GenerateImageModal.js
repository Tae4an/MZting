import React, { useState, useEffect } from "react";
import PropTypes from 'prop-types';
import styles from "../styles/GenerateImageModal.module.css";

const GenerateImageModal = ({ show, onClose }) => {
    const [imageList, setImageList] = useState([]);
    const [tagList, setTagList] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [generatedImage, setGeneratedImage] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [togglePart, setTogglePart] = useState(0); // 0이 이미지 생성, 1이 다른 이미지 리스트, 2가 이미지 생성 로그

    useEffect(() => {
        // 임시 데이터로 이미지 리스트와 태그 리스트 초기화
        const tempImageList = Array(12).fill().map((_, i) => `https://via.placeholder.com/150?text=Image${i+1}`);
        setImageList(tempImageList);

        const tempTagList = ['tag1', 'tag2', 'tag3', 'tag4', 'tag5', 'tag6', 'tag7', 'tag8'];
        setTagList(tempTagList);
    }, []);

    const handleTagSelect = (tag) => {
        setSelectedTags(prev =>
            prev.includes(tag) ? prev.filter(t => t !== tag) : [...prev, tag]
        );
    };

    const generateImage = () => {
        setIsLoading(true);
        // 여기에 실제 이미지 생성 로직을 추가합니다.
        // 임시로 3초 후에 이미지가 생성되었다고 가정합니다.
        setTimeout(() => {
            setGeneratedImage("https://via.placeholder.com/300?text=GeneratedImage");
            setIsLoading(false);
        }, 3000);
    };

    const GenerateImagePart = () => {
        return (
            <div className={styles.generateImageContainer}>
                <div className={styles.tagSelection}>
                    <h2>태그선택</h2>
                    <h3>설정 태그</h3>
                    <div className={styles.tagList}>
                        {tagList.map((tag, index) => (
                            <button
                                key={index}
                                onClick={() => handleTagSelect(tag)}
                                className={selectedTags.includes(tag) ? styles.selectedTag : styles.tag}
                            >
                                {tag}
                            </button>
                        ))}
                    </div>
                    <h3>외모 태그</h3>
                    <div className={styles.tagList}>
                        {tagList.map((tag, index) => (
                            <button
                                key={index}
                                onClick={() => handleTagSelect(tag)}
                                className={selectedTags.includes(tag) ? styles.selectedTag : styles.tag}
                            >
                                {tag}
                            </button>
                        ))}
                    </div>
                    <h3>악세서리 태그</h3>
                    <div className={styles.tagList}>
                        {tagList.map((tag, index) => (
                            <button
                                key={index}
                                onClick={() => handleTagSelect(tag)}
                                className={selectedTags.includes(tag) ? styles.selectedTag : styles.tag}
                            >
                                {tag}
                            </button>
                        ))}
                    </div>
                </div>
                <div className={styles.imagePreview}>
                    <h3>Generated Image</h3>
                    {isLoading ? (
                        <p>Generating image...</p>
                    ) : generatedImage ? (
                        <img src={generatedImage} alt="Generated" className={styles.generatedImage}/>
                    ) : (
                        <img src="https://via.placeholder.com/500?text=DefaultImage" alt="Default"
                             className={styles.defaultImage}/>
                    )}
                    <div className={styles.selectedTags}>
                        <h4>Selected Tags:</h4>
                        {selectedTags.map((tag, index) => (
                            <span key={index} className={styles.selectedTag}>{tag}</span>
                        ))}
                    </div>
                    <button onClick={generateImage} disabled={isLoading || selectedTags.length === 0}>
                        Generate Image
                    </button>
                </div>
            </div>
        );
    }

    const LoadOtherImagePart = () => {
        return (
            <div>
                <div className={styles.imageGrid}>
                    {imageList.map((image, index) => (
                        <img
                            key={index}
                            src={image}
                            alt={`Generated ${index + 1}`}
                            className={styles.image}
                        />
                    ))}
                </div>
            </div>
        );
    }

    const ImageLogPart = () => {
        return (
            <div>
                <p>이미지 로그 부분</p>
            </div>
        )
    }

    const ButtonArea = () => {
        return (
            <div>
                {togglePart === 0 && (
                    <div>
                        <button onClick={() => setTogglePart(1)}>다른 이미지 보기</button>
                        <button onClick={() => setTogglePart(2)}>생성 로그 보기</button>
                    </div>
                )}
                {togglePart === 1 && (
                    <div>
                        <button onClick={() => setTogglePart(0)}>이미지 생성하기</button>
                        <button onClick={() => console.log("으에에")}>이미지 적용하기</button>
                    </div>
                )}
                {togglePart === 2 && (
                    <div>
                        <button onClick={() => setTogglePart(0)}>이미지 생성하기</button>
                        <button onClick={() => setTogglePart(1)}>이미지 적용하기</button>
                    </div>
                )}
            </div>
        )
    }

    return (
        <div className={styles.modalOverlay}>
            <div className={styles.modalContent}>
                <button className={styles.closeButton} onClick={onClose}>×</button>
                <h2 className={styles.title}>Generated Images</h2>
                {togglePart === 0 && <GenerateImagePart />}
                {togglePart === 1 && <LoadOtherImagePart />}
                {togglePart === 2 && <ImageLogPart />}
                <ButtonArea />
            </div>
        </div>
    );
};

GenerateImageModal.propTypes = {
    show: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
};

export {
    GenerateImageModal
};