package com.pirate.esredisdemo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 响应结果
 *
 * @author fyn
 * @version 1.0 2018/08/27
 */
@Document(indexName = "bank", type = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsAccount {
    @Id
    private int accountNumber;
    private int balance;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;
}
