package com.example.libai.pager.tabdetailpager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.libai.R;
import com.example.libai.activity.NewsDetailActivity;
import com.example.libai.adapter.TabDetailPagerListAdapter;
import com.example.libai.base.MenuDetailBasePager;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.model.TabDetailPagerBean;
import com.example.libai.utils.CacheUtils;
import com.example.libai.utils.Constants;
import com.example.libai.utils.LogUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class TabDetailPager extends MenuDetailBasePager {

    public static final String READ_ARRAY_ID = "read_array_id";
    private String title;
    private String url;
    private String category;
    private ListView listView;
    private TabDetailPagerListAdapter adapter;
    private int currentPage = 0;
    private int pageSize = 10;
    private boolean isPullUp = false;
    private int currentPosition = 0;

    /**
     * 新闻列表数据集合
     */
    private List<TabDetailPagerBean.ResultBean.DataBean> pageCache;
    private PullToRefreshListView mPullRefreshListView;

    public TabDetailPager(Context context, String title) {
        super(context);
        this.title = title;
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.tab_detail_pager, null);

        mPullRefreshListView = view.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        listView = mPullRefreshListView.getRefreshableView();

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isPullUp = false;
                currentPage = 0;
                getDataPullRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                isPullUp = true;
                currentPage += 1;
                url = Constants.getUrl(category, currentPage, pageSize);
                getDataPullRefresh();
            }
        });

        //设置ListView的item的点击监听
        listView.setOnItemClickListener(new MyOnItemClickListener());

        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int realPosition = position-1;
            TabDetailPagerBean.ResultBean.DataBean newsData = pageCache.get(realPosition);
//            Toast.makeText(context, "uniquekey=="+newsData.getUniquekey()+newsData.getTitle(), Toast.LENGTH_SHORT).show();

//            LogUtil.e(newsData.getTitle()+"的新闻地址："+newsData.getUrl());

            //1.取出保存的uniquekey集合
            String idArray = CacheUtils.getString(context, READ_ARRAY_ID);
            //2.判断是否存在，如果不存在，才保存，并且刷新适配器
            if (!idArray.contains(newsData.getUniquekey())){

                CacheUtils.putString(context,READ_ARRAY_ID,idArray + newsData.getUniquekey() + ",");

                //刷新适配器
                adapter.notifyDataSetChanged();//getCount-->getView

            }

            //跳转到新闻浏览页面
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(DatabaseHelper.URL, newsData.getForceUrl());
            intent.putExtra(DatabaseHelper.TITLE, newsData.getTitle());
            intent.putExtra(DatabaseHelper.UNIQUEKEY,newsData.getUniquekey());
            context.startActivity(intent);

        }
    }

    @Override
    public void initData() {
        super.initData();
        if (title.equals("最新")) {
            category = "all";
        } else if (title.equals("科技")) {
            category = "tech";
        } else if (title.equals("财经")) {
            category = "finance";
        } else if (title.equals("房产")) {
            category = "house";
        } else if (title.equals("汽车")) {
            category = "car";
        } else if (title.equals("文化")) {
            category = "culture";
        }
        currentPage = 0;
        url = Constants.getUrl(category, currentPage, pageSize);
        //把之前缓存的数据取出
        String saveJson = CacheUtils.getString(context, url);
//        LogUtil.e("json数据+++"+saveJson);
        if (!TextUtils.isEmpty(saveJson)) {
            //解析和处理显示数据
            processData(saveJson);
        }
//        LogUtil.e(title + "的联网地址==" + url);
        //联网请求数据
        getDataPullRefresh();
    }

    /**
     * 使用xUtils3联网请求数据
     */
    private void getDataPullRefresh() {
        RequestParams params = new RequestParams(url);
        params.setConnectTimeout(4000);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                LogUtil.e(title + "-页面数据请求成功==" + result);
                if (result != null) {
                    //缓存数据
                    CacheUtils.putString(context, url, result);
                    //解析和处理显示数据
                    processData(result);
                }
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(title + "-页面数据请求失败==" + ex.getMessage());
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e(title + "-页面数据请求onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e(title + "页面数据请求onFinished");
            }
        });
    }

    /**
     * 解析json数据和显示数据
     *
     * @param json
     */
    private void processData(String json) {
        TabDetailPagerBean bean = parsedJson(json);

        if (bean.getResult() == null) return;
//        LogUtil.e(title + "解析成功==" + bean.getResult().getData().get(0).getTitle());

        //准备ListView对应的集合数据
        List<TabDetailPagerBean.ResultBean.DataBean> news;
        news = bean.getResult().getData();
        if (currentPage == 0) {
            pageCache = news;
        } else {
            if (isPullUp) {
                currentPosition = pageCache.size();
                pageCache.removeAll(news);
                pageCache.addAll(news);
            }
        }
        //设置ListView的适配器
        adapter = new TabDetailPagerListAdapter(context, pageCache);
        listView.setAdapter(adapter);

        if (currentPage == 0) {
            listView.setSelection(0);
        } else {
            listView.setSelection(currentPosition + 1);
        }
    }

    private TabDetailPagerBean parsedJson(String json) {
        return new Gson().fromJson(json, TabDetailPagerBean.class);
    }
}
