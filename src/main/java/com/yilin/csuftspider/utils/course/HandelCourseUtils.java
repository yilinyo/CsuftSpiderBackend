package com.yilin.csuftspider.utils.course;

import com.yilin.csuftspider.model.response.CourseInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;

/**
 * Title: HandelCourseUtils
 * Description: TODO
 * 处理课表的工具类
 * @author Yilin
 * @version V1.0
 * @date 2022-10-07
 */
public class HandelCourseUtils {

    public static CourseInfo getCourseInfo(Document d){


        String[][] arr= new String[7][12];
        Element kbtable = d.getElementById("kbtable");

        Elements trs = kbtable.getElementsByTag("tr");



        for(int i=1,i1=0;i<trs.size()-1;i++,i1=i1+2) {

            Element tr = trs.get(i);

            Elements tds = tr.getElementsByTag("td");

            for(int j =0,j1=0;j<tds.size();j++,j1++) {


                Elements kbcontent = tds.get(j).getElementsByClass("kbcontent1");


                arr[j1][i1] = kbcontent.get(0).text();
                arr[j1][i1+1]=kbcontent.get(0).text();

            }




        }

        Element tr7 = trs.get(7);
        //获取备注课程信息
        String extra = tr7.getElementsByTag("td").get(0).text();



        CourseInfo courseInfo = new CourseInfo();

        courseInfo.setArr(arr);

        courseInfo.setExtra(extra);

        return courseInfo;


    }





}
