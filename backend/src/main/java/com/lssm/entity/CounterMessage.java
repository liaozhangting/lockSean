package com.lssm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CounterMessage implements Serializable {

    private static final long serivalVersionUID = 1L;

    /**
     * 内容ID
     */
    private Long contentId;
    /**
     * 计数器类型：view，like,comment,share
     */
    private CounterType counterType;

    /**
     * 增量值
     */
    private Integer delta;

    /**
     * 时间戳
     */
    private Long timestamp;

    public enum CounterType{
        VIEW("views","浏览量"),
        LIKE("like","点赞量"),
        COMMENT("comment","评论量"),
        SHARE("share","分享量");;

        @Getter
        private final String field;
        @Getter
        private final String desc;

        CounterType(String field, String desc){
            this.field = field;
            this.desc = desc;
        }

    }
}
