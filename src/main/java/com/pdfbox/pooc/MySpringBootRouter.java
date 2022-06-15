package com.pdfbox.pooc;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Component
public class MySpringBootRouter extends RouteBuilder {

  @Override
  public void configure() {

    onException(Exception.class)
            .log("${body}-${header.CamelFileName}");

    //final var folderToProcess = "in";

   /* from("file://./in?noop=true") // Listening this IN Directory
            //.log("${header.CamelFileName}")
            .process(e -> {
              try {
                var fileName = e.getIn().getHeader("CamelFileName", String.class);
                var pdfParser = new PDFParser(new RandomAccessFile(new File("./in/" + fileName), "r"));
                //
                pdfParser.parse();
                //
                try (var cosDoc = pdfParser.getDocument()) {
                  try (var pdDoc = new PDDocument(cosDoc)) {
                    if (pdDoc.getPages().getCount() > 1) {
                      for (int i = 1; i <= pdDoc.getPages().getCount(); i++) {
                        try (var pw = new PrintWriter("./out/" +
                                fileName.replace(".pdf", "") +
                                "_" + i + ".txt", StandardCharsets.UTF_16)) {
                          //
                          var pdfTextStripper = new PDFTextStripper();
                          pdfTextStripper.setStartPage(i);
                          pdfTextStripper.setEndPage(i);
                          pw.println(pdfTextStripper.getText(pdDoc).trim());
                        }
                      }
                    } else {
                      try (var pw = new PrintWriter("./out/" +
                              fileName.replace(".pdf", "") + ".txt", StandardCharsets.UTF_16)) {
                        pw.println(new PDFTextStripper().getText(pdDoc).trim());
                      }
                    }
                  }
                }
              } catch (Exception ex) {
                System.out.println("::: Parser Exception :::" + ex.getMessage());
              }
            })
            .log("Done Txt Generation....");


    from("file://./out?noop=true&charset=UTF_16")
            .setHeader("fileNameAndExt", header("CamelFileName").regexReplaceAll(".txt", ".xml"))
            .split(body().tokenize("\n"), new SetAggregationStrategy()).streaming()
              .setProperty("tag", simple("tag_${header.CamelSplitIndex}++"))
              .transform(simple("<${exchangeProperty.tag}>${body}</${exchangeProperty.tag}>"))
            .end()
            .transform(bodyAs(String.class).regexReplaceAll(",", ""))
            .transform(simple("<root>${body}</root>"))
            .setHeader(Exchange.FILE_NAME, header("fileNameAndExt"))
            .to("file://./pout?charset=UTF_16")
            .log("Done XML Generation....");*/

    from("file://./pout?noop=true&charset=UTF_16")
            .bean(MySpringBean::new)
            .log("Done Excel Generation");


  }


  static class SetAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
      String body = newExchange.getIn().getBody(String.class);
      if (oldExchange == null) {
        var set = new HashSet<>();
        set.add(body);
        newExchange.getIn().setBody(set);
        return newExchange;
      } else {
        @SuppressWarnings("unchecked")
        Set<String> set = Collections.checkedSet(oldExchange.getIn().getBody(Set.class), String.class);
        set.add(body);
        return oldExchange;
      }
    }
  }


}
