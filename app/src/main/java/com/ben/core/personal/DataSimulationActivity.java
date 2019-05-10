package com.ben.core.personal;

import com.ben.R;
import com.ben.base.BaseActivity;
import com.ben.library.log.L;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import java.util.Random;

public class DataSimulationActivity extends BaseActivity {


  @Override
  protected int getLayoutId() {
    return R.layout.activity_data_simulation;
  }

  boolean flagy = false;
  boolean flag4 = false;
  int[] temp = new int[11];

  @Override
  protected void initView() {

    new Thread(new Runnable() {
      @Override
      public void run() {
        int j = 1000000000;
        int ry5 = 0;
        int ratel5 = 0;
        int r4 = 0;
        int ratel4 = 0;
        int ratel3 = 0;
        int r3 = 0;
        int r2 = 0;
        int r1 = 0;

        String startTime = TimeUtils.getNowString();

        for (int i = 0; i < j; i++) {

          if (i % 1000000 == 0 && i != 0) {
            double span = TimeUtils
                .getTimeSpan(startTime, TimeUtils.getNowString(), TimeConstants.SEC);
            L.object((j - i) * span / i);
          }

          for (int b = 0; b < 8; b++) {
            danchou(b + 1);
          }
          baodi4(9);//这两行顺序换一下适应两种规则
          baodiy(10);//
          for (int b = 1; b < 10; b++) {
            if (temp[b] == 1) {
              ry5++;
            } else if (temp[b] <= 4) {
              r4++;
            } else if (temp[b] <= 44) {
              r3++;
            } else if (temp[b] <= 48) {
              ratel5++;
            } else if (temp[b] <= 60) {
              ratel4++;
            } else {
              ratel3++;
            }
          }
        }
        L.object("5星英灵：" + (ry5 / j) * 10 + "%  /n"
            + "4星英灵：" + (r4 / j) * 10 + "%  /n"
            + "3星英灵：" + (r3 / j) * 10 + "%  /n"
            + "5星礼装：：" + (ratel5 / j) * 10 + "%  /n"
            + "4星礼装：：" + (ratel4 / j) * 10 + "%  /n"
            + "3星礼装：：" + (ratel3 / j) * 10 + "%  /n");
      }
    }).start();


  }


  void danchou(int now) {
    int temp = new Random().nextInt(100) + 1;
    if (temp <= 44) {
      flagy = true;
    }
    if (temp <= 4 || temp >= 45 && temp <= 60) {
      flag4 = true;
    }
  }

  void baodiy(int now) {
    if (flagy) {
      danchou(now);
      return;
    }
    temp[now] = new Random().nextInt(44) + 1;
    if (temp[now] <= 4) {
      flag4 = true;
    }
  }

  void baodi4(int now) {
    if (flag4) {
      danchou(now);
      return;
    }
    temp[now] = new Random().nextInt(20) + 1;
    if (temp[now] > 4) {
      temp[now] += 40;
    } else
      flagy = true;
  }
}
