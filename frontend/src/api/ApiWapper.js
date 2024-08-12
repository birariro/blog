import {logout} from "../common/Information";

const ResponseToJson = (response) => {
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 204) {
        return "";
    } else if (response.status === 401) {
        logout();
        return "";
    } else if (response.status === 403) {
        logout();
        return "";
    } else {
        throw new Error(`요청 실패: 상태 코드 ${response.status}`);
    }
}

export default ResponseToJson