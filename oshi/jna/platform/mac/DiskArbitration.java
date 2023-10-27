/*    */ package oshi.jna.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.PointerType;
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
/*    */ public abstract interface DiskArbitration
/*    */   extends Library
/*    */ {
/* 36 */   public static final DiskArbitration INSTANCE = (DiskArbitration)Native.loadLibrary("DiskArbitration", DiskArbitration.class);
/*    */   
/*    */   public abstract DASessionRef DASessionCreate(CoreFoundation.CFAllocatorRef paramCFAllocatorRef);
/*    */   
/*    */   public abstract DADiskRef DADiskCreateFromBSDName(CoreFoundation.CFAllocatorRef paramCFAllocatorRef, DASessionRef paramDASessionRef, String paramString);
/*    */   
/*    */   public abstract DADiskRef DADiskCreateFromIOMedia(CoreFoundation.CFAllocatorRef paramCFAllocatorRef, DASessionRef paramDASessionRef, int paramInt);
/*    */   
/*    */   public abstract CoreFoundation.CFDictionaryRef DADiskCopyDescription(DADiskRef paramDADiskRef);
/*    */   
/*    */   public abstract String DADiskGetBSDName(DADiskRef paramDADiskRef);
/*    */   
/*    */   public static class DADiskRef
/*    */     extends PointerType
/*    */   {}
/*    */   
/*    */   public static class DASessionRef
/*    */     extends PointerType
/*    */   {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\mac\DiskArbitration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */