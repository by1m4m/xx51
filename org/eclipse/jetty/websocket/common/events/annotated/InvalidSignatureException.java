/*    */ package org.eclipse.jetty.websocket.common.events.annotated;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.reflect.Method;
/*    */ import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
/*    */ import org.eclipse.jetty.websocket.common.events.ParamList;
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
/*    */ public class InvalidSignatureException
/*    */   extends InvalidWebSocketException
/*    */ {
/*    */   public static InvalidSignatureException build(Method method, Class<? extends Annotation> annoClass, ParamList... paramlists)
/*    */   {
/* 33 */     StringBuilder err = new StringBuilder();
/* 34 */     err.append("Invalid declaration of ");
/* 35 */     err.append(method);
/* 36 */     err.append(System.lineSeparator());
/*    */     
/* 38 */     err.append("Acceptable method declarations for @");
/* 39 */     err.append(annoClass.getSimpleName());
/* 40 */     err.append(" are:");
/* 41 */     for (ParamList validParams : paramlists)
/*    */     {
/* 43 */       for (Class<?>[] params : validParams)
/*    */       {
/* 45 */         err.append(System.lineSeparator());
/* 46 */         err.append("public void ").append(method.getName());
/* 47 */         err.append('(');
/* 48 */         boolean delim = false;
/* 49 */         for (Class<?> type : params)
/*    */         {
/* 51 */           if (delim)
/*    */           {
/* 53 */             err.append(',');
/*    */           }
/* 55 */           err.append(' ');
/* 56 */           err.append(type.getName());
/* 57 */           if (type.isArray())
/*    */           {
/* 59 */             err.append("[]");
/*    */           }
/* 61 */           delim = true;
/*    */         }
/* 63 */         err.append(')');
/*    */       }
/*    */     }
/* 66 */     return new InvalidSignatureException(err.toString());
/*    */   }
/*    */   
/*    */   public InvalidSignatureException(String message)
/*    */   {
/* 71 */     super(message);
/*    */   }
/*    */   
/*    */   public InvalidSignatureException(String message, Throwable cause)
/*    */   {
/* 76 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\InvalidSignatureException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */