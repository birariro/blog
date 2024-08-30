import React, {useEffect, useState} from 'react';

const MouseUnFollowers = () => {
    const [mousePosition, setMousePosition] = useState({x: 0, y: 0});
    const [cats, setCats] = useState([]);

    const getRandomPosition = () => ({
        x: Math.random() * window.innerWidth,
        y: Math.random() * window.innerHeight
    });

    useEffect(() => {
        const catImages = [
            "/images/rolling-cat-pink.gif",
            "/images/rolling-cat-green.gif"
        ];

        setCats(catImages.map((src, index) => ({
            id: index,
            src,
            position: getRandomPosition()
        })));

        const handleMouseMove = (e) => {
            setMousePosition({x: e.clientX, y: e.clientY});
        };

        window.addEventListener('mousemove', handleMouseMove);

        return () => {
            window.removeEventListener('mousemove', handleMouseMove);
        };
    }, []);

    useEffect(() => {
        const updateCatPositions = () => {
            setCats(prevCats => prevCats.map(cat => {
                let newPosition = {...cat.position};
                const dx = mousePosition.x - newPosition.x;
                const dy = mousePosition.y - newPosition.y;
                const distance = Math.sqrt(dx * dx + dy * dy);

                // 마우스와의 거리에 따라 이동 방향과 속도 결정
                const maxDistance = 200; // 마우스의 영향력이 미치는 최대 거리
                const minDistance = 50;  // 고양이가 마우스로부터 유지하려는 최소 거리

                if (distance < maxDistance) {
                    const repelStrength = Math.max(0, (maxDistance - distance) / maxDistance);
                    const moveSpeed = 2 * repelStrength; // 최대 2픽셀씩 이동

                    if (distance < minDistance) {
                        // 마우스와 너무 가까우면 반대 방향으로 이동
                        newPosition.x -= (dx / distance) * moveSpeed;
                        newPosition.y -= (dy / distance) * moveSpeed;
                    } else {
                        // 마우스와의 거리를 유지하며 천천히 움직임
                        newPosition.x += (dx / distance) * moveSpeed * 0.1;
                        newPosition.y += (dy / distance) * moveSpeed * 0.1;
                    }
                }

                // 화면 경계 체크
                newPosition.x = Math.max(0, Math.min(window.innerWidth, newPosition.x));
                newPosition.y = Math.max(0, Math.min(window.innerHeight, newPosition.y));

                return {...cat, position: newPosition};
            }));
        };

        const animationId = requestAnimationFrame(function animate() {
            updateCatPositions();
            requestAnimationFrame(animate);
        });

        return () => cancelAnimationFrame(animationId);
    }, [mousePosition]);

    return (
        <div style={{position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', pointerEvents: 'none', zIndex: 9999}}>
            {cats.map(cat => (
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
            ))}
        </div>
    );
};

export default MouseUnFollowers;