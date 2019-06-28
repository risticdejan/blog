package com.dejanristic.blog.exception;

public class ArticleNotFound extends Exception {

    public ArticleNotFound() {
    }

    public ArticleNotFound(String message) {
        super(message);
    }

    public ArticleNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleNotFound(Throwable cause) {
        super(cause);
    }

    public ArticleNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
