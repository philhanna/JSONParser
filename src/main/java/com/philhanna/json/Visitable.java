package com.philhanna.json;

/**
 * An interface for objects that can interact with a visitor.
 */
public interface Visitable {

   /**
    * Accept a visitor. Implementations should simply call <code>
    * <pre>
    * visitor.visit(this);
    * </pre>
    * </code>
    * @param visitor the visitor
    * @throws JSONException if an application error occurs
    */
   public void accept(Visitor visitor) throws JSONException;
}
