/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class NativeString
/*     */   implements CharSequence, Comparable
/*     */ {
/*     */   static final String WIDE_STRING = "--WIDE-STRING--";
/*     */   private Pointer pointer;
/*     */   private String encoding;
/*     */   
/*     */   private class StringMemory
/*     */     extends Memory
/*     */   {
/*  31 */     public StringMemory(long size) { super(); }
/*     */     
/*  33 */     public String toString() { return NativeString.this.toString(); }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NativeString(String string)
/*     */   {
/*  41 */     this(string, Native.getDefaultStringEncoding());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NativeString(String string, boolean wide)
/*     */   {
/*  53 */     this(string, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NativeString(WString string)
/*     */   {
/*  60 */     this(string.toString(), "--WIDE-STRING--");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NativeString(String string, String encoding)
/*     */   {
/*  67 */     if (string == null) {
/*  68 */       throw new NullPointerException("String must not be null");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  73 */     this.encoding = encoding;
/*  74 */     if (this.encoding == "--WIDE-STRING--") {
/*  75 */       int len = (string.length() + 1) * Native.WCHAR_SIZE;
/*  76 */       this.pointer = new StringMemory(len);
/*  77 */       this.pointer.setWideString(0L, string);
/*     */     }
/*     */     else {
/*  80 */       byte[] data = Native.getBytes(string, encoding);
/*  81 */       this.pointer = new StringMemory(data.length + 1);
/*  82 */       this.pointer.write(0L, data, 0, data.length);
/*  83 */       this.pointer.setByte(data.length, (byte)0);
/*     */     }
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  88 */     return toString().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other)
/*     */   {
/*  93 */     if ((other instanceof CharSequence)) {
/*  94 */       return compareTo(other) == 0;
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 100 */     boolean wide = this.encoding == "--WIDE-STRING--";
/* 101 */     String s = wide ? "const wchar_t*" : "const char*";
/* 102 */     s = s + "(" + (wide ? this.pointer.getWideString(0L) : this.pointer.getString(0L, this.encoding)) + ")";
/* 103 */     return s;
/*     */   }
/*     */   
/*     */   public Pointer getPointer() {
/* 107 */     return this.pointer;
/*     */   }
/*     */   
/*     */   public char charAt(int index) {
/* 111 */     return toString().charAt(index);
/*     */   }
/*     */   
/*     */   public int length() {
/* 115 */     return toString().length();
/*     */   }
/*     */   
/*     */   public CharSequence subSequence(int start, int end) {
/* 119 */     return CharBuffer.wrap(toString()).subSequence(start, end);
/*     */   }
/*     */   
/*     */   public int compareTo(Object other)
/*     */   {
/* 124 */     if (other == null) {
/* 125 */       return 1;
/*     */     }
/* 127 */     return toString().compareTo(other.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\NativeString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */