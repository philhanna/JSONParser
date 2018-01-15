package com.philhanna.json;

/**
 * A JSON number.
 */
public class JSONNumber extends JSONValue {

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Converts a JSON number string into a Java number.
    * @param token a JSON number token
    * @return a JSONNumber instance
    * @throws NumberFormatException if the token is invalid
    */
   public static JSONNumber parseNumber(String token) {
      final Number number = Double.parseDouble(token);
      return new JSONNumber(number);
   }

   /**
    * Returns <code>true</code> if this token represents a valid JSON
    * number
    * @param token a token
    * @return <code>true</code> or <code>false</code>
    */
   public static boolean isNumber(String token) {
      try {
         parseNumber(token);
         return true;
      }
      catch (NumberFormatException e) {
         return false;
      }
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   private final Number number;

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON number
    * @param number the number
    */
   public JSONNumber(Number number) {
      super(JSONType.NUMBER);
      this.number = number;
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the number value
    */
   public Number getNumber() {
      return number;
   }

   @Override
   public String toString() {
      String value = String.valueOf(number);
      if (value.endsWith(".0"))
         value = String.valueOf(number.intValue());
      return value;
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }
}
