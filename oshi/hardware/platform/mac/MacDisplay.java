/*    */ package oshi.hardware.platform.mac;
/*    */ 
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.ptr.IntByReference;
/*    */ import com.sun.jna.ptr.PointerByReference;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.Display;
/*    */ import oshi.hardware.common.AbstractDisplay;
/*    */ import oshi.jna.platform.mac.CoreFoundation;
/*    */ import oshi.jna.platform.mac.CoreFoundation.CFStringRef;
/*    */ import oshi.jna.platform.mac.CoreFoundation.CFTypeRef;
/*    */ import oshi.jna.platform.mac.IOKit;
/*    */ import oshi.util.platform.mac.CfUtil;
/*    */ import oshi.util.platform.mac.IOKitUtil;
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
/*    */ public class MacDisplay
/*    */   extends AbstractDisplay
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 48 */   private static final Logger LOG = LoggerFactory.getLogger(MacDisplay.class);
/*    */   
/* 50 */   private static final CoreFoundation.CFStringRef cfEdid = CoreFoundation.CFStringRef.toCFString("IODisplayEDID");
/*    */   
/*    */   public MacDisplay(byte[] edid) {
/* 53 */     super(edid);
/* 54 */     LOG.debug("Initialized MacDisplay");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Display[] getDisplays()
/*    */   {
/* 63 */     List<Display> displays = new ArrayList();
/*    */     
/* 65 */     IntByReference serviceIterator = new IntByReference();
/* 66 */     IOKitUtil.getMatchingServices("IODisplayConnect", serviceIterator);
/* 67 */     int sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/* 68 */     while (sdService != 0)
/*    */     {
/* 70 */       IntByReference properties = new IntByReference();
/* 71 */       int ret = IOKit.INSTANCE.IORegistryEntryGetChildEntry(sdService, "IOService", properties);
/* 72 */       if (ret == 0)
/*    */       {
/* 74 */         CoreFoundation.CFTypeRef edid = IOKit.INSTANCE.IORegistryEntryCreateCFProperty(properties.getValue(), cfEdid, CfUtil.ALLOCATOR, 0);
/*    */         
/* 76 */         if (edid != null)
/*    */         {
/* 78 */           int length = CoreFoundation.INSTANCE.CFDataGetLength(edid);
/* 79 */           PointerByReference p = CoreFoundation.INSTANCE.CFDataGetBytePtr(edid);
/* 80 */           displays.add(new MacDisplay(p.getPointer().getByteArray(0L, length)));
/* 81 */           CfUtil.release(edid);
/*    */         }
/*    */       }
/*    */       
/* 85 */       IOKit.INSTANCE.IOObjectRelease(sdService);
/* 86 */       sdService = IOKit.INSTANCE.IOIteratorNext(serviceIterator.getValue());
/*    */     }
/* 88 */     IOKit.INSTANCE.IOObjectRelease(serviceIterator.getValue());
/* 89 */     return (Display[])displays.toArray(new Display[displays.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */