package com.software.grey.utils;

public class EndPoints {

    private EndPoints(){}

    public static final String POST = "/posts";
    public static final String EXPLORE = "/explore";
    public static final String ADD_POST = "";
    public static final String SIGNUP = "/signup";
    public static final String TEST = "/test";
    public static final String LOGIN = "/login";
    public static final String LOGIN_SUCCESS = "/login/success";
    public static final String LOGIN_FAIL = "/login/fail";
    public static final String SAVE_POST = "/toggle/save";
    public static final String REPORT_POST = "/report";
    public static final String DELETE_POST = "/delete";
    public static final String VERIFY_REGISTRATION = "user/verify";
    public static final String ROOT = "http://localhost:3000";
    public static final String GET_DIARY = "/diary";
    public static final String GET_FEED = "/feed";
    public static final String USER = "/user";
    public static final String UPDATE_USER = "/update";
    public static final String GET_SAVED_POSTS = "/saved";
    public static final String REMOVE_REPORTED_POST = REPORT_POST + "/remove";

}
