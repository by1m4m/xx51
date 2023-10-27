/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.concurrent.atomic.LongAdder;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.annotation.ManagedOperation;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.Dumpable;
/*     */ import org.eclipse.jetty.util.statistic.CounterStatistic;
/*     */ import org.eclipse.jetty.util.statistic.SampleStatistic;
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
/*     */ @ManagedObject("Tracks statistics on connections")
/*     */ public class ConnectionStatistics
/*     */   extends AbstractLifeCycle
/*     */   implements Connection.Listener, Dumpable
/*     */ {
/*  47 */   private final CounterStatistic _connections = new CounterStatistic();
/*  48 */   private final SampleStatistic _connectionsDuration = new SampleStatistic();
/*  49 */   private final LongAdder _rcvdBytes = new LongAdder();
/*  50 */   private final AtomicLong _bytesInStamp = new AtomicLong();
/*  51 */   private final LongAdder _sentBytes = new LongAdder();
/*  52 */   private final AtomicLong _bytesOutStamp = new AtomicLong();
/*  53 */   private final LongAdder _messagesIn = new LongAdder();
/*  54 */   private final AtomicLong _messagesInStamp = new AtomicLong();
/*  55 */   private final LongAdder _messagesOut = new LongAdder();
/*  56 */   private final AtomicLong _messagesOutStamp = new AtomicLong();
/*     */   
/*     */   @ManagedOperation(value="Resets the statistics", impact="ACTION")
/*     */   public void reset()
/*     */   {
/*  61 */     this._connections.reset();
/*  62 */     this._connectionsDuration.reset();
/*  63 */     this._rcvdBytes.reset();
/*  64 */     this._bytesInStamp.set(System.nanoTime());
/*  65 */     this._sentBytes.reset();
/*  66 */     this._bytesOutStamp.set(System.nanoTime());
/*  67 */     this._messagesIn.reset();
/*  68 */     this._messagesInStamp.set(System.nanoTime());
/*  69 */     this._messagesOut.reset();
/*  70 */     this._messagesOutStamp.set(System.nanoTime());
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*  76 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onOpened(Connection connection)
/*     */   {
/*  82 */     if (!isStarted()) {
/*  83 */       return;
/*     */     }
/*  85 */     this._connections.increment();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onClosed(Connection connection)
/*     */   {
/*  91 */     if (!isStarted()) {
/*  92 */       return;
/*     */     }
/*  94 */     this._connections.decrement();
/*     */     
/*  96 */     long elapsed = System.currentTimeMillis() - connection.getCreatedTimeStamp();
/*  97 */     this._connectionsDuration.record(elapsed);
/*     */     
/*  99 */     long bytesIn = connection.getBytesIn();
/* 100 */     if (bytesIn > 0L)
/* 101 */       this._rcvdBytes.add(bytesIn);
/* 102 */     long bytesOut = connection.getBytesOut();
/* 103 */     if (bytesOut > 0L) {
/* 104 */       this._sentBytes.add(bytesOut);
/*     */     }
/* 106 */     long messagesIn = connection.getMessagesIn();
/* 107 */     if (messagesIn > 0L)
/* 108 */       this._messagesIn.add(messagesIn);
/* 109 */     long messagesOut = connection.getMessagesOut();
/* 110 */     if (messagesOut > 0L) {
/* 111 */       this._messagesOut.add(messagesOut);
/*     */     }
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of bytes received by tracked connections")
/*     */   public long getReceivedBytes() {
/* 117 */     return this._rcvdBytes.sum();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of bytes received per second since the last invocation of this method")
/*     */   public long getReceivedBytesRate()
/*     */   {
/* 123 */     long now = System.nanoTime();
/* 124 */     long then = this._bytesInStamp.getAndSet(now);
/* 125 */     long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
/* 126 */     return elapsed == 0L ? 0L : getReceivedBytes() * 1000L / elapsed;
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of bytes sent by tracked connections")
/*     */   public long getSentBytes()
/*     */   {
/* 132 */     return this._sentBytes.sum();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of bytes sent per second since the last invocation of this method")
/*     */   public long getSentBytesRate()
/*     */   {
/* 138 */     long now = System.nanoTime();
/* 139 */     long then = this._bytesOutStamp.getAndSet(now);
/* 140 */     long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
/* 141 */     return elapsed == 0L ? 0L : getSentBytes() * 1000L / elapsed;
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The max duration of a connection in ms")
/*     */   public long getConnectionDurationMax()
/*     */   {
/* 147 */     return this._connectionsDuration.getMax();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The mean duration of a connection in ms")
/*     */   public double getConnectionDurationMean()
/*     */   {
/* 153 */     return this._connectionsDuration.getMean();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The standard deviation of the duration of a connection")
/*     */   public double getConnectionDurationStdDev()
/*     */   {
/* 159 */     return this._connectionsDuration.getStdDev();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The total number of connections opened")
/*     */   public long getConnectionsTotal()
/*     */   {
/* 165 */     return this._connections.getTotal();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The current number of open connections")
/*     */   public long getConnections()
/*     */   {
/* 171 */     return this._connections.getCurrent();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The max number of open connections")
/*     */   public long getConnectionsMax()
/*     */   {
/* 177 */     return this._connections.getMax();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The total number of messages received")
/*     */   public long getReceivedMessages()
/*     */   {
/* 183 */     return this._messagesIn.sum();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of messages received per second since the last invocation of this method")
/*     */   public long getReceivedMessagesRate()
/*     */   {
/* 189 */     long now = System.nanoTime();
/* 190 */     long then = this._messagesInStamp.getAndSet(now);
/* 191 */     long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
/* 192 */     return elapsed == 0L ? 0L : getReceivedMessages() * 1000L / elapsed;
/*     */   }
/*     */   
/*     */   @ManagedAttribute("The total number of messages sent")
/*     */   public long getSentMessages()
/*     */   {
/* 198 */     return this._messagesOut.sum();
/*     */   }
/*     */   
/*     */   @ManagedAttribute("Total number of messages sent per second since the last invocation of this method")
/*     */   public long getSentMessagesRate()
/*     */   {
/* 204 */     long now = System.nanoTime();
/* 205 */     long then = this._messagesOutStamp.getAndSet(now);
/* 206 */     long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
/* 207 */     return elapsed == 0L ? 0L : getSentMessages() * 1000L / elapsed;
/*     */   }
/*     */   
/*     */ 
/*     */   public String dump()
/*     */   {
/* 213 */     return ContainerLifeCycle.dump(this);
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 219 */     ContainerLifeCycle.dumpObject(out, this);
/* 220 */     List<String> children = new ArrayList();
/* 221 */     children.add(String.format("connections=%s", new Object[] { this._connections }));
/* 222 */     children.add(String.format("durations=%s", new Object[] { this._connectionsDuration }));
/* 223 */     children.add(String.format("bytes in/out=%s/%s", new Object[] { Long.valueOf(getReceivedBytes()), Long.valueOf(getSentBytes()) }));
/* 224 */     children.add(String.format("messages in/out=%s/%s", new Object[] { Long.valueOf(getReceivedMessages()), Long.valueOf(getSentMessages()) }));
/* 225 */     ContainerLifeCycle.dump(out, indent, new Collection[] { children });
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 231 */     return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\ConnectionStatistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */