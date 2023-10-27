/*    */ package org.eclipse.jetty.http;
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
/*    */ public enum HttpComplianceSection
/*    */ {
/* 25 */   CASE_INSENSITIVE_FIELD_VALUE_CACHE("", "Use case insensitive field value cache"), 
/* 26 */   METHOD_CASE_SENSITIVE("https://tools.ietf.org/html/rfc7230#section-3.1.1", "Method is case-sensitive"), 
/* 27 */   FIELD_COLON("https://tools.ietf.org/html/rfc7230#section-3.2", "Fields must have a Colon"), 
/* 28 */   FIELD_NAME_CASE_INSENSITIVE("https://tools.ietf.org/html/rfc7230#section-3.2", "Field name is case-insensitive"), 
/* 29 */   NO_WS_AFTER_FIELD_NAME("https://tools.ietf.org/html/rfc7230#section-3.2.4", "Whitespace not allowed after field name"), 
/* 30 */   NO_FIELD_FOLDING("https://tools.ietf.org/html/rfc7230#section-3.2.4", "No line Folding"), 
/* 31 */   NO_HTTP_0_9("https://tools.ietf.org/html/rfc7230#appendix-A.2", "No HTTP/0.9");
/*    */   
/*    */   final String url;
/*    */   final String description;
/*    */   
/*    */   private HttpComplianceSection(String url, String description)
/*    */   {
/* 38 */     this.url = url;
/* 39 */     this.description = description;
/*    */   }
/*    */   
/*    */   public String getURL()
/*    */   {
/* 44 */     return this.url;
/*    */   }
/*    */   
/*    */   public String getDescription()
/*    */   {
/* 49 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpComplianceSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */