/*    */ package oshi.jna.platform.windows;
/*    */ 
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.platform.win32.WTypes.BSTR;
/*    */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*    */ import com.sun.jna.win32.W32APIOptions;
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
/*    */ public abstract interface Ole32
/*    */   extends com.sun.jna.platform.win32.Ole32
/*    */ {
/* 35 */   public static final Ole32 INSTANCE = (Ole32)Native.loadLibrary("Ole32", Ole32.class, W32APIOptions.DEFAULT_OPTIONS);
/*    */   public static final int RPC_C_AUTHN_LEVEL_DEFAULT = 0;
/*    */   public static final int RPC_C_AUTHN_WINNT = 10;
/*    */   public static final int RPC_C_IMP_LEVEL_IMPERSONATE = 3;
/*    */   public static final int RPC_C_AUTHZ_NONE = 0;
/*    */   public static final int RPC_C_AUTHN_LEVEL_CALL = 3;
/*    */   public static final int RPC_E_TOO_LATE = -2147417831;
/*    */   public static final int RPC_E_CHANGED_MODE = -2147417850;
/*    */   public static final int EOAC_NONE = 0;
/*    */   
/*    */   public abstract WinNT.HRESULT CoInitializeSecurity(Pointer paramPointer1, NativeLong paramNativeLong, Pointer paramPointer2, Pointer paramPointer3, int paramInt1, int paramInt2, Pointer paramPointer4, int paramInt3, Pointer paramPointer5);
/*    */   
/*    */   public abstract WinNT.HRESULT CoSetProxyBlanket(Pointer paramPointer1, int paramInt1, int paramInt2, WTypes.BSTR paramBSTR, int paramInt3, int paramInt4, Pointer paramPointer2, int paramInt5);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\Ole32.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */