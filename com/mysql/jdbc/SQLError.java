/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLDataException;
/*      */ import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
/*      */ import com.mysql.jdbc.exceptions.MySQLNonTransientConnectionException;
/*      */ import com.mysql.jdbc.exceptions.MySQLQueryInterruptedException;
/*      */ import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTransactionRollbackException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTransientConnectionException;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.BindException;
/*      */ import java.sql.DataTruncation;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SQLError
/*      */ {
/*      */   static final int ER_WARNING_NOT_COMPLETE_ROLLBACK = 1196;
/*      */   private static Map<Integer, String> mysqlToSql99State;
/*      */   private static Map<Integer, String> mysqlToSqlState;
/*      */   public static final String SQL_STATE_BASE_TABLE_NOT_FOUND = "S0002";
/*      */   public static final String SQL_STATE_BASE_TABLE_OR_VIEW_ALREADY_EXISTS = "S0001";
/*      */   public static final String SQL_STATE_BASE_TABLE_OR_VIEW_NOT_FOUND = "42S02";
/*      */   public static final String SQL_STATE_COLUMN_ALREADY_EXISTS = "S0021";
/*      */   public static final String SQL_STATE_COLUMN_NOT_FOUND = "S0022";
/*      */   public static final String SQL_STATE_COMMUNICATION_LINK_FAILURE = "08S01";
/*      */   public static final String SQL_STATE_CONNECTION_FAIL_DURING_TX = "08007";
/*      */   public static final String SQL_STATE_CONNECTION_IN_USE = "08002";
/*      */   public static final String SQL_STATE_CONNECTION_NOT_OPEN = "08003";
/*      */   public static final String SQL_STATE_CONNECTION_REJECTED = "08004";
/*      */   public static final String SQL_STATE_DATE_TRUNCATED = "01004";
/*      */   public static final String SQL_STATE_DATETIME_FIELD_OVERFLOW = "22008";
/*      */   public static final String SQL_STATE_DEADLOCK = "41000";
/*      */   public static final String SQL_STATE_DISCONNECT_ERROR = "01002";
/*      */   public static final String SQL_STATE_DIVISION_BY_ZERO = "22012";
/*      */   public static final String SQL_STATE_DRIVER_NOT_CAPABLE = "S1C00";
/*      */   public static final String SQL_STATE_ERROR_IN_ROW = "01S01";
/*      */   public static final String SQL_STATE_GENERAL_ERROR = "S1000";
/*      */   public static final String SQL_STATE_ILLEGAL_ARGUMENT = "S1009";
/*      */   public static final String SQL_STATE_INDEX_ALREADY_EXISTS = "S0011";
/*      */   public static final String SQL_STATE_INDEX_NOT_FOUND = "S0012";
/*      */   public static final String SQL_STATE_INSERT_VALUE_LIST_NO_MATCH_COL_LIST = "21S01";
/*      */   public static final String SQL_STATE_INVALID_AUTH_SPEC = "28000";
/*      */   public static final String SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST = "22018";
/*      */   public static final String SQL_STATE_INVALID_COLUMN_NUMBER = "S1002";
/*      */   public static final String SQL_STATE_INVALID_CONNECTION_ATTRIBUTE = "01S00";
/*      */   public static final String SQL_STATE_MEMORY_ALLOCATION_FAILURE = "S1001";
/*      */   public static final String SQL_STATE_MORE_THAN_ONE_ROW_UPDATED_OR_DELETED = "01S04";
/*      */   public static final String SQL_STATE_NO_DEFAULT_FOR_COLUMN = "S0023";
/*      */   public static final String SQL_STATE_NO_ROWS_UPDATED_OR_DELETED = "01S03";
/*      */   public static final String SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE = "22003";
/*      */   public static final String SQL_STATE_PRIVILEGE_NOT_REVOKED = "01006";
/*      */   public static final String SQL_STATE_SYNTAX_ERROR = "42000";
/*      */   public static final String SQL_STATE_TIMEOUT_EXPIRED = "S1T00";
/*      */   public static final String SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN = "08007";
/*      */   public static final String SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE = "08001";
/*      */   public static final String SQL_STATE_WRONG_NO_OF_PARAMETERS = "07001";
/*      */   public static final String SQL_STATE_INVALID_TRANSACTION_TERMINATION = "2D000";
/*      */   private static Map<String, String> sqlStateMessages;
/*      */   private static final long DEFAULT_WAIT_TIMEOUT_SECONDS = 28800L;
/*      */   private static final int DUE_TO_TIMEOUT_FALSE = 0;
/*      */   private static final int DUE_TO_TIMEOUT_MAYBE = 2;
/*      */   private static final int DUE_TO_TIMEOUT_TRUE = 1;
/*      */   private static final Constructor<?> JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR;
/*      */   private static Method THROWABLE_INIT_CAUSE_METHOD;
/*      */   
/*      */   static
/*      */   {
/*  153 */     if (Util.isJdbc4()) {
/*      */       try {
/*  155 */         JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = Class.forName("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException").getConstructor(new Class[] { MySQLConnection.class, Long.TYPE, Long.TYPE, Exception.class });
/*      */ 
/*      */       }
/*      */       catch (SecurityException e)
/*      */       {
/*  160 */         throw new RuntimeException(e);
/*      */       } catch (NoSuchMethodException e) {
/*  162 */         throw new RuntimeException(e);
/*      */       } catch (ClassNotFoundException e) {
/*  164 */         throw new RuntimeException(e);
/*      */       }
/*      */     } else {
/*  167 */       JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = null;
/*      */     }
/*      */     try
/*      */     {
/*  171 */       THROWABLE_INIT_CAUSE_METHOD = Throwable.class.getMethod("initCause", new Class[] { Throwable.class });
/*      */     }
/*      */     catch (Throwable t) {
/*  174 */       THROWABLE_INIT_CAUSE_METHOD = null;
/*      */     }
/*      */     
/*  177 */     sqlStateMessages = new HashMap();
/*  178 */     sqlStateMessages.put("01002", Messages.getString("SQLError.35"));
/*      */     
/*  180 */     sqlStateMessages.put("01004", Messages.getString("SQLError.36"));
/*      */     
/*  182 */     sqlStateMessages.put("01006", Messages.getString("SQLError.37"));
/*      */     
/*  184 */     sqlStateMessages.put("01S00", Messages.getString("SQLError.38"));
/*      */     
/*  186 */     sqlStateMessages.put("01S01", Messages.getString("SQLError.39"));
/*      */     
/*  188 */     sqlStateMessages.put("01S03", Messages.getString("SQLError.40"));
/*      */     
/*  190 */     sqlStateMessages.put("01S04", Messages.getString("SQLError.41"));
/*      */     
/*  192 */     sqlStateMessages.put("07001", Messages.getString("SQLError.42"));
/*      */     
/*  194 */     sqlStateMessages.put("08001", Messages.getString("SQLError.43"));
/*      */     
/*  196 */     sqlStateMessages.put("08002", Messages.getString("SQLError.44"));
/*      */     
/*  198 */     sqlStateMessages.put("08003", Messages.getString("SQLError.45"));
/*      */     
/*  200 */     sqlStateMessages.put("08004", Messages.getString("SQLError.46"));
/*      */     
/*  202 */     sqlStateMessages.put("08007", Messages.getString("SQLError.47"));
/*      */     
/*  204 */     sqlStateMessages.put("08S01", Messages.getString("SQLError.48"));
/*      */     
/*  206 */     sqlStateMessages.put("21S01", Messages.getString("SQLError.49"));
/*      */     
/*  208 */     sqlStateMessages.put("22003", Messages.getString("SQLError.50"));
/*      */     
/*  210 */     sqlStateMessages.put("22008", Messages.getString("SQLError.51"));
/*      */     
/*  212 */     sqlStateMessages.put("22012", Messages.getString("SQLError.52"));
/*      */     
/*  214 */     sqlStateMessages.put("41000", Messages.getString("SQLError.53"));
/*      */     
/*  216 */     sqlStateMessages.put("28000", Messages.getString("SQLError.54"));
/*      */     
/*  218 */     sqlStateMessages.put("42000", Messages.getString("SQLError.55"));
/*      */     
/*  220 */     sqlStateMessages.put("42S02", Messages.getString("SQLError.56"));
/*      */     
/*  222 */     sqlStateMessages.put("S0001", Messages.getString("SQLError.57"));
/*      */     
/*  224 */     sqlStateMessages.put("S0002", Messages.getString("SQLError.58"));
/*      */     
/*  226 */     sqlStateMessages.put("S0011", Messages.getString("SQLError.59"));
/*      */     
/*  228 */     sqlStateMessages.put("S0012", Messages.getString("SQLError.60"));
/*      */     
/*  230 */     sqlStateMessages.put("S0021", Messages.getString("SQLError.61"));
/*      */     
/*  232 */     sqlStateMessages.put("S0022", Messages.getString("SQLError.62"));
/*      */     
/*  234 */     sqlStateMessages.put("S0023", Messages.getString("SQLError.63"));
/*      */     
/*  236 */     sqlStateMessages.put("S1000", Messages.getString("SQLError.64"));
/*      */     
/*  238 */     sqlStateMessages.put("S1001", Messages.getString("SQLError.65"));
/*      */     
/*  240 */     sqlStateMessages.put("S1002", Messages.getString("SQLError.66"));
/*      */     
/*  242 */     sqlStateMessages.put("S1009", Messages.getString("SQLError.67"));
/*      */     
/*  244 */     sqlStateMessages.put("S1C00", Messages.getString("SQLError.68"));
/*      */     
/*  246 */     sqlStateMessages.put("S1T00", Messages.getString("SQLError.69"));
/*      */     
/*      */ 
/*  249 */     mysqlToSqlState = new Hashtable();
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
/*  260 */     mysqlToSqlState.put(Integer.valueOf(1040), "08004");
/*  261 */     mysqlToSqlState.put(Integer.valueOf(1042), "08004");
/*  262 */     mysqlToSqlState.put(Integer.valueOf(1043), "08004");
/*  263 */     mysqlToSqlState.put(Integer.valueOf(1047), "08S01");
/*      */     
/*  265 */     mysqlToSqlState.put(Integer.valueOf(1081), "08S01");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  270 */     mysqlToSqlState.put(Integer.valueOf(1129), "08004");
/*  271 */     mysqlToSqlState.put(Integer.valueOf(1130), "08004");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  278 */     mysqlToSqlState.put(Integer.valueOf(1045), "28000");
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
/*  295 */     mysqlToSqlState.put(Integer.valueOf(1037), "S1001");
/*      */     
/*  297 */     mysqlToSqlState.put(Integer.valueOf(1038), "S1001");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  306 */     mysqlToSqlState.put(Integer.valueOf(1064), "42000");
/*  307 */     mysqlToSqlState.put(Integer.valueOf(1065), "42000");
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
/*  334 */     mysqlToSqlState.put(Integer.valueOf(1055), "S1009");
/*  335 */     mysqlToSqlState.put(Integer.valueOf(1056), "S1009");
/*  336 */     mysqlToSqlState.put(Integer.valueOf(1057), "S1009");
/*  337 */     mysqlToSqlState.put(Integer.valueOf(1059), "S1009");
/*  338 */     mysqlToSqlState.put(Integer.valueOf(1060), "S1009");
/*  339 */     mysqlToSqlState.put(Integer.valueOf(1061), "S1009");
/*  340 */     mysqlToSqlState.put(Integer.valueOf(1062), "S1009");
/*  341 */     mysqlToSqlState.put(Integer.valueOf(1063), "S1009");
/*  342 */     mysqlToSqlState.put(Integer.valueOf(1066), "S1009");
/*  343 */     mysqlToSqlState.put(Integer.valueOf(1067), "S1009");
/*  344 */     mysqlToSqlState.put(Integer.valueOf(1068), "S1009");
/*  345 */     mysqlToSqlState.put(Integer.valueOf(1069), "S1009");
/*  346 */     mysqlToSqlState.put(Integer.valueOf(1070), "S1009");
/*  347 */     mysqlToSqlState.put(Integer.valueOf(1071), "S1009");
/*  348 */     mysqlToSqlState.put(Integer.valueOf(1072), "S1009");
/*  349 */     mysqlToSqlState.put(Integer.valueOf(1073), "S1009");
/*  350 */     mysqlToSqlState.put(Integer.valueOf(1074), "S1009");
/*  351 */     mysqlToSqlState.put(Integer.valueOf(1075), "S1009");
/*  352 */     mysqlToSqlState.put(Integer.valueOf(1082), "S1009");
/*  353 */     mysqlToSqlState.put(Integer.valueOf(1083), "S1009");
/*  354 */     mysqlToSqlState.put(Integer.valueOf(1084), "S1009");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  359 */     mysqlToSqlState.put(Integer.valueOf(1058), "21S01");
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
/*      */ 
/*  395 */     mysqlToSqlState.put(Integer.valueOf(1051), "42S02");
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  400 */     mysqlToSqlState.put(Integer.valueOf(1054), "S0022");
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
/*  412 */     mysqlToSqlState.put(Integer.valueOf(1205), "41000");
/*  413 */     mysqlToSqlState.put(Integer.valueOf(1213), "41000");
/*      */     
/*  415 */     mysqlToSql99State = new HashMap();
/*      */     
/*  417 */     mysqlToSql99State.put(Integer.valueOf(1205), "41000");
/*  418 */     mysqlToSql99State.put(Integer.valueOf(1213), "41000");
/*  419 */     mysqlToSql99State.put(Integer.valueOf(1022), "23000");
/*      */     
/*  421 */     mysqlToSql99State.put(Integer.valueOf(1037), "HY001");
/*      */     
/*  423 */     mysqlToSql99State.put(Integer.valueOf(1038), "HY001");
/*      */     
/*  425 */     mysqlToSql99State.put(Integer.valueOf(1040), "08004");
/*      */     
/*  427 */     mysqlToSql99State.put(Integer.valueOf(1042), "08S01");
/*      */     
/*  429 */     mysqlToSql99State.put(Integer.valueOf(1043), "08S01");
/*      */     
/*  431 */     mysqlToSql99State.put(Integer.valueOf(1044), "42000");
/*      */     
/*  433 */     mysqlToSql99State.put(Integer.valueOf(1045), "28000");
/*      */     
/*  435 */     mysqlToSql99State.put(Integer.valueOf(1050), "42S01");
/*      */     
/*  437 */     mysqlToSql99State.put(Integer.valueOf(1051), "42S02");
/*      */     
/*  439 */     mysqlToSql99State.put(Integer.valueOf(1052), "23000");
/*      */     
/*  441 */     mysqlToSql99State.put(Integer.valueOf(1053), "08S01");
/*      */     
/*  443 */     mysqlToSql99State.put(Integer.valueOf(1054), "42S22");
/*      */     
/*  445 */     mysqlToSql99State.put(Integer.valueOf(1055), "42000");
/*      */     
/*  447 */     mysqlToSql99State.put(Integer.valueOf(1056), "42000");
/*      */     
/*  449 */     mysqlToSql99State.put(Integer.valueOf(1057), "42000");
/*      */     
/*  451 */     mysqlToSql99State.put(Integer.valueOf(1058), "21S01");
/*      */     
/*  453 */     mysqlToSql99State.put(Integer.valueOf(1059), "42000");
/*      */     
/*  455 */     mysqlToSql99State.put(Integer.valueOf(1060), "42S21");
/*      */     
/*  457 */     mysqlToSql99State.put(Integer.valueOf(1061), "42000");
/*      */     
/*  459 */     mysqlToSql99State.put(Integer.valueOf(1062), "23000");
/*      */     
/*  461 */     mysqlToSql99State.put(Integer.valueOf(1063), "42000");
/*      */     
/*  463 */     mysqlToSql99State.put(Integer.valueOf(1064), "42000");
/*      */     
/*  465 */     mysqlToSql99State.put(Integer.valueOf(1065), "42000");
/*      */     
/*  467 */     mysqlToSql99State.put(Integer.valueOf(1066), "42000");
/*      */     
/*  469 */     mysqlToSql99State.put(Integer.valueOf(1067), "42000");
/*      */     
/*  471 */     mysqlToSql99State.put(Integer.valueOf(1068), "42000");
/*      */     
/*  473 */     mysqlToSql99State.put(Integer.valueOf(1069), "42000");
/*      */     
/*  475 */     mysqlToSql99State.put(Integer.valueOf(1070), "42000");
/*      */     
/*  477 */     mysqlToSql99State.put(Integer.valueOf(1071), "42000");
/*      */     
/*  479 */     mysqlToSql99State.put(Integer.valueOf(1072), "42000");
/*      */     
/*  481 */     mysqlToSql99State.put(Integer.valueOf(1073), "42000");
/*      */     
/*  483 */     mysqlToSql99State.put(Integer.valueOf(1074), "42000");
/*      */     
/*  485 */     mysqlToSql99State.put(Integer.valueOf(1075), "42000");
/*      */     
/*  487 */     mysqlToSql99State.put(Integer.valueOf(1080), "08S01");
/*      */     
/*  489 */     mysqlToSql99State.put(Integer.valueOf(1081), "08S01");
/*      */     
/*  491 */     mysqlToSql99State.put(Integer.valueOf(1082), "42S12");
/*      */     
/*  493 */     mysqlToSql99State.put(Integer.valueOf(1083), "42000");
/*      */     
/*  495 */     mysqlToSql99State.put(Integer.valueOf(1084), "42000");
/*      */     
/*  497 */     mysqlToSql99State.put(Integer.valueOf(1090), "42000");
/*      */     
/*  499 */     mysqlToSql99State.put(Integer.valueOf(1091), "42000");
/*      */     
/*  501 */     mysqlToSql99State.put(Integer.valueOf(1101), "42000");
/*      */     
/*  503 */     mysqlToSql99State.put(Integer.valueOf(1102), "42000");
/*      */     
/*  505 */     mysqlToSql99State.put(Integer.valueOf(1103), "42000");
/*      */     
/*  507 */     mysqlToSql99State.put(Integer.valueOf(1104), "42000");
/*      */     
/*  509 */     mysqlToSql99State.put(Integer.valueOf(1106), "42000");
/*      */     
/*  511 */     mysqlToSql99State.put(Integer.valueOf(1107), "42000");
/*      */     
/*  513 */     mysqlToSql99State.put(Integer.valueOf(1109), "42S02");
/*      */     
/*  515 */     mysqlToSql99State.put(Integer.valueOf(1110), "42000");
/*      */     
/*  517 */     mysqlToSql99State.put(Integer.valueOf(1112), "42000");
/*      */     
/*  519 */     mysqlToSql99State.put(Integer.valueOf(1113), "42000");
/*      */     
/*  521 */     mysqlToSql99State.put(Integer.valueOf(1115), "42000");
/*      */     
/*  523 */     mysqlToSql99State.put(Integer.valueOf(1118), "42000");
/*      */     
/*  525 */     mysqlToSql99State.put(Integer.valueOf(1120), "42000");
/*      */     
/*  527 */     mysqlToSql99State.put(Integer.valueOf(1121), "42000");
/*      */     
/*  529 */     mysqlToSql99State.put(Integer.valueOf(1131), "42000");
/*      */     
/*  531 */     mysqlToSql99State.put(Integer.valueOf(1132), "42000");
/*      */     
/*  533 */     mysqlToSql99State.put(Integer.valueOf(1133), "42000");
/*      */     
/*  535 */     mysqlToSql99State.put(Integer.valueOf(1136), "21S01");
/*      */     
/*  537 */     mysqlToSql99State.put(Integer.valueOf(1138), "42000");
/*      */     
/*  539 */     mysqlToSql99State.put(Integer.valueOf(1139), "42000");
/*      */     
/*  541 */     mysqlToSql99State.put(Integer.valueOf(1140), "42000");
/*      */     
/*  543 */     mysqlToSql99State.put(Integer.valueOf(1141), "42000");
/*      */     
/*  545 */     mysqlToSql99State.put(Integer.valueOf(1142), "42000");
/*      */     
/*  547 */     mysqlToSql99State.put(Integer.valueOf(1143), "42000");
/*      */     
/*  549 */     mysqlToSql99State.put(Integer.valueOf(1144), "42000");
/*      */     
/*  551 */     mysqlToSql99State.put(Integer.valueOf(1145), "42000");
/*      */     
/*  553 */     mysqlToSql99State.put(Integer.valueOf(1146), "42S02");
/*      */     
/*  555 */     mysqlToSql99State.put(Integer.valueOf(1147), "42000");
/*      */     
/*  557 */     mysqlToSql99State.put(Integer.valueOf(1148), "42000");
/*      */     
/*  559 */     mysqlToSql99State.put(Integer.valueOf(1149), "42000");
/*      */     
/*  561 */     mysqlToSql99State.put(Integer.valueOf(1152), "08S01");
/*      */     
/*  563 */     mysqlToSql99State.put(Integer.valueOf(1153), "08S01");
/*      */     
/*  565 */     mysqlToSql99State.put(Integer.valueOf(1154), "08S01");
/*      */     
/*  567 */     mysqlToSql99State.put(Integer.valueOf(1155), "08S01");
/*      */     
/*  569 */     mysqlToSql99State.put(Integer.valueOf(1156), "08S01");
/*      */     
/*  571 */     mysqlToSql99State.put(Integer.valueOf(1157), "08S01");
/*      */     
/*  573 */     mysqlToSql99State.put(Integer.valueOf(1158), "08S01");
/*      */     
/*  575 */     mysqlToSql99State.put(Integer.valueOf(1159), "08S01");
/*      */     
/*  577 */     mysqlToSql99State.put(Integer.valueOf(1160), "08S01");
/*      */     
/*  579 */     mysqlToSql99State.put(Integer.valueOf(1161), "08S01");
/*      */     
/*  581 */     mysqlToSql99State.put(Integer.valueOf(1162), "42000");
/*      */     
/*  583 */     mysqlToSql99State.put(Integer.valueOf(1163), "42000");
/*      */     
/*  585 */     mysqlToSql99State.put(Integer.valueOf(1164), "42000");
/*      */     
/*      */ 
/*      */ 
/*  589 */     mysqlToSql99State.put(Integer.valueOf(1166), "42000");
/*      */     
/*  591 */     mysqlToSql99State.put(Integer.valueOf(1167), "42000");
/*      */     
/*  593 */     mysqlToSql99State.put(Integer.valueOf(1169), "23000");
/*      */     
/*  595 */     mysqlToSql99State.put(Integer.valueOf(1170), "42000");
/*      */     
/*  597 */     mysqlToSql99State.put(Integer.valueOf(1171), "42000");
/*      */     
/*  599 */     mysqlToSql99State.put(Integer.valueOf(1172), "42000");
/*      */     
/*  601 */     mysqlToSql99State.put(Integer.valueOf(1173), "42000");
/*      */     
/*  603 */     mysqlToSql99State.put(Integer.valueOf(1177), "42000");
/*      */     
/*  605 */     mysqlToSql99State.put(Integer.valueOf(1178), "42000");
/*      */     
/*  607 */     mysqlToSql99State.put(Integer.valueOf(1179), "25000");
/*      */     
/*      */ 
/*  610 */     mysqlToSql99State.put(Integer.valueOf(1184), "08S01");
/*      */     
/*  612 */     mysqlToSql99State.put(Integer.valueOf(1189), "08S01");
/*      */     
/*  614 */     mysqlToSql99State.put(Integer.valueOf(1190), "08S01");
/*      */     
/*  616 */     mysqlToSql99State.put(Integer.valueOf(1203), "42000");
/*      */     
/*  618 */     mysqlToSql99State.put(Integer.valueOf(1207), "25000");
/*      */     
/*  620 */     mysqlToSql99State.put(Integer.valueOf(1211), "42000");
/*      */     
/*  622 */     mysqlToSql99State.put(Integer.valueOf(1213), "40001");
/*      */     
/*  624 */     mysqlToSql99State.put(Integer.valueOf(1216), "23000");
/*      */     
/*  626 */     mysqlToSql99State.put(Integer.valueOf(1217), "23000");
/*      */     
/*  628 */     mysqlToSql99State.put(Integer.valueOf(1218), "08S01");
/*      */     
/*  630 */     mysqlToSql99State.put(Integer.valueOf(1222), "21000");
/*      */     
/*      */ 
/*  633 */     mysqlToSql99State.put(Integer.valueOf(1226), "42000");
/*      */     
/*  635 */     mysqlToSql99State.put(Integer.valueOf(1230), "42000");
/*      */     
/*  637 */     mysqlToSql99State.put(Integer.valueOf(1231), "42000");
/*      */     
/*  639 */     mysqlToSql99State.put(Integer.valueOf(1232), "42000");
/*      */     
/*  641 */     mysqlToSql99State.put(Integer.valueOf(1234), "42000");
/*      */     
/*  643 */     mysqlToSql99State.put(Integer.valueOf(1235), "42000");
/*      */     
/*  645 */     mysqlToSql99State.put(Integer.valueOf(1239), "42000");
/*      */     
/*  647 */     mysqlToSql99State.put(Integer.valueOf(1241), "21000");
/*      */     
/*  649 */     mysqlToSql99State.put(Integer.valueOf(1242), "21000");
/*      */     
/*  651 */     mysqlToSql99State.put(Integer.valueOf(1247), "42S22");
/*      */     
/*  653 */     mysqlToSql99State.put(Integer.valueOf(1248), "42000");
/*      */     
/*  655 */     mysqlToSql99State.put(Integer.valueOf(1249), "01000");
/*      */     
/*  657 */     mysqlToSql99State.put(Integer.valueOf(1250), "42000");
/*      */     
/*  659 */     mysqlToSql99State.put(Integer.valueOf(1251), "08004");
/*      */     
/*  661 */     mysqlToSql99State.put(Integer.valueOf(1252), "42000");
/*      */     
/*  663 */     mysqlToSql99State.put(Integer.valueOf(1253), "42000");
/*      */     
/*  665 */     mysqlToSql99State.put(Integer.valueOf(1261), "01000");
/*      */     
/*  667 */     mysqlToSql99State.put(Integer.valueOf(1262), "01000");
/*      */     
/*  669 */     mysqlToSql99State.put(Integer.valueOf(1263), "01000");
/*      */     
/*  671 */     mysqlToSql99State.put(Integer.valueOf(1264), "01000");
/*      */     
/*  673 */     mysqlToSql99State.put(Integer.valueOf(1265), "01000");
/*      */     
/*  675 */     mysqlToSql99State.put(Integer.valueOf(1280), "42000");
/*      */     
/*  677 */     mysqlToSql99State.put(Integer.valueOf(1281), "42000");
/*      */     
/*  679 */     mysqlToSql99State.put(Integer.valueOf(1286), "42000");
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
/*      */   static SQLWarning convertShowWarningsToSQLWarnings(Connection connection)
/*      */     throws SQLException
/*      */   {
/*  699 */     return convertShowWarningsToSQLWarnings(connection, 0, false);
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
/*      */   static SQLWarning convertShowWarningsToSQLWarnings(Connection connection, int warningCountIfKnown, boolean forTruncationOnly)
/*      */     throws SQLException
/*      */   {
/*  724 */     Statement stmt = null;
/*  725 */     ResultSet warnRs = null;
/*      */     
/*  727 */     SQLWarning currentWarning = null;
/*      */     try
/*      */     {
/*  730 */       if (warningCountIfKnown < 100) {
/*  731 */         stmt = connection.createStatement();
/*      */         
/*  733 */         if (stmt.getMaxRows() != 0) {
/*  734 */           stmt.setMaxRows(0);
/*      */         }
/*      */       }
/*      */       else {
/*  738 */         stmt = connection.createStatement(1003, 1007);
/*      */         
/*      */ 
/*  741 */         stmt.setFetchSize(Integer.MIN_VALUE);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  751 */       warnRs = stmt.executeQuery("SHOW WARNINGS");
/*      */       int code;
/*  753 */       while (warnRs.next()) {
/*  754 */         code = warnRs.getInt("Code");
/*      */         
/*  756 */         if (forTruncationOnly) {
/*  757 */           if ((code == 1265) || (code == 1264)) {
/*  758 */             DataTruncation newTruncation = new MysqlDataTruncation(warnRs.getString("Message"), 0, false, false, 0, 0, code);
/*      */             
/*      */ 
/*  761 */             if (currentWarning == null) {
/*  762 */               currentWarning = newTruncation;
/*      */             } else {
/*  764 */               currentWarning.setNextWarning(newTruncation);
/*      */             }
/*      */           }
/*      */         }
/*      */         else {
/*  769 */           String message = warnRs.getString("Message");
/*      */           
/*  771 */           SQLWarning newWarning = new SQLWarning(message, mysqlToSqlState(code, connection.getUseSqlStateCodes()), code);
/*      */           
/*      */ 
/*      */ 
/*  775 */           if (currentWarning == null) {
/*  776 */             currentWarning = newWarning;
/*      */           } else {
/*  778 */             currentWarning.setNextWarning(newWarning);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  783 */       if ((forTruncationOnly) && (currentWarning != null)) {
/*  784 */         throw currentWarning;
/*      */       }
/*      */       
/*  787 */       return currentWarning;
/*      */     } finally {
/*  789 */       SQLException reThrow = null;
/*      */       
/*  791 */       if (warnRs != null) {
/*      */         try {
/*  793 */           warnRs.close();
/*      */         } catch (SQLException sqlEx) {
/*  795 */           reThrow = sqlEx;
/*      */         }
/*      */       }
/*      */       
/*  799 */       if (stmt != null) {
/*      */         try {
/*  801 */           stmt.close();
/*      */ 
/*      */         }
/*      */         catch (SQLException sqlEx)
/*      */         {
/*  806 */           reThrow = sqlEx;
/*      */         }
/*      */       }
/*      */       
/*  810 */       if (reThrow != null) {
/*  811 */         throw reThrow;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void dumpSqlStatesMappingsAsXml() throws Exception {
/*  817 */     TreeMap<Integer, Integer> allErrorNumbers = new TreeMap();
/*  818 */     Map<Object, String> mysqlErrorNumbersToNames = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  826 */     for (Integer errorNumber : mysqlToSql99State.keySet()) {
/*  827 */       allErrorNumbers.put(errorNumber, errorNumber);
/*      */     }
/*      */     
/*  830 */     for (Integer errorNumber : mysqlToSqlState.keySet()) {
/*  831 */       allErrorNumbers.put(errorNumber, errorNumber);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  837 */     Field[] possibleFields = MysqlErrorNumbers.class.getDeclaredFields();
/*      */     
/*      */ 
/*  840 */     for (int i = 0; i < possibleFields.length; i++) {
/*  841 */       String fieldName = possibleFields[i].getName();
/*      */       
/*  843 */       if (fieldName.startsWith("ER_")) {
/*  844 */         mysqlErrorNumbersToNames.put(possibleFields[i].get(null), fieldName);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  849 */     System.out.println("<ErrorMappings>");
/*      */     
/*  851 */     for (Integer errorNumber : allErrorNumbers.keySet()) {
/*  852 */       String sql92State = mysqlToSql99(errorNumber.intValue());
/*  853 */       String oldSqlState = mysqlToXOpen(errorNumber.intValue());
/*      */       
/*  855 */       System.out.println("   <ErrorMapping mysqlErrorNumber=\"" + errorNumber + "\" mysqlErrorName=\"" + (String)mysqlErrorNumbersToNames.get(errorNumber) + "\" legacySqlState=\"" + (oldSqlState == null ? "" : oldSqlState) + "\" sql92SqlState=\"" + (sql92State == null ? "" : sql92State) + "\"/>");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  864 */     System.out.println("</ErrorMappings>");
/*      */   }
/*      */   
/*      */   static String get(String stateCode) {
/*  868 */     return (String)sqlStateMessages.get(stateCode);
/*      */   }
/*      */   
/*      */   private static String mysqlToSql99(int errno) {
/*  872 */     Integer err = Integer.valueOf(errno);
/*      */     
/*  874 */     if (mysqlToSql99State.containsKey(err)) {
/*  875 */       return (String)mysqlToSql99State.get(err);
/*      */     }
/*      */     
/*  878 */     return "HY000";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static String mysqlToSqlState(int errno, boolean useSql92States)
/*      */   {
/*  890 */     if (useSql92States) {
/*  891 */       return mysqlToSql99(errno);
/*      */     }
/*      */     
/*  894 */     return mysqlToXOpen(errno);
/*      */   }
/*      */   
/*      */   private static String mysqlToXOpen(int errno) {
/*  898 */     Integer err = Integer.valueOf(errno);
/*      */     
/*  900 */     if (mysqlToSqlState.containsKey(err)) {
/*  901 */       return (String)mysqlToSqlState.get(err);
/*      */     }
/*      */     
/*  904 */     return "S1000";
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
/*      */   public static SQLException createSQLException(String message, String sqlState, ExceptionInterceptor interceptor)
/*      */   {
/*  920 */     return createSQLException(message, sqlState, 0, interceptor);
/*      */   }
/*      */   
/*      */ 
/*  924 */   public static SQLException createSQLException(String message, ExceptionInterceptor interceptor) { return createSQLException(message, interceptor, null); }
/*      */   
/*      */   public static SQLException createSQLException(String message, ExceptionInterceptor interceptor, Connection conn) {
/*  927 */     SQLException sqlEx = new SQLException(message);
/*      */     
/*  929 */     if (interceptor != null) {
/*  930 */       SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);
/*      */       
/*  932 */       if (interceptedEx != null) {
/*  933 */         return interceptedEx;
/*      */       }
/*      */     }
/*      */     
/*  937 */     return sqlEx;
/*      */   }
/*      */   
/*      */   public static SQLException createSQLException(String message, String sqlState, Throwable cause, ExceptionInterceptor interceptor) {
/*  941 */     return createSQLException(message, sqlState, cause, interceptor, null);
/*      */   }
/*      */   
/*      */   public static SQLException createSQLException(String message, String sqlState, Throwable cause, ExceptionInterceptor interceptor, Connection conn) {
/*  945 */     if ((THROWABLE_INIT_CAUSE_METHOD == null) && 
/*  946 */       (cause != null)) {
/*  947 */       message = message + " due to " + cause.toString();
/*      */     }
/*      */     
/*      */ 
/*  951 */     SQLException sqlEx = createSQLException(message, sqlState, interceptor);
/*      */     
/*  953 */     if ((cause != null) && (THROWABLE_INIT_CAUSE_METHOD != null)) {
/*      */       try {
/*  955 */         THROWABLE_INIT_CAUSE_METHOD.invoke(sqlEx, new Object[] { cause });
/*      */       }
/*      */       catch (Throwable t) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  962 */     if (interceptor != null) {
/*  963 */       SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);
/*      */       
/*  965 */       if (interceptedEx != null) {
/*  966 */         return interceptedEx;
/*      */       }
/*      */     }
/*      */     
/*  970 */     return sqlEx;
/*      */   }
/*      */   
/*      */   public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, ExceptionInterceptor interceptor)
/*      */   {
/*  975 */     return createSQLException(message, sqlState, vendorErrorCode, false, interceptor);
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
/*      */   public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, boolean isTransient, ExceptionInterceptor interceptor)
/*      */   {
/*  989 */     return createSQLException(message, sqlState, vendorErrorCode, isTransient, interceptor, null);
/*      */   }
/*      */   
/*      */   public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, boolean isTransient, ExceptionInterceptor interceptor, Connection conn) {
/*      */     try {
/*  994 */       SQLException sqlEx = null;
/*      */       
/*  996 */       if (sqlState != null) {
/*  997 */         if (sqlState.startsWith("08")) {
/*  998 */           if (isTransient) {
/*  999 */             if (!Util.isJdbc4()) {
/* 1000 */               sqlEx = new MySQLTransientConnectionException(message, sqlState, vendorErrorCode);
/*      */             }
/*      */             else {
/* 1003 */               sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransientConnectionException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */ 
/*      */             }
/*      */             
/*      */ 
/*      */ 
/*      */           }
/* 1011 */           else if (!Util.isJdbc4()) {
/* 1012 */             sqlEx = new MySQLNonTransientConnectionException(message, sqlState, vendorErrorCode);
/*      */           }
/*      */           else {
/* 1015 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1022 */         else if (sqlState.startsWith("22")) {
/* 1023 */           if (!Util.isJdbc4()) {
/* 1024 */             sqlEx = new MySQLDataException(message, sqlState, vendorErrorCode);
/*      */           }
/*      */           else {
/* 1027 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLDataException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1035 */         else if (sqlState.startsWith("23"))
/*      */         {
/* 1037 */           if (!Util.isJdbc4()) {
/* 1038 */             sqlEx = new MySQLIntegrityConstraintViolationException(message, sqlState, vendorErrorCode);
/*      */           }
/*      */           else {
/* 1041 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1049 */         else if (sqlState.startsWith("42")) {
/* 1050 */           if (!Util.isJdbc4()) {
/* 1051 */             sqlEx = new MySQLSyntaxErrorException(message, sqlState, vendorErrorCode);
/*      */           }
/*      */           else {
/* 1054 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1061 */         else if (sqlState.startsWith("40")) {
/* 1062 */           if (!Util.isJdbc4()) {
/* 1063 */             sqlEx = new MySQLTransactionRollbackException(message, sqlState, vendorErrorCode);
/*      */           }
/*      */           else {
/* 1066 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */         }
/* 1074 */         else if (sqlState.startsWith("70100")) {
/* 1075 */           if (!Util.isJdbc4()) {
/* 1076 */             sqlEx = new MySQLQueryInterruptedException(message, sqlState, vendorErrorCode);
/*      */           } else {
/* 1078 */             sqlEx = (SQLException)Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLQueryInterruptedException", new Class[] { String.class, String.class, Integer.TYPE }, new Object[] { message, sqlState, Integer.valueOf(vendorErrorCode) }, interceptor);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1086 */           sqlEx = new SQLException(message, sqlState, vendorErrorCode);
/*      */         }
/*      */       } else {
/* 1089 */         sqlEx = new SQLException(message, sqlState, vendorErrorCode);
/*      */       }
/*      */       
/* 1092 */       if (interceptor != null) {
/* 1093 */         SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);
/*      */         
/* 1095 */         if (interceptedEx != null) {
/* 1096 */           return interceptedEx;
/*      */         }
/*      */       }
/*      */       
/* 1100 */       return sqlEx;
/*      */     } catch (SQLException sqlEx) {
/* 1102 */       SQLException unexpectedEx = new SQLException("Unable to create correct SQLException class instance, error class/codes may be incorrect. Reason: " + Util.stackTraceToString(sqlEx), "S1000");
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1107 */       if (interceptor != null) {
/* 1108 */         SQLException interceptedEx = interceptor.interceptException(unexpectedEx, conn);
/*      */         
/* 1110 */         if (interceptedEx != null) {
/* 1111 */           return interceptedEx;
/*      */         }
/*      */       }
/*      */       
/* 1115 */       return unexpectedEx;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static SQLException createCommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException, ExceptionInterceptor interceptor)
/*      */   {
/* 1122 */     SQLException exToReturn = null;
/*      */     
/* 1124 */     if (!Util.isJdbc4()) {
/* 1125 */       exToReturn = new CommunicationsException(conn, lastPacketSentTimeMs, lastPacketReceivedTimeMs, underlyingException);
/*      */     } else {
/*      */       try
/*      */       {
/* 1129 */         exToReturn = (SQLException)Util.handleNewInstance(JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR, new Object[] { conn, Long.valueOf(lastPacketSentTimeMs), Long.valueOf(lastPacketReceivedTimeMs), underlyingException }, interceptor);
/*      */ 
/*      */       }
/*      */       catch (SQLException sqlEx)
/*      */       {
/* 1134 */         return sqlEx;
/*      */       }
/*      */     }
/*      */     
/* 1138 */     if ((THROWABLE_INIT_CAUSE_METHOD != null) && (underlyingException != null)) {
/*      */       try {
/* 1140 */         THROWABLE_INIT_CAUSE_METHOD.invoke(exToReturn, new Object[] { underlyingException });
/*      */       }
/*      */       catch (Throwable t) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1147 */     if (interceptor != null) {
/* 1148 */       SQLException interceptedEx = interceptor.interceptException(exToReturn, conn);
/*      */       
/* 1150 */       if (interceptedEx != null) {
/* 1151 */         return interceptedEx;
/*      */       }
/*      */     }
/*      */     
/* 1155 */     return exToReturn;
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
/*      */   public static String createLinkFailureMessageBasedOnHeuristics(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException, boolean streamingResultSetInPlay)
/*      */   {
/* 1175 */     long serverTimeoutSeconds = 0L;
/* 1176 */     boolean isInteractiveClient = false;
/*      */     
/* 1178 */     if (conn != null) {
/* 1179 */       isInteractiveClient = conn.getInteractiveClient();
/*      */       
/* 1181 */       String serverTimeoutSecondsStr = null;
/*      */       
/* 1183 */       if (isInteractiveClient) {
/* 1184 */         serverTimeoutSecondsStr = conn.getServerVariable("interactive_timeout");
/*      */       }
/*      */       else {
/* 1187 */         serverTimeoutSecondsStr = conn.getServerVariable("wait_timeout");
/*      */       }
/*      */       
/*      */ 
/* 1191 */       if (serverTimeoutSecondsStr != null) {
/*      */         try {
/* 1193 */           serverTimeoutSeconds = Long.parseLong(serverTimeoutSecondsStr);
/*      */         }
/*      */         catch (NumberFormatException nfe) {
/* 1196 */           serverTimeoutSeconds = 0L;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1201 */     StringBuffer exceptionMessageBuf = new StringBuffer();
/*      */     
/* 1203 */     long nowMs = System.currentTimeMillis();
/*      */     
/* 1205 */     if (lastPacketSentTimeMs == 0L) {
/* 1206 */       lastPacketSentTimeMs = nowMs;
/*      */     }
/*      */     
/* 1209 */     long timeSinceLastPacketSentMs = nowMs - lastPacketSentTimeMs;
/* 1210 */     long timeSinceLastPacketSeconds = timeSinceLastPacketSentMs / 1000L;
/*      */     
/* 1212 */     long timeSinceLastPacketReceivedMs = nowMs - lastPacketReceivedTimeMs;
/*      */     
/* 1214 */     int dueToTimeout = 0;
/*      */     
/* 1216 */     StringBuffer timeoutMessageBuf = null;
/*      */     
/* 1218 */     if (streamingResultSetInPlay) {
/* 1219 */       exceptionMessageBuf.append(Messages.getString("CommunicationsException.ClientWasStreaming"));
/*      */     }
/*      */     else {
/* 1222 */       if (serverTimeoutSeconds != 0L) {
/* 1223 */         if (timeSinceLastPacketSeconds > serverTimeoutSeconds) {
/* 1224 */           dueToTimeout = 1;
/*      */           
/* 1226 */           timeoutMessageBuf = new StringBuffer();
/*      */           
/* 1228 */           timeoutMessageBuf.append(Messages.getString("CommunicationsException.2"));
/*      */           
/*      */ 
/* 1231 */           if (!isInteractiveClient) {
/* 1232 */             timeoutMessageBuf.append(Messages.getString("CommunicationsException.3"));
/*      */           }
/*      */           else {
/* 1235 */             timeoutMessageBuf.append(Messages.getString("CommunicationsException.4"));
/*      */           }
/*      */           
/*      */         }
/*      */       }
/* 1240 */       else if (timeSinceLastPacketSeconds > 28800L) {
/* 1241 */         dueToTimeout = 2;
/*      */         
/* 1243 */         timeoutMessageBuf = new StringBuffer();
/*      */         
/* 1245 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.5"));
/*      */         
/* 1247 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.6"));
/*      */         
/* 1249 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.7"));
/*      */         
/* 1251 */         timeoutMessageBuf.append(Messages.getString("CommunicationsException.8"));
/*      */       }
/*      */       
/*      */ 
/* 1255 */       if ((dueToTimeout == 1) || (dueToTimeout == 2))
/*      */       {
/*      */ 
/* 1258 */         if (lastPacketReceivedTimeMs != 0L) {
/* 1259 */           Object[] timingInfo = { Long.valueOf(timeSinceLastPacketReceivedMs), Long.valueOf(timeSinceLastPacketSentMs) };
/*      */           
/*      */ 
/*      */ 
/* 1263 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfo", timingInfo));
/*      */         }
/*      */         else
/*      */         {
/* 1267 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfoNoRecv", new Object[] { Long.valueOf(timeSinceLastPacketSentMs) }));
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1272 */         if (timeoutMessageBuf != null) {
/* 1273 */           exceptionMessageBuf.append(timeoutMessageBuf);
/*      */         }
/*      */         
/* 1276 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.11"));
/*      */         
/* 1278 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.12"));
/*      */         
/* 1280 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.13"));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 1289 */       else if ((underlyingException instanceof BindException)) {
/* 1290 */         if ((conn.getLocalSocketAddress() != null) && (!Util.interfaceExists(conn.getLocalSocketAddress())))
/*      */         {
/*      */ 
/* 1293 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.LocalSocketAddressNotAvailable"));
/*      */         }
/*      */         else
/*      */         {
/* 1297 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.TooManyClientConnections"));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1304 */     if (exceptionMessageBuf.length() == 0)
/*      */     {
/* 1306 */       exceptionMessageBuf.append(Messages.getString("CommunicationsException.20"));
/*      */       
/*      */ 
/* 1309 */       if ((THROWABLE_INIT_CAUSE_METHOD == null) && (underlyingException != null))
/*      */       {
/* 1311 */         exceptionMessageBuf.append(Messages.getString("CommunicationsException.21"));
/*      */         
/* 1313 */         exceptionMessageBuf.append(Util.stackTraceToString(underlyingException));
/*      */       }
/*      */       
/*      */ 
/* 1317 */       if ((conn != null) && (conn.getMaintainTimeStats()) && (!conn.getParanoid()))
/*      */       {
/* 1319 */         exceptionMessageBuf.append("\n\n");
/* 1320 */         if (lastPacketReceivedTimeMs != 0L) {
/* 1321 */           Object[] timingInfo = { Long.valueOf(timeSinceLastPacketReceivedMs), Long.valueOf(timeSinceLastPacketSentMs) };
/*      */           
/*      */ 
/*      */ 
/* 1325 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfo", timingInfo));
/*      */         }
/*      */         else
/*      */         {
/* 1329 */           exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfoNoRecv", new Object[] { Long.valueOf(timeSinceLastPacketSentMs) }));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1336 */     return exceptionMessageBuf.toString();
/*      */   }
/*      */   
/*      */   public static SQLException notImplemented() {
/* 1340 */     if (Util.isJdbc4()) {
/*      */       try {
/* 1342 */         return (SQLException)Class.forName("java.sql.SQLFeatureNotSupportedException").newInstance();
/*      */       }
/*      */       catch (Throwable t) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1350 */     return new NotImplemented();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\SQLError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */