import React, { useState, useEffect } from "react";
import PropTypes from 'prop-types';
import styles from "../styles/GenerateImageModal.module.css";
import {sendGetRequest} from "../services";

const GenerateImageModal = ({ show, onClose }) => {
    const [imageList, setImageList] = useState([]);
    const [selectedTags, setSelectedTags] = useState([]);
    const [generatedImage, setGeneratedImage] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [togglePart, setTogglePart] = useState(0); // 0이 이미지 생성, 1이 다른 이미지 리스트, 2가 이미지 생성 로그

    const tagCategories = {
        hair: ['단발','장발','웨이브','포니테일','묶음'],
        camera: ['정면','측면','뒤쪽','상단 측면','셀카'],
        cloth: ['캐주얼','정장','겨울 옷'],
        background: ['도시','자연','실내'],
        accessory: ['안경']
    }

    useEffect(() => {
        // 임시 데이터로 이미지 리스트와 태그 리스트 초기화
        const tempImageList = Array(12).fill().map((_, i) => `https://via.placeholder.com/150?text=Image${i+1}`);
        setImageList(tempImageList);

        fetchTag();
    }, []);

    const fetchTag = async () => {
        const response = await sendGetRequest({}, "api/gnimage/tag")
        console.log(response)
    }

    const handleTagSelect = (category, tag) => {
        setSelectedTags(prev =>
            prev.some(t => t.category === category && t.tag === tag)
                ? prev.filter(t => !(t.category === category && t.tag === tag))
                : [...prev.filter(t => t.category !== category), { category, tag }]
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
                    <h2>태그 선택</h2>
                    {Object.keys(tagCategories).map((category, index) => (
                        <div key={index}>
                            <h3>{category}</h3>
                            <div className={styles.tagList}>
                                {tagCategories[category].map((tag, i) => (
                                    <button
                                        key={i}
                                        onClick={() => handleTagSelect(category, tag)}
                                        className={selectedTags.some(t => t.category === category && t.tag === tag) ? styles.selectedTag : styles.tag}
                                    >
                                        {tag}
                                    </button>
                                ))}
                            </div>
                        </div>
                    ))}
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
                        <h4 style={{ marginBottom: "20px" }}>Selected Tags:</h4>
                        <div className={styles.tagContainer}>
                            {selectedTags.map((tag, index) => (
                                <span key={index} className={styles.selectedTag}>{tag.category}: {tag.tag}</span>
                            ))}
                        </div>
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
                        <button onClick={() => setTogglePart(1)} style={{ marginRight: "10px" }}>다른 이미지 보기</button>
                        <button onClick={() => setTogglePart(2)} style={{ marginLeft: "10px"}}>생성 로그 보기</button>
                    </div>
                )}
                {togglePart === 1 && (
                    <div>
                        <button onClick={() => setTogglePart(0)} style={{ marginRight: "10px" }}>이미지 생성하기</button>
                        <button onClick={() => console.log("으에에")} style={{ marginLeft: "10px"}}>이미지 적용하기</button>
                    </div>
                )}
                {togglePart === 2 && (
                    <div>
                        <button onClick={() => setTogglePart(0)} style={{ marginRight: "10px" }}>이미지 생성하기</button>
                        <button onClick={() => setTogglePart(1)} style={{ marginLeft: "10px"}}>이미지 적용하기</button>
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