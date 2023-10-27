/*    */ package oshi.jna.platform.unix;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.Structure;
/*    */ import com.sun.jna.Structure.ByReference;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import com.sun.jna.ptr.PointerByReference;
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
/*    */ public abstract interface CLibrary
/*    */   extends Library
/*    */ {
/*    */   public static final int AI_CANONNAME = 2;
/*    */   
/*    */   public abstract int sysctl(int[] paramArrayOfInt, int paramInt1, Pointer paramPointer1, IntByReference paramIntByReference, Pointer paramPointer2, int paramInt2);
/*    */   
/*    */   public abstract int sysctlbyname(String paramString, Pointer paramPointer1, IntByReference paramIntByReference, Pointer paramPointer2, int paramInt);
/*    */   
/*    */   public abstract int sysctlnametomib(String paramString, Pointer paramPointer, IntByReference paramIntByReference);
/*    */   
/*    */   public abstract int getloadavg(double[] paramArrayOfDouble, int paramInt);
/*    */   
/*    */   public abstract int getpid();
/*    */   
/*    */   public abstract int getaddrinfo(String paramString1, String paramString2, Addrinfo paramAddrinfo, PointerByReference paramPointerByReference);
/*    */   
/*    */   public abstract void freeaddrinfo(Pointer paramPointer);
/*    */   
/*    */   public abstract String gai_strerror(int paramInt);
/*    */   
/*    */   public abstract int readlink(String paramString, Pointer paramPointer, int paramInt);
/*    */   
/*    */   public abstract int getpagesize();
/*    */   
/*    */   public static class Timeval
/*    */     extends Structure
/*    */   {
/*    */     public long tv_sec;
/*    */     public long tv_usec;
/*    */     
/*    */     protected List<String> getFieldOrder()
/*    */     {
/* 53 */       return Arrays.asList(new String[] { "tv_sec", "tv_usec" });
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Sockaddr extends Structure {
/*    */     public short sa_family;
/* 59 */     public byte[] sa_data = new byte[14];
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     protected List<String> getFieldOrder()
/*    */     {
/* 66 */       return Arrays.asList(new String[] { "sa_family", "sa_data" });
/*    */     }
/*    */     
/*    */     public static class ByReference extends CLibrary.Sockaddr implements Structure.ByReference
/*    */     {}
/*    */   }
/*    */   
/*    */   public static class Addrinfo extends Structure
/*    */   {
/*    */     public int ai_flags;
/*    */     public int ai_family;
/*    */     public int ai_socktype;
/*    */     public int ai_protocol;
/*    */     public int ai_addrlen;
/*    */     public CLibrary.Sockaddr.ByReference ai_addr;
/*    */     public String ai_canonname;
/*    */     public ByReference ai_next;
/*    */     
/*    */     protected List<String> getFieldOrder() {
/* 85 */       return Arrays.asList(new String[] { "ai_flags", "ai_family", "ai_socktype", "ai_protocol", "ai_addrlen", "ai_addr", "ai_canonname", "ai_next" });
/*    */     }
/*    */     
/*    */ 
/*    */     public Addrinfo() {}
/*    */     
/*    */     public Addrinfo(Pointer p)
/*    */     {
/* 93 */       super();
/* 94 */       read();
/*    */     }
/*    */     
/*    */     public static class ByReference
/*    */       extends CLibrary.Addrinfo
/*    */       implements Structure.ByReference
/*    */     {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\unix\CLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */