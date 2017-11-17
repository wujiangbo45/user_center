package com.navinfo.opentsp.user.service.resultdto.backend;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-02
 * @modify
 * @copyright Navi Tsp
 */
public class QueryStaticsResult {

    private List<StaticsItem> series = new LinkedList<>();

    private List<String> xAxis = new LinkedList<>();

    private String yAxisTitle;

    private String headTitle;

    public void addSeries(StaticsItem item) {
        this.series.add(item);
    }

    public void addXAxis(String xAxisName) {
        this.xAxis.add(xAxisName);
    }

    public List<StaticsItem> getSeries() {
        return series;
    }

    public void setSeries(List<StaticsItem> series) {
        this.series = series;
    }

    public List<String> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<String> xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxisTitle() {
        return yAxisTitle;
    }

    public void setyAxisTitle(String yAxisTitle) {
        this.yAxisTitle = yAxisTitle;
    }

    public String getHeadTitle() {
        return headTitle;
    }

    public void setHeadTitle(String headTitle) {
        this.headTitle = headTitle;
    }

    public static class StaticsItem {
        private String name;
        private List<Integer> data = new LinkedList<>();

        public StaticsItem (){}

        public StaticsItem (String name, List<Integer> data){
            this.name = name;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Integer> getData() {
            return data;
        }

        public void addData(Integer integer) {
            this.data.add(integer);
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

}
