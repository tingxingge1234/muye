package cn.project.muye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.project.muye.FirstFragment;
import cn.project.muye.R;


public class MuYeMainActivity extends BaseActivity {
    public static final String TAG = MuYeMainActivity.class.getName();
    RadioButton mRadioFirst,mRadioCategory,mRadioShops,mRadioSearch,mRadioMore;
    int index;
    int currentTabIndex;
    RadioButton[] mRadios = new RadioButton[5];
    Fragment[] mFragments = new Fragment[5];
    FirstFragment mFirstFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mu_ye_main);
        initFragment();
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_contains, mFirstFragment)
                .show(mFirstFragment)
                .commit();
        initView();
    }

    private void initView() {
        mRadioFirst = (RadioButton) findViewById(R.id.first);
        mRadioCategory = (RadioButton) findViewById(R.id.category);
        mRadioShops = (RadioButton) findViewById(R.id.shops);
        mRadioSearch = (RadioButton) findViewById(R.id.search);
        mRadioMore = (RadioButton) findViewById(R.id.more);
    }

    private void initFragment() {
        mFirstFragment = new FirstFragment();
        mFragments[0] = mFirstFragment;
    }
    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.first:
                index = 0;
                break;
            case R.id.category:
                index = 1;
                break;
            case R.id.shops:
                index = 2;
                break;
            case R.id.search:
                    index = 3;
                break;
            case R.id.more:
                    index = 4;
                break;
        }
        if (currentTabIndex != index) {
            Log.e(TAG, "index" + index);
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);
            if (!mFragments[index].isAdded()) {
                trx.add(R.id.fl_contains, mFragments[index]);
            }
            trx.show(mFragments[index]).commit();
            setRadioChecked(index);
            currentTabIndex = index;
            Log.e(TAG, "currentTabIndex=" + currentTabIndex);
        }
    }
    private void setRadioChecked(int index) {
        for (int i = 0; i <mRadios.length ; i++) {
            if (i == index) {
                mRadios[i].setChecked(true);
            } else {
                mRadios[i].setChecked(false);
            }
        }
    }
}
