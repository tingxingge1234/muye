package cn.project.muye.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.project.muye.R;


/**
 * Created by Administrator on 2016/6/16 0016.
 */
public class CategoryTitleViewHolder extends RecyclerView.ViewHolder {
    public TextView mtvCategoryChildTitleName;
    public TextView mtvAllGoods;

    public CategoryTitleViewHolder(View itemView) {
        super(itemView);
        mtvCategoryChildTitleName = (TextView) itemView.findViewById(R.id.tv_child_name);
        mtvAllGoods = (TextView) itemView.findViewById(R.id.tv_all_goods);
    }
}
