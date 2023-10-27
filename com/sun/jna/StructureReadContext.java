/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.reflect.Field;
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
/*    */ public class StructureReadContext
/*    */   extends FromNativeContext
/*    */ {
/*    */   private Structure structure;
/*    */   private Field field;
/*    */   
/*    */   StructureReadContext(Structure struct, Field field)
/*    */   {
/* 25 */     super(field.getType());
/* 26 */     this.structure = struct;
/* 27 */     this.field = field;
/*    */   }
/*    */   
/* 30 */   public Structure getStructure() { return this.structure; }
/*    */   
/* 32 */   public Field getField() { return this.field; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\StructureReadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */