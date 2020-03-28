package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.domain.Grade;
import com.toby.tobyspring.user.domain.User;

public class DefaultUserUpgradePolicy implements UserUpgradePolicy {

    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Override
    public boolean canUpgrade(User user) {
        Grade currentGrade = user.getGrade();
        switch (currentGrade) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecomend() >= MIN_RECOMMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown Grade: " + currentGrade);
        }
    }
}
