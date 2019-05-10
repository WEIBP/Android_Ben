package com.ben.core.personal;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.ben.R;
import com.ben.core.personal.treelist.FileBean;
import com.ben.core.personal.treelist.SimpleTreeAdapter;
import com.ben.core.personal.treelist.TreeListViewAdapter;
import com.ben.core.personal.work.OrganizationAdapter;
import com.ben.core.personal.work.OrganizationalBean;
import com.ben.core.personal.work.RoleAdapter;
import com.ben.core.personal.work.RoleBean;
import com.ben.core.personal.work.RoleSection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends Activity {

        @BindView(R.id.list_left)
        ListView listLeft;

        @BindView(R.id.list_right)
        ListView listRight;

        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.list_person)
        RecyclerView listPerson;
        @BindView(R.id.list_card)
        RecyclerView listCard;

        private List<FileBean> mDatas = new ArrayList<FileBean>();
        private TreeListViewAdapter mAdapter;

        private List<FileBean> mDatasRight = new ArrayList<FileBean>();
        private TreeListViewAdapter mAdapterRight;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(getLayoutId());
                ButterKnife.bind(this);
                initView();
                initZhihuijiagou();
        }

        protected int getLayoutId() {
                requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
                if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                return R.layout.activity_test;
        }


        private void initView() {
                // id , pid , label , 其他属性
                mDatas.add(new FileBean(1, 0, "常设机构"));
                mDatas.add(new FileBean(2, 0, "临时机构"));
                mDatas.add(new FileBean(3, 2, "十九大安保JW工作组"));
                mDatas.add(new FileBean(4, 2, "联勤指挥部"));
                mDatas.add(new FileBean(5, 3, "xx专班"));
                mDatas.add(new FileBean(6, 3, "xx筹备班"));
                mDatas.add(new FileBean(7, 3, "xx联勤指挥部"));
                mDatas.add(new FileBean(8, 3, "xx分指挥部"));
                mDatas.add(new FileBean(9, 4, "国家会议中心前方指挥部"));
                mDatas.add(new FileBean(10, 4, "人民大会堂和国家大剧院前方指挥部"));
                mDatas.add(new FileBean(11, 4, "雁栖湖国际会都前方指挥部"));
                mDatas.add(new FileBean(12, 4, "故宫现场指挥部"));
                mDatas.add(new FileBean(13, 4, "路线分指挥部"));
                mDatas.add(new FileBean(14, 4, "住地分指挥部"));
                mDatas.add(new FileBean(15, 4, "首都机场前方指挥部"));
                mDatas.add(new FileBean(16, 15, "现场一组"));
                mDatas.add(new FileBean(17, 15, "现场二组"));
                mDatas.add(new FileBean(18, 17, "xx一组"));
                mDatas.add(new FileBean(19, 17, "xx二组"));

                try {
                        mAdapter = new SimpleTreeAdapter<>(listLeft, this, mDatas, 100);
                        listLeft.setAdapter(mAdapter);
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                }


                // id , pid , label , 其他属性
                mDatasRight.add(new FileBean(1, 0, "每日勤务（5起）"));
                mDatasRight.add(new FileBean(2, 1, "总书记入京QW"));
                mDatasRight.add(new FileBean(3, 1, "总理出访QW"));
                mDatasRight.add(new FileBean(4, 1, "副总理离京QW"));
                mDatasRight.add(new FileBean(5, 1, "国防部长出访QW"));
                mDatasRight.add(new FileBean(6, 1, "美国总统访问QW"));


                try {
                        mAdapterRight = new SimpleTreeAdapter<>(listRight, this, mDatasRight, 100);
                        listRight.setAdapter(mAdapterRight);
                } catch (IllegalAccessException e) {
                        e.printStackTrace();
                }


        }

        protected void initZhihuijiagou() {

                List<RoleSection> roleSections = new ArrayList<>();
                roleSections.add(new RoleSection(true, "负责人:"));
                roleSections.add(new RoleSection(new RoleBean("负责人", "李润华", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "陶 晶", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "陈思源", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "田运胜", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "王磊", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "伊燕京", "")));
                roleSections.add(new RoleSection(new RoleBean("负责人", "李伟杰", "")));
                OrganizationalBean organizationalBean = new OrganizationalBean(OrganizationalBean.only_one, "联勤指挥部", roleSections);
                textName.setText(organizationalBean.getOrganizational());
                listPerson.setLayoutManager(new GridLayoutManager(this,3));
                listPerson.setAdapter(new RoleAdapter(this,R.layout.list_item_person,R.layout.list_item_person_header,roleSections));

                List<OrganizationalBean> organizationalBeen = new ArrayList<>();


                List<RoleSection> roleSections2 = new ArrayList<>();
                roleSections2.add(new RoleSection(true, "指挥长:"));
                roleSections2.add(new RoleSection(new RoleBean("指挥长", "李润华", "")));
                roleSections2.add(new RoleSection(true, "副指挥长:"));
                roleSections2.add(new RoleSection(new RoleBean("指挥长", "牛国泉", "")));
                roleSections2.add(new RoleSection(new RoleBean("指挥长", "洪世杰", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.left, "国家会议中心 \n 前方指挥部", roleSections2));

                List<RoleSection> roleSections3 = new ArrayList<>();
                roleSections3.add(new RoleSection(true, "指挥长:"));
                roleSections3.add(new RoleSection(new RoleBean("指挥长", "陶 晶", "")));
                roleSections3.add(new RoleSection(true, "副指挥长:"));
                roleSections3.add(new RoleSection(new RoleBean("指挥长", "张 明", "")));
                roleSections3.add(new RoleSection(new RoleBean("指挥长", "武颐发", "")));
                roleSections3.add(new RoleSection(new RoleBean("指挥长", "张贺丽", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.nomal, "人民大会堂和国家大剧院前方指挥部", roleSections3));

                List<RoleSection> roleSections4 = new ArrayList<>();
                roleSections4.add(new RoleSection(true, "指挥长:"));
                roleSections4.add(new RoleSection(new RoleBean("指挥长", "陈思源", "")));
                roleSections4.add(new RoleSection(true, "副指挥长:"));
                roleSections4.add(new RoleSection(new RoleBean("指挥长", "王  赤", "")));
                roleSections4.add(new RoleSection(new RoleBean("指挥长", "陈新民", "")));
                roleSections4.add(new RoleSection(new RoleBean("指挥长", "王伟", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.nomal, "雁栖湖国际会都前方指挥部", roleSections4));

                List<RoleSection> roleSections5 = new ArrayList<>();
                roleSections5.add(new RoleSection(true, "指挥长:"));
                roleSections5.add(new RoleSection(new RoleBean("指挥长", "衡晓帆", "")));
                roleSections5.add(new RoleSection(true, "副指挥长:"));
                roleSections5.add(new RoleSection(new RoleBean("指挥长", "洪世杰", "")));
                roleSections5.add(new RoleSection(new RoleBean("指挥长", "武颐发", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.nomal, "故宫现场指挥部", roleSections5));


                List<RoleSection> roleSections6 = new ArrayList<>();
                roleSections6.add(new RoleSection(true, "指挥长:"));
                roleSections6.add(new RoleSection(new RoleBean("指挥长", "张 兵", "")));
                roleSections6.add(new RoleSection(true, "副指挥长:"));
                roleSections6.add(new RoleSection(new RoleBean("指挥长", "陈 军", "")));
                roleSections6.add(new RoleSection(new RoleBean("指挥长", "程  建", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.nomal, "路线分指挥部", roleSections6));

                List<RoleSection> roleSections7 = new ArrayList<>();
                roleSections7.add(new RoleSection(true, "指挥长:"));
                roleSections7.add(new RoleSection(new RoleBean("指挥长", "孙连辉", "")));
                roleSections7.add(new RoleSection(true, "副指挥长:"));
                roleSections7.add(new RoleSection(new RoleBean("指挥长", "赵光", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.nomal, "住地分指挥部", roleSections7));

                List<RoleSection> roleSections8 = new ArrayList<>();
                roleSections8.add(new RoleSection(true, "指挥长:"));
                roleSections8.add(new RoleSection(new RoleBean("指挥长", "李润华", "")));
                roleSections8.add(new RoleSection(true, "副指挥长:"));
                roleSections8.add(new RoleSection(new RoleBean("指挥长", "齐耀中", "")));
                roleSections8.add(new RoleSection(new RoleBean("指挥长", "郑激扬", "")));
                roleSections8.add(new RoleSection(new RoleBean("指挥长", "陈 军", "")));
                organizationalBeen.add(new OrganizationalBean(OrganizationalBean.right, "首都机场外围前方指挥部", roleSections8));
                OrganizationAdapter organizationAdapter = new OrganizationAdapter(this,organizationalBeen);
                LinearLayoutManager ms= new LinearLayoutManager(this);
                ms.setOrientation(LinearLayoutManager.HORIZONTAL);
                listCard.setLayoutManager(ms);
                listCard.setAdapter(organizationAdapter);


        }

}
