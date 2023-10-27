/*    */ package rdp.gold.brute.rdp;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.log4j.Logger;
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
/*    */ public class PacketSniffer
/*    */ {
/*    */   public static final String SERVER_X224_CONNECTION_REQUEST = "Server X224ConnectionRequest";
/* 30 */   protected static final Pair serverX224ConnectionRequest = new Pair("Server X224ConnectionRequest", "03 00 XX XX 0E D0");
/* 31 */   private final Logger logger = Logger.getLogger(PacketSniffer.class);
/* 32 */   protected Pair[] regexps = null;
/*    */   
/*    */ 
/*    */   public PacketSniffer(String id, Pair[] regexps)
/*    */   {
/* 37 */     this.regexps = regexps;
/*    */   }
/*    */   
/*    */ 
/*    */   public void handleData(ByteBuffer buf)
/*    */   {
/* 43 */     matchPacket(buf);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean matchX224ConnectionRequest(ByteBuffer buf)
/*    */   {
/* 49 */     String header = buf.toPlainHexString(19);
/* 50 */     return serverX224ConnectionRequest.regexp.matcher(header).find();
/*    */   }
/*    */   
/*    */   private void matchPacket(ByteBuffer buf) {
/* 54 */     String header = buf.toPlainHexString(100);
/* 55 */     for (Pair pair : this.regexps) {
/* 56 */       if (pair.regexp.matcher(header).find()) {
/* 57 */         this.logger.info("Packet: " + pair.name + ".");
/* 58 */         return;
/*    */       }
/*    */     }
/*    */     
/* 62 */     this.logger.info("Unknown packet: " + header + ".");
/*    */   }
/*    */   
/*    */   protected static class Pair {
/*    */     String name;
/*    */     Pattern regexp;
/*    */     
/*    */     protected Pair(String name, String regexp) {
/* 70 */       this.name = name;
/* 71 */       this.regexp = Pattern.compile("^" + replaceShortcuts(regexp), 2);
/*    */     }
/*    */     
/*    */     private static String replaceShortcuts(String regexp) {
/* 75 */       String result = regexp;
/* 76 */       result = result.replaceAll("XX\\*", "([0-9a-fA-F]{2} )*?");
/* 77 */       result = result.replaceAll("XX\\?", "([0-9a-fA-F]{2} )?");
/* 78 */       result = result.replaceAll("XX", "[0-9a-fA-F]{2}");
/* 79 */       return result;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\PacketSniffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */