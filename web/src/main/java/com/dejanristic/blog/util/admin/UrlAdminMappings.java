package com.dejanristic.blog.util.admin;

public class UrlAdminMappings {

    public static final String ADMIN = "/admin";
    public static final String ADMIN_RELEASED_ARTICLES_LIST = "/admin/released";
    public static final String ADMIN_UNRELEASED_ARTICLES_LIST = "/admin/unreleased";
    public static final String REDIRECT_ADMIN_UNRELEASED_ARTICLES_LIST = "redirect:" + ADMIN_UNRELEASED_ARTICLES_LIST;
    public static final String ADMIN_ARTICLE_SHOW = "/admin/article/show";
    public static final String ADMIN_ARTICLE_DELETE = "/admin/article/delete";
    public static final String ADMIN_ARTICLE_RELEASE = "/admin/article/release";
    public static final String ADMIN_USER_LIST = "/admin/user";
    public static final String ADMIN_USER_SHOW = "/admin/user/show";
    public static final String REDIRECT_ADMIN_USER_LIST = "redirect:" + ADMIN_USER_LIST;
    public static final String ADMIN_USER_BANNED = "/admin/user/banned";
    public static final String ADMIN_USER_UNBANNED = "/admin/user/unbanned";

    public UrlAdminMappings() {
    }

}
