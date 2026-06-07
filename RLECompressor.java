import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class RLECompressor {

    public static void compress(File inputImage) throws IOException {
        BufferedImage image = ImageIO.read(inputImage);

        // ye grayscale mein convert krta he
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                grayImage.setRGB(x, y, rgb);
            }
        }

        // Ye output file generate krta he
        File outFile = new File("compressed.rle");
        FileOutputStream fos = new FileOutputStream(outFile);
        DataOutputStream dos = new DataOutputStream(fos);

        // width and header of the image
        dos.writeInt(grayImage.getWidth());
        dos.writeInt(grayImage.getHeight());

        // Rle apply krta he row by row
        for (int y = 0; y < grayImage.getHeight(); y++) {
            int prevPixel = -1;
            int count = 0;
            for (int x = 0; x < grayImage.getWidth(); x++) {
                int current = grayImage.getRGB(x, y) & 0xFF;
                if (current == prevPixel) {
                    count++;
                } else {
                    if (count > 0) {
                        dos.writeByte(prevPixel);
                        dos.writeInt(count);
                    }
                    prevPixel = current;
                    count = 1;
                }
            }
            // rOW KO LAST MEI RUN KRTA HE
            dos.writeByte(prevPixel);
            dos.writeInt(count);
        }

        dos.close();
        fos.close();
    }
}
