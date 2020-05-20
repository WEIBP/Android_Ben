package com.ben.core.androidbase;

import java.util.List;
import java.util.UUID;

/**
 * author: Ben
 * created on: 2020/3/6 16:22
 * description:
 */
public class BleAdvertisedData {
        private List<UUID> mUuids;
        private String mName;
        public BleAdvertisedData(List<UUID> uuids, String name){
                mUuids = uuids;
                mName = name;
        }

        public List<UUID> getUuids(){
                return mUuids;
        }

        public String getName(){
                return mName;
        }
}
