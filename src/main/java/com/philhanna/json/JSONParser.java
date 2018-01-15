package com.philhanna.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;

/**
 * A parser for JSON representations.
 */
public class JSONParser {

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger.getLogger(JSONParser.class);

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns a new instance of the JSON parser
    * @return a JSONParser
    */
   public static JSONParser newParser() {
      return new JSONParser();
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Private constructor to prevent instantiation
    */
   private JSONParser() {
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Parses a JSON string. Creates a <code>StringReader</code> and
    * delegates to the {@link #parse(Reader)} method.
    * @param input a string containing a JSON representation.
    * @return a JSONValue with the objects parsed from the string.
    * @throws JSONException if the JSON string is not well-formed
    * @throws IOException if there is an I/O error
    */
   public JSONValue parse(String input) throws JSONException, IOException {
      final JSONValue output = parse(new StringReader(input));
      return output;
   }

   /**
    * Parses a JSON representation from a file. Creates a
    * <code>FileReader</code> and delegates to the
    * {@link #parse(Reader)} method.
    * @param inputFile a file containing a JSON representation.
    * @return a JSONValue with the objects parsed from the file.
    * @throws JSONException if the JSON string is not well-formed
    * @throws IOException if there is an I/O error
    */
   public JSONValue parse(File inputFile) throws JSONException, IOException {
      final JSONValue output = parse(new FileReader(inputFile));
      return output;
   }

   /**
    * Parses a JSON representation from a <code>Reader</code>.
    * @param reader a <code>Reader</code> containing a JSON
    *        representation.
    * @return a JSONValue with the objects parsed from the file.
    * @throws JSONException if the JSON string is not well-formed
    * @throws IOException if there is an I/O error
    */
   public JSONValue parse(Reader reader) throws JSONException, IOException {
      log.debug("Entry");
      final JSONTokenizer tokenizer = new DefaultJSONTokenizer(reader);
      final String token = tokenizer.readToken();
      if (token == null) {
         log.debug("No tokens found");
         return null;
      }
      final JSONValue value = JSONValue.parse(token, tokenizer);
      log.debug(String.format("Return JSON value of type %s", value.getType()));
      log.debug("Exit");
      return value;
   }

}
