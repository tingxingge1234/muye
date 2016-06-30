package cn.project.muye.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.midi.MidiReceiver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.project.muye.D;
import cn.project.muye.I;
import cn.project.muye.R;
import cn.project.muye.activity.LoginActivity;
import cn.project.muye.activity.MuYeMainActivity;
import cn.project.muye.adapter.CategoryAdapter;
import cn.project.muye.adapter.CategoryChildAdapter;
import cn.project.muye.bean.CategoryChildBean;
import cn.project.muye.bean.CategoryGroupBean;
import cn.project.muye.data.ApiParams;
import cn.project.muye.data.GsonRequest;
import cn.project.muye.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    public static final String TAG = CategoryFragment.class.getName();
    MuYeMainActivity mContext;
    ArrayList<CategoryGroupBean> mGroupList;
    ArrayList<CategoryChildBean> mChildList;
    String path;
    TextView tvLogin;
    RecyclerView rlCategory,rlCategoryChild;
    TextView tvChildName;
    TextView tvAllGoods;
    TextView tvRefresh;
    SwipeRefreshLayout srl;
    CategoryAdapter mGroupAdapter;
    CategoryChildAdapter mChildAdapter;
    GridLayoutManager mGridLayoutManager;
    LinearLayoutManager mLinearLayoutManager;
    String childPath;
    int parentId;
    int pId;

    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_category_test, container, false);
        mContext = (MuYeMainActivity) getActivity();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        onLoginClickListener();
        Update_parentId();
    }

    private void onLoginClickListener() {
//        tvLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext, LoginActivity.class));
//            }
//        });
    }


    private void initData() {
        getPath();
        mContext.executeRequest(new GsonRequest<CategoryGroupBean[]>(path, CategoryGroupBean[].class,
                responseDownloadCategoryListener(), mContext.errorListener()));
        getChildPath();
        mContext.executeRequest(new GsonRequest<CategoryChildBean[]>(childPath, CategoryChildBean[].class,
                responseDownloadCategoryChildListener(), mContext.errorListener()));
    }

    private Response.Listener<CategoryGroupBean[]> responseDownloadCategoryListener() {
        return new Response.Listener<CategoryGroupBean[]>() {
            @Override
            public void onResponse(CategoryGroupBean[] categoryGroupBeen) {
                if (categoryGroupBeen != null) {
                    mGroupList = Utils.array2List(categoryGroupBeen);
//                    Log.e(TAG, "mGroupList:" + mGroupList);
                    mGroupAdapter.initContact(mGroupList);
//                    for (int i = 0; i < mGroupList.size(); i++) {
//                        Log.e(TAG, "mGroupListSize=" + mGroupList.size());
//                        parentId = mGroupList.get(i).getId();
//                        String url = null;
//                        Log.e(TAG, "pid=" + pId);
//                        if (pId != 0) {
//                            pId = mContext.getIntent().getIntExtra("parentId", 0);
//                        }
//                        Log.e(TAG, "pid=" + pId);
//                        try {
//                            url = new ApiParams()
//                                    .with(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
//                                    .with(I.PAGE_ID,I.PAGE_ID_DEFAULT+"")
//                                    .with(D.CategoryChild.PARENT_ID,pId+"")
//                                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN);
//                            Log.e(TAG, "ChildUrl=" + url);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Log.e(TAG, "parentId" + CategoryFragment.this.parentId);
//
//                    }

                }
            }
        };
    }

    private Response.Listener<CategoryChildBean[]> responseDownloadCategoryChildListener() {
        return new Response.Listener<CategoryChildBean[]>() {
            @Override
            public void onResponse(CategoryChildBean[] categoryChildBean) {
                if (categoryChildBean != null) {
                    mChildList = Utils.array2List(categoryChildBean);
                    Log.e(TAG, "categoryChildBean" + categoryChildBean);
                    mChildAdapter.initContact(mChildList);
                }
                mContext.sendStickyBroadcast(new Intent("update_childBean"));
            }
        };
    }

    private void initView(View layout) {
        tvChildName = (TextView) layout.findViewById(R.id.tv_child_name);
        tvAllGoods = (TextView) layout.findViewById(R.id.tv_all_goods);
        tvRefresh = (TextView) layout.findViewById(R.id.tvRefreshHint);
        srl = (SwipeRefreshLayout) layout.findViewById(R.id.srl_category_child);
        mGridLayoutManager = new GridLayoutManager(mContext, 3);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlCategory = (RecyclerView) layout.findViewById(R.id.rl_category);
        rlCategoryChild = (RecyclerView) layout.findViewById(R.id.rl_category_child);
        rlCategory.setHasFixedSize(true);
        rlCategoryChild.setHasFixedSize(true);
        tvLogin = (TextView) layout.findViewById(R.id.tv_category_login);
        mGroupList = new ArrayList<CategoryGroupBean>();
        mChildList = new ArrayList<CategoryChildBean>();
        mGroupAdapter = new CategoryAdapter(mContext, mGroupList);
        mChildAdapter = new CategoryChildAdapter(mContext, mChildList);
        mGridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rlCategoryChild.setLayoutManager(mGridLayoutManager);
        rlCategory.setLayoutManager(mLinearLayoutManager);
        rlCategory.setAdapter(mGroupAdapter);
        rlCategoryChild.setAdapter(mChildAdapter);
    }


    public String getPath() {

        try {
            path = new ApiParams()
                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP);
            Log.e(TAG, "path=" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public String getChildPath() {
        try {
            childPath = new ApiParams()
                    .with(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                    .with(I.PAGE_ID,I.PAGE_ID_DEFAULT+"")
                    .with(D.CategoryChild.PARENT_ID,pId+"")
                    .getRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN);
            Log.e(TAG, "getChildPath-childPath=" + childPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return childPath;
    }

    class CategoryParentIdReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "Receiver11111");
            pId = intent.getIntExtra("id", 344);
            Log.e(TAG, "id"+pId);
            initData();
        }
    }
    CategoryParentIdReceiver mReceiver;
    private  void  Update_parentId() {
        mReceiver = new CategoryParentIdReceiver();
        IntentFilter filter = new IntentFilter("parentId");
        mContext.registerReceiver(mReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }
}
