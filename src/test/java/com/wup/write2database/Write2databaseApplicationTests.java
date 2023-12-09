package com.wup.write2database;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wup.write2database.entity.TestPaper;
import com.wup.write2database.service.TestPaperService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    private TestPaperService testPaperService;

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
            TestPaper testPaper = new TestPaper();
            testPaper.setPicUrl(jsonObject.getStr("tv"));
            testPaper.setAnswer(jsonObject.getStr("da"));
            testPaper.setCode(jsonObject.getStr("code"));
            testPaper.setKeyWords(jsonObject.getStr("tags"));
            testPaper.setType(Integer.valueOf(jsonObject.getStr("tx")));
            testPaper.setStar(Integer.valueOf(jsonObject.getStr("star")));
            testPaper.setErrRate(Double.valueOf(jsonObject.getStr("errRate")));
            //删除question中的前端格式代码
            String question = jsonObject.getStr("tm");
            testPaper.setQuestion(question.replaceAll("<br/>", " "));
            testPaperService.save(testPaper);
        }
    }

    //文件下载到本地同时更改文件名为[code].jpg
    public void renameFile(String oldPath, String newPath) {
        new File(oldPath).renameTo(new File(newPath));
    }

    //下载试题对应的pic到本地
    @Test
    void downloadImgFile() {
        List<TestPaper> testQuestionList = testPaperService.list();
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
    //获取解析
    @Test
    void testGetAnalysis() {
        for (int i = 0; i < NUMBER_OF_QUESTION; i++) {
            String analysisUrl = "https://tiba.jsyks.com/Post/" + TESTCODE[i] +".htm";
            String analysisHtml = HttpUtil.get(analysisUrl);
            Document document = Jsoup.parse(analysisHtml);
            // html中解析的容器id是‘answer’
            Element element = document.getElementById("answer");
            // 这一部分的html很简单，可以观察到解析部分被标签<h2>包围，因此直接将返回值进行分割
            // 字符串进一步处理
            String[] split1 = element.toString().split("h2>");
            String analysis = StrUtil.removeSuffix(split1[1], "</");
            testPaperService.update(new LambdaUpdateWrapper<TestPaper>()
                    .eq(TestPaper::getCode, TESTCODE[i])
                    .set(TestPaper::getAnalysis, analysis));
        }
    }
    @Test
    void handleQuestionAndAnswer() {
        // 所有的选择题
        List<TestPaper> selectTests = testPaperService.list(new LambdaQueryWrapper<TestPaper>()
                .eq(TestPaper::getType, 2));
        selectTests.forEach(selectTest -> {
            String[] split = selectTest.getQuestion().split("A");
            String question = split[0];
            String selectItem = "A" + split[1];
            testPaperService.update(new LambdaUpdateWrapper<TestPaper>()
                    .eq(TestPaper::getId, selectTest.getId())
                    .set(TestPaper::getQuestion, question)
                    .set(TestPaper::getSelectItem, selectItem));
        });
    }
}