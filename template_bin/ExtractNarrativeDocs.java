/*
 * ExtractNarrativeDocs.java
 *
 * Copyright 2011 Bio-Behavior Analysis Systems, LLC
 */
import java.io.*;
import java.util.*;

/**
 * The ExtractNarrativeDocs class is a class of static methods.  It implements 
 * a procedural algorithm which is not particularly "object-oriented".  The 
 * algorithm extracts Narrative Documentation comments out of source code files.
 *
 * The Narrative Documentation comments are blocks that start with the "~ND~START~"
 * and end with "~ND~END~".  Optionally, the beginning of a Narrative Documentation 
 * block can also include a block label.  For example, "~ND~START~MyStuff~ is a 
 * Narrative Documentation block with the label MyStuff.
 * 
 * The Narrative Documentation comments are extracted from a source code file
 * and placed in a file with the same name as the source code file, but with ".nd"
 * appended to the end of the file name.  This is the primary output of extraction
 * from a source code file.  
 *
 * If a block of Narrative Documentation is labeled, then in addition to being 
 * extracted to the primary output file, the content of that block is also extracted
 * to a file named with the original file name, followed by the block label, followed
 * by the ".nd" extension.  This output is referred to as the "secondary" output
 * of the extraction process.
 */
public class ExtractNarrativeDocs {

    /**
     * The extension to add to the name of output files created by this 
     * program.
     */
    private static final String OUTPUT_FILE_EXTENSION = ".nd";

    /**
     * The writer which is used to create the primary output for a particular 
     * source code file.
     */
    private static PrintWriter primaryOutput = null;

    /**
     * The writer which is used to create the secondary output for a particular
     * comment block within a source code file.
     */
    private static PrintWriter secondaryOutput = null;

    /**
     * Extract the Narrative Documentation comments from the specified source
     * code file.
     *
     * @param file the source code file from which to extract the Narrative
     *             Documentation files.
     */
    private static void extractFromFile(File file) throws IOException {

        // Notify user of file currently being parsed    
        BufferedReader br = new BufferedReader(new FileReader(file));
        System.out.println("Extracting Narrative Documenation from file: " + file.getName());

        // Notify user of primary output file currently being created
        String primaryOutputName = file.getPath() + OUTPUT_FILE_EXTENSION;
        primaryOutput = new PrintWriter(primaryOutputName);
        System.out.println("Creating Primary Output file: " + primaryOutputName);

        // Read through all the lines in the file and process each one
	String s = br.readLine();
	while (s != null) {
            processLine(file, s);	    
            s = br.readLine();
        }

        // Close the primary output file
        if (primaryOutput != null) {
            primaryOutput.close();
            primaryOutput = null;
        }

        // Close the secondary output file if necessary
        if (secondaryOutput != null) {
            secondaryOutput.close();
            secondaryOutput = null;
        }
        
    }

    /**
     * This boolean keeps track of whether or not we are in a Narrative Document
     * section of the input file currently being processed.
     */
    private static boolean inNdSection = false;

    /**
     * The string which marks the beginning of a Narrative Documentation block
     */
    private static final String ND_START_MARKER = "~ND~START~";

    /**
     * The string which marks the end of a Narrative Documentation block label.
     */
    private static final String ND_LABEL_END_MARKER = "~";

    /**
     * The string which marks the end of a Narrative Documenation block
     */
    private static final String ND_END_MARKER = "~ND~END~";

    /**
     * Set of strings that are suppressed from the extracted output
     */
    private static Set<String> suppressedStrings = new HashSet<String>();

    /**
     * The string which marks a directive to add a suppressed string from
     * the Narrative Documentation
     */
    private static final String ADD_SUPPRESS_MARKER = "~ND~SUPPRESS~";

    /**
     * The string which marks a directive to remove a suppressed string, 
     * i.e. no longer suppress a string from the Narrative Documentation.
     */    
     private static final String REMOVE_SUPPRESS_MARKER = "~ND~UNSUPPRESS~";
    
    /**
     * The array of file name extensions that indicate files that should
     * be processed by this program.
     */
    private static final String[] EXTENSIONS = {".java", ".c"};

