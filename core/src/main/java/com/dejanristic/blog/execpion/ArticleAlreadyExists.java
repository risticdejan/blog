package com.dejanristic.blog.execpion;

public class ArticleAlreadyExists extends Exception {

    public ArticleAlreadyExists() {
    }

    public ArticleAlreadyExists(String message) {
        super(message);
    }

    public ArticleAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleAlreadyExists(Throwable cause) {
        super(cause);
    }

    public ArticleAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
