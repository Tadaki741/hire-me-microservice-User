package com.example.hirememicroserviceUser.HttpResponse;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseBody {
    private final Object data;
    private final ResponseError error;

    public ResponseBody(Object data, ResponseError error){
        this.data = data;
        this.error = error;
    }

    public ResponseBody(Object data){
        this(data, null);
    }

    public ResponseBody(){
        this.data = null;
        this.error = null;
    }
}