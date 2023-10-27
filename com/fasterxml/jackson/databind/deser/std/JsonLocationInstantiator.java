/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonLocation;
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*    */ import com.fasterxml.jackson.databind.PropertyName;
/*    */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonLocationInstantiator
/*    */   extends ValueInstantiator
/*    */ {
/*    */   public String getValueTypeDesc()
/*    */   {
/* 21 */     return JsonLocation.class.getName();
/*    */   }
/*    */   
/*    */   public boolean canCreateFromObjectWith() {
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public CreatorProperty[] getFromObjectArguments(DeserializationConfig config) {
/* 29 */     JavaType intType = config.constructType(Integer.TYPE);
/* 30 */     JavaType longType = config.constructType(Long.TYPE);
/* 31 */     return new CreatorProperty[] { creatorProp("sourceRef", config.constructType(Object.class), 0), creatorProp("byteOffset", longType, 1), creatorProp("charOffset", longType, 2), creatorProp("lineNr", intType, 3), creatorProp("columnNr", intType, 4) };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static CreatorProperty creatorProp(String name, JavaType type, int index)
/*    */   {
/* 41 */     return new CreatorProperty(new PropertyName(name), type, null, null, null, null, index, null, PropertyMetadata.STD_REQUIRED);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object createFromObjectWith(DeserializationContext ctxt, Object[] args)
/*    */   {
/* 47 */     return new JsonLocation(args[0], _long(args[1]), _long(args[2]), _int(args[3]), _int(args[4]));
/*    */   }
/*    */   
/*    */   private static final long _long(Object o)
/*    */   {
/* 52 */     return o == null ? 0L : ((Number)o).longValue();
/*    */   }
/*    */   
/*    */   private static final int _int(Object o) {
/* 56 */     return o == null ? 0 : ((Number)o).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\JsonLocationInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */