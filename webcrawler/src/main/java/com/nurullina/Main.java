package com.nurullina;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author lnurullina
 */
public class Main {

    public static void main(String[] args) throws IOException {
//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter url (add the space after and then press enter)");
//        String url = in.nextLine();
//        MySpider spider = new MySpider(url);
//        spider.getPageLinks(url);
//        TfIdfCounter tfIdfCounter = new TfIdfCounter(30);
//        tfIdfCounter.getTfIdfForAllWords();
        TfIdfCounter tfIdfCounter = new TfIdfCounter(30);
        tfIdfCounter.getBestMatch("access my account");
    }
}
