package assignment;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * CS 314H Assignment 2 - Random Writing
 *
 * Your task is to implement this RandomWriter class
 */
public class RandomWriter implements TextProcessor {
    private StringBuilder alltext; //users shouldn't be able to access these variables
    private StringBuilder currentString;
    private HashMap<String, ArrayList<Character>> map; 
    private int Level;
    private ArrayList<String> keys;


    public static void main(String[] args) throws IOException {
      //args[0]: input file name
      //args[1]: output file name
      //args[2]: level of analysis (k)
      //args[3]: length of output (length)

      //use checkArgs to check for errors, catch exceptions, print errors and exit

      try {checkArgs(args);}
      catch (IllegalArgumentException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }

      TextProcessor rw = createProcessor(Integer.parseInt(args[2]));
      rw.readText(args[0]);
      rw.writeText(args[1], Integer.parseInt(args[3]));

    }

    public static void checkArgs(String[] args) throws IOException {
      //implement all the error checking here, throw exceptions for each error case
      //args[0]: input file name
      //args[1]: output file name
      //args[2]: level of analysis (k)
      //args[3]: length of output (length)

      //throwing illegalargumentexception for all errors
      //error processing:
      //error 1: wrong number of arguments
      if (args.length != 4) {
        throw new IllegalArgumentException("Error: wrong number of arguments, expected 4 argments. args[0]: input file name, args[1]: output file name, args[2]: level of analysis (k), args[3]: length of output (length)");
      }

      //error 2: wrong argument types (java docs said getClass() returns class type)
      //args[2] and args[3] should be integers but get passed in as Strings, try catch converting them to integers
      try { Integer.parseInt(args[2]); }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException("Error: level of analysis (argument 3) must be an integer");
      }

      try { Integer.parseInt(args[3]); }
      catch (NumberFormatException e) {
        throw new IllegalArgumentException("Error: length of output (argument 4) must be an integer");
      }

      //error 4: source file can be read
      //java.nio has a Path and Files class that can check if a file is readable      
      if (!Files.isReadable(Paths.get(args[0]))) {
        throw new IllegalArgumentException("Error: source file cannot be read");
      }

      //error 5: destination file cannot be written to
      if (!Files.isWritable(Paths.get(args[1]))) {
        throw new IllegalArgumentException("Error: destination file cannot be written to");
      }
      
      
      //error 6: length of output is negative
      if (Integer.parseInt(args[3]) < 0) {
        throw new IllegalArgumentException("Error: length of output must be non-negative");
      }

