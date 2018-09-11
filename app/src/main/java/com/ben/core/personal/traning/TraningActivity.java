package com.ben.core.personal.traning;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import butterknife.OnClick;
import com.ben.R;
import com.ben.base.BaseActivity;
import java.util.List;

public class TraningActivity extends BaseActivity {

  @Override protected int getLayoutId() {
    return R.layout.activity_traning;
  }

  @Override protected void initView() {
        setContentView(true,true,"Android训练");
  }

  @OnClick(R.id.button_tell) void tell(){
    Uri webpage = Uri.parse("http://www.baidu.com");
    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
    Intent chooser = Intent.createChooser(webIntent, "请选择打开网页应用");
    if (isIntentSafe(webIntent)){
      startActivity(chooser);
    }
  }

  private  boolean isIntentSafe(Intent intent){
    PackageManager packageManager = getPackageManager();
    List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
    return activities.size() > 0;
  }


}
