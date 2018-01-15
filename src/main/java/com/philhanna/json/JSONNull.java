package com.philhanna.json;

/**
 * The JSON <code>null</code> object.
 */
public class JSONNull extends JSONValue {

   // ====================================================================
   // Class variables and constants
   // ====================================================================

   /**
    * A singleton instance of <code>JSONNull</code>
    */
   public static final JSONNull VALUE = new JSONNull();

   // ====================================================================
   // Class variables and constants
   // ====================================================================

   /**
    * Returns <code>true</code> if this token is the string
    * <code>"null"</code>
    * @param token a token
    * @return <code>true</code> or <code>false</code>.
    */
   public static boolean isNull(String token) {
      return token != null && token.equals("null");
   }

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON null object.
    */
   private JSONNull() {
      super(JSONType.NULL);
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the value (a <code>null</code>).
    */
   public Object getValue() {
      return null;
   }

   @Override
   public String toString() {
      return "null";
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }
}
