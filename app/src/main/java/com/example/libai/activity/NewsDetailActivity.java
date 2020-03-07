package com.example.libai.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.libai.R;
import com.example.libai.databases.CollectionDao;
import com.example.libai.databases.CommentDao;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.fragment.ListBottomSheetDialogFragment;
import com.example.libai.model.Collection;
import com.example.libai.model.Comment;
import com.example.libai.utils.CacheUtils;

public class NewsDetailActivity extends AppCompatActivity {

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
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_send)
    Button btnSend;
    private String url;
    private String title;
    private WebSettings webSettings;
    private String uniquekey;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        initView();
        getData();
    }

    private void getData() {
        url = getIntent().getStringExtra(DatabaseHelper.URL);
        title = getIntent().getStringExtra(DatabaseHelper.TITLE);
        uniquekey = getIntent().getStringExtra(DatabaseHelper.UNIQUEKEY);

        webSettings = webview.getSettings();
        //设置支持JavaScript
        webSettings.setJavaScriptEnabled(true);
        //设置文字大小
        webSettings.setTextZoom(100);
        //不让从当前网页跳转到系统的浏览器中
        webview.setWebViewClient(new WebViewClient() {
            //当加载页面完成的时候回调
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbLoading.setVisibility(View.GONE);
            }
        });
        webview.loadUrl(url);

        //弹出评论页面
        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListBottomSheetDialogFragment dialogFragment = new ListBottomSheetDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString(DatabaseHelper.UNIQUEKEY,uniquekey);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(),"dialog");
            }
        });

        //发送评论内容
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etContent.getText().toString();
                CacheUtils cacheUtils = new CacheUtils();
                String phone = cacheUtils.getString(NewsDetailActivity.this,DatabaseHelper.PHONE);
                if (phone.equals("")){
                    Toast.makeText(NewsDetailActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewsDetailActivity.this,LoginActivity.class));
                    return;
                }else if (content.equals("")){
                    Toast.makeText(NewsDetailActivity.this, "发送内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String time = String.valueOf(System.currentTimeMillis());
                Comment comment = new Comment(phone,content,time,uniquekey);
                CommentDao commentDao = new CommentDao(NewsDetailActivity.this);
                if (commentDao.add(comment)){
                    etContent.setText("");
                    Toast.makeText(NewsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(NewsDetailActivity.this, "评论失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initView() {
        tvTitle.setVisibility(View.GONE);
        ibMenu.setVisibility(View.GONE);
        ibBack.setVisibility(View.VISIBLE);
        ibFontsize.setVisibility(View.VISIBLE);
        ibCollection.setVisibility(View.VISIBLE);

        CacheUtils cacheUtils = new CacheUtils();
        String phone = cacheUtils.getString(this, DatabaseHelper.PHONE);
        String uniquekey= getIntent().getStringExtra(DatabaseHelper.UNIQUEKEY);
        CollectionDao collectionDao = new CollectionDao(this);
        Collection collection = new Collection(phone, uniquekey);
        if (!phone.equals("")) {
            if (collectionDao.isCheck(collection)) {
                ibCollection.setImageResource(R.drawable.collection_check);
            } else {
                ibCollection.setImageResource(R.drawable.collection);
            }
        }

    }

    @OnClick({R.id.ib_back, R.id.ib_fontsize, R.id.ib_collection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_fontsize:
//                Toast.makeText(this, "设置文字大小", Toast.LENGTH_SHORT).show();
                showChangeTextSizeDialog();
                break;
            case R.id.ib_collection:
                CacheUtils cacheUtils = new CacheUtils();
                String phone = cacheUtils.getString(this, DatabaseHelper.PHONE);
                if (phone.equals("")) {
                    Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                CollectionDao collectionDao = new CollectionDao(this);
                Collection collection = new Collection(phone,uniquekey, title, url);
                if (collectionDao.isCheck(collection)) {
                    collectionDao.del(collection);
//                    Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
                    ibCollection.setImageResource(R.drawable.collection);
                } else {
                    collectionDao.add(collection);
//                    Toast.makeText(this, "添加收藏", Toast.LENGTH_SHORT).show();
                    ibCollection.setImageResource(R.drawable.collection_check);
                }

                break;
        }
    }

    private int tempSize = 2;
    private int realSize = tempSize;

    private void showChangeTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置文字大小");
        String[] items = {"超大字体", "大字体", "正常字体", "小字体", "超小字体"};
        builder.setSingleChoiceItems(items, realSize, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempSize = which;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                realSize = tempSize;
                changeTextSize(realSize);
            }
        });
        builder.show();
    }

    private void changeTextSize(int realSize) {
        switch (realSize) {
            case 0://超大字体
                webSettings.setTextZoom(200);
                break;
            case 1://大字体
                webSettings.setTextZoom(150);
                break;
            case 2://正常字体
                webSettings.setTextZoom(100);
                break;
            case 3://小字体
                webSettings.setTextZoom(75);
                break;
            case 4://超小字体
                webSettings.setTextZoom(50);
                break;
        }
    }

}