      //error 3: negative level k or k >= length of input file
      //need to process input file to get length
      //since error 4 has already been checked, we can assume the file can be read and is valid
      BufferedReader br = new BufferedReader(new FileReader(args[0]));
      StringBuilder alltext = new StringBuilder();
      int nextChar;
      while ((nextChar = br.read()) != -1) {alltext.append((char)nextChar);}
      if (Integer.parseInt(args[2]) < 0 || Integer.parseInt(args[2]) >= (alltext.length()-1)) { //-1 because we add a space to the end of each line
        throw new IllegalArgumentException("Error: level of analysis must be non-negative and less than length of input file");
      }
    }

    // Unless you need extra logic here, you might not have to touch this method
    public static TextProcessor createProcessor(int level) {
      return new RandomWriter(level);
    }

    private RandomWriter(int level) {
      // Do whatever you want here
      //using hashmap implementation (attached picture with my brainstorming in the doc, refer to that)
      //basic idea: create hashmap of every string of length level, and the value is an arraylist of all the characters that follow that string in the text. 
      // Then, when generating text, you can randomly select a character from the arraylist for the current string of length level, and append it to the output. 
      // Then, you can update the current string by removing the first character and adding the new character to the end. Repeat this process until you have generated the desired length of text.
      //am i allowed to use ai to autocomplete comments? it knew my exact idea, saves a lot of time
      
      //use this just to initlialize the variables, readtext creates the map, writetext iterates through it
      alltext = new StringBuilder();
      currentString = new StringBuilder();
      map = new HashMap<String, ArrayList<Character>>();
      Level = level;
      keys = new ArrayList<String>();
    }


    public void readText(String inputFilename) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(inputFilename));
      //add a space to the end of each line
      //dont need to bc each new line has /n, which counts as a character
      //need test cases for this

      //readLine() deletes the newline character, have to manually add it back at the end. will have one extra at the last line
      //br.ready() doesn't check for \n
      //instead of using ready, ill try to read the next char in the conditional and assign it to a variable
      int nextChar;
      while ((nextChar = br.read()) != -1) {
        alltext.append((char)nextChar);
      }

      br.close();

      //read text and create hashmap (see below)
      //using hashmap implementation (attached picture with my brainstorming in the doc, refer to that)
      //basic idea: create hashmap of every string of length level, and the value is an arraylist of all the characters that follow that string in the text. 
      // Then, when generating text, you can randomly select a character from the arraylist for the current string of length level, and append it to the output. 
      // Then, you can update the current string by removing the first character and adding the new character to the end. Repeat this process until you have generated the desired length of text.
      //am i allowed to use ai to autocomplete comments? it knew my exact idea, saves a lot of time

      //StringBuilder docs: https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/StringBuilder.html

      //this approach only works for Level > 0, if level = 0 just randomly pick characters from all possible characters in the text. does my current approach work with level = 0?


      //initialize currentString with first level characters
      for (int i = 0; i < Level; i ++) currentString.append(alltext.charAt(i));

      
      //since we add a space at the end of each line, stop iteration at length-1 to ignore the last element (which is a space)
      //currentstring is the string of length level before index i, i is the index of the character to add to the map
      //no longer need to iterate to length-1 because space no longer added to end of each line
      String cur;
      for (int i = Level; i < alltext.length(); i ++) {
        cur = currentString.toString();
        keys.add(cur);

        if (!map.containsKey(cur)) map.put(cur, new ArrayList<Character>());

        map.get(cur).add(alltext.charAt(i));

        //update currentString with character at index i, remove first character
        currentString.append(alltext.charAt(i));
        currentString.deleteCharAt(0);
      }

      //last cur doesn't get added to keys because it shouldn't exist (has extra space that was added to end of last line)
    }

    public void writeText(String outputFilename, int length) throws IOException {
      //reset currentstring and use as string to find next character to add to output
      //pick random starting string
      Random rand = new Random();
      currentString = new StringBuilder(keys.get(rand.nextInt(keys.size()))); //nextInt is exclusive, starts from 0

      //forgot to check if curstring doesn't exist in the map
      //in this case pick a new random starting string without adding it to the output
      //pick a random character from the arraylist of that starting string and add it to the output
      //then instead of making curstring using the last [level-1] characters of the output + the new chararacter,
      //make curstring using the last [level-1] characters of the new key + new character

      //kinda hard to understand so here's an example:
      //level =2, starting string = "abc abd"
      //map: {"ab"=[c, d], "bc"=[ ], "c "=[a], " a"=[b]}
      //lets say the output starts with ab, and picks d as the next character
      //output is now abd, and curstring is bd. but bd doesn't exist in the map
      //so we pick a new starting string, say "bc", and use " " as the next character
      //output is now "abd "
      //but instead of curstring being "d ", it is now "c ", using the new starting string
      //essentially, currentString resets
      //so technically "abddddddddd" is a valid output ;-;

      StringBuilder output = new StringBuilder();
            
      //iterate from 0 instead of level
      Character curChar;
      String curString;
      ArrayList<Character> curValue;
      for (int i = 0; i < length; i++) {
        curString = currentString.toString(); //current key (string of length Level)

        //if curstring doesnt exist in the map, make curString a new random starting string
        if (!map.containsKey(curString)) {
          currentString = new StringBuilder(keys.get(rand.nextInt(keys.size())));
          curString = currentString.toString();
        }

        curValue = map.get(curString); //arraylist of all characters after current key
        curChar = curValue.get(rand.nextInt(curValue.size())); //random character from curValue

        //add curChar to output and update currentString
        output.append(curChar);

        currentString.append(curChar);
        currentString.deleteCharAt(0);
      }

      //write output to file
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename));
      bw.write(output.toString());
      bw.close();
    }

    //used for testing
    public Map<String, ArrayList<Character>> getMap() {
      return map;
    }
}
