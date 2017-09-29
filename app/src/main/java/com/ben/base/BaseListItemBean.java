package com.ben.base;

import android.view.View;

/**
 * Created by Ben on 2017/9/21.
 */

public class BaseListItemBean {
        private String name;
        private int logo;
        private String description;
        private String note;

        public BaseListItemBean(String name, int logo, String description, String note, int status, View.OnClickListener onClickListener) {
                this.name = name;
                this.logo = logo;
                this.description = description;
                this.note = note;
                this.status = status;
                this.onClickListener = onClickListener;
        }

        private int status;
        private View.OnClickListener onClickListener;

        public View.OnClickListener getOnClickListener() {
                return onClickListener;
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
                this.onClickListener = onClickListener;
        }


        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getLogo() {
                return logo;
        }

        public void setLogo(int logo) {
                this.logo = logo;
        }

        public String getDescription() {
                return description;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public String getNote() {
                return note;
        }

        public void setNote(String note) {
                this.note = note;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }



}
