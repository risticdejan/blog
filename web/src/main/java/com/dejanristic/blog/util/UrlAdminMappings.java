package com.dejanristic.blog.util;

public class UrlAdminMappings {

    public static final String ADMIN = "/admin";
    public static final String ADMIN_RELEASED_ARTICLES_LIST = "/admin/released";
    public static final String ADMIN_UNRELEASED_ARTICLES_LIST = "/admin/unreleased";
    public static final String REDIRECT_ADMIN_UNRELEASED_ARTICLES_LIST = "redirect:" + ADMIN_UNRELEASED_ARTICLES_LIST;
    public static final String ADMIN_ARTICLE_SHOW = "/admin/article/show";
    public static final String ADMIN_ARTICLE_DELETE = "/admin/article/delete";
    public static final String ADMIN_ARTICLE_RELEASE = "/admin/article/release";

    public UrlAdminMappings() {
    }

}
