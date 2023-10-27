/*    */ package oshi.jna.platform.windows.COM;
/*    */ 
/*    */ import com.sun.jna.NativeLong;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.platform.win32.COM.COMUtils;
/*    */ import com.sun.jna.platform.win32.COM.Unknown;
/*    */ import com.sun.jna.platform.win32.Guid.CLSID;
/*    */ import com.sun.jna.platform.win32.Guid.GUID;
/*    */ import com.sun.jna.platform.win32.WTypes.BSTR;
/*    */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*    */ import com.sun.jna.ptr.PointerByReference;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.jna.platform.windows.Ole32;
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
/*    */ public class WbemLocator
/*    */   extends Unknown
/*    */ {
/* 38 */   private static final Logger LOG = LoggerFactory.getLogger(WbemLocator.class);
/*    */   
/* 40 */   public static final Guid.CLSID CLSID_WbemLocator = new Guid.CLSID("4590f811-1d3a-11d0-891f-00aa004b2e24");
/* 41 */   public static final Guid.GUID IID_IWbemLocator = new Guid.GUID("dc12a687-737f-11cf-884d-00aa004b2e24");
/*    */   
/*    */   private WbemLocator(Pointer pvInstance) {
/* 44 */     super(pvInstance);
/*    */   }
/*    */   
/*    */   public static WbemLocator create() {
/* 48 */     PointerByReference pbr = new PointerByReference();
/*    */     
/* 50 */     WinNT.HRESULT hres = Ole32.INSTANCE.CoCreateInstance(CLSID_WbemLocator, null, 1, IID_IWbemLocator, pbr);
/*    */     
/* 52 */     if (COMUtils.FAILED(hres)) {
/* 53 */       LOG.error(String.format("Failed to create WbemLocator object. Error code = 0x%08x", new Object[] { Integer.valueOf(hres.intValue()) }));
/* 54 */       Ole32.INSTANCE.CoUninitialize();
/* 55 */       return null;
/*    */     }
/*    */     
/* 58 */     return new WbemLocator(pbr.getValue());
/*    */   }
/*    */   
/*    */ 
/*    */   public WinNT.HRESULT ConnectServer(WTypes.BSTR strNetworkResource, WTypes.BSTR strUser, WTypes.BSTR strPassword, WTypes.BSTR strLocale, NativeLong lSecurityFlags, WTypes.BSTR strAuthority, Pointer pCtx, PointerByReference ppNamespace)
/*    */   {
/* 64 */     return (WinNT.HRESULT)_invokeNativeObject(3, new Object[] { getPointer(), strNetworkResource, strUser, strPassword, strLocale, lSecurityFlags, strAuthority, pCtx, ppNamespace }, WinNT.HRESULT.class);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\COM\WbemLocator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */