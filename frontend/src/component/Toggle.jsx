import React, {useState} from 'react';
import './Toggle.css';
import {useNavigate} from 'react-router-dom';

const Toggle = () => {
    const [showParty, setShowParty] = useState(false);

    const navigate = useNavigate();
    const handleToggle = () => {
        const show = showParty;
        setShowParty(!show);
        if (show) {
            navigate('/');

        } else {
            navigate('/party');
        }
    };

    return (
        <div className="cat-toggle">
            <button
                className={`toggle-button ${showParty ? 'active' : ''}`}
                onClick={handleToggle}
            >
                {showParty ? 'Hide' : 'Show'}
            </button>
        </div>
    );
};

export default Toggle;
