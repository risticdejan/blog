package com.dejanristic.blog.util;

public class UrlMappings {

    public static final String HOME = "/home";
    public static final String REDIRECT_HOME = "redirect:" + HOME;
    public static final String ABOUT = "/about";
    public static final String CONTACT = "/contact";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REGISTER = "/register";
    public static final String REDIRECT_REGISTRER = "redirect:" + REGISTER;
    public static final String ARTICLE_CREATE = "/article/create";
    public static final String REDIRECT_ARTICLE_CREATE = "redirect:" + ARTICLE_CREATE;
    public static final String ARTICLE_STORE = "/article";

    private UrlMappings() {
    }
}
