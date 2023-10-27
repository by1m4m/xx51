/*     */ package oshi.util.platform.mac;
/*     */ 
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.jna.platform.mac.IOKit.IOConnect;
/*     */ import oshi.jna.platform.mac.IOKit.SMCKeyData;
/*     */ import oshi.jna.platform.mac.IOKit.SMCKeyDataKeyInfo;
/*     */ import oshi.jna.platform.mac.IOKit.SMCVal;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.Util;
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
/*     */ public class SmcUtil
/*     */ {
/*  45 */   private static final Logger LOG = LoggerFactory.getLogger(SmcUtil.class);
/*     */   
/*  47 */   private static IOKit.IOConnect conn = new IOKit.IOConnect();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private static Map<Integer, IOKit.SMCKeyDataKeyInfo> keyInfoCache = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final byte[] DATATYPE_SP78 = ParseUtil.stringToByteArray("sp78", 5);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int smcOpen()
/*     */   {
/*  68 */     int service = IOKitUtil.getMatchingService("AppleSMC");
/*  69 */     if (service == 0) {
/*  70 */       LOG.error("Error: no SMC found");
/*  71 */       return 1;
/*     */     }
/*     */     
/*  74 */     int result = IOKit.INSTANCE.IOServiceOpen(service, SystemB.INSTANCE.mach_task_self(), 0, conn);
/*  75 */     IOKit.INSTANCE.IOObjectRelease(service);
/*  76 */     if (result != 0) {
/*  77 */       LOG.error(String.format("Error: IOServiceOpen() = 0x%08x", new Object[] { Integer.valueOf(result) }));
/*  78 */       return result;
/*     */     }
/*     */     
/*  81 */     Util.sleep(5L);
/*  82 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int smcClose()
/*     */   {
/*  91 */     return IOKit.INSTANCE.IOServiceClose(conn.getValue());
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
/*     */   public static double smcGetSp78(String key, int retries)
/*     */   {
/* 106 */     IOKit.SMCVal val = new IOKit.SMCVal();
/* 107 */     int result = smcReadKey(key, val, retries);
/* 108 */     if ((result == 0) && (val.dataSize > 0) && (Arrays.equals(val.dataType, DATATYPE_SP78))) {
/* 109 */       return val.bytes[0] + val.bytes[1] / 256.0D;
/*     */     }
/*     */     
/* 112 */     return 0.0D;
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
/*     */   public static long smcGetLong(String key, int retries)
/*     */   {
/* 125 */     IOKit.SMCVal val = new IOKit.SMCVal();
/* 126 */     int result = smcReadKey(key, val, retries);
/* 127 */     if (result == 0) {
/* 128 */       return ParseUtil.byteArrayToLong(val.bytes, val.dataSize);
/*     */     }
/*     */     
/* 131 */     return 0L;
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
/*     */   public static float smcGetFpe2(String key, int retries)
/*     */   {
/* 145 */     IOKit.SMCVal val = new IOKit.SMCVal();
/* 146 */     int result = smcReadKey(key, val, retries);
/* 147 */     if (result == 0) {
/* 148 */       return ParseUtil.byteArrayToFloat(val.bytes, val.dataSize, 2);
/*     */     }
/*     */     
/* 151 */     return 0.0F;
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
/*     */   public static int smcGetKeyInfo(IOKit.SMCKeyData inputStructure, IOKit.SMCKeyData outputStructure)
/*     */   {
/* 164 */     if (keyInfoCache.containsKey(Integer.valueOf(inputStructure.key))) {
/* 165 */       IOKit.SMCKeyDataKeyInfo keyInfo = (IOKit.SMCKeyDataKeyInfo)keyInfoCache.get(Integer.valueOf(inputStructure.key));
/* 166 */       outputStructure.keyInfo.dataSize = keyInfo.dataSize;
/* 167 */       outputStructure.keyInfo.dataType = keyInfo.dataType;
/* 168 */       outputStructure.keyInfo.dataAttributes = keyInfo.dataAttributes;
/*     */     } else {
/* 170 */       inputStructure.data8 = 9;
/* 171 */       Util.sleep(4L);
/* 172 */       int result = smcCall(2, inputStructure, outputStructure);
/* 173 */       if (result != 0) {
/* 174 */         return result;
/*     */       }
/* 176 */       IOKit.SMCKeyDataKeyInfo keyInfo = new IOKit.SMCKeyDataKeyInfo();
/* 177 */       keyInfo.dataSize = outputStructure.keyInfo.dataSize;
/* 178 */       keyInfo.dataType = outputStructure.keyInfo.dataType;
/* 179 */       keyInfo.dataAttributes = outputStructure.keyInfo.dataAttributes;
/* 180 */       keyInfoCache.put(Integer.valueOf(inputStructure.key), keyInfo);
/*     */     }
/* 182 */     return 0;
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
/*     */   public static int smcReadKey(String key, IOKit.SMCVal val, int retries)
/*     */   {
/* 197 */     IOKit.SMCKeyData inputStructure = new IOKit.SMCKeyData();
/* 198 */     IOKit.SMCKeyData outputStructure = new IOKit.SMCKeyData();
/*     */     
/* 200 */     inputStructure.key = ((int)ParseUtil.strToLong(key, 4));
/*     */     
/* 202 */     int retry = 0;
/*     */     int result;
/* 204 */     do { result = smcGetKeyInfo(inputStructure, outputStructure);
/* 205 */       if (result == 0)
/*     */       {
/*     */ 
/*     */ 
/* 209 */         val.dataSize = outputStructure.keyInfo.dataSize;
/* 210 */         val.dataType = ParseUtil.longToByteArray(outputStructure.keyInfo.dataType, 4, 5);
/*     */         
/* 212 */         inputStructure.keyInfo.dataSize = val.dataSize;
/* 213 */         inputStructure.data8 = 5;
/*     */         
/* 215 */         Util.sleep(4L);
/* 216 */         result = smcCall(2, inputStructure, outputStructure);
/*     */         
/* 218 */         if (result == 0)
/*     */           break;
/*     */       }
/* 221 */       retry++; } while (retry < retries);
/*     */     
/* 223 */     if (result != 0) {
/* 224 */       return result;
/*     */     }
/*     */     
/* 227 */     System.arraycopy(outputStructure.bytes, 0, val.bytes, 0, val.bytes.length);
/*     */     
/* 229 */     return 0;
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
/*     */   public static int smcCall(int index, IOKit.SMCKeyData inputStructure, IOKit.SMCKeyData outputStructure)
/*     */   {
/* 244 */     int structureInputSize = inputStructure.size();
/* 245 */     IntByReference structureOutputSizePtr = new IntByReference(outputStructure.size());
/*     */     
/* 247 */     int result = IOKit.INSTANCE.IOConnectCallStructMethod(conn.getValue(), index, inputStructure, structureInputSize, outputStructure, structureOutputSizePtr);
/*     */     
/* 249 */     if (result != 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 254 */       return result;
/*     */     }
/*     */     
/* 257 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\platform\mac\SmcUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */