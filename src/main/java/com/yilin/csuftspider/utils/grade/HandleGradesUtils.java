package com.yilin.csuftspider.utils.grade;

import com.yilin.csuftspider.common.ErrorCode;
import com.yilin.csuftspider.exception.BusinessException;
import com.yilin.csuftspider.model.domain.Grade;
import com.yilin.csuftspider.model.domain.GradeAnalysis;
import com.yilin.csuftspider.model.domain.LevelGrade;
import com.yilin.csuftspider.model.response.GradeAnalysisInfo;
import com.yilin.csuftspider.model.response.GradesInfo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Title: HandleGradesUtils
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-09-27
 */
@Slf4j
public class HandleGradesUtils {


      static class Info {

        public Double gpa;

        public Double apf;

          public Double credit;

          public Double basicPoint;


         public Info(Double gpa, Double apf,Double credit) {
             this.gpa = gpa;
             this.apf = apf;
             this.credit = credit;
         }

          public Info(Double gpa, Double apf, Double credit, Double basicPoint) {
              this.gpa = gpa;
              this.apf = apf;
              this.credit = credit;
              this.basicPoint = basicPoint;
          }
      }


    //封装 处理成绩列表工具
    public static List<Grade> getGradeList(Document d){

        Element table  = d.getElementById("dataList");

//        Element tbody = table.getElementsByTag("tbody").get(0);
        Elements trs = table.getElementsByTag("tr");

        if(trs == null){

            throw new BusinessException(ErrorCode.NO_DATA,"暂时没有数据");
        }

        List<Grade> gradeList = new ArrayList<>();





        for(Element el : trs){

            Grade grade = new Grade();
            //取td标签 集
            Elements tds = el.getElementsByTag("td");

            if(tds == null){

                throw new BusinessException(ErrorCode.NO_DATA,"暂时没有数据");
            }

            int tds_len = tds.size();
            for(int i = 0 ; i < tds_len ;i++){

                if(i == 0){
                    //编号
                    if("未查询到数据".equals(tds.get(i).text())){
                        throw new BusinessException(ErrorCode.NULL_ERROR,"如果你不是新生请前往教务系统完成教师评教才能正常查询");
                    }
                    grade.setId(Integer.valueOf(tds.get(i).text()));
                }
                else if(i == 1){
                    //学期
                    grade.setTerm(tds.get(i).text());
                }
                else if(i == 3){
                    //课程名称
                    grade.setCourseName(tds.get(i).text());

                }
                else if(i == 4){
                    String text = tds.get(i).text();


                    //课程分数 以字符串存储考虑 不是数字
                    grade.setGrade(text);
                }
                else if(i == 5){
                    //课程学分
                    grade.setCredit(Double.valueOf(tds.get(i).text()));

                }
                else if(i == 7){
                    //课程绩点
                    String gpTmp = tds.get(i).text();
                    String grr = tds.get(4).text();

                    String regex = "[0-9]*\\.?[0-9]+";
                    //有些 同学 绩点 栏为空 ，需要手动计算
                   if(Pattern.matches(regex, gpTmp)){
                       grade.setGradePoint(gpTmp);
                   }else{

                       grade.setGradePoint(String.valueOf(handelGp(grr)));
                   }



                }
                else if(i == 10){

                    //考试方式

                    String text = tds.get(i).text();
                    if("正常考试".equals(text)){

                        grade.setStatus(0);
                    }else{

                        grade.setStatus(1);
                    }




                }
                else if(i == 11){

                    grade.setAttribute(tds.get(i).text());
                }

            }

                gradeList.add(grade);


        }
        return gradeList;
    }

    //处理所有学期课程 (或者单学期） 成绩列表

