package com.nurullina;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

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
    double calculate(String word, List<String> doc) {
        int wordCount = 0;
        for (String docWord : doc) {
            if (docWord.equals(word)) {
                wordCount++;
            }
        }
        double tf = (double) wordCount / (double) doc.size();
        Path path = Paths.get("words/" + word + ".txt");
        long lineCount = 0;
        try {
            lineCount = Files.lines(path).count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        double idf = (double) lineCount / (double) docsNum;
        return tf * idf;

    }

    void getTfIdfForAllWords() {
//        File folder = new File("words");
//        File[] listOfFiles = folder.listFiles();
//
//        Set<String> words = new HashSet<>();
//        for (File listOfFile : listOfFiles) {
//            if (listOfFile.isFile()) {
//                words.add(listOfFile.getName().replace(".txt", ""));
////                System.out.println("File " + listOfFile.getName());
//            } else if (listOfFile.isDirectory()) {
//                System.out.println("Directory " + listOfFile.getName());
//            }
//        }
//        File contentsFolder = new File("contents");
//        List<File> listOfDocs = Arrays.asList(contentsFolder.listFiles());
//        Collections.sort(listOfDocs);
//        for (String uniqueWord : words) {
//            try (BufferedWriter wordWriter = new BufferedWriter(new FileWriter("results/tf-idf.txt", true))) {
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(uniqueWord).append(":");
//                for (File doc : listOfDocs) {
//                    List<String> listDocWords = getDoc(doc);
//                    stringBuilder.append(calculate(uniqueWord, listDocWords)).append(",");
//                }
//                wordWriter.write(stringBuilder.append("\n").toString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    void getBestMatch(String query) {
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
        LinkedList<List<Double>> vectors = new LinkedList<>();
        for (File doc : listOfDocs) {
            vectors.addLast(new ArrayList<>());
            for (String uniqueWord : words) {
                List<String> listDocWords = getDoc(doc);
                vectors.getLast().add(calculate(uniqueWord, listDocWords));
            }
        }

        vectors.addLast(new ArrayList<>());
        for (String uniqueWord : words) {
            vectors.getLast().add(calculate(uniqueWord, Arrays.asList(StringUtils.split(query, ' '))));
        }
        List<Double> queryVector = vectors.getLast();
        HashMap<Integer, Double> cosines = new HashMap<>();
        for (int i = 0; i < vectors.size() - 1; i++) {
            cosines.put(i, CosineCalculator.cosineSimilarity(vectors.get(i), queryVector));
        }
        Map<Integer, Double> sortedByCosine = sortByValue(cosines);
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("results/cosines.txt", true))) {
            int count = 0;
            for (Integer doc : sortedByCosine.keySet()) {
                stringBuilder.append(doc).append(": ").append(sortedByCosine.get(doc)).append("\n");
                count++;
                if (count >= 20){
                    break;
                }
            }
            writer.write(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
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

    public static Map<Integer, Double> sortByValue(HashMap<Integer, Double> map) {
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(map.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Map<Integer, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
