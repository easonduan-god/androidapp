package com.aa.oa.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Base64;
import android.widget.Toast;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.aa.oa.LoginException;
import com.aa.oa.data.LoginRepository;
import com.aa.oa.data.Result;
import com.aa.oa.data.model.Constant;
import com.aa.oa.data.model.LoggedInUser;
import com.aa.oa.R;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        //Result<LoggedInUser> result = remoteLogin(username, password);
//1、参数封装
        RequestParams params = new RequestParams(SERVER_HOST_LOGIN);
        params.addParameter("username",username);
        params.addParameter("password",password);
        LoggedInUser user = new LoggedInUser(1,username);


        //2、调用接口登陆 post方法
        x.http().request(HttpMethod.PUT, params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String json) {
                Result<LoggedInUser> result = null;
                //3、将返回的json解析为Result对象
                Result<LoggedInUser> resultObj = JSON.parseObject(json, new TypeReference<Result<LoggedInUser>>(){});
                if(resultObj.getCode()==0){
                    resultObj.getObj().setPassword(password);
                    result = new Result.Success<LoggedInUser>(resultObj.getObj());

                }

                if (result instanceof Result.Success) {
                    LoggedInUser user = ((Result.Success<LoggedInUser>)result).getData();
                    loginResult.setValue(new LoginResult(new LoggedInUserView(user.getUserName())));


                    //处理登陆仓库
                    loginRepository.setLoggedInUser(user);

                    //将用户名密码保存到本地 以便于下次登陆
                    SharedPreferences sp = x.app()
                        .getApplicationContext()
                        .getSharedPreferences("userinfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("username",user.getLoginName());
                    editor.putString("password", Base64.encodeToString(password.getBytes(), 0));
                    editor.commit();
                }
                else {
                    loginResult.setValue(new LoginResult(resultObj.getMsg()));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LoginResult loginResult = new LoginResult("登陆失败");
                LoginViewModel.this.loginResult.setValue(loginResult);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }

    public static final String SERVER_HOST_LOGIN = Constant.SERVER_HOST + "android/login";

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        }
        else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        }
        else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
        else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
