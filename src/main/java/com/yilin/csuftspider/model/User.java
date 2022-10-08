package com.yilin.csuftspider.model;

import lombok.Data;

/**
 * Title: User
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */
@Data
public class User {
    /**
     * 姓名
     */
    private String name;


    /**
     * 学号
     */

    private String sid;

    public User(String name, String sid) {
        this.name = name;
        this.sid = sid;
    }

    public User() {
    }
}
