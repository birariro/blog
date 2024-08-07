package com.birairo.blog.vo;

public class VoWithId<VO, ID> {
    private final VO vo;
    private final ID id;

    public VoWithId(VO vo, ID id) {
        this.vo = vo;
        this.id = id;
    }
}
