/*    */ package oshi.hardware.platform.unix.solaris;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import oshi.hardware.Display;
/*    */ import oshi.hardware.common.AbstractDisplay;
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
/*    */ public class SolarisDisplay
/*    */   extends AbstractDisplay
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 41 */   private static final Logger LOG = LoggerFactory.getLogger(SolarisDisplay.class);
/*    */   
/*    */   public SolarisDisplay(byte[] edid) {
/* 44 */     super(edid);
/* 45 */     LOG.debug("Initialized SolarisDisplay");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Display[] getDisplays()
/*    */   {
/* 54 */     List<String> xrandr = ExecutingCommand.runNative("xrandr --verbose");
/*    */     
/*    */ 
/* 57 */     if (xrandr.isEmpty()) {
/* 58 */       return new Display[0];
/*    */     }
/* 60 */     List<Display> displays = new ArrayList();
/* 61 */     StringBuilder sb = null;
/* 62 */     for (String s : xrandr) {
/* 63 */       if (s.contains("EDID")) {
/* 64 */         sb = new StringBuilder();
/* 65 */       } else if (sb != null) {
/* 66 */         sb.append(s.trim());
/* 67 */         if (sb.length() >= 256)
/*    */         {
/*    */ 
/* 70 */           String edidStr = sb.toString();
/* 71 */           LOG.debug("Parsed EDID: {}", edidStr);
/* 72 */           byte[] edid = ParseUtil.hexStringToByteArray(edidStr);
/* 73 */           if (edid.length >= 128) {
/* 74 */             displays.add(new SolarisDisplay(edid));
/*    */           }
/* 76 */           sb = null;
/*    */         }
/*    */       }
/*    */     }
/* 80 */     return (Display[])displays.toArray(new Display[displays.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */