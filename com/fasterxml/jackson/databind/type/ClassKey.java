/*    */ package com.fasterxml.jackson.databind.type;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public final class ClassKey
/*    */   implements Comparable<ClassKey>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private String _className;
/*    */   private Class<?> _class;
/*    */   private int _hashCode;
/*    */   
/*    */   public ClassKey()
/*    */   {
/* 37 */     this._class = null;
/* 38 */     this._className = null;
/* 39 */     this._hashCode = 0;
/*    */   }
/*    */   
/*    */   public ClassKey(Class<?> clz)
/*    */   {
/* 44 */     this._class = clz;
/* 45 */     this._className = clz.getName();
/* 46 */     this._hashCode = this._className.hashCode();
/*    */   }
/*    */   
/*    */   public void reset(Class<?> clz)
/*    */   {
/* 51 */     this._class = clz;
/* 52 */     this._className = clz.getName();
/* 53 */     this._hashCode = this._className.hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compareTo(ClassKey other)
/*    */   {
/* 66 */     return this._className.compareTo(other._className);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 78 */     if (o == this) return true;
/* 79 */     if (o == null) return false;
/* 80 */     if (o.getClass() != getClass()) return false;
/* 81 */     ClassKey other = (ClassKey)o;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 90 */     return other._class == this._class;
/*    */   }
/*    */   
/* 93 */   public int hashCode() { return this._hashCode; }
/*    */   
/* 95 */   public String toString() { return this._className; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\ClassKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */