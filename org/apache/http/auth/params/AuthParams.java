/*    */ package org.apache.http.auth.params;
/*    */ 
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public final class AuthParams
/*    */ {
/*    */   public static String getCredentialCharset(HttpParams params)
/*    */   {
/* 58 */     if (params == null) {
/* 59 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 61 */     String charset = (String)params.getParameter("http.auth.credential-charset");
/*    */     
/* 63 */     if (charset == null) {
/* 64 */       charset = "US-ASCII";
/*    */     }
/* 66 */     return charset;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void setCredentialCharset(HttpParams params, String charset)
/*    */   {
/* 77 */     if (params == null) {
/* 78 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*    */     }
/* 80 */     params.setParameter("http.auth.credential-charset", charset);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\auth\params\AuthParams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */