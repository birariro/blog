import React, {useEffect, useRef, useState} from 'react';

const MouseFollower = () => {
    const [position, setPosition] = useState({x: 0, y: 0});
    const [isFlipped, setIsFlipped] = useState(false);
    const imageRef = useRef(null);

    useEffect(() => {
        let animationFrameId;

        const updateMousePosition = (e) => {
            animationFrameId = requestAnimationFrame(() => {
                setPosition({x: e.clientX, y: e.clientY});

                if (imageRef.current) {
                    const imageRect = imageRef.current.getBoundingClientRect();
                    const imageCenter = imageRect.left + imageRect.width / 2;
                    setIsFlipped(e.clientX > imageCenter);
                }
            });
        };

        window.addEventListener('mousemove', updateMousePosition);

        return () => {
            window.removeEventListener('mousemove', updateMousePosition);
            cancelAnimationFrame(animationFrameId);
        };
    }, []);

    return (
        <div
            style={{
                position: 'fixed',
                top: 0,
                left: 0,
                pointerEvents: 'none',
                zIndex: 9999,
                width: '100%',
                height: '100%'
            }}
        >
            {/*https://tenor.com/ko/view/bttv-rolling-cat-cute-pixelated-gif-17442480*/}
            <img
                ref={imageRef}
                src="/images/rolling-cat-pixel.gif"
                alt="Mouse follower"
                style={{
                    transition: 'all 3.0s ease-out, transform 0.1s ease-out',
                    position: 'absolute',
                    left: `${position.x}px`,
                    top: `${position.y}px`,
                    width: '30px',
                    height: 'auto',
                    transform: `translate(-50%, -50%) scaleX(${isFlipped ? -1 : 1})`,
                }}
            />
        </div>
    );
};

export default MouseFollower;

