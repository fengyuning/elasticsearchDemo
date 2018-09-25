package com.pirate.esredisdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestBO {
    private Integer id;
    private Integer a;
    private Integer b;


    public static void main(String[] args) {

        List<TestBO> list = new ArrayList<>();
        list.add(new TestBO(1, 1, 1));
        list.add(new TestBO(2, 3, 2));
        list.add(new TestBO(3, 3, 3));

        System.out.println(list.indexOf(new TestBO(1, 1, 1)));
    }
}
