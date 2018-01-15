package com.philhanna.json;

/**
 * The JSON <code>false</code> object.
 */
public class JSONFalse extends JSONValue {

   // ====================================================================
   // Class variables and constants
   // ====================================================================

   /**
    * A singleton instance of <code>JSONFalse</code>
    */
   public static final JSONFalse VALUE = new JSONFalse();

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if this token is the string
    * <code>"false"</code>
    * @param token a token
    * @return <code>true</code> or <code>false</code>.
    */
   public static boolean isFalse(String token) {
      return token != null && token.equals("false");
   }

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON <code>false</code> object.
    */
   private JSONFalse() {
      super(JSONType.FALSE);
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the value (boolean <code>false</code>).
    */
   public boolean getValue() {
      return false;
   }

   @Override
   public String toString() {
      return "false";
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }
}
