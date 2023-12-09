package com.wup.write2database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wup.write2database.entity.TestQuestion;
import com.wup.write2database.service.TestQuestionService;
import com.wup.write2database.mapper.TestQuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author brainwu
* @description 针对表【test_paper(科目一试题表)】的数据库操作Service实现
* @createDate 2023-12-09 15:43:13
*/
@Service
public class TestQuestionServiceImpl extends ServiceImpl<TestQuestionMapper, TestQuestion>
    implements TestQuestionService{

}




