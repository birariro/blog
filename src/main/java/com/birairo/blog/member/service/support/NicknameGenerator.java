package com.birairo.blog.member.service.support;

import com.birairo.blog.member.service.NicknameGenerate;
import com.birairo.blog.vo.Nickname;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class NicknameGenerator implements NicknameGenerate {
    List<String> verbs = Arrays.asList(
            "가는", "먹는", "마시는", "자는", "일어나는", "보는", "듣는", "쓰는", "읽는", "만나는",
            "공부하는", "일하는", "배우는", "가르치는", "노는", "쉬는", "춤추는", "노래하는",
            "말하는", "사랑하는", "싫어하는", "좋아하는", "필요로 하는", "사는", "파는", "만드는",
            "주는", "받는", "찾는", "도와주는", "기다리는", "시작하는", "끝나는", "여는", "닫는",
            "앉는", "서는", "타는", "걷는", "뛰는", "오는", "보내는", "쓰는", "입는", "찍는",
            "운전하는", "청소하는", "요리하는", "사랑받는", "기억하는", "잊는", "바라는", "원하는",
            "설명하는", "준비하는", "기다리는", "놀라는", "만족하는", "화내는", "걱정하는", "울는",
            "웃는", "속삭이는", "떠드는", "끓는", "익는", "치료하는", "수리하는", "고치는",
            "다루는", "싸우는", "포기하는", "승리하는", "지키는", "잃는", "쌓는", "포장하는",
            "신는", "끊는", "가져가는", "돌려주는", "떠나는", "만나는", "설치하는", "상상하는",
            "믿는", "추억하는", "감사하는", "탐색하는"
    );
    List<String> names = Arrays.asList(
            "뭉치", "두리", "코코", "라니", "별이", "뽀송", "토토", "미니", "쭈니",
            "하늘이", "쪼꼬", "달콩이", "쁘띠", "몽이", "꼬마", "하리",
            "또리", "보니", "무지", "아리", "꼬비", "룰루", "누리",
            "삐약", "쥬쥬", "포포", "꾸러기", "토리", "쫑이", "삐리",
            "피치", "말랑이", "로로", "깜찍이", "포동이", "벼리", "똘똘이",
            "츄츄", "쁘랑이", "찡찡이", "아기곰", "젤리", "말티", "바비",
            "빙글이", "콩순이", "다롱이", "초롱이", "새미", "꼬꼬", "뽀미", "쪼리", "꾸잉", "냥이",
            "루루", "콩콩", "메이", "바비", "초코", "팡팡", "베리", "까미", "토순", "새봄",
            "쫀득", "몽실", "별루", "디디", "푸름", "꼬냥", "쑥쑥", "포리", "달이", "구름",
            "말티", "누나", "새봄", "사랑", "몽글", "반디", "요미", "짱구", "몽땅",
            "둥둥", "솔비", "코튼", "쏘미", "푸푸", "젤로", "토미"
    );

    public Nickname generateNickname() {
        String verb = getRandomSelect(verbs);
        String name = getRandomSelect(names);

        return Nickname.of(String.format("%s%s%d", verb, name, (int) (Math.random() * 1000)));
    }

    private String getRandomSelect(List<String> Candidates) {
        int index = (int) (Math.random() * Candidates.size());
        return Candidates.get(index);
    }

}