    /**
     * Check to see if the string starting at location i in the specified line
     * is a string in the suppression set and therefore should be suppressed.
     * 
     * @return the number of characters from the string starting at location
     *         i that should be suppressed.  0 if location i does not start
     *         a suppressed string.
     */
    private static int checkShouldSuppress(String line, int i) {

        Iterator<String> iter = suppressedStrings.iterator();
        while (iter.hasNext()) {
            String s = iter.next();
            try {
                if (line.substring(i, i+s.length()).equals(s)) {
                    return s.length();
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

        return 0;
    }

    /**
     * Check to see if the string starting at location i in the specified line
     * is a directive to add a string to the suppressed strings set.
     *
     * @return the number of characters from the string starting at location
     *         i that have been handled due to recognition of an add suppression
     *         string directive.  0 if no add suppression string directive is
     *         found.
     */
    private static int checkForAddSuppression(String line, int i) {
        try {
            String s = line.substring(i, i+ADD_SUPPRESS_MARKER.length());
            if (ADD_SUPPRESS_MARKER.equals(s)) {
                // We have encountered the ADD_SUPPRESS_MARKER string
                // Get the string that should be suppressed
                String restOfLine = line.substring(i+ADD_SUPPRESS_MARKER.length());
                int endLocation = restOfLine.indexOf(ND_LABEL_END_MARKER);
                if (endLocation > 0) {
                    String suppressString = restOfLine.substring(0, endLocation);
                    suppressedStrings.add(suppressString);
                    return ADD_SUPPRESS_MARKER.length() + suppressString.length() + 
                           ND_LABEL_END_MARKER.length();
                } 
            } 
            
        } catch (IndexOutOfBoundsException e) {
            
        }

        return 0;
    }

    /**
     * Check to see if the string starting at location i in the specified line
     * is a directive to remove a string to the suppressed strings set.
     *
     * @return the number of characters from the string starting at location
     *         i that have been handled due to recognition of a remove suppression
     *         string directive.  0 if no remove suppression string directive is
     *         found.
     */
    private static int checkForRemoveSuppression(String line, int i) {
        try {
            String s = line.substring(i, i+REMOVE_SUPPRESS_MARKER.length());
            if (REMOVE_SUPPRESS_MARKER.equals(s)) {
                String restOfLine = line.substring(i+REMOVE_SUPPRESS_MARKER.length());
                int endLocation = restOfLine.indexOf(ND_LABEL_END_MARKER);
                if (endLocation > 0) {
                    String unsuppressedString = restOfLine.substring(0, endLocation);
                    suppressedStrings.remove(unsuppressedString);
                    return REMOVE_SUPPRESS_MARKER.length() + unsuppressedString.length() +
                           ND_LABEL_END_MARKER.length();
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }

        return 0;
    }

    /**
     * Process a single line of text from an input file
     *
     * @param file the file from which the line was read
     * @param line the line to process
     */
    public static void processLine(File file, String line) throws IOException {

        // i is the index of the character within the line that we are currently
        // examining and processing
        int i = 0;
        int charsHandled = 0;
        while (i < line.length()) {           

            charsHandled = checkForAddSuppression(line, i);
            if (charsHandled > 0) {
                i = i + charsHandled;
                continue;
            }

            charsHandled = checkForRemoveSuppression(line, i);
            if (charsHandled > 0) {
                i = i + charsHandled;
                continue;
            }

            if (!inNdSection) {
                // Examine the n characters starting with the current character
                // (where n is the length of the ND_START_MARKER string) to see if 
                // contain the ND_START_MARKER string and thus mark the beginning of 
                // a Narrative Documentation comment.
                try {
                    String s = line.substring(i, i+ND_START_MARKER.length());
 
                    if (ND_START_MARKER.equals(s)) {
                        // We have encountered the ND_START_MARKER string.
                        // Therefore we are in a Narrative Documentation section.
                        inNdSection = true;
                        // The label for the section (if it is labeled), will
                        // be the from the end of the marker to the end of label marker.
                        String restOfLine = line.substring(i+ND_START_MARKER.length());
                        int endMarkerLocation = restOfLine.trim().indexOf(ND_LABEL_END_MARKER);
                        if (endMarkerLocation > 0) {
                            // This Narrative Documenation block seems to be labeled.
                            String ndLabel = restOfLine.substring(0, endMarkerLocation);
                            if (!"".equals(ndLabel)) {
                                // If the label string is not blank, then we are 
                                // in a labeled Narrative Documenation section.
                                // So we open a secondary output file.
                                String secondaryOutputName = 
                                    file.getPath() + "." + ndLabel + OUTPUT_FILE_EXTENSION;
                                secondaryOutput = new PrintWriter(secondaryOutputName);
                                // Notify user of secondary output file currently being created
                                System.out.println("Creating Secondary Output file: " + 
                                    secondaryOutputName);
                                // We've now "handled" the characters in the ND_START_MARKER 
                                // and the characters in the ndLabel, so increment the
                                // index telling us what character to look at next and
                                // move on.
                                i = i + ND_START_MARKER.length() + ndLabel.length() + 
                                    ND_LABEL_END_MARKER.length();
                                continue;
                            } else {
                                // We did find an Narrative Documentation block.  We thought we 
                                // found a labeled block, but the label was blank.
                                // So we can just move on passed the found ND_START_MARKER
                                i = i + ND_START_MARKER.length();
                                continue;
                            }

                        } else {
                            // This Narrative Documenation block is unlabeled.
                            // So we move on passed the found ND_START_MARKER
                            i = i + ND_START_MARKER.length();
                            continue;
                        }

                    }
    
                } catch (IndexOutOfBoundsException e) {
                    // We will get an IndexOutOfBoundsException if there are
                    // not enough remaining characters in the line to extract
                    // a substring of length ND_START_MARKER.length().  That being the case,
                    // then we cannot be working with a Narrative Documentation 
                    // marker.
                }
            }
   
            // If we are now in a Narrative Documentation block,
            // we need to check to see if we've reached the end of 
            // that block by looking for the end of narrative documentation 
            // marker.
            if (inNdSection) {
                try {
                    String s = line.substring(i, i+ND_END_MARKER.length());
                    if (ND_END_MARKER.equals(s)) {
                        // We have encountered an end of narrative block marker 
                        // while in the midst of a Narrative Documenation block.
                        // Therefore, we have reached the end of the Narrative 
                        // Documentation block.
                        inNdSection = false;
                        // If we were writing secondary output based on this
                        // being a labeled Narrative Documentation block,
                        // then we need to close that secondary output.
                        if (secondaryOutput != null) {
                            secondaryOutput.close();
                            secondaryOutput = null;
                        }
                        // We found and "handled" the ND_END_MARKER string.
                        // So we've handled the characters in the ND_END_MARKER.
                        // Increment the index telling us what character to look at next
                        // to account for the handled ND_END_MARKER.length() characters.
                        i = i + ND_END_MARKER.length();
                        continue;
                    }
                } catch (IndexOutOfBoundsException e) {
                    // We will get an IndexOutOfBoundsException if there are
                    // not enough remaining characters in the line to extract
                    // a substring of length ND_END_MARKER.length().  That 
                    // being the case, then we cannot be working with an end 
                    // of comment marker.  So we just move on.
                }
            }
                   
            // If we are in a Narrative Documentation block, and we have
            // not reach the end (or beyond the end) of the line, then
            // we need to output the current character to the primary
            // output and, if we have a secondary output, to the secondary 
            // output.
            if ((inNdSection) && (i<line.length())) {
                 int charsToSuppress = checkShouldSuppress(line, i);
                 if (charsToSuppress > 0) {
                     i = i + charsToSuppress;
                     continue;
                 } else {
                     // No string starting at i is a suppressed string.  So output
                     // the character at i and move on.
                     if (primaryOutput != null) primaryOutput.print(line.charAt(i));
                     if (secondaryOutput != null) secondaryOutput.print(line.charAt(i));
                     // We've handled the current character, so move on.
                     i = i + 1;
                     continue;
                 } 
            }

	    // If we get here, then we:
            //
            // 1. Did not encounter an ND_START_MARKER
            // 2. Were not in a Narrative Documentation section and encountered 
            //    an ND_END_MARKER 
            // 3. Were not in a Narrative Documentation section at all, and
            //    there was no need to output the character.
            //
            // So we can just ignore the current character and move on to the 
            // next.
            i = i + 1;
        }

        // If we reached the end of the line and are still in a Narrative 
        // Documentation section, then we need to output a new line to the 
        // respective output files.
	if (inNdSection) {
            if (primaryOutput != null) primaryOutput.println();
            if (secondaryOutput != null) secondaryOutput.println();
        }

    }

    /**
     * This method determines makes the extraction recurse through
     * directory trees.  
     * 
     * If the specified file, f, does not exist, then this method
     * does nothing.
     *
     * If the specified file is a directory, then this method gets a 
     * list of the files in that directory and recursively calls 
     * itself.
     *
     * If the specified file is not a directory, then this method
     * checks to see if it is a file that ends with one of the 
     * file extensions to be handled by this program.  If the 
     * file name ends with one of the appropriate extensions,
     * then the method extractFromFile is called to perform the
     * actual extraction of Narrative Documentation comments.
     *
     * @param f the file or directory from which to extract
     *          Narrative Documentation comments
     */
    private static void extractFrom(File f) throws IOException {

        if (f.exists()) {
            if (f.isDirectory()) {
                 String[] fileNames = f.list();
                 for (int i=0; i<fileNames.length; i++) {
                     extractFrom(new File(f.getPath(), fileNames[i]));
                 }
            } else {
                 String name = f.getName();                 
                 for (int j=0; j<EXTENSIONS.length; j++) {
                     if (name.endsWith(EXTENSIONS[j])) {
                         extractFromFile(f);
                     }
                 }
            }
        }
    }

    /**
     * The main entry point for this program. 
     * 
     * @param args the command line arguments
     *             a list of files and/or directories that should be processed
     *             by this program
     */
    public static void main(String[] args) throws IOException {
        for (int i=0; i<args.length; i++) {
            extractFrom(new File(args[i]));
        }
    }

}
