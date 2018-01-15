package com.philhanna.json;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONException;
import com.philhanna.json.JSONParser;
import com.philhanna.json.JSONValue;

/**
 * Unit tests for JSONParser
 */
public class TestJSONParser extends BaseTest {

   // ==================================================================
   // Class constants and variables
   // ==================================================================

   private static final Logger log = Logger.getLogger(TestJSONParser.class);

   // ==================================================================
   // Class methods
   // ==================================================================

   // ==================================================================
   // Instance variables
   // ==================================================================

   private JSONParser parser;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @Before
   public void setUp() throws Exception {
      super.setUp();
      parser = JSONParser.newParser();
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
   public void testParseString() throws JSONException, IOException {

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
      log.debug(String.format("Parsing %s", input));
      parser.parse(input);
   }

   @Test
   public void testParseFile() throws JSONException, IOException {
      final InputStream stream = getClass()
            .getResourceAsStream("/testdata/googlemap.json");
      final JSONValue jsonValue = parser.parse(new InputStreamReader(stream));
      assertNotNull(jsonValue);
   }

}
