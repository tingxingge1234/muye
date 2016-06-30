package cn.project.muye.adapter;


import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.project.muye.MuYeApplication;
import cn.project.muye.R;
import cn.project.muye.activity.MuYeMainActivity;
import cn.project.muye.bean.CategoryChildBean;
import cn.project.muye.bean.CategoryGroupBean;
import cn.project.muye.view.DisPlayUtils;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = CategoryAdapter.class.getName();
    MuYeMainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<CategoryChildBean> mChildList;
    boolean isMore;
    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
        notifyDataSetChanged();
    }

    public CategoryAdapter(Context mContext, ArrayList<CategoryGroupBean> mGroupList) {
        this.mContext = (MuYeMainActivity) mContext;
        this.mGroupList = mGroupList;
    }

    public void initContact(ArrayList<CategoryGroupBean> list) {
        this.mGroupList.clear();
        this.mGroupList.addAll(list);
        notifyDataSetChanged();
    }

    public void addContact(ArrayList<CategoryGroupBean> contactList) {
        this.mGroupList.addAll(contactList);
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG,"onCreateViewHolder-parent="+parent);
        Log.e(TAG,"onCreateViewHolder-viewType="+viewType);
        RecyclerView.ViewHolder holder = null;
        View layout = null;
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = inflater.inflate(R.layout.item_category, parent, false);
        holder = new CategoryViewHolder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        CategoryViewHolder mCategoryViewHolder = (CategoryViewHolder)holder;
        final CategoryGroupBean mCategoryBean = mGroupList.get(position);
        Log.e(TAG, "onBindViewHolder-mGroupListSize=" + mGroupList.size());
        Log.e(TAG, "onBindViewHolder-mCategoryBean=" + mCategoryBean);
        mCategoryViewHolder.tvCategoryName.setText(mCategoryBean.getName());
        final CategoryChildAdapter mChildAdapter;
        mChildList = new ArrayList<CategoryChildBean>();
        IntentFilter filter = new IntentFilter();
        filter.addAction("update_childBean");
        final ArrayList<CategoryChildBean> categoryChildList = MuYeApplication.getInstance().getCategoryChildList();
        Log.e(TAG, "onBindViewHolder-categoryChildList=" + categoryChildList);
        mCategoryViewHolder.ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisPlayUtils.initBackwithTitle(mContext,mCategoryBean.getName());
                CategoryGroupBean categoryGroupBean = mGroupList.get(position);
                int id = categoryGroupBean.getId();
                Log.e(TAG, "id=" + id);
                mContext.sendStickyBroadcast(new Intent("parentId").putExtra("id",id));
//                for (int i = 0; i <mGroupList.size() ; i++) {
//                    Log.e(TAG,"onBindViewHolder-categoryChildList.size()="+categoryChildList.size());
//                    CategoryChildBean categoryChildBean = categoryChildList.get(position);
//                    if (mGroupList.get(i).getId() == categoryChildList.get(position).getParentId()) {
//                        DisPlayUtils.initBackwithTitle(mContext,mCategoryBean.getName());
//                        mChildList.add(categoryChildBean);
//                        CategoryChildAdapter mChildAdapter;
//                        mChildAdapter = new CategoryChildAdapter(mContext,mChildList);
//                        mChildAdapter.initContact(mChildList);
//                        mChildAdapter.notifyDataSetChanged();
//                    }
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroupList==null?0:mGroupList.size();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        LinearLayout ll_category;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tv_item_category);
            ll_category = (LinearLayout) itemView.findViewById(R.id.ll_category);
        }
    }


}
