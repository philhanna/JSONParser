package com.philhanna.json;

import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * The superclass of all JSON objects.
 */
public abstract class JSONValue implements Visitable {

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger.getLogger(JSONValue.class);

   private static final String ERRMSG_INVALID_TOKEN = "[%s] is not a valid JSON token";

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Parses a JSON value starting with the specified token. Reads more
    * tokens if necessary (for Object or Array types).
    * @param token the token
    * @param tokenizer the source of tokens
    * @return a JSONValue of the appropriate type
    * @throws JSONException if a parsing error occurs
    * @throws IOException if an I/O error occurs
    */
   public static JSONValue parse(String token, JSONTokenizer tokenizer)
         throws JSONException, IOException {
      log.debug("Entry");
      log.debug(String.format("Token is %s", token));

      // Check for string

      if (JSONString.isString(token)) {
         log.debug("Parsing JSON string");
         final JSONString value = JSONString.parseString(token);
         log.debug(
               String.format(
                     "Exit, returning string value %s",
                     value.toString()));
         return value;
      }

      // Check for number

      if (JSONNumber.isNumber(token)) {
         log.debug("Parsing JSON number");
         final JSONNumber value = JSONNumber.parseNumber(token);
         log.debug(
               String.format(
                     "Exit, returning number value %s",
                     String.valueOf(value.getNumber())));
         return value;
      }

      // Check for object

      if (JSONObject.isObjectStart(token)) {
         log.debug("Parsing JSON object");
         final JSONObject value = JSONObject.parseObject(tokenizer);
         log.debug(
               String.format(
                     "Exit, returning object with %d members",
                     value.size()));
         return value;
      }

      // Check for array

      if (JSONArray.isArrayStart(token)) {
         log.debug("Parsing JSON array");
         final JSONArray value = JSONArray.parseArray(tokenizer);
         log.debug(
               String.format(
                     "Exit, returning array with %d elements",
                     value.size()));
         return value;
      }

      // Check for true

      if (JSONTrue.isTrue(token)) {
         log.debug("Parsing JSON true");
         final JSONTrue value = JSONTrue.VALUE;
         log.debug(String.format("Exit, returning JSON true"));
         return value;
      }

      // Check for false

      if (JSONFalse.isFalse(token)) {
         log.debug("Parsing JSON false");
         final JSONFalse value = JSONFalse.VALUE;
         log.debug(String.format("Exit, returning JSON false"));
         return value;
      }

      // Check for null

      if (JSONNull.isNull(token)) {
         log.debug("Parsing JSON null");
         final JSONNull value = JSONNull.VALUE;
         log.debug(String.format("Exit, returning JSON null"));
         return value;
      }

      // Unknown token type

      final String errmsg = String.format(ERRMSG_INVALID_TOKEN, token);
      log.debug(errmsg);
      throw new JSONException(errmsg);
   }
   // ====================================================================
   // Instance variables
   // ====================================================================

   private final JSONType jSONType;

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSONValue of the specified type
    * @param jSONType the value type, from the <code>Type</code>
    *        enumeration
    */
   JSONValue(JSONType jSONType) {
      this.jSONType = jSONType;
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the value type
    * @return one of the enumerated value types
    */
   public JSONType getType() {
      return jSONType;
   }
}
