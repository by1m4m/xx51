/*    */ package com.google.common.html;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.escape.Escaper;
/*    */ import com.google.common.escape.Escapers;
/*    */ import com.google.common.escape.Escapers.Builder;
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
/*    */ @Beta
/*    */ @GwtCompatible
/*    */ public final class HtmlEscapers
/*    */ {
/*    */   public static Escaper htmlEscaper()
/*    */   {
/* 54 */     return HTML_ESCAPER;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 61 */   private static final Escaper HTML_ESCAPER = Escapers.builder()
/* 62 */     .addEscape('"', "&quot;")
/*    */     
/* 64 */     .addEscape('\'', "&#39;")
/* 65 */     .addEscape('&', "&amp;")
/* 66 */     .addEscape('<', "&lt;")
/* 67 */     .addEscape('>', "&gt;")
/* 68 */     .build();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\html\HtmlEscapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */