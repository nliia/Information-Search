package com.nurullina;

import com.sun.istack.internal.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author lnurullina
 */
class MySpider {
    private static final int MAX_PAGES_TO_VISIT = 20;
    private Set<String> visitedPages = new HashSet<String>();
    private List<String> pagesToVisit = new ArrayList<String>();
    private List<String> linksFound = new LinkedList<String>();
    private Document htmlDocument;

    void search(String url, String searchWord) {
        while (visitedPages.size() < MAX_PAGES_TO_VISIT) {
            String currentUrl;
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                visitedPages.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            crawlForLinks(currentUrl);
            boolean success = searchWord(searchWord);
            if (success) {
                System.out.println(String.format(" Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(getLinksFound());
        }
        System.out.println("\n Done. Visited " + visitedPages.size() + " web page(s)");
    }

    private void crawlForLinks(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            {
                System.out.println("\nReceived web page at " + url);
            } else {
                System.err.println("Response status: " + connection.response().statusCode());
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage) {
                this.linksFound.add(link.absUrl("href"));
            }
        } catch (IOException ioe) {
            System.err.println("Error during connection establishment");
        }
    }

    private boolean searchWord(@NotNull String word) {
        System.out.println("Searching for the word " + word);
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(word.toLowerCase());
    }


    private List<String> getLinksFound() {
        return linksFound;
    }

    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (visitedPages.contains(nextUrl));
        visitedPages.add(nextUrl);
        return nextUrl;
    }
}
