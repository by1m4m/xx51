/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.nio.CharBuffer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class WString
/*    */   implements CharSequence, Comparable
/*    */ {
/*    */   private String string;
/*    */   
/*    */   public WString(String s)
/*    */   {
/* 23 */     if (s == null) throw new NullPointerException("String initializer must be non-null");
/* 24 */     this.string = s;
/*    */   }
/*    */   
/* 27 */   public String toString() { return this.string; }
/*    */   
/*    */   public boolean equals(Object o) {
/* 30 */     return ((o instanceof WString)) && (toString().equals(o.toString()));
/*    */   }
/*    */   
/* 33 */   public int hashCode() { return toString().hashCode(); }
/*    */   
/*    */   public int compareTo(Object o) {
/* 36 */     return toString().compareTo(o.toString());
/*    */   }
/*    */   
/* 39 */   public int length() { return toString().length(); }
/*    */   
/*    */   public char charAt(int index) {
/* 42 */     return toString().charAt(index);
/*    */   }
/*    */   
/* 45 */   public CharSequence subSequence(int start, int end) { return CharBuffer.wrap(toString()).subSequence(start, end); }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\WString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */