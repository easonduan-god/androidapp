package com.aa.oa.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {//用户显示用户名
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
