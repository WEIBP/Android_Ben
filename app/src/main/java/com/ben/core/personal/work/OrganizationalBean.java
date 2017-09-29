package com.ben.core.personal.work;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by Ben on 2017/9/25.
 */

public class OrganizationalBean  implements MultiItemEntity {
        public static final int only_one = 1;
        public static final int left = 2;
        public static final int right = 3;
        public static final int nomal = 4;

        private int itemType;
        private String organizational;
        private List<RoleSection> roleSections;


        public OrganizationalBean(int itemType, String organizational, List<RoleSection> roleSections) {
                this.itemType = itemType;
                this.organizational = organizational;
                this.roleSections = roleSections;
        }

        public String getOrganizational() {
                return organizational;
        }

        public void setOrganizational(String organizational) {
                this.organizational = organizational;
        }
        public void setItemType(int itemType) {
                this.itemType = itemType;
        }

        public List<RoleSection> getRoleSections() {
                return roleSections;
        }

        public void setRoleSections(List<RoleSection> roleSections) {
                this.roleSections = roleSections;
        }

        @Override
        public int getItemType() {
                return itemType;
        }
}
