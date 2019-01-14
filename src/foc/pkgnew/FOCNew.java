package foc.pkgnew;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;

public class FOCNew {

    static double GMT = 0;
    static String[][] TimeZoneKey = new String[200][200];

    public static void main(String[] args) {
        time();
        timecomp();
    }

    public static void time() {
        //gets GMT time.
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
        //System.out.println(sdf.format(cal.getTime()));
        GMT = Double.parseDouble(sdf.format(cal.getTime()));
    }

    public static void timecomp() {

        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("Timezones.xls"));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            DataFormatter formatter = new DataFormatter();

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) {
                        cols = tmp;
                    }
                }
            }
            
            // setting r to 1 skips the first row
            for (int r = 1; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell((short) c);
                        if (cell != null) {
                            // Your code here
                            String cellstr = formatter.formatCellValue(cell);
                            TimeZoneKey[r][c]=cellstr;
                            //System.out.println(TimeZoneKey[r][c]);
                        }
                    }
                }
            }
            
            System.out.println("It is five o'clock in the following nations:");
            for (int r = 0; r < TimeZoneKey.length; r++) {
                for (int c = 1; c < TimeZoneKey.length; c++) {
                    if (TimeZoneKey[r][c] != null){
                        double GMTMOD = Double.parseDouble(TimeZoneKey[r][c]);
                        //System.out.println(GMTMOD);
                        double newTime = GMT+GMTMOD;
                        if(GMT+GMTMOD >= 24.00){
                            newTime=newTime-24.00;
                        }
                        System.out.println(newTime);
                        if(newTime >= 17.00 && newTime < 18.00){
                            System.out.print(TimeZoneKey[r][0]+", ");
                        }
                    }
                    
                }
            }
            System.out.println("");
            
        } catch (Exception ioe) {
            ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }
    }
}

//TO DO:
//ADD UI
//CHANGE FROM USING DOUBLE TO TIMES