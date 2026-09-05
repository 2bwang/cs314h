package assignment;

import org.junit.jupiter.api.Test;

import static assignment.ImageEffect.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

//javac -cp lib/junit-platform-console-standalone-1.10.0.jar -d bin src/assignment/*.java test/assignment/TransformationsTest.java
//java -jar lib/junit-platform-console-standalone-1.10.0.jar --class-path bin --select-class assignment.TransformationsTest

import java.util.ArrayList;

public class TransformationsTest {

    @Test
    public void testInvert() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);

        int[][] pixels = {
                { WHITE, BLACK, WHITE },
                { BLACK, WHITE, BLACK },
                { WHITE, BLACK, WHITE }
        };

        int[][] expected = {
                { BLACK, WHITE, BLACK },
                { WHITE, BLACK, WHITE },
                { BLACK, WHITE, BLACK }
        };

        ImageEffect invertEffect = new Invert();

        int[][] actual = invertEffect.apply(pixels, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    //testing NoRed and RedOnly
    @Test
    public void testRed() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);

        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);

        int[][] pixels = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
        };

        int[][] expected = {
                { BLACK, BLACK, BLACK },
                { BLACK, BLACK, BLACK },
        };

        ImageEffect noRedEffect = new NoRed();
        ImageEffect redEffect = new RedOnly();

        int[][] actual = noRedEffect.apply(pixels, new ArrayList<>());
        actual = redEffect.apply(actual, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    //testing NoBlue and BlueOnly
    @Test
    public void testBlue() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);

        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);

        int[][] pixels = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
        };

        int[][] expected = {
                { BLACK, BLACK, BLACK },
                { BLACK, BLACK, BLACK },
        };

        ImageEffect noEffect = new NoBlue();
        ImageEffect Effect = new BlueOnly();

        int[][] actual = noEffect.apply(pixels, new ArrayList<>());
        actual = Effect.apply(actual, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    //testing NoGreen and GreenOnly
    @Test
    public void testGreen() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);

        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);

        int[][] pixels = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
        };

        int[][] expected = {
                { BLACK, BLACK, BLACK },
                { BLACK, BLACK, BLACK },
        };

        ImageEffect noEffect = new NoGreen();
        ImageEffect Effect = new GreenOnly();

        int[][] actual = noEffect.apply(pixels, new ArrayList<>());
        actual = Effect.apply(actual, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }    

    //testing blackandwhite
    @Test
    public void testBlackAndWhite() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);
        
        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);



        int[][] pixels = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
        };

        int[][] expected = {
                { BLACK, BLACK, BLACK },
                { WHITE, WHITE, WHITE },
        };

        ImageEffect bwEffect = new BlackAndWhite();

        int[][] actual = bwEffect.apply(pixels, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    //test horizontal and vertical reflection
    @Test
    public void testReflects() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);

        int[][] pixels = {
                { BLACK, BLACK, BLACK },
                { WHITE, BLACK, BLACK },
                { WHITE, WHITE, BLACK }
        };

        int[][] expected = {
                { BLACK, WHITE, WHITE },
                { BLACK, BLACK, WHITE },
                { BLACK, BLACK, BLACK }
        };

        ImageEffect vertEffect = new VerticalReflect();
        ImageEffect horizEffect = new HorizontalReflect();

        int[][] actual = vertEffect.apply(pixels, new ArrayList<>());
        actual = horizEffect.apply(actual, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    //testing threshold and jagged array (no jaggedCheck)
    @Test
    public void testThreshold() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);
        
        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);

        //pixels below should threshold convert
        final int ORANGE = makePixel(255, 165, 0);
        final int PURPLE = makePixel(130, 0, 130);
        final int PINK = makePixel(255, 192, 203);
        final int BROWN = makePixel(165, 42, 42);

        int[][] pixels = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
                { ORANGE, PURPLE, PINK, BROWN },
        };

        int[][] expected = {
                { RED, GREEN, BLUE },
                { YELLOW, CYAN, MAGENTA },
                { YELLOW, MAGENTA, WHITE, RED },
        };

        ImageEffect Effect = new Threshold();

        int[][] actual = Effect.apply(pixels, new ArrayList<>());

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                assertEquals(getRed(expected[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected[i][j]), getBlue(actual[i][j]));
            }
        }

    }

    // testing growth and srhink (and jaggedCheck)
    @Test
    public void testGrowShrink() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);
        
        //pixels below should convert to black (sum < 127.5x3 = 382.5)
        final int RED = makePixel(255, 0, 0);
        final int GREEN = makePixel(0, 255, 0);
        final int BLUE = makePixel(0, 0, 255);

        //pixels below should convert to white (sum >= 127.5x3 = 382.5)
        final int YELLOW = makePixel(255, 255, 0);
        final int CYAN = makePixel(0, 255, 255);
        final int MAGENTA = makePixel(255, 0, 255);

        //pixels below should threshold convert
        final int ORANGE = makePixel(255, 165, 0);
        final int PURPLE = makePixel(130, 0, 130);
        final int PINK = makePixel(255, 192, 203);
        final int BROWN = makePixel(165, 42, 42);

        int[][] pixels = {
                { WHITE, WHITE, BLACK },
                { WHITE, WHITE, BLACK },
                { BLACK, BLACK, BLACK, BLACK },
        };

        //shrink should make it a 2x4 (yx) array (jagged check), then double the size into 1x2

        int[][] expected1 = {
                { WHITE, BLACK }
        };

        //grow should double this into a 2x4 array
        int[][] expected2 = {
                { WHITE, WHITE, BLACK, BLACK },
                { WHITE, WHITE, BLACK, BLACK }
        };

        ImageEffect Effect = new Shrink();

        int[][] actual = Effect.apply(pixels, new ArrayList<>());

        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                assertEquals(getRed(expected1[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected1[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected1[i][j]), getBlue(actual[i][j]));
            }
        }

        Effect = new Grow();
        
        actual = Effect.apply(actual, new ArrayList<>());

        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                assertEquals(getRed(expected2[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected2[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected2[i][j]), getBlue(actual[i][j]));
            }
        }
    }

    @Test
    public void testSmooth() {
        final int WHITE = makePixel(255, 255, 255);
        final int BLACK = makePixel(0, 0, 0);
        final int NOTBLACK1 = makePixel(36, 36, 36);
        final int CORNER = makePixel(9, 9, 9);
        final int SIDE = makePixel(6, 6, 6);
        final int CENTER = makePixel(4, 4, 4);

        int[][] pixels = {
                { BLACK, BLACK, BLACK },
                { BLACK, NOTBLACK1, BLACK },
                { BLACK, BLACK, BLACK, BLACK },
        };

        //smooth should make it a 3x4 (yx) array (jagged check), then make all the pixels around the center 4, 6, or 9 depending on where it is

        int[][] expected1 = {
                { CORNER, SIDE, SIDE, BLACK },
                { SIDE, CENTER, CENTER, BLACK },
                { CORNER, SIDE, SIDE, BLACK }
        };

        ImageEffect Effect = new Smooth();

        int[][] actual = Effect.apply(pixels, new ArrayList<>());

        for (int i = 0; i < actual.length; i++) {
            for (int j = 0; j < actual[i].length; j++) {
                assertEquals(getRed(expected1[i][j]), getRed(actual[i][j]));
                assertEquals(getGreen(expected1[i][j]), getGreen(actual[i][j]));
                assertEquals(getBlue(expected1[i][j]), getBlue(actual[i][j]));
            }
        }
    }

}
