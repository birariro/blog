import React, {useState} from 'react';

const PopCat = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleImage = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%'}}>
            <img
                src={isOpen ? '/images/open-cat.jpeg' : '/images/close-cat.jpeg'}
                alt={isOpen ? 'Open Cat' : 'Close Cat'}
                onClick={toggleImage}
                style={{cursor: 'pointer', height: '500px'}}
            />
        </div>
    );
};

export default PopCat;