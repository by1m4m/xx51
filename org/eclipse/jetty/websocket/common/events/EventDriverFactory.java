/*     */ package org.eclipse.jetty.websocket.common.events;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventDriverFactory
/*     */ {
/*  36 */   private static final Logger LOG = Log.getLogger(EventDriverFactory.class);
/*     */   private final WebSocketContainerScope containerScope;
/*     */   private final List<EventDriverImpl> implementations;
/*     */   
/*     */   public EventDriverFactory(WebSocketContainerScope containerScope)
/*     */   {
/*  42 */     this.containerScope = containerScope;
/*  43 */     this.implementations = new ArrayList();
/*     */     
/*  45 */     addImplementation(new JettyListenerImpl());
/*  46 */     addImplementation(new JettyAnnotatedImpl());
/*     */   }
/*     */   
/*     */   public void addImplementation(EventDriverImpl impl)
/*     */   {
/*  51 */     if (this.implementations.contains(impl))
/*     */     {
/*  53 */       LOG.warn("Ignoring attempt to add duplicate EventDriverImpl: " + impl, new Object[0]);
/*  54 */       return;
/*     */     }
/*     */     
/*  57 */     this.implementations.add(impl);
/*     */   }
/*     */   
/*     */   public void clearImplementations()
/*     */   {
/*  62 */     this.implementations.clear();
/*     */   }
/*     */   
/*     */   protected String getClassName(Object websocket)
/*     */   {
/*  67 */     return websocket.getClass().getName();
/*     */   }
/*     */   
/*     */   public List<EventDriverImpl> getImplementations()
/*     */   {
/*  72 */     return this.implementations;
/*     */   }
/*     */   
/*     */   public boolean removeImplementation(EventDriverImpl impl)
/*     */   {
/*  77 */     return this.implementations.remove(impl);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  83 */     StringBuilder msg = new StringBuilder();
/*  84 */     msg.append(getClass().getSimpleName());
/*  85 */     msg.append("[implementations=[");
/*  86 */     boolean delim = false;
/*  87 */     for (EventDriverImpl impl : this.implementations)
/*     */     {
/*  89 */       if (delim)
/*     */       {
/*  91 */         msg.append(',');
/*     */       }
/*  93 */       msg.append(impl.toString());
/*  94 */       delim = true;
/*     */     }
/*  96 */     msg.append("]");
/*  97 */     return msg.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EventDriver wrap(Object websocket)
/*     */   {
/* 109 */     if (websocket == null)
/*     */     {
/* 111 */       throw new InvalidWebSocketException("null websocket object");
/*     */     }
/*     */     
/* 114 */     for (EventDriverImpl impl : this.implementations)
/*     */     {
/* 116 */       if (impl.supports(websocket))
/*     */       {
/*     */         try
/*     */         {
/* 120 */           return impl.create(websocket, this.containerScope.getPolicy().clonePolicy());
/*     */         }
/*     */         catch (Throwable e)
/*     */         {
/* 124 */           throw new InvalidWebSocketException("Unable to create websocket", e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 130 */     StringBuilder err = new StringBuilder();
/* 131 */     err.append(getClassName(websocket));
/* 132 */     err.append(" is not a valid WebSocket object.");
/* 133 */     err.append("  Object must obey one of the following rules: ");
/*     */     
/* 135 */     int len = this.implementations.size();
/* 136 */     for (int i = 0; i < len; i++)
/*     */     {
/* 138 */       EventDriverImpl impl = (EventDriverImpl)this.implementations.get(i);
/* 139 */       if (i > 0)
/*     */       {
/* 141 */         err.append(" or ");
/*     */       }
/* 143 */       err.append("\n(").append(i + 1).append(") ");
/* 144 */       err.append(impl.describeRule());
/*     */     }
/*     */     
/* 147 */     throw new InvalidWebSocketException(err.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\EventDriverFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */