package com.aa.oa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import com.aa.oa.data.Result;
import com.aa.oa.data.model.Constant;
import com.aa.oa.data.model.LoggedInUser;
import com.aa.oa.data.utils.StringUtils;
import com.aa.oa.ui.login.LoginActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 101;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
            .setDrawerLayout(drawer)
            .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /**
     * 利用反射机制调用MenuBuilder的setOptionalIconsVisible方法设置mOptionalIconsVisible为true，
     * 给菜单设置图标时才可见 让菜单同时显示图标和文字
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
            || super.onSupportNavigateUp();
    }

    public void scan(MenuItem item) {
        startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE_SCAN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case REQUEST_CODE_SCAN:
                    String url = data.getStringExtra(Intents.Scan.RESULT);
                    if(StringUtils.isScanUrl(url)){
                        RequestParams params = new RequestParams(url);
                        //params.addParameter("","");
                        x.http().request(HttpMethod.PUT,params, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String json) {
                                //0未扫码 1已扫码 2登陆请求成功 -1请求失败
                                Result<LoggedInUser> result = JSON.parseObject(json,
                                    new TypeReference<Result<LoggedInUser>>() {
                                    });
                                int code = result.getCode();
                                if(code == 1){//确认页面
                                    runOnUiThread(()->{
                                        new AlertDialog.Builder(MainActivity.this).setTitle("是否确认登陆").setIcon(R.mipmap.confirm)
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    //再套一次娃
                                                    RequestParams params = new RequestParams(url);
                                                    //取出用户名和密码
                                                    SharedPreferences sp =
                                                        getSharedPreferences("userinfo",
                                                            Context.MODE_PRIVATE);
                                                    String username =
                                                        sp.getString("username", null);
                                                    String password =
                                                        sp.getString("password", null);

                                                    params.addParameter("username",username);
                                                    params.addParameter("password", new String(Base64.decode(password, 0)));
                                                    x.http().request(HttpMethod.PUT,params, new Callback.CommonCallback<String>() {
                                                        @Override
                                                        public void onSuccess(String json) {
                                                            //0未扫码 1已扫码 2登陆请求成功 -1请求失败
                                                            Result<LoggedInUser> result = JSON.parseObject(json,new TypeReference<Result<LoggedInUser>>() {});
                                                            runOnUiThread(()->{
                                                                if(result.getCode()==2){
                                                                    Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                                                                }else{
                                                                    Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                        }
                                                        @Override public void onError(Throwable ex,boolean isOnCallback) {} @Override public void onCancelled( CancelledException cex) { }
                                                        @Override public void onFinished() { }
                                                    });
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(MainActivity.this,"取消登陆",Toast.LENGTH_LONG).show();
                                                }
                                        }).create().show();

                                    });

                                }
                            }
                            @Override public void onError(Throwable ex, boolean isOnCallback) { } @Override public void onCancelled(CancelledException cex) { }
                            @Override public void onFinished() { }
                        });
                    }
                    Toast.makeText(this,url,Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    public void logout(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("code", Constant.LOGOUT_CODE);
        startActivity(intent);
    }

    public void confirmLogin(View view) {
    }
}
