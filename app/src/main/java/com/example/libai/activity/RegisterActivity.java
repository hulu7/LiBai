package com.example.libai.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.R;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.databases.UserDao;
import com.example.libai.model.User;
import com.example.libai.utils.CacheUtils;

public class RegisterActivity extends AppCompatActivity {

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
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_reset)
    Button btnReset;
    private String phone;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initView();

        //注册一个新用户
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = etPhone.getText().toString();
                password = etPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();

                //检查用户的输入
                checkUser();

            }
        });

        //关闭页面
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //文本框重置
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPassword.setText("");
                etPhone.setText("");
                etConfirmPassword.setText("");
            }
        });

    }

    /**
     * 检查用户是否输入正确
     */
    private void checkUser() {

        boolean isPhoneNum = isMobileNO(phone);

        UserDao userDao = new UserDao(this);

        if (phone.equals("") || phone == null) {
            Toast.makeText(RegisterActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.equals("") || password == null) {
            Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (confirmPassword.equals("") || confirmPassword == null) {
            Toast.makeText(RegisterActivity.this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else if (!isPhoneNum) {
            Toast.makeText(RegisterActivity.this, "请输入有效的手机号码！", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 8 || password.length() > 20) {
            Toast.makeText(RegisterActivity.this, "请输入8-20位的密码！", Toast.LENGTH_SHORT).show();
            return;
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "密码和确认密码必须一致！", Toast.LENGTH_SHORT).show();
            return;
        } else if (userDao.checkPhone(phone) != null) {
            Toast.makeText(this, "该用户已注册！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            User user = new User(phone,phone, password);
            userDao.add(user);

            CacheUtils cacheUtils = new CacheUtils();
            cacheUtils.putString(this, DatabaseHelper.PHONE, phone);

            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initView() {

        tvTitle.setText("注册");
        ibMenu.setVisibility(View.GONE);
        ibFontsize.setVisibility(View.GONE);
        ibCollection.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

}
