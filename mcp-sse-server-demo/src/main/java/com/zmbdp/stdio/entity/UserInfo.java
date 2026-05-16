package com.zmbdp.stdio.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserInfo {
    private String name;
    private Integer age;
    private String sex;
    private String address;
}