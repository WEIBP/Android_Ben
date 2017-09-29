package com.ben.core.personal.treelist;

/**
 * Created by Ben on 2017/9/27.
 */

public class FileBean {
        @TreeNodeId
        private int _id;
        @TreeNodePid
        private int parentId;
        @TreeNodeLabel
        private String name;
        private long length;
        private String desc;

        public FileBean(int _id, int parentId, String name)
        {
                super();
                this._id = _id;
                this.parentId = parentId;
                this.name = name;
        }

}
