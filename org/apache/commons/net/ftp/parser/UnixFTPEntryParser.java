/*     */ package org.apache.commons.net.ftp.parser;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import org.apache.commons.net.ftp.FTPClientConfig;
/*     */ import org.apache.commons.net.ftp.FTPFile;
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
/*     */ public class UnixFTPEntryParser
/*     */   extends ConfigurableFTPFileEntryParserImpl
/*     */ {
/*     */   static final String DEFAULT_DATE_FORMAT = "MMM d yyyy";
/*     */   static final String DEFAULT_RECENT_DATE_FORMAT = "MMM d HH:mm";
/*     */   static final String NUMERIC_DATE_FORMAT = "yyyy-MM-dd HH:mm";
/*     */   private static final String JA_MONTH = "月";
/*     */   private static final String JA_DAY = "日";
/*     */   private static final String JA_YEAR = "年";
/*     */   private static final String DEFAULT_DATE_FORMAT_JA = "M'月' d'日' yyyy'年'";
/*     */   private static final String DEFAULT_RECENT_DATE_FORMAT_JA = "M'月' d'日' HH:mm";
/*  71 */   public static final FTPClientConfig NUMERIC_DATE_CONFIG = new FTPClientConfig("UNIX", "yyyy-MM-dd HH:mm", null);
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
/*     */   private static final String REGEX = "([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3})|(?:\\d{1,2}月\\s+\\d{1,2}日))\\s+((?:\\d+(?::\\d+)?)|(?:\\d{4}年))\\s(.*)";
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
/*     */   final boolean trimLeadingSpaces;
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
/*     */   public UnixFTPEntryParser()
/*     */   {
/* 163 */     this(null);
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
/*     */   public UnixFTPEntryParser(FTPClientConfig config)
/*     */   {
/* 180 */     this(config, false);
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
/*     */   public UnixFTPEntryParser(FTPClientConfig config, boolean trimLeadingSpaces)
/*     */   {
/* 198 */     super("([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s*(\\d+)\\s+(?:(\\S+(?:\\s\\S+)*?)\\s+)?(?:(\\S+(?:\\s\\S+)*)\\s+)?(\\d+(?:,\\s*\\d+)?)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3})|(?:\\d{1,2}月\\s+\\d{1,2}日))\\s+((?:\\d+(?::\\d+)?)|(?:\\d{4}年))\\s(.*)");
/* 199 */     configure(config);
/* 200 */     this.trimLeadingSpaces = trimLeadingSpaces;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> preParse(List<String> original)
/*     */   {
/* 208 */     ListIterator<String> iter = original.listIterator();
/* 209 */     while (iter.hasNext()) {
/* 210 */       String entry = (String)iter.next();
/* 211 */       if (entry.matches("^total \\d+$")) {
/* 212 */         iter.remove();
/*     */       }
/*     */     }
/* 215 */     return original;
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
/*     */   public FTPFile parseFTPEntry(String entry)
/*     */   {
/* 230 */     FTPFile file = new FTPFile();
/* 231 */     file.setRawListing(entry);
/*     */     
/* 233 */     boolean isDevice = false;
/*     */     
/* 235 */     if (matches(entry))
/*     */     {
/* 237 */       String typeStr = group(1);
/* 238 */       String hardLinkCount = group(15);
/* 239 */       String usr = group(16);
/* 240 */       String grp = group(17);
/* 241 */       String filesize = group(18);
/* 242 */       String datestr = group(19) + " " + group(20);
/* 243 */       String name = group(21);
/* 244 */       if (this.trimLeadingSpaces) {
/* 245 */         name = name.replaceFirst("^\\s+", "");
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 250 */         if (group(19).contains("月")) {
/* 251 */           FTPTimestampParserImpl jaParser = new FTPTimestampParserImpl();
/* 252 */           jaParser.configure(new FTPClientConfig("UNIX", "M'月' d'日' yyyy'年'", "M'月' d'日' HH:mm"));
/*     */           
/* 254 */           file.setTimestamp(jaParser.parseTimestamp(datestr));
/*     */         } else {
/* 256 */           file.setTimestamp(super.parseTimestamp(datestr));
/*     */         }
/*     */       }
/*     */       catch (ParseException e) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       int type;
/*     */       
/*     */ 
/*     */ 
/* 268 */       switch (typeStr.charAt(0))
/*     */       {
/*     */       case 'd': 
/* 271 */         type = 1;
/* 272 */         break;
/*     */       case 'e': 
/* 274 */         type = 2;
/* 275 */         break;
/*     */       case 'l': 
/* 277 */         type = 2;
/* 278 */         break;
/*     */       case 'b': 
/*     */       case 'c': 
/* 281 */         isDevice = true;
/* 282 */         type = 0;
/* 283 */         break;
/*     */       case '-': 
/*     */       case 'f': 
/* 286 */         type = 0;
/* 287 */         break;
/*     */       default: 
/* 289 */         type = 3;
/*     */       }
/*     */       
/* 292 */       file.setType(type);
/*     */       
/* 294 */       int g = 4;
/* 295 */       for (int access = 0; access < 3; g += 4)
/*     */       {
/*     */ 
/* 298 */         file.setPermission(access, 0, !group(g).equals("-"));
/*     */         
/* 300 */         file.setPermission(access, 1, !group(g + 1).equals("-"));
/*     */         
/*     */ 
/* 303 */         String execPerm = group(g + 2);
/* 304 */         if ((!execPerm.equals("-")) && (!Character.isUpperCase(execPerm.charAt(0))))
/*     */         {
/* 306 */           file.setPermission(access, 2, true);
/*     */         }
/*     */         else
/*     */         {
/* 310 */           file.setPermission(access, 2, false);
/*     */         }
/* 295 */         access++;
/*     */       }
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
/* 314 */       if (!isDevice)
/*     */       {
/*     */         try
/*     */         {
/* 318 */           file.setHardLinkCount(Integer.parseInt(hardLinkCount));
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 326 */       file.setUser(usr);
/* 327 */       file.setGroup(grp);
/*     */       
/*     */       try
/*     */       {
/* 331 */         file.setSize(Long.parseLong(filesize));
/*     */       }
/*     */       catch (NumberFormatException e) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 340 */       if (type == 2)
/*     */       {
/*     */ 
/* 343 */         int end = name.indexOf(" -> ");
/*     */         
/* 345 */         if (end == -1)
/*     */         {
/* 347 */           file.setName(name);
/*     */         }
/*     */         else
/*     */         {
/* 351 */           file.setName(name.substring(0, end));
/* 352 */           file.setLink(name.substring(end + 4));
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 358 */         file.setName(name);
/*     */       }
/* 360 */       return file;
/*     */     }
/* 362 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FTPClientConfig getDefaultConfiguration()
/*     */   {
/* 373 */     return new FTPClientConfig("UNIX", "MMM d yyyy", "MMM d HH:mm");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\parser\UnixFTPEntryParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */