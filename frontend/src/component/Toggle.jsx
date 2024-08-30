import React from 'react';
import './Toggle.css';


const Toggle = ({showParty, setShowParty}) => {
    return (
        <div className="cat-toggle">
            <button
                className={`toggle-button ${showParty ? 'active' : ''}`}
                onClick={() => setShowParty(!showParty)}
            >
                {showParty ? 'Hide' : 'Show'}
            </button>
        </div>
    );
};

export default Toggle;