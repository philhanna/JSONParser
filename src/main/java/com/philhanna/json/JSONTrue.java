package com.philhanna.json;

/**
 * The JSON <code>true</code> object.
 */
public class JSONTrue extends JSONValue {

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   /**
    * A singleton instance of <code>JSONTrue</code>
    */
   public static final JSONTrue VALUE = new JSONTrue();

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if this token is the string
    * <code>"true"</code>
    * @param token a token
    * @return <code>true</code> or <code>false</code>.
    */
   public static boolean isTrue(String token) {
      return token != null && token.equals("true");
   }

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON true object.
    */
   private JSONTrue() {
      super(JSONType.TRUE);
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the value (boolean <code>true</code>).
    */
   public boolean getValue() {
      return true;
   }

   @Override
   public String toString() {
      return "true";
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }
}
