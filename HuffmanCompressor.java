import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class HuffmanCompressor {

    private static class Node implements Comparable<Node> {
        int value, freq;
        Node left, right;

        public Node(int value, int freq) {
            this.value = value;
            this.freq = freq;
        }

        public boolean isLeaf() {
            return (left == null && right == null);
        }

        public int compareTo(Node other) {
            return Integer.compare(this.freq, other.freq);
        }
       }

    private static void buildCodeMap(Node root, String code, Map<Integer, String> codeMap) {
        if (root.isLeaf()) {
            codeMap.put(root.value, code);
            return;
        }
        buildCodeMap(root.left, code + "0", codeMap);
        buildCodeMap(root.right, code + "1", codeMap);
    }

    public static void compress(File inputImage) throws IOException {
        BufferedImage image = ImageIO.read(inputImage);

        int width = image.getWidth();
        int height = image.getHeight();

        // Ye Grayscale mein convert krega or frequency count krega
        int[] freq = new int[256];
        int[][] pixels = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int gray = new Color(image.getRGB(x, y)).getRed(); // grayscale
                pixels[y][x] = gray;
                freq[gray]++;
            }
        }

        // Ye Huffman Tree bnaega
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0)
                pq.add(new Node(i, freq[i]));
        }

        while (pq.size() > 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            Node parent = new Node(-1, a.freq + b.freq);
            parent.left = a;
            parent.right = b;
            pq.add(parent);
        }

        Node root = pq.poll();

        // Ye Huffman code build krega
        Map<Integer, String> codeMap = new HashMap<>();
        buildCodeMap(root, "", codeMap);

        // Ye Encode krega image data ko
        StringBuilder encodedData = new StringBuilder();
        for (int[] row : pixels) {
            for (int pixel : row) {
                encodedData.append(codeMap.get(pixel));
            }
        }

        // Ye Output krega compress data ko
        File compressedFile = new File("compressed.huffman");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(compressedFile))) {
            writer.write(encodedData.toString());
        }

        System.out.println("Image successfully compressed using Huffman coding.");
    }
}
