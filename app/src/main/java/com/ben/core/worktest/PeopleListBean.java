package com.ben.core.worktest;

import java.util.List;

/**
 * Created by Ben on 2018/2/28.
 */

public class PeopleListBean {

        private String msg;
        private String code;
        private List<DataBean> data;

        public String getMsg() {
                return msg;
        }

        public void setMsg(String msg) {
                this.msg = msg;
        }

        public String getCode() {
                return code;
        }

        public void setCode(String code) {
                this.code = code;
        }

        public List<DataBean> getData() {
                return data;
        }

        public void setData(List<DataBean> data) {
                this.data = data;
        }

        public  class DataBean {
                /**
                 * cellphone : 13411111111
                 * name : 测试
                 * id : 1
                 */

                private String cellphone;
                private String name;
                private int id;

                public String getCellphone() {
                        return cellphone;
                }

                public void setCellphone(String cellphone) {
                        this.cellphone = cellphone;
                }

                public String getName() {
                        return name;
                }

                public void setName(String name) {
                        this.name = name;
                }

                public int getId() {
                        return id;
                }

                public void setId(int id) {
                        this.id = id;
                }
        }
}
