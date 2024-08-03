package com.pricecomparison.service;

import com.aspose.cells.*;
import com.aspose.cells.Workbook;
import com.pricecomparison.dto.PriceEntryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

        List<PriceEntryDto> sortedPriceEntries = getSortedPriceEntries(productId);

        setCellStyleForDates(worksheet, workbook, sortedPriceEntries);

        fillDataInCells(worksheet, sortedPriceEntries);

        createLineChart(worksheet, sortedPriceEntries);

        saveWorkbook(workbook);
    }

    private List<PriceEntryDto> getSortedPriceEntries(Long productId) {
        List<PriceEntryDto> priceEntries = priceEntryService.getAllPriceEntriesByProductId(productId);
        return priceEntries.stream()
                .sorted(Comparator.comparing(PriceEntryDto::date).reversed())
                .limit(10)
                .toList();
    }

    private void setCellStyleForDates(Worksheet worksheet, Workbook workbook, List<PriceEntryDto> sortedPriceEntries) {
        Style style = workbook.createStyle();
        style.setCustom("yyyy-MM-dd");

        Cells cells = worksheet.getCells();

        int dataRowStart = 2;
        int rowCount = sortedPriceEntries.size();
        for (int i = dataRowStart; i < dataRowStart + rowCount; i++) {
            cells.get("A" + i).setStyle(style);
        }
    }

    private void fillDataInCells(Worksheet worksheet, List<PriceEntryDto> sortedPriceEntries) {
        int dataRowStart = 2;
        int rowCounter = 0;
        for (int i = sortedPriceEntries.size() - 1; i >= 0; i--) {
            int rowIndex = dataRowStart + rowCounter++;
            PriceEntryDto entry = sortedPriceEntries.get(i);

            worksheet.getCells().get("A" + rowIndex).putValue(entry.date());
            worksheet.getCells().get("B" + rowIndex).putValue(entry.price());
        }
    }

    private void createLineChart(Worksheet worksheet, List<PriceEntryDto> sortedPriceEntries) {
        int chartIndex = worksheet.getCharts().add(ChartType.LINE, 12, 0, 27, 8);
        Chart chart = worksheet.getCharts().get(chartIndex);

        String dataRange = "A2:B" + (2 + sortedPriceEntries.size() - 1);
        chart.setChartDataRange(dataRange, true);

        chart.getLegend().getLegendEntries().get(0).setDeleted(true);
    }

    private void saveWorkbook(Workbook workbook) {
        try {
            workbook.save("Price-Line-Chart.xls", SaveFormat.XLSX);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void convertChartToImage() {
        Workbook workbook = null;
        try {
            workbook = new Workbook("Price-Line-Chart.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Worksheet worksheet = Objects.requireNonNull(workbook).getWorksheets().get(0);

        setWorksheetForPrinting(worksheet);

        ImageOrPrintOptions options = setImageOptions();

        try {
            renderSheetToImage(worksheet, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWorksheetForPrinting(Worksheet worksheet) {
        worksheet.getPageSetup().setPrintArea("A13:H27");
        worksheet.getPageSetup().setLeftMargin(0);
        worksheet.getPageSetup().setRightMargin(0);
        worksheet.getPageSetup().setTopMargin(0);
        worksheet.getPageSetup().setBottomMargin(0);
    }

    private ImageOrPrintOptions setImageOptions() {
        ImageOrPrintOptions options = new ImageOrPrintOptions();
        options.setOnePagePerSheet(true);
        options.setImageType(ImageType.JPEG);
        return options;
    }

    private void renderSheetToImage(Worksheet worksheet, ImageOrPrintOptions options) throws Exception {
        SheetRender sr = new SheetRender(worksheet, options);
        sr.toImage(0, "src/main/resources/image/Price-Line-Chart.jpg");
    }
}
