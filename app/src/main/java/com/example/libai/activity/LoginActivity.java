package com.example.libai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.R;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.databases.UserDao;
import com.example.libai.model.User;
import com.example.libai.utils.CacheUtils;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_menu)
    ImageButton ibMenu;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    @BindView(R.id.ib_fontsize)
    ImageButton ibFontsize;
    @BindView(R.id.ib_collection)
    ImageButton ibCollection;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private String phone;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();

        //登录检测
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etPhone.getText().toString();
                password = etPassword.getText().toString();

                //检查手机号和密码是否正确
                checkLogin();

            }
        });

        //跳转到注册界面
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        //关闭页面
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void checkLogin() {

        UserDao userDao = new UserDao(this);
        User user = new User(phone,password);
        boolean isUser = userDao.checkUser(user);

        if (phone.equals("") || password.equals("")) {
            Toast.makeText(LoginActivity.this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isUser) {
            Toast.makeText(LoginActivity.this, "手机号或密码不正确！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            CacheUtils cacheUtils = new CacheUtils();
            cacheUtils.putString(this, DatabaseHelper.PHONE, phone);

            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {

        tvTitle.setText("登录");
        ibMenu.setVisibility(View.GONE);
        ibFontsize.setVisibility(View.GONE);
        ibCollection.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);

    }
}
