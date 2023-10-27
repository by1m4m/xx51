/*     */ package org.apache.commons.net.telnet;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TelnetClient
/*     */   extends Telnet
/*     */ {
/*     */   private InputStream __input;
/*     */   private OutputStream __output;
/*  48 */   protected boolean readerThread = true;
/*     */   
/*     */ 
/*     */   private TelnetInputListener inputListener;
/*     */   
/*     */ 
/*     */ 
/*     */   public TelnetClient()
/*     */   {
/*  57 */     super("VT100");
/*     */     
/*  59 */     this.__input = null;
/*  60 */     this.__output = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TelnetClient(String termtype)
/*     */   {
/*  71 */     super(termtype);
/*  72 */     this.__input = null;
/*  73 */     this.__output = null;
/*     */   }
/*     */   
/*     */   void _flushOutputStream()
/*     */     throws IOException
/*     */   {
/*  79 */     this._output_.flush();
/*     */   }
/*     */   
/*     */   void _closeOutputStream() throws IOException {
/*     */     try {
/*  84 */       this._output_.close();
/*     */     } finally {
/*  86 */       this._output_ = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _connectAction_()
/*     */     throws IOException
/*     */   {
/*  98 */     super._connectAction_();
/*  99 */     TelnetInputStream tmp = new TelnetInputStream(this._input_, this, this.readerThread);
/* 100 */     if (this.readerThread)
/*     */     {
/* 102 */       tmp._start();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */     this.__input = new BufferedInputStream(tmp);
/* 111 */     this.__output = new TelnetOutputStream(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 125 */       if (this.__input != null) {
/* 126 */         this.__input.close();
/*     */       }
/* 128 */       if (this.__output != null) {
/* 129 */         this.__output.close();
/*     */       }
/*     */     } finally {
/* 132 */       this.__output = null;
/* 133 */       this.__input = null;
/* 134 */       super.disconnect();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 147 */     return this.__output;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/* 159 */     return this.__input;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getLocalOptionState(int option)
/*     */   {
/* 172 */     return (_stateIsWill(option)) && (_requestedWill(option));
/*     */   }
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
/*     */   public boolean getRemoteOptionState(int option)
/*     */   {
/* 186 */     return (_stateIsDo(option)) && (_requestedDo(option));
/*     */   }
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
/*     */   public boolean sendAYT(long timeout)
/*     */     throws IOException, IllegalArgumentException, InterruptedException
/*     */   {
/* 207 */     return _sendAYT(timeout);
/*     */   }
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
/*     */   public void sendSubnegotiation(int[] message)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 231 */     if (message.length < 1) {
/* 232 */       throw new IllegalArgumentException("zero length message");
/*     */     }
/* 234 */     _sendSubnegotiation(message);
/*     */   }
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
/*     */   public void sendCommand(byte command)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 254 */     _sendCommand(command);
/*     */   }
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
/*     */   public void addOptionHandler(TelnetOptionHandler opthand)
/*     */     throws InvalidTelnetOptionException, IOException
/*     */   {
/* 271 */     super.addOptionHandler(opthand);
/*     */   }
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
/*     */   public void deleteOptionHandler(int optcode)
/*     */     throws InvalidTelnetOptionException, IOException
/*     */   {
/* 287 */     super.deleteOptionHandler(optcode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerSpyStream(OutputStream spystream)
/*     */   {
/* 300 */     super._registerSpyStream(spystream);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stopSpyStream()
/*     */   {
/* 309 */     super._stopSpyStream();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerNotifHandler(TelnetNotificationHandler notifhand)
/*     */   {
/* 322 */     super.registerNotifHandler(notifhand);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregisterNotifHandler()
/*     */   {
/* 332 */     super.unregisterNotifHandler();
/*     */   }
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
/*     */   public void setReaderThread(boolean flag)
/*     */   {
/* 363 */     this.readerThread = flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getReaderThread()
/*     */   {
/* 373 */     return this.readerThread;
/*     */   }
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
/*     */   public synchronized void registerInputListener(TelnetInputListener listener)
/*     */   {
/* 400 */     this.inputListener = listener;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void unregisterInputListener()
/*     */   {
/* 410 */     this.inputListener = null;
/*     */   }
/*     */   
/*     */   void notifyInputListener()
/*     */   {
/*     */     TelnetInputListener listener;
/* 416 */     synchronized (this) {
/* 417 */       listener = this.inputListener;
/*     */     }
/* 419 */     if (listener != null) {
/* 420 */       listener.telnetInputAvailable();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\telnet\TelnetClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */