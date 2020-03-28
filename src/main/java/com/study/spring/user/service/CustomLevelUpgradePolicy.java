package com.study.spring.user.service;

import com.study.spring.user.domain.User;

public class CustomLevelUpgradePolicy implements UserLevelUpgradePolicy {

    @Override
    public boolean canUpgradeLevel(User user) {
        return false;
    }

    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
    }
}
