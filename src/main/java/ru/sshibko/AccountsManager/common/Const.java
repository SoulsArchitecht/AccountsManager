package ru.sshibko.AccountsManager.common;

public class Const {

    public static final String REGEX_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String REGEX_LOGIN = "^(?i:([A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6})|([\\d\\w]+))$";
    public static final String REGEX_LINK = "((((https?://)(www\\.)?)|www\\.)[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|])";

}
