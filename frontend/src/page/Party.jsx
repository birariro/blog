import React from 'react';
import PopCat from "../component/PopCat";
import MouseFollower from "../component/MouseFollower";
import MouseUniqueFollowers from "../component/MouseUnFollowers";

const Party = () => {
    return (
        <div>
            <PopCat/>
            <MouseFollower/>
            <MouseUniqueFollowers/>
        </div>
    );
};

export default Party;