package com.myapp.chatgpt.data.types.exception;

/**
 * @description: 自定义的关于 GPT 服务的异常
 * @author: 云奇迹
 * @date: 2024/3/17
 */
public class ChatGPTException extends RuntimeException{

    /**
     * 异常码
     */
    private String code;
    /**
     * 异常信息
     */
    private String message;

    public ChatGPTException(String code){
        this.code = code;
    }

    public ChatGPTException(String code,Throwable cause){
        this.code = code;
        this.message = cause.getMessage();
    }

    public ChatGPTException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public ChatGPTException(String code, String message,Throwable cause){
        this.code = code;
        this.message = message;
        super.initCause(cause);
    }
}
