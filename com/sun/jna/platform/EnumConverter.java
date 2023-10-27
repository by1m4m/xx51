/*    */ package com.sun.jna.platform;
/*    */ 
/*    */ import com.sun.jna.FromNativeContext;
/*    */ import com.sun.jna.ToNativeContext;
/*    */ import com.sun.jna.TypeConverter;
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
/*    */ public class EnumConverter<T extends Enum<T>>
/*    */   implements TypeConverter
/*    */ {
/*    */   private final Class<T> clazz;
/*    */   
/*    */   public EnumConverter(Class<T> clazz)
/*    */   {
/* 38 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */   public T fromNative(Object input, FromNativeContext context)
/*    */   {
/* 43 */     Integer i = (Integer)input;
/*    */     
/* 45 */     T[] vals = (Enum[])this.clazz.getEnumConstants();
/* 46 */     return vals[i.intValue()];
/*    */   }
/*    */   
/*    */   public Integer toNative(Object input, ToNativeContext context)
/*    */   {
/* 51 */     T t = (Enum)this.clazz.cast(input);
/*    */     
/* 53 */     return Integer.valueOf(t.ordinal());
/*    */   }
/*    */   
/*    */   public Class<Integer> nativeType()
/*    */   {
/* 58 */     return Integer.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\EnumConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */