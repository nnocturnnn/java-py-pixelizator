package world.ucode;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Algorithms {
    public static void applyAlgorithms(BufferedImage image, int pixel, int num) {
        switch (num) {
            case 0: {
                rectangle(image, pixel);
                break;
            }
            default: System.out.println("Invalid algorithms type");
        }
    }

    public static String getAlgorithm(int num) {
            return "rectangle";
    }
    public static void rectangle(BufferedImage image, int pixSize) {
        assert image != null;
        Raster src = image.getData();
        WritableRaster dest = src.createCompatibleWritableRaster();
        for(int y = 0; y < src.getHeight(); y += pixSize) {
            for(int x = 0; x < src.getWidth(); x += pixSize) {
                int[] pixel = new int[3];
                pixel = src.getPixel(x, y, pixel);
                for(int yd = y; (yd < y + pixSize) && (yd < dest.getHeight()); yd++) {
                    for(int xd = x; (xd < x + pixSize) && (xd < dest.getWidth()); xd++)
                        dest.setPixel(xd, yd, pixel);
                }
            }
        }
        image.setData(dest);
    }
}
