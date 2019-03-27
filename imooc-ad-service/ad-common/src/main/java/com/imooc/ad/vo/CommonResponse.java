package com.imooc.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Alex
 * @Desc <p>
 *     统一响应
 * </p>
 * @Date 2019/2/24 16:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {

    private  int code;
    private String message;
    private T data;
    public CommonResponse(int code,String message){
        this.code = code;
        this.message = message;
    }
}
