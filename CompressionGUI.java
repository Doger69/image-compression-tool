import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class CompressionGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Image Compression - JPEG / RLE / Huffman");

        JButton openButton = new JButton("Open Image");
        JLabel fileLabel = new JLabel("No file selected.");
        JComboBox<String> methodBox = new JComboBox<>(new String[]{"JPEG", "RLE", "Huffman"});
        JTextField qualityField = new JTextField("0.5", 5);
        JButton compressButton = new JButton("Compress");

        frame.setLayout(new FlowLayout());
        frame.add(openButton);
        frame.add(fileLabel);
        frame.add(new JLabel("Method:"));
        frame.add(methodBox);
        frame.add(new JLabel("JPEG Quality (0.0 - 1.0):"));
        frame.add(qualityField);
        frame.add(compressButton);

        frame.setSize(500, 180);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        final File[] selectedFile = {null};

        openButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "bmp"));
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                fileLabel.setText("Selected: " + selectedFile[0].getName());
            }
        });

        compressButton.addActionListener(e -> {
            if (selectedFile[0] == null) {
                JOptionPane.showMessageDialog(frame, "Please select an image file first.");
                return;
            }

            String method = (String) methodBox.getSelectedItem();

            switch (method) {
                case "JPEG":
                    try {
                        float quality = Float.parseFloat(qualityField.getText());
                       
                        JOptionPane.showMessageDialog(frame, "JPEG compression successful!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "JPEG compression failed.");
                    }
                    break;

                case "RLE":
                    try {
                        RLECompressor.compress(selectedFile[0]);
                        JOptionPane.showMessageDialog(frame, "RLE compression successful!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "RLE compression failed.");
                    }
                    break;

                case "Huffman":
                    try {
                        HuffmanCompressor.compress(selectedFile[0]);
                        JOptionPane.showMessageDialog(frame, "Huffman compression successful!");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Huffman compression failed.");
                    }
                    break;
            }
        });
    }
}
