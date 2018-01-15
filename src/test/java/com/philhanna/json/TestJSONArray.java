package com.philhanna.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONArray;
import com.philhanna.json.JSONNumber;
import com.philhanna.json.JSONString;

/**
 * Unit tests for JSONArray
 */
public class TestJSONArray extends BaseTest {

   @Before
   public void setUp() throws Exception {
      super.setUp();
   }

   @After
   public void tearDown() throws Exception {
      super.tearDown();
   }

   @Test
   public void addsElements() {
      JSONArray array = new JSONArray();
      array.add(new JSONString("Larry"));
      array.add(new JSONString("Curly"));
      array.add(new JSONNumber(1975));
      array.add(new JSONString("Moe"));
      assertEquals(4, array.size());
   }

}
