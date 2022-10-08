package com.yilin.csuftspider.utils.pdf;




import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import com.yilin.csuftspider.model.domain.Grade;
import com.yilin.csuftspider.model.response.GradesInfo;

import lombok.extern.slf4j.Slf4j;
import com.itextpdf.text.BaseColor;

import java.io.IOException;
import java.util.List;

/**
 * pdf工具类
 */

@Slf4j
public class  PdfUtils {

    static final LineSeparator LINE = new LineSeparator(1f,100, BaseColor.BLACK,Element.ALIGN_CENTER,-5f);
    // 设置字体
        public static void  draw(Document docu,String title, String name, String sid, GradesInfo info) throws IOException, DocumentException {


            Document doc = docu;
            /** 创建 PdfWriter 对象 */

            doc.open();// 打开文档

            BaseFont bfChinese=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            //设置字体样式
            Font textFont = new Font(bfChinese,11, Font.NORMAL); //正常
            //Font redTextFont = new Font(bfChinese,11,Font.NORMAL,Color.RED); //正常,红色
            Font boldFont = new Font(bfChinese,11,Font.BOLD); //加粗
            //Font redBoldFont = new Font(bfChinese,11,Font.BOLD,Color.RED); //加粗,红色
            Font firsetTitleFont = new Font(bfChinese,22,Font.BOLD); //一级标题
            Font secondTitleFont = new Font(bfChinese,15,Font.BOLD, CMYKColor.BLUE); //二级标题
            Font underlineFont = new Font(bfChinese,11,Font.UNDERLINE); //下划线斜体
            //设置字体
            Font FontChinese24 = new Font(bfChinese, 24, Font.BOLD);
            Font FontChinese18 = new Font(bfChinese, 18, Font.BOLD);
            Font FontChinese16 = new Font(bfChinese, 16, Font.BOLD);
            Font FontChinese12 = new Font(bfChinese, 12, Font.NORMAL);
            Font FontChinese12Bold = new Font(bfChinese, 12, Font.BOLD);
            Font FontChinese11Bold = new Font(bfChinese, 11,Font.BOLD);
            Font FontChinese11 = new Font(bfChinese, 11, Font.ITALIC);
            Font FontChinese11Normal = new Font(bfChinese, 11, Font.NORMAL);
            Font FontChinese8Bold = new Font(bfChinese, 8,Font.BOLD);
            Font FontChinese8Normal = new Font(bfChinese, 8, Font.NORMAL);


        //标题
            Paragraph titleParagraph = new Paragraph(title,FontChinese18);
            titleParagraph.setAlignment(Element.ALIGN_CENTER);// 居中
            doc.add(titleParagraph);

        //个人信息
            Paragraph element1 = new Paragraph(sid,FontChinese11Bold);
            element1.setAlignment(Element.ALIGN_RIGHT);
            doc.add(element1);

            element1 = new Paragraph(name,FontChinese11Bold);
            element1.setAlignment(Element.ALIGN_RIGHT);
            doc.add(element1);

            element1 = new Paragraph("GPA: "+info.getGpa().toString(),FontChinese8Bold);
            element1.setAlignment(Element.ALIGN_RIGHT);
            doc.add(element1);

        //画线
            doc.add(LINE);




        //空格
            Paragraph blank1 = new Paragraph(" ");
            doc.add(blank1);



        //画表格
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            // 设置表格的宽度
            table.setTotalWidth(500);
            // 也可以每列分别设置宽度
            table.setTotalWidth(new float[] { 40, 62,142, 50,50,50, 50,50});
            // 锁住宽度
            table.setLockedWidth(true);


            //画表格属性
                PdfPCell cell = new PdfPCell(new Phrase("序号",FontChinese8Bold));

                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                cell.setBackgroundColor(BaseColor.GRAY);
                table.addCell(cell);


                  cell = new PdfPCell(new Phrase("时间",FontChinese8Bold));

                 cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                cell.setBackgroundColor(BaseColor.GRAY);
                table.addCell(cell);

            cell = new PdfPCell(new Phrase("课程名称",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("说明",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("成绩",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("学分",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("绩点",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("属性",FontChinese8Bold));

            cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
            cell.setBackgroundColor(BaseColor.GRAY);
            table.addCell(cell);

            //填充表格内容
            List<Grade> gradeList = info.getGradeList();



            for (Grade g : gradeList) {


                // id
                cell = new PdfPCell(new Phrase(g.getId().toString(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);

                // 时间
                cell = new PdfPCell(new Phrase(g.getTerm(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);


                // 课程名称
                cell = new PdfPCell(new Phrase(g.getCourseName(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);

                // 说明

                String status = "";
                if(g.getStatus()==0){
                    status = "正常";
                }else{
                    status = "补考";
                }
                cell = new PdfPCell(new Phrase(status,FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);



                // 成绩
                cell = new PdfPCell(new Phrase(g.getGrade(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);


                // 学分
                cell = new PdfPCell(new Phrase(g.getCredit().toString(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);

                // 绩点
                cell = new PdfPCell(new Phrase(g.getGradePoint().toString(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);


                // 属性
                cell = new PdfPCell(new Phrase(g.getAttribute(),FontChinese8Normal));
                cell.setVerticalAlignment(PdfPCell.ALIGN_CENTER);//设置单元格的垂直对齐方式
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置单元格的水平对齐方式
                table.addCell(cell);



            }




                doc.add(table);

            doc.close();






        }



}

