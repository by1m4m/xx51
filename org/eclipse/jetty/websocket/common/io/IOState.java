/*     */ package org.eclipse.jetty.websocket.common.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.common.CloseInfo;
/*     */ import org.eclipse.jetty.websocket.common.ConnectionState;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOState
/*     */ {
/*     */   public static abstract interface ConnectionStateListener
/*     */   {
/*     */     public abstract void onConnectionStateChange(ConnectionState paramConnectionState);
/*     */   }
/*     */   
/*     */   private static enum CloseHandshakeSource
/*     */   {
/*  50 */     NONE, 
/*     */     
/*  52 */     LOCAL, 
/*     */     
/*  54 */     REMOTE, 
/*     */     
/*  56 */     ABNORMAL;
/*     */     
/*     */ 
/*     */ 
/*     */     private CloseHandshakeSource() {}
/*     */   }
/*     */   
/*     */ 
/*  64 */   private static final Logger LOG = Log.getLogger(IOState.class);
/*     */   private ConnectionState state;
/*  66 */   private final List<ConnectionStateListener> listeners = new CopyOnWriteArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean inputAvailable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean outputAvailable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CloseHandshakeSource closeHandshakeSource;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CloseInfo closeInfo;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  95 */   private AtomicReference<CloseInfo> finalClose = new AtomicReference();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean cleanClose;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IOState()
/*     */   {
/* 107 */     this.state = ConnectionState.CONNECTING;
/* 108 */     this.inputAvailable = false;
/* 109 */     this.outputAvailable = false;
/* 110 */     this.closeHandshakeSource = CloseHandshakeSource.NONE;
/* 111 */     this.closeInfo = null;
/* 112 */     this.cleanClose = false;
/*     */   }
/*     */   
/*     */   public void addListener(ConnectionStateListener listener)
/*     */   {
/* 117 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void assertInputOpen() throws IOException
/*     */   {
/* 122 */     if (!isInputAvailable())
/*     */     {
/* 124 */       throw new IOException("Connection input is closed");
/*     */     }
/*     */   }
/*     */   
/*     */   public void assertOutputOpen() throws IOException
/*     */   {
/* 130 */     if (!isOutputAvailable())
/*     */     {
/* 132 */       throw new IOException("Connection output is closed");
/*     */     }
/*     */   }
/*     */   
/*     */   public CloseInfo getCloseInfo()
/*     */   {
/* 138 */     CloseInfo ci = (CloseInfo)this.finalClose.get();
/* 139 */     if (ci != null)
/*     */     {
/* 141 */       return ci;
/*     */     }
/* 143 */     return this.closeInfo;
/*     */   }
/*     */   
/*     */   public ConnectionState getConnectionState()
/*     */   {
/* 148 */     return this.state;
/*     */   }
/*     */   
/*     */   public boolean isClosed()
/*     */   {
/* 153 */     synchronized (this)
/*     */     {
/* 155 */       return this.state == ConnectionState.CLOSED;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInputAvailable()
/*     */   {
/* 161 */     return this.inputAvailable;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/* 166 */     return !isClosed();
/*     */   }
/*     */   
/*     */   public boolean isOutputAvailable()
/*     */   {
/* 171 */     return this.outputAvailable;
/*     */   }
/*     */   
/*     */   private void notifyStateListeners(ConnectionState state)
/*     */   {
/* 176 */     if (LOG.isDebugEnabled())
/* 177 */       LOG.debug("Notify State Listeners: {}", new Object[] { state });
/* 178 */     for (ConnectionStateListener listener : this.listeners)
/*     */     {
/* 180 */       if (LOG.isDebugEnabled())
/*     */       {
/* 182 */         LOG.debug("{}.onConnectionStateChange({})", new Object[] { listener.getClass().getSimpleName(), state.name() });
/*     */       }
/* 184 */       listener.onConnectionStateChange(state);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onAbnormalClose(CloseInfo close)
/*     */   {
/* 196 */     if (LOG.isDebugEnabled())
/* 197 */       LOG.debug("onAbnormalClose({})", new Object[] { close });
/* 198 */     ConnectionState event = null;
/* 199 */     synchronized (this)
/*     */     {
/* 201 */       if (this.state == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 204 */         return;
/*     */       }
/*     */       
/* 207 */       if (this.state == ConnectionState.OPEN)
/*     */       {
/* 209 */         this.cleanClose = false;
/*     */       }
/*     */       
/* 212 */       this.state = ConnectionState.CLOSED;
/* 213 */       this.finalClose.compareAndSet(null, close);
/* 214 */       this.inputAvailable = false;
/* 215 */       this.outputAvailable = false;
/* 216 */       this.closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
/* 217 */       event = this.state;
/*     */     }
/* 219 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCloseLocal(CloseInfo closeInfo)
/*     */   {
/* 228 */     boolean open = false;
/* 229 */     synchronized (this)
/*     */     {
/* 231 */       ConnectionState initialState = this.state;
/* 232 */       if (LOG.isDebugEnabled())
/* 233 */         LOG.debug("onCloseLocal({}) : {}", new Object[] { closeInfo, initialState });
/* 234 */       if (initialState == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 237 */         if (LOG.isDebugEnabled())
/* 238 */           LOG.debug("already closed", new Object[0]);
/* 239 */         return;
/*     */       }
/*     */       
/* 242 */       if (initialState == ConnectionState.CONNECTED)
/*     */       {
/*     */ 
/* 245 */         if (LOG.isDebugEnabled())
/* 246 */           LOG.debug("FastClose in CONNECTED detected", new Object[0]);
/* 247 */         open = true;
/*     */       }
/*     */     }
/*     */     
/* 251 */     if (open) {
/* 252 */       openAndCloseLocal(closeInfo);
/*     */     } else {
/* 254 */       closeLocal(closeInfo);
/*     */     }
/*     */   }
/*     */   
/*     */   private void openAndCloseLocal(CloseInfo closeInfo)
/*     */   {
/* 260 */     onOpened();
/* 261 */     if (LOG.isDebugEnabled())
/* 262 */       LOG.debug("FastClose continuing with Closure", new Object[0]);
/* 263 */     closeLocal(closeInfo);
/*     */   }
/*     */   
/*     */   private void closeLocal(CloseInfo closeInfo)
/*     */   {
/* 268 */     ConnectionState event = null;
/* 269 */     ConnectionState abnormalEvent = null;
/* 270 */     synchronized (this)
/*     */     {
/* 272 */       if (LOG.isDebugEnabled()) {
/* 273 */         LOG.debug("onCloseLocal(), input={}, output={}", new Object[] { Boolean.valueOf(this.inputAvailable), Boolean.valueOf(this.outputAvailable) });
/*     */       }
/* 275 */       this.closeInfo = closeInfo;
/*     */       
/*     */ 
/* 278 */       this.outputAvailable = false;
/*     */       
/* 280 */       if (this.closeHandshakeSource == CloseHandshakeSource.NONE)
/*     */       {
/* 282 */         this.closeHandshakeSource = CloseHandshakeSource.LOCAL;
/*     */       }
/*     */       
/* 285 */       if (!this.inputAvailable)
/*     */       {
/* 287 */         if (LOG.isDebugEnabled())
/* 288 */           LOG.debug("Close Handshake satisfied, disconnecting", new Object[0]);
/* 289 */         this.cleanClose = true;
/* 290 */         this.state = ConnectionState.CLOSED;
/* 291 */         this.finalClose.compareAndSet(null, closeInfo);
/* 292 */         event = this.state;
/*     */       }
/* 294 */       else if (this.state == ConnectionState.OPEN)
/*     */       {
/*     */ 
/* 297 */         this.state = ConnectionState.CLOSING;
/* 298 */         event = this.state;
/*     */         
/*     */ 
/* 301 */         if (closeInfo.isAbnormal())
/*     */         {
/* 303 */           abnormalEvent = ConnectionState.CLOSED;
/* 304 */           this.finalClose.compareAndSet(null, closeInfo);
/* 305 */           this.cleanClose = false;
/* 306 */           this.outputAvailable = false;
/* 307 */           this.inputAvailable = false;
/* 308 */           this.closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 314 */     if (event != null)
/*     */     {
/* 316 */       notifyStateListeners(event);
/* 317 */       if (abnormalEvent != null)
/*     */       {
/* 319 */         notifyStateListeners(abnormalEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onCloseRemote(CloseInfo closeInfo)
/*     */   {
/* 330 */     if (LOG.isDebugEnabled())
/* 331 */       LOG.debug("onCloseRemote({})", new Object[] { closeInfo });
/* 332 */     ConnectionState event = null;
/* 333 */     synchronized (this)
/*     */     {
/* 335 */       if (this.state == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 338 */         return;
/*     */       }
/*     */       
/* 341 */       if (LOG.isDebugEnabled()) {
/* 342 */         LOG.debug("onCloseRemote(), input={}, output={}", new Object[] { Boolean.valueOf(this.inputAvailable), Boolean.valueOf(this.outputAvailable) });
/*     */       }
/* 344 */       this.closeInfo = closeInfo;
/*     */       
/*     */ 
/* 347 */       this.inputAvailable = false;
/*     */       
/* 349 */       if (this.closeHandshakeSource == CloseHandshakeSource.NONE)
/*     */       {
/* 351 */         this.closeHandshakeSource = CloseHandshakeSource.REMOTE;
/*     */       }
/*     */       
/* 354 */       if (!this.outputAvailable)
/*     */       {
/* 356 */         LOG.debug("Close Handshake satisfied, disconnecting", new Object[0]);
/* 357 */         this.cleanClose = true;
/* 358 */         this.state = ConnectionState.CLOSED;
/* 359 */         this.finalClose.compareAndSet(null, closeInfo);
/* 360 */         event = this.state;
/*     */       }
/* 362 */       else if (this.state == ConnectionState.OPEN)
/*     */       {
/*     */ 
/* 365 */         this.state = ConnectionState.CLOSING;
/* 366 */         event = this.state;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 371 */     if (event != null)
/*     */     {
/* 373 */       notifyStateListeners(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onConnected()
/*     */   {
/* 384 */     ConnectionState event = null;
/* 385 */     synchronized (this)
/*     */     {
/* 387 */       if (this.state != ConnectionState.CONNECTING)
/*     */       {
/* 389 */         LOG.debug("Unable to set to connected, not in CONNECTING state: {}", new Object[] { this.state });
/* 390 */         return;
/*     */       }
/*     */       
/* 393 */       this.state = ConnectionState.CONNECTED;
/* 394 */       this.inputAvailable = false;
/* 395 */       this.outputAvailable = true;
/* 396 */       event = this.state;
/*     */     }
/* 398 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFailedUpgrade()
/*     */   {
/* 406 */     assert (this.state == ConnectionState.CONNECTING);
/* 407 */     ConnectionState event = null;
/* 408 */     synchronized (this)
/*     */     {
/* 410 */       this.state = ConnectionState.CLOSED;
/* 411 */       this.cleanClose = false;
/* 412 */       this.inputAvailable = false;
/* 413 */       this.outputAvailable = false;
/* 414 */       event = this.state;
/*     */     }
/* 416 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onOpened()
/*     */   {
/* 424 */     if (LOG.isDebugEnabled()) {
/* 425 */       LOG.debug("onOpened()", new Object[0]);
/*     */     }
/* 427 */     ConnectionState event = null;
/* 428 */     synchronized (this)
/*     */     {
/* 430 */       if (this.state == ConnectionState.OPEN)
/*     */       {
/*     */ 
/* 433 */         return;
/*     */       }
/*     */       
/* 436 */       if (this.state != ConnectionState.CONNECTED)
/*     */       {
/* 438 */         LOG.debug("Unable to open, not in CONNECTED state: {}", new Object[] { this.state });
/* 439 */         return;
/*     */       }
/*     */       
/* 442 */       this.state = ConnectionState.OPEN;
/* 443 */       this.inputAvailable = true;
/* 444 */       this.outputAvailable = true;
/* 445 */       event = this.state;
/*     */     }
/* 447 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onReadFailure(Throwable t)
/*     */   {
/* 458 */     ConnectionState event = null;
/* 459 */     synchronized (this)
/*     */     {
/* 461 */       if (this.state == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 464 */         return;
/*     */       }
/*     */       
/*     */ 
/* 468 */       String reason = "WebSocket Read Failure";
/* 469 */       if ((t instanceof EOFException))
/*     */       {
/* 471 */         reason = "WebSocket Read EOF";
/* 472 */         Throwable cause = t.getCause();
/* 473 */         if ((cause != null) && (StringUtil.isNotBlank(cause.getMessage())))
/*     */         {
/* 475 */           reason = "EOF: " + cause.getMessage();
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 480 */       else if (StringUtil.isNotBlank(t.getMessage()))
/*     */       {
/* 482 */         reason = t.getMessage();
/*     */       }
/*     */       
/*     */ 
/* 486 */       CloseInfo close = new CloseInfo(1006, reason);
/*     */       
/* 488 */       this.finalClose.compareAndSet(null, close);
/*     */       
/* 490 */       this.cleanClose = false;
/* 491 */       this.state = ConnectionState.CLOSED;
/* 492 */       this.closeInfo = close;
/* 493 */       this.inputAvailable = false;
/* 494 */       this.outputAvailable = false;
/* 495 */       this.closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
/* 496 */       event = this.state;
/*     */     }
/* 498 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onWriteFailure(Throwable t)
/*     */   {
/* 509 */     ConnectionState event = null;
/* 510 */     synchronized (this)
/*     */     {
/* 512 */       if (this.state == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 515 */         return;
/*     */       }
/*     */       
/*     */ 
/* 519 */       String reason = "WebSocket Write Failure";
/* 520 */       if ((t instanceof EOFException))
/*     */       {
/* 522 */         reason = "WebSocket Write EOF";
/* 523 */         Throwable cause = t.getCause();
/* 524 */         if ((cause != null) && (StringUtil.isNotBlank(cause.getMessage())))
/*     */         {
/* 526 */           reason = "EOF: " + cause.getMessage();
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 531 */       else if (StringUtil.isNotBlank(t.getMessage()))
/*     */       {
/* 533 */         reason = t.getMessage();
/*     */       }
/*     */       
/*     */ 
/* 537 */       CloseInfo close = new CloseInfo(1006, reason);
/*     */       
/* 539 */       this.finalClose.compareAndSet(null, close);
/*     */       
/* 541 */       this.cleanClose = false;
/* 542 */       this.state = ConnectionState.CLOSED;
/* 543 */       this.inputAvailable = false;
/* 544 */       this.outputAvailable = false;
/* 545 */       this.closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
/* 546 */       event = this.state;
/*     */     }
/* 548 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */   public void onDisconnected()
/*     */   {
/* 553 */     ConnectionState event = null;
/* 554 */     synchronized (this)
/*     */     {
/* 556 */       if (this.state == ConnectionState.CLOSED)
/*     */       {
/*     */ 
/* 559 */         return;
/*     */       }
/*     */       
/* 562 */       CloseInfo close = new CloseInfo(1006, "Disconnected");
/*     */       
/* 564 */       this.cleanClose = false;
/* 565 */       this.state = ConnectionState.CLOSED;
/* 566 */       this.closeInfo = close;
/* 567 */       this.inputAvailable = false;
/* 568 */       this.outputAvailable = false;
/* 569 */       this.closeHandshakeSource = CloseHandshakeSource.ABNORMAL;
/* 570 */       event = this.state;
/*     */     }
/* 572 */     notifyStateListeners(event);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 578 */     StringBuilder str = new StringBuilder();
/* 579 */     str.append(getClass().getSimpleName());
/* 580 */     str.append("@").append(Integer.toHexString(hashCode()));
/* 581 */     str.append("[").append(this.state);
/* 582 */     str.append(',');
/* 583 */     if (!this.inputAvailable)
/*     */     {
/* 585 */       str.append('!');
/*     */     }
/* 587 */     str.append("in,");
/* 588 */     if (!this.outputAvailable)
/*     */     {
/* 590 */       str.append('!');
/*     */     }
/* 592 */     str.append("out");
/* 593 */     if ((this.state == ConnectionState.CLOSED) || (this.state == ConnectionState.CLOSING))
/*     */     {
/* 595 */       CloseInfo ci = (CloseInfo)this.finalClose.get();
/* 596 */       if (ci != null)
/*     */       {
/* 598 */         str.append(",finalClose=").append(ci);
/*     */       }
/*     */       else
/*     */       {
/* 602 */         str.append(",close=").append(this.closeInfo);
/*     */       }
/* 604 */       str.append(",clean=").append(this.cleanClose);
/* 605 */       str.append(",closeSource=").append(this.closeHandshakeSource);
/*     */     }
/* 607 */     str.append(']');
/* 608 */     return str.toString();
/*     */   }
/*     */   
/*     */   public boolean wasAbnormalClose()
/*     */   {
/* 613 */     return this.closeHandshakeSource == CloseHandshakeSource.ABNORMAL;
/*     */   }
/*     */   
/*     */   public boolean wasCleanClose()
/*     */   {
/* 618 */     return this.cleanClose;
/*     */   }
/*     */   
/*     */   public boolean wasLocalCloseInitiated()
/*     */   {
/* 623 */     return this.closeHandshakeSource == CloseHandshakeSource.LOCAL;
/*     */   }
/*     */   
/*     */   public boolean wasRemoteCloseInitiated()
/*     */   {
/* 628 */     return this.closeHandshakeSource == CloseHandshakeSource.REMOTE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\IOState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */