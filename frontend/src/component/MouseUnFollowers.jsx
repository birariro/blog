import React, {useEffect, useState} from 'react';

// 상수 정의
const CAT_IMAGES = [
    "/images/rolling-cat-pink.gif",
    "/images/rolling-cat-green.gif"
];
const RAINBOW_CAT_IMAGE = "/images/rolling-cat-rainbow.gif";
const MAX_DISTANCE = 200;
const MIN_DISTANCE = 70;
const RAINBOW_CAT_TRIGGER_DISTANCE = 40;

// 타입 정의
/**
 * @typedef {Object} Position
 * @property {number} x
 * @property {number} y
 */

/**
 * @typedef {Object} Cat
 * @property {string} id
 * @property {string} src
 * @property {Position} position
 */

/**
 * @typedef {Object} RainbowCat
 * @property {string} id
 * @property {string} src
 * @property {Position} position
 * @property {Position} targetPosition
 * @property {number} speed
 */

// 유틸리티 함수
const getRandomPosition = () => ({
    x: Math.random() * window.innerWidth,
    y: Math.random() * window.innerHeight
});

const calculateDistance = (pos1, pos2) =>
    Math.sqrt(Math.pow(pos1.x - pos2.x, 2) + Math.pow(pos1.y - pos2.y, 2));

// 커스텀 훅: 마우스 위치 추적
const useMousePosition = () => {
    const [mousePosition, setMousePosition] = useState({x: 0, y: 0});

    useEffect(() => {
        const handleMouseMove = (e) => {
            setMousePosition({x: e.clientX, y: e.clientY});
        };

        window.addEventListener('mousemove', handleMouseMove);
        return () => window.removeEventListener('mousemove', handleMouseMove);
    }, []);

    return mousePosition;
};

// 커스텀 훅: 고양이 위치 관리
const useCatPositions = (mousePosition) => {
    const [cats, setCats] = useState([]);

    useEffect(() => {
        setCats(CAT_IMAGES.map((src, index) => ({
            id: `cat-${index}`,
            src,
            position: getRandomPosition()
        })));
    }, []);

    useEffect(() => {
        const updatePositions = () => {
            setCats(prevCats => prevCats.map(cat => {
                let newPosition = {...cat.position};
                const dx = mousePosition.x - newPosition.x;
                const dy = mousePosition.y - newPosition.y;
                const distance = calculateDistance(mousePosition, newPosition);

                if (distance < MAX_DISTANCE) {
                    const repelStrength = Math.max(0, (MAX_DISTANCE - distance) / MAX_DISTANCE);
                    const moveSpeed = 2 * repelStrength;

                    if (distance < MIN_DISTANCE) {
                        newPosition.x -= (dx / distance) * moveSpeed;
                        newPosition.y -= (dy / distance) * moveSpeed;
                    } else {
                        newPosition.x += (dx / distance) * moveSpeed * 0.1;
                        newPosition.y += (dy / distance) * moveSpeed * 0.1;
                    }
                }

                newPosition.x = Math.max(0, Math.min(window.innerWidth, newPosition.x));
                newPosition.y = Math.max(0, Math.min(window.innerHeight, newPosition.y));

                return {...cat, position: newPosition};
            }));
        };

        const animationId = requestAnimationFrame(updatePositions);
        return () => cancelAnimationFrame(animationId);
    }, [mousePosition]);

    return cats;
};

// 커스텀 훅: 레인보우 고양이 관리
const useRainbowCat = (cats) => {
    const [rainbowCat, setRainbowCat] = useState(false);

    useEffect(() => {
        if (cats.length < 2) return;

        const distance = calculateDistance(cats[0].position, cats[1].position);

        if (distance < RAINBOW_CAT_TRIGGER_DISTANCE && !rainbowCat) {
            setRainbowCat({
                id: 'rainbow',
                src: RAINBOW_CAT_IMAGE,
                position: getRandomPosition(),
                targetPosition: getRandomPosition()
            });
            cats[0].position = getRandomPosition();
            cats[1].position = getRandomPosition();

        }
    }, [cats, rainbowCat]);


    return rainbowCat;
};

// 컴포넌트: 개별 고양이
const Cat = React.memo(({cat, mousePosition}) => (
    <img
        key={cat.id}
        src={cat.src}
        alt={`Cat ${cat.id}`}
        style={{
            position: 'absolute',
            left: `${cat.position.x}px`,
            top: `${cat.position.y}px`,
            width: '40px',
            height: 'auto',
            transform: `translate(-50%, -50%) scaleX(${cat.position.x < mousePosition.x ? 1 : -1})`,
            transition: 'transform 0.1s ease-out',
        }}
    />
));

// 메인 컴포넌트
const MouseUnFollowers = () => {
    const mousePosition = useMousePosition();
    const cats = useCatPositions(mousePosition);
    const rainbowCat = useRainbowCat(cats);

    return (
        <div style={{position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', pointerEvents: 'none', zIndex: 9999}}>
            {cats.map(cat => (
                <Cat key={cat.id} cat={cat} mousePosition={mousePosition}/>
            ))}
            {rainbowCat && (
                <img
                    key={rainbowCat.id}
                    src={rainbowCat.src}
                    alt="Rainbow Cat"
                    style={{
                        position: 'absolute',
                        left: `${rainbowCat.position.x}px`,
                        top: `${rainbowCat.position.y}px`,
                        width: '100px',
                        height: 'auto',
                        transform: 'translate(-50%, -50%)',
                        transition: 'all 0.1s ease-out',
                    }}
                />
            )}
        </div>
    );
};

export default MouseUnFollowers;