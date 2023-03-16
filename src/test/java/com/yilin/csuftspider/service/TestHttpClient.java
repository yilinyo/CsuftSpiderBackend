package com.yilin.csuftspider.service;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.constant.ConstantData;
import com.yilin.csuftspider.constant.UrlConstant;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Notice;
import com.yilin.csuftspider.utils.Session;
import javafx.util.Pair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.annotation.Resource;
import java.util.*;

/**
 * Title: TestHttpClient
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2023-02-19
 */
@SpringBootTest
public class TestHttpClient {


    @Resource
    UserService userService;
    @Resource
    EvaluationService evaluationService;

    //声明request变量
    private MockHttpServletRequest request;


    private String href;

    @Test
    public void getEvaluationNums(){

        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        userService.login("学号","密码",request);
        Session s = (Session)request.getSession().getAttribute(UserService.USER_LOGIN_STATE);
        System.out.println(evaluationService.getNums(s).size());




    }

    @Test
    public void testEvaluation(){

        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        userService.login("学号","密码",request);

        Session s = (Session)request.getSession().getAttribute(UserService.USER_LOGIN_STATE);


        String s1 = s.get(UrlConstant.EVALUATION_URL);

        Document d= Jsoup.parse(s1);


        Elements nsb_r_list_nsb_table = d.getElementsByClass("Nsb_r_list Nsb_table");

        if(nsb_r_list_nsb_table == null){

            throw new BusinessException(ErrorCode.PARAMS_ERROR);

        }

        Element element = nsb_r_list_nsb_table.get(0);

        Elements as = element.getElementsByTag("a");


        for(Element a:as){

            String evaUrl = a.attr("href");
            System.out.println(evaUrl);
            HashMap<String,String> hm = new HashMap<>();
//            hm.put("pageIndex","2");
            System.out.println(s1 = s.post(UrlConstant.BASE_URL+evaUrl,hm));
            d = Jsoup.parse(s1);

            //进入 评教 老师选项 页面
             nsb_r_list_nsb_table = d.getElementsByClass("Nsb_r_list Nsb_table");
            if(nsb_r_list_nsb_table == null){

                throw new BusinessException(ErrorCode.PARAMS_ERROR);

            }

            element = nsb_r_list_nsb_table.get(0);

            Elements trs = element.getElementsByTag("tr");

            Element tr1 = trs.get(6);


            Elements tds = tr1.getElementsByTag("td");
//            未评教
//            if(tds.get(6).text().equals("否")){

                Elements a1 = tds.get(8).getElementsByTag("a");

                String href = a1.get(0).attr("href");

                int i = href.indexOf("'");
                int j = href.lastIndexOf("'");

                String substring = href.substring(i+1, j);

                s1 = s.get(UrlConstant.BASE_URL + substring);

               d = Jsoup.parse(s1);

               //获取表单的信息
                Elements inputs = d.getElementsByTag("input");

                List<Pair<String,String>> params  =new ArrayList<>();



                for(Element input:inputs){
                    String name = input.attr("name");

                    String value = input.attr("value");

                    if("issubmit".equals(name)){
                        value  = "1";
                    }

                    params.add(new Pair<>(name,value));





                }

                 params.add(new Pair<>("jynr","讲述内容充实，信息量大，能反映或联系学科发展的新思想、新概念、新成果2。"));







            s1 = s.postByOrderParams(UrlConstant.EVALUATION_COMMIT_URL, params);



            System.out.println(s1);




            }



        }



//    }







    @Test
    public void test(){

        Session session = new Session();

        String txt = session.get(UrlConstant.BASE_NOTICE_URL);


        Document parse = Jsoup.parse(txt);

        Elements title = parse.getElementsByTag("title");

        if(title == null || (!"教务处-通知公告-".equals(title.get(0).text()))){

            throw new BusinessException(ErrorCode.PARAMS_ERROR,"学校教务系统正在维护，请稍后再重试");
        }




        Elements rt_detail = parse.getElementsByClass("rt detail");

        Elements lis = rt_detail.get(0).getElementsByTag("li");








        for(Element li:lis){

            Notice notice = new Notice();
            Element a = li.getElementsByTag("a").get(0);


            String href = a.attr("href");

            String link = href.replaceFirst("./", UrlConstant.BASE_NOTICE_URL);

            String context = a.text();

            String date = li.getElementsByTag("span").get(0).text();

            notice.setContext(context);
            notice.setLink(link);

            notice.setDate(date);
            System.out.println(notice);

        }





    }
}
