package com.specenergocontrol.comands;

public class HttpErrorException extends RuntimeException {

    private int code;

    public HttpErrorException(String message){
        super(message);
    }

    public HttpErrorException(int code, String message){
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
