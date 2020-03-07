package com.example.libai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.R;
import com.example.libai.adapter.MyCollectionAdapter;
import com.example.libai.databases.CollectionDao;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.model.Collection;
import com.example.libai.model.TabDetailPagerBean;
import com.example.libai.utils.CacheUtils;

import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private ArrayList<TabDetailPagerBean.ResultBean.DataBean> data;
    private String phone;
    private ArrayList<Collection> collections;
    private TabDetailPagerBean.ResultBean.DataBean dataBean;
    private CacheUtils cacheUtils;
    private CollectionDao collectionDao;
    private MyCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);

        tvTitle.setText("我的收藏");
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //取出缓存的手机号
        cacheUtils = new CacheUtils();
        phone = cacheUtils.getString(this, DatabaseHelper.PHONE);
        collectionDao = new CollectionDao(this);

        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //设置布局
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取数据
        collections = collectionDao.findPhone(phone);

        //添加数据
        data = new ArrayList<>();
        for (int i = 0; i < collections.size(); i++) {
            String title = collections.get(i).getTitle();
            String url = collections.get(i).getUrl();
            String uniquekey = collections.get(i).getUniquekey();
            dataBean = new TabDetailPagerBean.ResultBean.DataBean();
            dataBean.setTitle(title);
            dataBean.setUrl(url);
            dataBean.setUniquekey(uniquekey);
            data.add(dataBean);
        }

        if (data.size() > 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }

        //设置适配器
        adapter = new MyCollectionAdapter(data);
        recyclerView.setAdapter(adapter);

        //点击标题跳转到网页
        adapter.setOnItemClickListener(new MyCollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //跳转到新闻浏览页面
                Intent intent = new Intent(CollectionActivity.this, NewsDetailActivity.class);
                intent.putExtra(DatabaseHelper.URL, data.get(position).getUrl());
                intent.putExtra(DatabaseHelper.TITLE, data.get(position).getTitle());
                intent.putExtra(DatabaseHelper.UNIQUEKEY, data.get(position).getUniquekey());
                startActivity(intent);
            }
        });
    }
}
