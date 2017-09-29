package com.ben.core.personal;


import com.ben.R;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by Ben on 2017/9/21.
 */

public  class FunctionsAdapter extends BaseSectionQuickAdapter<MySection,BaseViewHolder> {

        public FunctionsAdapter(int layoutResId, int sectionHeadResId, List<com.ben.core.personal.MySection> data) {
                super(layoutResId, sectionHeadResId, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, com.ben.core.personal.MySection item) {
                helper.setText(R.id.func_name,item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, com.ben.core.personal.MySection item) {
                helper.setImageResource(R.id.imgae_func,item.t.getLogo())
                        .setText(R.id.text_funcname,item.t.getName())
                        .setOnClickListener(R.id.root_view,item.t.getOnClickListener());
        }
}
