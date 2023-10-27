/*    */ package oshi.jna.platform.windows.COM;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.platform.win32.COM.Unknown;
/*    */ import com.sun.jna.platform.win32.WTypes.BSTR;
/*    */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*    */ import com.sun.jna.ptr.PointerByReference;
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
/*    */ public class WbemServices
/*    */   extends Unknown
/*    */ {
/*    */   public WbemServices(Pointer pvInstance)
/*    */   {
/* 31 */     super(pvInstance);
/*    */   }
/*    */   
/*    */ 
/*    */   public WinNT.HRESULT ExecQuery(WTypes.BSTR strQueryLanguage, WTypes.BSTR strQuery, NativeLong lFlags, Pointer pCtx, PointerByReference ppEnum)
/*    */   {
/* 37 */     return (WinNT.HRESULT)_invokeNativeObject(20, new Object[] {
/* 38 */       getPointer(), strQueryLanguage, strQuery, lFlags, pCtx, ppEnum }, WinNT.HRESULT.class);
/*    */   }
/*    */   
/*    */ 
/*    */   public WinNT.HRESULT ExecMethod(WTypes.BSTR strObjectPath, WTypes.BSTR strMethodName, NativeLong lFlags, Pointer pCtx, Pointer pInParams, PointerByReference ppOutParams, PointerByReference ppCallResult)
/*    */   {
/* 44 */     return (WinNT.HRESULT)_invokeNativeObject(24, new Object[] { getPointer(), strObjectPath, strMethodName, lFlags, pCtx, pInParams, ppOutParams, ppCallResult }, WinNT.HRESULT.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\COM\WbemServices.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */