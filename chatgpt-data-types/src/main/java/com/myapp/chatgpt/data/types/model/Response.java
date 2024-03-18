package com.myapp.chatgpt.data.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 通用的返回对象
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> implements Serializable {

    private static final long serialVersionUID = -7988151926241837899L;

    private String code;
    private String info;

    private T data;


}
