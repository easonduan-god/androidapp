package com.aa.oa.data;

import com.aa.oa.data.model.LoggedInUser;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {



    public Result<LoggedInUser> login(String username, String password) {

            // TODO: handle loggedInUser authentication 在此处处理进行用户认证 也即是调用远程接口 并将认证成功的用户返回

            return new Result.Success<>(new LoggedInUser(1, "1"));
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
