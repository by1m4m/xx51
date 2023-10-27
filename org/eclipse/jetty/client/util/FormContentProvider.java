/*    */ package org.eclipse.jetty.client.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLEncoder;
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.nio.charset.UnsupportedCharsetException;
/*    */ import java.util.Iterator;
/*    */ import org.eclipse.jetty.util.Fields;
/*    */ import org.eclipse.jetty.util.Fields.Field;
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
/*    */ public class FormContentProvider
/*    */   extends StringContentProvider
/*    */ {
/*    */   public FormContentProvider(Fields fields)
/*    */   {
/* 38 */     this(fields, StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */   public FormContentProvider(Fields fields, Charset charset)
/*    */   {
/* 43 */     super("application/x-www-form-urlencoded", convert(fields, charset), charset);
/*    */   }
/*    */   
/*    */   public static String convert(Fields fields)
/*    */   {
/* 48 */     return convert(fields, StandardCharsets.UTF_8);
/*    */   }
/*    */   
/*    */ 
/*    */   public static String convert(Fields fields, Charset charset)
/*    */   {
/* 54 */     StringBuilder builder = new StringBuilder(fields.getSize() * 32);
/* 55 */     for (Iterator localIterator1 = fields.iterator(); localIterator1.hasNext();) { field = (Fields.Field)localIterator1.next();
/*    */       
/* 57 */       for (String value : field.getValues())
/*    */       {
/* 59 */         if (builder.length() > 0)
/* 60 */           builder.append("&");
/* 61 */         builder.append(encode(field.getName(), charset)).append("=").append(encode(value, charset));
/*    */       } }
/*    */     Fields.Field field;
/* 64 */     return builder.toString();
/*    */   }
/*    */   
/*    */   private static String encode(String value, Charset charset)
/*    */   {
/*    */     try
/*    */     {
/* 71 */       return URLEncoder.encode(value, charset.name());
/*    */     }
/*    */     catch (UnsupportedEncodingException x)
/*    */     {
/* 75 */       throw new UnsupportedCharsetException(charset.name());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\FormContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */