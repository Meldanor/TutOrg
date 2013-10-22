package de.meldanor.tutorg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LevenshteinDistance implements EditDistance {

    private int[][] levenshteinMatrix;

    private int originLength;
    private char[] originChars;

    public LevenshteinDistance(String origin) {
        this.levenshteinMatrix = new int[origin.length() + 1][];
        this.originLength = origin.length();
        this.originChars = origin.toCharArray();
    }

    @Override
    public int getDifference(String other) {
        prepare(other);
        calculate(other);

        return levenshteinMatrix[originLength][other.length()];
    }

    private void prepare(String other) {
        int j = 0;
        int originBorder = this.originLength + 1;
        int otherBorder = other.length() + 1;
        for (int i = 0; i < originBorder; ++i, ++j) {
            levenshteinMatrix[i] = new int[otherBorder];
            levenshteinMatrix[i][0] = i;
            if (j < otherBorder) {
                levenshteinMatrix[0][j] = j;
            }

        }
        for (; j < otherBorder; ++j)
            levenshteinMatrix[0][j] = j;

    }

    private void calculate(String other) {

        int right;
        int min;
        int originBorder = this.originLength + 1;
        int otherBorder = other.length() + 1;
        char[] otherChars = other.toCharArray();
        for (int a = 1; a < originBorder; ++a) {
            for (int b = 1; b < otherBorder; ++b) {
                right = 0;
                if (originChars[a - 1] != otherChars[b - 1])
                    right = 1;

                min = Math.min(levenshteinMatrix[a - 1][b] + 1, levenshteinMatrix[a][b - 1] + 1);
                min = Math.min(min, levenshteinMatrix[a - 1][b - 1] + right);

                levenshteinMatrix[a][b] = min;
            }
        }
    }

    @Override
    public List<String> similarNames(List<String> names) {
        return this.similarNames(names, 2);
    }

    public List<String> similarNames(List<String> names, int maxDifference) {

        List<SimilarName> similarName = new ArrayList<SimilarName>();;
        int diff = 0;
        for (String name : names) {
            diff = getDifference(name);
            // 100% Match
            if (diff == 0) {
                return Arrays.asList(name);
            }
            if (diff <= maxDifference) {
                similarName.add(new SimilarName(name, diff));
            }
        }

        Collections.sort(similarName);
        List<String> result = new ArrayList<String>();
        for (SimilarName name : similarName) {
            result.add(name.name);
        }
        return result;

    }

    private class SimilarName implements Comparable<SimilarName> {
        private String name;
        private int difference;

        public SimilarName(String name, int difference) {
            this.name = name;
            this.difference = difference;
        }

        @Override
        public int compareTo(SimilarName other) {
            return this.difference - other.difference;
        }
    }

}
