package com.philhanna.json;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONArray;
import com.philhanna.json.JSONFalse;
import com.philhanna.json.JSONNull;
import com.philhanna.json.JSONNumber;
import com.philhanna.json.JSONObject;
import com.philhanna.json.JSONString;
import com.philhanna.json.JSONTrue;
import com.philhanna.json.JSONType;

/**
 * Unit tests for JSONValue
 */
public class TestJSONValue extends BaseTest {

   @Before
   public void setUp() throws Exception {
      super.setUp();
   }

   @After
   public void tearDown() throws Exception {
      super.tearDown();
   }

   @Test
   public void getsTypes() {
      assertEquals(JSONType.ARRAY, new JSONArray().getType());
      assertEquals(JSONType.STRING, new JSONString("Whatever").getType());
      assertEquals(JSONType.NUMBER, new JSONNumber(25).getType());
      assertEquals(JSONType.OBJECT, new JSONObject().getType());
      assertEquals(JSONType.ARRAY, new JSONArray().getType());
      assertEquals(JSONType.TRUE, JSONTrue.VALUE.getType());
      assertEquals(JSONType.FALSE, JSONFalse.VALUE.getType());
      assertEquals(JSONType.NULL, JSONNull.VALUE.getType());
   }

}
