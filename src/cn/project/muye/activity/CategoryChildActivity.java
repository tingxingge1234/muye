package cn.project.muye.activity;

import android.app.Activity;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import java.util.ArrayList;

import cn.project.muye.D;
import cn.project.muye.I;
import cn.project.muye.R;
import cn.project.muye.adapter.GoodsAdapter;
import cn.project.muye.bean.CategoryChildBean;
import cn.project.muye.bean.NewGoodBean;
import cn.project.muye.data.ApiParams;
import cn.project.muye.data.GsonRequest;
import cn.project.muye.utils.Utils;

public class CategoryChildActivity extends BaseActivity {
    public static final String TAG= CategoryChildActivity.class.getName();
    CategoryChildActivity mContext;
    ArrayList<NewGoodBean> mGoodList;
    GoodsAdapter mAdapter;
    private int pageId = 0;
    private int action = I.ACTION_DOWNLOAD;
    String path;
    /**
     * 下拉刷新空间
     */
    SwipeRefreshLayout msrl;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        mContext = this;
        mGoodList = new ArrayList<NewGoodBean>();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                        lastItemPosition == mAdapter.getItemCount() - 1) {
                    if (mAdapter.isMore()) {
                        msrl.setRefreshing(true);
                        action = I.ACTION_PULL_UP;
                        pageId+=I.PAGE_SIZE_DEFAULT;
                        getPath(catId);
                        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path,
                                NewGoodBean[].class, responseDownloadNewGoodListener(),
                                mContext.errorListener()));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                msrl.setEnabled(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
            }
        });
    }

    private void setPullDownRefreshListener() {
        msrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageId = 0;
                action = I.ACTION_PULL_DOWN;
                getPath(catId);
                mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path, NewGoodBean[].class,
                        responseDownloadNewGoodListener(), mContext.errorListener()));
            }
        });
    }

    private void initData() {
        catId = getIntent().getIntExtra(D.CategoryChild.PARENT_ID, 0);
        getPath(catId);
        mContext.executeRequest(new GsonRequest<NewGoodBean[]>(path,NewGoodBean[].class,
                responseDownloadNewGoodListener(), mContext.errorListener()));
    }

    private Response.Listener<NewGoodBean[]> responseDownloadNewGoodListener() {
        return new Response.Listener<NewGoodBean[]>() {
            @Override
            public void onResponse(NewGoodBean[] newGoodBeen) {
                if (newGoodBeen != null) {
                    mAdapter.setMore(true);
                    msrl.setRefreshing(false);
                    mAdapter.setFooterText(getResources().getString(R.string.load_more));
                    //将数组转换为集合
                    ArrayList<NewGoodBean> list = Utils.array2List(newGoodBeen);
                    Log.e(TAG, "list=" + list);
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

    private String getPath(int catId) {
        try {
            path = new ApiParams()
                    .with(I.NewAndBoutiqueGood.CAT_ID,  catId+ "")
                    .with(I.PAGE_ID, pageId + "")
                    .with(I.PAGE_SIZE, I.PAGE_SIZE_DEFAULT + "")
                    .getRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS);
            Log.e(TAG, "path=" + path);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initView() {
        msrl = (SwipeRefreshLayout)findViewById(R.id.srl_category_child);
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_category_child);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new GoodsAdapter(mContext, mGoodList,I.SORT_BY_ADDTIME_DESC);
        mRecyclerView.setAdapter(mAdapter);
    }
}
