package com.ben.core.personal.work;

/**
 * Created by Ben on 2017/9/25.
 */

public class RoleBean {
        private String role;
        private String name;

        public RoleBean(String role, String name, String phone) {
                this.role = role;
                this.name = name;
                this.phone = phone;
        }

        private String phone;

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }
}
