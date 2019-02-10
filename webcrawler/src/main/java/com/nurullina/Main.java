package com.nurullina;

import java.util.Scanner;

/**
 * @author lnurullina
 */
public class Main {

    public static void main(String[] args) {
        MySpider spider = new MySpider();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter url (add the space after and then press enter)");
        String url = in.nextLine();
        System.out.println("Enter the word for search");
        String word = in.nextLine();
        spider.search(url, word);
    }
}
