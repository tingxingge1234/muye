package cn.project.muye.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.NetworkImageView;

import cn.project.muye.D;
import cn.project.muye.I;
import cn.project.muye.R;
import cn.project.muye.bean.GoodDetailsBean;
import cn.project.muye.data.ApiParams;
import cn.project.muye.data.GsonRequest;
import cn.project.muye.utils.ImageUtils;
import cn.project.muye.utils.Utils;
import cn.project.muye.view.DisPlayUtils;

public class GoodsDetailsActivity extends BaseActivity {
    public static final String TAG = GoodsDetailsActivity.class.getName();
    GoodDetailsBean mGoodDetails;
    GoodsDetailsActivity mContext;
    NetworkImageView nivGoods,nivGoods1;
    TextView tvGoodsName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_details);
        mContext = this;
        initView();
        initData();
    }

    private void initData() {
        int goodsId = getIntent().getIntExtra(D.NewGood.KEY_GOODS_ID, 0);
        try {
            String path = new ApiParams()
                    .with(D.NewGood.KEY_GOODS_ID, goodsId + "")
                    .getRequestUrl(I.REQUEST_FIND_GOOD_DETAILS);
            executeRequest(new GsonRequest<GoodDetailsBean>(path, GoodDetailsBean.class,
                    responseDownloadGoodDetailsListener(), errorListener()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response.Listener<GoodDetailsBean> responseDownloadGoodDetailsListener() {
        return new Response.Listener<GoodDetailsBean>() {
            @Override
            public void onResponse(GoodDetailsBean goodDetailsBean) {
                Log.e(TAG, "goodDetailsBean=" + goodDetailsBean);
                if (goodDetailsBean != null) {
                    mGoodDetails = goodDetailsBean;
                    tvGoodsName.setText(mGoodDetails.getGoodsName());
                    String colorImg = mGoodDetails.getGoodsImg();
                    Log.e(TAG, "colorImag=" + colorImg);
                    ImageUtils.setGoodDetailThumb(colorImg, nivGoods);
                    ImageUtils.setGoodDetailThumb(colorImg, nivGoods1);

                    //初始化颜色面板
                } else {
                    Utils.showToast(mContext, "商品详情下载失败", Toast.LENGTH_LONG);
                    finish();
                }
            }
        };
    }

    private void initView() {
        nivGoods = (NetworkImageView) findViewById(R.id.niv_goods);
        nivGoods1 = (NetworkImageView) findViewById(R.id.niv_goods1);
        tvGoodsName = (TextView) findViewById(R.id.tv_goods_name);
    }
}
