package daniel.yyf.er017.october;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class JavaExcelTest {
    public static void main(String[] args) throws Exception {
        Workbook workbook = Workbook.getWorkbook(JavaExcelTest.class.getResourceAsStream
                ("/october/question_example.xls"));
        //获得工作表
        Sheet sheet = workbook.getSheet(0);

        //遍历行
        for (int i = 1; i < sheet.getRows(); i++) {
            //遍历每一行的每个单元格
            Cell[] cells = sheet.getRow(i);
            String des = cells[0].getContents();
            System.out.println("des:" + des);
            String type = cells[1].getContents();
            System.out.println("type:" + type);
            int score = new Integer(cells[2].getContents());
            System.out.println("score:" + score);
            int t = new Integer(cells[3].getContents());
            for (int j = 0; j < t; j++) {
                System.out.println("option " + j + ":" + cells[4 + j].getContents());
            }
            System.out.println("answer:"+cells[4 + t].getContents());
        }
    }
}
