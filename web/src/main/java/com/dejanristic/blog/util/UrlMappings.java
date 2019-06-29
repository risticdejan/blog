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
    public static final String USER_ARTICLES = "article/user";
    public static final String ARTICLE = "/article";
    public static final String ARTICLE_RELEASED_LIST = "/article/released";
    public static final String ARTICLE_UNRELEASED_LIST = "/article/unreleased";
    public static final String REDIRECT_ARTICLE_UNRELEASED_LIST = "redirect:" + ARTICLE_UNRELEASED_LIST;
    public static final String ARTICLE_CREATE = "/article/create";
    public static final String REDIRECT_ARTICLE_CREATE = "redirect:" + ARTICLE_CREATE;
    public static final String ARTICLE_STORE = "/article";
    public static final String ARTICLE_SHOW = "/article";
    public static final String REDIRECT_ARTICLE_SHOW = "redirect:" + ARTICLE_SHOW;
    public static final String ARTICLE_EDIT = "/article/edit";
    public static final String REDIRECT_ARTICLE_EDIT = "redirect:" + ARTICLE_EDIT;
    public static final String ARTICLE_UPDATE = "/article/update";
    public static final String ARTICLE_DELETE = "/article/delete";
    public static final String ARTICLE_CATEGORY_LIST = "/article/category";

    private UrlMappings() {
    }
}
