package com.example.libai.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.libai.R;
import com.example.libai.databases.DatabaseHelper;
import com.example.libai.databases.UserDao;
import com.example.libai.model.Comment;
import com.example.libai.model.User;
import com.example.libai.utils.CacheUtils;
import com.example.libai.utils.LogUtil;
import com.example.libai.utils.TimeUtil;

import java.io.File;
import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private final ArrayList<Comment> list;
    private Context context;
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.fragment_item_list_dialog_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        UserDao userDao = new UserDao(context);
        User user = userDao.checkPhone(list.get(i).getPhone());

        viewHolder.tvName.setText(user.getName());
        viewHolder.tvContent.setText(list.get(i).getContent());
        LogUtil.e("content"+list.get(i).getContent());
        viewHolder.tvTime.setText(TimeUtil.CommonFormatTime(Long.parseLong(list.get(i).getTime())));
//        Glide.with(context).load(user.getPath()).error(R.drawable.portrait).into(viewHolder.ivPortrait);
        if (user.getPath().equals("") || user.getPath() == null){
            viewHolder.ivPortrait.setImageResource(R.drawable.portrait);
        }else {
            viewHolder.ivPortrait.setImageURI(Uri.fromFile(new File(user.getPath())));
        }

        CacheUtils cacheUtils = new CacheUtils();
        String phone = cacheUtils.getString(context, DatabaseHelper.PHONE);
        if (phone.equals(list.get(i).getPhone())){
            viewHolder.ivDelete.setVisibility(View.VISIBLE);
            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null){
                        onItemClickListener.onItemClick(v,i);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        ImageView ivDelete;
        ImageView ivPortrait;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivPortrait = itemView.findViewById(R.id.iv_portrait);
        }
    }
}
