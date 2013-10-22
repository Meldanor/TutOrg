package de.meldanor.tutorg;

public class SimilarityMatcher {

    private int[][] levenshteinMatrix;

    private int originLength;
    private char[] originChars;

    public SimilarityMatcher(String origin) {
        this.levenshteinMatrix = new int[origin.length() + 1][];
        this.originLength = origin.length();
        this.originChars = origin.toCharArray();
    }

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
}
