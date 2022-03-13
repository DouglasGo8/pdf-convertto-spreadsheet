package com.pdfbox.pooc;

import org.apache.camel.Handler;
import org.apache.camel.language.xpath.XPath;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component("myBean")
public class MySpringBean {


  @Handler
  public void writeMyWorkbook(
          @XPath("//tag_36/text()") String nRfC,
          @XPath("//tag_37/text()") String compFields,
          @XPath("//tag_59/text()") String razaoSocial1,
          @XPath("//tag_58/text()") String razaoSocial2,
          @XPath("//tag_38/text()") String cnpjFields,
          @XPath("//tag_41/text()") String tomador,
          @XPath("//tag_63/text()") String cpf1,
          @XPath("//tag_62/text()") String cpf2,
          @XPath("//tag_45/text()") String discrim,
          @XPath("//tag_44/text()") String codServ,
          @XPath("//tag_80/text()") String valor1,
          @XPath("//tag_49/text()") String valor2) {

    //System.out.println(NrFC);
    var excelFile = "./excel/LuccaBook.xlsx";
    try {

      var fis = new FileInputStream(excelFile);
      var workBook = WorkbookFactory.create(fis);
      var sheet = workBook.getSheet("Sheet1");
      var rowCount = sheet.getLastRowNum();
      var row = sheet.createRow(++rowCount);
      var col = 0;
      //
      var cell = row.createCell(col);
      cell.setCellValue(rowCount); // Qtd Row

      cell = row.createCell(++col);
      cell.setCellValue(nRfC); // Nr Nota

      cell = row.createCell(++col);
      cell.setCellValue(compFields.trim().substring(0, compFields.trim().indexOf(" "))); // Competencia Data

      var firstSpaceOccur = compFields.trim().indexOf(" ");
      compFields = compFields.trim().substring(firstSpaceOccur);
      var secondSpaceOccur = compFields.trim().indexOf(" ") + 1;
      compFields = compFields.trim().substring(secondSpaceOccur);
      compFields = compFields.trim().substring(compFields.indexOf(" "));

      cell = row.createCell(++col);
      cell.setCellValue(compFields.trim()); // Cod Verificacao

      cell = row.createCell(++col);
      cell.setCellValue(razaoSocial1.toUpperCase().startsWith("CLIA") ?
              razaoSocial1 : razaoSocial2); // Razao Social

      cell = row.createCell(++col);
      cell.setCellValue(cnpjFields.trim().substring(0, cnpjFields.indexOf(" "))); // CNPJ

      firstSpaceOccur = cnpjFields.trim().indexOf(" ");
      cnpjFields = cnpjFields.trim().substring(firstSpaceOccur);
      cnpjFields = cnpjFields.trim().substring(0, cnpjFields.trim().indexOf(" "));

      cell = row.createCell(++col);
      cell.setCellValue(cnpjFields); // Incri Municipal

      cell = row.createCell(++col);
      cell.setCellValue(tomador); // Tomador

      cell = row.createCell(++col);
      cell.setCellValue(razaoSocial1.toUpperCase().startsWith("CLIA") ? cpf1 : cpf2); // CPF

      cell = row.createCell(++col);
      cell.setCellValue(discrim); // discrim

      cell = row.createCell(++col);
      cell.setCellValue(codServ.trim().substring(0, codServ.indexOf("-"))); // Cod do Servico

      cell = row.createCell(++col);
      cell.setCellValue(codServ.substring(codServ.indexOf("-") + 1)); // atividade

      cell = row.createCell(++col);
      cell.setCellValue(valor2.trim().length() <= 5 ? valor2: valor1); // valor

      fis.close();
      var fos = new FileOutputStream(excelFile);
      workBook.write(fos);
      workBook.close();
      fos.close();

    } catch (IOException ex) {
      ex.printStackTrace();
    }


  }

}
