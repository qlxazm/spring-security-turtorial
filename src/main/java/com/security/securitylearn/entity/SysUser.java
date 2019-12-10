package com.security.securitylearn.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * @author qian
 * 使用jpa连接数据库
 */
@Data
@Entity
@Table(name = "tb_user3")
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;
    private String username;
    private String encodePassword;
    private Integer age;
}
