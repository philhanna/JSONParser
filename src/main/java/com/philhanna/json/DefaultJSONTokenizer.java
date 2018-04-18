package com.philhanna.json;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import org.apache.log4j.Logger;

/**
 * Tokenizes a JSON representation from a <code>Reader</code>.
 */
public class DefaultJSONTokenizer implements JSONTokenizer {

   /**
    * Enumerated parsing states
    */
   private enum State {
      BETWEEN_TOKENS,
      READING_STRING,
      READING_ESCAPE_SEQUENCE,
      READING_LITERAL,
      READING_NUMBER,
      READING_UNICODE_HEX_DIGITS,
   }

   // ====================================================================
   // Class constants and variables
   // ====================================================================

   private static final Logger log = Logger
         .getLogger(DefaultJSONTokenizer.class);

   private static final String ERRMSG_CONTROL_CHAR = "Control character 0x%04x is not allowed inside the JSON string";
   private static final String ERRMSG_ESCAPE = "0x%04x is not a valid escape character inside %s";
   private static final String ERRMSG_UNICODE_SHORT = "Only %d hex digits found in %s. Must be exactly 4";
   private static final String ERRMSG_NUMBER = "%s is not a valid numeric literal";
   private static final String ERRMSG_BAD_LITERAL = "%s is not a valid JSON literal (true|false|null)";

   // ====================================================================
   // Class methods
   // ====================================================================

   /**
    * Returns <code>true</code> if the specified character could be part
    * of a JSON number literal.
    * @param c the character
    */
   static final boolean isNumberCharacter(int c) {
      return stringContains("0123456789.-+eE", c);
   }

   /**
    * Returns <code>true</code> if the specified character can legally
    * follow a backslash inside a JSON string.
    * @param c the character
    */
   static final boolean isEscapedCharacter(int c) {
      return stringContains("\"\\/bfnrtu", c);
   }

   /**
    * Returns <code>true</code> if the specified
    * character is a hexadecimal digit
    * @param c the character
    */
   static final boolean isHexDigit(int c) {
      return stringContains("0123456789ABCDEFabcdef", c);
   }

   /**
    * Returns <code>true</code> if the specified string contains the
    * specified character.
    * @param s the string
    * @param c the character
    * @return <code>true</code> or <code>false</code>
    */
   private static final boolean stringContains(String s, int c) {
      for (int i = 0, n = s.length(); i < n; i++) {
         if (s.charAt(i) == c)
            return true;
      }
      return false;
   }

   // ====================================================================
   // Instance variables
   // ====================================================================

   private final PushbackReader in;
   private String lastToken;

   // ====================================================================
   // Constructors
   // ====================================================================

   /**
    * Creates a new JSON tokenizer for the specified input reader.
    * @param in an input reader
    */
   public DefaultJSONTokenizer(Reader in) {
      this.in = new PushbackReader(in);
   }

   // ====================================================================
   // Implementation of JSONTokenizer
   // ====================================================================

   @Override
   public String readToken() throws JSONException, IOException {

      // Reread last token, if one has been unread

      if (lastToken != null) {
         String token = lastToken;
         lastToken = null;
         log.debug(String.format("Returning pushed-back token [%s]", token));
         return token;
      }

      final StringBuilder sb = new StringBuilder();
      int hexDigitCount = 0;
      String token = null;
      State state = State.BETWEEN_TOKENS;
      outer: for (;;) {

         int c = in.read();
         if (c == -1) {
            break outer;
         }
         if (log.isTraceEnabled()) {
            log.trace(String.format("State=%s,c=%c", state, c));
         }

         switch (state) {

            case BETWEEN_TOKENS:
               if (Character.isWhitespace(c))
                  ;
               else if (c == '{') {
                  token = "{";
                  break outer;
               }
               else if (c == '[') {
                  token = "[";
                  break outer;
               }
               else if (c == ',') {
                  token = ",";
                  break outer;
               }
               else if (c == ':') {
                  token = ":";
                  break outer;
               }
               else if (c == ']') {
                  token = "]";
                  break outer;
               }
               else if (c == '}') {
                  token = "}";
                  break outer;
               }
               else if (c == '"') {
                  sb.append((char) c);
                  state = State.READING_STRING;
               }
               else if (isNumberCharacter(c)) {
                  sb.append((char) c);
                  state = State.READING_NUMBER;
               }
               else {
                  sb.append((char) c);
                  state = State.READING_LITERAL;
               }
               break;

            case READING_STRING:
               if (c == '"') {
                  sb.append((char) c);
                  final String string = sb.toString();
                  token = string;
                  break outer;
               }
               else if (c == '\\') {
                  sb.append((char) c);
                  state = State.READING_ESCAPE_SEQUENCE;
               }
               else if (c >= '\u0000' && c <= '\u001F') {
                  final String errmsg = String.format(ERRMSG_CONTROL_CHAR, c);
                  log.trace(errmsg);
                  throw new JSONException(errmsg);
               }
               else {
                  sb.append((char) c);
                  state = State.READING_STRING;
               }
               break;

            case READING_ESCAPE_SEQUENCE:
               if (c == 'u') {
                  sb.append((char) c);
                  hexDigitCount = 0;
                  state = State.READING_UNICODE_HEX_DIGITS;
               }
               else if (isEscapedCharacter(c)) {
                  sb.append((char) c);
                  state = State.READING_STRING;
               }
               else {
                  final String errmsg = String
                        .format(ERRMSG_ESCAPE, c, sb.toString());
                  log.trace(errmsg);
                  throw new JSONException(errmsg);
               }
               break;

            case READING_UNICODE_HEX_DIGITS:
               hexDigitCount++;
               if (hexDigitCount > 4) {
                  in.unread(c);
                  state = State.READING_STRING;
               }
               else if (isHexDigit(c)) {
                  sb.append((char) c);
                  state = State.READING_UNICODE_HEX_DIGITS;
               }
               else {
                  final String errmsg = String.format(
                        ERRMSG_UNICODE_SHORT,
                        hexDigitCount,
                        sb.toString());
                  log.trace(errmsg);
                  throw new JSONException(errmsg);
               }
               break;

            case READING_NUMBER:
               if (isNumberCharacter(c)) {
                  sb.append((char) c);
                  state = State.READING_NUMBER;
               }
               else {
                  in.unread(c);
                  final String number = sb.toString();
                  try {
                     Double.parseDouble(number);
                  }
                  catch (NumberFormatException e) {
                     final String errmsg = String.format(ERRMSG_NUMBER, number);
                     log.trace(errmsg, e);
                     throw new JSONException(errmsg, e);
                  }
                  token = number;
                  break outer;
               }
               break;

            case READING_LITERAL:
               if (Character.isLetter(c)) {
                  sb.append((char) c);
                  state = State.READING_LITERAL;
               }
               else {
                  in.unread(c);
                  final String literal = sb.toString();
                  if (literal.equals("true")
                        || literal.equals("false")
                        || literal.equals("null")) {
                     token = literal;
                     break outer;
                  }
                  final String errmsg = String
                        .format(ERRMSG_BAD_LITERAL, literal);
                  log.trace(errmsg);
                  throw new JSONException(errmsg);
               }
               break;
         }
      }

      // Return null at end of file

      log.debug(String.format("Returning token [%s]", token));
      return token;
   }

   @Override
   public void unread(String token) {
      lastToken = token;
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Closes the tokenizer and the underlying input reader
    * @throws IOException
    */
   public void close() throws IOException {
      in.close();
   }
}
