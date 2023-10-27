/*    */ package com.google.common.primitives;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */ final class ParseRequest
/*    */ {
/*    */   final String rawValue;
/*    */   final int radix;
/*    */   
/*    */   private ParseRequest(String rawValue, int radix)
/*    */   {
/* 26 */     this.rawValue = rawValue;
/* 27 */     this.radix = radix;
/*    */   }
/*    */   
/*    */   static ParseRequest fromString(String stringValue) {
/* 31 */     if (stringValue.length() == 0) {
/* 32 */       throw new NumberFormatException("empty string");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 38 */     char firstChar = stringValue.charAt(0);
/* 39 */     int radix; String rawValue; int radix; if ((stringValue.startsWith("0x")) || (stringValue.startsWith("0X"))) {
/* 40 */       String rawValue = stringValue.substring(2);
/* 41 */       radix = 16; } else { int radix;
/* 42 */       if (firstChar == '#') {
/* 43 */         String rawValue = stringValue.substring(1);
/* 44 */         radix = 16; } else { int radix;
/* 45 */         if ((firstChar == '0') && (stringValue.length() > 1)) {
/* 46 */           String rawValue = stringValue.substring(1);
/* 47 */           radix = 8;
/*    */         } else {
/* 49 */           rawValue = stringValue;
/* 50 */           radix = 10;
/*    */         }
/*    */       } }
/* 53 */     return new ParseRequest(rawValue, radix);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\primitives\ParseRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */