import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) {
        String imagePath = "aaa.jpg";
        System.out.println("aa");

        try {
            // Read the image using ImageIO
            // Read the image using ImageIO
            BufferedImage originalImage = ImageIO.read(new File(imagePath));

            // Define the maximum dimensions (e.g., 235x235)
            int maxHeight = 120;

            // Calculate the new width and height while maintaining the aspect ratio
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int newWidth, newHeight;

            newHeight = maxHeight;
            newWidth = (int) ((double) originalWidth / originalHeight * newHeight);

            // Scale the image
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
            BufferedImage scaledBufferedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            scaledBufferedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

            // Access the scaled image data as an array
            int width = scaledBufferedImage.getWidth();
            int height = scaledBufferedImage.getHeight();
            int[] imageData = new int[width * height];
            scaledBufferedImage.getRGB(0, 0, width, height, imageData, 0, width);

            // Now, you can work with the imageData array for further processing
            int[] grayScaledData = new int[width * height];
            // For example, you can print the pixel values of the scaled image
            StringBuilder asciiArt = new StringBuilder();
            // for loop with index
            for (int i = 0; i < imageData.length; i++) {
                int red = (imageData[i] >> 16) & 0xFF;
                int green = (imageData[i] >> 8) & 0xFF;
                int blue = imageData[i] & 0xFF;
                // int grayScaled = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                int grayScaled = (red + green + blue) / 3;
                grayScaledData[i] = transformNumber(grayScaled);
                // System.out.println(grayScaled);
            }
            String density = "Ñ@#W$9876543210?!abc;:+=-,._ ";
            // break the density into an array
            char[] densityArray = density.toCharArray();
            reverseCharArray(densityArray);
            int splitRatio = (255 / densityArray.length) + 1;
            int widthOrHeight = Math.max(width, height);
            for (int i = 0; i < grayScaledData.length; i++) {
                try {
                    int index = grayScaledData[i] / splitRatio;
                    if (i % widthOrHeight == 0) {
                        System.out.println();
                        asciiArt.append(System.lineSeparator());
                    }
                    System.out.print(densityArray[index]);
                    asciiArt.append(densityArray[index]);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            String outputFilePath = "ascii_art.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
                writer.write(asciiArt.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    public static void reverseCharArray(char[] arr) {
        int start = 0;
        int end = arr.length - 1;

        while (start < end) {
            // Swap the characters at the start and end positions
            char temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;

            // Move the pointers towards the center
            start++;
            end--;
        }
    }

    public static int transformNumber(int number) {
        // tweaking the multiplier will change the darkness of the image
        double darkMultiplier = 1.05;
        double lightMultiplier = 0.8;
        int darkLimit = 215;
        int lightLimit = 70;
        if (number * darkMultiplier > 255) {
            return 255;
        }
        if (number < lightLimit) {
            return (int) (number * lightMultiplier);
        }
        if (number < darkLimit) {
            return (int) (number * darkMultiplier);
        }

        return 0;
    }
}

// javac Main.java && java Main
