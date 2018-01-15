package com.philhanna.json;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.DefaultJSONTokenizer;
import com.philhanna.json.JSONException;

import java.io.*;

/**
 * Unit tests for DefaultJSONTokenizer
 */
public class TestDefaultJSONTokenizer extends BaseTest {

   // ==================================================================
   // Class constants and variables
   // ==================================================================

   private static final Logger log = Logger
         .getLogger(TestDefaultJSONTokenizer.class);

   // ==================================================================
   // Class methods
   // ==================================================================

   // ==================================================================
   // Instance variables
   // ==================================================================

   // ==================================================================
   // Fixtures
   // ==================================================================

   @Before
   public void setUp() throws Exception {
      super.setUp();
   }

   @After
   public void tearDown() throws Exception {
      super.tearDown();
   }

   // ==================================================================
   // Helper methods
   // ==================================================================

   // ==================================================================
   // Unit tests
   // ==================================================================

   @Test
   public void testStudents() throws Exception {

      final InputStream stream = getClass()
            .getResourceAsStream("/testdata/students.json");
      final Reader reader = new InputStreamReader(stream);
      final DefaultJSONTokenizer jt = new DefaultJSONTokenizer(reader);
      try {
         final String[] expectedTokens = {
               "{",
               "\"Students\"",
               ":",
               "[",
               "{",
               "\"Name\"",
               ":",
               "\"Alfred\"",
               ",",
               "\"Sex\"",
               ":",
               "\"M\"",
               ",",
               "\"Age\"",
               ":",
               "14", };
         for (int i = 0; i < expectedTokens.length; i++) {
            final String expectedToken = expectedTokens[i];
            final String actualToken = jt.readToken();
            log.debug(
                  String.format(
                        "Token %d expected=\"%s\",actual=\"%s\"",
                        i,
                        expectedToken,
                        actualToken));
            assertNotNull(
                  "Tokenizer reached premature end of file",
                  actualToken);
            assertEquals(expectedToken, actualToken);
         }
      }
      finally {
         jt.close();
      }
   }

   @Test
   public void printGoogleMap() throws Exception {
      final InputStream stream = getClass()
            .getResourceAsStream("/testdata/googlemap.json");
      final Reader reader = new InputStreamReader(stream);
      final DefaultJSONTokenizer jt = new DefaultJSONTokenizer(reader);
      int n = 0;
      for (;;) {
         final String token = jt.readToken();
         if (token == null)
            break;
         n++;
         log.debug(String.format("Token %04d is %s", n, token));
      }
      jt.close();
   }

   @Test
   public void handlesUnread() throws Exception {
      final String input = "{\n"
            + "   \"results\" : [\n"
            + "      {\n"
            + "         \"elevation\" : 114.5410537719727,\n"
            + "         \"location\" : {\n"
            + "            \"lat\" : 35.82,\n"
            + "            \"lng\" : -78.75\n"
            + "         },\n"
            + "         \"resolution\" : 4.771975994110107\n"
            + "      }\n"
            + "   ],\n"
            + "   \"status\" : \"OK\"\n"
            + "}";

      final DefaultJSONTokenizer tokenizer = new DefaultJSONTokenizer(
            new StringReader(input));
      try {
         int n = 0;
         int repeatCount = 0;
         for (;;) {
            final String token = tokenizer.readToken();
            if (token == null)
               break;
            if (token.equals("\"lat\"")) {
               repeatCount++;
               if (repeatCount <= 3) {
                  tokenizer.unread(token);
               }
               else {
                  repeatCount = 0;
               }
            }
            log.debug(String.format("%04d: %s\n", ++n, token));
         }
      }
      finally {
         tokenizer.close();
      }
   }

   // Some unit tests that are expected to pass

   private void tokenizeValidJSON(String input) throws Exception {
      final Reader in = new StringReader(input);
      final DefaultJSONTokenizer jt = new DefaultJSONTokenizer(in);
      try {
         for (;;) {
            final String token = jt.readToken();
            if (token == null)
               break;
            log.debug(String.format("Token is %s", token));
         }
      }
      finally {
         jt.close();
      }
   }

   @Test
   public void handlesEmptyInput() throws Exception {
      tokenizeValidJSON("");
   }

   @Test
   public void handlesEmptyString() throws Exception {
      tokenizeValidJSON("\"\"");
   }

   @Test
   public void handlesJustWhitespace() throws Exception {
      tokenizeValidJSON("\t  \n\r");
   }

   @Test
   public void handlesEmptyObject() throws Exception {
      tokenizeValidJSON("{}");
   }

   @Test
   public void handlesEmptyArray() throws Exception {
      tokenizeValidJSON("  [  ]  ");
   }

   @Test
   public void handlesMultipleObjects() throws Exception {
      tokenizeValidJSON("{ \"number\" : 12 }, { \"location\" : \"Africa\" }");
   }

   // Now some unit tests that are expected to fail

   private void tokenizeInvalidJSON(String input) throws Exception {
      final Reader in = new StringReader(input);
      final DefaultJSONTokenizer jt = new DefaultJSONTokenizer(in);
      try {
         for (;;) {
            final String token = jt.readToken();
            if (token == null)
               break;
         }
         fail("Should have thrown exception due to invalid number");
      }
      catch (JSONException wasExpected) {
         log.debug(
               String.format(
                     "Expected exception was thrown: %s",
                     wasExpected.getMessage()));
      }
      finally {
         jt.close();
      }
   }

   @Test
   public void catchesBadNumberFormat() throws Exception {
      tokenizeInvalidJSON("{ \"age\" : 3+3+3 }");
   }

   @Test
   public void catchesShortUnicode() throws Exception {
      tokenizeInvalidJSON("{ \"\\ue5\" }");
   }

   @Test
   public void catchesInvalidJSONCharacter() throws Exception {
      tokenizeInvalidJSON("{ TRUE }");
   }

}
