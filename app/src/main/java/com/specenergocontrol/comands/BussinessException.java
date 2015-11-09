package com.specenergocontrol.comands;

/**
 * Created by Комп on 31.03.2015.
 */
public class BussinessException extends RuntimeException {

    public static final int CODE_TRY_COUNT = 0x0010;
    public static final int CODE_UNCNOUN_USER = 0x0020;
    private int code;

    public BussinessException(String message){
        super(message);
    }

    public BussinessException(int code){
        super("");
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
