package com.ben.core.personal;

import com.ben.base.BaseListItemBean;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Ben on 2017/9/22.
 */

public class MySection extends SectionEntity<BaseListItemBean> {


        public MySection(boolean isHeader, String header) {
                super(isHeader, header);
        }

        public MySection(BaseListItemBean baseListItemBean) {
                super(baseListItemBean);
        }
}
