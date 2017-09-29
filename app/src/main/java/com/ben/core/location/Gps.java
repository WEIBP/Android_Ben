package com.ben.core.location;

/**
 * Created by Ben on 2017/9/15.
 */

public class Gps {

        private double wgLat;
        private double wgLon;

        public Gps(double wgLat, double wgLon) {
                setWgLat(wgLat);
                setWgLon(wgLon);
        }

        public double getWgLat() {
                return wgLat;
        }

        public void setWgLat(double wgLat) {
                this.wgLat = wgLat;
        }

        public double getWgLon() {
                return wgLon;
        }

        public void setWgLon(double wgLon) {
                this.wgLon = wgLon;
        }

        @Override
        public String toString() {
                return wgLat + "," + wgLon;
        }
}