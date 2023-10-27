/*    */ package oshi.jna.platform.unix.freebsd;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.Structure;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import oshi.jna.platform.unix.CLibrary;
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
/*    */ public abstract interface Libc
/*    */   extends CLibrary
/*    */ {
/* 36 */   public static final Libc INSTANCE = (Libc)Native.loadLibrary("libc", Libc.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 41 */   public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
/* 42 */   public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
/*    */   
/*    */   public static final int CPUSTATES = 5;
/*    */   
/*    */   public static final int CP_USER = 0;
/*    */   public static final int CP_NICE = 1;
/*    */   public static final int CP_SYS = 2;
/*    */   public static final int CP_INTR = 3;
/*    */   public static final int CP_IDLE = 4;
/*    */   
/*    */   public static class CpTime
/*    */     extends Structure
/*    */   {
/* 55 */     public long[] cpu_ticks = new long[5];
/*    */     
/*    */     protected List<String> getFieldOrder()
/*    */     {
/* 59 */       return Arrays.asList(new String[] { "cpu_ticks" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\unix\freebsd\Libc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */