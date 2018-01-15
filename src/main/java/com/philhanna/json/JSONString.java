package com.philhanna.json;

import org.apache.log4j.Logger;

/**
 * A JSON string.
 */
public class JSONString extends JSONValue {

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger.getLogger(JSONString.class);

   private static final String ERRMSG_NO_STARTING_QUOTE = "[%s] is not a valid JSON string because it does not start with a quote character";
   private static final String ERRMSG_NO_CLOSING_QUOTE = "[%s] is not a valid JSON string because it does not end with a quote character";
   private static final String ERRMSG_BAD_CHAR = "0x%04x is not a valid escape character in %s";
   private static final String ERRMSG_BAD_HEX = "Invalid hexadecimal digit 0x%04x in unicode escape sequence in %s";
   private static final String ERRMSG_ENDS_WITH_BACKSLASH = "Backslash not followed by escape character in %s";
   private static final String ERRMSG_UNICODE_TOO_SHORT = "Only %d hexadecimal digits in unicode escape sequence in %s. Must be exactly 4";

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if this token is a valid JSONString
    * @param token a Java string
    * @return <code>true</code> or <code>false</code>
    */
   public static boolean isString(String token) {
      return token != null && token.startsWith("\"");
   }

   /**
    * Converts the specified character to a unicode escape sequence
    * @param c the character
    * @return the string BACKSLASH + "u" + four hex digits
    */
   static String toUnicode(char c) {
      return String.format("\\u%04x", (int) c);
   }

   /**
    * Converts a JSON string to its Java equivalent.
    * <ul>
    * <li>Removes the enclosing quotes</li>
    * <li>Replaces escaped characters with their unicode
    * equivalents</li>
    * </ul>
    * @param token a string enclosed in quotes, with perhaps embedded
    *        escaped characters.
    * @return a JSON string
    * @throws JSONException if the string is not a valid JSON string
    */
   public static final JSONString parseString(String token)
         throws JSONException {

      if (!token.startsWith("\"")) {
         final String errmsg = String.format(ERRMSG_NO_STARTING_QUOTE, token);
         log.debug(errmsg);
         throw new JSONException(errmsg);
      }

      if (!token.endsWith("\"")) {
         final String errmsg = String.format(ERRMSG_NO_CLOSING_QUOTE, token);
         log.debug(errmsg);
         throw new JSONException(errmsg);
      }

      // Drop the surrounding quotes

      token = token.substring(1, token.length() - 1);

      // Remove embedded escape characters, if any

      if (token.contains("\\")) {
         final StringBuffer sb = new StringBuffer();
         final int n = token.length();
         int state = 0;
         int hexDigitCount = 0;
         int hexValue = 0;
         for (int i = 0; i < n; i++) {
            int c = token.charAt(i);
            switch (state) {

               case 0: // Reading ordinary characters
                  if (c == '\\') {
                     state = 1;
                  }
                  else {
                     sb.append((char) c);
                  }
                  break;

               case 1: // Reading the character after a backslash
                  if (c == '"') {
                     sb.append('"');
                     state = 0;
                  }
                  else if (c == '\\') {
                     sb.append('\\');
                     state = 0;
                  }
                  else if (c == '/') {
                     sb.append('/');
                     state = 0;
                  }
                  else if (c == 'b') {
                     sb.append('\b');
                     state = 0;
                  }
                  else if (c == 'f') {
                     sb.append('\f');
                     state = 0;
                  }
                  else if (c == 'n') {
                     sb.append('\n');
                     state = 0;
                  }
                  else if (c == 'r') {
                     sb.append('\r');
                     state = 0;
                  }
                  else if (c == 't') {
                     sb.append('\t');
                     state = 0;
                  }
                  else if (c == 'u') {
                     hexDigitCount = 0;
                     hexValue = 0;
                     state = 2;
                  }
                  else {
                     final String errmsg = String
                           .format(ERRMSG_BAD_CHAR, c, token);
                     log.debug(errmsg);
                     throw new JSONException(errmsg);
                  }
                  break;

               case 2: // Reading a unicode escape sequence
                  if (hexDigitCount < 4) {
                     int value = Character.digit(c, 16);
                     if (value < 0) {
                        final String errmsg = String
                              .format(ERRMSG_BAD_HEX, c, token);
                        log.debug(errmsg);
                        throw new JSONException(errmsg);
                     }
                     hexValue *= 16;
                     hexValue += value;
                     hexDigitCount++;
                     if (hexDigitCount == 4) {
                        sb.append((char) hexValue);
                        state = 0;
                     }
                  }
                  break;
            }
         }

         // Evaluate final state

         switch (state) {
            case 0: // OK
            {
               break;
            }
            case 1: // After a backslash
            {
               final String errmsg = String
                     .format(ERRMSG_ENDS_WITH_BACKSLASH, token);
               log.debug(errmsg);
               throw new JSONException(errmsg);
            }
            case 2: // Reading hex digits
            {
               final String errmsg = String
                     .format(ERRMSG_UNICODE_TOO_SHORT, hexDigitCount, token);
               log.debug(errmsg);
               throw new JSONException(errmsg);
            }
         }

         token = sb.toString();
      }

      return new JSONString(token);
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   private final String string;

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON string from a Java <code>String</code> object
    * @param string the Java string value
    */
   public JSONString(String string) {
      super(JSONType.STRING);
      this.string = string;
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Returns the Java <code>String</code> value
    */
   public String getString() {
      return string;
   }

   @Override
   public String toString() {
      final StringBuilder sb = new StringBuilder();
      sb.append("\"");
      for (int i = 0, n = string.length(); i < n; i++) {
         char c = string.charAt(i);
         switch (c) {
            case '"':
               sb.append("\\\"");
               break;
            case '\\':
               sb.append("\\\\");
               break;
            case '/':
               sb.append("\\/");
               break;
            case '\b':
               sb.append("\\b");
               break;
            case '\f':
               sb.append("\\f");
               break;
            case '\n':
               sb.append("\\n");
               break;
            case '\r':
               sb.append("\\r");
               break;
            case '\t':
               sb.append("\\t");
               break;
            default:
               if (c >= '\u0020' && c <= '\u00fe') {
                  sb.append(c);
               }
               else {
                  sb.append(toUnicode(c));
               }
               break;
         }
      }
      sb.append("\"");
      final String output = sb.toString();
      return output;
   }

   @Override
   public void accept(Visitor visitor) throws JSONException {
      visitor.visit(this);
   }
}
