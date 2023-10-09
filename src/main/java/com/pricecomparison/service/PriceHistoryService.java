package com.pricecomparison.service;

import com.aspose.cells.*;
import com.aspose.cells.Workbook;
import com.pricecomparison.dto.PriceEntryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PriceHistoryService {
    private final PriceEntryService priceEntryService;

    public void createPriceHistory(Long productId) {
        createChart(productId);
        convertChartToImage();
    }

    public void createChart(Long productId) {
        Workbook workbook = new Workbook();

        Worksheet worksheet = workbook.getWorksheets().get(0);

        Style style = workbook.createStyle();
        style.setCustom("yyyy-MM-dd");

        Cells cells = worksheet.getCells();

        List<Cell> dateCells = new ArrayList<>();
        dateCells.add(cells.get("A2"));
        dateCells.add(cells.get("A3"));
        dateCells.add(cells.get("A4"));
        dateCells.add(cells.get("A5"));
        dateCells.add(cells.get("A6"));
        dateCells.add(cells.get("A7"));
        dateCells.add(cells.get("A8"));
        dateCells.add(cells.get("A9"));
        dateCells.add(cells.get("A10"));
        dateCells.add(cells.get("A11"));

        for(Cell dateCell : dateCells) {
            dateCell.setStyle(style);
        }

        List<PriceEntryDto> priceEntries = priceEntryService.getAllPriceEntriesByProductId(productId);

        List<PriceEntryDto> sortedPriceEntries = priceEntries.stream()
                .sorted(Comparator.comparing(PriceEntryDto::date).reversed())
                .limit(10)
                .toList();

        worksheet.getCells().get("A2").putValue(sortedPriceEntries.get(9).date());
        worksheet.getCells().get("A3").putValue(sortedPriceEntries.get(8).date());
        worksheet.getCells().get("A4").putValue(sortedPriceEntries.get(7).date());
        worksheet.getCells().get("A5").putValue(sortedPriceEntries.get(6).date());
        worksheet.getCells().get("A6").putValue(sortedPriceEntries.get(5).date());
        worksheet.getCells().get("A7").putValue(sortedPriceEntries.get(4).date());
        worksheet.getCells().get("A8").putValue(sortedPriceEntries.get(3).date());
        worksheet.getCells().get("A9").putValue(sortedPriceEntries.get(2).date());
        worksheet.getCells().get("A10").putValue(sortedPriceEntries.get(1).date());
        worksheet.getCells().get("A11").putValue(sortedPriceEntries.get(0).date());

        worksheet.getCells().get("B2").putValue(sortedPriceEntries.get(9).price());
        worksheet.getCells().get("B3").putValue(sortedPriceEntries.get(8).price());
        worksheet.getCells().get("B4").putValue(sortedPriceEntries.get(7).price());
        worksheet.getCells().get("B5").putValue(sortedPriceEntries.get(6).price());
        worksheet.getCells().get("B6").putValue(sortedPriceEntries.get(5).price());
        worksheet.getCells().get("B7").putValue(sortedPriceEntries.get(4).price());
        worksheet.getCells().get("B8").putValue(sortedPriceEntries.get(3).price());
        worksheet.getCells().get("B9").putValue(sortedPriceEntries.get(2).price());
        worksheet.getCells().get("B10").putValue(sortedPriceEntries.get(1).price());
        worksheet.getCells().get("B11").putValue(sortedPriceEntries.get(0).price());

        int chartIndex = worksheet.getCharts().add(ChartType.LINE, 12, 0, 27, 8);

        Chart chart = worksheet.getCharts().get(chartIndex);

        chart.setChartDataRange("A2:B11", true);

        chart.getLegend().getLegendEntries().get(0).setDeleted(true);

        try {
            workbook.save("Price-Line-Chart.xls", SaveFormat.XLSX);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void convertChartToImage() {
        Workbook workbook;
        try {
            workbook = new Workbook("Price-Line-Chart.xls");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Worksheet worksheet = workbook.getWorksheets().get(0);

        worksheet.getPageSetup().setPrintArea("A13:H27");

        worksheet.getPageSetup().setLeftMargin(0);
        worksheet.getPageSetup().setRightMargin(0);
        worksheet.getPageSetup().setTopMargin(0);
        worksheet.getPageSetup().setBottomMargin(0);

        ImageOrPrintOptions options = new ImageOrPrintOptions();
        options.setOnePagePerSheet(true);
        options.setImageType(ImageType.JPEG);

        try {
            SheetRender sr = new SheetRender(worksheet, options);
            sr.toImage(0, "src/main/resources/image/Price-Line-Chart.jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
