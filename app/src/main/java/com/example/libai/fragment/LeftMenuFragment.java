package com.example.libai.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.*;
import com.example.libai.R;
import com.example.libai.activity.*;
import com.example.libai.adapter.MyLeftMenuAdapter;
import com.example.libai.base.BaseFragment;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.databases.UserDao;
import com.example.libai.model.User;
import com.example.libai.utils.CacheUtils;

import java.io.File;


/**
 * 左侧菜单的fragment
 */
public class LeftMenuFragment extends BaseFragment {

    private ImageView ivPortrait;
    private TextView tvName;
    private ListView listView;
    private Button btnLogin;
    private Button btnExit;
    private String[] data;

    @Override
    public View initView() {
//        LogUtil.e("左侧菜单视图被初始化了");
        View view = View.inflate(context, R.layout.activity_leftmenu, null);
        ivPortrait = view.findViewById(R.id.iv_portrait);
        tvName = view.findViewById(R.id.tv_name);
        listView = view.findViewById(R.id.list_view);
        btnLogin = view.findViewById(R.id.btn_login);
        btnExit = view.findViewById(R.id.btn_exit);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
//        LogUtil.e("左侧菜单数据被初始化了");

        data = new String[]{"我的收藏","我的评论","修改密码","个人信息"};
        listView.setAdapter(new MyLeftMenuAdapter(context,data));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CacheUtils cacheUtils = new CacheUtils();
                String phone = cacheUtils.getString(context, DatabaseHelper.PHONE);
                if (phone.equals("")){
                    Toast.makeText(context, "请登录账号！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context,LoginActivity.class));
                    return;
                }
                switch (position){
                    case 0:
                        startActivity(new Intent(context, CollectionActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(context, CommentActivity .class));
                        break;
                    case 2:
                        startActivity(new Intent(context, ModifyPasswordActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(context, InformationActivity.class));
                        break;
                }
            }
        });

        //跳转到登录界面
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CacheUtils.delString(context, DatabaseHelper.PHONE);
                Toast.makeText(context, "已安全退出", Toast.LENGTH_SHORT).show();
                onResume();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();


        ivPortrait.setImageResource(R.drawable.portrait);

        CacheUtils cacheUtils = new CacheUtils();
        UserDao userDao = new UserDao(context);
        String phone = cacheUtils.getString(context,DatabaseHelper.PHONE);
        User user = userDao.checkPhone(phone);

        if (phone.equals("")){
            tvName.setText("手机号");
            btnLogin.setVisibility(View.VISIBLE);
            btnExit.setVisibility(View.GONE);
        }else{
            tvName.setText(userDao.checkPhone(phone).getName());
            btnLogin.setVisibility(View.GONE);
            btnExit.setVisibility(View.VISIBLE);
            if (user.getPath().equals("") || user.getPath() == null) {
                ivPortrait.setImageResource(R.drawable.portrait);
            }else{
                ivPortrait.setImageURI(Uri.fromFile(new File(user.getPath())));
            }
        }
    }
}
