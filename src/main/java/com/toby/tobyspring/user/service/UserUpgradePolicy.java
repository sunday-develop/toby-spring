package com.toby.tobyspring.user.service;

import com.toby.tobyspring.user.domain.User;

public interface UserUpgradePolicy {
    boolean canUpgrade(User user);
}
