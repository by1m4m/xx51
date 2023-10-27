/*    */ package com.sun.jna.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.Pointer;
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
/*    */ abstract interface XAttr
/*    */   extends Library
/*    */ {
/* 26 */   public static final XAttr INSTANCE = (XAttr)Native.loadLibrary(null, XAttr.class);
/*    */   public static final int XATTR_NOFOLLOW = 1;
/*    */   public static final int XATTR_CREATE = 2;
/*    */   public static final int XATTR_REPLACE = 4;
/*    */   public static final int XATTR_NOSECURITY = 8;
/*    */   public static final int XATTR_NODEFAULT = 16;
/*    */   public static final int XATTR_SHOWCOMPRESSION = 32;
/*    */   public static final int XATTR_MAXNAMELEN = 127;
/*    */   public static final String XATTR_FINDERINFO_NAME = "com.apple.FinderInfo";
/*    */   public static final String XATTR_RESOURCEFORK_NAME = "com.apple.ResourceFork";
/*    */   
/*    */   public abstract long getxattr(String paramString1, String paramString2, Pointer paramPointer, long paramLong, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int setxattr(String paramString1, String paramString2, Pointer paramPointer, long paramLong, int paramInt1, int paramInt2);
/*    */   
/*    */   public abstract int removexattr(String paramString1, String paramString2, int paramInt);
/*    */   
/*    */   public abstract long listxattr(String paramString, Pointer paramPointer, long paramLong, int paramInt);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\mac\XAttr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */