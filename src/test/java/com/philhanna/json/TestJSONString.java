package com.philhanna.json;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONException;
import com.philhanna.json.JSONString;

/**
 * Unit tests for JSONString
 */
public class TestJSONString extends BaseTest {

   private static final Logger log = Logger.getLogger(TestJSONString.class);

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
   // Unit tests
   // ==================================================================

   @Test
   public void testParseGoodString() throws JSONException {
      final String token = "\"Hello, world!\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("Hello, world!", actual.getString());
   }

   @Test
   public void testParseEmbeddedQuotes() throws JSONException {
      final String token = "\"George Herman \\\"Babe\\\" Ruth\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("George Herman \"Babe\" Ruth", actual.getString());
   }

   @Test
   public void testParseBackslash() throws JSONException {
      final String token = "\"C:\\\\temp\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("C:\\temp", actual.getString());
   }

   @Test
   public void testParseSlash() throws JSONException {
      final String token = "\"path\\/home\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("path/home", actual.getString());
   }

   @Test
   public void testParseBackspace() throws JSONException {
      final String token = "\"back\\bspace\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("back\bspace", actual.getString());
   }

   @Test
   public void testParseFormFeed() throws JSONException {
      final String token = "\"form\\ffeed\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("form\ffeed", actual.getString());
   }

   @Test
   public void testParseLineFeed() throws JSONException {
      final String token = "\"line\\nfeed\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("line\nfeed", actual.getString());
   }

   @Test
   public void testParseCarriageReturn() throws JSONException {
      final String token = "\"c\\rr\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("c\rr", actual.getString());
   }

   @Test
   public void testParseTab() throws JSONException {
      final String token = "\"tab\\tseparated\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("tab\tseparated", actual.getString());
   }

   @Test
   public void testGoodUnicode() throws JSONException {
      final String token = "\"Should be a slash (\\u002F)\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("Should be a slash (/)", actual.getString());
   }

   @Test
   public void testUnicodeFollowedByDigit() throws JSONException {
      final String token = "\"Unicode \\u000ABC\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("Unicode \nBC", actual.getString());
   }

   @Test
   public void testBackToBackUnicode() throws JSONException {
      final String token = "\"Unicode \\u000A\\u000D\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("Unicode \n\r", actual.getString());
   }

   @Test
   public void testAllTogether() throws JSONException {
      final String token = "\"\\\"\\\\\\/\\b\\f\\n\\r\\t\"";
      final JSONString actual = JSONString.parseString(token);
      assertEquals("\"\\/\b\f\n\r\t", actual.getString());
   }

   @Test
   public void testToUnicode() {
      assertEquals("\\u002f", JSONString.toUnicode('/'));
      assertEquals("\\uffff", JSONString.toUnicode((char) -1));
   }

   // Tests that are expected to throw an exception

   private void shouldFail(String token) {
      try {
         final JSONString actual = JSONString.parseString(token);
         fail(
               "Should have thrown exception. Got "
                     + actual.getString()
                     + " instead");
      }
      catch (Exception isExpected) {
         log.debug(
               String.format(
                     "Got expected exception %s",
                     isExpected.getMessage()));
      }
   }

   @Test
   public void testNoStartingQuote() {
      shouldFail("Bogus\"");
   }

   @Test
   public void testNoEndingQuote() {
      shouldFail("\"Bogus");
   }

   @Test
   public void testUnicodeTooShort() {
      shouldFail("\"\\u23 x\"");
   }

   @Test
   public void testParseBogusEscape() {
      shouldFail("\"hover\\xhex\"");
   }

   @Test
   public void testBogusWhitespace() {
      shouldFail(" \"Bogus whitespace\" ");
   }
}
