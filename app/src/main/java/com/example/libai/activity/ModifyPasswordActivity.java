package com.example.libai.activity;

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

public class ModifyPasswordActivity extends AppCompatActivity {

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
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_new_password)
    EditText etNewPassword;
    @BindView(R.id.et_confirm_new_password)
    EditText etConfirmNewPassword;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_reset)
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        ButterKnife.bind(this);

        initView();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmNewPassword = etConfirmNewPassword.getText().toString();

                CacheUtils cacheUtils = new CacheUtils();
                String phone = cacheUtils.getString(ModifyPasswordActivity.this, DatabaseHelper.PHONE);
                UserDao userDao = new UserDao(ModifyPasswordActivity.this);

                if (password.equals("")) {
                    Toast.makeText(ModifyPasswordActivity.this, "旧密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newPassword.equals("")) {
                    Toast.makeText(ModifyPasswordActivity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (confirmNewPassword.equals("")) {
                    Toast.makeText(ModifyPasswordActivity.this, "确认新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(ModifyPasswordActivity.this, "新密码和确认新密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!userDao.checkPhone(phone).getPassword().equals(password)) {
                    Toast.makeText(ModifyPasswordActivity.this, "原密码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    User user = new User(phone, newPassword);
                    if (userDao.updatePwd(user)) {
                        CacheUtils.delString(ModifyPasswordActivity.this, DatabaseHelper.PHONE);
                        Toast.makeText(ModifyPasswordActivity.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ModifyPasswordActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                }

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
                etNewPassword.setText("");
                etConfirmNewPassword.setText("");
            }
        });

    }

    private void initView() {

        tvTitle.setText("修改密码");
        ibMenu.setVisibility(View.GONE);
        ibFontsize.setVisibility(View.GONE);
        ibCollection.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);

    }

}
