/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.sql.Date;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.TimeZone;
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
/*     */ public class BufferRow
/*     */   extends ResultSetRow
/*     */ {
/*     */   private Buffer rowFromServer;
/*  57 */   private int homePosition = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private int preNullBitmaskHomePosition = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private int lastRequestedIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int lastRequestedPos;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Field[] metadata;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isBinaryEncoded;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean[] isNull;
/*     */   
/*     */ 
/*     */ 
/*     */   private List<InputStream> openStreams;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BufferRow(Buffer buf, Field[] fields, boolean isBinaryEncoded, ExceptionInterceptor exceptionInterceptor)
/*     */     throws SQLException
/*     */   {
/* 101 */     super(exceptionInterceptor);
/*     */     
/* 103 */     this.rowFromServer = buf;
/* 104 */     this.metadata = fields;
/* 105 */     this.isBinaryEncoded = isBinaryEncoded;
/* 106 */     this.homePosition = this.rowFromServer.getPosition();
/* 107 */     this.preNullBitmaskHomePosition = this.homePosition;
/*     */     
/* 109 */     if (fields != null) {
/* 110 */       setMetadata(fields);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void closeOpenStreams() {
/* 115 */     if (this.openStreams != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */       Iterator<InputStream> iter = this.openStreams.iterator();
/*     */       
/* 123 */       while (iter.hasNext()) {
/*     */         try
/*     */         {
/* 126 */           ((InputStream)iter.next()).close();
/*     */         }
/*     */         catch (IOException e) {}
/*     */       }
/*     */       
/*     */ 
/* 132 */       this.openStreams.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   private int findAndSeekToOffset(int index) throws SQLException {
/* 137 */     if (!this.isBinaryEncoded)
/*     */     {
/* 139 */       if (index == 0) {
/* 140 */         this.lastRequestedIndex = 0;
/* 141 */         this.lastRequestedPos = this.homePosition;
/* 142 */         this.rowFromServer.setPosition(this.homePosition);
/*     */         
/* 144 */         return 0;
/*     */       }
/*     */       
/* 147 */       if (index == this.lastRequestedIndex) {
/* 148 */         this.rowFromServer.setPosition(this.lastRequestedPos);
/*     */         
/* 150 */         return this.lastRequestedPos;
/*     */       }
/*     */       
/* 153 */       int startingIndex = 0;
/*     */       
/* 155 */       if (index > this.lastRequestedIndex) {
/* 156 */         if (this.lastRequestedIndex >= 0) {
/* 157 */           startingIndex = this.lastRequestedIndex;
/*     */         } else {
/* 159 */           startingIndex = 0;
/*     */         }
/*     */         
/* 162 */         this.rowFromServer.setPosition(this.lastRequestedPos);
/*     */       } else {
/* 164 */         this.rowFromServer.setPosition(this.homePosition);
/*     */       }
/*     */       
/* 167 */       for (int i = startingIndex; i < index; i++) {
/* 168 */         this.rowFromServer.fastSkipLenByteArray();
/*     */       }
/*     */       
/* 171 */       this.lastRequestedIndex = index;
/* 172 */       this.lastRequestedPos = this.rowFromServer.getPosition();
/*     */       
/* 174 */       return this.lastRequestedPos;
/*     */     }
/*     */     
/* 177 */     return findAndSeekToOffsetForBinaryEncoding(index);
/*     */   }
/*     */   
/*     */   private int findAndSeekToOffsetForBinaryEncoding(int index) throws SQLException
/*     */   {
/* 182 */     if (index == 0) {
/* 183 */       this.lastRequestedIndex = 0;
/* 184 */       this.lastRequestedPos = this.homePosition;
/* 185 */       this.rowFromServer.setPosition(this.homePosition);
/*     */       
/* 187 */       return 0;
/*     */     }
/*     */     
/* 190 */     if (index == this.lastRequestedIndex) {
/* 191 */       this.rowFromServer.setPosition(this.lastRequestedPos);
/*     */       
/* 193 */       return this.lastRequestedPos;
/*     */     }
/*     */     
/* 196 */     int startingIndex = 0;
/*     */     
/* 198 */     if (index > this.lastRequestedIndex) {
/* 199 */       if (this.lastRequestedIndex >= 0) {
/* 200 */         startingIndex = this.lastRequestedIndex;
/*     */       }
/*     */       else {
/* 203 */         startingIndex = 0;
/* 204 */         this.lastRequestedPos = this.homePosition;
/*     */       }
/*     */       
/* 207 */       this.rowFromServer.setPosition(this.lastRequestedPos);
/*     */     } else {
/* 209 */       this.rowFromServer.setPosition(this.homePosition);
/*     */     }
/*     */     
/* 212 */     for (int i = startingIndex; i < index; i++) {
/* 213 */       if (this.isNull[i] == 0)
/*     */       {
/*     */ 
/*     */ 
/* 217 */         int curPosition = this.rowFromServer.getPosition();
/*     */         
/* 219 */         switch (this.metadata[i].getMysqlType())
/*     */         {
/*     */         case 6: 
/*     */           break;
/*     */         
/*     */         case 1: 
/* 225 */           this.rowFromServer.setPosition(curPosition + 1);
/* 226 */           break;
/*     */         
/*     */         case 2: 
/*     */         case 13: 
/* 230 */           this.rowFromServer.setPosition(curPosition + 2);
/*     */           
/* 232 */           break;
/*     */         case 3: 
/*     */         case 9: 
/* 235 */           this.rowFromServer.setPosition(curPosition + 4);
/*     */           
/* 237 */           break;
/*     */         case 8: 
/* 239 */           this.rowFromServer.setPosition(curPosition + 8);
/*     */           
/* 241 */           break;
/*     */         case 4: 
/* 243 */           this.rowFromServer.setPosition(curPosition + 4);
/*     */           
/* 245 */           break;
/*     */         case 5: 
/* 247 */           this.rowFromServer.setPosition(curPosition + 8);
/*     */           
/* 249 */           break;
/*     */         case 11: 
/* 251 */           this.rowFromServer.fastSkipLenByteArray();
/*     */           
/* 253 */           break;
/*     */         
/*     */         case 10: 
/* 256 */           this.rowFromServer.fastSkipLenByteArray();
/*     */           
/* 258 */           break;
/*     */         case 7: 
/*     */         case 12: 
/* 261 */           this.rowFromServer.fastSkipLenByteArray();
/*     */           
/* 263 */           break;
/*     */         case 0: 
/*     */         case 15: 
/*     */         case 16: 
/*     */         case 246: 
/*     */         case 249: 
/*     */         case 250: 
/*     */         case 251: 
/*     */         case 252: 
/*     */         case 253: 
/*     */         case 254: 
/*     */         case 255: 
/* 275 */           this.rowFromServer.fastSkipLenByteArray();
/*     */           
/* 277 */           break;
/*     */         
/*     */         default: 
/* 280 */           throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + this.metadata[i].getMysqlType() + Messages.getString("MysqlIO.98") + (i + 1) + Messages.getString("MysqlIO.99") + this.metadata.length + Messages.getString("MysqlIO.100"), "S1000", this.exceptionInterceptor);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     this.lastRequestedIndex = index;
/* 293 */     this.lastRequestedPos = this.rowFromServer.getPosition();
/*     */     
/* 295 */     return this.lastRequestedPos;
/*     */   }
/*     */   
/*     */   public synchronized InputStream getBinaryInputStream(int columnIndex) throws SQLException
/*     */   {
/* 300 */     if ((this.isBinaryEncoded) && 
/* 301 */       (isNull(columnIndex))) {
/* 302 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 306 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 308 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 310 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 312 */     if (length == -1L) {
/* 313 */       return null;
/*     */     }
/*     */     
/* 316 */     InputStream stream = new ByteArrayInputStream(this.rowFromServer.getByteBuffer(), offset, (int)length);
/*     */     
/*     */ 
/* 319 */     if (this.openStreams == null) {
/* 320 */       this.openStreams = new LinkedList();
/*     */     }
/*     */     
/* 323 */     return stream;
/*     */   }
/*     */   
/*     */   public byte[] getColumnValue(int index) throws SQLException {
/* 327 */     findAndSeekToOffset(index);
/*     */     
/* 329 */     if (!this.isBinaryEncoded) {
/* 330 */       return this.rowFromServer.readLenByteArray(0);
/*     */     }
/*     */     
/* 333 */     if (this.isNull[index] != 0) {
/* 334 */       return null;
/*     */     }
/*     */     
/* 337 */     switch (this.metadata[index].getMysqlType()) {
/*     */     case 6: 
/* 339 */       return null;
/*     */     
/*     */     case 1: 
/* 342 */       return new byte[] { this.rowFromServer.readByte() };
/*     */     
/*     */     case 2: 
/*     */     case 13: 
/* 346 */       return this.rowFromServer.getBytes(2);
/*     */     
/*     */     case 3: 
/*     */     case 9: 
/* 350 */       return this.rowFromServer.getBytes(4);
/*     */     
/*     */     case 8: 
/* 353 */       return this.rowFromServer.getBytes(8);
/*     */     
/*     */     case 4: 
/* 356 */       return this.rowFromServer.getBytes(4);
/*     */     
/*     */     case 5: 
/* 359 */       return this.rowFromServer.getBytes(8);
/*     */     
/*     */     case 0: 
/*     */     case 7: 
/*     */     case 10: 
/*     */     case 11: 
/*     */     case 12: 
/*     */     case 15: 
/*     */     case 16: 
/*     */     case 246: 
/*     */     case 249: 
/*     */     case 250: 
/*     */     case 251: 
/*     */     case 252: 
/*     */     case 253: 
/*     */     case 254: 
/*     */     case 255: 
/* 376 */       return this.rowFromServer.readLenByteArray(0);
/*     */     }
/*     */     
/* 379 */     throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + this.metadata[index].getMysqlType() + Messages.getString("MysqlIO.98") + (index + 1) + Messages.getString("MysqlIO.99") + this.metadata.length + Messages.getString("MysqlIO.100"), "S1000", this.exceptionInterceptor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInt(int columnIndex)
/*     */     throws SQLException
/*     */   {
/* 391 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 393 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 395 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 397 */     if (length == -1L) {
/* 398 */       return 0;
/*     */     }
/*     */     
/* 401 */     return StringUtils.getInt(this.rowFromServer.getByteBuffer(), offset, offset + (int)length);
/*     */   }
/*     */   
/*     */   public long getLong(int columnIndex) throws SQLException
/*     */   {
/* 406 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 408 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 410 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 412 */     if (length == -1L) {
/* 413 */       return 0L;
/*     */     }
/*     */     
/* 416 */     return StringUtils.getLong(this.rowFromServer.getByteBuffer(), offset, offset + (int)length);
/*     */   }
/*     */   
/*     */   public double getNativeDouble(int columnIndex) throws SQLException
/*     */   {
/* 421 */     if (isNull(columnIndex)) {
/* 422 */       return 0.0D;
/*     */     }
/*     */     
/* 425 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 427 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 429 */     return getNativeDouble(this.rowFromServer.getByteBuffer(), offset);
/*     */   }
/*     */   
/*     */   public float getNativeFloat(int columnIndex) throws SQLException {
/* 433 */     if (isNull(columnIndex)) {
/* 434 */       return 0.0F;
/*     */     }
/*     */     
/* 437 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 439 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 441 */     return getNativeFloat(this.rowFromServer.getByteBuffer(), offset);
/*     */   }
/*     */   
/*     */   public int getNativeInt(int columnIndex) throws SQLException {
/* 445 */     if (isNull(columnIndex)) {
/* 446 */       return 0;
/*     */     }
/*     */     
/* 449 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 451 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 453 */     return getNativeInt(this.rowFromServer.getByteBuffer(), offset);
/*     */   }
/*     */   
/*     */   public long getNativeLong(int columnIndex) throws SQLException {
/* 457 */     if (isNull(columnIndex)) {
/* 458 */       return 0L;
/*     */     }
/*     */     
/* 461 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 463 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 465 */     return getNativeLong(this.rowFromServer.getByteBuffer(), offset);
/*     */   }
/*     */   
/*     */   public short getNativeShort(int columnIndex) throws SQLException {
/* 469 */     if (isNull(columnIndex)) {
/* 470 */       return 0;
/*     */     }
/*     */     
/* 473 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 475 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 477 */     return getNativeShort(this.rowFromServer.getByteBuffer(), offset);
/*     */   }
/*     */   
/*     */   public Timestamp getNativeTimestamp(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 483 */     if (isNull(columnIndex)) {
/* 484 */       return null;
/*     */     }
/*     */     
/* 487 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 489 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 491 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 493 */     return getNativeTimestamp(this.rowFromServer.getByteBuffer(), offset, (int)length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public Reader getReader(int columnIndex) throws SQLException
/*     */   {
/* 498 */     InputStream stream = getBinaryInputStream(columnIndex);
/*     */     
/* 500 */     if (stream == null) {
/* 501 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 505 */       return new InputStreamReader(stream, this.metadata[columnIndex].getCharacterSet());
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 508 */       SQLException sqlEx = SQLError.createSQLException("", this.exceptionInterceptor);
/*     */       
/* 510 */       sqlEx.initCause(e);
/*     */       
/* 512 */       throw sqlEx;
/*     */     }
/*     */   }
/*     */   
/*     */   public String getString(int columnIndex, String encoding, MySQLConnection conn) throws SQLException
/*     */   {
/* 518 */     if ((this.isBinaryEncoded) && 
/* 519 */       (isNull(columnIndex))) {
/* 520 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 524 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 526 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 528 */     if (length == -1L) {
/* 529 */       return null;
/*     */     }
/*     */     
/* 532 */     if (length == 0L) {
/* 533 */       return "";
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 539 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 541 */     return getString(encoding, conn, this.rowFromServer.getByteBuffer(), offset, (int)length);
/*     */   }
/*     */   
/*     */ 
/*     */   public Time getTimeFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 548 */     if (isNull(columnIndex)) {
/* 549 */       return null;
/*     */     }
/*     */     
/* 552 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 554 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 556 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 558 */     return getTimeFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */ 
/*     */   public Timestamp getTimestampFast(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 565 */     if (isNull(columnIndex)) {
/* 566 */       return null;
/*     */     }
/*     */     
/* 569 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 571 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 573 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 575 */     return getTimestampFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public boolean isFloatingPointNumber(int index)
/*     */     throws SQLException
/*     */   {
/* 581 */     if (this.isBinaryEncoded) {
/* 582 */       switch (this.metadata[index].getSQLType()) {
/*     */       case 2: 
/*     */       case 3: 
/*     */       case 6: 
/*     */       case 8: 
/* 587 */         return true;
/*     */       }
/* 589 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 593 */     findAndSeekToOffset(index);
/*     */     
/* 595 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 597 */     if (length == -1L) {
/* 598 */       return false;
/*     */     }
/*     */     
/* 601 */     if (length == 0L) {
/* 602 */       return false;
/*     */     }
/*     */     
/* 605 */     int offset = this.rowFromServer.getPosition();
/* 606 */     byte[] buffer = this.rowFromServer.getByteBuffer();
/*     */     
/* 608 */     for (int i = 0; i < (int)length; i++) {
/* 609 */       char c = (char)buffer[(offset + i)];
/*     */       
/* 611 */       if ((c == 'e') || (c == 'E')) {
/* 612 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 616 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isNull(int index) throws SQLException {
/* 620 */     if (!this.isBinaryEncoded) {
/* 621 */       findAndSeekToOffset(index);
/*     */       
/* 623 */       return this.rowFromServer.readFieldLength() == -1L;
/*     */     }
/*     */     
/* 626 */     return this.isNull[index];
/*     */   }
/*     */   
/*     */   public long length(int index) throws SQLException {
/* 630 */     findAndSeekToOffset(index);
/*     */     
/* 632 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 634 */     if (length == -1L) {
/* 635 */       return 0L;
/*     */     }
/*     */     
/* 638 */     return length;
/*     */   }
/*     */   
/*     */   public void setColumnValue(int index, byte[] value) throws SQLException {
/* 642 */     throw new OperationNotSupportedException();
/*     */   }
/*     */   
/*     */   public ResultSetRow setMetadata(Field[] f) throws SQLException {
/* 646 */     super.setMetadata(f);
/*     */     
/* 648 */     if (this.isBinaryEncoded) {
/* 649 */       setupIsNullBitmask();
/*     */     }
/*     */     
/* 652 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setupIsNullBitmask()
/*     */     throws SQLException
/*     */   {
/* 661 */     if (this.isNull != null) {
/* 662 */       return;
/*     */     }
/*     */     
/* 665 */     this.rowFromServer.setPosition(this.preNullBitmaskHomePosition);
/*     */     
/* 667 */     int nullCount = (this.metadata.length + 9) / 8;
/*     */     
/* 669 */     byte[] nullBitMask = new byte[nullCount];
/*     */     
/* 671 */     for (int i = 0; i < nullCount; i++) {
/* 672 */       nullBitMask[i] = this.rowFromServer.readByte();
/*     */     }
/*     */     
/* 675 */     this.homePosition = this.rowFromServer.getPosition();
/*     */     
/* 677 */     this.isNull = new boolean[this.metadata.length];
/*     */     
/* 679 */     int nullMaskPos = 0;
/* 680 */     int bit = 4;
/*     */     
/* 682 */     for (int i = 0; i < this.metadata.length; i++)
/*     */     {
/* 684 */       this.isNull[i] = ((nullBitMask[nullMaskPos] & bit) != 0 ? 1 : false);
/*     */       
/* 686 */       if ((bit <<= 1 & 0xFF) == 0) {
/* 687 */         bit = 1;
/*     */         
/* 689 */         nullMaskPos++;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Date getDateFast(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar targetCalendar) throws SQLException
/*     */   {
/* 696 */     if (isNull(columnIndex)) {
/* 697 */       return null;
/*     */     }
/*     */     
/* 700 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 702 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 704 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 706 */     return getDateFast(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, conn, rs, targetCalendar);
/*     */   }
/*     */   
/*     */   public Date getNativeDate(int columnIndex, MySQLConnection conn, ResultSetImpl rs, Calendar cal)
/*     */     throws SQLException
/*     */   {
/* 712 */     if (isNull(columnIndex)) {
/* 713 */       return null;
/*     */     }
/*     */     
/* 716 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 718 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 720 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 722 */     return getNativeDate(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, conn, rs, cal);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getNativeDateTimeValue(int columnIndex, Calendar targetCalendar, int jdbcType, int mysqlType, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 730 */     if (isNull(columnIndex)) {
/* 731 */       return null;
/*     */     }
/*     */     
/* 734 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 736 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 738 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 740 */     return getNativeDateTimeValue(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, targetCalendar, jdbcType, mysqlType, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Time getNativeTime(int columnIndex, Calendar targetCalendar, TimeZone tz, boolean rollForward, MySQLConnection conn, ResultSetImpl rs)
/*     */     throws SQLException
/*     */   {
/* 748 */     if (isNull(columnIndex)) {
/* 749 */       return null;
/*     */     }
/*     */     
/* 752 */     findAndSeekToOffset(columnIndex);
/*     */     
/* 754 */     long length = this.rowFromServer.readFieldLength();
/*     */     
/* 756 */     int offset = this.rowFromServer.getPosition();
/*     */     
/* 758 */     return getNativeTime(columnIndex, this.rowFromServer.getByteBuffer(), offset, (int)length, targetCalendar, tz, rollForward, conn, rs);
/*     */   }
/*     */   
/*     */   public int getBytesSize()
/*     */   {
/* 763 */     return this.rowFromServer.getBufLength();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\BufferRow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */