package com.philhanna.json;

/**
 * Base class for all JSON exceptions
 */
public class JSONException extends Exception {

   private static final long serialVersionUID = 1L;

   /**
    * Returns a new JSON exception
    */
   public JSONException() {
      super();
   }

   /**
    * Returns a new JSON exception with the specified detail message
    * @param message the detail message
    */
   public JSONException(String message) {
      super(message);
   }

   /**
    * Returns a new JSON exception with the specified root cause
    * exception
    * @param cause the root cause exception
    */
   public JSONException(Throwable cause) {
      super(cause);
   }

   /**
    * Returns a new JSON exception with the specified detail message and
    * root cause exception
    * @param message the detail message
    * @param cause the root cause exception
    */
   public JSONException(String message, Throwable cause) {
      super(message, cause);
   }

}
