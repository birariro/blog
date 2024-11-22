import config from './config';

const articles = []
articles.push({
    "id": 0,
    "title": "first blog title",
    "summary": "first blog title",
    "content": "first blog content\n\n## h2 \n\nimage \n\n![img.jpg](https://blog.kakaocdn.net/dn/bp6jlk/btsGmYghRnI/XnknRmWKHBF2LJ2jsFkmjk/img.jpg)",
    "createdAt": "2024-08-26"
})
articles.push({
    "id": 1,
    "title": "second blog title",
    "summary": "second blog title",
    "content": "second blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-08-25"
})
articles.push({
    "id": 2,
    "title": "Free Blog Architecture", //ThumbnailUrl
    "thumbnail": "https://file.notion.so/f/f/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/7fa67c75-520a-4d57-ae3f-954ffb0ec747/image.png?table=block&id=934f3fa6-1b7a-45bc-982c-d36175353588&spaceId=7a30642b-e6b9-43db-a0ea-62c65ba3cad7&expirationTimestamp=1732348800000&signature=JugEVge7dgg1dWNPBA7RGG47huenjduhINGnF4tKjEM&downloadName=image.png",
    "tags": [
        "serverless",
        "aws"
    ],
    "summary": "\n누구나 한 번쯤 내가 직접 블로그 웹사이트를 만들어서 사용해야겠다고 생각하는 시기가 온다....\n\n\n![arch.png](https://prod-files-secure.s3.us-we",
    "content": "\n누구나 한 번쯤 내가 직접 블로그 웹사이트를 만들어서 사용해야겠다고 생각하는 시기가 온다.\n\n\n그렇다. 나는 지금 왔다. 그렇기에 간단한 요구사항을 만족하는 블로그 사이트를 개발하고 배포하려한다.\n\n- 웹 을 통해 블로그 게시물을 볼 수 있어야 한다\n- 쉽게 블로그 게시글을 작성할 수 있어야한다\n- 블로그 게시글은 수정, 삭제 등 관리가 되어야 한다.\n- 쉽게 운영, 배포가 되어야 한다.\n\n## 웹 배포 방법\n\n\n웹을 쉽게 배포하는 방법으로 아래의 후보로 AWS S3 와 Github Page가 있었으며\n\n\n어느 걸 사용해도 상관없었기에 AWS로 선택하였다.\n\n\n## CI / CD 구성\n\n\n웹과 서버를 매번 손으로 한땀한땀 배포를 하는건 참으로 어려운일 이다.\n\n\n월 실행시간 2000분을 무료로 제공하는 Github Action을 사용하여 구성한다.\n\n\n배포가 3분 정도 시간 걸릴 경우 [한달 약 600회, 하루 20회 무료이다.](https://docs.github.com/ko/billing/managing-billing-for-your-products/managing-billing-for-github-actions/about-billing-for-github-actions)\n\n\n## 서버 구성\n\n\n가장 큰 비용을 담당하는 서버와 데이터베이스를 해결해야 한다.\n\n\n간단한 블로그 수준이기에 데이터베이스는 컨테이너를 사용하는 것으로 데이터베이스 비용을 제거하면\n\n\n서버 비용만 지불하면 된다.\n\n\n그럼 서버와 데이터베이스 컨테이너를 띄울 환경을 결정해야 하는데\n\n\n소유하고 있는 시놀로지 NAS에서 제공하는 Docker를 사용하는 방법도 있지만 가장 최후의 방법이라고 생각했다.\n\n\nGCP는 Disk 30GB 와 매월 744시간(744/24 = 31일) 사용 가능한 무료 클라우드 인스턴스를 제공하는데\n\n\njar docker, mysql docker를 실행시켰더니 ssh 쉘이 끊어지고 인스턴스가 정신을 못차린다.\n\n\n따라서 AWS EC2의 t2.small(1 Core, 2G RAM, Disk 20GB) 인스턴스를 사용하여 구성하였다.\n\n\n![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/7fa67c75-520a-4d57-ae3f-954ffb0ec747/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=c985b0cd5b70479b4f0f89fdca30f8702d062bae01cf10163ae6033e46fd9e15&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\n그렇게 웹과 서버는 위의 구조로 구성 되었다.\n\n\n### 그러나 DB 만 비싼줄 알았더니 EC2도 비싸다.\n\n\n![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/060fa76e-6ebd-41fb-87b0-9a066554ffd9/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=e668d57af46ce236f31b166d86f4a86b186c7d93d7f705bc39b29905e57fb9e3&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\n10일동만 가만히 띄워놓기만 한 비용이다.\n\n\n즉 한달에 약 7만원이 예상되는 비용이 발생하게 되는데 단순 CRUD API 주제에 월 7만원을 내야한다니 허락 할 수 없다.\n\n\n### 앞으로 무료만 쓴다\n\n\n돈은 쓰기 싫고 블로그는 운영하고싶은 이기심이 든 나에겐 Serverless가 희망이였다\n\n\nServerless의 중요한 무료 정책은 아래와 같다.\n\n- DymamoDB 25GB 무료\n- Lambda 월 1백만건 무료\n- API Gateway 월 1백만건 HTTP API 호출 무료\n\nAPI 요청이 월 1천만건 무료라는 말은 초당 3.85 회(10000000/30/24/60/60) 무료 라는것 이고\n\n\nAPI 요청이 저녁 12시 부터 아침7시까지는 없다고 예상하면 초당 5.44 회(10000000/30/17/60/60)도 가능하다.\n\n\n그리고 DymamoDB를 사용한다면 데이터베이스도 무료이다.\n\n\n또한 블로그 게시글의 리소스 파일을 저장하는 S3도 잠재적인 공포의 대상이다.\n\n\n“Notion같은 서비스는 어떻게 리소스 비용을 부담하는거지?” 라는 생각이 들다보니\n\n\n내 블로그 리소스를 Notion에서 관리하면 나는 비용이 공짜 아닌가? 라는 생각이 든다.\n\n\n![arch.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/cbfbae02-6847-4b28-a92d-e3f705f17de1/arch.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=68ca4dd612863ec2d1165dac2189f36083783fcf9219007985d4eecd56f5c396&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\n블로그 게시글 리소스를 Notion으로 처리하니 관리도 용이해진다.\n\n\n![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/8896f976-4b2e-41e0-a8e6-47ece9011724/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=c37dc17a94d3b41ee6352a08314509c492c47214a80dff3c55ad838b4131e191&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\nEC2(30일(11.43 *3 = 34.29)) 를 사용했을때와 비교해보면 98.5 %감소 하였다.\n\n\n도메인 라우팅 비용 한 달에 700원.\n\n\n### 만약 웹 비용도 많이 나왔다면 용서없었다\n\n\nGithub Page로 전환 했을것이다.\n\n\n![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/fca6673e-45db-4af6-aafd-96886d6c096a/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=82fc251c43820fb511042231a92510bd6dc499ff3d4c52b5d3e513a896b206ab&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\n### lambda 의 배포를 쉽게 해야한다\n\n\nlambda 코드를 배포하는 과정을 쉽게 하기위해 serverless를 사용한다\n\n\n![arch.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/8763c918-cd21-4868-8889-9063144c0a32/arch.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=b87ac5129f2fa766bf973404f690565af4046bd799f192089dbe462f442655eb&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n\n### 앞으로 700원씩 게속 낼것인가?\n\n\n어림없지.\n\n\n![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/7a30642b-e6b9-43db-a0ea-62c65ba3cad7/c7b79c36-f137-487c-b4c7-73e216513a50/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45FSPPWI6X%2F20241122%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20241122T023844Z&X-Amz-Expires=3600&X-Amz-Signature=77ee402c7555e05fe216a0097a32414a314846fb8b8448fc1e863ebc9a999733&X-Amz-SignedHeaders=host&x-id=GetObject)\n\n",
    "createdAt": "2024-08-25"
})

articles.push({
    "id": 3,
    "title": "테스트팁 - 테스트 하기 좋은 코드",
    "summary": "\n## Application.class 에 불필요한 어노테이션 추가 금지\n\n\n```java...rent/user-guide/#writing-tests-assertions-third-pa",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-06-26"
})
articles.push({
    "id": 4,
    "title": "Redis 를 사용한 Distributed Lock",
    "summary": "\n분산 환경에서 서로 다른 클라이언트가 공유 리소스를 사용하는 경우\n\n\n동시성 문제가 발생...ption(\"lock timeout\");\n\t      }\n\n\t      return//to",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-05-31"
})
articles.push({
    "id": 5,
    "title": "JPA Entity ID 타입 선택시 후보들",
    "summary": "\n# GenerationType identity, sequence, table\n\n\niden...roovy\nimplementation 'com.github.f4b6a3:uuid-creat",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-05-27"
})
articles.push({
    "id": 6,
    "title": "@Transactional(readOnly = true) 는 Flush 가 동작안할꺼라 믿었다",
    "summary": "\n```java\npublic class MarineService {\n    WeaponSe...tends T> TypedQuery<S> getQuery(@Nullable Specific",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2024-05-23"
})
articles.push({
    "id": 7,
    "title": "깃허브 잔디 조작해서 아스키 아트 그리기",
    "summary": "\n로컬에만 커밋을 해 두고 푸시 안 하고 있다가\n\n\n며칠이 지난 나중에 푸시하니 로컬에 커...값을 도출하고 있는지 확인하기 위해\n\n\ngit add, commit와 같은 고수준 명령을 ",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2023-12-07"
})
articles.push({
    "id": 8,
    "title": "QueryDSL에서 SubQuery 에는 limit 를 사용 할 수 없다.",
    "summary": "\n> QueryDSL 5.0.0에서 발생\n\n\nSubQuery 종류 중\n\n\n[from 절에 ...rayList<AfterLoadAction>();\n\n    final SqlStatemen",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2023-11-15"
})
articles.push({
    "id": 9,
    "title": "QueryDSL-JPA에서 인라인뷰를 써야 한다면 도망치자",
    "summary": "\n> QueryDSL 5.0.0에서 발생\n\n\nQuery DSL JPA을 사용도중 selec...ect(\n                    detailHistory.key.as(\"key",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2023-10-18"
})
articles.push({
    "id": 10,
    "title": "Java 의 Generic Type Erasure",
    "summary": "\n![img.webp](https://blog.kakaocdn.net/dn/bGFtr3/b....getReturnType();\n\n  System.out.println(\"type = \" ",
    "content": "blog content\n\n### h3 \n\ncontent",
    "createdAt": "2023-08-31"
})


function isDemo() {
    let baseUrl = config.API_BASE_URL;
    return baseUrl === 'demo';
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
    return API(`/article/${id}`)
}

export const API = async (url = {}) => {

    let baseUrl = config.API_BASE_URL;
    const response = await fetch(`${baseUrl}${url}`, {});

    return response.json();
};