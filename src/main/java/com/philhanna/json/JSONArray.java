package com.philhanna.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * A JSON array.
 */
public class JSONArray extends JSONValue implements Iterable<JSONValue> {

   private static enum State {
      LOOKING_FOR_VALUE,
      LOOKING_FOR_COMMA
   }

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger.getLogger(JSONArray.class);

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if the token is "["
    * @param token a token
    * @return <code>true</code> or <code>false</code>
    */
   public static boolean isArrayStart(String token) {
      return token != null && token.equals("[");
   }

   /**
    * Parses the array from elements provided by this tokenizer
    * @param tokenizer a source of tokens
    * @return a JSONArray
    * @throws JSONException if a parsing error occurs
    * @throws IOException if an I/O error occurs
    */
   public static JSONArray parseArray(JSONTokenizer tokenizer)
         throws JSONException, IOException {

      log.debug("Entry");

      // Result

      final JSONArray array = new JSONArray();

      // Element value

      JSONValue value = null;

      // Parsing state

      State state = State.LOOKING_FOR_VALUE;
      log.debug(String.format("Entering state %s", state));

      // Read tokens until the closing bracket is found

      for (;;) {

         // Read the next token

         final String token = tokenizer.readToken();
         log.debug(
               String.format("Received token [%s] in state %s", token, state));
         if (token == null)
            break;

         // Handle the token according to the parser state

         switch (state) {

            case LOOKING_FOR_VALUE: {
               if (token.equals("]")) {
                  log.debug("Found closing bracket for array");
                  log.debug("Exit");
                  return array;
               }
               log.debug("Parsing value");
               value = JSONValue.parse(token, tokenizer);
               log.debug(String.format("Value is type %s", value.getType()));

               array.add(value);
               log.debug(
                     String.format(
                           "Added %s to array elements",
                           value.getType()));

               state = State.LOOKING_FOR_COMMA;
               log.debug(String.format("Entering state %s", state));

               break;
            }

            case LOOKING_FOR_COMMA: {
               if (token.equals("]")) {
                  log.debug("Found closing bracket for array");
                  log.debug("Exit");
                  return array;
               }
               if (token.equals(",")) {
                  log.debug("Parsing comma");
                  state = State.LOOKING_FOR_VALUE;
                  log.debug(String.format("Entering state %s", state));
               }
               else {
                  final String errmsg = String
                        .format("Looking for comma but found %s", token);
                  log.debug(errmsg);
                  throw new JSONException(errmsg);
               }
               break;
            }
         }
      }
      log.debug("Exit");
      return array;
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   private final List<JSONValue> elements = new ArrayList<JSONValue>();

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON array with no elements.
    */
   public JSONArray() {
      super(JSONType.ARRAY);
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns an iterator over the list of values.
    * @return an iterator over an ordered <code>List</code> of JSON
    *         values
    */
   public Iterator<JSONValue> iterator() {
      return elements.iterator();
   }

   /**
    * Adds an element to the array.
    * @param element a JSONValue to be added to the array
    */
   public void add(JSONValue element) {
      elements.add(element);
   }

   /**
    * Returns the number of elements in the array.
    * @return the element count
    */
   public int size() {
      return elements.size();
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (int i = 0; i < elements.size(); i++) {
         if (i > 0)
            sb.append(",");
         sb.append(elements.get(i).toString());
      }
      sb.append("]");
      final String output = sb.toString();
      return output;
   }
}
