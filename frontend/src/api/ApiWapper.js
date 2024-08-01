const ResponseToJson = (response) => {
    if (response.status === 200) {
        return response.json();
    } else if (response.status === 204) {
        return "";
    } else {
        throw new Error(`요청 실패: 상태 코드 ${response.status}`);
    }
}

export default ResponseToJson