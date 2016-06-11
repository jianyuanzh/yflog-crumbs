package com.yflog.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.charts.AxisCrosses;
import org.apache.poi.ss.usermodel.charts.AxisPosition;
import org.apache.poi.ss.usermodel.charts.ChartDataSource;
import org.apache.poi.ss.usermodel.charts.ChartLegend;
import org.apache.poi.ss.usermodel.charts.DataSources;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.apache.poi.ss.usermodel.charts.ScatterChartData;
import org.apache.poi.ss.usermodel.charts.ScatterChartSeries;
import org.apache.poi.ss.usermodel.charts.ValueAxis;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created with Intellij IDEA.
 * User: Robin
 * Date: 6/6/16
 */
public class PerformanceExcel {

    public static void main(String[] argv) throws Exception {
        if (argv.length != 1) {
            System.err.println("PerformanceExcel folder");
            return;
        }

        String path = argv[0];

        File pathFile = new File(path);
        if (!pathFile.exists()) {
            System.err.println("The path " + path + " is not exist");
            return;
        }

        File[] files = pathFile.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("Empty files in the path " + path);
            return;
        }

        Workbook wb = new XSSFWorkbook();

        for (File f : files) {
            //format
            //service_type_result_LINUX_LOW_1000_100
            String fileName = f.getName();
            String[] names = fileName.split("_");
            if (names.length != 7) {
                System.err.println("Unknown file - " + fileName);
                continue;
            }
            String name = String.format("%s_%s_%s_%s",
                    names[1], names[4], names[5], names[6]);
            String sheetName = WorkbookUtil.createSafeSheetName(name);
            Sheet sheet = wb.createSheet(sheetName);
            System.out.println("Processing - " + f.getAbsolutePath());
            insertFile(sheet, f);
        }

        FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        wb.write(fileOut);
        fileOut.close();
        System.out.println("Done");
    }

    public static void insertFile(Sheet sheet, File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = null;

        boolean overallResult = false;
        int rowNum = 0;
        int performanceCnt = 0;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("########Test finished at")) {
                sheet.createRow(rowNum++);
                overallResult = true;
            }

            if (!overallResult) {
                performanceCnt++;
            }
            Row row = sheet.createRow(rowNum++);
            row.setHeight((short) 500);
            String[] results = line.split("\\s+");

            int cellNum = 0;
            for (String r : results) {
                Cell cell = row.createCell(cellNum);
                sheet.setColumnWidth(cellNum++, 5000);
                cell.setCellValue(r);
            }
        }

        sheet.createRow(rowNum++);
        sheet.createRow(rowNum++);
        sheet.createRow(rowNum++);

        int xRow = rowNum;
        int startRow = xRow + 1;
        int perfCnt = performanceCnt-3;
        _rewriteXDataForGraph(sheet, rowNum++, perfCnt);
        //Heap commit
        _rewriteDataForGraph(sheet, rowNum++, 3, perfCnt);
        //Heap usage
        _rewriteDataForGraph(sheet, rowNum++, 4, perfCnt);
        //JVM cpu usage
        _rewriteDataForGraph(sheet, rowNum++, 9, perfCnt);
        //host cpu usage
        _rewriteDataForGraph(sheet, rowNum++, 12, perfCnt);
        //Receive request cnt
        _rewriteDataForGraph(sheet, rowNum++, 29, perfCnt);
        //Report cnt
        _rewriteDataForGraph(sheet, rowNum++, 30, perfCnt);

        int finishRow = rowNum-1;

        rowNum += 5;
        int graphRowHeight = 15;
        int graphColumnWidth = 10;
        //Draw the heap commit and heap usage
        drawChart(sheet, xRow, startRow, startRow+2, perfCnt, 0, rowNum, graphColumnWidth, rowNum + graphRowHeight);
        rowNum += 3 + graphRowHeight;
        startRow += 2;
        //Draw the cpu usage
        drawChart(sheet, xRow, startRow, startRow+2, perfCnt, 0, rowNum, graphColumnWidth, rowNum + graphRowHeight);

        rowNum += 3 + graphRowHeight;
        startRow += 2;
        //Draw the receive/report metrics
        drawChart(sheet, xRow, startRow, startRow+2, perfCnt, 0, rowNum, graphColumnWidth, rowNum + graphRowHeight);
        //TestChart.draw(sheet, 0, 3, 0, 26);
    }

    private static void _rewriteXDataForGraph(Sheet newSheet, int rowNum, int cnt) {
        Row row = newSheet.createRow(rowNum);
        int cellNum = 0;

        for (int i = 1; i <= cnt; i ++) {
            Cell cell = row.createCell(i);
            newSheet.setColumnWidth(cellNum++, 5000);
            cell.setCellValue(i);
        }
    }
    private static void _rewriteDataForGraph(Sheet sheet, int rowNum, int columnNum, int cnt) {
        Row row = sheet.createRow(rowNum);

        int cellNum = 0;

        Cell cell = row.createCell(cellNum);
        sheet.setColumnWidth(cellNum++, 5000);
        cell.setCellValue(sheet.getRow(2).getCell(columnNum).getStringCellValue());

        for (int i = 0; i < cnt; i++) {
            cell = row.createCell(cellNum);
            sheet.setColumnWidth(cellNum++, 5000);
            cell.setCellValue(((int)Double.parseDouble(sheet.getRow(i+3).getCell(columnNum).getStringCellValue())));
        }

    }
    private static void drawChart(Sheet sheet, int xRow, int startRow, int finishRow, int cnt,
                                  int columnGraphStart, int rowGraphStart, int columnGraphFinish, int rowGraphFinish) {
        if (cnt <= 0) {
            System.err.println("Illegal column count - " + cnt);
            return;
        }

        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0,
                columnGraphStart, rowGraphStart, columnGraphFinish, rowGraphFinish);

        Chart chart = drawing.createChart(anchor);
        ChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.BOTTOM);

        ScatterChartData data = chart.getChartDataFactory().createScatterChartData();

        ValueAxis bottomAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        ChartDataSource<Number> xs = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(xRow, xRow, 1, cnt));
        while (startRow < finishRow) {
            ChartDataSource<Number> ys = DataSources.fromNumericCellRange(sheet, new CellRangeAddress(startRow, startRow, 1, cnt));
            ScatterChartSeries series = data.addSerie(xs, ys);
            series.setTitle(sheet.getRow(startRow).getCell(0).getStringCellValue());
            startRow++;
        }

        chart.plot(data, bottomAxis, leftAxis);

    }


}
