/*    */ package com.fasterxml.jackson.databind.exc;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import java.util.Collection;
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
/*    */ public class IgnoredPropertyException
/*    */   extends PropertyBindingException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public IgnoredPropertyException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*    */   {
/* 25 */     super(msg, loc, referringClass, propName, propertyIds);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static IgnoredPropertyException from(JsonParser jp, Object fromObjectOrClass, String propertyName, Collection<Object> propertyIds)
/*    */   {
/* 42 */     if (fromObjectOrClass == null)
/* 43 */       throw new IllegalArgumentException();
/*    */     Class<?> ref;
/*    */     Class<?> ref;
/* 46 */     if ((fromObjectOrClass instanceof Class)) {
/* 47 */       ref = (Class)fromObjectOrClass;
/*    */     } else {
/* 49 */       ref = fromObjectOrClass.getClass();
/*    */     }
/* 51 */     String msg = "Ignored field \"" + propertyName + "\" (class " + ref.getName() + ") encountered; mapper configured not to allow this";
/*    */     
/* 53 */     IgnoredPropertyException e = new IgnoredPropertyException(msg, jp.getCurrentLocation(), ref, propertyName, propertyIds);
/*    */     
/*    */ 
/* 56 */     e.prependPath(fromObjectOrClass, propertyName);
/* 57 */     return e;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\exc\IgnoredPropertyException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */