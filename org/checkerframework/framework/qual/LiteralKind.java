/*    */ package org.checkerframework.framework.qual;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
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
/*    */ public enum LiteralKind
/*    */ {
/* 20 */   NULL, 
/*    */   
/* 22 */   INT, 
/*    */   
/* 24 */   LONG, 
/*    */   
/* 26 */   FLOAT, 
/*    */   
/* 28 */   DOUBLE, 
/*    */   
/* 30 */   BOOLEAN, 
/*    */   
/* 32 */   CHAR, 
/*    */   
/* 34 */   STRING, 
/*    */   
/* 36 */   ALL, 
/*    */   
/*    */ 
/*    */ 
/* 40 */   PRIMITIVE;
/*    */   
/*    */ 
/*    */ 
/*    */   private LiteralKind() {}
/*    */   
/*    */ 
/*    */   public static List<LiteralKind> allLiteralKinds()
/*    */   {
/* 49 */     List<LiteralKind> list = new ArrayList(Arrays.asList(values()));
/* 50 */     list.remove(ALL);
/* 51 */     list.remove(PRIMITIVE);
/* 52 */     return list;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static List<LiteralKind> primitiveLiteralKinds()
/*    */   {
/* 63 */     return new ArrayList(Arrays.asList(new LiteralKind[] { INT, LONG, FLOAT, DOUBLE, BOOLEAN, CHAR }));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\LiteralKind.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */