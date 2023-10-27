/*     */ package oshi.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import javax.xml.bind.DatatypeConverter;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class EdidUtil
/*     */ {
/*  37 */   private static final Logger LOG = LoggerFactory.getLogger(EdidUtil.class);
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
/*     */   public static String toHexString(byte[] edid)
/*     */   {
/*  50 */     return DatatypeConverter.printHexBinary(edid);
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
/*     */   public static String getManufacturerID(byte[] edid)
/*     */   {
/*  64 */     String temp = String.format("%8s%8s", new Object[] { Integer.toBinaryString(edid[8] & 0xFF), Integer.toBinaryString(edid[9] & 0xFF) }).replace(' ', '0');
/*  65 */     LOG.debug("Manufacurer ID: {}", temp);
/*     */     
/*     */ 
/*  68 */     return String.format("%s%s%s", new Object[] { Character.valueOf((char)(64 + Integer.parseInt(temp.substring(1, 6), 2))), Character.valueOf((char)(64 + Integer.parseInt(temp.substring(7, 11), 2))), Character.valueOf((char)(64 + Integer.parseInt(temp.substring(12, 16), 2))) }).replace("@", "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getProductID(byte[] edid)
/*     */   {
/*  80 */     return Integer.toHexString(
/*  81 */       ByteBuffer.wrap(Arrays.copyOfRange(edid, 10, 12)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
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
/*     */   public static String getSerialNo(byte[] edid)
/*     */   {
/*  94 */     LOG.debug("Serial number: {}", Arrays.toString(Arrays.copyOfRange(edid, 12, 16)));
/*  95 */     return String.format("%s%s%s%s", new Object[] { getAlphaNumericOrHex(edid[15]), getAlphaNumericOrHex(edid[14]), 
/*  96 */       getAlphaNumericOrHex(edid[13]), getAlphaNumericOrHex(edid[12]) });
/*     */   }
/*     */   
/*     */   private static String getAlphaNumericOrHex(byte b) {
/* 100 */     return Character.isLetterOrDigit((char)b) ? String.format("%s", new Object[] { Character.valueOf((char)b) }) : String.format("%02X", new Object[] { Byte.valueOf(b) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte getWeek(byte[] edid)
/*     */   {
/* 112 */     return edid[16];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getYear(byte[] edid)
/*     */   {
/* 124 */     byte temp = edid[17];
/* 125 */     LOG.debug("Year-1990: {}", Byte.valueOf(temp));
/* 126 */     return temp + 1990;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getVersion(byte[] edid)
/*     */   {
/* 138 */     return edid[18] + "." + edid[19];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isDigital(byte[] edid)
/*     */   {
/* 150 */     return 1 == edid[20] >> 7;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getHcm(byte[] edid)
/*     */   {
/* 162 */     return edid[21];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getVcm(byte[] edid)
/*     */   {
/* 174 */     return edid[22];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static byte[][] getDescriptors(byte[] edid)
/*     */   {
/* 186 */     byte[][] desc = new byte[4][18];
/* 187 */     for (int i = 0; i < desc.length; i++) {
/* 188 */       System.arraycopy(edid, 54 + 18 * i, desc[i], 0, 18);
/*     */     }
/* 190 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getDescriptorType(byte[] desc)
/*     */   {
/* 202 */     return ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 4)).getInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getTimingDescriptor(byte[] desc)
/*     */   {
/* 213 */     int clock = ByteBuffer.wrap(Arrays.copyOfRange(desc, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
/* 214 */     int hActive = (desc[2] & 0xFF) + (desc[4] & 0xF0) << 4;
/* 215 */     int vActive = (desc[5] & 0xFF) + (desc[7] & 0xF0) << 4;
/* 216 */     return String.format("Clock %dMHz, Active Pixels %dx%d ", new Object[] { Integer.valueOf(clock), Integer.valueOf(hActive), Integer.valueOf(vActive) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getDescriptorRangeLimits(byte[] desc)
/*     */   {
/* 227 */     return String.format("Field Rate %d-%d Hz vertical, %d-%d Hz horizontal, Max clock: %d MHz", new Object[] { Byte.valueOf(desc[5]), Byte.valueOf(desc[6]), 
/* 228 */       Byte.valueOf(desc[7]), Byte.valueOf(desc[8]), Integer.valueOf(desc[9] * 10) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getDescriptorText(byte[] desc)
/*     */   {
/* 239 */     return new String(Arrays.copyOfRange(desc, 4, 18)).trim();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getDescriptorHex(byte[] desc)
/*     */   {
/* 250 */     return DatatypeConverter.printHexBinary(desc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(byte[] edid)
/*     */   {
/* 261 */     StringBuilder sb = new StringBuilder();
/* 262 */     sb.append("  Manuf. ID=").append(getManufacturerID(edid)).append(", Product ID=")
/* 263 */       .append(getProductID(edid)).append(", ")
/* 264 */       .append(isDigital(edid) ? "Digital" : "Analog").append(", Serial=")
/* 265 */       .append(getSerialNo(edid)).append(", ManufDate=")
/* 266 */       .append(getWeek(edid) * 12 / 52 + 1 + "/").append(getYear(edid)).append(", EDID v")
/* 267 */       .append(getVersion(edid));
/* 268 */     int hSize = getHcm(edid);
/* 269 */     int vSize = getVcm(edid);
/* 270 */     sb.append(String.format("%n  %d x %d cm (%.1f x %.1f in)", new Object[] { Integer.valueOf(hSize), Integer.valueOf(vSize), Double.valueOf(hSize / 2.54D), Double.valueOf(vSize / 2.54D) }));
/* 271 */     byte[][] desc = getDescriptors(edid);
/* 272 */     for (byte[] b : desc) {
/* 273 */       switch (getDescriptorType(b)) {
/*     */       case 255: 
/* 275 */         sb.append("\n  Serial Number: ").append(getDescriptorText(b));
/* 276 */         break;
/*     */       case 254: 
/* 278 */         sb.append("\n  Unspecified Text: ").append(getDescriptorText(b));
/* 279 */         break;
/*     */       case 253: 
/* 281 */         sb.append("\n  Range Limits: ").append(getDescriptorRangeLimits(b));
/* 282 */         break;
/*     */       case 252: 
/* 284 */         sb.append("\n  Monitor Name: ").append(getDescriptorText(b));
/* 285 */         break;
/*     */       case 251: 
/* 287 */         sb.append("\n  White Point Data: ").append(getDescriptorHex(b));
/* 288 */         break;
/*     */       case 250: 
/* 290 */         sb.append("\n  Standard Timing ID: ").append(getDescriptorHex(b));
/* 291 */         break;
/*     */       default: 
/* 293 */         if ((getDescriptorType(b) <= 15) && (getDescriptorType(b) >= 0)) {
/* 294 */           sb.append("\n  Manufacturer Data: ").append(getDescriptorHex(b));
/*     */         } else {
/* 296 */           sb.append("\n  Preferred Timing: ").append(getTimingDescriptor(b));
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 301 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\EdidUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */