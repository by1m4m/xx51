/*    */ package oshi.hardware.platform.linux;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.Display;
/*    */ import oshi.hardware.common.AbstractDisplay;
/*    */ import oshi.hardware.platform.unix.solaris.SolarisDisplay;
/*    */ import oshi.util.ExecutingCommand;
/*    */ import oshi.util.ParseUtil;
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
/*    */ public class LinuxDisplay
/*    */   extends AbstractDisplay
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 42 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxDisplay.class);
/*    */   
/*    */   public LinuxDisplay(byte[] edid) {
/* 45 */     super(edid);
/* 46 */     LOG.debug("Initialized LinuxDisplay");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Display[] getDisplays()
/*    */   {
/* 55 */     List<String> xrandr = ExecutingCommand.runNative("xrandr --verbose");
/*    */     
/*    */ 
/* 58 */     if (xrandr.isEmpty()) {
/* 59 */       return new Display[0];
/*    */     }
/* 61 */     List<Display> displays = new ArrayList();
/* 62 */     StringBuilder sb = null;
/* 63 */     for (String s : xrandr) {
/* 64 */       if (s.contains("EDID")) {
/* 65 */         sb = new StringBuilder();
/* 66 */       } else if (sb != null) {
/* 67 */         sb.append(s.trim());
/* 68 */         if (sb.length() >= 256)
/*    */         {
/*    */ 
/* 71 */           String edidStr = sb.toString();
/* 72 */           LOG.debug("Parsed EDID: {}", edidStr);
/* 73 */           byte[] edid = ParseUtil.hexStringToByteArray(edidStr);
/* 74 */           if (edid.length >= 128) {
/* 75 */             displays.add(new SolarisDisplay(edid));
/*    */           }
/* 77 */           sb = null;
/*    */         }
/*    */       }
/*    */     }
/* 81 */     return (Display[])displays.toArray(new Display[displays.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */