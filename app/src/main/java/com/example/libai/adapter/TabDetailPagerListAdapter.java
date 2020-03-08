package com.example.libai.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.libai.R;
import com.example.libai.model.TabDetailPagerBean;
import com.example.libai.utils.CacheUtils;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import static com.example.libai.pager.tabdetailpager.TabDetailPager.READ_ARRAY_ID;

public class TabDetailPagerListAdapter extends BaseAdapter {

    /**
     * 图片的数量
     */
    private int IMAGE_01 = 0;
    private int IMAGE_02 = 1;
    private int IMAGE_03 = 2;

    private Context context;
    private ImageOptions imageOptions;
    private List<TabDetailPagerBean.ResultBean.DataBean> news;

    public TabDetailPagerListAdapter(Context context, List<TabDetailPagerBean.ResultBean.DataBean> news) {
        this.context = context;
        this.news = news;
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(90), DensityUtil.dip2px(90))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
//        news.get(position).getThumbnail_pic_s();
//        if (news.get(position).getThumbnail_pic_s() != null &&
//                news.get(position).getThumbnail_pic_s02() != null &&
//                news.get(position).getThumbnail_pic_s03() != null) {
//            return IMAGE_03;
//        } else if (news.get(position).getThumbnail_pic_s() != null &&
//                news.get(position).getThumbnail_pic_s02() != null) {
//            return IMAGE_02;
//        }
        return IMAGE_01;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //根据位置得到数据
        TabDetailPagerBean.ResultBean.DataBean dataBean = news.get(position);

        String idArray = CacheUtils.getString(context,READ_ARRAY_ID);

//        LogUtil.e(idArray.contains(dataBean.getUniquekey())+"");

        Image01_ViewHolder holder01 = null;
        Image02_ViewHolder holder02 = null;
        Image03_ViewHolder holder03 = null;

        if (getItemViewType(position) == IMAGE_01) {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tabdetail_pager01, null);
                holder01 = new Image01_ViewHolder();

                //查找控件
                holder01.tv_title = convertView.findViewById(R.id.tv_title);
                holder01.tv_author = convertView.findViewById(R.id.tv_author);
                holder01.iv_image01 = convertView.findViewById(R.id.iv_image01);
                convertView.setTag(holder01);
            } else {
                holder01 = (Image01_ViewHolder) convertView.getTag();
            }

            //获取数据重新赋值
            holder01.tv_title.setText(dataBean.getTitle());
            holder01.tv_author.setText(dataBean.getAuthor());

            //请求图片
//            String imageUrl01 = dataBean.getThumbnail_pic_s();
//            x.image().bind(holder01.iv_image01, imageUrl01, imageOptions);

            if (idArray.contains(dataBean.getUniquekey())){
                //设置灰色
                holder01.tv_title.setTextColor(Color.GRAY);
            }else{
                //设置黑色
                holder01.tv_title.setTextColor(Color.BLACK);
            }

        }else if (getItemViewType(position) == IMAGE_02){
            if (convertView == null){
                convertView = View.inflate(context, R.layout.item_tabdetail_pager02,null);
                holder02 =new Image02_ViewHolder();

                //查找控件
                holder02.tv_title =  convertView.findViewById(R.id.tv_title);
                holder02.iv_image01 = convertView.findViewById(R.id.iv_image01);
                holder02.iv_image02 = convertView.findViewById(R.id.iv_image02);
                convertView.setTag(holder02);
            }else {
                holder02 = (Image02_ViewHolder) convertView.getTag();
            }

            //获取数据重新赋值
            holder02.tv_title.setText(dataBean.getTitle());

            //请求图片
//            String imageUrl01 = dataBean.getThumbnail_pic_s();
//            String imageUrl02 = dataBean.getThumbnail_pic_s02();
//            x.image().bind(holder02.iv_image01, imageUrl01, imageOptions);
//            x.image().bind(holder02.iv_image02, imageUrl02, imageOptions);

            if (idArray.contains(dataBean.getUniquekey())){
                //设置灰色
                holder02.tv_title.setTextColor(Color.GRAY);
            }else{
                //设置黑色
                holder02.tv_title.setTextColor(Color.BLACK);
            }

        } else {
            if (convertView == null){
                convertView = View.inflate(context, R.layout.item_tabdetail_pager03,null);
                holder03 =new Image03_ViewHolder();

                //查找控件
                holder03.tv_title = convertView.findViewById(R.id.tv_title);
                holder03.iv_image01 = convertView.findViewById(R.id.iv_image01);
                holder03.iv_image02 = convertView.findViewById(R.id.iv_image02);
                holder03.iv_image03 = convertView.findViewById(R.id.iv_image03);
                convertView.setTag(holder03);
            }else {
                holder03 = (Image03_ViewHolder) convertView.getTag();
            }

            //获取数据重新赋值
            holder03.tv_title.setText(dataBean.getTitle());

            //请求图片
//            String imageUrl01 = dataBean.getThumbnail_pic_s();
//            String imageUrl02 = dataBean.getThumbnail_pic_s02();
//            String imageUrl03 = dataBean.getThumbnail_pic_s03();
//            x.image().bind(holder03.iv_image01, imageUrl01, imageOptions);
//            x.image().bind(holder03.iv_image02, imageUrl02, imageOptions);
//            x.image().bind(holder03.iv_image03, imageUrl03, imageOptions);

            if (idArray.contains(dataBean.getUniquekey())){
                //设置灰色
                holder03.tv_title.setTextColor(Color.GRAY);
            }else{
                //设置黑色
                holder03.tv_title.setTextColor(Color.BLACK);
            }

        }

        return convertView;

    }

    private class Image01_ViewHolder {
        TextView tv_title, tv_author;
        ImageView iv_image01;
    }

    private class Image02_ViewHolder {
        TextView tv_title;
        ImageView iv_image01, iv_image02;
    }

    private class Image03_ViewHolder {
        TextView tv_title;
        ImageView iv_image01, iv_image02, iv_image03;
    }

}
