package cn.project.muye.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.midi.MidiReceiver;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import cn.project.muye.fragment.CategoryFragment;
import cn.project.muye.fragment.FirstFragment;
import cn.project.muye.R;


public class MuYeMainActivity extends BaseActivity {
    public static final String TAG = MuYeMainActivity.class.getName();
    RadioButton mRadioFirst,mRadioCategory,mRadioShops,mRadioSearch,mRadioMore;
    int index;
    int currentTabIndex;
    RadioButton[] mRadios = new RadioButton[5];
    Fragment[] mFragments = new Fragment[2];
    FirstFragment mFirstFragment;
    CategoryFragment mCategoryFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mu_ye_main);
        initFragment();
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_contains, mFirstFragment)
                .add(R.id.fl_contains,mCategoryFragment).hide(mCategoryFragment)
                .show(mFirstFragment)
                .commit();
        initView();
        registerCategoryChildListReceiver();
    }

    private void initView() {
        mRadioFirst = (RadioButton) findViewById(R.id.first);
        mRadioCategory = (RadioButton) findViewById(R.id.category);
        mRadioShops = (RadioButton) findViewById(R.id.shops);
        mRadioSearch = (RadioButton) findViewById(R.id.search);
        mRadioMore = (RadioButton) findViewById(R.id.more);
        mRadios[0] = mRadioFirst;
        mRadios[1] = mRadioCategory;
        mRadios[2] = mRadioShops;
        mRadios[3] = mRadioSearch;
        mRadios[4] = mRadioMore;
    }

    private void initFragment() {
        mFirstFragment = new FirstFragment();
        mCategoryFragment = new CategoryFragment();
        mFragments[0] = mFirstFragment;
        mFragments[1]= mCategoryFragment;
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
            Log.e(TAG, "currentTabIndex=" + currentTabIndex);
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
        Log.e(TAG, "index="+index);
        for (int i = 0; i <mRadios.length ; i++) {
            if (i == index) {
                mRadios[i].setChecked(true);
            } else {
                mRadios[i].setChecked(false);
            }
        }
    }

    class UpdateCategoryChildListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
    UpdateCategoryChildListReceiver mReceiver;
    private void registerCategoryChildListReceiver() {
        mReceiver = new UpdateCategoryChildListReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("update_childBean");
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}
