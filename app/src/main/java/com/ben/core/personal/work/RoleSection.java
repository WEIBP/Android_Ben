package com.ben.core.personal.work;

import com.ben.base.BaseListItemBean;
import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by Ben on 2017/9/25.
 */

public class RoleSection extends SectionEntity<RoleBean> {


        public RoleSection(boolean isHeader, String header) {
                super(isHeader, header);
        }

        public RoleSection(RoleBean roleBean) {
                super(roleBean);
        }
}