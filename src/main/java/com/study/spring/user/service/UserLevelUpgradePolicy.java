package com.study.spring.user.service;

import com.study.spring.user.domain.User;

public interface UserLevelUpgradePolicy {

    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}
