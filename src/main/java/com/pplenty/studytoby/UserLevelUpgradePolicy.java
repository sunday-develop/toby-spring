package com.pplenty.studytoby;

/**
 * Created by yusik on 2020/03/28.
 */
public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
