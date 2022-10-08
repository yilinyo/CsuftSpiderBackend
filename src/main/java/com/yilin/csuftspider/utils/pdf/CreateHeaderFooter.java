package com.yilin.csuftspider.utils.pdf;

/**
 * Title: CreatHeaderFooter
 * Description: TODO
 *
 * @author Yilin
 * @version V1.0
 * @date 2022-10-04
 */
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class CreateHeaderFooter extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        //Header
        float x = document.top(-20);

        //Footer
        float y = document.bottom(-20);
        //生成下划线，使用空格占位
        cb.setLineWidth(1);
        cb.moveTo(document.left(),x);
        cb.lineTo(document.right(),x);
        cb.setColorStroke(BaseColor.BLACK);
        cb.stroke();

        cb.beginText();
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        cb.setFontAndSize(bf, 10);

        //左
//        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
//                "H-Left",
//                document.left(), x, 0);
        //中
//        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
//                writer.getPageNumber()+ " page",
//                (document.right() + document.left())/2,
//                x, 0);
        //右
//        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
//                "H-Right",
//                document.right(), x, 0);



        //左
//        cb.showTextAligned(PdfContentByte.ALIGN_LEFT,
//                "F-Left",
//                document.left(), y, 0);
        //中
        cb.showTextAligned(PdfContentByte.ALIGN_CENTER,
                "- "+writer.getPageNumber()+" -",
                (document.right() + document.left())/2,
                y, 0);
        //右
//        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT,
//                "F-Right",
//                document.right(), y, 0);

        cb.endText();

        cb.restoreState();
        super.onStartPage(writer,document);
    }
}

