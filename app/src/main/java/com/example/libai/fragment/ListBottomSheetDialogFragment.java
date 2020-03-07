package com.example.libai.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;
import com.example.libai.R;
import com.example.libai.activity.LoginActivity;
import com.example.libai.adapter.CommentAdapter;
import com.example.libai.databases.CommentDao;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.model.Comment;
import com.example.libai.utils.CacheUtils;

import java.util.ArrayList;

public class ListBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private ImageView ivBack;
    private RecyclerView recyclerView;
    private String uniquekey;
    private CommentDao commentDao;
    private TextView tvContent;
    private TextView textView;
    private TextView tvCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //给dialog设置主题为透明背景 不然会有默认的白色背景
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new BottomSheetDialog(this.getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        //获取dialog对象
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        //把windows的默认背景颜色去掉，不然圆角显示不见
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackground(new ColorDrawable(Color.TRANSPARENT));
        //获取dialog的根布局
        FrameLayout bottomSheet = dialog.getDelegate().findViewById(R.id.design_bottom_sheet);
        if (bottomSheet != null){
            //获取根布局的LayoutParams对象
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
            layoutParams.height = getPeekHeight();
            //修改弹窗的最大高度，不予许上滑（默认可以上滑）
            bottomSheet.setLayoutParams(layoutParams);
            final BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(bottomSheet);
            //peekHeight即弹窗的最大高度
            behavior.setPeekHeight(getPeekHeight());
            //初始为展开状态
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //关闭弹窗
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            });
        }
    }

    /**
     * 弹窗高度
     * @return
     */
    private int getPeekHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 在这里将view的高度设置为精确高度，即可屏蔽向上滑动不占全屏的手势。
        //如果不设置高度的话 会默认向上滑动时dialog覆盖全屏
        View view = inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getScreenHeight(getActivity()) / 2));
        ivBack = view.findViewById(R.id.iv_back);
        recyclerView = view.findViewById(R.id.recycler_view);
        tvContent = view.findViewById(R.id.tv_content);
        textView = view.findViewById(R.id.text_view);
        tvCount = view.findViewById(R.id.tv_count);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        updateData();
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出评论输入框
                InputDialog inputDialog = new InputDialog(getActivity());
                Window window = inputDialog.getWindow();
                WindowManager.LayoutParams params = window.getAttributes();
                //设置软键盘通常是可见的
                window.setSoftInputMode(params.SOFT_INPUT_STATE_VISIBLE);
                inputDialog.show();
            }
        });
    }

    /**
     * 设置RecyclerView数据
     */
    private void updateData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentDao = new CommentDao(getContext());
        uniquekey = getArguments().getString(DatabaseHelper.UNIQUEKEY);
        final ArrayList<Comment> list = commentDao.findUniquekey(uniquekey);
        CommentAdapter adapter = new CommentAdapter(getContext(), list);

        tvCount.setText(list.size()+"条评论");

        if (list.isEmpty()){
            textView.setVisibility(View.VISIBLE);
        }else{
            textView.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                CommentDao commentDao = new CommentDao(getContext());
                int id = list.get(position).getId();
                if(commentDao.del(id)){
                    updateData();
                    Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class InputDialog extends Dialog {
        private String content;

        public InputDialog(@NonNull Context context) {
            super(context);
//        super(context, R.style.CustomDialog);
            init(context);
        }

        public Context mContext;
        public View mRootView;
        private EditText etContent;
        private Button btnSend;

        public void init(Context context) {
            mContext = context;
            mRootView = LayoutInflater.from(context).inflate(R.layout.dialog_input, null);
            setContentView(mRootView);
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
            window.setGravity(Gravity.BOTTOM);

            etContent = mRootView.findViewById(R.id.et_content);
            btnSend = mRootView.findViewById(R.id.btn_send);

            //发送评论内容
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    content = etContent.getText().toString();
                    CacheUtils cacheUtils = new CacheUtils();
                    String phone = cacheUtils.getString(v.getContext(),DatabaseHelper.PHONE);
                    if (phone.equals("")){
                        Toast.makeText(v.getContext(), "请先登录！", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(v.getContext(), LoginActivity.class));
                        return;
                    }else if (content.equals("")){
                        Toast.makeText(v.getContext(), "发送内容不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String time = String.valueOf(System.currentTimeMillis());
                    Comment comment = new Comment(phone,content,time,uniquekey);
                    CommentDao commentDao = new CommentDao(v.getContext());
                    if (commentDao.add(comment)){
                        etContent.setText("");
                        updateData();
                        Toast.makeText(v.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(v.getContext(), "评论失败！", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

}
