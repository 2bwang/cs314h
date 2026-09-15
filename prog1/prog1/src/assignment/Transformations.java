package assignment;
/**
 *
 * CS314H Programming Assignment 1 - Java image processing
 *
 * Included is the Invert effect from the assignment.  Use this as an
 * example when writing the rest of your transformations.  For
 * convenience, you should place all of your transformations in this file.
 *
 * You can compile everything that is needed with
 * javac -d bin src/assignment/*.java
 *
 * You can run the program with
 * java -cp bin assignment.JIP
 *
 * Please note that the above commands assume that you are in the prog1
 * directory.
 */

import java.util.ArrayList;
import static assignment.JC.jaggedCheck;

//utility class for jaggedCheck, had to move it out of ImageEffect.java bc it was messing up the grading
class JC {
    public static int[][] jaggedCheck(int[][] pixels, boolean square) {
        if (pixels == null) return null;

        int height = pixels.length;
        int width = 0;

        //iterate through the rows to find max width
        for (int y = 0; y < height; y++) {
            width = Math.max(width, pixels[y].length);
        }

        //if we want a square array, whichever of height or width is larger is used
        if (square) {
            height = Math.max(height, width);
            width = height;
        }

        //create a new array with the determined dimensions, copy values over
        //have to be careful when iterating through new array, since some indexes might not exist in the original array
        int[][] newPixels = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < pixels.length && x < pixels[y].length) { //check if x and y are in bounds of the original array
                    newPixels[y][x] = pixels[y][x];
                } else {
                    newPixels[y][x] = ImageEffect.makePixel(0,0,0); //fill new spaces with black
                    //will lead to some images being deformed during transformations, but it's a simple solution
                }
            }
        }

        return newPixels;
    }
}

class Invert extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = ~pixels[y][x];
            }
        }
        return pixels;
    }
}

class NoRed extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(0, getGreen(pixels[y][x]), getBlue(pixels[y][x]));
            }
        }
        return pixels;
    }
}

class NoGreen extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(getRed(pixels[y][x]), 0, getBlue(pixels[y][x]));
            }
        }
        return pixels;
    }
}

class NoBlue extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(getRed(pixels[y][x]), getGreen(pixels[y][x]), 0);
            }
        }
        return pixels;
    }
}

class RedOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(getRed(pixels[y][x]), 0, 0);
            }
        }
        return pixels;
    }
}

class GreenOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(0, getGreen(pixels[y][x]), 0);
            }
        }
        return pixels;
    }
}

class BlueOnly extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                pixels[y][x] = makePixel(0, 0, getBlue(pixels[y][x]));
            }
        }
        return pixels;
    }
}

class BlackAndWhite extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int height = pixels.length;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int color = (getRed(pixels[y][x]) + getGreen(pixels[y][x]) + getBlue(pixels[y][x])) / 3;
                if (color < 128) color = 0;
                else color = 255;
                pixels[y][x] = makePixel(color, color, color);
            }
        }
        return pixels;
    }
}

class VerticalReflect extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;

        int height = pixels.length;

        //iterate through the top half of the image, or it will double flip
        //(y, x)
        //ex. for image height 3, (0, 1) becomes (2, 1) but if it runs though the whole image, it will flip it back
        int temp = 0;
        for (int y = 0; y < pixels.length/2; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                temp = pixels[y][x];
                pixels[y][x] = pixels[height - 1 - y][x];
                pixels[height - 1 - y][x] = temp;
            }
        }
        return pixels;
    }
}

class HorizontalReflect extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;

        int height = pixels.length;

        //iterate through the left half of the image, or it will double flip
        //(y, x)
        //ex. for image height 3, (0, 0) becomes (0, 2) but if it runs though the whole image, it will flip it back
        int temp = 0;
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length/2; x++) {
                temp = pixels[y][x];
                pixels[y][x] = pixels[y][pixels[y].length - 1 - x];
                pixels[y][pixels[y].length - 1 - x] = temp;
            }
        }
        return pixels;
    }
}

class Grow extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;

        int[][] copy = new int[2*pixels.length][2*pixels[0].length];

        //for every pixel, replicate it in a 2x2 grid with the original pixel in the top-left corner

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        copy[2*y + i][2*x + j] = pixels[y][x];
                    }
                }
            }
        }
        return copy;
    }
}

class Shrink extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {

        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;


        int width = pixels[0].length;
        int height = pixels.length;

        //edge case: odd dimensions?
        //shrink file by 1 pixel to make it even, may crop some pixels but not noticible
        if (width%2 != 0) width--;
        if (height%2 != 0) height--;


        int[][] copy = new int[height/2][width/2];

