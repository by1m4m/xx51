/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public class StringArray
/*    */   extends Memory
/*    */   implements Function.PostCallRead
/*    */ {
/*    */   private String encoding;
/* 26 */   private List natives = new ArrayList();
/*    */   private Object[] original;
/*    */   
/*    */   public StringArray(String[] strings) {
/* 30 */     this(strings, false);
/*    */   }
/*    */   
/*    */   public StringArray(String[] strings, boolean wide) {
/* 34 */     this((Object[])strings, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
/*    */   }
/*    */   
/*    */   public StringArray(String[] strings, String encoding) {
/* 38 */     this((Object[])strings, encoding);
/*    */   }
/*    */   
/*    */ 
/* 42 */   public StringArray(WString[] strings) { this(strings, "--WIDE-STRING--"); }
/*    */   
/*    */   private StringArray(Object[] strings, String encoding) {
/* 45 */     super((strings.length + 1) * Pointer.SIZE);
/* 46 */     this.original = strings;
/* 47 */     this.encoding = encoding;
/* 48 */     for (int i = 0; i < strings.length; i++) {
/* 49 */       Pointer p = null;
/* 50 */       if (strings[i] != null) {
/* 51 */         NativeString ns = new NativeString(strings[i].toString(), encoding);
/* 52 */         this.natives.add(ns);
/* 53 */         p = ns.getPointer();
/*    */       }
/* 55 */       setPointer(Pointer.SIZE * i, p);
/*    */     }
/* 57 */     setPointer(Pointer.SIZE * strings.length, null);
/*    */   }
/*    */   
/*    */   public void read() {
/* 61 */     boolean returnWide = this.original instanceof WString[];
/* 62 */     boolean wide = this.encoding == "--WIDE-STRING--";
/* 63 */     for (int si = 0; si < this.original.length; si++) {
/* 64 */       Pointer p = getPointer(si * Pointer.SIZE);
/* 65 */       Object s = null;
/* 66 */       if (p != null) {
/* 67 */         s = wide ? p.getWideString(0L) : p.getString(0L, this.encoding);
/* 68 */         if (returnWide) s = new WString((String)s);
/*    */       }
/* 70 */       this.original[si] = s;
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString() {
/* 75 */     boolean wide = this.encoding == "--WIDE-STRING--";
/* 76 */     String s = wide ? "const wchar_t*[]" : "const char*[]";
/* 77 */     s = s + Arrays.asList(this.original);
/* 78 */     return s;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\StringArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */