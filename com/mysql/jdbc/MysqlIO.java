/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.authentication.MysqlClearPasswordPlugin;
/*      */ import com.mysql.jdbc.authentication.MysqlNativePasswordPlugin;
/*      */ import com.mysql.jdbc.authentication.MysqlOldPasswordPlugin;
/*      */ import com.mysql.jdbc.authentication.Sha256PasswordPlugin;
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.log.Log;
/*      */ import com.mysql.jdbc.log.LogUtils;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import com.mysql.jdbc.util.ReadAheadInputStream;
/*      */ import com.mysql.jdbc.util.ResultSetUtil;
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.management.ThreadInfo;
/*      */ import java.lang.management.ThreadMXBean;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.math.BigInteger;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.URL;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.zip.Deflater;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MysqlIO
/*      */ {
/*      */   private static final int UTF8_CHARSET_INDEX = 33;
/*      */   private static final String CODE_PAGE_1252 = "Cp1252";
/*      */   protected static final int NULL_LENGTH = -1;
/*      */   protected static final int COMP_HEADER_LENGTH = 3;
/*      */   protected static final int MIN_COMPRESS_LEN = 50;
/*      */   protected static final int HEADER_LENGTH = 4;
/*      */   protected static final int AUTH_411_OVERHEAD = 33;
/*   88 */   private static int maxBufferSize = 65535;
/*      */   
/*      */   private static final String NONE = "none";
/*      */   
/*      */   private static final int CLIENT_LONG_PASSWORD = 1;
/*      */   
/*      */   private static final int CLIENT_FOUND_ROWS = 2;
/*      */   
/*      */   private static final int CLIENT_LONG_FLAG = 4;
/*      */   
/*      */   protected static final int CLIENT_CONNECT_WITH_DB = 8;
/*      */   
/*      */   private static final int CLIENT_COMPRESS = 32;
/*      */   
/*      */   private static final int CLIENT_LOCAL_FILES = 128;
/*      */   private static final int CLIENT_PROTOCOL_41 = 512;
/*      */   private static final int CLIENT_INTERACTIVE = 1024;
/*      */   protected static final int CLIENT_SSL = 2048;
/*      */   private static final int CLIENT_TRANSACTIONS = 8192;
/*      */   protected static final int CLIENT_RESERVED = 16384;
/*      */   protected static final int CLIENT_SECURE_CONNECTION = 32768;
/*      */   private static final int CLIENT_MULTI_STATEMENTS = 65536;
/*      */   private static final int CLIENT_MULTI_RESULTS = 131072;
/*      */   private static final int CLIENT_PLUGIN_AUTH = 524288;
/*      */   private static final int CLIENT_CONNECT_ATTRS = 1048576;
/*      */   private static final int CLIENT_PLUGIN_AUTH_LENENC_CLIENT_DATA = 2097152;
/*      */   private static final int CLIENT_CAN_HANDLE_EXPIRED_PASSWORD = 4194304;
/*      */   private static final int SERVER_STATUS_IN_TRANS = 1;
/*      */   private static final int SERVER_STATUS_AUTOCOMMIT = 2;
/*      */   static final int SERVER_MORE_RESULTS_EXISTS = 8;
/*      */   private static final int SERVER_QUERY_NO_GOOD_INDEX_USED = 16;
/*      */   private static final int SERVER_QUERY_NO_INDEX_USED = 32;
/*      */   private static final int SERVER_QUERY_WAS_SLOW = 2048;
/*      */   private static final int SERVER_STATUS_CURSOR_EXISTS = 64;
/*      */   private static final String FALSE_SCRAMBLE = "xxxxxxxx";
/*      */   protected static final int MAX_QUERY_SIZE_TO_LOG = 1024;
/*      */   protected static final int MAX_QUERY_SIZE_TO_EXPLAIN = 1048576;
/*      */   protected static final int INITIAL_PACKET_SIZE = 1024;
/*  126 */   private static String jvmPlatformCharset = null;
/*      */   
/*      */ 
/*      */   protected static final String ZERO_DATE_VALUE_MARKER = "0000-00-00";
/*      */   
/*      */ 
/*      */   protected static final String ZERO_DATETIME_VALUE_MARKER = "0000-00-00 00:00:00";
/*      */   
/*      */   private static final String EXPLAINABLE_STATEMENT = "SELECT";
/*      */   
/*  136 */   private static final String[] EXPLAINABLE_STATEMENT_EXTENSION = { "INSERT", "UPDATE", "REPLACE", "DELETE" };
/*      */   private static final int MAX_PACKET_DUMP_LENGTH = 1024;
/*      */   
/*      */   static {
/*  140 */     OutputStreamWriter outWriter = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  148 */       outWriter = new OutputStreamWriter(new ByteArrayOutputStream());
/*  149 */       jvmPlatformCharset = outWriter.getEncoding();
/*      */     } finally {
/*      */       try {
/*  152 */         if (outWriter != null) {
/*  153 */           outWriter.close();
/*      */         }
/*      */       }
/*      */       catch (IOException ioEx) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  163 */   private boolean packetSequenceReset = false;
/*      */   
/*      */ 
/*      */ 
/*      */   protected int serverCharsetIndex;
/*      */   
/*      */ 
/*      */ 
/*  171 */   private Buffer reusablePacket = null;
/*  172 */   private Buffer sendPacket = null;
/*  173 */   private Buffer sharedSendPacket = null;
/*      */   
/*      */ 
/*  176 */   protected BufferedOutputStream mysqlOutput = null;
/*      */   protected MySQLConnection connection;
/*  178 */   private Deflater deflater = null;
/*  179 */   protected InputStream mysqlInput = null;
/*  180 */   private LinkedList<StringBuffer> packetDebugRingBuffer = null;
/*  181 */   private RowData streamingData = null;
/*      */   
/*      */ 
/*  184 */   protected Socket mysqlConnection = null;
/*  185 */   protected SocketFactory socketFactory = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SoftReference<Buffer> loadFileBufRef;
/*      */   
/*      */ 
/*      */ 
/*      */   private SoftReference<Buffer> splitBufRef;
/*      */   
/*      */ 
/*      */ 
/*      */   private SoftReference<Buffer> compressBufRef;
/*      */   
/*      */ 
/*      */ 
/*  202 */   protected String host = null;
/*      */   protected String seed;
/*  204 */   private String serverVersion = null;
/*  205 */   private String socketFactoryClassName = null;
/*  206 */   private byte[] packetHeaderBuf = new byte[4];
/*  207 */   private boolean colDecimalNeedsBump = false;
/*  208 */   private boolean hadWarnings = false;
/*  209 */   private boolean has41NewNewProt = false;
/*      */   
/*      */ 
/*  212 */   private boolean hasLongColumnInfo = false;
/*  213 */   private boolean isInteractiveClient = false;
/*  214 */   private boolean logSlowQueries = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  220 */   private boolean platformDbCharsetMatches = true;
/*  221 */   private boolean profileSql = false;
/*  222 */   private boolean queryBadIndexUsed = false;
/*  223 */   private boolean queryNoIndexUsed = false;
/*  224 */   private boolean serverQueryWasSlow = false;
/*      */   
/*      */ 
/*  227 */   private boolean use41Extensions = false;
/*  228 */   private boolean useCompression = false;
/*  229 */   private boolean useNewLargePackets = false;
/*  230 */   private boolean useNewUpdateCounts = false;
/*  231 */   private byte packetSequence = 0;
/*  232 */   private byte compressedPacketSequence = 0;
/*  233 */   private byte readPacketSequence = -1;
/*  234 */   private boolean checkPacketSequence = false;
/*  235 */   private byte protocolVersion = 0;
/*  236 */   private int maxAllowedPacket = 1048576;
/*  237 */   protected int maxThreeBytes = 16581375;
/*  238 */   protected int port = 3306;
/*      */   protected int serverCapabilities;
/*  240 */   private int serverMajorVersion = 0;
/*  241 */   private int serverMinorVersion = 0;
/*  242 */   private int oldServerStatus = 0;
/*  243 */   private int serverStatus = 0;
/*  244 */   private int serverSubMinorVersion = 0;
/*  245 */   private int warningCount = 0;
/*  246 */   protected long clientParam = 0L;
/*  247 */   protected long lastPacketSentTimeMs = 0L;
/*  248 */   protected long lastPacketReceivedTimeMs = 0L;
/*  249 */   private boolean traceProtocol = false;
/*  250 */   private boolean enablePacketDebug = false;
/*      */   private boolean useConnectWithDb;
/*      */   private boolean needToGrabQueryFromPacket;
/*      */   private boolean autoGenerateTestcaseScript;
/*      */   private long threadId;
/*      */   private boolean useNanosForElapsedTime;
/*      */   private long slowQueryThreshold;
/*      */   private String queryTimingUnits;
/*  258 */   private boolean useDirectRowUnpack = true;
/*      */   private int useBufferRowSizeThreshold;
/*  260 */   private int commandCount = 0;
/*      */   private List<StatementInterceptorV2> statementInterceptors;
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*  263 */   private int authPluginDataLength = 0;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MysqlIO(String host, int port, Properties props, String socketFactoryClassName, MySQLConnection conn, int socketTimeout, int useBufferRowSizeThreshold)
/*      */     throws IOException, SQLException
/*      */   {
/*  282 */     this.connection = conn;
/*      */     
/*  284 */     if (this.connection.getEnablePacketDebug()) {
/*  285 */       this.packetDebugRingBuffer = new LinkedList();
/*      */     }
/*  287 */     this.traceProtocol = this.connection.getTraceProtocol();
/*      */     
/*      */ 
/*  290 */     this.useAutoSlowLog = this.connection.getAutoSlowLog();
/*      */     
/*  292 */     this.useBufferRowSizeThreshold = useBufferRowSizeThreshold;
/*  293 */     this.useDirectRowUnpack = this.connection.getUseDirectRowUnpack();
/*      */     
/*  295 */     this.logSlowQueries = this.connection.getLogSlowQueries();
/*      */     
/*  297 */     this.reusablePacket = new Buffer(1024);
/*  298 */     this.sendPacket = new Buffer(1024);
/*      */     
/*  300 */     this.port = port;
/*  301 */     this.host = host;
/*      */     
/*  303 */     this.socketFactoryClassName = socketFactoryClassName;
/*  304 */     this.socketFactory = createSocketFactory();
/*  305 */     this.exceptionInterceptor = this.connection.getExceptionInterceptor();
/*      */     try
/*      */     {
/*  308 */       this.mysqlConnection = this.socketFactory.connect(this.host, this.port, props);
/*      */       
/*      */ 
/*      */ 
/*  312 */       if (socketTimeout != 0) {
/*      */         try {
/*  314 */           this.mysqlConnection.setSoTimeout(socketTimeout);
/*      */         }
/*      */         catch (Exception ex) {}
/*      */       }
/*      */       
/*      */ 
/*  320 */       this.mysqlConnection = this.socketFactory.beforeHandshake();
/*      */       
/*  322 */       if (this.connection.getUseReadAheadInput()) {
/*  323 */         this.mysqlInput = new ReadAheadInputStream(this.mysqlConnection.getInputStream(), 16384, this.connection.getTraceProtocol(), this.connection.getLog());
/*      */ 
/*      */       }
/*  326 */       else if (this.connection.useUnbufferedInput()) {
/*  327 */         this.mysqlInput = this.mysqlConnection.getInputStream();
/*      */       } else {
/*  329 */         this.mysqlInput = new BufferedInputStream(this.mysqlConnection.getInputStream(), 16384);
/*      */       }
/*      */       
/*      */ 
/*  333 */       this.mysqlOutput = new BufferedOutputStream(this.mysqlConnection.getOutputStream(), 16384);
/*      */       
/*      */ 
/*      */ 
/*  337 */       this.isInteractiveClient = this.connection.getInteractiveClient();
/*  338 */       this.profileSql = this.connection.getProfileSql();
/*  339 */       this.autoGenerateTestcaseScript = this.connection.getAutoGenerateTestcaseScript();
/*      */       
/*  341 */       this.needToGrabQueryFromPacket = ((this.profileSql) || (this.logSlowQueries) || (this.autoGenerateTestcaseScript));
/*      */       
/*      */ 
/*      */ 
/*  345 */       if ((this.connection.getUseNanosForElapsedTime()) && (Util.nanoTimeAvailable()))
/*      */       {
/*  347 */         this.useNanosForElapsedTime = true;
/*      */         
/*  349 */         this.queryTimingUnits = Messages.getString("Nanoseconds");
/*      */       } else {
/*  351 */         this.queryTimingUnits = Messages.getString("Milliseconds");
/*      */       }
/*      */       
/*  354 */       if (this.connection.getLogSlowQueries()) {
/*  355 */         calculateSlowQueryThreshold();
/*      */       }
/*      */     } catch (IOException ioEx) {
/*  358 */       throw SQLError.createCommunicationsException(this.connection, 0L, 0L, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasLongColumnInfo()
/*      */   {
/*  368 */     return this.hasLongColumnInfo;
/*      */   }
/*      */   
/*      */   protected boolean isDataAvailable() throws SQLException {
/*      */     try {
/*  373 */       return this.mysqlInput.available() > 0;
/*      */     } catch (IOException ioEx) {
/*  375 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected long getLastPacketSentTimeMs()
/*      */   {
/*  386 */     return this.lastPacketSentTimeMs;
/*      */   }
/*      */   
/*      */   protected long getLastPacketReceivedTimeMs() {
/*  390 */     return this.lastPacketReceivedTimeMs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ResultSetImpl getResultSet(StatementImpl callingStatement, long columnCount, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, boolean isBinaryEncoded, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/*  420 */     Field[] fields = null;
/*      */     
/*      */ 
/*      */ 
/*  424 */     if (metadataFromCache == null) {
/*  425 */       fields = new Field[(int)columnCount];
/*      */       
/*  427 */       for (int i = 0; i < columnCount; i++) {
/*  428 */         Buffer fieldPacket = null;
/*      */         
/*  430 */         fieldPacket = readPacket();
/*  431 */         fields[i] = unpackField(fieldPacket, false);
/*      */       }
/*      */     } else {
/*  434 */       for (int i = 0; i < columnCount; i++) {
/*  435 */         skipPacket();
/*      */       }
/*      */     }
/*      */     
/*  439 */     Buffer packet = reuseAndReadPacket(this.reusablePacket);
/*      */     
/*  441 */     readServerStatusForResultSets(packet);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  447 */     if ((this.connection.versionMeetsMinimum(5, 0, 2)) && (this.connection.getUseCursorFetch()) && (isBinaryEncoded) && (callingStatement != null) && (callingStatement.getFetchSize() != 0) && (callingStatement.getResultSetType() == 1003))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  453 */       ServerPreparedStatement prepStmt = (ServerPreparedStatement)callingStatement;
/*      */       
/*  455 */       boolean usingCursor = true;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  463 */       if (this.connection.versionMeetsMinimum(5, 0, 5)) {
/*  464 */         usingCursor = (this.serverStatus & 0x40) != 0;
/*      */       }
/*      */       
/*      */ 
/*  468 */       if (usingCursor) {
/*  469 */         RowData rows = new RowDataCursor(this, prepStmt, fields);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  474 */         ResultSetImpl rs = buildResultSetWithRows(callingStatement, catalog, fields, rows, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  480 */         if (usingCursor) {
/*  481 */           rs.setFetchSize(callingStatement.getFetchSize());
/*      */         }
/*      */         
/*  484 */         return rs;
/*      */       }
/*      */     }
/*      */     
/*  488 */     RowData rowData = null;
/*      */     
/*  490 */     if (!streamResults) {
/*  491 */       rowData = readSingleRowSet(columnCount, maxRows, resultSetConcurrency, isBinaryEncoded, metadataFromCache == null ? fields : metadataFromCache);
/*      */     }
/*      */     else
/*      */     {
/*  495 */       rowData = new RowDataDynamic(this, (int)columnCount, metadataFromCache == null ? fields : metadataFromCache, isBinaryEncoded);
/*      */       
/*      */ 
/*  498 */       this.streamingData = rowData;
/*      */     }
/*      */     
/*  501 */     ResultSetImpl rs = buildResultSetWithRows(callingStatement, catalog, metadataFromCache == null ? fields : metadataFromCache, rowData, resultSetType, resultSetConcurrency, isBinaryEncoded);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  507 */     return rs;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected NetworkResources getNetworkResources()
/*      */   {
/*  515 */     return new NetworkResources(this.mysqlConnection, this.mysqlInput, this.mysqlOutput);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void forceClose()
/*      */   {
/*      */     try
/*      */     {
/*  523 */       getNetworkResources().forceClose();
/*      */     } finally {
/*  525 */       this.mysqlConnection = null;
/*  526 */       this.mysqlInput = null;
/*  527 */       this.mysqlOutput = null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void skipPacket()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  540 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */       
/*      */ 
/*  543 */       if (lengthRead < 4) {
/*  544 */         forceClose();
/*  545 */         throw new IOException(Messages.getString("MysqlIO.1"));
/*      */       }
/*      */       
/*  548 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       
/*      */ 
/*      */ 
/*  552 */       if (this.traceProtocol) {
/*  553 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  555 */         traceMessageBuf.append(Messages.getString("MysqlIO.2"));
/*  556 */         traceMessageBuf.append(packetLength);
/*  557 */         traceMessageBuf.append(Messages.getString("MysqlIO.3"));
/*  558 */         traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
/*      */         
/*      */ 
/*  561 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       }
/*      */       
/*  564 */       byte multiPacketSeq = this.packetHeaderBuf[3];
/*      */       
/*  566 */       if (!this.packetSequenceReset) {
/*  567 */         if ((this.enablePacketDebug) && (this.checkPacketSequence)) {
/*  568 */           checkPacketSequencing(multiPacketSeq);
/*      */         }
/*      */       } else {
/*  571 */         this.packetSequenceReset = false;
/*      */       }
/*      */       
/*  574 */       this.readPacketSequence = multiPacketSeq;
/*      */       
/*  576 */       skipFully(this.mysqlInput, packetLength);
/*      */     } catch (IOException ioEx) {
/*  578 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */     catch (OutOfMemoryError oom) {
/*      */       try {
/*  582 */         this.connection.realClose(false, false, true, oom);
/*      */       }
/*      */       catch (Exception ex) {}
/*  585 */       throw oom;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Buffer readPacket()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*  600 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */       
/*      */ 
/*  603 */       if (lengthRead < 4) {
/*  604 */         forceClose();
/*  605 */         throw new IOException(Messages.getString("MysqlIO.1"));
/*      */       }
/*      */       
/*  608 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       
/*      */ 
/*      */ 
/*  612 */       if (packetLength > this.maxAllowedPacket) {
/*  613 */         throw new PacketTooBigException(packetLength, this.maxAllowedPacket);
/*      */       }
/*      */       
/*  616 */       if (this.traceProtocol) {
/*  617 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  619 */         traceMessageBuf.append(Messages.getString("MysqlIO.2"));
/*  620 */         traceMessageBuf.append(packetLength);
/*  621 */         traceMessageBuf.append(Messages.getString("MysqlIO.3"));
/*  622 */         traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
/*      */         
/*      */ 
/*  625 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       }
/*      */       
/*  628 */       byte multiPacketSeq = this.packetHeaderBuf[3];
/*      */       
/*  630 */       if (!this.packetSequenceReset) {
/*  631 */         if ((this.enablePacketDebug) && (this.checkPacketSequence)) {
/*  632 */           checkPacketSequencing(multiPacketSeq);
/*      */         }
/*      */       } else {
/*  635 */         this.packetSequenceReset = false;
/*      */       }
/*      */       
/*  638 */       this.readPacketSequence = multiPacketSeq;
/*      */       
/*      */ 
/*  641 */       byte[] buffer = new byte[packetLength + 1];
/*  642 */       int numBytesRead = readFully(this.mysqlInput, buffer, 0, packetLength);
/*      */       
/*      */ 
/*  645 */       if (numBytesRead != packetLength) {
/*  646 */         throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
/*      */       }
/*      */       
/*      */ 
/*  650 */       buffer[packetLength] = 0;
/*      */       
/*  652 */       Buffer packet = new Buffer(buffer);
/*  653 */       packet.setBufLength(packetLength + 1);
/*      */       
/*  655 */       if (this.traceProtocol) {
/*  656 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/*  658 */         traceMessageBuf.append(Messages.getString("MysqlIO.4"));
/*  659 */         traceMessageBuf.append(getPacketDumpToLog(packet, packetLength));
/*      */         
/*      */ 
/*  662 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       }
/*      */       
/*  665 */       if (this.enablePacketDebug) {
/*  666 */         enqueuePacketForDebugging(false, false, 0, this.packetHeaderBuf, packet);
/*      */       }
/*      */       
/*      */ 
/*  670 */       if (this.connection.getMaintainTimeStats()) {
/*  671 */         this.lastPacketReceivedTimeMs = System.currentTimeMillis();
/*      */       }
/*      */       
/*  674 */       return packet;
/*      */     } catch (IOException ioEx) {
/*  676 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */     catch (OutOfMemoryError oom) {
/*      */       try {
/*  680 */         this.connection.realClose(false, false, true, oom);
/*      */       }
/*      */       catch (Exception ex) {}
/*  683 */       throw oom;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Field unpackField(Buffer packet, boolean extractDefaultValues)
/*      */     throws SQLException
/*      */   {
/*  700 */     if (this.use41Extensions)
/*      */     {
/*      */ 
/*  703 */       if (this.has41NewNewProt)
/*      */       {
/*  705 */         int catalogNameStart = packet.getPosition() + 1;
/*  706 */         int catalogNameLength = packet.fastSkipLenString();
/*  707 */         catalogNameStart = adjustStartForFieldLength(catalogNameStart, catalogNameLength);
/*      */       }
/*      */       
/*  710 */       int databaseNameStart = packet.getPosition() + 1;
/*  711 */       int databaseNameLength = packet.fastSkipLenString();
/*  712 */       databaseNameStart = adjustStartForFieldLength(databaseNameStart, databaseNameLength);
/*      */       
/*  714 */       int tableNameStart = packet.getPosition() + 1;
/*  715 */       int tableNameLength = packet.fastSkipLenString();
/*  716 */       tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
/*      */       
/*      */ 
/*  719 */       int originalTableNameStart = packet.getPosition() + 1;
/*  720 */       int originalTableNameLength = packet.fastSkipLenString();
/*  721 */       originalTableNameStart = adjustStartForFieldLength(originalTableNameStart, originalTableNameLength);
/*      */       
/*      */ 
/*  724 */       int nameStart = packet.getPosition() + 1;
/*  725 */       int nameLength = packet.fastSkipLenString();
/*      */       
/*  727 */       nameStart = adjustStartForFieldLength(nameStart, nameLength);
/*      */       
/*      */ 
/*  730 */       int originalColumnNameStart = packet.getPosition() + 1;
/*  731 */       int originalColumnNameLength = packet.fastSkipLenString();
/*  732 */       originalColumnNameStart = adjustStartForFieldLength(originalColumnNameStart, originalColumnNameLength);
/*      */       
/*  734 */       packet.readByte();
/*      */       
/*  736 */       short charSetNumber = (short)packet.readInt();
/*      */       
/*  738 */       long colLength = 0L;
/*      */       
/*  740 */       if (this.has41NewNewProt) {
/*  741 */         colLength = packet.readLong();
/*      */       } else {
/*  743 */         colLength = packet.readLongInt();
/*      */       }
/*      */       
/*  746 */       int colType = packet.readByte() & 0xFF;
/*      */       
/*  748 */       short colFlag = 0;
/*      */       
/*  750 */       if (this.hasLongColumnInfo) {
/*  751 */         colFlag = (short)packet.readInt();
/*      */       } else {
/*  753 */         colFlag = (short)(packet.readByte() & 0xFF);
/*      */       }
/*      */       
/*  756 */       int colDecimals = packet.readByte() & 0xFF;
/*      */       
/*  758 */       int defaultValueStart = -1;
/*  759 */       int defaultValueLength = -1;
/*      */       
/*  761 */       if (extractDefaultValues) {
/*  762 */         defaultValueStart = packet.getPosition() + 1;
/*  763 */         defaultValueLength = packet.fastSkipLenString();
/*      */       }
/*      */       
/*  766 */       Field field = new Field(this.connection, packet.getByteBuffer(), databaseNameStart, databaseNameLength, tableNameStart, tableNameLength, originalTableNameStart, originalTableNameLength, nameStart, nameLength, originalColumnNameStart, originalColumnNameLength, colLength, colType, colFlag, colDecimals, defaultValueStart, defaultValueLength, charSetNumber);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  774 */       return field;
/*      */     }
/*      */     
/*  777 */     int tableNameStart = packet.getPosition() + 1;
/*  778 */     int tableNameLength = packet.fastSkipLenString();
/*  779 */     tableNameStart = adjustStartForFieldLength(tableNameStart, tableNameLength);
/*      */     
/*  781 */     int nameStart = packet.getPosition() + 1;
/*  782 */     int nameLength = packet.fastSkipLenString();
/*  783 */     nameStart = adjustStartForFieldLength(nameStart, nameLength);
/*      */     
/*  785 */     int colLength = packet.readnBytes();
/*  786 */     int colType = packet.readnBytes();
/*  787 */     packet.readByte();
/*      */     
/*  789 */     short colFlag = 0;
/*      */     
/*  791 */     if (this.hasLongColumnInfo) {
/*  792 */       colFlag = (short)packet.readInt();
/*      */     } else {
/*  794 */       colFlag = (short)(packet.readByte() & 0xFF);
/*      */     }
/*      */     
/*  797 */     int colDecimals = packet.readByte() & 0xFF;
/*      */     
/*  799 */     if (this.colDecimalNeedsBump) {
/*  800 */       colDecimals++;
/*      */     }
/*      */     
/*  803 */     Field field = new Field(this.connection, packet.getByteBuffer(), nameStart, nameLength, tableNameStart, tableNameLength, colLength, colType, colFlag, colDecimals);
/*      */     
/*      */ 
/*      */ 
/*  807 */     return field;
/*      */   }
/*      */   
/*      */   private int adjustStartForFieldLength(int nameStart, int nameLength) {
/*  811 */     if (nameLength < 251) {
/*  812 */       return nameStart;
/*      */     }
/*      */     
/*  815 */     if ((nameLength >= 251) && (nameLength < 65536)) {
/*  816 */       return nameStart + 2;
/*      */     }
/*      */     
/*  819 */     if ((nameLength >= 65536) && (nameLength < 16777216)) {
/*  820 */       return nameStart + 3;
/*      */     }
/*      */     
/*  823 */     return nameStart + 8;
/*      */   }
/*      */   
/*      */   protected boolean isSetNeededForAutoCommitMode(boolean autoCommitFlag) {
/*  827 */     if ((this.use41Extensions) && (this.connection.getElideSetAutoCommits())) {
/*  828 */       boolean autoCommitModeOnServer = (this.serverStatus & 0x2) != 0;
/*      */       
/*      */ 
/*  831 */       if ((!autoCommitFlag) && (versionMeetsMinimum(5, 0, 0)))
/*      */       {
/*      */ 
/*      */ 
/*  835 */         boolean inTransactionOnServer = (this.serverStatus & 0x1) != 0;
/*      */         
/*      */ 
/*  838 */         return !inTransactionOnServer;
/*      */       }
/*      */       
/*  841 */       return autoCommitModeOnServer != autoCommitFlag;
/*      */     }
/*      */     
/*  844 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean inTransactionOnServer() {
/*  848 */     return (this.serverStatus & 0x1) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void changeUser(String userName, String password, String database)
/*      */     throws SQLException
/*      */   {
/*  862 */     this.packetSequence = -1;
/*  863 */     this.compressedPacketSequence = -1;
/*      */     
/*  865 */     int passwordLength = 16;
/*  866 */     int userLength = userName != null ? userName.length() : 0;
/*  867 */     int databaseLength = database != null ? database.length() : 0;
/*      */     
/*  869 */     int packLength = (userLength + passwordLength + databaseLength) * 3 + 7 + 4 + 33;
/*      */     
/*  871 */     if ((this.serverCapabilities & 0x80000) != 0)
/*      */     {
/*  873 */       proceedHandshakeWithPluggableAuthentication(userName, password, database, null);
/*      */     }
/*  875 */     else if ((this.serverCapabilities & 0x8000) != 0) {
/*  876 */       Buffer changeUserPacket = new Buffer(packLength + 1);
/*  877 */       changeUserPacket.writeByte((byte)17);
/*      */       
/*  879 */       if (versionMeetsMinimum(4, 1, 1)) {
/*  880 */         secureAuth411(changeUserPacket, packLength, userName, password, database, false);
/*      */       }
/*      */       else {
/*  883 */         secureAuth(changeUserPacket, packLength, userName, password, database, false);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  888 */       Buffer packet = new Buffer(packLength);
/*  889 */       packet.writeByte((byte)17);
/*      */       
/*      */ 
/*  892 */       packet.writeString(userName);
/*      */       
/*  894 */       if (this.protocolVersion > 9) {
/*  895 */         packet.writeString(Util.newCrypt(password, this.seed));
/*      */       } else {
/*  897 */         packet.writeString(Util.oldCrypt(password, this.seed));
/*      */       }
/*      */       
/*  900 */       boolean localUseConnectWithDb = (this.useConnectWithDb) && (database != null) && (database.length() > 0);
/*      */       
/*      */ 
/*  903 */       if (localUseConnectWithDb) {
/*  904 */         packet.writeString(database);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  910 */       send(packet, packet.getPosition());
/*  911 */       checkErrorPacket();
/*      */       
/*  913 */       if (!localUseConnectWithDb) {
/*  914 */         changeDatabaseTo(database);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Buffer checkErrorPacket()
/*      */     throws SQLException
/*      */   {
/*  928 */     return checkErrorPacket(-1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void checkForCharsetMismatch()
/*      */   {
/*  935 */     if ((this.connection.getUseUnicode()) && (this.connection.getEncoding() != null))
/*      */     {
/*  937 */       String encodingToCheck = jvmPlatformCharset;
/*      */       
/*  939 */       if (encodingToCheck == null) {
/*  940 */         encodingToCheck = System.getProperty("file.encoding");
/*      */       }
/*      */       
/*  943 */       if (encodingToCheck == null) {
/*  944 */         this.platformDbCharsetMatches = false;
/*      */       } else {
/*  946 */         this.platformDbCharsetMatches = encodingToCheck.equals(this.connection.getEncoding());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void clearInputStream() throws SQLException
/*      */   {
/*      */     try {
/*  954 */       int len = this.mysqlInput.available();
/*      */       
/*  956 */       while (len > 0) {
/*  957 */         this.mysqlInput.skip(len);
/*  958 */         len = this.mysqlInput.available();
/*      */       }
/*      */     } catch (IOException ioEx) {
/*  961 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   protected void resetReadPacketSequence()
/*      */   {
/*  967 */     this.readPacketSequence = 0;
/*      */   }
/*      */   
/*      */   protected void dumpPacketRingBuffer() throws SQLException {
/*  971 */     if ((this.packetDebugRingBuffer != null) && (this.connection.getEnablePacketDebug()))
/*      */     {
/*  973 */       StringBuffer dumpBuffer = new StringBuffer();
/*      */       
/*  975 */       dumpBuffer.append("Last " + this.packetDebugRingBuffer.size() + " packets received from server, from oldest->newest:\n");
/*      */       
/*  977 */       dumpBuffer.append("\n");
/*      */       
/*  979 */       Iterator<StringBuffer> ringBufIter = this.packetDebugRingBuffer.iterator();
/*  980 */       while (ringBufIter.hasNext()) {
/*  981 */         dumpBuffer.append((StringBuffer)ringBufIter.next());
/*  982 */         dumpBuffer.append("\n");
/*      */       }
/*      */       
/*  985 */       this.connection.getLog().logTrace(dumpBuffer.toString());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void explainSlowQuery(byte[] querySQL, String truncatedQuery)
/*      */     throws SQLException
/*      */   {
/*  999 */     if ((StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, "SELECT")) || ((versionMeetsMinimum(5, 6, 3)) && (StringUtils.startsWithIgnoreCaseAndWs(truncatedQuery, EXPLAINABLE_STATEMENT_EXTENSION) != -1)))
/*      */     {
/*      */ 
/*      */ 
/* 1003 */       PreparedStatement stmt = null;
/* 1004 */       ResultSet rs = null;
/*      */       try
/*      */       {
/* 1007 */         stmt = (PreparedStatement)this.connection.clientPrepareStatement("EXPLAIN ?");
/* 1008 */         stmt.setBytesNoEscapeNoQuotes(1, querySQL);
/* 1009 */         rs = stmt.executeQuery();
/*      */         
/* 1011 */         StringBuffer explainResults = new StringBuffer(Messages.getString("MysqlIO.8") + truncatedQuery + Messages.getString("MysqlIO.9"));
/*      */         
/*      */ 
/*      */ 
/* 1015 */         ResultSetUtil.appendResultSetSlashGStyle(explainResults, rs);
/*      */         
/* 1017 */         this.connection.getLog().logWarn(explainResults.toString());
/*      */       }
/*      */       catch (SQLException sqlEx) {}finally {
/* 1020 */         if (rs != null) {
/* 1021 */           rs.close();
/*      */         }
/*      */         
/* 1024 */         if (stmt != null) {
/* 1025 */           stmt.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static int getMaxBuf() {
/* 1032 */     return maxBufferSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerMajorVersion()
/*      */   {
/* 1041 */     return this.serverMajorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerMinorVersion()
/*      */   {
/* 1050 */     return this.serverMinorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final int getServerSubMinorVersion()
/*      */   {
/* 1059 */     return this.serverSubMinorVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getServerVersion()
/*      */   {
/* 1068 */     return this.serverVersion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void doHandshake(String user, String password, String database)
/*      */     throws SQLException
/*      */   {
/* 1085 */     this.checkPacketSequence = false;
/* 1086 */     this.readPacketSequence = 0;
/*      */     
/* 1088 */     Buffer buf = readPacket();
/*      */     
/*      */ 
/* 1091 */     this.protocolVersion = buf.readByte();
/*      */     
/* 1093 */     if (this.protocolVersion == -1) {
/*      */       try {
/* 1095 */         this.mysqlConnection.close();
/*      */       }
/*      */       catch (Exception e) {}
/*      */       
/*      */ 
/* 1100 */       int errno = 2000;
/*      */       
/* 1102 */       errno = buf.readInt();
/*      */       
/* 1104 */       String serverErrorMessage = buf.readString("ASCII", getExceptionInterceptor());
/*      */       
/* 1106 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.10"));
/*      */       
/* 1108 */       errorBuf.append(serverErrorMessage);
/* 1109 */       errorBuf.append("\"");
/*      */       
/* 1111 */       String xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */       
/*      */ 
/* 1114 */       throw SQLError.createSQLException(SQLError.get(xOpen) + ", " + errorBuf.toString(), xOpen, errno, getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 1118 */     this.serverVersion = buf.readString("ASCII", getExceptionInterceptor());
/*      */     
/*      */ 
/* 1121 */     int point = this.serverVersion.indexOf('.');
/*      */     
/* 1123 */     if (point != -1) {
/*      */       try {
/* 1125 */         int n = Integer.parseInt(this.serverVersion.substring(0, point));
/* 1126 */         this.serverMajorVersion = n;
/*      */       }
/*      */       catch (NumberFormatException NFE1) {}
/*      */       
/*      */ 
/* 1131 */       String remaining = this.serverVersion.substring(point + 1, this.serverVersion.length());
/*      */       
/* 1133 */       point = remaining.indexOf('.');
/*      */       
/* 1135 */       if (point != -1) {
/*      */         try {
/* 1137 */           int n = Integer.parseInt(remaining.substring(0, point));
/* 1138 */           this.serverMinorVersion = n;
/*      */         }
/*      */         catch (NumberFormatException nfe) {}
/*      */         
/*      */ 
/* 1143 */         remaining = remaining.substring(point + 1, remaining.length());
/*      */         
/* 1145 */         int pos = 0;
/*      */         
/* 1147 */         while ((pos < remaining.length()) && 
/* 1148 */           (remaining.charAt(pos) >= '0') && (remaining.charAt(pos) <= '9'))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/* 1153 */           pos++;
/*      */         }
/*      */         try
/*      */         {
/* 1157 */           int n = Integer.parseInt(remaining.substring(0, pos));
/* 1158 */           this.serverSubMinorVersion = n;
/*      */         }
/*      */         catch (NumberFormatException nfe) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1165 */     if (versionMeetsMinimum(4, 0, 8)) {
/* 1166 */       this.maxThreeBytes = 16777215;
/* 1167 */       this.useNewLargePackets = true;
/*      */     } else {
/* 1169 */       this.maxThreeBytes = 16581375;
/* 1170 */       this.useNewLargePackets = false;
/*      */     }
/*      */     
/* 1173 */     this.colDecimalNeedsBump = versionMeetsMinimum(3, 23, 0);
/* 1174 */     this.colDecimalNeedsBump = (!versionMeetsMinimum(3, 23, 15));
/* 1175 */     this.useNewUpdateCounts = versionMeetsMinimum(3, 22, 5);
/*      */     
/*      */ 
/* 1178 */     this.threadId = buf.readLong();
/*      */     
/* 1180 */     if (this.protocolVersion > 9)
/*      */     {
/* 1182 */       this.seed = buf.readString("ASCII", getExceptionInterceptor(), 8);
/*      */       
/* 1184 */       buf.readByte();
/*      */     }
/*      */     else {
/* 1187 */       this.seed = buf.readString("ASCII", getExceptionInterceptor());
/*      */     }
/*      */     
/* 1190 */     this.serverCapabilities = 0;
/*      */     
/*      */ 
/* 1193 */     if (buf.getPosition() < buf.getBufLength()) {
/* 1194 */       this.serverCapabilities = buf.readInt();
/*      */     }
/*      */     
/* 1197 */     if ((versionMeetsMinimum(4, 1, 1)) || ((this.protocolVersion > 9) && ((this.serverCapabilities & 0x200) != 0)))
/*      */     {
/*      */ 
/*      */ 
/* 1201 */       this.serverCharsetIndex = (buf.readByte() & 0xFF);
/*      */       
/* 1203 */       this.serverStatus = buf.readInt();
/* 1204 */       checkTransactionState(0);
/*      */       
/*      */ 
/* 1207 */       this.serverCapabilities |= buf.readInt() << 16;
/*      */       
/* 1209 */       if ((this.serverCapabilities & 0x80000) != 0)
/*      */       {
/* 1211 */         this.authPluginDataLength = (buf.readByte() & 0xFF);
/*      */       }
/*      */       else {
/* 1214 */         buf.readByte();
/*      */       }
/*      */       
/* 1217 */       buf.setPosition(buf.getPosition() + 10);
/*      */       
/* 1219 */       if ((this.serverCapabilities & 0x8000) != 0) {
/*      */         StringBuffer newSeed;
/*      */         String seedPart2;
/*      */         StringBuffer newSeed;
/* 1223 */         if (this.authPluginDataLength > 0)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1230 */           String seedPart2 = buf.readString("ASCII", getExceptionInterceptor(), this.authPluginDataLength - 8);
/* 1231 */           newSeed = new StringBuffer(this.authPluginDataLength);
/*      */         } else {
/* 1233 */           seedPart2 = buf.readString("ASCII", getExceptionInterceptor());
/* 1234 */           newSeed = new StringBuffer(20);
/*      */         }
/* 1236 */         newSeed.append(this.seed);
/* 1237 */         newSeed.append(seedPart2);
/* 1238 */         this.seed = newSeed.toString();
/*      */       }
/*      */     }
/*      */     
/* 1242 */     if (((this.serverCapabilities & 0x20) != 0) && (this.connection.getUseCompression()))
/*      */     {
/* 1244 */       this.clientParam |= 0x20;
/*      */     }
/*      */     
/* 1247 */     this.useConnectWithDb = ((database != null) && (database.length() > 0) && (!this.connection.getCreateDatabaseIfNotExist()));
/*      */     
/*      */ 
/*      */ 
/* 1251 */     if (this.useConnectWithDb) {
/* 1252 */       this.clientParam |= 0x8;
/*      */     }
/*      */     
/* 1255 */     if (((this.serverCapabilities & 0x800) == 0) && (this.connection.getUseSSL()))
/*      */     {
/* 1257 */       if (this.connection.getRequireSSL()) {
/* 1258 */         this.connection.close();
/* 1259 */         forceClose();
/* 1260 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.15"), "08001", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 1264 */       this.connection.setUseSSL(false);
/*      */     }
/*      */     
/* 1267 */     if ((this.serverCapabilities & 0x4) != 0)
/*      */     {
/* 1269 */       this.clientParam |= 0x4;
/* 1270 */       this.hasLongColumnInfo = true;
/*      */     }
/*      */     
/*      */ 
/* 1274 */     if (!this.connection.getUseAffectedRows()) {
/* 1275 */       this.clientParam |= 0x2;
/*      */     }
/*      */     
/* 1278 */     if (this.connection.getAllowLoadLocalInfile()) {
/* 1279 */       this.clientParam |= 0x80;
/*      */     }
/*      */     
/* 1282 */     if (this.isInteractiveClient) {
/* 1283 */       this.clientParam |= 0x400;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1289 */     if ((this.serverCapabilities & 0x80000) != 0) {
/* 1290 */       proceedHandshakeWithPluggableAuthentication(user, password, database, buf);
/* 1291 */       return;
/*      */     }
/*      */     
/*      */ 
/* 1295 */     if (this.protocolVersion > 9) {
/* 1296 */       this.clientParam |= 1L;
/*      */     } else {
/* 1298 */       this.clientParam &= 0xFFFFFFFFFFFFFFFE;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1304 */     if ((versionMeetsMinimum(4, 1, 0)) || ((this.protocolVersion > 9) && ((this.serverCapabilities & 0x4000) != 0))) {
/* 1305 */       if ((versionMeetsMinimum(4, 1, 1)) || ((this.protocolVersion > 9) && ((this.serverCapabilities & 0x200) != 0))) {
/* 1306 */         this.clientParam |= 0x200;
/* 1307 */         this.has41NewNewProt = true;
/*      */         
/*      */ 
/* 1310 */         this.clientParam |= 0x2000;
/*      */         
/*      */ 
/* 1313 */         this.clientParam |= 0x20000;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1318 */         if (this.connection.getAllowMultiQueries()) {
/* 1319 */           this.clientParam |= 0x10000;
/*      */         }
/*      */       } else {
/* 1322 */         this.clientParam |= 0x4000;
/* 1323 */         this.has41NewNewProt = false;
/*      */       }
/*      */       
/* 1326 */       this.use41Extensions = true;
/*      */     }
/*      */     
/* 1329 */     int passwordLength = 16;
/* 1330 */     int userLength = user != null ? user.length() : 0;
/* 1331 */     int databaseLength = database != null ? database.length() : 0;
/*      */     
/* 1333 */     int packLength = (userLength + passwordLength + databaseLength) * 3 + 7 + 4 + 33;
/*      */     
/* 1335 */     Buffer packet = null;
/*      */     
/* 1337 */     if (!this.connection.getUseSSL()) {
/* 1338 */       if ((this.serverCapabilities & 0x8000) != 0) {
/* 1339 */         this.clientParam |= 0x8000;
/*      */         
/* 1341 */         if ((versionMeetsMinimum(4, 1, 1)) || ((this.protocolVersion > 9) && ((this.serverCapabilities & 0x200) != 0))) {
/* 1342 */           secureAuth411(null, packLength, user, password, database, true);
/*      */         }
/*      */         else {
/* 1345 */           secureAuth(null, packLength, user, password, database, true);
/*      */         }
/*      */       }
/*      */       else {
/* 1349 */         packet = new Buffer(packLength);
/*      */         
/* 1351 */         if ((this.clientParam & 0x4000) != 0L) {
/* 1352 */           if ((versionMeetsMinimum(4, 1, 1)) || ((this.protocolVersion > 9) && ((this.serverCapabilities & 0x200) != 0))) {
/* 1353 */             packet.writeLong(this.clientParam);
/* 1354 */             packet.writeLong(this.maxThreeBytes);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1359 */             packet.writeByte((byte)8);
/*      */             
/*      */ 
/* 1362 */             packet.writeBytesNoNull(new byte[23]);
/*      */           } else {
/* 1364 */             packet.writeLong(this.clientParam);
/* 1365 */             packet.writeLong(this.maxThreeBytes);
/*      */           }
/*      */         } else {
/* 1368 */           packet.writeInt((int)this.clientParam);
/* 1369 */           packet.writeLongInt(this.maxThreeBytes);
/*      */         }
/*      */         
/*      */ 
/* 1373 */         packet.writeString(user, "Cp1252", this.connection);
/*      */         
/* 1375 */         if (this.protocolVersion > 9) {
/* 1376 */           packet.writeString(Util.newCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         } else {
/* 1378 */           packet.writeString(Util.oldCrypt(password, this.seed), "Cp1252", this.connection);
/*      */         }
/*      */         
/* 1381 */         if (this.useConnectWithDb) {
/* 1382 */           packet.writeString(database, "Cp1252", this.connection);
/*      */         }
/*      */         
/* 1385 */         send(packet, packet.getPosition());
/*      */       }
/*      */     } else {
/* 1388 */       negotiateSSLConnection(user, password, database, packLength);
/*      */       
/* 1390 */       if ((this.serverCapabilities & 0x8000) != 0) {
/* 1391 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 1392 */           secureAuth411(null, packLength, user, password, database, true);
/*      */         } else {
/* 1394 */           secureAuth411(null, packLength, user, password, database, true);
/*      */         }
/*      */       }
/*      */       else {
/* 1398 */         packet = new Buffer(packLength);
/*      */         
/* 1400 */         if (this.use41Extensions) {
/* 1401 */           packet.writeLong(this.clientParam);
/* 1402 */           packet.writeLong(this.maxThreeBytes);
/*      */         } else {
/* 1404 */           packet.writeInt((int)this.clientParam);
/* 1405 */           packet.writeLongInt(this.maxThreeBytes);
/*      */         }
/*      */         
/*      */ 
/* 1409 */         packet.writeString(user);
/*      */         
/* 1411 */         if (this.protocolVersion > 9) {
/* 1412 */           packet.writeString(Util.newCrypt(password, this.seed));
/*      */         } else {
/* 1414 */           packet.writeString(Util.oldCrypt(password, this.seed));
/*      */         }
/*      */         
/* 1417 */         if (((this.serverCapabilities & 0x8) != 0) && (database != null) && (database.length() > 0))
/*      */         {
/* 1419 */           packet.writeString(database);
/*      */         }
/*      */         
/* 1422 */         send(packet, packet.getPosition());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1430 */     if ((!versionMeetsMinimum(4, 1, 1)) && (this.protocolVersion > 9) && ((this.serverCapabilities & 0x200) != 0)) {
/* 1431 */       checkErrorPacket();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1437 */     if (((this.serverCapabilities & 0x20) != 0) && (this.connection.getUseCompression()))
/*      */     {
/*      */ 
/*      */ 
/* 1441 */       this.deflater = new Deflater();
/* 1442 */       this.useCompression = true;
/* 1443 */       this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
/*      */     }
/*      */     
/*      */ 
/* 1447 */     if (!this.useConnectWithDb) {
/* 1448 */       changeDatabaseTo(database);
/*      */     }
/*      */     try
/*      */     {
/* 1452 */       this.mysqlConnection = this.socketFactory.afterHandshake();
/*      */     } catch (IOException ioEx) {
/* 1454 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1465 */   private Map<String, AuthenticationPlugin> authenticationPlugins = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/* 1470 */   private List<String> disabledAuthenticationPlugins = null;
/*      */   
/*      */ 
/*      */ 
/* 1474 */   private String defaultAuthenticationPlugin = null;
/*      */   
/*      */ 
/*      */ 
/* 1478 */   private String defaultAuthenticationPluginProtocolName = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void loadAuthenticationPlugins()
/*      */     throws SQLException
/*      */   {
/* 1499 */     this.defaultAuthenticationPlugin = this.connection.getDefaultAuthenticationPlugin();
/* 1500 */     if ((this.defaultAuthenticationPlugin == null) || ("".equals(this.defaultAuthenticationPlugin.trim()))) {
/* 1501 */       throw SQLError.createSQLException(Messages.getString("Connection.BadDefaultAuthenticationPlugin", new Object[] { this.defaultAuthenticationPlugin }), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1508 */     String disabledPlugins = this.connection.getDisabledAuthenticationPlugins();
/* 1509 */     if ((disabledPlugins != null) && (!"".equals(disabledPlugins))) {
/* 1510 */       this.disabledAuthenticationPlugins = new ArrayList();
/* 1511 */       List<String> pluginsToDisable = StringUtils.split(disabledPlugins, ",", true);
/* 1512 */       Iterator<String> iter = pluginsToDisable.iterator();
/* 1513 */       while (iter.hasNext()) {
/* 1514 */         this.disabledAuthenticationPlugins.add(iter.next());
/*      */       }
/*      */     }
/*      */     
/* 1518 */     this.authenticationPlugins = new HashMap();
/*      */     
/*      */ 
/* 1521 */     AuthenticationPlugin plugin = new MysqlOldPasswordPlugin();
/* 1522 */     plugin.init(this.connection, this.connection.getProperties());
/* 1523 */     boolean defaultIsFound = addAuthenticationPlugin(plugin);
/*      */     
/* 1525 */     plugin = new MysqlNativePasswordPlugin();
/* 1526 */     plugin.init(this.connection, this.connection.getProperties());
/* 1527 */     if (addAuthenticationPlugin(plugin)) { defaultIsFound = true;
/*      */     }
/* 1529 */     plugin = new MysqlClearPasswordPlugin();
/* 1530 */     plugin.init(this.connection, this.connection.getProperties());
/* 1531 */     if (addAuthenticationPlugin(plugin)) { defaultIsFound = true;
/*      */     }
/* 1533 */     plugin = new Sha256PasswordPlugin();
/* 1534 */     plugin.init(this.connection, this.connection.getProperties());
/* 1535 */     if (addAuthenticationPlugin(plugin)) { defaultIsFound = true;
/*      */     }
/*      */     
/* 1538 */     String authenticationPluginClasses = this.connection.getAuthenticationPlugins();
/* 1539 */     if ((authenticationPluginClasses != null) && (!"".equals(authenticationPluginClasses)))
/*      */     {
/* 1541 */       List<Extension> plugins = Util.loadExtensions(this.connection, this.connection.getProperties(), authenticationPluginClasses, "Connection.BadAuthenticationPlugin", getExceptionInterceptor());
/*      */       
/*      */ 
/*      */ 
/* 1545 */       for (Extension object : plugins) {
/* 1546 */         plugin = (AuthenticationPlugin)object;
/* 1547 */         if (addAuthenticationPlugin(plugin)) { defaultIsFound = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1552 */     if (!defaultIsFound) {
/* 1553 */       throw SQLError.createSQLException(Messages.getString("Connection.DefaultAuthenticationPluginIsNotListed", new Object[] { this.defaultAuthenticationPlugin }), getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean addAuthenticationPlugin(AuthenticationPlugin plugin)
/*      */     throws SQLException
/*      */   {
/* 1569 */     boolean isDefault = false;
/* 1570 */     String pluginClassName = plugin.getClass().getName();
/* 1571 */     String pluginProtocolName = plugin.getProtocolPluginName();
/* 1572 */     boolean disabledByClassName = (this.disabledAuthenticationPlugins != null) && (this.disabledAuthenticationPlugins.contains(pluginClassName));
/*      */     
/*      */ 
/* 1575 */     boolean disabledByMechanism = (this.disabledAuthenticationPlugins != null) && (this.disabledAuthenticationPlugins.contains(pluginProtocolName));
/*      */     
/*      */ 
/*      */ 
/* 1579 */     if ((disabledByClassName) || (disabledByMechanism))
/*      */     {
/* 1581 */       if (this.defaultAuthenticationPlugin.equals(pluginClassName)) {
/* 1582 */         throw SQLError.createSQLException(Messages.getString("Connection.BadDisabledAuthenticationPlugin", new Object[] { disabledByClassName ? pluginClassName : pluginProtocolName }), getExceptionInterceptor());
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 1588 */       this.authenticationPlugins.put(pluginProtocolName, plugin);
/* 1589 */       if (this.defaultAuthenticationPlugin.equals(pluginClassName)) {
/* 1590 */         this.defaultAuthenticationPluginProtocolName = pluginProtocolName;
/* 1591 */         isDefault = true;
/*      */       }
/*      */     }
/* 1594 */     return isDefault;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private AuthenticationPlugin getAuthenticationPlugin(String pluginName)
/*      */     throws SQLException
/*      */   {
/* 1614 */     AuthenticationPlugin plugin = (AuthenticationPlugin)this.authenticationPlugins.get(pluginName);
/*      */     
/* 1616 */     if ((plugin != null) && (!plugin.isReusable())) {
/*      */       try {
/* 1618 */         plugin = (AuthenticationPlugin)plugin.getClass().newInstance();
/* 1619 */         plugin.init(this.connection, this.connection.getProperties());
/*      */       } catch (Throwable t) {
/* 1621 */         SQLException sqlEx = SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[] { plugin.getClass().getName() }), getExceptionInterceptor());
/*      */         
/* 1623 */         sqlEx.initCause(t);
/* 1624 */         throw sqlEx;
/*      */       }
/*      */     }
/*      */     
/* 1628 */     return plugin;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkConfidentiality(AuthenticationPlugin plugin)
/*      */     throws SQLException
/*      */   {
/* 1637 */     if ((plugin.requiresConfidentiality()) && ((!this.connection.getUseSSL()) || (!this.connection.getRequireSSL()))) {
/* 1638 */       throw SQLError.createSQLException(Messages.getString("Connection.AuthenticationPluginRequiresSSL", new Object[] { plugin.getProtocolPluginName() }), getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void proceedHandshakeWithPluggableAuthentication(String user, String password, String database, Buffer challenge)
/*      */     throws SQLException
/*      */   {
/* 1664 */     if (this.authenticationPlugins == null) {
/* 1665 */       loadAuthenticationPlugins();
/*      */     }
/*      */     
/* 1668 */     int passwordLength = 16;
/* 1669 */     int userLength = user != null ? user.length() : 0;
/* 1670 */     int databaseLength = database != null ? database.length() : 0;
/*      */     
/* 1672 */     int packLength = (userLength + passwordLength + databaseLength) * 3 + 7 + 4 + 33;
/*      */     
/* 1674 */     AuthenticationPlugin plugin = null;
/* 1675 */     Buffer fromServer = null;
/* 1676 */     ArrayList<Buffer> toServer = new ArrayList();
/* 1677 */     Boolean done = null;
/* 1678 */     Buffer last_sent = null;
/*      */     
/* 1680 */     boolean old_raw_challenge = false;
/*      */     
/* 1682 */     int counter = 100;
/*      */     
/* 1684 */     while (0 < counter--)
/*      */     {
/* 1686 */       if (done == null)
/*      */       {
/* 1688 */         if (challenge != null)
/*      */         {
/*      */ 
/* 1691 */           this.clientParam |= 0xAA201;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1703 */           if (this.connection.getAllowMultiQueries()) {
/* 1704 */             this.clientParam |= 0x10000;
/*      */           }
/*      */           
/* 1707 */           if (((this.serverCapabilities & 0x400000) != 0) && (!this.connection.getDisconnectOnExpiredPasswords())) {
/* 1708 */             this.clientParam |= 0x400000;
/*      */           }
/* 1710 */           if (((this.serverCapabilities & 0x100000) != 0) && (!"none".equals(this.connection.getConnectionAttributes())))
/*      */           {
/* 1712 */             this.clientParam |= 0x100000;
/*      */           }
/* 1714 */           if ((this.serverCapabilities & 0x200000) != 0) {
/* 1715 */             this.clientParam |= 0x200000;
/*      */           }
/*      */           
/* 1718 */           this.has41NewNewProt = true;
/* 1719 */           this.use41Extensions = true;
/*      */           
/* 1721 */           if (this.connection.getUseSSL()) {
/* 1722 */             negotiateSSLConnection(user, password, database, packLength);
/*      */           }
/*      */           
/* 1725 */           String pluginName = null;
/*      */           
/* 1727 */           if ((this.serverCapabilities & 0x80000) != 0) {
/* 1728 */             if ((!versionMeetsMinimum(5, 5, 10)) || ((versionMeetsMinimum(5, 6, 0)) && (!versionMeetsMinimum(5, 6, 2)))) {
/* 1729 */               pluginName = challenge.readString("ASCII", getExceptionInterceptor(), this.authPluginDataLength);
/*      */             } else {
/* 1731 */               pluginName = challenge.readString("ASCII", getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */           
/* 1735 */           plugin = getAuthenticationPlugin(pluginName);
/*      */           
/* 1737 */           if (plugin == null) { plugin = getAuthenticationPlugin(this.defaultAuthenticationPluginProtocolName);
/*      */           }
/* 1739 */           checkConfidentiality(plugin);
/* 1740 */           fromServer = new Buffer(StringUtils.getBytes(this.seed));
/*      */         }
/*      */         else {
/* 1743 */           plugin = getAuthenticationPlugin(this.defaultAuthenticationPluginProtocolName);
/* 1744 */           checkConfidentiality(plugin);
/*      */         }
/*      */         
/*      */       }
/*      */       else
/*      */       {
/* 1750 */         challenge = checkErrorPacket();
/* 1751 */         old_raw_challenge = false;
/*      */         
/* 1753 */         if (challenge.isOKPacket())
/*      */         {
/* 1755 */           if (!done.booleanValue()) {
/* 1756 */             throw SQLError.createSQLException(Messages.getString("Connection.UnexpectedAuthenticationApproval", new Object[] { plugin.getProtocolPluginName() }), getExceptionInterceptor());
/*      */           }
/* 1758 */           plugin.destroy();
/* 1759 */           break;
/*      */         }
/* 1761 */         if (challenge.isAuthMethodSwitchRequestPacket())
/*      */         {
/* 1763 */           String pluginName = challenge.readString("ASCII", getExceptionInterceptor());
/*      */           
/*      */ 
/* 1766 */           if ((plugin != null) && (!plugin.getProtocolPluginName().equals(pluginName))) {
/* 1767 */             plugin.destroy();
/* 1768 */             plugin = getAuthenticationPlugin(pluginName);
/*      */             
/* 1770 */             if (plugin == null) {
/* 1771 */               throw SQLError.createSQLException(Messages.getString("Connection.BadAuthenticationPlugin", new Object[] { pluginName }), getExceptionInterceptor());
/*      */             }
/*      */           }
/*      */           
/* 1775 */           checkConfidentiality(plugin);
/* 1776 */           fromServer = new Buffer(StringUtils.getBytes(challenge.readString("ASCII", getExceptionInterceptor())));
/*      */ 
/*      */ 
/*      */         }
/* 1780 */         else if (versionMeetsMinimum(5, 5, 16)) {
/* 1781 */           fromServer = new Buffer(challenge.getBytes(challenge.getPosition(), challenge.getBufLength() - challenge.getPosition()));
/*      */         } else {
/* 1783 */           old_raw_challenge = true;
/* 1784 */           fromServer = new Buffer(challenge.getBytes(challenge.getPosition() - 1, challenge.getBufLength() - challenge.getPosition() + 1));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       try
/*      */       {
/* 1792 */         plugin.setAuthenticationParameters(user, password);
/* 1793 */         done = Boolean.valueOf(plugin.nextAuthenticationStep(fromServer, toServer));
/*      */       } catch (SQLException e) {
/* 1795 */         throw SQLError.createSQLException(e.getMessage(), e.getSQLState(), e, getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 1799 */       if (toServer.size() > 0) {
/* 1800 */         if (challenge == null)
/*      */         {
/*      */ 
/* 1803 */           String enc = this.connection.getEncoding();
/* 1804 */           int charsetIndex = 0;
/* 1805 */           if (enc != null) {
/* 1806 */             charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(CharsetMapping.getMysqlEncodingForJavaEncoding(enc, this.connection));
/*      */           } else {
/* 1808 */             enc = "utf-8";
/*      */           }
/* 1810 */           if (charsetIndex == 0) {
/* 1811 */             charsetIndex = 33;
/*      */           }
/*      */           
/*      */ 
/* 1815 */           last_sent = new Buffer(packLength + 1);
/* 1816 */           last_sent.writeByte((byte)17);
/*      */           
/*      */ 
/* 1819 */           last_sent.writeString(user, enc, this.connection);
/*      */           
/* 1821 */           last_sent.writeByte((byte)((Buffer)toServer.get(0)).getBufLength());
/* 1822 */           last_sent.writeBytesNoNull(((Buffer)toServer.get(0)).getByteBuffer(), 0, ((Buffer)toServer.get(0)).getBufLength());
/*      */           
/* 1824 */           if (this.useConnectWithDb) {
/* 1825 */             last_sent.writeString(database, enc, this.connection);
/*      */           }
/*      */           else {
/* 1828 */             last_sent.writeByte((byte)0);
/*      */           }
/*      */           
/*      */ 
/* 1832 */           last_sent.writeByte((byte)(charsetIndex % 256));
/* 1833 */           if (charsetIndex > 255) {
/* 1834 */             last_sent.writeByte((byte)(charsetIndex / 256));
/*      */           } else {
/* 1836 */             last_sent.writeByte((byte)0);
/*      */           }
/*      */           
/*      */ 
/* 1840 */           if ((this.serverCapabilities & 0x80000) != 0) {
/* 1841 */             last_sent.writeString(plugin.getProtocolPluginName(), enc, this.connection);
/*      */           }
/*      */           
/*      */ 
/* 1845 */           if ((this.clientParam & 0x100000) != 0L) {
/* 1846 */             sendConnectionAttributes(last_sent, enc, this.connection);
/* 1847 */             last_sent.writeByte((byte)0);
/*      */           }
/*      */           
/* 1850 */           send(last_sent, last_sent.getPosition());
/*      */         }
/* 1852 */         else if (challenge.isAuthMethodSwitchRequestPacket())
/*      */         {
/*      */ 
/* 1855 */           byte savePacketSequence = this.packetSequence++;
/* 1856 */           savePacketSequence = (byte)(savePacketSequence + 1);this.packetSequence = savePacketSequence;
/*      */           
/* 1858 */           last_sent = new Buffer(((Buffer)toServer.get(0)).getBufLength() + 4);
/* 1859 */           last_sent.writeBytesNoNull(((Buffer)toServer.get(0)).getByteBuffer(), 0, ((Buffer)toServer.get(0)).getBufLength());
/* 1860 */           send(last_sent, last_sent.getPosition());
/*      */         } else { byte savePacketSequence;
/* 1862 */           if ((challenge.isRawPacket()) || (old_raw_challenge))
/*      */           {
/* 1864 */             savePacketSequence = this.packetSequence++;
/*      */             
/* 1866 */             for (Buffer buffer : toServer) {
/* 1867 */               savePacketSequence = (byte)(savePacketSequence + 1);this.packetSequence = savePacketSequence;
/*      */               
/* 1869 */               last_sent = new Buffer(buffer.getBufLength() + 4);
/* 1870 */               last_sent.writeBytesNoNull(buffer.getByteBuffer(), 0, ((Buffer)toServer.get(0)).getBufLength());
/* 1871 */               send(last_sent, last_sent.getPosition());
/*      */             }
/*      */             
/*      */           }
/*      */           else
/*      */           {
/* 1877 */             last_sent = new Buffer(packLength);
/* 1878 */             last_sent.writeLong(this.clientParam);
/* 1879 */             last_sent.writeLong(this.maxThreeBytes);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/* 1884 */             last_sent.writeByte((byte)33);
/*      */             
/* 1886 */             last_sent.writeBytesNoNull(new byte[23]);
/*      */             
/*      */ 
/* 1889 */             last_sent.writeString(user, "utf-8", this.connection);
/*      */             
/* 1891 */             if ((this.serverCapabilities & 0x200000) != 0)
/*      */             {
/* 1893 */               last_sent.writeLenBytes(((Buffer)toServer.get(0)).getBytes(((Buffer)toServer.get(0)).getBufLength()));
/*      */             }
/*      */             else {
/* 1896 */               last_sent.writeByte((byte)((Buffer)toServer.get(0)).getBufLength());
/* 1897 */               last_sent.writeBytesNoNull(((Buffer)toServer.get(0)).getByteBuffer(), 0, ((Buffer)toServer.get(0)).getBufLength());
/*      */             }
/*      */             
/* 1900 */             if (this.useConnectWithDb) {
/* 1901 */               last_sent.writeString(database, "utf-8", this.connection);
/*      */             }
/*      */             else {
/* 1904 */               last_sent.writeByte((byte)0);
/*      */             }
/*      */             
/* 1907 */             if ((this.serverCapabilities & 0x80000) != 0) {
/* 1908 */               last_sent.writeString(plugin.getProtocolPluginName(), "utf-8", this.connection);
/*      */             }
/*      */             
/*      */ 
/* 1912 */             if ((this.clientParam & 0x100000) != 0L) {
/* 1913 */               sendConnectionAttributes(last_sent, "utf-8", this.connection);
/*      */             }
/*      */             
/* 1916 */             send(last_sent, last_sent.getPosition());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1923 */     if (counter == 0) {
/* 1924 */       throw SQLError.createSQLException(Messages.getString("CommunicationsException.TooManyAuthenticationPluginNegotiations"), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1930 */     if (((this.serverCapabilities & 0x20) != 0) && (this.connection.getUseCompression()))
/*      */     {
/*      */ 
/*      */ 
/* 1934 */       this.deflater = new Deflater();
/* 1935 */       this.useCompression = true;
/* 1936 */       this.mysqlInput = new CompressedInputStream(this.connection, this.mysqlInput);
/*      */     }
/*      */     
/* 1939 */     if (!this.useConnectWithDb) {
/* 1940 */       changeDatabaseTo(database);
/*      */     }
/*      */     try
/*      */     {
/* 1944 */       this.mysqlConnection = this.socketFactory.afterHandshake();
/*      */     } catch (IOException ioEx) {
/* 1946 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   private Properties getConnectionAttributesAsProperties(String atts) throws SQLException
/*      */   {
/* 1952 */     Properties props = new Properties();
/*      */     
/*      */ 
/* 1955 */     if (atts != null) {
/* 1956 */       String[] pairs = atts.split(",");
/* 1957 */       for (String pair : pairs) {
/* 1958 */         int keyEnd = pair.indexOf(":");
/* 1959 */         if ((keyEnd > 0) && (keyEnd + 1 < pair.length())) {
/* 1960 */           props.setProperty(pair.substring(0, keyEnd), pair.substring(keyEnd + 1));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1968 */     props.setProperty("_client_name", "MySQL Connector Java");
/* 1969 */     props.setProperty("_client_version", "5.1.29");
/* 1970 */     props.setProperty("_runtime_vendor", NonRegisteringDriver.RUNTIME_VENDOR);
/* 1971 */     props.setProperty("_runtime_version", NonRegisteringDriver.RUNTIME_VERSION);
/* 1972 */     props.setProperty("_client_license", "GPL");
/*      */     
/*      */ 
/* 1975 */     return props;
/*      */   }
/*      */   
/*      */   private void sendConnectionAttributes(Buffer buf, String enc, MySQLConnection conn) throws SQLException {
/* 1979 */     String atts = conn.getConnectionAttributes();
/*      */     
/* 1981 */     Buffer lb = new Buffer(100);
/*      */     Properties props;
/*      */     try {
/* 1984 */       props = getConnectionAttributesAsProperties(atts);
/*      */       
/* 1986 */       for (Object key : props.keySet()) {
/* 1987 */         lb.writeLenString((String)key, enc, conn.getServerCharacterEncoding(), null, conn.parserKnowsUnicode(), conn);
/* 1988 */         lb.writeLenString(props.getProperty((String)key), enc, conn.getServerCharacterEncoding(), null, conn.parserKnowsUnicode(), conn);
/*      */       }
/*      */     }
/*      */     catch (UnsupportedEncodingException e) {}
/*      */     
/*      */ 
/*      */ 
/* 1995 */     buf.writeByte((byte)(lb.getPosition() - 4));
/* 1996 */     buf.writeBytesNoNull(lb.getByteBuffer(), 4, lb.getBufLength() - 4);
/*      */   }
/*      */   
/*      */   private void changeDatabaseTo(String database)
/*      */     throws SQLException
/*      */   {
/* 2002 */     if ((database == null) || (database.length() == 0)) {
/* 2003 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 2007 */       sendCommand(2, database, null, false, null, 0);
/*      */     } catch (Exception ex) {
/* 2009 */       if (this.connection.getCreateDatabaseIfNotExist()) {
/* 2010 */         sendCommand(3, "CREATE DATABASE IF NOT EXISTS " + database, null, false, null, 0);
/*      */         
/*      */ 
/* 2013 */         sendCommand(2, database, null, false, null, 0);
/*      */       } else {
/* 2015 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final ResultSetRow nextRow(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacketForBufferRow, Buffer existingRowPacket)
/*      */     throws SQLException
/*      */   {
/* 2043 */     if ((this.useDirectRowUnpack) && (existingRowPacket == null) && (!isBinaryEncoded) && (!useBufferRowIfPossible) && (!useBufferRowExplicit))
/*      */     {
/*      */ 
/* 2046 */       return nextRowFast(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacketForBufferRow);
/*      */     }
/*      */     
/*      */ 
/* 2050 */     Buffer rowPacket = null;
/*      */     
/* 2052 */     if (existingRowPacket == null) {
/* 2053 */       rowPacket = checkErrorPacket();
/*      */       
/* 2055 */       if ((!useBufferRowExplicit) && (useBufferRowIfPossible) && 
/* 2056 */         (rowPacket.getBufLength() > this.useBufferRowSizeThreshold)) {
/* 2057 */         useBufferRowExplicit = true;
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 2063 */       rowPacket = existingRowPacket;
/* 2064 */       checkErrorPacket(existingRowPacket);
/*      */     }
/*      */     
/*      */ 
/* 2068 */     if (!isBinaryEncoded)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2073 */       rowPacket.setPosition(rowPacket.getPosition() - 1);
/*      */       
/* 2075 */       if (!rowPacket.isLastDataPacket()) {
/* 2076 */         if ((resultSetConcurrency == 1008) || ((!useBufferRowIfPossible) && (!useBufferRowExplicit)))
/*      */         {
/*      */ 
/* 2079 */           byte[][] rowData = new byte[columnCount][];
/*      */           
/* 2081 */           for (int i = 0; i < columnCount; i++) {
/* 2082 */             rowData[i] = rowPacket.readLenByteArray(0);
/*      */           }
/*      */           
/* 2085 */           return new ByteArrayRow(rowData, getExceptionInterceptor());
/*      */         }
/*      */         
/* 2088 */         if (!canReuseRowPacketForBufferRow) {
/* 2089 */           this.reusablePacket = new Buffer(rowPacket.getBufLength());
/*      */         }
/*      */         
/* 2092 */         return new BufferRow(rowPacket, fields, false, getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 2096 */       readServerStatusForResultSets(rowPacket);
/*      */       
/* 2098 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2105 */     if (!rowPacket.isLastDataPacket()) {
/* 2106 */       if ((resultSetConcurrency == 1008) || ((!useBufferRowIfPossible) && (!useBufferRowExplicit)))
/*      */       {
/* 2108 */         return unpackBinaryResultSetRow(fields, rowPacket, resultSetConcurrency);
/*      */       }
/*      */       
/*      */ 
/* 2112 */       if (!canReuseRowPacketForBufferRow) {
/* 2113 */         this.reusablePacket = new Buffer(rowPacket.getBufLength());
/*      */       }
/*      */       
/* 2116 */       return new BufferRow(rowPacket, fields, true, getExceptionInterceptor());
/*      */     }
/*      */     
/* 2119 */     rowPacket.setPosition(rowPacket.getPosition() - 1);
/* 2120 */     readServerStatusForResultSets(rowPacket);
/*      */     
/* 2122 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   final ResultSetRow nextRowFast(Field[] fields, int columnCount, boolean isBinaryEncoded, int resultSetConcurrency, boolean useBufferRowIfPossible, boolean useBufferRowExplicit, boolean canReuseRowPacket)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 2131 */       int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */       
/*      */ 
/* 2134 */       if (lengthRead < 4) {
/* 2135 */         forceClose();
/* 2136 */         throw new RuntimeException(Messages.getString("MysqlIO.43"));
/*      */       }
/*      */       
/* 2139 */       int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2144 */       if (packetLength == this.maxThreeBytes) {
/* 2145 */         reuseAndReadPacket(this.reusablePacket, packetLength);
/*      */         
/*      */ 
/* 2148 */         return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, useBufferRowIfPossible, useBufferRowExplicit, canReuseRowPacket, this.reusablePacket);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2155 */       if (packetLength > this.useBufferRowSizeThreshold) {
/* 2156 */         reuseAndReadPacket(this.reusablePacket, packetLength);
/*      */         
/*      */ 
/* 2159 */         return nextRow(fields, columnCount, isBinaryEncoded, resultSetConcurrency, true, true, false, this.reusablePacket);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 2164 */       int remaining = packetLength;
/*      */       
/* 2166 */       boolean firstTime = true;
/*      */       
/* 2168 */       byte[][] rowData = (byte[][])null;
/*      */       
/* 2170 */       for (int i = 0; i < columnCount; i++)
/*      */       {
/* 2172 */         int sw = this.mysqlInput.read() & 0xFF;
/* 2173 */         remaining--;
/*      */         
/* 2175 */         if (firstTime) {
/* 2176 */           if (sw == 255)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2181 */             Buffer errorPacket = new Buffer(packetLength + 4);
/* 2182 */             errorPacket.setPosition(0);
/* 2183 */             errorPacket.writeByte(this.packetHeaderBuf[0]);
/* 2184 */             errorPacket.writeByte(this.packetHeaderBuf[1]);
/* 2185 */             errorPacket.writeByte(this.packetHeaderBuf[2]);
/* 2186 */             errorPacket.writeByte((byte)1);
/* 2187 */             errorPacket.writeByte((byte)sw);
/* 2188 */             readFully(this.mysqlInput, errorPacket.getByteBuffer(), 5, packetLength - 1);
/* 2189 */             errorPacket.setPosition(4);
/* 2190 */             checkErrorPacket(errorPacket);
/*      */           }
/*      */           
/* 2193 */           if ((sw == 254) && (packetLength < 9)) {
/* 2194 */             if (this.use41Extensions) {
/* 2195 */               this.warningCount = (this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8);
/*      */               
/* 2197 */               remaining -= 2;
/*      */               
/* 2199 */               if (this.warningCount > 0) {
/* 2200 */                 this.hadWarnings = true;
/*      */               }
/*      */               
/*      */ 
/*      */ 
/*      */ 
/* 2206 */               this.oldServerStatus = this.serverStatus;
/*      */               
/* 2208 */               this.serverStatus = (this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8);
/*      */               
/* 2210 */               checkTransactionState(this.oldServerStatus);
/*      */               
/* 2212 */               remaining -= 2;
/*      */               
/* 2214 */               if (remaining > 0) {
/* 2215 */                 skipFully(this.mysqlInput, remaining);
/*      */               }
/*      */             }
/*      */             
/* 2219 */             return null;
/*      */           }
/*      */           
/* 2222 */           rowData = new byte[columnCount][];
/*      */           
/* 2224 */           firstTime = false;
/*      */         }
/*      */         
/* 2227 */         int len = 0;
/*      */         
/* 2229 */         switch (sw) {
/*      */         case 251: 
/* 2231 */           len = -1;
/* 2232 */           break;
/*      */         
/*      */         case 252: 
/* 2235 */           len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8;
/*      */           
/* 2237 */           remaining -= 2;
/* 2238 */           break;
/*      */         
/*      */         case 253: 
/* 2241 */           len = this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8 | (this.mysqlInput.read() & 0xFF) << 16;
/*      */           
/*      */ 
/*      */ 
/* 2245 */           remaining -= 3;
/* 2246 */           break;
/*      */         
/*      */         case 254: 
/* 2249 */           len = (int)(this.mysqlInput.read() & 0xFF | (this.mysqlInput.read() & 0xFF) << 8 | (this.mysqlInput.read() & 0xFF) << 16 | (this.mysqlInput.read() & 0xFF) << 24 | (this.mysqlInput.read() & 0xFF) << 32 | (this.mysqlInput.read() & 0xFF) << 40 | (this.mysqlInput.read() & 0xFF) << 48 | (this.mysqlInput.read() & 0xFF) << 56);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2257 */           remaining -= 8;
/* 2258 */           break;
/*      */         
/*      */         default: 
/* 2261 */           len = sw;
/*      */         }
/*      */         
/* 2264 */         if (len == -1) {
/* 2265 */           rowData[i] = null;
/* 2266 */         } else if (len == 0) {
/* 2267 */           rowData[i] = Constants.EMPTY_BYTE_ARRAY;
/*      */         } else {
/* 2269 */           rowData[i] = new byte[len];
/*      */           
/* 2271 */           int bytesRead = readFully(this.mysqlInput, rowData[i], 0, len);
/*      */           
/*      */ 
/* 2274 */           if (bytesRead != len) {
/* 2275 */             throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException(Messages.getString("MysqlIO.43")), getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 2280 */           remaining -= bytesRead;
/*      */         }
/*      */       }
/*      */       
/* 2284 */       if (remaining > 0) {
/* 2285 */         skipFully(this.mysqlInput, remaining);
/*      */       }
/*      */       
/* 2288 */       return new ByteArrayRow(rowData, getExceptionInterceptor());
/*      */     } catch (IOException ioEx) {
/* 2290 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void quit()
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/*      */       try
/*      */       {
/* 2306 */         if (!this.mysqlConnection.isClosed()) {
/*      */           try {
/* 2308 */             this.mysqlConnection.shutdownInput();
/*      */           }
/*      */           catch (UnsupportedOperationException ex) {}
/*      */         }
/*      */       }
/*      */       catch (IOException ioEx) {
/* 2314 */         this.connection.getLog().logWarn("Caught while disconnecting...", ioEx);
/*      */       }
/*      */       
/* 2317 */       Buffer packet = new Buffer(6);
/* 2318 */       this.packetSequence = -1;
/* 2319 */       this.compressedPacketSequence = -1;
/* 2320 */       packet.writeByte((byte)1);
/* 2321 */       send(packet, packet.getPosition());
/*      */     } finally {
/* 2323 */       forceClose();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Buffer getSharedSendPacket()
/*      */   {
/* 2334 */     if (this.sharedSendPacket == null) {
/* 2335 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */     
/* 2338 */     return this.sharedSendPacket;
/*      */   }
/*      */   
/*      */   void closeStreamer(RowData streamer) throws SQLException {
/* 2342 */     if (this.streamingData == null) {
/* 2343 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.17") + streamer + Messages.getString("MysqlIO.18"), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 2347 */     if (streamer != this.streamingData) {
/* 2348 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.19") + streamer + Messages.getString("MysqlIO.20") + Messages.getString("MysqlIO.21") + Messages.getString("MysqlIO.22"), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2354 */     this.streamingData = null;
/*      */   }
/*      */   
/*      */   boolean tackOnMoreStreamingResults(ResultSetImpl addingTo) throws SQLException {
/* 2358 */     if ((this.serverStatus & 0x8) != 0)
/*      */     {
/* 2360 */       boolean moreRowSetsExist = true;
/* 2361 */       ResultSetImpl currentResultSet = addingTo;
/* 2362 */       boolean firstTime = true;
/*      */       
/* 2364 */       while ((moreRowSetsExist) && (
/* 2365 */         (firstTime) || (!currentResultSet.reallyResult())))
/*      */       {
/*      */ 
/*      */ 
/* 2369 */         firstTime = false;
/*      */         
/* 2371 */         Buffer fieldPacket = checkErrorPacket();
/* 2372 */         fieldPacket.setPosition(0);
/*      */         
/* 2374 */         java.sql.Statement owningStatement = addingTo.getStatement();
/*      */         
/* 2376 */         int maxRows = owningStatement.getMaxRows();
/*      */         
/*      */ 
/*      */ 
/* 2380 */         ResultSetImpl newResultSet = readResultsForQueryOrUpdate((StatementImpl)owningStatement, maxRows, owningStatement.getResultSetType(), owningStatement.getResultSetConcurrency(), true, owningStatement.getConnection().getCatalog(), fieldPacket, addingTo.isBinaryEncoded, -1L, null);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2388 */         currentResultSet.setNextResultSet(newResultSet);
/*      */         
/* 2390 */         currentResultSet = newResultSet;
/*      */         
/* 2392 */         moreRowSetsExist = (this.serverStatus & 0x8) != 0;
/*      */         
/* 2394 */         if ((!currentResultSet.reallyResult()) && (!moreRowSetsExist))
/*      */         {
/* 2396 */           return false;
/*      */         }
/*      */       }
/*      */       
/* 2400 */       return true;
/*      */     }
/*      */     
/* 2403 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   ResultSetImpl readAllResults(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/* 2411 */     resultPacket.setPosition(resultPacket.getPosition() - 1);
/*      */     
/* 2413 */     ResultSetImpl topLevelResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 2418 */     ResultSetImpl currentResultSet = topLevelResultSet;
/*      */     
/* 2420 */     boolean checkForMoreResults = (this.clientParam & 0x20000) != 0L;
/*      */     
/*      */ 
/* 2423 */     boolean serverHasMoreResults = (this.serverStatus & 0x8) != 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2429 */     if ((serverHasMoreResults) && (streamResults))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 2434 */       if (topLevelResultSet.getUpdateCount() != -1L) {
/* 2435 */         tackOnMoreStreamingResults(topLevelResultSet);
/*      */       }
/*      */       
/* 2438 */       reclaimLargeReusablePacket();
/*      */       
/* 2440 */       return topLevelResultSet;
/*      */     }
/*      */     
/* 2443 */     boolean moreRowSetsExist = checkForMoreResults & serverHasMoreResults;
/*      */     
/* 2445 */     while (moreRowSetsExist) {
/* 2446 */       Buffer fieldPacket = checkErrorPacket();
/* 2447 */       fieldPacket.setPosition(0);
/*      */       
/* 2449 */       ResultSetImpl newResultSet = readResultsForQueryOrUpdate(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, fieldPacket, isBinaryEncoded, preSentColumnCount, metadataFromCache);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2454 */       currentResultSet.setNextResultSet(newResultSet);
/*      */       
/* 2456 */       currentResultSet = newResultSet;
/*      */       
/* 2458 */       moreRowSetsExist = (this.serverStatus & 0x8) != 0;
/*      */     }
/*      */     
/* 2461 */     if (!streamResults) {
/* 2462 */       clearInputStream();
/*      */     }
/*      */     
/* 2465 */     reclaimLargeReusablePacket();
/*      */     
/* 2467 */     return topLevelResultSet;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void resetMaxBuf()
/*      */   {
/* 2474 */     this.maxAllowedPacket = this.connection.getMaxAllowedPacket();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final Buffer sendCommand(int command, String extraData, Buffer queryPacket, boolean skipCheck, String extraDataCharEncoding, int timeoutMillis)
/*      */     throws SQLException
/*      */   {
/* 2500 */     this.commandCount += 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2507 */     this.enablePacketDebug = this.connection.getEnablePacketDebug();
/* 2508 */     this.readPacketSequence = 0;
/*      */     
/* 2510 */     int oldTimeout = 0;
/*      */     
/* 2512 */     if (timeoutMillis != 0) {
/*      */       try {
/* 2514 */         oldTimeout = this.mysqlConnection.getSoTimeout();
/* 2515 */         this.mysqlConnection.setSoTimeout(timeoutMillis);
/*      */       } catch (SocketException e) {
/* 2517 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 2524 */       checkForOutstandingStreamingData();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2529 */       this.oldServerStatus = this.serverStatus;
/* 2530 */       this.serverStatus = 0;
/* 2531 */       this.hadWarnings = false;
/* 2532 */       this.warningCount = 0;
/*      */       
/* 2534 */       this.queryNoIndexUsed = false;
/* 2535 */       this.queryBadIndexUsed = false;
/* 2536 */       this.serverQueryWasSlow = false;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2542 */       if (this.useCompression) {
/* 2543 */         int bytesLeft = this.mysqlInput.available();
/*      */         
/* 2545 */         if (bytesLeft > 0) {
/* 2546 */           this.mysqlInput.skip(bytesLeft);
/*      */         }
/*      */       }
/*      */       long id;
/*      */       try {
/* 2551 */         clearInputStream();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2560 */         if (queryPacket == null) {
/* 2561 */           int packLength = 8 + (extraData != null ? extraData.length() : 0) + 2;
/*      */           
/*      */ 
/* 2564 */           if (this.sendPacket == null) {
/* 2565 */             this.sendPacket = new Buffer(packLength);
/*      */           }
/*      */           
/* 2568 */           this.packetSequence = -1;
/* 2569 */           this.compressedPacketSequence = -1;
/* 2570 */           this.readPacketSequence = 0;
/* 2571 */           this.checkPacketSequence = true;
/* 2572 */           this.sendPacket.clear();
/*      */           
/* 2574 */           this.sendPacket.writeByte((byte)command);
/*      */           
/* 2576 */           if ((command == 2) || (command == 5) || (command == 6) || (command == 3) || (command == 22))
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/* 2581 */             if (extraDataCharEncoding == null) {
/* 2582 */               this.sendPacket.writeStringNoNull(extraData);
/*      */             } else {
/* 2584 */               this.sendPacket.writeStringNoNull(extraData, extraDataCharEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */             }
/*      */             
/*      */ 
/*      */           }
/* 2589 */           else if (command == 12) {
/* 2590 */             id = Long.parseLong(extraData);
/* 2591 */             this.sendPacket.writeLong(id);
/*      */           }
/*      */           
/* 2594 */           send(this.sendPacket, this.sendPacket.getPosition());
/*      */         } else {
/* 2596 */           this.packetSequence = -1;
/* 2597 */           this.compressedPacketSequence = -1;
/* 2598 */           send(queryPacket, queryPacket.getPosition());
/*      */         }
/*      */       }
/*      */       catch (SQLException sqlEx) {
/* 2602 */         throw sqlEx;
/*      */       } catch (Exception ex) {
/* 2604 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ex, getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 2608 */       Buffer returnPacket = null;
/*      */       
/* 2610 */       if (!skipCheck) {
/* 2611 */         if ((command == 23) || (command == 26))
/*      */         {
/* 2613 */           this.readPacketSequence = 0;
/* 2614 */           this.packetSequenceReset = true;
/*      */         }
/*      */         
/* 2617 */         returnPacket = checkErrorPacket(command);
/*      */       }
/*      */       
/* 2620 */       return returnPacket;
/*      */     } catch (IOException ioEx) {
/* 2622 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */     finally {
/* 2625 */       if (timeoutMillis != 0) {
/*      */         try {
/* 2627 */           this.mysqlConnection.setSoTimeout(oldTimeout);
/*      */         } catch (SocketException e) {
/* 2629 */           throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, e, getExceptionInterceptor());
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/* 2636 */   private int statementExecutionDepth = 0;
/*      */   private boolean useAutoSlowLog;
/*      */   
/*      */   protected boolean shouldIntercept() {
/* 2640 */     return this.statementInterceptors != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final ResultSetInternalMethods sqlQueryDirect(StatementImpl callingStatement, String query, String characterEncoding, Buffer queryPacket, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Field[] cachedMetadata)
/*      */     throws Exception
/*      */   {
/* 2667 */     this.statementExecutionDepth += 1;
/*      */     try
/*      */     {
/* 2670 */       if (this.statementInterceptors != null) {
/* 2671 */         ResultSetInternalMethods interceptedResults = invokeStatementInterceptorsPre(query, callingStatement, false);
/*      */         
/*      */ 
/* 2674 */         if (interceptedResults != null) {
/* 2675 */           return interceptedResults;
/*      */         }
/*      */       }
/*      */       
/* 2679 */       long queryStartTime = 0L;
/* 2680 */       long queryEndTime = 0L;
/*      */       
/* 2682 */       String statementComment = this.connection.getStatementComment();
/*      */       
/* 2684 */       if (this.connection.getIncludeThreadNamesAsStatementComment()) {
/* 2685 */         statementComment = (statementComment != null ? statementComment + ", " : "") + "java thread: " + Thread.currentThread().getName();
/*      */       }
/*      */       
/* 2688 */       if (query != null)
/*      */       {
/*      */ 
/*      */ 
/* 2692 */         int packLength = 5 + query.length() * 3 + 2;
/*      */         
/* 2694 */         byte[] commentAsBytes = null;
/*      */         
/* 2696 */         if (statementComment != null) {
/* 2697 */           commentAsBytes = StringUtils.getBytes(statementComment, null, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 2702 */           packLength += commentAsBytes.length;
/* 2703 */           packLength += 6;
/*      */         }
/*      */         
/* 2706 */         if (this.sendPacket == null) {
/* 2707 */           this.sendPacket = new Buffer(packLength);
/*      */         } else {
/* 2709 */           this.sendPacket.clear();
/*      */         }
/*      */         
/* 2712 */         this.sendPacket.writeByte((byte)3);
/*      */         
/* 2714 */         if (commentAsBytes != null) {
/* 2715 */           this.sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES);
/* 2716 */           this.sendPacket.writeBytesNoNull(commentAsBytes);
/* 2717 */           this.sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES);
/*      */         }
/*      */         
/* 2720 */         if (characterEncoding != null) {
/* 2721 */           if (this.platformDbCharsetMatches) {
/* 2722 */             this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */ 
/*      */ 
/*      */           }
/* 2727 */           else if (StringUtils.startsWithIgnoreCaseAndWs(query, "LOAD DATA")) {
/* 2728 */             this.sendPacket.writeBytesNoNull(StringUtils.getBytes(query));
/*      */           } else {
/* 2730 */             this.sendPacket.writeStringNoNull(query, characterEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), this.connection);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 2738 */           this.sendPacket.writeStringNoNull(query);
/*      */         }
/*      */         
/* 2741 */         queryPacket = this.sendPacket;
/*      */       }
/*      */       
/* 2744 */       byte[] queryBuf = null;
/* 2745 */       int oldPacketPosition = 0;
/*      */       
/* 2747 */       if (this.needToGrabQueryFromPacket) {
/* 2748 */         queryBuf = queryPacket.getByteBuffer();
/*      */         
/*      */ 
/* 2751 */         oldPacketPosition = queryPacket.getPosition();
/*      */         
/* 2753 */         queryStartTime = getCurrentTimeNanosOrMillis();
/*      */       }
/*      */       
/* 2756 */       if (this.autoGenerateTestcaseScript) {
/* 2757 */         String testcaseQuery = null;
/*      */         
/* 2759 */         if (query != null) {
/* 2760 */           if (statementComment != null) {
/* 2761 */             testcaseQuery = "/* " + statementComment + " */ " + query;
/*      */           } else {
/* 2763 */             testcaseQuery = query;
/*      */           }
/*      */         } else {
/* 2766 */           testcaseQuery = StringUtils.toString(queryBuf, 5, oldPacketPosition - 5);
/*      */         }
/*      */         
/*      */ 
/* 2770 */         StringBuffer debugBuf = new StringBuffer(testcaseQuery.length() + 32);
/* 2771 */         this.connection.generateConnectionCommentBlock(debugBuf);
/* 2772 */         debugBuf.append(testcaseQuery);
/* 2773 */         debugBuf.append(';');
/* 2774 */         this.connection.dumpTestcaseQuery(debugBuf.toString());
/*      */       }
/*      */       
/*      */ 
/* 2778 */       Buffer resultPacket = sendCommand(3, null, queryPacket, false, null, 0);
/*      */       
/*      */ 
/* 2781 */       long fetchBeginTime = 0L;
/* 2782 */       long fetchEndTime = 0L;
/*      */       
/* 2784 */       String profileQueryToLog = null;
/*      */       
/* 2786 */       boolean queryWasSlow = false;
/*      */       
/* 2788 */       if ((this.profileSql) || (this.logSlowQueries)) {
/* 2789 */         queryEndTime = getCurrentTimeNanosOrMillis();
/*      */         
/* 2791 */         boolean shouldExtractQuery = false;
/*      */         
/* 2793 */         if (this.profileSql) {
/* 2794 */           shouldExtractQuery = true;
/* 2795 */         } else if (this.logSlowQueries) {
/* 2796 */           long queryTime = queryEndTime - queryStartTime;
/*      */           
/* 2798 */           boolean logSlow = false;
/*      */           
/* 2800 */           if (!this.useAutoSlowLog) {
/* 2801 */             logSlow = queryTime > this.connection.getSlowQueryThresholdMillis();
/*      */           } else {
/* 2803 */             logSlow = this.connection.isAbonormallyLongQuery(queryTime);
/*      */             
/* 2805 */             this.connection.reportQueryTime(queryTime);
/*      */           }
/*      */           
/* 2808 */           if (logSlow) {
/* 2809 */             shouldExtractQuery = true;
/* 2810 */             queryWasSlow = true;
/*      */           }
/*      */         }
/*      */         
/* 2814 */         if (shouldExtractQuery)
/*      */         {
/* 2816 */           boolean truncated = false;
/*      */           
/* 2818 */           int extractPosition = oldPacketPosition;
/*      */           
/* 2820 */           if (oldPacketPosition > this.connection.getMaxQuerySizeToLog()) {
/* 2821 */             extractPosition = this.connection.getMaxQuerySizeToLog() + 5;
/* 2822 */             truncated = true;
/*      */           }
/*      */           
/* 2825 */           profileQueryToLog = StringUtils.toString(queryBuf, 5, extractPosition - 5);
/*      */           
/*      */ 
/* 2828 */           if (truncated) {
/* 2829 */             profileQueryToLog = profileQueryToLog + Messages.getString("MysqlIO.25");
/*      */           }
/*      */         }
/*      */         
/* 2833 */         fetchBeginTime = queryEndTime;
/*      */       }
/*      */       
/* 2836 */       ResultSetInternalMethods rs = readAllResults(callingStatement, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, resultPacket, false, -1L, cachedMetadata);
/*      */       
/*      */ 
/*      */ 
/* 2840 */       if ((queryWasSlow) && (!this.serverQueryWasSlow)) {
/* 2841 */         StringBuffer mesgBuf = new StringBuffer(48 + profileQueryToLog.length());
/*      */         
/*      */ 
/* 2844 */         mesgBuf.append(Messages.getString("MysqlIO.SlowQuery", new Object[] { String.valueOf(this.useAutoSlowLog ? " 95% of all queries " : Long.valueOf(this.slowQueryThreshold)), this.queryTimingUnits, Long.valueOf(queryEndTime - queryStartTime) }));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 2849 */         mesgBuf.append(profileQueryToLog);
/*      */         
/* 2851 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2853 */         eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), (int)(queryEndTime - queryStartTime), this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), mesgBuf.toString()));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2860 */         if (this.connection.getExplainSlowQueries()) {
/* 2861 */           if (oldPacketPosition < 1048576) {
/* 2862 */             explainSlowQuery(queryPacket.getBytes(5, oldPacketPosition - 5), profileQueryToLog);
/*      */           }
/*      */           else {
/* 2865 */             this.connection.getLog().logWarn(Messages.getString("MysqlIO.28") + 1048576 + Messages.getString("MysqlIO.29"));
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2873 */       if (this.logSlowQueries)
/*      */       {
/* 2875 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2877 */         if ((this.queryBadIndexUsed) && (this.profileSql)) {
/* 2878 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.33") + profileQueryToLog));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2891 */         if ((this.queryNoIndexUsed) && (this.profileSql)) {
/* 2892 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.35") + profileQueryToLog));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2905 */         if ((this.serverQueryWasSlow) && (this.profileSql)) {
/* 2906 */           eventSink.consumeEvent(new ProfilerEvent((byte)6, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), Messages.getString("MysqlIO.ServerSlowQuery") + profileQueryToLog));
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2920 */       if (this.profileSql) {
/* 2921 */         fetchEndTime = getCurrentTimeNanosOrMillis();
/*      */         
/* 2923 */         ProfilerEventHandler eventSink = ProfilerEventHandlerFactory.getInstance(this.connection);
/*      */         
/* 2925 */         eventSink.consumeEvent(new ProfilerEvent((byte)3, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), queryEndTime - queryStartTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), profileQueryToLog));
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2933 */         eventSink.consumeEvent(new ProfilerEvent((byte)5, "", catalog, this.connection.getId(), callingStatement != null ? callingStatement.getId() : 999, ((ResultSetImpl)rs).resultId, System.currentTimeMillis(), fetchEndTime - fetchBeginTime, this.queryTimingUnits, null, LogUtils.findCallingClassAndMethod(new Throwable()), null));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2942 */       if (this.hadWarnings) {
/* 2943 */         scanForAndThrowDataTruncation();
/*      */       }
/*      */       ResultSetInternalMethods interceptedResults;
/* 2946 */       if (this.statementInterceptors != null) {
/* 2947 */         interceptedResults = invokeStatementInterceptorsPost(query, callingStatement, rs, false, null);
/*      */         
/*      */ 
/* 2950 */         if (interceptedResults != null) {
/* 2951 */           rs = interceptedResults;
/*      */         }
/*      */       }
/*      */       
/* 2955 */       return rs;
/*      */     } catch (SQLException sqlEx) {
/* 2957 */       if (this.statementInterceptors != null) {
/* 2958 */         invokeStatementInterceptorsPost(query, callingStatement, null, false, sqlEx);
/*      */       }
/*      */       
/*      */ 
/* 2962 */       if (callingStatement != null) {
/* 2963 */         synchronized (callingStatement.cancelTimeoutMutex) {
/* 2964 */           if (callingStatement.wasCancelled) {
/* 2965 */             SQLException cause = null;
/*      */             
/* 2967 */             if (callingStatement.wasCancelledByTimeout) {
/* 2968 */               cause = new MySQLTimeoutException();
/*      */             } else {
/* 2970 */               cause = new MySQLStatementCancelledException();
/*      */             }
/*      */             
/* 2973 */             callingStatement.resetCancelledState();
/*      */             
/* 2975 */             throw cause;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2980 */       throw sqlEx;
/*      */     } finally {
/* 2982 */       this.statementExecutionDepth -= 1;
/*      */     }
/*      */   }
/*      */   
/*      */   ResultSetInternalMethods invokeStatementInterceptorsPre(String sql, Statement interceptedStatement, boolean forceExecute) throws SQLException
/*      */   {
/* 2988 */     ResultSetInternalMethods previousResultSet = null;
/*      */     
/* 2990 */     Iterator<StatementInterceptorV2> interceptors = this.statementInterceptors.iterator();
/*      */     
/* 2992 */     while (interceptors.hasNext()) {
/* 2993 */       StatementInterceptorV2 interceptor = (StatementInterceptorV2)interceptors.next();
/*      */       
/* 2995 */       boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
/* 2996 */       boolean shouldExecute = ((executeTopLevelOnly) && ((this.statementExecutionDepth == 1) || (forceExecute))) || (!executeTopLevelOnly);
/*      */       
/*      */ 
/* 2999 */       if (shouldExecute) {
/* 3000 */         String sqlToInterceptor = sql;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3007 */         ResultSetInternalMethods interceptedResultSet = interceptor.preProcess(sqlToInterceptor, interceptedStatement, this.connection);
/*      */         
/*      */ 
/*      */ 
/* 3011 */         if (interceptedResultSet != null) {
/* 3012 */           previousResultSet = interceptedResultSet;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3017 */     return previousResultSet;
/*      */   }
/*      */   
/*      */   ResultSetInternalMethods invokeStatementInterceptorsPost(String sql, Statement interceptedStatement, ResultSetInternalMethods originalResultSet, boolean forceExecute, SQLException statementException)
/*      */     throws SQLException
/*      */   {
/* 3023 */     Iterator<StatementInterceptorV2> interceptors = this.statementInterceptors.iterator();
/*      */     
/* 3025 */     while (interceptors.hasNext()) {
/* 3026 */       StatementInterceptorV2 interceptor = (StatementInterceptorV2)interceptors.next();
/*      */       
/* 3028 */       boolean executeTopLevelOnly = interceptor.executeTopLevelOnly();
/* 3029 */       boolean shouldExecute = ((executeTopLevelOnly) && ((this.statementExecutionDepth == 1) || (forceExecute))) || (!executeTopLevelOnly);
/*      */       
/*      */ 
/* 3032 */       if (shouldExecute) {
/* 3033 */         String sqlToInterceptor = sql;
/*      */         
/* 3035 */         ResultSetInternalMethods interceptedResultSet = interceptor.postProcess(sqlToInterceptor, interceptedStatement, originalResultSet, this.connection, this.warningCount, this.queryNoIndexUsed, this.queryBadIndexUsed, statementException);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 3040 */         if (interceptedResultSet != null) {
/* 3041 */           originalResultSet = interceptedResultSet;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 3046 */     return originalResultSet;
/*      */   }
/*      */   
/*      */   private void calculateSlowQueryThreshold() {
/* 3050 */     this.slowQueryThreshold = this.connection.getSlowQueryThresholdMillis();
/*      */     
/* 3052 */     if (this.connection.getUseNanosForElapsedTime()) {
/* 3053 */       long nanosThreshold = this.connection.getSlowQueryThresholdNanos();
/*      */       
/* 3055 */       if (nanosThreshold != 0L) {
/* 3056 */         this.slowQueryThreshold = nanosThreshold;
/*      */       } else {
/* 3058 */         this.slowQueryThreshold *= 1000000L;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected long getCurrentTimeNanosOrMillis() {
/* 3064 */     if (this.useNanosForElapsedTime) {
/* 3065 */       return Util.getCurrentTimeNanosOrMillis();
/*      */     }
/*      */     
/* 3068 */     return System.currentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   String getHost()
/*      */   {
/* 3077 */     return this.host;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean isVersion(int major, int minor, int subminor)
/*      */   {
/* 3092 */     return (major == getServerMajorVersion()) && (minor == getServerMinorVersion()) && (subminor == getServerSubMinorVersion());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   boolean versionMeetsMinimum(int major, int minor, int subminor)
/*      */   {
/* 3108 */     if (getServerMajorVersion() >= major) {
/* 3109 */       if (getServerMajorVersion() == major) {
/* 3110 */         if (getServerMinorVersion() >= minor) {
/* 3111 */           if (getServerMinorVersion() == minor) {
/* 3112 */             return getServerSubMinorVersion() >= subminor;
/*      */           }
/*      */           
/*      */ 
/* 3116 */           return true;
/*      */         }
/*      */         
/*      */ 
/* 3120 */         return false;
/*      */       }
/*      */       
/*      */ 
/* 3124 */       return true;
/*      */     }
/*      */     
/* 3127 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final String getPacketDumpToLog(Buffer packetToDump, int packetLength)
/*      */   {
/* 3141 */     if (packetLength < 1024) {
/* 3142 */       return packetToDump.dump(packetLength);
/*      */     }
/*      */     
/* 3145 */     StringBuffer packetDumpBuf = new StringBuffer(4096);
/* 3146 */     packetDumpBuf.append(packetToDump.dump(1024));
/* 3147 */     packetDumpBuf.append(Messages.getString("MysqlIO.36"));
/* 3148 */     packetDumpBuf.append(1024);
/* 3149 */     packetDumpBuf.append(Messages.getString("MysqlIO.37"));
/*      */     
/* 3151 */     return packetDumpBuf.toString();
/*      */   }
/*      */   
/*      */   private final int readFully(InputStream in, byte[] b, int off, int len) throws IOException
/*      */   {
/* 3156 */     if (len < 0) {
/* 3157 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/* 3160 */     int n = 0;
/*      */     
/* 3162 */     while (n < len) {
/* 3163 */       int count = in.read(b, off + n, len - n);
/*      */       
/* 3165 */       if (count < 0) {
/* 3166 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { Integer.valueOf(len), Integer.valueOf(n) }));
/*      */       }
/*      */       
/*      */ 
/* 3170 */       n += count;
/*      */     }
/*      */     
/* 3173 */     return n;
/*      */   }
/*      */   
/*      */   private final long skipFully(InputStream in, long len) throws IOException {
/* 3177 */     if (len < 0L) {
/* 3178 */       throw new IOException("Negative skip length not allowed");
/*      */     }
/*      */     
/* 3181 */     long n = 0L;
/*      */     
/* 3183 */     while (n < len) {
/* 3184 */       long count = in.skip(len - n);
/*      */       
/* 3186 */       if (count < 0L) {
/* 3187 */         throw new EOFException(Messages.getString("MysqlIO.EOF", new Object[] { Long.valueOf(len), Long.valueOf(n) }));
/*      */       }
/*      */       
/*      */ 
/* 3191 */       n += count;
/*      */     }
/*      */     
/* 3194 */     return n;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ResultSetImpl readResultsForQueryOrUpdate(StatementImpl callingStatement, int maxRows, int resultSetType, int resultSetConcurrency, boolean streamResults, String catalog, Buffer resultPacket, boolean isBinaryEncoded, long preSentColumnCount, Field[] metadataFromCache)
/*      */     throws SQLException
/*      */   {
/* 3222 */     long columnCount = resultPacket.readFieldLength();
/*      */     
/* 3224 */     if (columnCount == 0L)
/* 3225 */       return buildResultSetWithUpdates(callingStatement, resultPacket);
/* 3226 */     if (columnCount == -1L) {
/* 3227 */       String charEncoding = null;
/*      */       
/* 3229 */       if (this.connection.getUseUnicode()) {
/* 3230 */         charEncoding = this.connection.getEncoding();
/*      */       }
/*      */       
/* 3233 */       String fileName = null;
/*      */       
/* 3235 */       if (this.platformDbCharsetMatches) {
/* 3236 */         fileName = charEncoding != null ? resultPacket.readString(charEncoding, getExceptionInterceptor()) : resultPacket.readString();
/*      */       }
/*      */       else
/*      */       {
/* 3240 */         fileName = resultPacket.readString();
/*      */       }
/*      */       
/* 3243 */       return sendFileToServer(callingStatement, fileName);
/*      */     }
/* 3245 */     ResultSetImpl results = getResultSet(callingStatement, columnCount, maxRows, resultSetType, resultSetConcurrency, streamResults, catalog, isBinaryEncoded, metadataFromCache);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3250 */     return results;
/*      */   }
/*      */   
/*      */   private int alignPacketSize(int a, int l)
/*      */   {
/* 3255 */     return a + l - 1 & (l - 1 ^ 0xFFFFFFFF);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ResultSetImpl buildResultSetWithRows(StatementImpl callingStatement, String catalog, Field[] fields, RowData rows, int resultSetType, int resultSetConcurrency, boolean isBinaryEncoded)
/*      */     throws SQLException
/*      */   {
/* 3263 */     ResultSetImpl rs = null;
/*      */     
/* 3265 */     switch (resultSetConcurrency) {
/*      */     case 1007: 
/* 3267 */       rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
/*      */       
/*      */ 
/* 3270 */       if (isBinaryEncoded) {
/* 3271 */         rs.setBinaryEncoded();
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case 1008: 
/* 3277 */       rs = ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, true);
/*      */       
/*      */ 
/* 3280 */       break;
/*      */     
/*      */     default: 
/* 3283 */       return ResultSetImpl.getInstance(catalog, fields, rows, this.connection, callingStatement, false);
/*      */     }
/*      */     
/*      */     
/* 3287 */     rs.setResultSetType(resultSetType);
/* 3288 */     rs.setResultSetConcurrency(resultSetConcurrency);
/*      */     
/* 3290 */     return rs;
/*      */   }
/*      */   
/*      */   private ResultSetImpl buildResultSetWithUpdates(StatementImpl callingStatement, Buffer resultPacket)
/*      */     throws SQLException
/*      */   {
/* 3296 */     long updateCount = -1L;
/* 3297 */     long updateID = -1L;
/* 3298 */     String info = null;
/*      */     try
/*      */     {
/* 3301 */       if (this.useNewUpdateCounts) {
/* 3302 */         updateCount = resultPacket.newReadLength();
/* 3303 */         updateID = resultPacket.newReadLength();
/*      */       } else {
/* 3305 */         updateCount = resultPacket.readLength();
/* 3306 */         updateID = resultPacket.readLength();
/*      */       }
/*      */       
/* 3309 */       if (this.use41Extensions)
/*      */       {
/* 3311 */         this.serverStatus = resultPacket.readInt();
/*      */         
/* 3313 */         checkTransactionState(this.oldServerStatus);
/*      */         
/* 3315 */         this.warningCount = resultPacket.readInt();
/*      */         
/* 3317 */         if (this.warningCount > 0) {
/* 3318 */           this.hadWarnings = true;
/*      */         }
/*      */         
/* 3321 */         resultPacket.readByte();
/*      */         
/* 3323 */         setServerSlowQueryFlags();
/*      */       }
/*      */       
/* 3326 */       if (this.connection.isReadInfoMsgEnabled()) {
/* 3327 */         info = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */       }
/*      */     } catch (Exception ex) {
/* 3330 */       SQLException sqlEx = SQLError.createSQLException(SQLError.get("S1000"), "S1000", -1, getExceptionInterceptor());
/*      */       
/* 3332 */       sqlEx.initCause(ex);
/*      */       
/* 3334 */       throw sqlEx;
/*      */     }
/*      */     
/* 3337 */     ResultSetInternalMethods updateRs = ResultSetImpl.getInstance(updateCount, updateID, this.connection, callingStatement);
/*      */     
/*      */ 
/* 3340 */     if (info != null) {
/* 3341 */       ((ResultSetImpl)updateRs).setServerInfo(info);
/*      */     }
/*      */     
/* 3344 */     return (ResultSetImpl)updateRs;
/*      */   }
/*      */   
/*      */   private void setServerSlowQueryFlags() {
/* 3348 */     this.queryBadIndexUsed = ((this.serverStatus & 0x10) != 0);
/*      */     
/* 3350 */     this.queryNoIndexUsed = ((this.serverStatus & 0x20) != 0);
/*      */     
/* 3352 */     this.serverQueryWasSlow = ((this.serverStatus & 0x800) != 0);
/*      */   }
/*      */   
/*      */   private void checkForOutstandingStreamingData() throws SQLException
/*      */   {
/* 3357 */     if (this.streamingData != null) {
/* 3358 */       boolean shouldClobber = this.connection.getClobberStreamingResults();
/*      */       
/* 3360 */       if (!shouldClobber) {
/* 3361 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.39") + this.streamingData + Messages.getString("MysqlIO.40") + Messages.getString("MysqlIO.41") + Messages.getString("MysqlIO.42"), getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3369 */       this.streamingData.getOwner().realClose(false);
/*      */       
/*      */ 
/* 3372 */       clearInputStream();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Buffer compressPacket(Buffer packet, int offset, int packetLen)
/*      */     throws SQLException
/*      */   {
/* 3387 */     int compressedLength = packetLen;
/* 3388 */     int uncompressedLength = 0;
/* 3389 */     byte[] compressedBytes = null;
/* 3390 */     int offsetWrite = offset;
/*      */     
/* 3392 */     if (packetLen < 50) {
/* 3393 */       compressedBytes = packet.getByteBuffer();
/*      */     }
/*      */     else {
/* 3396 */       byte[] bytesToCompress = packet.getByteBuffer();
/* 3397 */       compressedBytes = new byte[bytesToCompress.length * 2];
/*      */       
/* 3399 */       if (this.deflater == null) {
/* 3400 */         this.deflater = new Deflater();
/*      */       }
/* 3402 */       this.deflater.reset();
/* 3403 */       this.deflater.setInput(bytesToCompress, offset, packetLen);
/* 3404 */       this.deflater.finish();
/*      */       
/* 3406 */       compressedLength = this.deflater.deflate(compressedBytes);
/*      */       
/* 3408 */       if (compressedLength > packetLen)
/*      */       {
/* 3410 */         compressedBytes = packet.getByteBuffer();
/* 3411 */         compressedLength = packetLen;
/*      */       } else {
/* 3413 */         uncompressedLength = packetLen;
/* 3414 */         offsetWrite = 0;
/*      */       }
/*      */     }
/*      */     
/* 3418 */     Buffer compressedPacket = new Buffer(7 + compressedLength);
/*      */     
/* 3420 */     compressedPacket.setPosition(0);
/* 3421 */     compressedPacket.writeLongInt(compressedLength);
/* 3422 */     compressedPacket.writeByte(this.compressedPacketSequence);
/* 3423 */     compressedPacket.writeLongInt(uncompressedLength);
/* 3424 */     compressedPacket.writeBytesNoNull(compressedBytes, offsetWrite, compressedLength);
/*      */     
/* 3426 */     return compressedPacket;
/*      */   }
/*      */   
/*      */   private final void readServerStatusForResultSets(Buffer rowPacket) throws SQLException
/*      */   {
/* 3431 */     if (this.use41Extensions) {
/* 3432 */       rowPacket.readByte();
/*      */       
/* 3434 */       this.warningCount = rowPacket.readInt();
/*      */       
/* 3436 */       if (this.warningCount > 0) {
/* 3437 */         this.hadWarnings = true;
/*      */       }
/*      */       
/* 3440 */       this.oldServerStatus = this.serverStatus;
/* 3441 */       this.serverStatus = rowPacket.readInt();
/* 3442 */       checkTransactionState(this.oldServerStatus);
/*      */       
/* 3444 */       setServerSlowQueryFlags();
/*      */     }
/*      */   }
/*      */   
/*      */   private SocketFactory createSocketFactory() throws SQLException {
/*      */     try {
/* 3450 */       if (this.socketFactoryClassName == null) {
/* 3451 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.75"), "08001", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/* 3455 */       return (SocketFactory)Class.forName(this.socketFactoryClassName).newInstance();
/*      */     }
/*      */     catch (Exception ex) {
/* 3458 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.76") + this.socketFactoryClassName + Messages.getString("MysqlIO.77"), "08001", getExceptionInterceptor());
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 3463 */       sqlEx.initCause(ex);
/*      */       
/* 3465 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   private void enqueuePacketForDebugging(boolean isPacketBeingSent, boolean isPacketReused, int sendLength, byte[] header, Buffer packet)
/*      */     throws SQLException
/*      */   {
/* 3472 */     if (this.packetDebugRingBuffer.size() + 1 > this.connection.getPacketDebugBufferSize()) {
/* 3473 */       this.packetDebugRingBuffer.removeFirst();
/*      */     }
/*      */     
/* 3476 */     StringBuffer packetDump = null;
/*      */     
/* 3478 */     if (!isPacketBeingSent) {
/* 3479 */       int bytesToDump = Math.min(1024, packet.getBufLength());
/*      */       
/*      */ 
/* 3482 */       Buffer packetToDump = new Buffer(4 + bytesToDump);
/*      */       
/* 3484 */       packetToDump.setPosition(0);
/* 3485 */       packetToDump.writeBytesNoNull(header);
/* 3486 */       packetToDump.writeBytesNoNull(packet.getBytes(0, bytesToDump));
/*      */       
/* 3488 */       String packetPayload = packetToDump.dump(bytesToDump);
/*      */       
/* 3490 */       packetDump = new StringBuffer(96 + packetPayload.length());
/*      */       
/* 3492 */       packetDump.append("Server ");
/*      */       
/* 3494 */       if (isPacketReused) {
/* 3495 */         packetDump.append("(re-used)");
/*      */       } else {
/* 3497 */         packetDump.append("(new)");
/*      */       }
/*      */       
/* 3500 */       packetDump.append(" ");
/* 3501 */       packetDump.append(packet.toSuperString());
/* 3502 */       packetDump.append(" --------------------> Client\n");
/* 3503 */       packetDump.append("\nPacket payload:\n\n");
/* 3504 */       packetDump.append(packetPayload);
/*      */       
/* 3506 */       if (bytesToDump == 1024) {
/* 3507 */         packetDump.append("\nNote: Packet of " + packet.getBufLength() + " bytes truncated to " + 1024 + " bytes.\n");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 3512 */       int bytesToDump = Math.min(1024, sendLength);
/*      */       
/* 3514 */       String packetPayload = packet.dump(bytesToDump);
/*      */       
/* 3516 */       packetDump = new StringBuffer(68 + packetPayload.length());
/*      */       
/* 3518 */       packetDump.append("Client ");
/* 3519 */       packetDump.append(packet.toSuperString());
/* 3520 */       packetDump.append("--------------------> Server\n");
/* 3521 */       packetDump.append("\nPacket payload:\n\n");
/* 3522 */       packetDump.append(packetPayload);
/*      */       
/* 3524 */       if (bytesToDump == 1024) {
/* 3525 */         packetDump.append("\nNote: Packet of " + sendLength + " bytes truncated to " + 1024 + " bytes.\n");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3531 */     this.packetDebugRingBuffer.addLast(packetDump);
/*      */   }
/*      */   
/*      */ 
/*      */   private RowData readSingleRowSet(long columnCount, int maxRows, int resultSetConcurrency, boolean isBinaryEncoded, Field[] fields)
/*      */     throws SQLException
/*      */   {
/* 3538 */     ArrayList<ResultSetRow> rows = new ArrayList();
/*      */     
/* 3540 */     boolean useBufferRowExplicit = useBufferRowExplicit(fields);
/*      */     
/*      */ 
/* 3543 */     ResultSetRow row = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
/*      */     
/*      */ 
/* 3546 */     int rowCount = 0;
/*      */     
/* 3548 */     if (row != null) {
/* 3549 */       rows.add(row);
/* 3550 */       rowCount = 1;
/*      */     }
/*      */     
/* 3553 */     while (row != null) {
/* 3554 */       row = nextRow(fields, (int)columnCount, isBinaryEncoded, resultSetConcurrency, false, useBufferRowExplicit, false, null);
/*      */       
/*      */ 
/* 3557 */       if ((row != null) && (
/* 3558 */         (maxRows == -1) || (rowCount < maxRows))) {
/* 3559 */         rows.add(row);
/* 3560 */         rowCount++;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 3565 */     RowData rowData = new RowDataStatic(rows);
/*      */     
/* 3567 */     return rowData;
/*      */   }
/*      */   
/*      */   public static boolean useBufferRowExplicit(Field[] fields) {
/* 3571 */     if (fields == null) {
/* 3572 */       return false;
/*      */     }
/*      */     
/* 3575 */     for (int i = 0; i < fields.length; i++) {
/* 3576 */       switch (fields[i].getSQLType()) {
/*      */       case -4: 
/*      */       case -1: 
/*      */       case 2004: 
/*      */       case 2005: 
/* 3581 */         return true;
/*      */       }
/*      */       
/*      */     }
/* 3585 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void reclaimLargeReusablePacket()
/*      */   {
/* 3592 */     if ((this.reusablePacket != null) && (this.reusablePacket.getCapacity() > 1048576))
/*      */     {
/* 3594 */       this.reusablePacket = new Buffer(1024);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final Buffer reuseAndReadPacket(Buffer reuse)
/*      */     throws SQLException
/*      */   {
/* 3609 */     return reuseAndReadPacket(reuse, -1);
/*      */   }
/*      */   
/*      */   private final Buffer reuseAndReadPacket(Buffer reuse, int existingPacketLength) throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3616 */       reuse.setWasMultiPacket(false);
/* 3617 */       int packetLength = 0;
/*      */       
/* 3619 */       if (existingPacketLength == -1) {
/* 3620 */         int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */         
/*      */ 
/* 3623 */         if (lengthRead < 4) {
/* 3624 */           forceClose();
/* 3625 */           throw new IOException(Messages.getString("MysqlIO.43"));
/*      */         }
/*      */         
/* 3628 */         packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       }
/*      */       else
/*      */       {
/* 3632 */         packetLength = existingPacketLength;
/*      */       }
/*      */       
/* 3635 */       if (this.traceProtocol) {
/* 3636 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/* 3638 */         traceMessageBuf.append(Messages.getString("MysqlIO.44"));
/* 3639 */         traceMessageBuf.append(packetLength);
/* 3640 */         traceMessageBuf.append(Messages.getString("MysqlIO.45"));
/* 3641 */         traceMessageBuf.append(StringUtils.dumpAsHex(this.packetHeaderBuf, 4));
/*      */         
/*      */ 
/* 3644 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       }
/*      */       
/* 3647 */       byte multiPacketSeq = this.packetHeaderBuf[3];
/*      */       
/* 3649 */       if (!this.packetSequenceReset) {
/* 3650 */         if ((this.enablePacketDebug) && (this.checkPacketSequence)) {
/* 3651 */           checkPacketSequencing(multiPacketSeq);
/*      */         }
/*      */       } else {
/* 3654 */         this.packetSequenceReset = false;
/*      */       }
/*      */       
/* 3657 */       this.readPacketSequence = multiPacketSeq;
/*      */       
/*      */ 
/* 3660 */       reuse.setPosition(0);
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3668 */       if (reuse.getByteBuffer().length <= packetLength) {
/* 3669 */         reuse.setByteBuffer(new byte[packetLength + 1]);
/*      */       }
/*      */       
/*      */ 
/* 3673 */       reuse.setBufLength(packetLength);
/*      */       
/*      */ 
/* 3676 */       int numBytesRead = readFully(this.mysqlInput, reuse.getByteBuffer(), 0, packetLength);
/*      */       
/*      */ 
/* 3679 */       if (numBytesRead != packetLength) {
/* 3680 */         throw new IOException("Short read, expected " + packetLength + " bytes, only read " + numBytesRead);
/*      */       }
/*      */       
/*      */ 
/* 3684 */       if (this.traceProtocol) {
/* 3685 */         StringBuffer traceMessageBuf = new StringBuffer();
/*      */         
/* 3687 */         traceMessageBuf.append(Messages.getString("MysqlIO.46"));
/* 3688 */         traceMessageBuf.append(getPacketDumpToLog(reuse, packetLength));
/*      */         
/*      */ 
/* 3691 */         this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */       }
/*      */       
/* 3694 */       if (this.enablePacketDebug) {
/* 3695 */         enqueuePacketForDebugging(false, true, 0, this.packetHeaderBuf, reuse);
/*      */       }
/*      */       
/*      */ 
/* 3699 */       boolean isMultiPacket = false;
/*      */       
/* 3701 */       if (packetLength == this.maxThreeBytes) {
/* 3702 */         reuse.setPosition(this.maxThreeBytes);
/*      */         
/*      */ 
/* 3705 */         isMultiPacket = true;
/*      */         
/* 3707 */         packetLength = readRemainingMultiPackets(reuse, multiPacketSeq);
/*      */       }
/*      */       
/* 3710 */       if (!isMultiPacket) {
/* 3711 */         reuse.getByteBuffer()[packetLength] = 0;
/*      */       }
/*      */       
/* 3714 */       if (this.connection.getMaintainTimeStats()) {
/* 3715 */         this.lastPacketReceivedTimeMs = System.currentTimeMillis();
/*      */       }
/*      */       
/* 3718 */       return reuse;
/*      */     } catch (IOException ioEx) {
/* 3720 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */     catch (OutOfMemoryError oom)
/*      */     {
/*      */       try {
/* 3725 */         clearInputStream();
/*      */       }
/*      */       catch (Exception ex) {}
/*      */       try {
/* 3729 */         this.connection.realClose(false, false, true, oom);
/*      */       }
/*      */       catch (Exception ex) {}
/* 3732 */       throw oom;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private int readRemainingMultiPackets(Buffer reuse, byte multiPacketSeq)
/*      */     throws IOException, SQLException
/*      */   {
/* 3741 */     int lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */     
/*      */ 
/* 3744 */     if (lengthRead < 4) {
/* 3745 */       forceClose();
/* 3746 */       throw new IOException(Messages.getString("MysqlIO.47"));
/*      */     }
/*      */     
/* 3749 */     int packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */     
/*      */ 
/*      */ 
/* 3753 */     Buffer multiPacket = new Buffer(packetLength);
/* 3754 */     boolean firstMultiPkt = true;
/*      */     for (;;)
/*      */     {
/* 3757 */       if (!firstMultiPkt) {
/* 3758 */         lengthRead = readFully(this.mysqlInput, this.packetHeaderBuf, 0, 4);
/*      */         
/*      */ 
/* 3761 */         if (lengthRead < 4) {
/* 3762 */           forceClose();
/* 3763 */           throw new IOException(Messages.getString("MysqlIO.48"));
/*      */         }
/*      */         
/*      */ 
/* 3767 */         packetLength = (this.packetHeaderBuf[0] & 0xFF) + ((this.packetHeaderBuf[1] & 0xFF) << 8) + ((this.packetHeaderBuf[2] & 0xFF) << 16);
/*      */       }
/*      */       else
/*      */       {
/* 3771 */         firstMultiPkt = false;
/*      */       }
/*      */       
/* 3774 */       if ((!this.useNewLargePackets) && (packetLength == 1)) {
/* 3775 */         clearInputStream();
/*      */         
/* 3777 */         break; }
/* 3778 */       if (packetLength < this.maxThreeBytes) {
/* 3779 */         byte newPacketSeq = this.packetHeaderBuf[3];
/*      */         
/* 3781 */         if (newPacketSeq != multiPacketSeq + 1) {
/* 3782 */           throw new IOException(Messages.getString("MysqlIO.49"));
/*      */         }
/*      */         
/*      */ 
/* 3786 */         multiPacketSeq = newPacketSeq;
/*      */         
/*      */ 
/* 3789 */         multiPacket.setPosition(0);
/*      */         
/*      */ 
/* 3792 */         multiPacket.setBufLength(packetLength);
/*      */         
/*      */ 
/* 3795 */         byte[] byteBuf = multiPacket.getByteBuffer();
/* 3796 */         int lengthToWrite = packetLength;
/*      */         
/* 3798 */         int bytesRead = readFully(this.mysqlInput, byteBuf, 0, packetLength);
/*      */         
/*      */ 
/* 3801 */         if (bytesRead != lengthToWrite) {
/* 3802 */           throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.50") + lengthToWrite + Messages.getString("MysqlIO.51") + bytesRead + ".", getExceptionInterceptor()), getExceptionInterceptor());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3812 */         reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
/*      */         
/* 3814 */         break;
/*      */       }
/*      */       
/* 3817 */       byte newPacketSeq = this.packetHeaderBuf[3];
/*      */       
/* 3819 */       if (newPacketSeq != multiPacketSeq + 1) {
/* 3820 */         throw new IOException(Messages.getString("MysqlIO.53"));
/*      */       }
/*      */       
/*      */ 
/* 3824 */       multiPacketSeq = newPacketSeq;
/*      */       
/*      */ 
/* 3827 */       multiPacket.setPosition(0);
/*      */       
/*      */ 
/* 3830 */       multiPacket.setBufLength(packetLength);
/*      */       
/*      */ 
/* 3833 */       byte[] byteBuf = multiPacket.getByteBuffer();
/* 3834 */       int lengthToWrite = packetLength;
/*      */       
/* 3836 */       int bytesRead = readFully(this.mysqlInput, byteBuf, 0, packetLength);
/*      */       
/*      */ 
/* 3839 */       if (bytesRead != lengthToWrite) {
/* 3840 */         throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, SQLError.createSQLException(Messages.getString("MysqlIO.54") + lengthToWrite + Messages.getString("MysqlIO.55") + bytesRead + ".", getExceptionInterceptor()), getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3849 */       reuse.writeBytesNoNull(byteBuf, 0, lengthToWrite);
/*      */     }
/*      */     
/* 3852 */     reuse.setPosition(0);
/* 3853 */     reuse.setWasMultiPacket(true);
/* 3854 */     return packetLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkPacketSequencing(byte multiPacketSeq)
/*      */     throws SQLException
/*      */   {
/* 3863 */     if ((multiPacketSeq == Byte.MIN_VALUE) && (this.readPacketSequence != Byte.MAX_VALUE)) {
/* 3864 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -128, but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3870 */     if ((this.readPacketSequence == -1) && (multiPacketSeq != 0)) {
/* 3871 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # -1, but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 3877 */     if ((multiPacketSeq != Byte.MIN_VALUE) && (this.readPacketSequence != -1) && (multiPacketSeq != this.readPacketSequence + 1))
/*      */     {
/* 3879 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, new IOException("Packets out of order, expected packet # " + (this.readPacketSequence + 1) + ", but received packet # " + multiPacketSeq), getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void enableMultiQueries()
/*      */     throws SQLException
/*      */   {
/* 3888 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 3890 */     buf.clear();
/* 3891 */     buf.writeByte((byte)27);
/* 3892 */     buf.writeInt(0);
/* 3893 */     sendCommand(27, null, buf, false, null, 0);
/*      */   }
/*      */   
/*      */   void disableMultiQueries() throws SQLException {
/* 3897 */     Buffer buf = getSharedSendPacket();
/*      */     
/* 3899 */     buf.clear();
/* 3900 */     buf.writeByte((byte)27);
/* 3901 */     buf.writeInt(1);
/* 3902 */     sendCommand(27, null, buf, false, null, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void send(Buffer packet, int packetLen)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 3914 */       if ((this.maxAllowedPacket > 0) && (packetLen > this.maxAllowedPacket)) {
/* 3915 */         throw new PacketTooBigException(packetLen, this.maxAllowedPacket);
/*      */       }
/*      */       
/* 3918 */       if ((this.serverMajorVersion >= 4) && ((packetLen - 4 >= this.maxThreeBytes) || ((this.useCompression) && (packetLen - 4 >= this.maxThreeBytes - 3))))
/*      */       {
/*      */ 
/*      */ 
/* 3922 */         sendSplitPackets(packet, packetLen);
/*      */       }
/*      */       else {
/* 3925 */         this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */         
/* 3927 */         Buffer packetToSend = packet;
/* 3928 */         packetToSend.setPosition(0);
/* 3929 */         packetToSend.writeLongInt(packetLen - 4);
/* 3930 */         packetToSend.writeByte(this.packetSequence);
/*      */         
/* 3932 */         if (this.useCompression) {
/* 3933 */           this.compressedPacketSequence = ((byte)(this.compressedPacketSequence + 1));
/* 3934 */           int originalPacketLen = packetLen;
/*      */           
/* 3936 */           packetToSend = compressPacket(packetToSend, 0, packetLen);
/* 3937 */           packetLen = packetToSend.getPosition();
/*      */           
/* 3939 */           if (this.traceProtocol) {
/* 3940 */             StringBuffer traceMessageBuf = new StringBuffer();
/*      */             
/* 3942 */             traceMessageBuf.append(Messages.getString("MysqlIO.57"));
/* 3943 */             traceMessageBuf.append(getPacketDumpToLog(packetToSend, packetLen));
/*      */             
/* 3945 */             traceMessageBuf.append(Messages.getString("MysqlIO.58"));
/* 3946 */             traceMessageBuf.append(getPacketDumpToLog(packet, originalPacketLen));
/*      */             
/*      */ 
/* 3949 */             this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */           }
/*      */           
/*      */         }
/* 3953 */         else if (this.traceProtocol) {
/* 3954 */           StringBuffer traceMessageBuf = new StringBuffer();
/*      */           
/* 3956 */           traceMessageBuf.append(Messages.getString("MysqlIO.59"));
/* 3957 */           traceMessageBuf.append("host: '");
/* 3958 */           traceMessageBuf.append(this.host);
/* 3959 */           traceMessageBuf.append("' threadId: '");
/* 3960 */           traceMessageBuf.append(this.threadId);
/* 3961 */           traceMessageBuf.append("'\n");
/* 3962 */           traceMessageBuf.append(packetToSend.dump(packetLen));
/*      */           
/* 3964 */           this.connection.getLog().logTrace(traceMessageBuf.toString());
/*      */         }
/*      */         
/*      */ 
/* 3968 */         this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, packetLen);
/* 3969 */         this.mysqlOutput.flush();
/*      */       }
/*      */       
/* 3972 */       if (this.enablePacketDebug) {
/* 3973 */         enqueuePacketForDebugging(true, false, packetLen + 5, this.packetHeaderBuf, packet);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3980 */       if (packet == this.sharedSendPacket) {
/* 3981 */         reclaimLargeSharedSendPacket();
/*      */       }
/*      */       
/* 3984 */       if (this.connection.getMaintainTimeStats()) {
/* 3985 */         this.lastPacketSentTimeMs = System.currentTimeMillis();
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 3988 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ResultSetImpl sendFileToServer(StatementImpl callingStatement, String fileName)
/*      */     throws SQLException
/*      */   {
/* 4006 */     if (this.useCompression) {
/* 4007 */       this.compressedPacketSequence = ((byte)(this.compressedPacketSequence + 1));
/*      */     }
/*      */     
/* 4010 */     Buffer filePacket = this.loadFileBufRef == null ? null : (Buffer)this.loadFileBufRef.get();
/*      */     
/*      */ 
/* 4013 */     int bigPacketLength = Math.min(this.connection.getMaxAllowedPacket() - 12, alignPacketSize(this.connection.getMaxAllowedPacket() - 16, 4096) - 12);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4018 */     int oneMeg = 1048576;
/*      */     
/* 4020 */     int smallerPacketSizeAligned = Math.min(oneMeg - 12, alignPacketSize(oneMeg - 16, 4096) - 12);
/*      */     
/*      */ 
/* 4023 */     int packetLength = Math.min(smallerPacketSizeAligned, bigPacketLength);
/*      */     
/* 4025 */     if (filePacket == null) {
/*      */       try {
/* 4027 */         filePacket = new Buffer(packetLength + 4);
/* 4028 */         this.loadFileBufRef = new SoftReference(filePacket);
/*      */       } catch (OutOfMemoryError oom) {
/* 4030 */         throw SQLError.createSQLException("Could not allocate packet of " + packetLength + " bytes required for LOAD DATA LOCAL INFILE operation." + " Try increasing max heap allocation for JVM or decreasing server variable " + "'max_allowed_packet'", "S1001", getExceptionInterceptor());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4038 */     filePacket.clear();
/* 4039 */     send(filePacket, 0);
/*      */     
/* 4041 */     byte[] fileBuf = new byte[packetLength];
/*      */     
/* 4043 */     BufferedInputStream fileIn = null;
/*      */     try
/*      */     {
/* 4046 */       if (!this.connection.getAllowLoadLocalInfile()) {
/* 4047 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.LoadDataLocalNotAllowed"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4052 */       InputStream hookedStream = null;
/*      */       
/* 4054 */       if (callingStatement != null) {
/* 4055 */         hookedStream = callingStatement.getLocalInfileInputStream();
/*      */       }
/*      */       
/* 4058 */       if (hookedStream != null) {
/* 4059 */         fileIn = new BufferedInputStream(hookedStream);
/* 4060 */       } else if (!this.connection.getAllowUrlInLocalInfile()) {
/* 4061 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */ 
/*      */       }
/* 4064 */       else if (fileName.indexOf(':') != -1) {
/*      */         try {
/* 4066 */           URL urlFromFileName = new URL(fileName);
/* 4067 */           fileIn = new BufferedInputStream(urlFromFileName.openStream());
/*      */         }
/*      */         catch (MalformedURLException badUrlEx) {
/* 4070 */           fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */         }
/*      */         
/*      */       } else {
/* 4074 */         fileIn = new BufferedInputStream(new FileInputStream(fileName));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4079 */       int bytesRead = 0;
/*      */       
/* 4081 */       while ((bytesRead = fileIn.read(fileBuf)) != -1) {
/* 4082 */         filePacket.clear();
/* 4083 */         filePacket.writeBytesNoNull(fileBuf, 0, bytesRead);
/* 4084 */         send(filePacket, filePacket.getPosition());
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 4087 */       StringBuffer messageBuf = new StringBuffer(Messages.getString("MysqlIO.60"));
/*      */       
/*      */ 
/* 4090 */       if (!this.connection.getParanoid()) {
/* 4091 */         messageBuf.append("'");
/*      */         
/* 4093 */         if (fileName != null) {
/* 4094 */           messageBuf.append(fileName);
/*      */         }
/*      */         
/* 4097 */         messageBuf.append("'");
/*      */       }
/*      */       
/* 4100 */       messageBuf.append(Messages.getString("MysqlIO.63"));
/*      */       
/* 4102 */       if (!this.connection.getParanoid()) {
/* 4103 */         messageBuf.append(Messages.getString("MysqlIO.64"));
/* 4104 */         messageBuf.append(Util.stackTraceToString(ioEx));
/*      */       }
/*      */       
/* 4107 */       throw SQLError.createSQLException(messageBuf.toString(), "S1009", getExceptionInterceptor());
/*      */     }
/*      */     finally {
/* 4110 */       if (fileIn != null) {
/*      */         try {
/* 4112 */           fileIn.close();
/*      */         } catch (Exception ex) {
/* 4114 */           SQLException sqlEx = SQLError.createSQLException(Messages.getString("MysqlIO.65"), "S1000", getExceptionInterceptor());
/*      */           
/* 4116 */           sqlEx.initCause(ex);
/*      */           
/* 4118 */           throw sqlEx;
/*      */         }
/*      */         
/* 4121 */         fileIn = null;
/*      */       }
/*      */       else {
/* 4124 */         filePacket.clear();
/* 4125 */         send(filePacket, filePacket.getPosition());
/* 4126 */         checkErrorPacket();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4131 */     filePacket.clear();
/* 4132 */     send(filePacket, filePacket.getPosition());
/*      */     
/* 4134 */     Buffer resultPacket = checkErrorPacket();
/*      */     
/* 4136 */     return buildResultSetWithUpdates(callingStatement, resultPacket);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Buffer checkErrorPacket(int command)
/*      */     throws SQLException
/*      */   {
/* 4152 */     Buffer resultPacket = null;
/* 4153 */     this.serverStatus = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/* 4160 */       resultPacket = reuseAndReadPacket(this.reusablePacket);
/*      */     }
/*      */     catch (SQLException sqlEx) {
/* 4163 */       throw sqlEx;
/*      */     } catch (Exception fallThru) {
/* 4165 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, fallThru, getExceptionInterceptor());
/*      */     }
/*      */     
/*      */ 
/* 4169 */     checkErrorPacket(resultPacket);
/*      */     
/* 4171 */     return resultPacket;
/*      */   }
/*      */   
/*      */   private void checkErrorPacket(Buffer resultPacket) throws SQLException
/*      */   {
/* 4176 */     int statusCode = resultPacket.readByte();
/*      */     
/*      */ 
/* 4179 */     if (statusCode == -1)
/*      */     {
/* 4181 */       int errno = 2000;
/*      */       
/* 4183 */       if (this.protocolVersion > 9) {
/* 4184 */         errno = resultPacket.readInt();
/*      */         
/* 4186 */         String xOpen = null;
/*      */         
/* 4188 */         String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */         
/*      */ 
/* 4191 */         if (serverErrorMessage.charAt(0) == '#')
/*      */         {
/*      */ 
/* 4194 */           if (serverErrorMessage.length() > 6) {
/* 4195 */             xOpen = serverErrorMessage.substring(1, 6);
/* 4196 */             serverErrorMessage = serverErrorMessage.substring(6);
/*      */             
/* 4198 */             if (xOpen.equals("HY000")) {
/* 4199 */               xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */             }
/*      */           }
/*      */           else {
/* 4203 */             xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */           }
/*      */         }
/*      */         else {
/* 4207 */           xOpen = SQLError.mysqlToSqlState(errno, this.connection.getUseSqlStateCodes());
/*      */         }
/*      */         
/*      */ 
/* 4211 */         clearInputStream();
/*      */         
/* 4213 */         StringBuffer errorBuf = new StringBuffer();
/*      */         
/* 4215 */         String xOpenErrorMessage = SQLError.get(xOpen);
/*      */         
/* 4217 */         if ((!this.connection.getUseOnlyServerErrorMessages()) && 
/* 4218 */           (xOpenErrorMessage != null)) {
/* 4219 */           errorBuf.append(xOpenErrorMessage);
/* 4220 */           errorBuf.append(Messages.getString("MysqlIO.68"));
/*      */         }
/*      */         
/*      */ 
/* 4224 */         errorBuf.append(serverErrorMessage);
/*      */         
/* 4226 */         if ((!this.connection.getUseOnlyServerErrorMessages()) && 
/* 4227 */           (xOpenErrorMessage != null)) {
/* 4228 */           errorBuf.append("\"");
/*      */         }
/*      */         
/*      */ 
/* 4232 */         appendDeadlockStatusInformation(xOpen, errorBuf);
/*      */         
/* 4234 */         if ((xOpen != null) && (xOpen.startsWith("22"))) {
/* 4235 */           throw new MysqlDataTruncation(errorBuf.toString(), 0, true, false, 0, 0, errno);
/*      */         }
/* 4237 */         throw SQLError.createSQLException(errorBuf.toString(), xOpen, errno, false, getExceptionInterceptor(), this.connection);
/*      */       }
/*      */       
/*      */ 
/* 4241 */       String serverErrorMessage = resultPacket.readString(this.connection.getErrorMessageEncoding(), getExceptionInterceptor());
/*      */       
/* 4243 */       clearInputStream();
/*      */       
/* 4245 */       if (serverErrorMessage.indexOf(Messages.getString("MysqlIO.70")) != -1) {
/* 4246 */         throw SQLError.createSQLException(SQLError.get("S0022") + ", " + serverErrorMessage, "S0022", -1, false, getExceptionInterceptor(), this.connection);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4253 */       StringBuffer errorBuf = new StringBuffer(Messages.getString("MysqlIO.72"));
/*      */       
/* 4255 */       errorBuf.append(serverErrorMessage);
/* 4256 */       errorBuf.append("\"");
/*      */       
/* 4258 */       throw SQLError.createSQLException(SQLError.get("S1000") + ", " + errorBuf.toString(), "S1000", -1, false, getExceptionInterceptor(), this.connection);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void appendDeadlockStatusInformation(String xOpen, StringBuffer errorBuf)
/*      */     throws SQLException
/*      */   {
/* 4266 */     if ((this.connection.getIncludeInnodbStatusInDeadlockExceptions()) && (xOpen != null) && ((xOpen.startsWith("40")) || (xOpen.startsWith("41"))) && (this.streamingData == null))
/*      */     {
/*      */ 
/*      */ 
/* 4270 */       ResultSet rs = null;
/*      */       try
/*      */       {
/* 4273 */         rs = sqlQueryDirect(null, "SHOW ENGINE INNODB STATUS", this.connection.getEncoding(), null, -1, 1003, 1007, false, this.connection.getCatalog(), null);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4279 */         if (rs.next()) {
/* 4280 */           errorBuf.append("\n\n");
/* 4281 */           errorBuf.append(rs.getString("Status"));
/*      */         } else {
/* 4283 */           errorBuf.append("\n\n");
/* 4284 */           errorBuf.append(Messages.getString("MysqlIO.NoInnoDBStatusFound"));
/*      */         }
/*      */       }
/*      */       catch (Exception ex) {
/* 4288 */         errorBuf.append("\n\n");
/* 4289 */         errorBuf.append(Messages.getString("MysqlIO.InnoDBStatusFailed"));
/*      */         
/* 4291 */         errorBuf.append("\n\n");
/* 4292 */         errorBuf.append(Util.stackTraceToString(ex));
/*      */       } finally {
/* 4294 */         if (rs != null) {
/* 4295 */           rs.close();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 4300 */     if (this.connection.getIncludeThreadDumpInDeadlockExceptions()) {
/* 4301 */       errorBuf.append("\n\n*** Java threads running at time of deadlock ***\n\n");
/*      */       
/* 4303 */       ThreadMXBean threadMBean = ManagementFactory.getThreadMXBean();
/* 4304 */       long[] threadIds = threadMBean.getAllThreadIds();
/*      */       
/* 4306 */       ThreadInfo[] threads = threadMBean.getThreadInfo(threadIds, Integer.MAX_VALUE);
/*      */       
/* 4308 */       Object activeThreads = new ArrayList();
/*      */       
/* 4310 */       for (ThreadInfo info : threads) {
/* 4311 */         if (info != null) {
/* 4312 */           ((List)activeThreads).add(info);
/*      */         }
/*      */       }
/*      */       
/* 4316 */       for (ThreadInfo threadInfo : (List)activeThreads)
/*      */       {
/*      */ 
/*      */ 
/* 4320 */         errorBuf.append('"');
/* 4321 */         errorBuf.append(threadInfo.getThreadName());
/* 4322 */         errorBuf.append("\" tid=");
/* 4323 */         errorBuf.append(threadInfo.getThreadId());
/* 4324 */         errorBuf.append(" ");
/* 4325 */         errorBuf.append(threadInfo.getThreadState());
/*      */         
/* 4327 */         if (threadInfo.getLockName() != null) {
/* 4328 */           errorBuf.append(" on lock=" + threadInfo.getLockName());
/*      */         }
/* 4330 */         if (threadInfo.isSuspended()) {
/* 4331 */           errorBuf.append(" (suspended)");
/*      */         }
/* 4333 */         if (threadInfo.isInNative()) {
/* 4334 */           errorBuf.append(" (running in native)");
/*      */         }
/*      */         
/* 4337 */         StackTraceElement[] stackTrace = threadInfo.getStackTrace();
/*      */         
/* 4339 */         if (stackTrace.length > 0) {
/* 4340 */           errorBuf.append(" in ");
/* 4341 */           errorBuf.append(stackTrace[0].getClassName());
/* 4342 */           errorBuf.append(".");
/* 4343 */           errorBuf.append(stackTrace[0].getMethodName());
/* 4344 */           errorBuf.append("()");
/*      */         }
/*      */         
/* 4347 */         errorBuf.append("\n");
/*      */         
/* 4349 */         if (threadInfo.getLockOwnerName() != null) {
/* 4350 */           errorBuf.append("\t owned by " + threadInfo.getLockOwnerName() + " Id=" + threadInfo.getLockOwnerId());
/*      */           
/* 4352 */           errorBuf.append("\n");
/*      */         }
/*      */         
/* 4355 */         for (int j = 0; j < stackTrace.length; j++) {
/* 4356 */           StackTraceElement ste = stackTrace[j];
/* 4357 */           errorBuf.append("\tat " + ste.toString());
/* 4358 */           errorBuf.append("\n");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void sendSplitPackets(Buffer packet, int packetLen)
/*      */     throws SQLException
/*      */   {
/*      */     try
/*      */     {
/* 4375 */       Buffer packetToSend = this.splitBufRef == null ? null : (Buffer)this.splitBufRef.get();
/* 4376 */       Buffer toCompress = (!this.useCompression) || (this.compressBufRef == null) ? null : (Buffer)this.compressBufRef.get();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4383 */       if (packetToSend == null) {
/* 4384 */         packetToSend = new Buffer(this.maxThreeBytes + 4);
/* 4385 */         this.splitBufRef = new SoftReference(packetToSend);
/*      */       }
/* 4387 */       if (this.useCompression) {
/* 4388 */         int cbuflen = packetLen + (packetLen / this.maxThreeBytes + 1) * 4;
/* 4389 */         if (toCompress == null) {
/* 4390 */           toCompress = new Buffer(cbuflen);
/* 4391 */         } else if (toCompress.getBufLength() < cbuflen) {
/* 4392 */           toCompress.setPosition(toCompress.getBufLength());
/* 4393 */           toCompress.ensureCapacity(cbuflen - toCompress.getBufLength());
/*      */         }
/*      */       }
/*      */       
/* 4397 */       int len = packetLen - 4;
/* 4398 */       int splitSize = this.maxThreeBytes;
/* 4399 */       int originalPacketPos = 4;
/* 4400 */       byte[] origPacketBytes = packet.getByteBuffer();
/*      */       
/* 4402 */       int toCompressPosition = 0;
/*      */       
/*      */ 
/* 4405 */       while (len >= 0) {
/* 4406 */         this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */         
/* 4408 */         if (len < splitSize) {
/* 4409 */           splitSize = len;
/*      */         }
/*      */         
/* 4412 */         packetToSend.setPosition(0);
/* 4413 */         packetToSend.writeLongInt(splitSize);
/* 4414 */         packetToSend.writeByte(this.packetSequence);
/* 4415 */         if (len > 0) {
/* 4416 */           System.arraycopy(origPacketBytes, originalPacketPos, packetToSend.getByteBuffer(), 4, splitSize);
/*      */         }
/*      */         
/* 4419 */         if (this.useCompression) {
/* 4420 */           System.arraycopy(packetToSend.getByteBuffer(), 0, toCompress.getByteBuffer(), toCompressPosition, 4 + splitSize);
/* 4421 */           toCompressPosition += 4 + splitSize;
/*      */         } else {
/* 4423 */           this.mysqlOutput.write(packetToSend.getByteBuffer(), 0, 4 + splitSize);
/* 4424 */           this.mysqlOutput.flush();
/*      */         }
/*      */         
/* 4427 */         originalPacketPos += splitSize;
/* 4428 */         len -= this.maxThreeBytes;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4433 */       if (this.useCompression) {
/* 4434 */         len = toCompressPosition;
/* 4435 */         toCompressPosition = 0;
/* 4436 */         splitSize = this.maxThreeBytes - 3;
/* 4437 */         while (len >= 0) {
/* 4438 */           this.compressedPacketSequence = ((byte)(this.compressedPacketSequence + 1));
/*      */           
/* 4440 */           if (len < splitSize) {
/* 4441 */             splitSize = len;
/*      */           }
/*      */           
/* 4444 */           Buffer compressedPacketToSend = compressPacket(toCompress, toCompressPosition, splitSize);
/* 4445 */           packetLen = compressedPacketToSend.getPosition();
/* 4446 */           this.mysqlOutput.write(compressedPacketToSend.getByteBuffer(), 0, packetLen);
/* 4447 */           this.mysqlOutput.flush();
/*      */           
/*      */ 
/* 4450 */           toCompressPosition += splitSize;
/* 4451 */           len -= this.maxThreeBytes - 3;
/*      */         }
/*      */       }
/*      */     } catch (IOException ioEx) {
/* 4455 */       throw SQLError.createCommunicationsException(this.connection, this.lastPacketSentTimeMs, this.lastPacketReceivedTimeMs, ioEx, getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */   
/*      */   private void reclaimLargeSharedSendPacket()
/*      */   {
/* 4461 */     if ((this.sharedSendPacket != null) && (this.sharedSendPacket.getCapacity() > 1048576))
/*      */     {
/* 4463 */       this.sharedSendPacket = new Buffer(1024);
/*      */     }
/*      */   }
/*      */   
/*      */   boolean hadWarnings() {
/* 4468 */     return this.hadWarnings;
/*      */   }
/*      */   
/*      */   void scanForAndThrowDataTruncation() throws SQLException {
/* 4472 */     if ((this.streamingData == null) && (versionMeetsMinimum(4, 1, 0)) && (this.connection.getJdbcCompliantTruncation()) && (this.warningCount > 0))
/*      */     {
/* 4474 */       SQLError.convertShowWarningsToSQLWarnings(this.connection, this.warningCount, true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void secureAuth(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams)
/*      */     throws SQLException
/*      */   {
/* 4495 */     if (packet == null) {
/* 4496 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 4499 */     if (writeClientParams) {
/* 4500 */       if (this.use41Extensions) {
/* 4501 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 4502 */           packet.writeLong(this.clientParam);
/* 4503 */           packet.writeLong(this.maxThreeBytes);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 4508 */           packet.writeByte((byte)8);
/*      */           
/*      */ 
/* 4511 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 4513 */           packet.writeLong(this.clientParam);
/* 4514 */           packet.writeLong(this.maxThreeBytes);
/*      */         }
/*      */       } else {
/* 4517 */         packet.writeInt((int)this.clientParam);
/* 4518 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4523 */     packet.writeString(user, "Cp1252", this.connection);
/*      */     
/* 4525 */     if (password.length() != 0)
/*      */     {
/* 4527 */       packet.writeString("xxxxxxxx", "Cp1252", this.connection);
/*      */     }
/*      */     else {
/* 4530 */       packet.writeString("", "Cp1252", this.connection);
/*      */     }
/*      */     
/* 4533 */     if (this.useConnectWithDb) {
/* 4534 */       packet.writeString(database, "Cp1252", this.connection);
/*      */     }
/*      */     
/* 4537 */     send(packet, packet.getPosition());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 4542 */     if (password.length() > 0) {
/* 4543 */       Buffer b = readPacket();
/*      */       
/* 4545 */       b.setPosition(0);
/*      */       
/* 4547 */       byte[] replyAsBytes = b.getByteBuffer();
/*      */       
/* 4549 */       if ((replyAsBytes.length == 25) && (replyAsBytes[0] != 0))
/*      */       {
/* 4551 */         if (replyAsBytes[0] != 42) {
/*      */           try
/*      */           {
/* 4554 */             byte[] buff = Security.passwordHashStage1(password);
/*      */             
/*      */ 
/* 4557 */             byte[] passwordHash = new byte[buff.length];
/* 4558 */             System.arraycopy(buff, 0, passwordHash, 0, buff.length);
/*      */             
/*      */ 
/* 4561 */             passwordHash = Security.passwordHashStage2(passwordHash, replyAsBytes);
/*      */             
/*      */ 
/* 4564 */             byte[] packetDataAfterSalt = new byte[replyAsBytes.length - 5];
/*      */             
/*      */ 
/* 4567 */             System.arraycopy(replyAsBytes, 4, packetDataAfterSalt, 0, replyAsBytes.length - 5);
/*      */             
/*      */ 
/* 4570 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */             
/*      */ 
/* 4573 */             Security.passwordCrypt(packetDataAfterSalt, mysqlScrambleBuff, passwordHash, 20);
/*      */             
/*      */ 
/*      */ 
/* 4577 */             Security.passwordCrypt(mysqlScrambleBuff, buff, buff, 20);
/*      */             
/* 4579 */             Buffer packet2 = new Buffer(25);
/* 4580 */             packet2.writeBytesNoNull(buff);
/*      */             
/* 4582 */             this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */             
/* 4584 */             send(packet2, 24);
/*      */           } catch (NoSuchAlgorithmException nse) {
/* 4586 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.91") + Messages.getString("MysqlIO.92"), "S1000", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */         }
/*      */         else {
/*      */           try
/*      */           {
/* 4593 */             byte[] passwordHash = Security.createKeyFromOldPassword(password);
/*      */             
/*      */ 
/* 4596 */             byte[] netReadPos4 = new byte[replyAsBytes.length - 5];
/*      */             
/* 4598 */             System.arraycopy(replyAsBytes, 4, netReadPos4, 0, replyAsBytes.length - 5);
/*      */             
/*      */ 
/* 4601 */             byte[] mysqlScrambleBuff = new byte[20];
/*      */             
/*      */ 
/* 4604 */             Security.passwordCrypt(netReadPos4, mysqlScrambleBuff, passwordHash, 20);
/*      */             
/*      */ 
/*      */ 
/* 4608 */             String scrambledPassword = Util.scramble(StringUtils.toString(mysqlScrambleBuff), password);
/*      */             
/*      */ 
/* 4611 */             Buffer packet2 = new Buffer(packLength);
/* 4612 */             packet2.writeString(scrambledPassword, "Cp1252", this.connection);
/* 4613 */             this.packetSequence = ((byte)(this.packetSequence + 1));
/*      */             
/* 4615 */             send(packet2, 24);
/*      */           } catch (NoSuchAlgorithmException nse) {
/* 4617 */             throw SQLError.createSQLException(Messages.getString("MysqlIO.93") + Messages.getString("MysqlIO.94"), "S1000", getExceptionInterceptor());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void secureAuth411(Buffer packet, int packLength, String user, String password, String database, boolean writeClientParams)
/*      */     throws SQLException
/*      */   {
/* 4659 */     if (packet == null) {
/* 4660 */       packet = new Buffer(packLength);
/*      */     }
/*      */     
/* 4663 */     if (writeClientParams) {
/* 4664 */       if (this.use41Extensions) {
/* 4665 */         if (versionMeetsMinimum(4, 1, 1)) {
/* 4666 */           packet.writeLong(this.clientParam);
/* 4667 */           packet.writeLong(this.maxThreeBytes);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 4672 */           packet.writeByte((byte)33);
/*      */           
/*      */ 
/* 4675 */           packet.writeBytesNoNull(new byte[23]);
/*      */         } else {
/* 4677 */           packet.writeLong(this.clientParam);
/* 4678 */           packet.writeLong(this.maxThreeBytes);
/*      */         }
/*      */       } else {
/* 4681 */         packet.writeInt((int)this.clientParam);
/* 4682 */         packet.writeLongInt(this.maxThreeBytes);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4687 */     packet.writeString(user, "utf-8", this.connection);
/*      */     
/* 4689 */     if (password.length() != 0) {
/* 4690 */       packet.writeByte((byte)20);
/*      */       try
/*      */       {
/* 4693 */         packet.writeBytesNoNull(Security.scramble411(password, this.seed, this.connection));
/*      */       } catch (NoSuchAlgorithmException nse) {
/* 4695 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */       catch (UnsupportedEncodingException e)
/*      */       {
/* 4699 */         throw SQLError.createSQLException(Messages.getString("MysqlIO.95") + Messages.getString("MysqlIO.96"), "S1000", getExceptionInterceptor());
/*      */       }
/*      */       
/*      */     }
/*      */     else
/*      */     {
/* 4705 */       packet.writeByte((byte)0);
/*      */     }
/*      */     
/* 4708 */     if (this.useConnectWithDb) {
/* 4709 */       packet.writeString(database, "utf-8", this.connection);
/*      */     }
/*      */     else {
/* 4712 */       packet.writeByte((byte)0);
/*      */     }
/*      */     
/* 4715 */     if ((this.serverCapabilities & 0x200) != 0)
/*      */     {
/* 4717 */       String mysqlEncodingName = this.connection.getServerCharacterEncoding();
/* 4718 */       if (("ucs2".equalsIgnoreCase(mysqlEncodingName)) || ("utf16".equalsIgnoreCase(mysqlEncodingName)) || ("utf16le".equalsIgnoreCase(mysqlEncodingName)) || ("uft32".equalsIgnoreCase(mysqlEncodingName)))
/*      */       {
/*      */ 
/*      */ 
/* 4722 */         packet.writeByte((byte)33);
/* 4723 */         packet.writeByte((byte)0);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 4728 */     if ((this.serverCapabilities & 0x100000) != 0) {
/* 4729 */       sendConnectionAttributes(packet, "utf-8", this.connection);
/*      */     }
/*      */     
/* 4732 */     send(packet, packet.getPosition());
/*      */     
/* 4734 */     byte savePacketSequence = this.packetSequence++;
/*      */     
/* 4736 */     Buffer reply = checkErrorPacket();
/*      */     
/* 4738 */     if (reply.isLastDataPacket())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/* 4743 */       savePacketSequence = (byte)(savePacketSequence + 1);this.packetSequence = savePacketSequence;
/* 4744 */       packet.clear();
/*      */       
/* 4746 */       String seed323 = this.seed.substring(0, 8);
/* 4747 */       packet.writeString(Util.newCrypt(password, seed323));
/* 4748 */       send(packet, packet.getPosition());
/*      */       
/*      */ 
/* 4751 */       checkErrorPacket();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final ResultSetRow unpackBinaryResultSetRow(Field[] fields, Buffer binaryData, int resultSetConcurrency)
/*      */     throws SQLException
/*      */   {
/* 4768 */     int numFields = fields.length;
/*      */     
/* 4770 */     byte[][] unpackedRowData = new byte[numFields][];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4777 */     int nullCount = (numFields + 9) / 8;
/*      */     
/* 4779 */     byte[] nullBitMask = new byte[nullCount];
/*      */     
/* 4781 */     for (int i = 0; i < nullCount; i++) {
/* 4782 */       nullBitMask[i] = binaryData.readByte();
/*      */     }
/*      */     
/* 4785 */     int nullMaskPos = 0;
/* 4786 */     int bit = 4;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 4793 */     for (int i = 0; i < numFields; i++) {
/* 4794 */       if ((nullBitMask[nullMaskPos] & bit) != 0) {
/* 4795 */         unpackedRowData[i] = null;
/*      */       }
/* 4797 */       else if (resultSetConcurrency != 1008) {
/* 4798 */         extractNativeEncodedColumn(binaryData, fields, i, unpackedRowData);
/*      */       }
/*      */       else {
/* 4801 */         unpackNativeEncodedColumn(binaryData, fields, i, unpackedRowData);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 4806 */       if ((bit <<= 1 & 0xFF) == 0) {
/* 4807 */         bit = 1;
/*      */         
/* 4809 */         nullMaskPos++;
/*      */       }
/*      */     }
/*      */     
/* 4813 */     return new ByteArrayRow(unpackedRowData, getExceptionInterceptor());
/*      */   }
/*      */   
/*      */   private final void extractNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData)
/*      */     throws SQLException
/*      */   {
/* 4819 */     Field curField = fields[columnIndex];
/*      */     int length;
/* 4821 */     switch (curField.getMysqlType())
/*      */     {
/*      */     case 6: 
/*      */       break;
/*      */     
/*      */     case 1: 
/* 4827 */       unpackedRowData[columnIndex] = { binaryData.readByte() };
/* 4828 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 13: 
/* 4833 */       unpackedRowData[columnIndex] = binaryData.getBytes(2);
/* 4834 */       break;
/*      */     
/*      */     case 3: 
/*      */     case 9: 
/* 4838 */       unpackedRowData[columnIndex] = binaryData.getBytes(4);
/* 4839 */       break;
/*      */     
/*      */     case 8: 
/* 4842 */       unpackedRowData[columnIndex] = binaryData.getBytes(8);
/* 4843 */       break;
/*      */     
/*      */     case 4: 
/* 4846 */       unpackedRowData[columnIndex] = binaryData.getBytes(4);
/* 4847 */       break;
/*      */     
/*      */     case 5: 
/* 4850 */       unpackedRowData[columnIndex] = binaryData.getBytes(8);
/* 4851 */       break;
/*      */     
/*      */     case 11: 
/* 4854 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 4856 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */       
/* 4858 */       break;
/*      */     
/*      */     case 10: 
/* 4861 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 4863 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/*      */       
/* 4865 */       break;
/*      */     case 7: 
/*      */     case 12: 
/* 4868 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 4870 */       unpackedRowData[columnIndex] = binaryData.getBytes(length);
/* 4871 */       break;
/*      */     case 0: 
/*      */     case 15: 
/*      */     case 246: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/*      */     case 253: 
/*      */     case 254: 
/*      */     case 255: 
/* 4882 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 4884 */       break;
/*      */     case 16: 
/* 4886 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 4888 */       break;
/*      */     default: 
/* 4890 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void unpackNativeEncodedColumn(Buffer binaryData, Field[] fields, int columnIndex, byte[][] unpackedRowData)
/*      */     throws SQLException
/*      */   {
/* 4902 */     Field curField = fields[columnIndex];
/*      */     int length;
/* 4904 */     int hour; int minute; int seconds; int year; int month; int day; int after1000; int after100; switch (curField.getMysqlType())
/*      */     {
/*      */     case 6: 
/*      */       break;
/*      */     
/*      */     case 1: 
/* 4910 */       byte tinyVal = binaryData.readByte();
/*      */       
/* 4912 */       if (!curField.isUnsigned()) {
/* 4913 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(tinyVal));
/*      */       }
/*      */       else {
/* 4916 */         short unsignedTinyVal = (short)(tinyVal & 0xFF);
/*      */         
/* 4918 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(unsignedTinyVal));
/*      */       }
/*      */       
/*      */ 
/* 4922 */       break;
/*      */     
/*      */ 
/*      */     case 2: 
/*      */     case 13: 
/* 4927 */       short shortVal = (short)binaryData.readInt();
/*      */       
/* 4929 */       if (!curField.isUnsigned()) {
/* 4930 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(shortVal));
/*      */       }
/*      */       else {
/* 4933 */         int unsignedShortVal = shortVal & 0xFFFF;
/*      */         
/* 4935 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(unsignedShortVal));
/*      */       }
/*      */       
/*      */ 
/* 4939 */       break;
/*      */     
/*      */ 
/*      */     case 3: 
/*      */     case 9: 
/* 4944 */       int intVal = (int)binaryData.readLong();
/*      */       
/* 4946 */       if (!curField.isUnsigned()) {
/* 4947 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(intVal));
/*      */       }
/*      */       else {
/* 4950 */         long longVal = intVal & 0xFFFFFFFF;
/*      */         
/* 4952 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(longVal));
/*      */       }
/*      */       
/*      */ 
/* 4956 */       break;
/*      */     
/*      */ 
/*      */     case 8: 
/* 4960 */       long longVal = binaryData.readLongLong();
/*      */       
/* 4962 */       if (!curField.isUnsigned()) {
/* 4963 */         unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(longVal));
/*      */       }
/*      */       else {
/* 4966 */         BigInteger asBigInteger = ResultSetImpl.convertLongToUlong(longVal);
/*      */         
/* 4968 */         unpackedRowData[columnIndex] = StringUtils.getBytes(asBigInteger.toString());
/*      */       }
/*      */       
/*      */ 
/* 4972 */       break;
/*      */     
/*      */ 
/*      */     case 4: 
/* 4976 */       float floatVal = Float.intBitsToFloat(binaryData.readIntAsLong());
/*      */       
/* 4978 */       unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(floatVal));
/*      */       
/*      */ 
/* 4981 */       break;
/*      */     
/*      */ 
/*      */     case 5: 
/* 4985 */       double doubleVal = Double.longBitsToDouble(binaryData.readLongLong());
/*      */       
/* 4987 */       unpackedRowData[columnIndex] = StringUtils.getBytes(String.valueOf(doubleVal));
/*      */       
/*      */ 
/* 4990 */       break;
/*      */     
/*      */ 
/*      */     case 11: 
/* 4994 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 4996 */       hour = 0;
/* 4997 */       minute = 0;
/* 4998 */       seconds = 0;
/*      */       
/* 5000 */       if (length != 0) {
/* 5001 */         binaryData.readByte();
/* 5002 */         binaryData.readLong();
/* 5003 */         hour = binaryData.readByte();
/* 5004 */         minute = binaryData.readByte();
/* 5005 */         seconds = binaryData.readByte();
/*      */         
/* 5007 */         if (length > 8) {
/* 5008 */           binaryData.readLong();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/* 5013 */       byte[] timeAsBytes = new byte[8];
/*      */       
/* 5015 */       timeAsBytes[0] = ((byte)Character.forDigit(hour / 10, 10));
/* 5016 */       timeAsBytes[1] = ((byte)Character.forDigit(hour % 10, 10));
/*      */       
/* 5018 */       timeAsBytes[2] = 58;
/*      */       
/* 5020 */       timeAsBytes[3] = ((byte)Character.forDigit(minute / 10, 10));
/*      */       
/* 5022 */       timeAsBytes[4] = ((byte)Character.forDigit(minute % 10, 10));
/*      */       
/*      */ 
/* 5025 */       timeAsBytes[5] = 58;
/*      */       
/* 5027 */       timeAsBytes[6] = ((byte)Character.forDigit(seconds / 10, 10));
/*      */       
/* 5029 */       timeAsBytes[7] = ((byte)Character.forDigit(seconds % 10, 10));
/*      */       
/*      */ 
/* 5032 */       unpackedRowData[columnIndex] = timeAsBytes;
/*      */       
/*      */ 
/* 5035 */       break;
/*      */     
/*      */     case 10: 
/* 5038 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 5040 */       year = 0;
/* 5041 */       month = 0;
/* 5042 */       day = 0;
/*      */       
/* 5044 */       hour = 0;
/* 5045 */       minute = 0;
/* 5046 */       seconds = 0;
/*      */       
/* 5048 */       if (length != 0) {
/* 5049 */         year = binaryData.readInt();
/* 5050 */         month = binaryData.readByte();
/* 5051 */         day = binaryData.readByte();
/*      */       }
/*      */       
/* 5054 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 5055 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 5057 */           unpackedRowData[columnIndex] = null;
/*      */         }
/*      */         else {
/* 5060 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 5062 */             throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Date", "S1009", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/* 5066 */           year = 1;
/* 5067 */           month = 1;
/* 5068 */           day = 1;
/*      */         }
/*      */       }
/*      */       else {
/* 5072 */         byte[] dateAsBytes = new byte[10];
/*      */         
/* 5074 */         dateAsBytes[0] = ((byte)Character.forDigit(year / 1000, 10));
/*      */         
/*      */ 
/* 5077 */         after1000 = year % 1000;
/*      */         
/* 5079 */         dateAsBytes[1] = ((byte)Character.forDigit(after1000 / 100, 10));
/*      */         
/*      */ 
/* 5082 */         after100 = after1000 % 100;
/*      */         
/* 5084 */         dateAsBytes[2] = ((byte)Character.forDigit(after100 / 10, 10));
/*      */         
/* 5086 */         dateAsBytes[3] = ((byte)Character.forDigit(after100 % 10, 10));
/*      */         
/*      */ 
/* 5089 */         dateAsBytes[4] = 45;
/*      */         
/* 5091 */         dateAsBytes[5] = ((byte)Character.forDigit(month / 10, 10));
/*      */         
/* 5093 */         dateAsBytes[6] = ((byte)Character.forDigit(month % 10, 10));
/*      */         
/*      */ 
/* 5096 */         dateAsBytes[7] = 45;
/*      */         
/* 5098 */         dateAsBytes[8] = ((byte)Character.forDigit(day / 10, 10));
/* 5099 */         dateAsBytes[9] = ((byte)Character.forDigit(day % 10, 10));
/*      */         
/* 5101 */         unpackedRowData[columnIndex] = dateAsBytes;
/*      */       }
/*      */       
/* 5104 */       break;
/*      */     
/*      */     case 7: 
/*      */     case 12: 
/* 5108 */       length = (int)binaryData.readFieldLength();
/*      */       
/* 5110 */       year = 0;
/* 5111 */       month = 0;
/* 5112 */       day = 0;
/*      */       
/* 5114 */       hour = 0;
/* 5115 */       minute = 0;
/* 5116 */       seconds = 0;
/*      */       
/* 5118 */       int nanos = 0;
/*      */       
/* 5120 */       if (length != 0) {
/* 5121 */         year = binaryData.readInt();
/* 5122 */         month = binaryData.readByte();
/* 5123 */         day = binaryData.readByte();
/*      */         
/* 5125 */         if (length > 4) {
/* 5126 */           hour = binaryData.readByte();
/* 5127 */           minute = binaryData.readByte();
/* 5128 */           seconds = binaryData.readByte();
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 5136 */       if ((year == 0) && (month == 0) && (day == 0)) {
/* 5137 */         if ("convertToNull".equals(this.connection.getZeroDateTimeBehavior()))
/*      */         {
/* 5139 */           unpackedRowData[columnIndex] = null;
/*      */         }
/*      */         else {
/* 5142 */           if ("exception".equals(this.connection.getZeroDateTimeBehavior()))
/*      */           {
/* 5144 */             throw SQLError.createSQLException("Value '0000-00-00' can not be represented as java.sql.Timestamp", "S1009", getExceptionInterceptor());
/*      */           }
/*      */           
/*      */ 
/* 5148 */           year = 1;
/* 5149 */           month = 1;
/* 5150 */           day = 1;
/*      */         }
/*      */       }
/*      */       else {
/* 5154 */         int stringLength = 19;
/*      */         
/* 5156 */         byte[] nanosAsBytes = StringUtils.getBytes(Integer.toString(nanos));
/*      */         
/* 5158 */         stringLength += 1 + nanosAsBytes.length;
/*      */         
/* 5160 */         byte[] datetimeAsBytes = new byte[stringLength];
/*      */         
/* 5162 */         datetimeAsBytes[0] = ((byte)Character.forDigit(year / 1000, 10));
/*      */         
/*      */ 
/* 5165 */         after1000 = year % 1000;
/*      */         
/* 5167 */         datetimeAsBytes[1] = ((byte)Character.forDigit(after1000 / 100, 10));
/*      */         
/*      */ 
/* 5170 */         after100 = after1000 % 100;
/*      */         
/* 5172 */         datetimeAsBytes[2] = ((byte)Character.forDigit(after100 / 10, 10));
/*      */         
/* 5174 */         datetimeAsBytes[3] = ((byte)Character.forDigit(after100 % 10, 10));
/*      */         
/*      */ 
/* 5177 */         datetimeAsBytes[4] = 45;
/*      */         
/* 5179 */         datetimeAsBytes[5] = ((byte)Character.forDigit(month / 10, 10));
/*      */         
/* 5181 */         datetimeAsBytes[6] = ((byte)Character.forDigit(month % 10, 10));
/*      */         
/*      */ 
/* 5184 */         datetimeAsBytes[7] = 45;
/*      */         
/* 5186 */         datetimeAsBytes[8] = ((byte)Character.forDigit(day / 10, 10));
/*      */         
/* 5188 */         datetimeAsBytes[9] = ((byte)Character.forDigit(day % 10, 10));
/*      */         
/*      */ 
/* 5191 */         datetimeAsBytes[10] = 32;
/*      */         
/* 5193 */         datetimeAsBytes[11] = ((byte)Character.forDigit(hour / 10, 10));
/*      */         
/* 5195 */         datetimeAsBytes[12] = ((byte)Character.forDigit(hour % 10, 10));
/*      */         
/*      */ 
/* 5198 */         datetimeAsBytes[13] = 58;
/*      */         
/* 5200 */         datetimeAsBytes[14] = ((byte)Character.forDigit(minute / 10, 10));
/*      */         
/* 5202 */         datetimeAsBytes[15] = ((byte)Character.forDigit(minute % 10, 10));
/*      */         
/*      */ 
/* 5205 */         datetimeAsBytes[16] = 58;
/*      */         
/* 5207 */         datetimeAsBytes[17] = ((byte)Character.forDigit(seconds / 10, 10));
/*      */         
/* 5209 */         datetimeAsBytes[18] = ((byte)Character.forDigit(seconds % 10, 10));
/*      */         
/*      */ 
/* 5212 */         datetimeAsBytes[19] = 46;
/*      */         
/* 5214 */         int nanosOffset = 20;
/*      */         
/* 5216 */         for (int j = 0; j < nanosAsBytes.length; j++) {
/* 5217 */           datetimeAsBytes[(nanosOffset + j)] = nanosAsBytes[j];
/*      */         }
/*      */         
/* 5220 */         unpackedRowData[columnIndex] = datetimeAsBytes;
/*      */       }
/*      */       
/* 5223 */       break;
/*      */     
/*      */     case 0: 
/*      */     case 15: 
/*      */     case 16: 
/*      */     case 246: 
/*      */     case 249: 
/*      */     case 250: 
/*      */     case 251: 
/*      */     case 252: 
/*      */     case 253: 
/*      */     case 254: 
/* 5235 */       unpackedRowData[columnIndex] = binaryData.readLenByteArray(0);
/*      */       
/* 5237 */       break;
/*      */     
/*      */     default: 
/* 5240 */       throw SQLError.createSQLException(Messages.getString("MysqlIO.97") + curField.getMysqlType() + Messages.getString("MysqlIO.98") + columnIndex + Messages.getString("MysqlIO.99") + fields.length + Messages.getString("MysqlIO.100"), "S1000", getExceptionInterceptor());
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void negotiateSSLConnection(String user, String password, String database, int packLength)
/*      */     throws SQLException
/*      */   {
/* 5263 */     if (!ExportControlled.enabled()) {
/* 5264 */       throw new ConnectionFeatureNotAvailableException(this.connection, this.lastPacketSentTimeMs, null);
/*      */     }
/*      */     
/*      */ 
/* 5268 */     if ((this.serverCapabilities & 0x8000) != 0) {
/* 5269 */       this.clientParam |= 0x8000;
/*      */     }
/*      */     
/* 5272 */     this.clientParam |= 0x800;
/*      */     
/* 5274 */     Buffer packet = new Buffer(packLength);
/*      */     
/* 5276 */     if (this.use41Extensions) {
/* 5277 */       packet.writeLong(this.clientParam);
/* 5278 */       packet.writeLong(this.maxThreeBytes);
/* 5279 */       int charsetIndex = 0;
/* 5280 */       if (this.connection.getEncoding() != null) {
/* 5281 */         charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(CharsetMapping.getMysqlEncodingForJavaEncoding(this.connection.getEncoding(), this.connection));
/*      */       }
/* 5283 */       packet.writeByte(charsetIndex == 0 ? 33 : (byte)charsetIndex);
/* 5284 */       packet.writeBytesNoNull(new byte[23]);
/*      */     } else {
/* 5286 */       packet.writeInt((int)this.clientParam);
/*      */     }
/*      */     
/* 5289 */     send(packet, packet.getPosition());
/*      */     
/* 5291 */     ExportControlled.transformSocketToSSLSocket(this);
/*      */   }
/*      */   
/*      */   protected int getServerStatus() {
/* 5295 */     return this.serverStatus;
/*      */   }
/*      */   
/*      */   protected List<ResultSetRow> fetchRowsViaCursor(List<ResultSetRow> fetchedRows, long statementId, Field[] columnTypes, int fetchSize, boolean useBufferRowExplicit)
/*      */     throws SQLException
/*      */   {
/* 5301 */     if (fetchedRows == null) {
/* 5302 */       fetchedRows = new ArrayList(fetchSize);
/*      */     } else {
/* 5304 */       fetchedRows.clear();
/*      */     }
/*      */     
/* 5307 */     this.sharedSendPacket.clear();
/*      */     
/* 5309 */     this.sharedSendPacket.writeByte((byte)28);
/* 5310 */     this.sharedSendPacket.writeLong(statementId);
/* 5311 */     this.sharedSendPacket.writeLong(fetchSize);
/*      */     
/* 5313 */     sendCommand(28, null, this.sharedSendPacket, true, null, 0);
/*      */     
/*      */ 
/* 5316 */     ResultSetRow row = null;
/*      */     
/*      */ 
/* 5319 */     while ((row = nextRow(columnTypes, columnTypes.length, true, 1007, false, useBufferRowExplicit, false, null)) != null) {
/* 5320 */       fetchedRows.add(row);
/*      */     }
/*      */     
/* 5323 */     return fetchedRows;
/*      */   }
/*      */   
/*      */   protected long getThreadId() {
/* 5327 */     return this.threadId;
/*      */   }
/*      */   
/*      */   protected boolean useNanosForElapsedTime() {
/* 5331 */     return this.useNanosForElapsedTime;
/*      */   }
/*      */   
/*      */   protected long getSlowQueryThreshold() {
/* 5335 */     return this.slowQueryThreshold;
/*      */   }
/*      */   
/*      */   protected String getQueryTimingUnits() {
/* 5339 */     return this.queryTimingUnits;
/*      */   }
/*      */   
/*      */   protected int getCommandCount() {
/* 5343 */     return this.commandCount;
/*      */   }
/*      */   
/*      */   private void checkTransactionState(int oldStatus) throws SQLException {
/* 5347 */     boolean previouslyInTrans = (oldStatus & 0x1) != 0;
/* 5348 */     boolean currentlyInTrans = (this.serverStatus & 0x1) != 0;
/*      */     
/* 5350 */     if ((previouslyInTrans) && (!currentlyInTrans)) {
/* 5351 */       this.connection.transactionCompleted();
/* 5352 */     } else if ((!previouslyInTrans) && (currentlyInTrans)) {
/* 5353 */       this.connection.transactionBegun();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setStatementInterceptors(List<StatementInterceptorV2> statementInterceptors) {
/* 5358 */     this.statementInterceptors = statementInterceptors;
/*      */   }
/*      */   
/*      */   protected ExceptionInterceptor getExceptionInterceptor() {
/* 5362 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   protected void setSocketTimeout(int milliseconds) throws SQLException {
/*      */     try {
/* 5367 */       this.mysqlConnection.setSoTimeout(milliseconds);
/*      */     } catch (SocketException e) {
/* 5369 */       SQLException sqlEx = SQLError.createSQLException("Invalid socket timeout value or state", "S1009", getExceptionInterceptor());
/* 5370 */       sqlEx.initCause(e);
/*      */       
/* 5372 */       throw sqlEx;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void releaseResources() {
/* 5377 */     if (this.deflater != null) {
/* 5378 */       this.deflater.end();
/* 5379 */       this.deflater = null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\MysqlIO.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */