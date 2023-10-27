/*    */ package io.netty.util.internal.shaded.org.jctools.util;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Field;
/*    */ import sun.misc.Unsafe;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnsafeAccess
/*    */ {
/*    */   public static final boolean SUPPORTS_GET_AND_SET;
/*    */   public static final Unsafe UNSAFE;
/*    */   
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 46 */       Field field = Unsafe.class.getDeclaredField("theUnsafe");
/* 47 */       field.setAccessible(true);
/* 48 */       instance = (Unsafe)field.get(null);
/*    */     }
/*    */     catch (Exception ignored)
/*    */     {
/*    */       try
/*    */       {
/*    */         Unsafe instance;
/*    */         
/*    */ 
/*    */ 
/* 58 */         Constructor<Unsafe> c = Unsafe.class.getDeclaredConstructor(new Class[0]);
/* 59 */         c.setAccessible(true);
/* 60 */         instance = (Unsafe)c.newInstance(new Object[0]);
/*    */       }
/*    */       catch (Exception e) {
/*    */         Unsafe instance;
/* 64 */         SUPPORTS_GET_AND_SET = false;
/* 65 */         throw new RuntimeException(e);
/*    */       }
/*    */     }
/*    */     Unsafe instance;
/* 69 */     boolean getAndSetSupport = false;
/*    */     try
/*    */     {
/* 72 */       Unsafe.class.getMethod("getAndSetObject", new Class[] { Object.class, Long.TYPE, Object.class });
/* 73 */       getAndSetSupport = true;
/*    */     }
/*    */     catch (Exception localException1) {}
/*    */     
/*    */ 
/*    */ 
/* 79 */     UNSAFE = instance;
/* 80 */     SUPPORTS_GET_AND_SET = getAndSetSupport;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\shaded\org\jctools\util\UnsafeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */