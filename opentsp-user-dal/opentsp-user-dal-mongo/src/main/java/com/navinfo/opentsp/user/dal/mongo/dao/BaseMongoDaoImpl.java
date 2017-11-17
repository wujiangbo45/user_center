package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.BaseMongoDao;
import com.navinfo.opentsp.user.dal.dao.Identified;
import ognl.Ognl;
import ognl.OgnlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 基础dao
 * <p>
 * Created by wupeng on 10/24/15.
 */
public abstract class BaseMongoDaoImpl<T extends Identified<ID>, ID> implements BaseMongoDao<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(BaseMongoDaoImpl.class);

    private static final String primaryField_key = "pk_";

    private String id;

    @Autowired
    protected MongoTemplate mongoTemplate;

    private Class<T> clazz;

    private final Map<String, Field> fieldMap = new HashMap<>();

    public BaseMongoDaoImpl() {
        Type type = this.getClass().getGenericSuperclass();
        clazz = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        this.parse();
    }

    public BaseMongoDaoImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.parse();
    }

    private void parse() {
        logger.info("loading class : {}", clazz);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            if (AnnotationUtils.getAnnotation(field, Id.class) != null) {
                fieldMap.put(primaryField_key, field);
                id = field.getName();
            } else if (field.getName().equals("id")) {
                fieldMap.put(primaryField_key, field);
                id = field.getName();
            }
        }

        if (StringUtils.isEmpty(id)) {
            throw new IllegalArgumentException("can not found Id from class : {}" + clazz);
        }
    }

    @Override
    public ID save(T entity) {
        mongoTemplate.save(entity);
        return entity.getId();
    }

    @Override
    public T findById(ID id) {
        return this.mongoTemplate.findById(id, clazz);
    }

    @Override
    public int updateById(T entity) {
        try {
            Query query = new Query(new Criteria().and(id).is(entity.getId()));
            Update update = this.update(entity, false);
            return this.mongoTemplate.updateFirst(query, update, clazz).getN();
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }

        return 0;
    }

    private Update update(T entity, boolean selective) throws OgnlException {
        Update update = new Update();
        for (Field field : fieldMap.values()) {
            String name = field.getName();
            Object obj = Ognl.getValue(name, entity);

            if (obj == null && selective)
                continue;

            update.set(name, obj);
        }

        return update;
    }

    @Override
    public int updateByIdSelective(T entity) {
        try {
            Query query = new Query(new Criteria().and(id).is(entity.getId()));
            Update update = this.update(entity, true);
            return this.mongoTemplate.updateFirst(query, update, clazz).getN();
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int deleteById(ID idValue) {
        Query query = new Query(new Criteria().and(id).is(idValue));
        return this.mongoTemplate.remove(query, clazz).getN();
    }

    public Criteria criteria(T example, boolean selective) throws OgnlException {
        Criteria criteria = new Criteria();

        for (Field field : fieldMap.values()) {
            String name = field.getName();
            Object obj = Ognl.getValue(name, example);

            if (obj == null && selective)
                continue;

            criteria.and(name).is(obj);
        }

        return criteria;
    }

    @Override
    public List<T> findByExample(T example) {
        try {
            return this.mongoTemplate.find(new Query(this.criteria(example, false)), clazz);
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }

        return Collections.<T>emptyList();
    }

    @Override
    public List<T> findByExampleSelective(T example) {
        try {
            return this.mongoTemplate.find(new Query(this.criteria(example, true)), clazz);
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }

        return Collections.<T>emptyList();
    }

    protected Query andQuery(List<QueryItem> items) {
        Criteria criteria = new Criteria();
        for (QueryItem item : items) {
            criteria.and(item.getField()).is(item.getValue());
        }

        return new Query(criteria);
    }

    protected MongoTemplate mongoTemplate() {
        return this.mongoTemplate;
    }

    protected class QueryBuilder {
        private List<QueryItem> items;

        public QueryBuilder() {
            items = new LinkedList<>();
        }

        public QueryBuilder(int length) {
            items = new ArrayList<>(length);
        }

        public QueryBuilder addQueryItem(String field, Object value) {
            items.add(new QueryItem(field, value));
            return this;
        }

        public Query build() {
            return andQuery(items);
        }

    }

    protected class QueryItem {
        private String field;
        private Object value;

        public QueryItem(String field, Object value) {
            this.field = field;
            this.value = value;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
