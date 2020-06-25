package com.wequan.bu.repository.dao;

import com.wequan.bu.repository.model.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Zhaochao Huang
 */
public interface TopicMapper extends GeneralMapper<Topic> {
    List<Topic> selectByIds(@Param("ids") String ids);
}
