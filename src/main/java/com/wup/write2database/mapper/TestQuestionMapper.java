package com.wup.write2database.mapper;

import com.wup.write2database.entity.TestQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author brainwu
* @description 针对表【test_paper(科目一试题表)】的数据库操作Mapper
* @createDate 2023-12-09 15:43:13
* @Entity com.wup.write2database.entity.TestQuestion
*/
@Mapper
public interface TestQuestionMapper extends BaseMapper<TestQuestion> {

}




