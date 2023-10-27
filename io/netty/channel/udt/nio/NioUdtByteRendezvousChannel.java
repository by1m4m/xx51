/*    */ package io.netty.channel.udt.nio;
/*    */ 
/*    */ import com.barchart.udt.TypeUDT;
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
/*    */ @Deprecated
/*    */ public class NioUdtByteRendezvousChannel
/*    */   extends NioUdtByteConnectorChannel
/*    */ {
/*    */   public NioUdtByteRendezvousChannel()
/*    */   {
/* 29 */     super(NioUdtProvider.newRendezvousChannelUDT(TypeUDT.STREAM));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\udt\nio\NioUdtByteRendezvousChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */