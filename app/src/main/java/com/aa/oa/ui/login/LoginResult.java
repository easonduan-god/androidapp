package com.aa.oa.ui.login;

import androidx.annotation.Nullable;
import com.aa.oa.data.model.LoggedInUser;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    private int code;
    private String msg;
    private LoggedInUser user;
    @Nullable
    private Integer error;
    @Nullable
    private String errorStr;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }
    LoginResult(@Nullable String errorStr) {
        this.errorStr = errorStr;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
    @Nullable
    String getErrorStr() {
        return errorStr;
    }

    public void setSuccess(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LoggedInUser getUser() {
        return user;
    }

    public void setUser(LoggedInUser user) {
        this.user = user;
    }

    public void setError(@Nullable Integer error) {
        this.error = error;
    }
}
