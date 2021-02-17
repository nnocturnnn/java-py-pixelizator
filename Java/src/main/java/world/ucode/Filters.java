package world.ucode;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Filters {

    public static void applyFilters(BufferedImage image, int num) {

        if (num > 0 && num <= 2) {
            switch (num) {
                case 1: {gray(image); break;}
                case 2: {red(image); break;}
                default: break;
            }
        } else if (num != 0) {
            System.out.println("Invalid filter type");
        }
    }

    public static String getFilter(int num) {
        switch (num) {
            case 0: return "none";
            case 1: return "gray";
            case 2: return "red";
            default: return "unknown";
        }
    }

    private static void gray(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));

                int avr = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                Color newPixel = new Color(avr, avr, avr);

                image.setRGB(x, y, newPixel.getRGB());
            }
        }
    }

    private static void red(BufferedImage image) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));

                Color newPixel = new Color(255, c.getGreen(), c.getBlue());

                image.setRGB(x, y, newPixel.getRGB());
            }
        }
    }

}

