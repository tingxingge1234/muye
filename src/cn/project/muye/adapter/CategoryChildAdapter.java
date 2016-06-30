package cn.project.muye.adapter;


import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import cn.project.muye.D;
import cn.project.muye.I;
import cn.project.muye.R;
import cn.project.muye.activity.CategoryChildActivity;
import cn.project.muye.activity.GoodsDetailsActivity;
import cn.project.muye.bean.CategoryChildBean;
import cn.project.muye.bean.CategoryGroupBean;
import cn.project.muye.utils.ImageUtils;
import cn.project.muye.view.FooterViewHolder;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class CategoryChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = CategoryChildAdapter.class.getName();
    Context mContext;
    ArrayList<CategoryChildBean> mChildList;
    ViewGroup parent;
    String footerText;

    boolean isMore;
    static final int TYPE_ITEM = 0;
    static final int TYPE_FOOTER=1;

    FooterViewHolder mFooterViewHolder;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CategoryChildAdapter(Context mContext, ArrayList<CategoryChildBean> mChildList) {
        this.mContext = mContext;
        this.mChildList = mChildList;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
        notifyDataSetChanged();
    }
    public void initContact(ArrayList<CategoryChildBean> mChildList) {
        Log.e(TAG, "initContact-mChildList=" + mChildList);
        if (mChildList != null) {
            this.mChildList.clear();
        }
        this.mChildList.addAll(mChildList);
        notifyDataSetChanged();
    }

    public void addContact(ArrayList<CategoryChildBean> mChildList) {
        this.mChildList.addAll(mChildList);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View layout = null;
        this.parent = parent;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case TYPE_ITEM:
                layout = inflater.inflate(R.layout.item_category_child, parent, false);
                holder = new CategoryChildViewHolder(layout);
                break;
            case TYPE_FOOTER:
                layout = inflater.inflate(R.layout.item_footer, parent, false);
                holder = new FooterViewHolder(layout);
                break;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder-mChildListSize=" + mChildList.size());
        if (position == getItemCount() - 1) {
            mFooterViewHolder = (FooterViewHolder)holder;
            mFooterViewHolder.mtvFootertext.setText(footerText);
            return;
        }
        final int parentId = mChildList.get(position).getParentId();
        CategoryChildViewHolder mChildViewHolder = (CategoryChildViewHolder)holder;
        CategoryChildBean mChildBean = mChildList.get(position);
        Log.e(TAG, "onBindViewHolder-mChildBean=" + mChildBean);
        Log.e(TAG, "onBindViewHolder-position=" + position);
        Log.e(TAG, "onBindViewHolder-mChildListSize=" + mChildList.size());
        mChildViewHolder.tvCategoryChildName.setText(mChildBean.getName());
        String imgUrl = mChildList.get(position).getImageUrl();
        String url = I.DOWNLOAD_DOWNLOAD_CATEGORY_CHILD_IMAGE_URL + imgUrl;
        ImageUtils.setThumb(url,mChildViewHolder.nivChild);
        mChildViewHolder.ll_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, CategoryChildActivity.class)
                        .putExtra(D.CategoryChild.PARENT_ID,mChildList.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChildList==null?1:mChildList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    class CategoryChildViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryChildName;
        NetworkImageView nivChild;
        LinearLayout ll_child;
        public CategoryChildViewHolder(View itemView) {
            super(itemView);
            tvCategoryChildName = (TextView) itemView.findViewById(R.id.tv_item_category_child);
            nivChild = (NetworkImageView) itemView.findViewById(R.id.nivChild);
            ll_child = (LinearLayout) itemView.findViewById(R.id.ll_category_child);
        }
    }
}
