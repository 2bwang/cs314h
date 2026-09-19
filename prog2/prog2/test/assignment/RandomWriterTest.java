package assignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.*;

public class RandomWriterTest {

    @Test
    void testExample() {
        // This is just an example test case
        // You should write more tests

        int a = 1;
        int b = 2;
        int c = 3;

        assertEquals(a + b, c);
    }

    @Test
    void testCreateProcessor() {
        TextProcessor rw = RandomWriter.createProcessor(3);
        assertNotNull(rw); //tests if the randomwriter object is created
    }

    @Test
    void testReadText() throws IOException {
        //test if map is created as expected, given a specific input file
        //inputTest.txt contains "abc abd"
        //expected map for level 2:
        //{"ab"=[c, d]
        // "bc"=[ ]
        // "c "=[a]
        // " a'=[b]}"
        //bd shouldn't be populated because it doesn't have a character after it

        TextProcessor rw = RandomWriter.createProcessor(2);
        rw.readText("test_books/inputTest.txt");

        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("ab", new ArrayList<Character>(Arrays.asList('c', 'd')));
        correct.put("bc", new ArrayList<Character>(Arrays.asList(' ')));
        correct.put("c ", new ArrayList<Character>(Arrays.asList('a')));
        correct.put(" a", new ArrayList<Character>(Arrays.asList('b')));

        assertEquals(correct, ((RandomWriter)rw).getMap());
    }

    @Test
    void testWriteText() throws IOException {
        //inputTest.txt contains "abc abd"
        //expected map for level 2:
        //{"ab"=[c, d]
        // "bc"=[ ]
        // "c "=[a]
        // " a"=[b]}
        //bd shouldn't be populated because it doesn't have a character after it

        //test if the output text is valid (every subscript of length level) is followed by something that is in the map
        //might be a bit slow (arraylist.contains?) 
        //this assumes the randomness is correct, but it should be fine for testing if the output is valid
        //for probabilities, just check if the map is populated correctly (ex. "ab"={c, c, d}), and if the output is valid, then the randomness should be correct
        //assumes Random.nextInt is uniform, which it should 
        
        //first test if map is correct
        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("ab", new ArrayList<Character>(Arrays.asList('c', 'd')));
        correct.put("bc", new ArrayList<Character>(Arrays.asList(' ')));
        correct.put("c ", new ArrayList<Character>(Arrays.asList('a')));
        correct.put(" a", new ArrayList<Character>(Arrays.asList('b')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(2));
        rw.readText("test_books/inputTest.txt");

        assertEquals(correct, rw.getMap());

        //test if output is valid
        //idk how to black box test completely, the best way i can think of is verifying
        //1. output length
        //2. each character in the output exists in the input
        //can't simply check map for each subscript because if the previous chain didn't exist, any character is fair game
        //very big combination of random characters, difficult to account for all accurately
        //ex. for input "abc abd", level 2, output "abddddddddd" is valid because
        //"ab" is followed by "d", but "bd" doesn't exist, so any character in the map is valid including "d". "dd" doesn't exist and the cycle continues
        RandomWriter.main(new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "2", "10"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        char[] output = br.readLine().toCharArray(); //character array of all the output text
        BufferedReader br2 = new BufferedReader(new FileReader("test_books/inputTest.txt"));
        HashSet<Character> inputChars = new HashSet<>();
        char[] input = br2.readLine().toCharArray();
        for (char c :input) inputChars.add(c); //add all characters in the input to a hashset for easy lookup



        boolean valid = true;

        if (output.length != 10) valid = false; //check length

        for (int i = 0; i < output.length; i++) {
            if (!inputChars.contains(output[i])) valid = false; //check if character exists in input
        }

        assertTrue(valid);         
    }

    @Test
    void testLevel0() throws IOException {
        //same logic as testWriteText but for level 0
        //input "abc abd" level 0
        //map should be {"" = [a, b, c,  , a, b, d]}
        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("", new ArrayList<Character>(Arrays.asList('a', 'b', 'c', ' ', 'a', 'b', 'd')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(0));
        rw.readText("test_books/inputTest.txt");

        assertEquals(correct, rw.getMap());

        RandomWriter.main(new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "0", "10"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        char[] output = br.readLine().toCharArray(); //character array of all the output text
        BufferedReader br2 = new BufferedReader(new FileReader("test_books/inputTest.txt"));
        HashSet<Character> inputChars = new HashSet<>();
        char[] input = br2.readLine().toCharArray();
        for (char c :input) inputChars.add(c); //add all characters in the input to a hashset for easy lookup



        boolean valid = true;

        if (output.length != 10) valid = false; //check length

        for (int i = 0; i < output.length; i++) {
            if (!inputChars.contains(output[i])) valid = false; //check if character exists in input
        }

        assertTrue(valid);  
    }

    //test all the input argument error cases
    //error 1: wrong number of args
    @Test
    void testError1() throws IOException {
        //wrong number of args, 5
        String[] badArgs = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "0", "10", "monkey"};

        //lambda [() -> ] used to delay checkArgs from running so it gets handled by assertThrows, otherwise it would throw the error before assertThrows could catch it
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs)); //returns the error thrown
        //can't run main directly bc main would catch this error and just terminate the program
        //but if it raises the error as expected, main would terminate the program correctly

        //check if the error thrown is the correct one
        assertEquals("Error: wrong number of arguments, expected 4 argments. args[0]: input file name, args[1]: output file name, args[2]: level of analysis (k), args[3]: length of output (length)", error.getMessage());
    }

    //error 2: wrong argument types (3 and 4 should be integers)
    //argument 3 first
    @Test
    void testError2p1() throws IOException {
        //arg 3 is a string
        String[] badArgs = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "abc", "10"};

        //lambda [() -> ] used to delay checkArgs from running so it gets handled by assertThrows, otherwise it would throw the error before assertThrows could catch it
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs)); //returns the error thrown
        //can't run main directly bc main would catch this error and just terminate the program
        //but if it raises the error as expected, main would terminate the program correctly

        //check if the error thrown is the correct one
        assertEquals("Error: level of analysis (argument 3) must be an integer", error.getMessage());
    }

    //argument 4 next
    @Test
    void testError2p2() throws IOException {
        //arg 3 is a string
        String[] badArgs = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "0", "abc"};

        //lambda [() -> ] used to delay checkArgs from running so it gets handled by assertThrows, otherwise it would throw the error before assertThrows could catch it
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs)); //returns the error thrown
        //can't run main directly bc main would catch this error and just terminate the program
        //but if it raises the error as expected, main would terminate the program correctly

        //check if the error thrown is the correct one
        assertEquals("Error: length of output (argument 4) must be an integer", error.getMessage());
    }

    //error 4: source file can be read/empty
    //error 5: output file can be written to
    //error 6: length of output is negative
    @Test
    void testError456() throws IOException {
        //error 4
        String[] badArgs = new String[] {"abc.txt", "test_books/outputTest.txt", "0", "10"};
        
        //lambda [() -> ] used to delay checkArgs from running so it gets handled by assertThrows, otherwise it would throw the error before assertThrows could catch it
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs)); //returns the error thrown
        //can't run main directly bc main would catch this error and just terminate the program
        //but if it raises the error as expected, main would terminate the program correctly

        //check if the error thrown is the correct one
        assertEquals("Error: source file cannot be read", error.getMessage());

        //error 5
        String[] badArgs2 = new String[] {"test_books/inputTest.txt", "abc.txt", "0", "10"};
        IllegalArgumentException error2 = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs2));
        assertEquals("Error: destination file cannot be written to", error2.getMessage());
    
        //error 6
        String[] badArgs3 = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "0", "-10"};
        IllegalArgumentException error3 = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs3));
        assertEquals("Error: length of output must be non-negative", error3.getMessage());
    }


    //error 3: negative level k or k >= length of input file
    //negative level k first
    @Test
    void testError3() throws IOException {
        //arg 3 is not in bounds
        String[] badArgs = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "-10", "10"};

        //lambda [() -> ] used to delay checkArgs from running so it gets handled by assertThrows, otherwise it would throw the error before assertThrows could catch it
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs)); //returns the error thrown
        //can't run main directly bc main would catch this error and just terminate the program
        //but if it raises the error as expected, main would terminate the program correctly

        //check if the error thrown is the correct one
        assertEquals("Error: level of analysis must be non-negative and less than length of input file", error.getMessage());

        String[] badArgs2 = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "50000", "10"};
        IllegalArgumentException error2 = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs2));
        assertEquals("Error: level of analysis must be non-negative and less than length of input file", error2.getMessage());

        //empty input file, level 0
        String[] badArgs4 = new String[] {"test_books/emptyInputTest.txt", "test_books/outputTest.txt", "0", "10"};
        IllegalArgumentException error4 = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs4));
        assertEquals("Error: level of analysis must be non-negative and less than length of input file", error4.getMessage());
    }

    //some more edge cases:
    //output length 0 or less than level
    @Test
    void testOutputLength() throws IOException {
        //output length 0
        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("ab", new ArrayList<Character>(Arrays.asList('c', 'd')));
        correct.put("bc", new ArrayList<Character>(Arrays.asList(' ')));
        correct.put("c ", new ArrayList<Character>(Arrays.asList('a')));
        correct.put(" a", new ArrayList<Character>(Arrays.asList('b')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(2));
        rw.readText("test_books/inputTest.txt");

        assertEquals(correct, rw.getMap());

        //output should be blank
        RandomWriter.main(new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "2", "0"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        String output = br.readLine(); //should be null if the file is empty

        assertEquals(null, output);

        
        //output length less than level
        RandomWriter.main(new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "2", "1"} );
        BufferedReader br2 = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        String output2 = br2.readLine();

        assertEquals(1, output2.length());

        // String[] badArgs2 = new String[] {"test_books/inputTest.txt", "test_books/outputTest.txt", "50000", "10"};
        // IllegalArgumentException error2 = assertThrows(IllegalArgumentException.class, () -> RandomWriter.checkArgs(badArgs2));
        // assertEquals("Error: level of analysis must be non-negative and less than length of input file", error2.getMessage());
    }

    //black box testing for input file with all the same letter, 1 line
    @Test 
    void testDup() throws IOException {
        //input file is all the same letter, output file is predictable 

        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("aaa", new ArrayList<Character>(Arrays.asList('a', 'a')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(3));
        rw.readText("test_books/dupTest.txt");

        assertEquals(correct, rw.getMap());

        //output should be aaaaaaa
        RandomWriter.main(new String[] {"test_books/dupTest.txt", "test_books/outputTest.txt", "3", "7"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        String output = br.readLine(); //should be null if the file is empty

        assertEquals("aaaaaaa", output);
    }

    //black box testing for input file with all the same letter, multiple lines
    @Test 
    void testDup2() throws IOException {
        //input file is all the same letter, output file has two possibilies: a\na\na or \na\na\n
        //a\n
        //a\n
        //a\n
        //a\n
        //a

        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("a", new ArrayList<Character>(Arrays.asList('\n', '\n', '\n', '\n')));
        correct.put("\n", new ArrayList<Character>(Arrays.asList('a', 'a', 'a', 'a')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(1));
        rw.readText("test_books/dupTest2.txt");

        assertEquals(correct, rw.getMap());

        //output should be aaaaaaa
        RandomWriter.main(new String[] {"test_books/dupTest2.txt", "test_books/outputTest.txt", "1", "5"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        //can't use readLine since it removes the newLines, have to use read()
        int curChar;
        StringBuilder output = new StringBuilder();
        while ((curChar = br.read()) != -1) {
            output.append((char)curChar);
        }

        //can't use assertEquals because 2 possible outputs, use assertTrue with .equals instead
        assertTrue(output.toString().equals("a\na\na") || output.toString().equals("\na\na\n"));
    }

    //black box testing for input file with only newlines
    @Test 
    void testDup3() throws IOException {
        //input file is all the same letter, output file has two possibilies: a\na\na or \na\na\n
        //\n
        //\n
        //\n

        HashMap<String, ArrayList<Character>> correct = new HashMap<>();
        correct.put("\n", new ArrayList<Character>(Arrays.asList('\n', '\n')));

        RandomWriter rw = (RandomWriter)(RandomWriter.createProcessor(1));
        rw.readText("test_books/dupTest3.txt");

        assertEquals(correct, rw.getMap());

        //output should be aaaaaaa
        RandomWriter.main(new String[] {"test_books/dupTest3.txt", "test_books/outputTest.txt", "1", "5"} );
        BufferedReader br = new BufferedReader(new FileReader("test_books/outputTest.txt"));
        //can't use readLine since it removes the newLines, have to use read()
        int curChar;
        StringBuilder output = new StringBuilder();
        while ((curChar = br.read()) != -1) {
            output.append((char)curChar);
        }

        //can't use assertEquals because 2 possible outputs, use assertTrue with .equals instead
        assertTrue(output.toString().equals("\n\n\n\n\n"));
    }
}
