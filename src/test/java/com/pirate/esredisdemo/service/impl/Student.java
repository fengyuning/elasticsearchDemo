package com.pirate.esredisdemo.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;


@Document(indexName = "class", type = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    private Integer id;
    private String name;
    private String firstName;
    private Integer age;
    private Integer depId;
    private List<String> inters;
}
