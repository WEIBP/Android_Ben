package com.ben.core.personal;

import android.content.Intent;
import android.os.Build;
import android.transition.Explode;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;

import com.ben.R;
import com.ben.base.BaseFragment;
import com.ben.base.BaseListItemBean;
import com.ben.core.androidbase.BLEActivity;
import com.ben.core.androidbase.BLEKotlinActivity;
import com.ben.core.location.Location_Activity;
import com.ben.core.nomalview.TimerActivity;
import com.ben.core.personal.stocks.StocksActivity;
import com.ben.core.personal.timeworktools.TimeWorkToolsActivity;
import com.ben.core.personal.traning.TraningActivity;
import com.ben.core.personal.wifi.WifiActivity;
import com.ben.core.personal.work.NewZuzhijiagouActivity;
import com.ben.core.swr.SDKTestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment {

        @BindView(R.id.main_list) RecyclerView mainList;

        @Override protected int getLayoutId() {
                return R.layout.fragment_personal;
        }

        @Override protected void initView(View view) {
                List<MySection> mySections = new ArrayList<>();
                mySections.add(new MySection(true, "开发"));
                mySections.add(new MySection(
                        new BaseListItemBean("Service列表", R.drawable.keybord, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), ServiceListActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("TreeList", R.drawable.treelist, "", "", 0,
                                v -> startActivity(new Intent(getActivity(), TreeListActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("定位", R.drawable.location, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), Location_Activity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("数据库", R.drawable.database, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), Location_Activity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("网络调试", R.drawable.testnet, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), Location_Activity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("坐标系转换", R.drawable.testnet, "", "", 0,
                                v -> startActivity(new Intent(getActivity(),
                                        ConvertLocationActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("手机信息", R.drawable.testnet, "", "", 0,
                                v -> startActivity(new Intent(getActivity(),
                                        ConvertLocationActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("wifi调试", R.drawable.testnet, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), WifiActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("自定义控件", R.drawable.testnet, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), WifiActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("支付功能", R.drawable.testnet, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), Location_Activity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("stock", R.drawable.testnet, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), StocksActivity.class)))));

                mySections.add(new MySection(true, "应用"));
                mySections.add(new MySection(
                        new BaseListItemBean("我的关注", R.drawable.stock, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), Location_Activity.class)))));

                mySections.add(new MySection(true, "工作"));
                mySections.add(new MySection(
                        new BaseListItemBean("综合展示", R.drawable.work, "", "", 0, v -> startActivity(
                                new Intent(getActivity(), TestActivity.class)))));
                mySections.add(new MySection(
                        new BaseListItemBean("指挥架构", R.drawable.work, "", "", 0, v -> startActivity(
                                new Intent(getActivity(), NewZuzhijiagouActivity.class)))));

                mySections.add(new MySection(

                        new BaseListItemBean("iLab_SDK", R.drawable.work, "", "", 0, new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                        startActivity(new Intent(getActivity(), SDKTestActivity.class));
                                }
                        })));

                mySections.add(new MySection(true, "练习"));
                mySections.add(new MySection(
                        new BaseListItemBean("training", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), TraningActivity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("定时工具", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), TimeWorkToolsActivity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("蓝牙", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), BLEActivity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("蓝牙kotlin", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), BLEKotlinActivity.class)))));


                mySections.add(new MySection(
                        new BaseListItemBean("WIFI", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), TraningActivity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("Socket", R.drawable.work, "", "", 0,
                                v -> startActivity(
                                        new Intent(getActivity(), TraningActivity.class)))));

                mySections.add(new MySection(true, "个人"));
                mySections.add(new MySection(
                        new BaseListItemBean("数据模拟", R.drawable.work, "", "", 0, v -> startActivity(
                                new Intent(getActivity(), DataSimulationActivity.class)))));

                mySections.add(new MySection(
                        new BaseListItemBean("倒计时组件", R.drawable.work, "", "", 0, v -> startActivity(
                                new Intent(getActivity(), TimerActivity.class)))));

                FunctionsAdapter functionsAdapter =
                        new FunctionsAdapter(R.layout.item_function_image,
                                R.layout.item_function_header, mySections);
                mainList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                mainList.setAdapter(functionsAdapter);
        }
}
