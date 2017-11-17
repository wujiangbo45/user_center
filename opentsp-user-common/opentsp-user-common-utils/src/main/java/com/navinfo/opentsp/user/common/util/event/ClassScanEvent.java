package com.navinfo.opentsp.user.common.util.event;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
public class ClassScanEvent extends AbstractOpentspEvent<Class> {

    private Class clazz;

    public ClassScanEvent(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class getData() {
        return clazz;
    }
}
