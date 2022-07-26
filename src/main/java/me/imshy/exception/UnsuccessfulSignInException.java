package me.imshy.exception;

public class UnsuccessfulSignInException extends RequestErrorException {

    public UnsuccessfulSignInException(String msg) {
        super(msg);
    }
}
