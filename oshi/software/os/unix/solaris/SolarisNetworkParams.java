/*    */ package oshi.software.os.unix.solaris;
/*    */ 
/*    */ import oshi.software.common.AbstractNetworkParams;
/*    */ import oshi.util.ExecutingCommand;
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
/*    */ public class SolarisNetworkParams
/*    */   extends AbstractNetworkParams
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public String getIpv4DefaultGateway()
/*    */   {
/* 33 */     return searchGateway(ExecutingCommand.runNative("route get -inet default"));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpv6DefaultGateway()
/*    */   {
/* 41 */     return searchGateway(ExecutingCommand.runNative("route get -inet6 default"));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\solaris\SolarisNetworkParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */