package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {
    private static final Logger log = getLogger(SecurityUtil.class);
    static private Integer currentUser;

    static public void setCurrentUser(Integer user) {
        log.debug("login user: " + user);
        currentUser = user;
    }

    public static int authUserId() {
        return currentUser;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}