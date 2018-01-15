package com.philhanna.json;

import java.io.PrintWriter;

/**
 * A visitor that write a JSON representation in string form
 */
public class JSONWriter implements Visitor {

   // ====================================================================
   // Instance variables
   // ====================================================================

   private final JSONValue value;
   private PrintWriter out;
   private boolean pretty;
   private int indent = 0;

   // ====================================================================
   // Constructors
   // ====================================================================

   public JSONWriter(JSONValue value) {
      this.value = value;
   }

   // ====================================================================
   // Instance methods
   // ====================================================================

   /**
    * Writes the JSON object to an output stream
    * @param out a PrintWriter
    * @throws JSONException if an application error occurs
    */
   public void writeTo(PrintWriter out) throws JSONException {
      this.out = out;
      this.indent = 0;
      value.accept(this);
   }

   /**
    * Returns <code>true</code> if the pretty attribute is true
    * @return the pretty attribute
    */
   public boolean isPretty() {
      return pretty;
   }

   /**
    * Sets the pretty attribute
    * @param pretty the pretty to set
    */
   public void setPretty(boolean pretty) {
      this.pretty = pretty;
   }

   // ====================================================================
   // Implementation of Visitor
   // ====================================================================

   /**
    * Prints an object
    */
   @Override
   public void visit(JSONObject value) throws JSONException {
      printOpenBracket("{");
      int i = 0;
      for (final String key : value.keySet()) {
         i++;
         if (i > 1)
            printComma();
         printIndent();
         final JSONString string = new JSONString(key);
         string.accept(this);
         printColon();
         final JSONValue memberValue = value.get(key);
         memberValue.accept(this);
      }
      printCloseBracket("}");
   }

   @Override
   public void visit(JSONArray value) throws JSONException {
      printOpenBracket("[");
      int i = 0;
      for (final JSONValue element : value) {
         i++;
         if (i > 1)
            printComma();
         printIndent();
         element.accept(this);
      }
      printCloseBracket("]");
   }

   @Override
   public void visit(JSONString value) {
      out.print(value.toString());
   }

   @Override
   public void visit(JSONNumber value) {
      out.print(value.toString());
   }

   @Override
   public void visit(JSONTrue value) {
      out.print(value.toString());
   }

   @Override
   public void visit(JSONFalse value) {
      out.print(value.toString());
   }

   @Override
   public void visit(JSONNull value) {
      out.print(value.toString());
   }

   // ====================================================================
   // Private instance methods
   // ====================================================================

   private String getIndent() {
      final StringBuilder sb = new StringBuilder();
      for (int i = 0; i < indent; i++)
         sb.append("  ");
      final String output = sb.toString();
      return output;
   }

   private void printOpenBracket(String b) {
      out.print(b);
      if (pretty) {
         out.print("\n");
         indent++;
      }
   }

   private void printCloseBracket(String b) {
      if (pretty) {
         out.print("\n");
         indent--;
         out.print(getIndent());
      }
      out.print(b);
   }

   private void printComma() {
      out.print(",");
      if (pretty)
         out.print("\n");
   }

   private void printColon() {
      out.print(pretty
            ? " : "
            : ":");
   }

   private void printIndent() {
      if (pretty)
         out.print(getIndent());
   }
}
