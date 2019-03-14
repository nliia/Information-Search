package com.nurullina;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    void getTfIdfForAllWords() {
        File folder = new File("words");
        File[] listOfFiles = folder.listFiles();

        Set<String> words = new HashSet<>();
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                words.add(listOfFile.getName().replace(".txt", ""));
//                System.out.println("File " + listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                System.out.println("Directory " + listOfFile.getName());
            }
        }
        File contentsFolder = new File("contents");
        List<File> listOfDocs = Arrays.asList(contentsFolder.listFiles());
        Collections.sort(listOfDocs);
        for (String uniqueWord : words) {
            try (BufferedWriter wordWriter = new BufferedWriter(new FileWriter("results/tf-idf.txt", true))) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(uniqueWord).append(":");
                for (File doc : listOfDocs) {
                    List<String> listDocWords = getDoc(doc);
                    stringBuilder.append(calculate(uniqueWord, listDocWords)).append(",");
                }
                wordWriter.write(stringBuilder.append("\n").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<String> getDoc(File doc) {
        List<String> docWordsList = new ArrayList<>();
        if (doc.isFile()) {
            Path path = Paths.get(doc.getAbsolutePath());
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    docWordsList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return docWordsList;
    }
}
