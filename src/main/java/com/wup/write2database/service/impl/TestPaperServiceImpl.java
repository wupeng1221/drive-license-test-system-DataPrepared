package com.wup.write2database.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wup.write2database.entity.TestPaper;
import com.wup.write2database.service.TestPaperService;
import com.wup.write2database.mapper.TestPaperMapper;
import org.springframework.stereotype.Service;

/**
* @author brainwu
* @description 针对表【test_paper(科目一试题表)】的数据库操作Service实现
* @createDate 2023-12-15 22:34:26
*/
@Service
public class TestPaperServiceImpl extends ServiceImpl<TestPaperMapper, TestPaper>
    implements TestPaperService{

}




