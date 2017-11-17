package com.navinfo.opentsp.user.common.util.event;

import com.navinfo.opentsp.user.common.util.listener.OpentspListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class EventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final Map<Class<?>, List<OpentspListener<?>>> listeners;
    @Autowired
    private ExecutorService executorService;

    @Autowired
    public EventPublisher(List<OpentspListener<?>> listenerList){
        Map<Class<?>, List<OpentspListener<?>>> map = new HashMap<>();

        for(OpentspListener listener : listenerList) {
            logger.info("loading listener : {}", listener.getClass());
            Class clazz = null;
            //获取到泛型的事件类型
            Type[] interfaces = listener.getClass().getGenericInterfaces();
            Type superClasses = listener.getClass().getGenericSuperclass();

            for(Type type : interfaces) {
                if((type instanceof Class) && (OpentspListener.class.isAssignableFrom((Class<?>) type))) {
                    clazz = (Class) type;
                } else if((type instanceof ParameterizedType) && ((ParameterizedType) type).getRawType().getTypeName().equals(OpentspListener.class.getName())) {
                    type = ((ParameterizedType) type).getActualTypeArguments()[0];
                    clazz = (Class) type;
                }
            }

            if (superClasses != null && clazz == null) {
                Type type = ((ParameterizedType) superClasses).getActualTypeArguments()[0];
                clazz = (Class) type;
            }

            if(clazz == null) {
                logger.error("error found event for listener : {}", listener.getClass());
                continue;
            }

            List<OpentspListener<?>> list = map.get(clazz);
            if(list == null) {
                list = new LinkedList<>();
                map.put(clazz, list);
            }

            list.add(listener);
        }

        ListenerComparator comparator = new ListenerComparator();
        for(Map.Entry<Class<?>, List<OpentspListener<?>>> entry : map.entrySet()) {
            Collections.sort(entry.getValue(), comparator);
        }

        listeners = Collections.unmodifiableMap(map);
    }

    /**
     * 发布事件
     * @param opentspEvent
     */
    public void publishEvent(OpentspEvent<?> opentspEvent) {
        Class clazz = opentspEvent.getClass();
        logger.info("publish event : {}", clazz);

        List<OpentspListener<?>> opentspListeners = listeners.get(clazz);
        if(opentspListeners == null || opentspListeners.size() == 0) {
            logger.warn("no listener found for event : {}", clazz);
            return;
        }

        for(OpentspListener listener : opentspListeners) {
            if(listener.async()) { // 异步的
                logger.info("call async listener : {}", listener.getClass());
                this.executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        listener.onEvent(opentspEvent);
                    }
                });
            } else {
                logger.info("call sync listener : {}", listener.getClass());
                listener.onEvent(opentspEvent);
            }
        }

        logger.info("publish event success ! {}", clazz);
    }

    static class ListenerComparator implements Comparator<OpentspListener<?>> {
        @Override
        public int compare(OpentspListener<?> o1, OpentspListener<?> o2) {
            Assert.notNull(o1);
            Assert.notNull(o2);

            if(o1 == o2)
                return 0;

            if(o1.equals(o2)) {
                return 0;
            }

            return o1.order() - o2.order();
        }
    }

}