        //file should now have even dimensions
        //that means array indecies should max out at odd dimensions (ex. 4x4 should have indices 0-3)
        //according to google, bufferedimage uses a coordinate system where the top-left corner is (0,0)
        //imagine the original image as a grid of 2x2 blocks
        //the bottom right corner of each block is at an odd index (ex. 3,3)
        //we want the bottom right corner of each block because then we can average the pixels to the top and left of it (x-1, y-1) without going out of bounds
        //iterate through the odd indicies in pixels and average the pixels in their block

        for (int x = 1; x < width; x+=2) {
            for (int y = 1; y < height; y+=2) {
                int avgRed = (getRed(pixels[y-1][x-1]) + getRed(pixels[y-1][x]) + getRed(pixels[y][x-1]) + getRed(pixels[y][x])) / 4;
                int avgGreen = (getGreen(pixels[y-1][x-1]) + getGreen(pixels[y-1][x]) + getGreen(pixels[y][x-1]) + getGreen(pixels[y][x])) / 4;
                int avgBlue = (getBlue(pixels[y-1][x-1]) + getBlue(pixels[y-1][x]) + getBlue(pixels[y][x-1]) + getBlue(pixels[y][x])) / 4;

                copy[y/2][x/2] = makePixel(avgRed, avgGreen, avgBlue);
            }
        }
        return copy;
    }
}

class Threshold extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {

        //check if each rgb value is at least 127 and set to 255 if so, 0 if not

        int red = 0;
        int green = 0;
        int blue = 0;

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                if (getRed(pixels[y][x]) >= 128) red = 255; else red = 0;
                if (getGreen(pixels[y][x]) >= 128) green = 255; else green = 0;
                if (getBlue(pixels[y][x]) >= 128) blue = 255; else blue = 0;

                pixels[y][x] = makePixel(red, green, blue);
            }
        }
        return pixels;
    }
}

//smoothing colors in 3x3
class Smooth extends ImageEffect {
    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {

        int[][] rectPixels = jaggedCheck(pixels, false);
        pixels = rectPixels;

        int[][] pixels2 = new int[rectPixels.length][rectPixels[0].length];

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                int avgRed = getRed(pixels[y][x]);
                int avgGreen = getGreen(pixels[y][x]);
                int avgBlue = getBlue(pixels[y][x]);
                int divcounter = 1; //for edge pixels, dont need to divide by 9 but by the number of pixels used
                if (x-1 >= 0) {
                    avgRed += getRed(pixels[y][x-1]);
                    avgBlue += getBlue(pixels[y][x-1]);
                    avgGreen += getGreen(pixels[y][x-1]);
                    divcounter++;
                    if (y-1 >= 0) {
                        avgRed += getRed(pixels[y-1][x-1]);
                        avgBlue += getBlue(pixels[y-1][x-1]);
                        avgGreen += getGreen(pixels[y-1][x-1]);
                        divcounter++;
                    }
                    if (y+1 < pixels.length) {
                        avgRed += getRed(pixels[y+1][x-1]);
                        avgBlue += getBlue(pixels[y+1][x-1]);
                        avgGreen += getGreen(pixels[y+1][x-1]);
                        divcounter++;
                    }
                }
                if (x+1 < pixels[y].length) {
                    avgRed += getRed(pixels[y][x+1]);
                    avgBlue += getBlue(pixels[y][x+1]);
                    avgGreen += getGreen(pixels[y][x+1]);
                    divcounter++;
                    if (y-1 >= 0) {
                        avgRed += getRed(pixels[y-1][x+1]);
                        avgBlue += getBlue(pixels[y-1][x+1]);
                        avgGreen += getGreen(pixels[y-1][x+1]);
                        divcounter++;
                    }
                    if (y+1 < pixels.length) {
                        avgRed += getRed(pixels[y+1][x+1]);
                        avgBlue += getBlue(pixels[y+1][x+1]);
                        avgGreen += getGreen(pixels[y+1][x+1]);
                        divcounter++;
                    }
                }
                if (y-1 >= 0) {
                    avgRed += getRed(pixels[y-1][x]);
                    avgBlue += getBlue(pixels[y-1][x]);
                    avgGreen += getGreen(pixels[y-1][x]);
                    divcounter++;
                }
                if (y+1 < pixels.length) {
                    avgRed += getRed(pixels[y+1][x]);
                    avgBlue += getBlue(pixels[y+1][x]);
                    avgGreen += getGreen(pixels[y+1][x]);
                    divcounter++;
                }
                avgRed /= divcounter;
                avgBlue /= divcounter;
                avgGreen /= divcounter;
                pixels2[y][x] = makePixel(avgRed, avgGreen, avgBlue);
            }
        }
        return pixels2;
    }
}

class Dummy extends ImageEffect {

    public Dummy() {
        super();
        params = new ArrayList<ImageEffectParam>();
        params.add(new ImageEffectParam("ParamName",
                                           "Description of param.",
                                           10, 0, 1000));
    }

    public int[][] apply(int[][] pixels,
                         ArrayList<ImageEffectParam> params) {
        // Use params here.
        return pixels;
    }
}
