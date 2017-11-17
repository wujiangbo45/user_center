package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.common.util.event.ClassScanEvent;
import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by wupeng on 11/3/15.
 */
@Component
public class ClassValidatorDispatcher extends AbstractOpentspListener<ClassScanEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ClassValidatorDispatcher.class);

//    private final Map<Class, List<OpentspClassValidator>> listMap = new HashMap<>();
//    private final Map
//
//    @Autowired
//    public ClassValidatorDispatcher(List<OpentspClassValidator> implmentors) {
//        for (OpentspClassValidator implmentor : implmentors) {
//            List<OpentspClassValidator> list = listMap.get(implmentor.support());
//            if (list == null) {
//                list = new LinkedList<>();
//                listMap.put(implmentor.support(), list);
//            }
//
//            list.add(implmentor);
//        }
//
//    }




    @Override
    public void onEvent(ClassScanEvent event) {

    }
}
