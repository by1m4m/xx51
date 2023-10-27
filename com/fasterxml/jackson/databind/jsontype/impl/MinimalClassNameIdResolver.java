/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.type.TypeFactory;
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
/*    */ public class MinimalClassNameIdResolver
/*    */   extends ClassNameIdResolver
/*    */ {
/*    */   protected final String _basePackageName;
/*    */   protected final String _basePackagePrefix;
/*    */   
/*    */   protected MinimalClassNameIdResolver(JavaType baseType, TypeFactory typeFactory)
/*    */   {
/* 24 */     super(baseType, typeFactory);
/* 25 */     String base = baseType.getRawClass().getName();
/* 26 */     int ix = base.lastIndexOf('.');
/* 27 */     if (ix < 0) {
/* 28 */       this._basePackageName = "";
/* 29 */       this._basePackagePrefix = ".";
/*    */     } else {
/* 31 */       this._basePackagePrefix = base.substring(0, ix + 1);
/* 32 */       this._basePackageName = base.substring(0, ix);
/*    */     }
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.Id getMechanism() {
/* 37 */     return JsonTypeInfo.Id.MINIMAL_CLASS;
/*    */   }
/*    */   
/*    */   public String idFromValue(Object value)
/*    */   {
/* 42 */     String n = value.getClass().getName();
/* 43 */     if (n.startsWith(this._basePackagePrefix))
/*    */     {
/* 45 */       return n.substring(this._basePackagePrefix.length() - 1);
/*    */     }
/* 47 */     return n;
/*    */   }
/*    */   
/*    */ 
/*    */   protected JavaType _typeFromId(String id, TypeFactory typeFactory)
/*    */   {
/* 53 */     if (id.startsWith(".")) {
/* 54 */       StringBuilder sb = new StringBuilder(id.length() + this._basePackageName.length());
/* 55 */       if (this._basePackageName.length() == 0)
/*    */       {
/* 57 */         sb.append(id.substring(1));
/*    */       }
/*    */       else {
/* 60 */         sb.append(this._basePackageName).append(id);
/*    */       }
/* 62 */       id = sb.toString();
/*    */     }
/* 64 */     return super._typeFromId(id, typeFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\impl\MinimalClassNameIdResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */