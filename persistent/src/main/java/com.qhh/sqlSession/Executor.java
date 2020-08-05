package com.qhh.sqlSession;

import com.qhh.pojo.Configuration;
import com.qhh.pojo.MappedStatement;

import java.util.List;

public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
