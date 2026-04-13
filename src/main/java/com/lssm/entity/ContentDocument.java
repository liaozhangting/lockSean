package com.lssm.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.time.LocalDateTime;

@Data
@Document(indexName = "content_index")
@Setting(shards = 3, replicas = 1)
public class ContentDocument {
    
    @Id
    private Long id;
    
    @Field(type = FieldType.Long)
    private Long userId;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String title;
    
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String content;
    
    @Field(type = FieldType.Keyword)
    private String type;
    
    @Field(type = FieldType.Keyword)
    private String mediaUrl;
    
    @Field(type = FieldType.Integer)
    private Integer likeCount;
    
    @Field(type = FieldType.Integer)
    private Integer commentCount;
    
    @Field(type = FieldType.Integer)
    private Integer collectCount;
    
    @Field(type = FieldType.Double)
    private Double latitude;
    
    @Field(type = FieldType.Double)
    private Double longitude;
    
    @Field(type = FieldType.Keyword)
    private String location;
    
    @Field(type = FieldType.Date)
    private LocalDateTime createTime;
    
    @Field(type = FieldType.Date)
    private LocalDateTime updateTime;
}
