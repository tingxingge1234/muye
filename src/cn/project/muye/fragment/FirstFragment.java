package cn.project.muye.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import cn.project.muye.I;
import cn.project.muye.R;
import cn.project.muye.activity.LoginActivity;
import cn.project.muye.activity.MuYeMainActivity;
import cn.project.muye.adapter.GoodsAdapter;
import cn.project.muye.bean.NewGoodBean;
import cn.project.muye.data.ApiParams;
import cn.project.muye.data.GsonRequest;
import cn.project.muye.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    public static final String TAG = FirstFragment.class.getName();
    MuYeMainActivity mContext;
    ArrayList<NewGoodBean> mGoodList;
    GoodsAdapter mAdapter;
    private int pageId = 0;
    private int action = I.ACTION_DOWNLOAD;
    String path;

    /**
     * 下拉刷新空间
     */
    RecyclerView rv_recommend,rv_discount;
    TextView tv_login;

    GridLayoutManager mGridLayoutManager2,mGridLayoutManager3;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = (MuYeMainActivity) getActivity();
        View layout = inflater.inflate(R.layout.fragment_first, container, false);
        mGoodList = new ArrayList<NewGoodBean>();
        initView(layout);
        setListener();
        initData();
        return layout;
    }
    private void initData() {
        getPath(pageId);
        Log.e(TAG, "path = " + path);
        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path, NewGoodBean[].class,
                responseDownloadNewGoodListener(), mContext.errorListener()));
    }

    private Response.Listener<NewGoodBean[]> responseDownloadNewGoodListener() {
        return new Response.Listener<NewGoodBean[]>() {
            @Override
            public void onResponse(NewGoodBean[] newGoodBeen) {
                if (newGoodBeen != null) {
                    mAdapter.setMore(true);
                    mAdapter.setFooterText(getResources().getString(R.string.load_more));
                    //将数组转换为集合
                    ArrayList<NewGoodBean> list = Utils.array2List(newGoodBeen);
                    if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                        mAdapter.initContact(list);
                    } else if (action==I.ACTION_PULL_UP) {
                        mAdapter.addContact(list);
                    }
                    if (newGoodBeen.length < I.PAGE_SIZE_DEFAULT) {
                        mAdapter.setMore(false);
                        mAdapter.setFooterText(getResources().getString(R.string.no_more));
                    }
                }
            }
        };
    }


    private String getPath(int pageId) {
        try {
            path = new ApiParams()
                    .with(I.NewAndBoutiqueGood.CAT_ID, I.CAT_ID + "")
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                    .getRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setListener() {
//        setPullDownRefreshListener();
//        setPullUpRefreshListener();
        setLoginListener();
    }

    private void setLoginListener() {
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });
    }

    /**
     * 上拉刷新
     */
//    private void setPullUpRefreshListener() {
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            int lastItemPosition;
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastItemPosition == mAdapter.getItemCount() - 1) {
//                    if (mAdapter.isMore()) {
//                        msrl.setRefreshing(true);
//                        action = I.ACTION_PULL_UP;
//                        pageId+=I.PAGE_SIZE_DEFAULT;
//                        getPath(pageId);
//                        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path, NewGoodBean[].class,
//                                responseDownloadNewGoodListener(), mContext.errorListener()));
//                    }
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
//                msrl.setEnabled(mGridLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
//            }
//        });
//    }
    /**
     * 下拉刷新
     */
//    private void setPullDownRefreshListener() {
//        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pageId = 0;
//                action = I.ACTION_PULL_DOWN;
//                getPath(pageId);
//                mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path, NewGoodBean[].class,
//                        responseDownloadNewGoodListener(), mContext.errorListener()));
//            }
//        });
//    }

    private void initView(View layout ) {
//        msrl.setColorSchemeColors(
//                R.color.google_blue,
//                R.color.google_green,
//                R.color.google_red,
//                R.color.google_yellow
//        );
        tv_login = (TextView) layout.findViewById(R.id.tv_login);
        mGridLayoutManager2 = new GridLayoutManager(mContext, 2);
        mGridLayoutManager3 = new GridLayoutManager(mContext, 3);
        mGridLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        mGridLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        rv_discount = (RecyclerView) layout.findViewById(R.id.rl_discount);
        rv_recommend = (RecyclerView) layout.findViewById(R.id.rl_recommend);
        rv_discount.setHasFixedSize(true);
        rv_discount.setLayoutManager(mGridLayoutManager2);
        rv_recommend.setHasFixedSize(true);
        rv_recommend.setLayoutManager(mGridLayoutManager3);

        mAdapter = new GoodsAdapter(mContext, mGoodList,I.SORT_BY_ADDTIME_DESC);
        rv_discount.setAdapter(mAdapter);
        rv_recommend.setAdapter(mAdapter);
    }

}
