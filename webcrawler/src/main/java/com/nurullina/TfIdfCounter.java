package com.nurullina;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lnurullina
 */
class TfIdfCounter {
    private static int docsNum;

    TfIdfCounter(int docsNum) {
        TfIdfCounter.docsNum = docsNum;
    }

    /**
     * tf = number of word in doc/ number of all words in doc
     * idf = number of all docs / number of docs with the word
     * tfidf = tf*idf
     */
    float calculate(String word, List<String> doc) {
        int wordCount = 0;
        for (String docWord : doc) {
            if (docWord.equals(word)) {
                wordCount++;
            }
        }
        float tf = (float) wordCount / (float) doc.size();
        Path path = Paths.get("words/" + word + ".txt");
        long lineCount = 0;
        try {
            lineCount = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        float idf = (float) lineCount / (float) docsNum;
        return tf * idf;

    }

}
