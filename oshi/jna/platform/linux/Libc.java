/*    */ package oshi.jna.platform.linux;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.NativeLong;
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
/*    */ 
/*    */ public abstract interface Libc
/*    */   extends CLibrary
/*    */ {
/* 38 */   public static final Libc INSTANCE = (Libc)Native.loadLibrary("c", Libc.class);
/*    */   
/*    */   public abstract int sysinfo(Sysinfo paramSysinfo);
/*    */   
/*    */   public static class Sysinfo extends Structure {
/*    */     public NativeLong uptime;
/* 44 */     public NativeLong[] loads = new NativeLong[3];
/*    */     
/*    */     public NativeLong totalram;
/*    */     
/*    */     public NativeLong freeram;
/*    */     
/*    */     public NativeLong sharedram;
/*    */     
/*    */     public NativeLong bufferram;
/*    */     
/*    */     public NativeLong totalswap;
/*    */     
/*    */     public NativeLong freeswap;
/*    */     
/*    */     public short procs;
/*    */     
/*    */     public NativeLong totalhigh;
/*    */     
/*    */     public NativeLong freehigh;
/*    */     
/*    */     public int mem_unit;
/*    */     
/* 66 */     public byte[] _f = new byte[8];
/*    */     
/*    */     protected List<String> getFieldOrder()
/*    */     {
/* 70 */       return Arrays.asList(new String[] { "uptime", "loads", "totalram", "freeram", "sharedram", "bufferram", "totalswap", "freeswap", "procs", "totalhigh", "freehigh", "mem_unit", "_f" });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\linux\Libc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */