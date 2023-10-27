/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
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
/*    */ @GwtIncompatible
/*    */ public final class Defaults
/*    */ {
/* 32 */   private static final Double DOUBLE_DEFAULT = Double.valueOf(0.0D);
/* 33 */   private static final Float FLOAT_DEFAULT = Float.valueOf(0.0F);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <T> T defaultValue(Class<T> type)
/*    */   {
/* 42 */     Preconditions.checkNotNull(type);
/* 43 */     if (type == Boolean.TYPE)
/* 44 */       return Boolean.FALSE;
/* 45 */     if (type == Character.TYPE)
/* 46 */       return Character.valueOf('\000');
/* 47 */     if (type == Byte.TYPE)
/* 48 */       return Byte.valueOf((byte)0);
/* 49 */     if (type == Short.TYPE)
/* 50 */       return Short.valueOf((short)0);
/* 51 */     if (type == Integer.TYPE)
/* 52 */       return Integer.valueOf(0);
/* 53 */     if (type == Long.TYPE)
/* 54 */       return Long.valueOf(0L);
/* 55 */     if (type == Float.TYPE)
/* 56 */       return FLOAT_DEFAULT;
/* 57 */     if (type == Double.TYPE) {
/* 58 */       return DOUBLE_DEFAULT;
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Defaults.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */