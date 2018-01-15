package com.philhanna.json;

import java.io.IOException;

/**
 * Creates tokens from a JSON representation
 */
public interface JSONTokenizer {

   /**
    * Returns the next JSON token
    * @return a string containing the token
    * @throws JSONException if the syntax is invalid
    * @throws IOException if an I/O error occurs
    */
   public String readToken() throws JSONException, IOException;

   /**
    * Pushes back a single token so that it will be returned again on
    * the next call to <code>readToken()</code>.
    * @param token the token to unread
    */
   public void unread(String token);

}