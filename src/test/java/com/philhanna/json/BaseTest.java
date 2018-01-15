package com.philhanna.json;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Abstract base class for unit tests
 */
public abstract class BaseTest {

   // ==================================================================
   // Class constants and variables
   // ==================================================================

   // Logger for this class

   private static final Logger log = Logger.getLogger(BaseTest.class);

   // Output directory name

   private static final String OUTPUT_DIRECTORY = System
         .getProperty("java.io.tmpdir") + "/json/output";

   // Properties file used by unit tests

   private static final Properties testProperties = new Properties();
   private static final String PROPERTIES_FILE_NAME = "/unitTest.properties";
   private static final String LOCAL_PROPERTIES_FILE_NAME = "/%s.properties";

   // ==================================================================
   // Class methods
   // ==================================================================

   /**
    * Load the unit test properties and any local overrides
    */
   @BeforeClass
   public static void loadProperties() throws Exception {

      // Create the base unit test properties file

      final URL defaultURL = BaseTest.class.getResource(PROPERTIES_FILE_NAME);
      if (defaultURL != null) {
         final InputStream in = defaultURL.openStream();
         testProperties.load(in);
         in.close();
      }

      // Load any overrides for this machine

      final String localhost = InetAddress.getLocalHost().getHostName();
      final String localFile = String
            .format(LOCAL_PROPERTIES_FILE_NAME, localhost);
      final URL localURL = BaseTest.class.getResource(localFile);
      if (localURL == null) {
         log.debug(
               String.format(
                     "No %s file found. Using only default properties",
                     localFile));
      }
      else {

         // Load the local properties

         final Properties localProperties = new Properties();
         final InputStream in = localURL.openStream();
         localProperties.load(in);
         in.close();

         // Override in the default properties

         for (final String key : localProperties.stringPropertyNames()) {
            final String value = localProperties.getProperty(key);
            testProperties.setProperty(key, value);
         }
      }
   }

   // ==================================================================
   // Instance variables
   // ==================================================================

   protected File outputDirectory;

   // ==================================================================
   // Fixtures
   // ==================================================================

   @Before
   public void setUp() throws Exception {
      if (outputDirectory == null) {
         outputDirectory = new File(OUTPUT_DIRECTORY).getCanonicalFile();
         outputDirectory.mkdirs();
      }
   }

   @After
   public void tearDown() throws Exception {
   }

   // ==================================================================
   // Helper methods
   // ==================================================================

   /**
    * Returns the property with a given key, or throws an exception if
    * it is not found.
    * @param key the absolute property name
    * @return the property value
    * @throws IllegalArgumentException if the property is not found
    */
   protected String getProperty(String key) {
      final String value = testProperties.getProperty(key);
      if (value == null) {
         final String errmsg = String.format("%s property not found", key);
         log.error(errmsg);
         throw new IllegalArgumentException(errmsg);
      }
      return value;
   }

   /**
    * Returns the string property with the given key suffix. The prefix
    * is the simple name of the calling class (no package) plus "." to
    * separate it from the suffix.
    * @param keySuffix the key suffix
    * @return the property
    * @throws IllegalArgumentException if the property is not found
    */
   protected String getStringProperty(String keySuffix) {
      final String key = getClass().getSimpleName() + "." + keySuffix;
      return getProperty(key);
   }

   /**
    * Returns the integer property with the given key suffix. The prefix
    * is the simple name of the calling class (no package) plus "." to
    * separate it from the suffix.
    * @param keySuffix the key suffix
    * @return the property
    * @throws IllegalArgumentException if the property is not found
    * @throws NumberFormatException if the property is not an integer
    */
   protected Integer getIntegerProperty(String keySuffix) {
      final String value = getStringProperty(keySuffix);
      final Integer numericValue = new Integer(value);
      return numericValue;
   }
}