    public static GradesInfo getGradesInfo(Document d){

        List<Grade> gradeList = getGradeList(d);
        int num = gradeList.size();
        if(num == 0){

            throw new BusinessException(ErrorCode.NULL_ERROR,"暂无数据");
        }

        Info info = getInfo(gradeList);

        Double gpa = info.gpa;
        Double apf = info.apf;
        Double basicPoint = info.basicPoint;
//        Double sumScore = 0D;
//        Double sumGp = 0D;
//        Double sumCredit = 0D;
//        int num2 = 0;
//        for(Grade g : gradeList){
//
//            if("必修".equals(g.getAttribute())){
//                //判断是否是数字
//                String text = g.getGrade();
//                String regex = "[0-9]*\\.?[0-9]+";
//                if(Pattern.matches(regex, text) && g.getStatus() == 0 &&Double.parseDouble(text)>=59.9 ) {
//
//                    Double score = Double.parseDouble(text);
//
//                    sumScore += score;
//
//                    sumGp += g.getGradePoint() * g.getCredit();
//
//                    sumCredit += g.getCredit();
//                    num2++;
//                }else{
//
//                    if(g.getStatus() == 0 && (!Pattern.matches(regex, text))){
//                        Double score = (g.getGradePoint()*10.0) + 50.0;
//                        sumScore += score;
//                        sumGp += g.getGradePoint() * g.getCredit();
//                        sumCredit += g.getCredit();
//                        num2++;
//                    }
//                    else{
//                        if(g.getStatus() == 1 && Pattern.matches(regex, text) && Double.parseDouble(text)>=59.9 ){
//                            sumScore += 60.0;
//                            sumGp += g.getGradePoint() * 1.0;
//                            sumCredit += g.getCredit();
//                            num2++;
//
//                        }
//                        else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "及格".equals(text)){
//
//                            sumScore += 60.0;
//                            sumGp += g.getGradePoint() * 1.0;
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "不及格".equals(text)){
//
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }
//                        else if(g.getStatus() == 1 && Pattern.matches(regex, text) &&  Double.parseDouble(text)<=59.9){
//
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }
//
//
//
//                    }
//
//                }
//
//            }
//
//        }




