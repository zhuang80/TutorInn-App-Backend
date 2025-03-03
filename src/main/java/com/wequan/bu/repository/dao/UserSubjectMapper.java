package com.wequan.bu.repository.dao;

import com.wequan.bu.repository.model.UserSubject;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChrisChen
 */
@Mapper
public interface UserSubjectMapper extends GeneralMapper<UserSubject>{

    /**
     * 根据用户id删除subjects
     * @param userId 用户id
     */
    void deleteByUserId(Integer userId);
}