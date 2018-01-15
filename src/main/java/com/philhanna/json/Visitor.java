package com.philhanna.json;

/**
 * An interface for objects that implement the visitor pattern.
 */
public interface Visitor {

   /**
    * Visits a JSONObject
    * @param value the JSON object
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONObject value) throws JSONException;

   /**
    * Visits a JSONArray
    * @param value the JSON array
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONArray value) throws JSONException;

   /**
    * Visits a JSONString
    * @param value the JSON string
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONString value) throws JSONException;

   /**
    * Visits a JSONNumber
    * @param value the JSON number
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONNumber value) throws JSONException;

   /**
    * Visits a JSONTrue
    * @param value the JSON true
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONTrue value) throws JSONException;

   /**
    * Visits a JSONFalse
    * @param value the JSON false
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONFalse value) throws JSONException;

   /**
    * Visits a JSONNull
    * @param value the JSON null
    * @throws JSONException if an application error occurs
    */
   public void visit(JSONNull value) throws JSONException;

}