//        //百分制计算gpa 和 apf
//        double gpa = sumGp / sumCredit;
//        double apf = sumScore / (double)num2;
//
//
//        //保留三位小数
//        gpa = handelDouble(gpa);
//
//        apf = handelDouble(apf);


        GradesInfo gradesInfo = new GradesInfo(gradeList,gpa,apf,num,basicPoint);

        return gradesInfo;
    }


    //处理年度 成绩

    public static GradesInfo getAnnualGradesInfo(Document d,String year){


        //拼接字符

        String firstTerm = year + "-1";
        String secondTerm = year + "-2";


        List<Grade> gradeListTmp= getGradeList(d);
        List<Grade> gradeList = new ArrayList<>();

        int index = 0;
        for(Grade g:gradeListTmp){

            if(firstTerm.equals(g.getTerm()) || secondTerm.equals(g.getTerm()) ){

                g.setId(++index);

                gradeList.add(g);

            }


        }
        if(index == 0){

            throw new BusinessException(ErrorCode.NULL_ERROR,"暂无数据");
        }



        int num = index;

        Info info = getInfo(gradeList);

         Double gpa = info.gpa;
         Double apf = info.apf;
         Double basicPoint = info.basicPoint;

//        Double sumScore = 0D;
//        Double sumGp = 0D;
//        Double sumCredit = 0D;
//        int num2 = 0;
//
//        for(Grade g : gradeList){
//
//            if("必修".equals(g.getAttribute())){
//                //判断是否是数字
//                String text = g.getGrade();
//                String regex = "[0-9]*\\.?[0-9]+";
//                if(Pattern.matches(regex, text) && g.getStatus() == 0 &&Double.parseDouble(text)>=59.9 ) {
//
//                    Double score = Double.parseDouble(text);
//
//                    sumScore += score;
//
//                    sumGp += g.getGradePoint() * g.getCredit();
//
//                    sumCredit += g.getCredit();
//                    num2++;
//                }else{
//
//                    if(g.getStatus() == 0 && (!Pattern.matches(regex, text))){
//                        Double score = (g.getGradePoint()*10.0) + 50.0;
//                        sumScore += score;
//                        sumGp += g.getGradePoint() * g.getCredit();
//                        sumCredit += g.getCredit();
//                        num2++;
//                    }
//                    else{
//                        if(g.getStatus() == 1 && Pattern.matches(regex, text) && Double.parseDouble(text)>=59.9 ){
//                            sumScore += 60.0;
//                            sumGp += g.getGradePoint() * 1.0;
//                            sumCredit += g.getCredit();
//                            num2++;
//
//                        }
//                        else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "及格".equals(text)){
//
//                            sumScore += 60.0;
//                            sumGp += g.getGradePoint() * 1.0;
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "不及格".equals(text)){
//
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }
//                        else if(g.getStatus() == 1 && Pattern.matches(regex, text) &&  Double.parseDouble(text)<=59.9){
//
//                            sumCredit += g.getCredit();
//                            num2++;
//                        }
//
//
//
//                    }
//
//                }
//
//            }
//
//        }
//
//        //百分制计算gpa 和 apf
//        double gpa = sumGp / sumCredit;
//        double apf = sumScore / (double)num2;
//
//
//        //保留三位小数
//       gpa = handelDouble(gpa);
//
//       apf = handelDouble(apf);



        GradesInfo gradesInfo = new GradesInfo(gradeList,gpa,apf,num,basicPoint);

        return gradesInfo;



    }

    //处理 等级成绩页面document

    public static List<LevelGrade> getLevelGrades(Document d){

        List<LevelGrade> levelGradeList = new ArrayList<>();

        Element table  = d.getElementById("dataList");

        Elements trs = table.getElementsByTag("tr");

        if(trs == null || trs.size() <= 2){

            throw new BusinessException(ErrorCode.NO_DATA,"暂时没有数据");
        }

        int num = trs.size();

        for(int i = 0;i < num;i++){


            if(i >= 2){

                LevelGrade levelGrade = new LevelGrade();

                Element tr = trs.get(i);

                Elements tds = tr.getElementsByTag("td");

                //编号
                levelGrade.setId(Integer.valueOf(tds.get(0).text()));

                //等级考试名称
                levelGrade.setExamName(tds.get(1).text());

                //总分
                levelGrade.setGrade(Double.valueOf(tds.get(4).text()));

                //考试时间
                levelGrade.setDate(tds.get(8).text());


                levelGradeList.add(levelGrade);

            }


        }


        return levelGradeList;

    }


    public static GradeAnalysisInfo getAnalysisUtil(Document d){
        List<Grade> gradeList = getGradeList(d);
        int num = gradeList.size();

        List<GradeAnalysis> termList = new ArrayList<>();

        List<GradeAnalysis> yearList = new ArrayList<>();



        //以年为单位存储列表
        List<List<Grade>> lists_y = new ArrayList<>();




        List<Grade> list = new ArrayList<>();
        //当前时间
        String currentTime = gradeList.get(0).getTerm();
        String prTime = currentTime.substring(0,4);
        //正则构造
        String regex = prTime + ".*";

        //学分



        for(int i=0;i<num;i++) {
            Grade g = gradeList.get(i);
            currentTime = g.getTerm();
            prTime = currentTime.substring(0,4);

                if(Pattern.matches(regex, currentTime)&&"必修".equals(g.getAttribute())){

                    list.add(g);

                }else if("必修".equals(g.getAttribute())){

                    lists_y.add(list);
                    list = new ArrayList<>();
                    regex =  prTime +".*";
                    list.add(g);

                }

                if(i == num-1){

                    lists_y.add(list);
                }

        }
        //以学期为单位构造列表
        List<List<Grade>> lists_t = new ArrayList<>();



         list = new ArrayList<>();
        //当前时间
         currentTime = gradeList.get(0).getTerm();

        regex = currentTime;
        for(int i=0;i<num;i++) {
            Grade g = gradeList.get(i);
            currentTime = g.getTerm();


            if(currentTime.equals(regex)&&"必修".equals(g.getAttribute())){

                list.add(g);

            }else if("必修".equals(g.getAttribute())){

                lists_t.add(list);
                list = new ArrayList<>();
                regex =  currentTime;
                list.add(g);

            }

            if(i == num-1){

                lists_t.add(list);
            }

        }
        //year
        for(int i =0 ;i < lists_y.size();i++){


            GradeAnalysis gradeAnalysis = new GradeAnalysis();
            List<Grade> listTmp = lists_y.get(i);
            Info info = getInfo(listTmp);

            gradeAnalysis.setGpa(info.gpa);
            gradeAnalysis.setApf(info.apf);
            gradeAnalysis.setTime(listTmp.get(0).getTerm().substring(0,9));

            yearList.add(gradeAnalysis);


        }
        //term
        for(int i =0 ;i < lists_t.size();i++){


            GradeAnalysis gradeAnalysis = new GradeAnalysis();
            List<Grade> listTmp = lists_t.get(i);
            Info info = getInfo(listTmp);

            gradeAnalysis.setGpa(info.gpa);
            gradeAnalysis.setApf(info.apf);
            gradeAnalysis.setTime(listTmp.get(0).getTerm());

            termList.add(gradeAnalysis);


        }



        Info info = getInfo(gradeList);



        GradeAnalysisInfo gradeAnalysisInfo =new GradeAnalysisInfo();

        gradeAnalysisInfo.setTermList(termList);
        gradeAnalysisInfo.setYearList(yearList);
        gradeAnalysisInfo.setAllGpa(info.gpa);
        gradeAnalysisInfo.setAllApf(info.apf);
        gradeAnalysisInfo.setAllCredit(info.credit);

        return gradeAnalysisInfo;


    }

    public static Info getInfo(List<Grade> list){


        int num = list.size();

        //这里算平均绩点和平均分
        Double sumScore = 0D;
        Double sumGp = 0D;
        Double sumCredit = 0D;
        int num2 = 0;


        //这里是算学业基本分
        Double sumScore2 = 0D;
        Double sumCredit2 = 0D;





        for(Grade g : list){

            if("必修".equals(g.getAttribute())){

                if(g.getStatus()==0){

                    String grade = g.getGrade();

                    Double gradeD = 0D;

                    if("不及格".equals(grade)){
                        gradeD = 50.0;
                    }else if("及格".equals(grade)){

                        gradeD = 60.0;

                    }else if("中".equals(grade)){

                        gradeD = 70.0;

                    }else if("良".equals(grade)){

                        gradeD = 80.0;

                    }else if("优".equals(grade)){

                        gradeD = 90.0;

                    }else{

                        gradeD = Double.parseDouble(grade);

                    }


                    sumScore2 =sumScore2 + (gradeD * g.getCredit());

                    sumCredit2+=g.getCredit();







                }










                //正常的gpa、apf计算



                //判断是否是数字
                String text = g.getGrade();
                String regex = "[0-9]*\\.?[0-9]+";
                if(Pattern.matches(regex, text) && g.getStatus() == 0 &&Double.parseDouble(text)>=59.9 ) {

                    Double score = Double.parseDouble(text);

                    sumScore += score;

                    String gradePoint = g.getGradePoint();

                    Double gp = 0D;

                    if(!Pattern.matches(regex, gradePoint)){
                        gp = handelGp(text);
                    }
                    else{
                        gp = Double.parseDouble(gradePoint);
                    }


                    sumGp += gp * g.getCredit();

                    sumCredit += g.getCredit();
                    num2++;
                }else{

                    if(g.getStatus() == 0 && (!Pattern.matches(regex, text))){

                        String gradePoint = g.getGradePoint();

                        Double gp = 0D;

                        if(!Pattern.matches(regex, gradePoint)){
                            gp = handelGp(text);
                        }
                        else{
                            gp = Double.parseDouble(gradePoint);
                        }

                        Double score = (gp*10.0) + 50.0;
                        sumScore += score;
                        sumGp += gp * g.getCredit();
                        sumCredit += g.getCredit();
                        num2++;
                    }
                    else{
                        if(g.getStatus() == 1 && Pattern.matches(regex, text) && Double.parseDouble(text)>=59.9 ){
                            sumScore += 60.0;
                            String gradePoint = g.getGradePoint();

                            Double gp = 0D;

                            if(!Pattern.matches(regex, gradePoint)){
                                gp = handelGp(text);
                            }
                            else{
                                gp = Double.parseDouble(gradePoint);
                            }


                            sumGp += gp * 1.0;
                            sumCredit += g.getCredit();
                            num2++;

                        }
                        else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "及格".equals(text)){

                            String gradePoint = g.getGradePoint();

                            Double gp = 0D;

                            if(!Pattern.matches(regex, gradePoint)){
                                gp = handelGp(text);
                            }
                            else{
                                gp = Double.parseDouble(gradePoint);
                            }

                            sumScore += 60.0;
                            sumGp += gp * 1.0;
                            sumCredit += g.getCredit();
                            num2++;
                        }else if(g.getStatus() == 1 && !Pattern.matches(regex, text) && "不及格".equals(text)){
                            sumCredit += g.getCredit();
                        num2++;
                        }
                        else if(g.getStatus() == 1 && Pattern.matches(regex, text) &&  Double.parseDouble(text)<=59.9){
                            sumCredit += g.getCredit();
                         num2++;
                        }



                    }

                }

            }

            }
        //百分制计算gpa 和 apf
        double gpa = sumGp / sumCredit;
        double apf = sumScore / (double)num2;

        double basicPoint = sumScore2 / sumCredit2;


        //保留三位小数
        gpa = handelDouble(gpa);

        apf = handelDouble(apf);


        basicPoint = handelDouble(basicPoint);

        return new Info(gpa,apf,sumCredit,basicPoint);





    }

//根据成绩返回Gp 工具类
    public static double handelGp(String grade){

        String regex = "[0-9]*\\.?[0-9]+";

        if("不及格".equals(grade)){
           return 0;
        }else if("及格".equals(grade)){

            return 1.0;

        }else if("中".equals(grade)){

            return 2.0;

        }else if("良".equals(grade)){

           return 3.0;

        }else if("优".equals(grade)){

            return 4.0;

        }else if("合格".equals(grade)){

            return 1.0;

        }else if(Pattern.matches(regex, grade)){

            double gradeTmp =  handelDouble( (Double.parseDouble(grade) - 50.0) / 10.0 );
            // 小于 0.1 直接 近似为 0
            if(gradeTmp <= 0.1){
                return  0;
            }
            return gradeTmp;
        }else{
            log.info("额外成绩样式:" + grade);
            return 0;

        }




    }


    //保留三位小数工具类
    public static double handelDouble(double e){



        DecimalFormat df = new DecimalFormat("#.000");

        String format = df.format(e);


        return Double.parseDouble(format);

    }
}
