package com.wup.write2database;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wup.write2database.entity.TestQuestion;
import com.wup.write2database.service.TestQuestionService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("all")
class Write2databaseApplicationTests {
    //爬取试题需要的参数
    static final int NUMBER_OF_QUESTION = 1723;
    //爬取试题需要的参数
    static final String EXAM_VERSION = "2023100501";
    static String[] TESTCODE = new String[NUMBER_OF_QUESTION];
    //试题对应的code数组的类路径下的目录
    static final String QUESTION_CODE_FILE_PATH = "src/main/resources/static/questionCode.txt";
    //试题图片数据保存的本地地址
    static final String IMG_LOCAL_DES = "/Users/brainwu/Desktop/java/DriveLicenseTest/paper_img";

    @Autowired
    private TestQuestionService testQuestionService;

    @BeforeEach
    public void initTestCode() throws IOException {
        Path path = Paths.get(QUESTION_CODE_FILE_PATH);
        byte[] data = Files.readAllBytes(path);
        String codes = new String(data, StandardCharsets.UTF_8);
        TESTCODE = codes.split(",");
    }

    //构造http请求
    public String creatGetUrl(int index) {
        return "https://tkdata.mnks.cn/ExamData/" + TESTCODE[index] + ".json?CALL=?" + EXAM_VERSION + ".json";
    }

    @Test
    @SuppressWarnings("all")
    void testWrite2Mysql() throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
            HttpGet request = new HttpGet(creatGetUrl(i));
            CloseableHttpResponse response = httpClient.execute(request);
            String res = EntityUtils.toString(response.getEntity());
            //解码网页返回的字符串成utf8格式
            String resDecode = StringEscapeUtils.unescapeJava(res);
            byte[] bytes = resDecode.getBytes(StandardCharsets.UTF_8);
            String json = new String(bytes, StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONUtil.parseObj(json);
            //将数据储存到数据库
            TestQuestion testQuestion = new TestQuestion();
            testQuestion.setPicUrl(jsonObject.getStr("tv"));
            testQuestion.setAnswer(jsonObject.getStr("da"));
            testQuestion.setCode(jsonObject.getStr("code"));
            testQuestion.setKeyWords(jsonObject.getStr("tags"));
            testQuestion.setType(Integer.valueOf(jsonObject.getStr("tx")));
            testQuestion.setStar(Integer.valueOf(jsonObject.getStr("star")));
            testQuestion.setErrRate(Double.valueOf(jsonObject.getStr("errRate")));
            //删除question中的前端格式代码
            String question = jsonObject.getStr("tm");
            testQuestion.setQuestion(question.replaceAll("<br/>", " "));
            testQuestionService.save(testQuestion);
        }
    }

    //文件下载到本地同时更改文件名为[code].jpg
    public void renameFile(String oldPath, String newPath) {
        new File(oldPath).renameTo(new File(newPath));
    }

    //下载试题对应的pic到本地
    @Test
    void downloadImgFile() {
        List<TestQuestion> testQuestionList = testQuestionService.list();
        testQuestionList.forEach(testQuestion -> {
            String picUrl = testQuestion.getPicUrl();
            if (picUrl != null && !picUrl.isEmpty()) {
                HttpUtil.downloadFileFromUrl(picUrl, IMG_LOCAL_DES);
                String[] split = picUrl.split(".cn/");
                String oldPath = IMG_LOCAL_DES + "/" + split[1];
                String newPath = IMG_LOCAL_DES + "/" + testQuestion.getCode() + ".jpg";
                renameFile(oldPath, newPath);
            }
        });
    }
}