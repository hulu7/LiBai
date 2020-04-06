package com.example.libai.activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.R;
import com.example.libai.adapter.MyCommentAdapter;
import com.example.libai.databases.CommentDao;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.model.Comment;
import com.example.libai.utils.CacheUtils;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ib_back)
    ImageButton ibBack;
    private String phone;
    private ArrayList<Comment> list;
    private MyCommentAdapter adapter;
    private CommentDao commentDao;
    private CacheUtils cacheUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);

        tvTitle.setText("我的评论");
        ibBack.setVisibility(View.VISIBLE);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commentDao = new CommentDao(this);
        cacheUtils = new CacheUtils();
        phone = cacheUtils.getString(this, DatabaseHelper.PHONE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        list = commentDao.findPhone(phone);

        if (list.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        adapter = new MyCommentAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyCommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CommentDao commentDao = new CommentDao(CommentActivity.this);
                int id = list.get(position).getId();
                if (commentDao.del(id)) {
                    onResume();
                    Toast.makeText(CommentActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
