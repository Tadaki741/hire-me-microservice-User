package com.example.hirememicroserviceUser.HttpResponse;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseError {
    private final String msg;
    private final int code;
    private final Object data;

    public ResponseError(String msg, int code, Object data){
        this.msg = msg;
        this.code = code;
        this.data = data;
    }

    public ResponseError(String msg, int code){
        this(msg, code, null);
    }
}