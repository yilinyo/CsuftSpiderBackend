package com.yilin.csuftspider.service.impl;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.service.EvaluationService;
import com.yilin.csuftspider.utils.Session;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**
 * Title: EvaluationServiceimpl
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-03-08
 */
@Service
public class EvaluationServiceimpl implements EvaluationService {

//    @Resource
//    Executor threadPoolTaskExecutor;
    @Override
    public List<String> getNums(Session mySession) {

        //拿到 学期评教 首页
        String s = mySession.get(UrlConstant.EVALUATION_URL);



        Document d = Jsoup.parse(s);


        Elements titles = d.getElementsByTag("title");

        if (titles == null || (!"学生评价".equals(titles.get(0).text()))) {

            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评教信息错误！");
        }
        Elements nsb_r_list_nsb_table = d.getElementsByClass("Nsb_r_list Nsb_table");

        if (nsb_r_list_nsb_table == null) {

            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评教信息错误！");

        }

        List<String> urlLists = new ArrayList<>();

        Element element = nsb_r_list_nsb_table.get(0);
        //学期评教列表
        Elements as = element.getElementsByTag("a");


        //遍历每个学期的评教
        for (Element a : as) {

            String evaUrl = a.attr("href");

            HashMap<String, String> hm = new HashMap<>();
//            hm.put("pageIndex","3");
            //拿到 每个学期具体老师课程评教
            s = mySession.post(UrlConstant.BASE_URL + evaUrl, hm);
//            System.out.println(s);
            d = Jsoup.parse(s);
            //拿页数信息
            Elements page = d.getElementsByClass("Nsb_r_list_fy3");
            String pageInfo = page.get(0).text();



            int first = pageInfo.indexOf("共");
            int last = pageInfo.indexOf("页");

            int pageNum = Integer.parseInt(pageInfo.substring(first+1,last));


            doAdd(d, urlLists);
            //第一页以后的页面
            for(int i =2;i<=pageNum;i++){

                 hm = new HashMap<>();
            hm.put("pageIndex",String.valueOf(i));
                //拿到 每个学期具体老师课程评教
                s = mySession.post(UrlConstant.BASE_URL + evaUrl, hm);

                d = Jsoup.parse(s);

                doAdd(d,urlLists);

            }




        }

        return urlLists;
    }

    //提交 评教
    @Override
    public boolean commit(Session mySession, String text) {
        List<String> evaluations = getNums(mySession);


        for(String url:evaluations){
            //目标url
            String targetUrl = UrlConstant.BASE_URL + url;




                boolean isFinished = mainEvaluate(mySession, targetUrl, text,"1");



                if(!isFinished){
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评教信息错误！重试");
                }






        }

        //重试执行 防止失败提交卡顿

        evaluations = getNums(mySession);


        for(String url:evaluations){
            //目标url
            String targetUrl = UrlConstant.BASE_URL + url;




            boolean isFinished = mainEvaluate(mySession, targetUrl, text,"0");

             isFinished = mainEvaluate(mySession, targetUrl, text,"1");

            if(!isFinished){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评教信息错误！重试");
            }






        }

        return true;

    }

    private boolean mainEvaluate(Session s,String url,String text,String number){


        String s1 = s.get(url);

        Document d = Jsoup.parse(s1);

        //获取表单的信息
        Elements inputs = d.getElementsByTag("input");
        List<Pair<String,String>> params  =new ArrayList<>();



        for(Element input:inputs){
            String name = input.attr("name");

            String value = input.attr("value");

            if("issubmit".equals(name)){
                value  = number;
            }

            params.add(new Pair<>(name,value));





        }

        params.add(new Pair<>("jynr",text));


        s.postByOrderParams(UrlConstant.EVALUATION_COMMIT_URL, params);

        //睡眠 500ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return  true;


    }

    // 自动翻页计算
    private void doAdd(Document d, List<String> urlLists) {
        Element element;
        Elements nsb_r_list_nsb_table;
        //进入 评教 老师选项 页面 第一页
        nsb_r_list_nsb_table = d.getElementsByClass("Nsb_r_list Nsb_table");
        if (nsb_r_list_nsb_table == null) {

            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "评教信息错误！");

        }

        element = nsb_r_list_nsb_table.get(0);

        Elements trs = element.getElementsByTag("tr");

        int len = trs.size();

        for (int i = 1; i < len; i++) {
            //遍历每一个 课程
            Element tr1 = trs.get(i);


            Elements tds = tr1.getElementsByTag("td");
            //未评教
            if ("否".equals(tds.get(7).text())) {

                Elements a1 = tds.get(8).getElementsByTag("a");

                String href = a1.get(0).attr("href");

                int j = href.indexOf("'");
                int k = href.lastIndexOf("'");
                //具体没门课程的提交评教页面 url
                String url = href.substring(j + 1, k);

                urlLists.add(url);


            }


        }
    }
}
