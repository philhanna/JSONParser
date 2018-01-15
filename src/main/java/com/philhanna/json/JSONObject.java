package com.philhanna.json;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A JSON object.
 */
public class JSONObject extends JSONValue {

   private static enum State {
      LOOKING_FOR_KEY,
      LOOKING_FOR_COLON,
      LOOKING_FOR_VALUE,
      LOOKING_FOR_COMMA
   }

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger.getLogger(JSONObject.class);

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if this is the first token in a JSON
    * object (i.e., a "{").
    * @param token the token
    * @return <code>true</code> or <code>false</code>
    */
   public static boolean isObjectStart(String token) {
      return token != null && token.equals("{");
   }

   /**
    * Creates a JSON object from tokens provided by the specified
    * tokenizer.
    * @param tokenizer the source of tokens
    * @return a JSONObject
    * @throws JSONException if the JSON syntax is invalid
    * @throws IOException if an I/O error occurs
    */
   public static JSONObject parseObject(JSONTokenizer tokenizer)
         throws JSONException, IOException {

      log.debug("Entry");

      // Result

      final JSONObject object = new JSONObject();

      // Member key and value

      JSONString key = null;
      JSONValue value = null;

      // Parsing state

      State state = State.LOOKING_FOR_KEY;
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

            case LOOKING_FOR_KEY: {
               if (token.equals("}")) {
                  log.debug("Found closing bracket for object");
                  log.debug("Exit");
                  return object;
               }
               log.debug("Parsing key");
               key = JSONString.parseString(token);
               log.debug(String.format("Found key %s", key.getString()));
               state = State.LOOKING_FOR_COLON;
               log.debug(String.format("Entering state %s", state));
               break;
            }

            case LOOKING_FOR_COLON: {
               if (token.equals(":")) {
                  log.debug("Parsing colon");
                  state = State.LOOKING_FOR_VALUE;
                  log.debug(String.format("Entering state %s", state));
               }
               else {
                  final String errmsg = String
                        .format("Looking for colon but found %s", token);
                  log.debug(errmsg);
                  throw new JSONException(errmsg);
               }
               break;
            }

            case LOOKING_FOR_VALUE: {
               log.debug("Parsing value");
               value = JSONValue.parse(token, tokenizer);
               log.debug(String.format("Value is type %s", value.getType()));

               object.put(key.getString(), value);
               log.debug(
                     String.format(
                           "Added %s->%s to object members",
                           key.getString(),
                           value.getType()));

               state = State.LOOKING_FOR_COMMA;
               log.debug(String.format("Entering state %s", state));

               break;
            }

            case LOOKING_FOR_COMMA: {
               if (token.equals("}")) {
                  log.debug("Found closing bracket for object");
                  log.debug("Exit");
                  return object;
               }
               if (token.equals(",")) {
                  log.debug("Parsing comma");
                  state = State.LOOKING_FOR_KEY;
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
      return object;
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   /**
    * The object's key/value pairs.
    */
   private Map<String, JSONValue> members = new LinkedHashMap<String, JSONValue>();

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON object
    */
   public JSONObject() {
      super(JSONType.OBJECT);
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the underlying key set for this object
    */
   public Set<String> keySet() {
      return members.keySet();
   }

   /**
    * Sets a key/value pair in the object
    * @param string a Java String
    * @param value a JSONValue
    */
   public void put(String string, JSONValue value) {
      members.put(string, value);
   }

   /**
    * Returns the value mapped to the specified key
    * @param key the key
    * @return a JSONValue, or <code>null</code>, if the key is not
    *         present in the object
    */
   public JSONValue get(String key) {
      return members.get(key);
   }

   /**
    * Returns the number of members in the object
    * @return the number of members in the object
    */
   public int size() {
      return members.size();
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("{");
      int n = 0;
      for (final String javaKey : members.keySet()) {
         n++;
         if (n > 1)
            sb.append(",");
         final JSONString key = new JSONString(javaKey);
         final JSONValue value = members.get(javaKey);
         sb.append(key);
         sb.append(":");
         sb.append(value);
      }
      sb.append("}");
      final String output = sb.toString();
      return output;

   }
}
