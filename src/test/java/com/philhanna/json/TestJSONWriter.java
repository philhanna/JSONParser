package com.philhanna.json;

import java.io.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONParser;
import com.philhanna.json.JSONValue;
import com.philhanna.json.JSONWriter;

public class TestJSONWriter extends BaseTest {

   @Before
   public void setUp() throws Exception {
      super.setUp();
   }

   @After
   public void tearDown() throws Exception {
      super.tearDown();
   }

   @Test
   public void writeCompact() throws Exception {
      final InputStream stream = getClass()
            .getResourceAsStream("/testdata/googlemap.json");
      final Reader reader = new InputStreamReader(stream);
      final JSONParser parser = JSONParser.newParser();
      final JSONValue value = parser.parse(reader);

      final File outputFile = new File(outputDirectory, "googlemap.json");
      final PrintWriter out = new PrintWriter(new FileWriter(outputFile));
      final JSONWriter v = new JSONWriter(value);
      v.writeTo(out);
      out.flush();
      out.close();
   }

   @Test
   public void writePretty() throws Exception {
      final InputStream stream = getClass()
            .getResourceAsStream("/testdata/googlemap.json");
      final Reader reader = new InputStreamReader(stream);
      final JSONParser parser = JSONParser.newParser();
      final JSONValue value = parser.parse(reader);

      final File outputFile = new File(
            outputDirectory,
            "googlemap_pretty.json");
      final PrintWriter out = new PrintWriter(new FileWriter(outputFile));
      final JSONWriter v = new JSONWriter(value);
      v.setPretty(true);
      v.writeTo(out);
      out.flush();
      out.close();
   }

}
