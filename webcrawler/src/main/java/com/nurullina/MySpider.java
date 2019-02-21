package com.nurullina;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lnurullina
 */
class MySpider {
    private static final int MAX_PAGES_TO_VISIT = 100;
    private Set<String> visitedPages = new HashSet<>();
    private Parser parser = new Parser();
    private String mainUrl;

    MySpider(String mainUrl) throws IOException {
        this.mainUrl = mainUrl;
        FileUtils.cleanDirectory(new File("contents/"));
        FileUtils.cleanDirectory(new File("words/"));
    }

    void getPageLinks(String URL) {
        if ((!visitedPages.contains(URL) && (visitedPages.size() < MAX_PAGES_TO_VISIT) && !URL.contains("jpg"))) {
            System.out.println("URL: [" + URL + "]");

            try (BufferedWriter urlWriter = new BufferedWriter(new FileWriter("urls.txt", true))) {
                urlWriter.write(URL + "\n");
                visitedPages.add(URL);
                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");
                int docNum = visitedPages.size();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("contents/" + docNum + ".txt", true))) {
                    List<String> wordsList = parser.parseToWords(document.body().text());

                    wordsList.forEach(s -> {
                        try {
                            writer.write(s + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    Set<String> uniquWords = new HashSet<>(wordsList);
                    uniquWords.forEach(s -> {
                        try (BufferedWriter wordWriter = new BufferedWriter(new FileWriter("words/" + s + ".txt", true))) {
                            wordWriter.write(docNum + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }
}
