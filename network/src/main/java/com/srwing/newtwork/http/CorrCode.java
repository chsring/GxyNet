package com.srwing.newtwork.http;

/**
 * Description:网络请求的正常返回码
 * Created by small small su
 * Date: 2021/12/13
 * Email: surao@foryou56.com
 */
public enum CorrCode {
    CODE_CORRECT(300, "网络请求成功");

    public int code;
    public String name;

    CorrCode(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
