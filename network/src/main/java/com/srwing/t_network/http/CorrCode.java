package com.srwing.t_network.http;

/**
 * @author srwing
 * @fileName CorrCode
 * @time 2022/6/17 2:36 下午
 * @Email 694177407@qq.com (有问题请邮件)
 * @desc
 */
public enum CorrCode {
    CODE_CORRECT(0, "网络请求成功");

    public int code;
    public String name;

    CorrCode(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
