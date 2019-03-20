package com.nurullina;

import java.util.List;

/**
 * @author lnurullina
 */
public class CosineCalculator {
    public static double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        float dotProduct = (float) 0.0;
        float normA = (float) 0.0;
        float normB = (float) 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
