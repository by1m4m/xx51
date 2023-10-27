/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ContainerDeserializerBase<T>
/*    */   extends StdDeserializer<T>
/*    */ {
/*    */   protected ContainerDeserializerBase(JavaType selfType)
/*    */   {
/* 21 */     super(selfType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   protected ContainerDeserializerBase(Class<?> selfType)
/*    */   {
/* 29 */     super(selfType);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SettableBeanProperty findBackReference(String refName)
/*    */   {
/* 40 */     JsonDeserializer<Object> valueDeser = getContentDeserializer();
/* 41 */     if (valueDeser == null) {
/* 42 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': type: container deserializer of type " + getClass().getName() + " returned null for 'getContentDeserializer()'");
/*    */     }
/*    */     
/* 45 */     return valueDeser.findBackReference(refName);
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
/*    */   public abstract JavaType getContentType();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract JsonDeserializer<Object> getContentDeserializer();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void wrapAndThrow(Throwable t, Object ref, String key)
/*    */     throws IOException
/*    */   {
/* 77 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 78 */       t = t.getCause();
/*    */     }
/*    */     
/* 81 */     if ((t instanceof Error)) {
/* 82 */       throw ((Error)t);
/*    */     }
/*    */     
/* 85 */     if (((t instanceof IOException)) && (!(t instanceof JsonMappingException))) {
/* 86 */       throw ((IOException)t);
/*    */     }
/* 88 */     throw JsonMappingException.wrapWithPath(t, ref, key);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\ContainerDeserializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */