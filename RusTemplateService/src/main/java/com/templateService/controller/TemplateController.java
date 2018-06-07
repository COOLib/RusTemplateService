package com.templateService.controller;

import com.templateService.util.TemplateValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by user on 05.06.2018.
 */
@RestController
@RequestMapping(value = "/templateService")
public class TemplateController {

    @Value("${axesBreakdownPath}")
    private String axesTemplatePath;

    @Value("${axesBreakdownFile}")
    private String axesTemplateFile;

    @RequestMapping(value = "/axesBreakdown", method = RequestMethod.PUT, headers = {"Content-Type=application/json"},
            produces = {"application/json; charset=UTF-8"})
    public
    @ResponseBody
    File fillAxesBreakdown(@RequestBody Map<String, String> variables) throws Exception {

        File templateFile = new File("${axesBreakdownPath}");

        FileInputStream fis = new FileInputStream(templateFile);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);


        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String newCellValueKey = TemplateValue.isValue(cell.getStringCellValue());
                    if (newCellValueKey.length() > 0) {
                        cell.setCellValue(variables.get(newCellValueKey));

                    }
                }
            }
        }

        FileOutputStream fileOut = new FileOutputStream(new File("${results}" + variables.get("doc_num")));
        workbook.write(fileOut);
        fileOut.close();

        workbook.close();

        return templateFile;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(final HttpServletResponse response) throws IOException {
        
        File file = new File(axesTemplatePath + axesTemplateFile);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.getOutputStream().flush();
    }
}
