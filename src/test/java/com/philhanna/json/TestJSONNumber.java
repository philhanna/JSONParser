package com.philhanna.json;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.philhanna.json.JSONNumber;

/**
 * Unit tests for JSONNumber
 */
public class TestJSONNumber extends BaseTest {

   private static final Logger log = Logger.getLogger(TestJSONNumber.class);

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
   public void testParseInteger() {
      final String token = "123";
      final JSONNumber actual = JSONNumber.parseNumber(token);
      assertEquals(123, actual.getNumber().intValue());
   }

   @Test
   public void testParseDouble() {
      final String token = "123.45";
      final JSONNumber actual = JSONNumber.parseNumber(token);
      assertEquals(123.45, actual.getNumber().doubleValue(), 1e-10);
   }

   @Test
   public void testParseExponential() {
      final String token = "1E+3";
      final JSONNumber actual = JSONNumber.parseNumber(token);
      assertEquals(1000, actual.getNumber().doubleValue(), 1e-10);
   }

   @Test
   public void testParseNegativeExponential() {
      final String token = "1E-03";
      final JSONNumber actual = JSONNumber.parseNumber(token);
      assertEquals(0.001, actual.getNumber().doubleValue(), 1e-10);
   }

   // Tests that are expected to throw an exception

   private void shouldFail(String token) {
      try {
         final JSONNumber actual = JSONNumber.parseNumber(token);
         fail(
               "Should have thrown exception. Got "
                     + actual.getNumber()
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
   public void testNotNumeric() {
      shouldFail("NotNumeric");
   }
}
