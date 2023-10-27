/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.exceptions.MySQLQueryInterruptedException;
/*     */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*     */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
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
/*     */ public class RowDataDynamic
/*     */   implements RowData
/*     */ {
/*     */   private int columnCount;
/*     */   private Field[] metadata;
/*  42 */   private int index = -1;
/*     */   
/*     */   private MysqlIO io;
/*     */   
/*  46 */   private boolean isAfterEnd = false;
/*     */   
/*  48 */   private boolean noMoreRows = false;
/*     */   
/*  50 */   private boolean isBinaryEncoded = false;
/*     */   
/*     */   private ResultSetRow nextRow;
/*     */   
/*     */   private ResultSetImpl owner;
/*     */   
/*  56 */   private boolean streamerClosed = false;
/*     */   
/*  58 */   private boolean wasEmpty = false;
/*     */   
/*     */   private boolean useBufferRowExplicit;
/*     */   
/*     */   private boolean moreResultsExisted;
/*     */   
/*     */   private ExceptionInterceptor exceptionInterceptor;
/*     */   
/*  66 */   private boolean isInterrupted = false;
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
/*     */   public RowDataDynamic(MysqlIO io, int colCount, Field[] fields, boolean isBinaryEncoded)
/*     */     throws SQLException
/*     */   {
/*  84 */     this.io = io;
/*  85 */     this.columnCount = colCount;
/*  86 */     this.isBinaryEncoded = isBinaryEncoded;
/*  87 */     this.metadata = fields;
/*  88 */     this.exceptionInterceptor = this.io.getExceptionInterceptor();
/*  89 */     this.useBufferRowExplicit = MysqlIO.useBufferRowExplicit(this.metadata);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addRow(ResultSetRow row)
/*     */     throws SQLException
/*     */   {
/* 101 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterLast()
/*     */     throws SQLException
/*     */   {
/* 111 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeFirst()
/*     */     throws SQLException
/*     */   {
/* 121 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beforeLast()
/*     */     throws SQLException
/*     */   {
/* 131 */     notSupported();
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
/*     */   public void close()
/*     */     throws SQLException
/*     */   {
/* 146 */     Object mutex = this;
/*     */     
/* 148 */     MySQLConnection conn = null;
/*     */     
/* 150 */     if (this.owner != null) {
/* 151 */       conn = this.owner.connection;
/*     */       
/* 153 */       if (conn != null) {
/* 154 */         mutex = conn.getConnectionMutex();
/*     */       }
/*     */     }
/*     */     
/* 158 */     boolean hadMore = false;
/* 159 */     int howMuchMore = 0;
/*     */     
/* 161 */     synchronized (mutex)
/*     */     {
/* 163 */       while (next() != null) {
/* 164 */         hadMore = true;
/* 165 */         howMuchMore++;
/*     */         
/* 167 */         if (howMuchMore % 100 == 0) {
/* 168 */           Thread.yield();
/*     */         }
/*     */       }
/*     */       
/* 172 */       if (conn != null) {
/* 173 */         if ((!conn.getClobberStreamingResults()) && (conn.getNetTimeoutForStreamingResults() > 0))
/*     */         {
/* 175 */           String oldValue = conn.getServerVariable("net_write_timeout");
/*     */           
/*     */ 
/* 178 */           if ((oldValue == null) || (oldValue.length() == 0)) {
/* 179 */             oldValue = "60";
/*     */           }
/*     */           
/* 182 */           this.io.clearInputStream();
/*     */           
/* 184 */           Statement stmt = null;
/*     */           try
/*     */           {
/* 187 */             stmt = conn.createStatement();
/* 188 */             ((StatementImpl)stmt).executeSimpleNonQuery(conn, "SET net_write_timeout=" + oldValue);
/*     */           } finally {
/* 190 */             if (stmt != null) {
/* 191 */               stmt.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 196 */         if ((conn.getUseUsageAdvisor()) && 
/* 197 */           (hadMore))
/*     */         {
/* 199 */           ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(conn);
/*     */           
/*     */ 
/* 202 */           eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.owner.owningStatement == null ? "N/A" : this.owner.owningStatement.currentCatalog, this.owner.connectionId, this.owner.owningStatement == null ? -1 : this.owner.owningStatement.getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, null, Messages.getString("RowDataDynamic.2") + howMuchMore + Messages.getString("RowDataDynamic.3") + Messages.getString("RowDataDynamic.4") + Messages.getString("RowDataDynamic.5") + Messages.getString("RowDataDynamic.6") + this.owner.pointOfOrigin));
/*     */         }
/*     */       }
/*     */     }
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
/* 233 */     this.metadata = null;
/* 234 */     this.owner = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSetRow getAt(int ind)
/*     */     throws SQLException
/*     */   {
/* 247 */     notSupported();
/*     */     
/* 249 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getCurrentRowNumber()
/*     */     throws SQLException
/*     */   {
/* 260 */     notSupported();
/*     */     
/* 262 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ResultSetInternalMethods getOwner()
/*     */   {
/* 269 */     return this.owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */     throws SQLException
/*     */   {
/* 280 */     boolean hasNext = this.nextRow != null;
/*     */     
/* 282 */     if ((!hasNext) && (!this.streamerClosed)) {
/* 283 */       this.io.closeStreamer(this);
/* 284 */       this.streamerClosed = true;
/*     */     }
/*     */     
/* 287 */     return hasNext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAfterLast()
/*     */     throws SQLException
/*     */   {
/* 298 */     return this.isAfterEnd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isBeforeFirst()
/*     */     throws SQLException
/*     */   {
/* 309 */     return this.index < 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDynamic()
/*     */   {
/* 321 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */     throws SQLException
/*     */   {
/* 332 */     notSupported();
/*     */     
/* 334 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFirst()
/*     */     throws SQLException
/*     */   {
/* 345 */     notSupported();
/*     */     
/* 347 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLast()
/*     */     throws SQLException
/*     */   {
/* 358 */     notSupported();
/*     */     
/* 360 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void moveRowRelative(int rows)
/*     */     throws SQLException
/*     */   {
/* 372 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResultSetRow next()
/*     */     throws SQLException
/*     */   {
/* 385 */     nextRecord();
/*     */     
/* 387 */     if ((this.nextRow == null) && (!this.streamerClosed) && (!this.moreResultsExisted)) {
/* 388 */       this.io.closeStreamer(this);
/* 389 */       this.streamerClosed = true;
/*     */     }
/*     */     
/* 392 */     if ((this.nextRow != null) && 
/* 393 */       (this.index != Integer.MAX_VALUE)) {
/* 394 */       this.index += 1;
/*     */     }
/*     */     
/*     */ 
/* 398 */     return this.nextRow;
/*     */   }
/*     */   
/*     */   private void nextRecord() throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 405 */       if (!this.noMoreRows) {
/* 406 */         this.nextRow = (this.isInterrupted ? null : this.io.nextRow(this.metadata, this.columnCount, this.isBinaryEncoded, 1007, true, this.useBufferRowExplicit, true, null));
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */         if (this.nextRow == null) {
/* 413 */           this.noMoreRows = true;
/* 414 */           this.isAfterEnd = true;
/* 415 */           this.moreResultsExisted = this.io.tackOnMoreStreamingResults(this.owner);
/*     */           
/* 417 */           if (this.index == -1) {
/* 418 */             this.wasEmpty = true;
/*     */           }
/*     */         }
/*     */       } else {
/* 422 */         this.isAfterEnd = true;
/*     */       }
/*     */     } catch (SQLException sqlEx) {
/* 425 */       if ((sqlEx instanceof StreamingNotifiable)) {
/* 426 */         ((StreamingNotifiable)sqlEx).setWasStreamingResults();
/* 427 */       } else if ((sqlEx instanceof MySQLQueryInterruptedException)) {
/* 428 */         this.isInterrupted = true;
/*     */       }
/*     */       
/*     */ 
/* 432 */       throw sqlEx;
/*     */     } catch (Exception ex) {
/* 434 */       String exceptionType = ex.getClass().getName();
/* 435 */       String exceptionMessage = ex.getMessage();
/*     */       
/* 437 */       exceptionMessage = exceptionMessage + Messages.getString("RowDataDynamic.7");
/* 438 */       exceptionMessage = exceptionMessage + Util.stackTraceToString(ex);
/*     */       
/* 440 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("RowDataDynamic.8") + exceptionType + Messages.getString("RowDataDynamic.9") + exceptionMessage, "S1000", this.exceptionInterceptor);
/*     */       
/*     */ 
/*     */ 
/* 444 */       sqlEx.initCause(ex);
/*     */       
/* 446 */       throw sqlEx;
/*     */     }
/*     */   }
/*     */   
/*     */   private void notSupported() throws SQLException {
/* 451 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeRow(int ind)
/*     */     throws SQLException
/*     */   {
/* 463 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCurrentRow(int rowNumber)
/*     */     throws SQLException
/*     */   {
/* 475 */     notSupported();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setOwner(ResultSetImpl rs)
/*     */   {
/* 482 */     this.owner = rs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 491 */     return -1;
/*     */   }
/*     */   
/*     */   public boolean wasEmpty() {
/* 495 */     return this.wasEmpty;
/*     */   }
/*     */   
/*     */   public void setMetadata(Field[] metadata) {
/* 499 */     this.metadata = metadata;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\RowDataDynamic.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */