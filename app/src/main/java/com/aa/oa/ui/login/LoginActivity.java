package com.aa.oa.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.aa.oa.MainActivity;
import com.aa.oa.R;
import com.aa.oa.data.LoginRepository;
import com.aa.oa.data.model.Constant;
import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        SharedPreferences sp = getApplicationContext()
            .getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        //登陆model
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
            .get(LoginViewModel.class);
        //初始化控件
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        //判断是否注销
        if(getIntent().getIntExtra("code", 0)== Constant.LOGOUT_CODE){
            LoginRepository.getInstance().logout();
            //清空账号密码
            sp.edit().clear().commit();
        }else{//自动登陆
            String username = sp.getString("username", null);
            String password = sp.getString("password", null);
            if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(password)){
                usernameEditText.setText(username);
                passwordEditText.setText(new String(Base64.decode(password,0)));
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(username, new String(Base64.decode(password, 0)));
            }
        }

        //观察对象更新
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }else if (loginResult.getErrorStr() != null) {
                    showLoginFailed(loginResult.getErrorStr());
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }else if (loginResult.getSuccess() != null) {//可以在此处添加登陆跳转
                    updateUiWithUser(loginResult.getSuccess());
                    //Complete and destroy login activity once successful
                    finish();
                }
                setResult(Activity.RESULT_OK);

            }
        });
        //监听文本改版
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {//密码输入约束

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {//按钮点击事件
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
//                login(usernameEditText.getText().toString(),
//                    passwordEditText.getText().toString());
                loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            }
        });

        //检查权限
        checkCameraPermissions();
    }
    /**
     * 检测网络权限
     */
    private void checkCameraPermissions(){
        String[] perms = {Manifest.permission.INTERNET};
        if (EasyPermissions.hasPermissions(this, perms)) {//有权限

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_internet),
                1, perms);
        }
    }
    private void updateUiWithUser(LoggedInUserView model) {//登陆成功
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {//登陆失败信息
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
    private void showLoginFailed( String errorString) {//登陆失败信息
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}
