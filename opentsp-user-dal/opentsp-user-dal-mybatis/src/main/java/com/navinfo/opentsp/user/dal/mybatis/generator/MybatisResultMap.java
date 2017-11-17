package com.navinfo.opentsp.user.dal.mybatis.generator;

import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

import java.sql.JDBCType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * TODO  need tobe implement
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
public class MybatisResultMap {

    private List<ResultMapEntry> properties = new ArrayList<>();
    private String pojoName;
    private String id;

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public List<ResultMapEntry> getProperties() {
        return properties;
    }

    public void setProperties(List<ResultMapEntry> properties) {
        this.properties = properties;
    }

    public void addEntry(ResultMapEntry entry){
        properties.add(entry);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<resultMap id=\"").append(this.getId()).append("\" type=\"").append(this.getPojoName()).append("\">");
        for(ResultMapEntry entry : this.getProperties()) {
            sb.append("<result column=\"").append(entry.getColumn()).append("\" property=\"").append(entry.getProperty()).append("\" ");
            if(!StringUtils.isEmpty(entry.getJdbcType()))
                sb.append(" jdbcType=\"").append(entry.getJdbcType().getName()).append("\" ");
            sb.append("/>");
        }
        sb.append("</resultMap>");
        return sb.toString();
    }

    public static class ResultMapEntry {
        private String property;
        private String column;
        private JDBCType jdbcType;
        private String javaType;
        private TypeHandler typeHandler;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public JDBCType getJdbcType() {
            return jdbcType;
        }

        public void setJdbcType(JDBCType jdbcType) {
            this.jdbcType = jdbcType;
        }

        public String getJavaType() {
            return javaType;
        }

        public void setJavaType(String javaType) {
            this.javaType = javaType;
        }

        public TypeHandler getTypeHandler() {
            return typeHandler;
        }

        public void setTypeHandler(TypeHandler typeHandler) {
            this.typeHandler = typeHandler;
        }
    }
}
