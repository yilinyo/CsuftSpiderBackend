package com.yilin.csuftspider.utils.exame;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Exam;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: HandelExamesUtils
 * Description: TODO
 * 处理考试成绩工具类
 * @author Yilin
 * @version V1.0
 * @date 2022-09-28
 */
public class HandelExamesUtils {

    //考试信息处理工具方法

    public static List<Exam> getExameList(Document d){


        List<Exam> examList = new ArrayList<>();

        Element table  = d.getElementById("dataList");

        Elements trs = table.getElementsByTag("tr");

        if(trs == null || "未查询到数据".equals(trs.get(1).text())){

            throw new BusinessException(ErrorCode.NO_DATA,"暂时没有数据");
        }

        int num = trs.size();

        for(int i = 0;i < num;i++){


            if(i >= 1){

                Exam exam = new Exam();

                Element tr = trs.get(i);

                Elements tds = tr.getElementsByTag("td");

                //编号
                exam.setId(Integer.valueOf(tds.get(0).text()));

                //名称
                exam.setName(tds.get(3).text());

                //时间
                exam.setDate(tds.get(4).text());

                //地点
                exam.setLocation(tds.get(5).text());

                examList.add(exam);

            }


        }

        return examList;




    }



}
