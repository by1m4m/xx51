/*    */ package org.eclipse.jetty.websocket.common.extensions.identity;
/*    */ 
/*    */ import org.eclipse.jetty.util.QuotedStringTokenizer;
/*    */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*    */ import org.eclipse.jetty.websocket.api.BatchMode;
/*    */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*    */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*    */ import org.eclipse.jetty.websocket.common.extensions.AbstractExtension;
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
/*    */ @ManagedObject("Identity Extension")
/*    */ public class IdentityExtension
/*    */   extends AbstractExtension
/*    */ {
/*    */   private String id;
/*    */   
/*    */   public String getParam(String key)
/*    */   {
/* 36 */     return getConfig().getParameter(key, "?");
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 42 */     return "identity";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void incomingError(Throwable e)
/*    */   {
/* 49 */     nextIncomingError(e);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void incomingFrame(Frame frame)
/*    */   {
/* 56 */     nextIncomingFrame(frame);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*    */   {
/* 63 */     nextOutgoingFrame(frame, callback, batchMode);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setConfig(ExtensionConfig config)
/*    */   {
/* 69 */     super.setConfig(config);
/* 70 */     StringBuilder s = new StringBuilder();
/* 71 */     s.append(config.getName());
/* 72 */     s.append("@").append(Integer.toHexString(hashCode()));
/* 73 */     s.append("[");
/* 74 */     boolean delim = false;
/* 75 */     for (String param : config.getParameterKeys())
/*    */     {
/* 77 */       if (delim)
/*    */       {
/* 79 */         s.append(';');
/*    */       }
/* 81 */       s.append(param).append('=').append(QuotedStringTokenizer.quoteIfNeeded(config.getParameter(param, ""), ";="));
/* 82 */       delim = true;
/*    */     }
/* 84 */     s.append("]");
/* 85 */     this.id = s.toString();
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 91 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\identity\IdentityExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */