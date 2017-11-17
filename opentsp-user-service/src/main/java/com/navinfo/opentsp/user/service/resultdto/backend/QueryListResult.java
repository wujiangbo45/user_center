package com.navinfo.opentsp.user.service.resultdto.backend;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-02
 * @modify
 * @copyright Navi Tsp
 */
public class QueryListResult {

    private ListItem yesterday;
    private ListItem beforeYesterday;
    private ListItem total;

    public ListItem getYesterday() {
        return yesterday;
    }

    public void setYesterday(ListItem yesterday) {
        this.yesterday = yesterday;
    }

    public ListItem getBeforeYesterday() {
        return beforeYesterday;
    }

    public void setBeforeYesterday(ListItem beforeYesterday) {
        this.beforeYesterday = beforeYesterday;
    }

    public ListItem getTotal() {
        return total;
    }

    public void setTotal(ListItem total) {
        this.total = total;
    }

    public static class ListItem {
        private int web;
        private int mobile;
        private int tbox;

        public ListItem(){}

        public ListItem(int web, int mobile, int tbox){
            this.web = web;
            this.mobile = mobile;
            this.tbox = tbox;
        }

        public int getWeb() {
            return web;
        }

        public void setWeb(int web) {
            this.web = web;
        }

        public int getMobile() {
            return mobile;
        }

        public void setMobile(int mobile) {
            this.mobile = mobile;
        }

        public int getTbox() {
            return tbox;
        }

        public void setTbox(int tbox) {
            this.tbox = tbox;
        }
    }

}
