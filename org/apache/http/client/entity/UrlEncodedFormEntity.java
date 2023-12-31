/*    */ package org.apache.http.client.entity;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.util.List;
/*    */ import org.apache.http.NameValuePair;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.client.utils.URLEncodedUtils;
/*    */ import org.apache.http.entity.StringEntity;
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
/*    */ @NotThreadSafe
/*    */ public class UrlEncodedFormEntity
/*    */   extends StringEntity
/*    */ {
/*    */   public UrlEncodedFormEntity(List<? extends NameValuePair> parameters, String encoding)
/*    */     throws UnsupportedEncodingException
/*    */   {
/* 59 */     super(URLEncodedUtils.format(parameters, encoding), encoding);
/* 60 */     setContentType("application/x-www-form-urlencoded; charset=" + (encoding != null ? encoding : "ISO-8859-1"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UrlEncodedFormEntity(List<? extends NameValuePair> parameters)
/*    */     throws UnsupportedEncodingException
/*    */   {
/* 73 */     this(parameters, "ISO-8859-1");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\entity\UrlEncodedFormEntity.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */