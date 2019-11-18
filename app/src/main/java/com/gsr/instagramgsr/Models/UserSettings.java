package com.gsr.instagramgsr.Models;

public class UserSettings {

    private UserAccountSetting userAccountSetting;
    private User user;

    public UserSettings(UserAccountSetting userAccountSetting, User user) {
        this.userAccountSetting = userAccountSetting;
        this.user = user;
    }

    public UserSettings() {
    }

    public UserAccountSetting getUserAccountSetting() {
        return userAccountSetting;
    }

    public void setUserAccountSetting(UserAccountSetting userAccountSetting) {
        this.userAccountSetting = userAccountSetting;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "userAccountSetting=" + userAccountSetting +
                ", user=" + user +
                '}';
    }
}
