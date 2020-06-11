package com.wequan.bu.controller;

import com.wequan.bu.config.handler.MessageHandler;
import com.wequan.bu.repository.model.School;
import com.wequan.bu.service.SchoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Zhaochao Huang
 */
@RestController
@RequestMapping("/study_space")
@Api(tags = "School")
public class SchoolController {

    private static final Logger log = LoggerFactory.getLogger(SchoolController.class);

    @Autowired
    private MessageHandler messageHandler;
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/universities")
    @ApiOperation(value = "findAll", notes = "return a list of schools")
    public List<School> findAll(Integer pageNum, Integer pageSize){
        return schoolService.findAll(pageNum, pageSize);
    }

}
