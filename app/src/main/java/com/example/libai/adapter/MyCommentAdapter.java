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
import com.example.libai.databases.UserDao;
import com.example.libai.model.Comment;
import com.example.libai.model.User;

import java.io.File;
import java.util.ArrayList;

public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.ViewHolder> {

    private final ArrayList<Comment> list;
    private final Context context;
    private OnItemClickListener onItemClickListener = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyCommentAdapter(Context context, ArrayList<Comment> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.tvContent.setText(list.get(i).getContent());

        UserDao userDao = new UserDao(context);
        User user = userDao.checkPhone(list.get(i).getPhone());
        viewHolder.tvName.setText(user.getName());

        viewHolder.ivDelete.setVisibility(View.VISIBLE);
        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, i);
                }
            }
        });

//        Glide.with(context).load(user.getPath()).error(R.drawable.portrait).into(viewHolder.ivPortrait);
        if (user.getPath().equals("") || user.getPath() == null){
            viewHolder.ivPortrait.setImageResource(R.drawable.portrait);
        }else {
            viewHolder.ivPortrait.setImageURI(Uri.fromFile(new File(user.getPath())));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContent;
        ImageView ivDelete,ivPortrait;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivDelete = itemView.findViewById(R.id.iv_delete);
            ivPortrait = itemView.findViewById(R.id.iv_portrait);
        }
    }
}
