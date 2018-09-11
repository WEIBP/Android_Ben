package com.ben.common.net;

import java.util.List;

/**
 * Created by Ben on 2018/1/22.
 */

public class WeatherBean {

        /**
         * date : 20180122
         * message : Success !
         * status : 200
         * city : 北京
         * count : 322
         * data : {"shidu":"51%","pm25":40,"pm10":61,"quality":"良","wendu":"-6","ganmao":"极少数敏感人群应减少户外活动","yesterday":{"date":"21日星期日","sunrise":"07:32","high":"高温 0.0℃","low":"低温 -5.0℃","sunset":"17:19","aqi":66,"fx":"东南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"},"forecast":[{"date":"22日星期一","sunrise":"07:32","high":"高温 -2.0℃","low":"低温 -10.0℃","sunset":"17:20","aqi":66,"fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"23日星期二","sunrise":"07:31","high":"高温 -4.0℃","low":"低温 -12.0℃","sunset":"17:22","aqi":36,"fx":"西北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"24日星期三","sunrise":"07:31","high":"高温 -4.0℃","low":"低温 -11.0℃","sunset":"17:23","aqi":37,"fx":"南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"25日星期四","sunrise":"07:30","high":"高温 -4.0℃","low":"低温 -9.0℃","sunset":"17:24","aqi":26,"fx":"南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"26日星期五","sunrise":"07:29","high":"高温 -2.0℃","low":"低温 -8.0℃","sunset":"17:25","aqi":84,"fx":"南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"}]}
         */

        private String date;
        private String message;
        private int status;
        private String city;
        private int count;
        private DataBean data;

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public String getMessage() {
                return message;
        }

        public void setMessage(String message) {
                this.message = message;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public int getCount() {
                return count;
        }

        public void setCount(int count) {
                this.count = count;
        }

        public DataBean getData() {
                return data;
        }

        public void setData(DataBean data) {
                this.data = data;
        }

        public static class DataBean {
                /**
                 * shidu : 51%
                 * pm25 : 40.0
                 * pm10 : 61.0
                 * quality : 良
                 * wendu : -6
                 * ganmao : 极少数敏感人群应减少户外活动
                 * yesterday : {"date":"21日星期日","sunrise":"07:32","high":"高温 0.0℃","low":"低温 -5.0℃","sunset":"17:19","aqi":66,"fx":"东南风","fl":"<3级","type":"阴","notice":"不要被阴云遮挡住好心情"}
                 * forecast : [{"date":"22日星期一","sunrise":"07:32","high":"高温 -2.0℃","low":"低温 -10.0℃","sunset":"17:20","aqi":66,"fx":"西南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"23日星期二","sunrise":"07:31","high":"高温 -4.0℃","low":"低温 -12.0℃","sunset":"17:22","aqi":36,"fx":"西北风","fl":"3-4级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"24日星期三","sunrise":"07:31","high":"高温 -4.0℃","low":"低温 -11.0℃","sunset":"17:23","aqi":37,"fx":"南风","fl":"<3级","type":"晴","notice":"愿你拥有比阳光明媚的心情"},{"date":"25日星期四","sunrise":"07:30","high":"高温 -4.0℃","low":"低温 -9.0℃","sunset":"17:24","aqi":26,"fx":"南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"},{"date":"26日星期五","sunrise":"07:29","high":"高温 -2.0℃","low":"低温 -8.0℃","sunset":"17:25","aqi":84,"fx":"南风","fl":"<3级","type":"多云","notice":"阴晴之间，谨防紫外线侵扰"}]
                 */

                private String shidu;
                private double pm25;
                private double pm10;
                private String quality;
                private String wendu;
                private String ganmao;
                private YesterdayBean yesterday;
                private List<ForecastBean> forecast;

                public String getShidu() {
                        return shidu;
                }

                public void setShidu(String shidu) {
                        this.shidu = shidu;
                }

                public double getPm25() {
                        return pm25;
                }

                public void setPm25(double pm25) {
                        this.pm25 = pm25;
                }

                public double getPm10() {
                        return pm10;
                }

                public void setPm10(double pm10) {
                        this.pm10 = pm10;
                }

                public String getQuality() {
                        return quality;
                }

                public void setQuality(String quality) {
                        this.quality = quality;
                }

                public String getWendu() {
                        return wendu;
                }

                public void setWendu(String wendu) {
                        this.wendu = wendu;
                }

                public String getGanmao() {
                        return ganmao;
                }

                public void setGanmao(String ganmao) {
                        this.ganmao = ganmao;
                }

                public YesterdayBean getYesterday() {
                        return yesterday;
                }

                public void setYesterday(YesterdayBean yesterday) {
                        this.yesterday = yesterday;
                }

                public List<ForecastBean> getForecast() {
                        return forecast;
                }

                public void setForecast(List<ForecastBean> forecast) {
                        this.forecast = forecast;
                }

                public static class YesterdayBean {
                        /**
                         * date : 21日星期日
                         * sunrise : 07:32
                         * high : 高温 0.0℃
                         * low : 低温 -5.0℃
                         * sunset : 17:19
                         * aqi : 66.0
                         * fx : 东南风
                         * fl : <3级
                         * type : 阴
                         * notice : 不要被阴云遮挡住好心情
                         */

                        private String date;
                        private String sunrise;
                        private String high;
                        private String low;
                        private String sunset;
                        private double aqi;
                        private String fx;
                        private String fl;
                        private String type;
                        private String notice;

                        public String getDate() {
                                return date;
                        }

                        public void setDate(String date) {
                                this.date = date;
                        }

                        public String getSunrise() {
                                return sunrise;
                        }

                        public void setSunrise(String sunrise) {
                                this.sunrise = sunrise;
                        }

                        public String getHigh() {
                                return high;
                        }

                        public void setHigh(String high) {
                                this.high = high;
                        }

                        public String getLow() {
                                return low;
                        }

                        public void setLow(String low) {
                                this.low = low;
                        }

                        public String getSunset() {
                                return sunset;
                        }

                        public void setSunset(String sunset) {
                                this.sunset = sunset;
                        }

                        public double getAqi() {
                                return aqi;
                        }

                        public void setAqi(double aqi) {
                                this.aqi = aqi;
                        }

                        public String getFx() {
                                return fx;
                        }

                        public void setFx(String fx) {
                                this.fx = fx;
                        }

                        public String getFl() {
                                return fl;
                        }

                        public void setFl(String fl) {
                                this.fl = fl;
                        }

                        public String getType() {
                                return type;
                        }

                        public void setType(String type) {
                                this.type = type;
                        }

                        public String getNotice() {
                                return notice;
                        }

                        public void setNotice(String notice) {
                                this.notice = notice;
                        }
                }

                public static class ForecastBean {
                        /**
                         * date : 22日星期一
                         * sunrise : 07:32
                         * high : 高温 -2.0℃
                         * low : 低温 -10.0℃
                         * sunset : 17:20
                         * aqi : 66.0
                         * fx : 西南风
                         * fl : <3级
                         * type : 多云
                         * notice : 阴晴之间，谨防紫外线侵扰
                         */

                        private String date;
                        private String sunrise;
                        private String high;
                        private String low;
                        private String sunset;
                        private double aqi;
                        private String fx;
                        private String fl;
                        private String type;
                        private String notice;

                        public String getDate() {
                                return date;
                        }

                        public void setDate(String date) {
                                this.date = date;
                        }

                        public String getSunrise() {
                                return sunrise;
                        }

                        public void setSunrise(String sunrise) {
                                this.sunrise = sunrise;
                        }

                        public String getHigh() {
                                return high;
                        }

                        public void setHigh(String high) {
                                this.high = high;
                        }

                        public String getLow() {
                                return low;
                        }

                        public void setLow(String low) {
                                this.low = low;
                        }

                        public String getSunset() {
                                return sunset;
                        }

                        public void setSunset(String sunset) {
                                this.sunset = sunset;
                        }

                        public double getAqi() {
                                return aqi;
                        }

                        public void setAqi(double aqi) {
                                this.aqi = aqi;
                        }

                        public String getFx() {
                                return fx;
                        }

                        public void setFx(String fx) {
                                this.fx = fx;
                        }

                        public String getFl() {
                                return fl;
                        }

                        public void setFl(String fl) {
                                this.fl = fl;
                        }

                        public String getType() {
                                return type;
                        }

                        public void setType(String type) {
                                this.type = type;
                        }

                        public String getNotice() {
                                return notice;
                        }

                        public void setNotice(String notice) {
                                this.notice = notice;
                        }
                }
        }
}
