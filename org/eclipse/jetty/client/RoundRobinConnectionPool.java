/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.Connection;
/*     */ import org.eclipse.jetty.client.api.Destination;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
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
/*     */ @ManagedObject
/*     */ public class RoundRobinConnectionPool
/*     */   extends AbstractConnectionPool
/*     */ {
/*     */   private final List<Entry> entries;
/*     */   private int index;
/*     */   
/*     */   public RoundRobinConnectionPool(Destination destination, int maxConnections, Callback requester)
/*     */   {
/*  39 */     super(destination, maxConnections, requester);
/*  40 */     this.entries = new ArrayList(maxConnections);
/*  41 */     for (int i = 0; i < maxConnections; i++) {
/*  42 */       this.entries.add(new Entry(null));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onCreated(Connection connection)
/*     */   {
/*  48 */     synchronized (this)
/*     */     {
/*  50 */       for (Entry entry : this.entries)
/*     */       {
/*  52 */         if (entry.connection == null)
/*     */         {
/*  54 */           entry.connection = connection;
/*  55 */           break;
/*     */         }
/*     */       }
/*     */     }
/*  59 */     idle(connection, false);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Connection activate()
/*     */   {
/*  65 */     Connection connection = null;
/*  66 */     synchronized (this)
/*     */     {
/*  68 */       int offset = 0;
/*  69 */       int capacity = getMaxConnectionCount();
/*  70 */       while (offset < capacity)
/*     */       {
/*  72 */         int idx = this.index + offset;
/*  73 */         if (idx >= capacity) {
/*  74 */           idx -= capacity;
/*     */         }
/*  76 */         Entry entry = (Entry)this.entries.get(idx);
/*     */         
/*  78 */         if (entry.connection == null) {
/*     */           break;
/*     */         }
/*  81 */         if (!entry.active)
/*     */         {
/*  83 */           entry.active = true;
/*  84 */           Entry.access$308(entry);
/*  85 */           connection = entry.connection;
/*  86 */           this.index += offset + 1;
/*  87 */           if (this.index < capacity) break;
/*  88 */           this.index -= capacity; break;
/*     */         }
/*     */         
/*     */ 
/*  92 */         offset++;
/*     */       }
/*     */     }
/*  95 */     return connection == null ? null : active(connection);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isActive(Connection connection)
/*     */   {
/* 101 */     synchronized (this)
/*     */     {
/* 103 */       for (Entry entry : this.entries)
/*     */       {
/* 105 */         if (entry.connection == connection)
/* 106 */           return entry.active;
/*     */       }
/* 108 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean release(Connection connection)
/*     */   {
/* 115 */     boolean active = false;
/* 116 */     synchronized (this)
/*     */     {
/* 118 */       for (Entry entry : this.entries)
/*     */       {
/* 120 */         if (entry.connection == connection)
/*     */         {
/* 122 */           entry.active = false;
/* 123 */           active = true;
/* 124 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 128 */     if (active)
/* 129 */       released(connection);
/* 130 */     return idle(connection, isClosed());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Connection connection)
/*     */   {
/* 136 */     boolean active = false;
/* 137 */     boolean removed = false;
/* 138 */     synchronized (this)
/*     */     {
/* 140 */       for (Entry entry : this.entries)
/*     */       {
/* 142 */         if (entry.connection == connection)
/*     */         {
/* 144 */           active = entry.active;
/* 145 */           entry.reset();
/* 146 */           removed = true;
/* 147 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 151 */     if (active)
/* 152 */       released(connection);
/* 153 */     if (removed)
/* 154 */       removed(connection);
/* 155 */     return removed;
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 161 */     List<Entry> connections = new ArrayList();
/* 162 */     synchronized (this)
/*     */     {
/* 164 */       connections.addAll(this.entries);
/*     */     }
/* 166 */     ContainerLifeCycle.dumpObject(out, this);
/* 167 */     ContainerLifeCycle.dump(out, indent, new Collection[] { connections });
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 173 */     int present = 0;
/* 174 */     int active = 0;
/* 175 */     synchronized (this)
/*     */     {
/* 177 */       for (Entry entry : this.entries)
/*     */       {
/* 179 */         if (entry.connection != null)
/*     */         {
/* 181 */           present++;
/* 182 */           if (entry.active)
/* 183 */             active++;
/*     */         }
/*     */       }
/*     */     }
/* 187 */     return String.format("%s@%x[c=%d/%d,a=%d]", new Object[] {
/* 188 */       getClass().getSimpleName(), 
/* 189 */       Integer.valueOf(hashCode()), 
/* 190 */       Integer.valueOf(present), 
/* 191 */       Integer.valueOf(getMaxConnectionCount()), 
/* 192 */       Integer.valueOf(active) });
/*     */   }
/*     */   
/*     */ 
/*     */   private static class Entry
/*     */   {
/*     */     private Connection connection;
/*     */     private boolean active;
/*     */     private long used;
/*     */     
/*     */     private void reset()
/*     */     {
/* 204 */       this.connection = null;
/* 205 */       this.active = false;
/* 206 */       this.used = 0L;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 212 */       return String.format("{u=%d,c=%s}", new Object[] { Long.valueOf(this.used), this.connection });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\RoundRobinConnectionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */