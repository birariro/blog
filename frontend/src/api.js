import config from './config';

const articles = []
articles.push({
    "id": 0,
    "title": "first blog title",
    "content": "first blog content\n\n## h2 \n\nimage \n\n![img.jpg](https://blog.kakaocdn.net/dn/bp6jlk/btsGmYghRnI/XnknRmWKHBF2LJ2jsFkmjk/img.jpg)",
    "createdAt": "2024-08-26"
})
articles.push({
    "id": 1,
    "title": "second blog title",
    "content": "second blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-08-25"
})


function isDemo() {
    let baseUrl = config.API_BASE_URL;
    return baseUrl === 'http://localhost';
}

export const fetchArticles = async () => {
    if (isDemo()) {
        return articles;
    }
    return API(`/article`)
}

export const fetchArticle = async (id = {}) => {
    if (isDemo()) {
        return articles[id];
    }
    return API(`/article${id}`)
}

export const API = async (url = {}) => {

    let baseUrl = config.API_BASE_URL;
    const response = await fetch(`${baseUrl}${url}`, {});

    return response.json();
};