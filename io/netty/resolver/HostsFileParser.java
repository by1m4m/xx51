/*     */ package io.netty.resolver;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HostsFileParser
/*     */ {
/*     */   private static final String WINDOWS_DEFAULT_SYSTEM_ROOT = "C:\\Windows";
/*     */   private static final String WINDOWS_HOSTS_FILE_RELATIVE_PATH = "\\system32\\drivers\\etc\\hosts";
/*     */   private static final String X_PLATFORMS_HOSTS_FILE_PATH = "/etc/hosts";
/*  53 */   private static final Pattern WHITESPACES = Pattern.compile("[ \t]+");
/*     */   
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(HostsFileParser.class);
/*     */   
/*     */   private static File locateHostsFile() {
/*     */     File hostsFile;
/*  59 */     if (PlatformDependent.isWindows()) {
/*  60 */       File hostsFile = new File(System.getenv("SystemRoot") + "\\system32\\drivers\\etc\\hosts");
/*  61 */       if (!hostsFile.exists()) {
/*  62 */         hostsFile = new File("C:\\Windows\\system32\\drivers\\etc\\hosts");
/*     */       }
/*     */     } else {
/*  65 */       hostsFile = new File("/etc/hosts");
/*     */     }
/*  67 */     return hostsFile;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parseSilently()
/*     */   {
/*  76 */     return parseSilently(new Charset[] { Charset.defaultCharset() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parseSilently(Charset... charsets)
/*     */   {
/*  87 */     File hostsFile = locateHostsFile();
/*     */     try {
/*  89 */       return parse(hostsFile, charsets);
/*     */     } catch (IOException e) {
/*  91 */       if (logger.isWarnEnabled())
/*  92 */         logger.warn("Failed to load and parse hosts file at " + hostsFile.getPath(), e);
/*     */     }
/*  94 */     return HostsFileEntries.EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parse()
/*     */     throws IOException
/*     */   {
/* 105 */     return parse(locateHostsFile());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parse(File file)
/*     */     throws IOException
/*     */   {
/* 116 */     return parse(file, new Charset[] { Charset.defaultCharset() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parse(File file, Charset... charsets)
/*     */     throws IOException
/*     */   {
/* 128 */     ObjectUtil.checkNotNull(file, "file");
/* 129 */     ObjectUtil.checkNotNull(charsets, "charsets");
/* 130 */     if ((file.exists()) && (file.isFile())) {
/* 131 */       for (Charset charset : charsets) {
/* 132 */         HostsFileEntries entries = parse(new BufferedReader(new InputStreamReader(new FileInputStream(file), charset)));
/*     */         
/* 134 */         if (entries != HostsFileEntries.EMPTY) {
/* 135 */           return entries;
/*     */         }
/*     */       }
/*     */     }
/* 139 */     return HostsFileEntries.EMPTY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HostsFileEntries parse(Reader reader)
/*     */     throws IOException
/*     */   {
/* 150 */     ObjectUtil.checkNotNull(reader, "reader");
/* 151 */     BufferedReader buff = new BufferedReader(reader);
/*     */     try {
/* 153 */       Map<String, Inet4Address> ipv4Entries = new HashMap();
/* 154 */       Map<String, Inet6Address> ipv6Entries = new HashMap();
/*     */       String line;
/* 156 */       int commentPosition; while ((line = buff.readLine()) != null)
/*     */       {
/* 158 */         commentPosition = line.indexOf('#');
/* 159 */         if (commentPosition != -1) {
/* 160 */           line = line.substring(0, commentPosition);
/*     */         }
/*     */         
/* 163 */         line = line.trim();
/* 164 */         if (!line.isEmpty())
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 169 */           List<String> lineParts = new ArrayList();
/* 170 */           for (String s : WHITESPACES.split(line)) {
/* 171 */             if (!s.isEmpty()) {
/* 172 */               lineParts.add(s);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 177 */           if (lineParts.size() >= 2)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 182 */             byte[] ipBytes = NetUtil.createByteArrayFromIpAddressString((String)lineParts.get(0));
/*     */             
/* 184 */             if (ipBytes != null)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */               for (int i = 1; i < lineParts.size(); i++) {
/* 191 */                 String hostname = (String)lineParts.get(i);
/* 192 */                 String hostnameLower = hostname.toLowerCase(Locale.ENGLISH);
/* 193 */                 InetAddress address = InetAddress.getByAddress(hostname, ipBytes);
/* 194 */                 if ((address instanceof Inet4Address)) {
/* 195 */                   Inet4Address previous = (Inet4Address)ipv4Entries.put(hostnameLower, (Inet4Address)address);
/* 196 */                   if (previous != null)
/*     */                   {
/* 198 */                     ipv4Entries.put(hostnameLower, previous);
/*     */                   }
/*     */                 } else {
/* 201 */                   Inet6Address previous = (Inet6Address)ipv6Entries.put(hostnameLower, (Inet6Address)address);
/* 202 */                   if (previous != null)
/*     */                   {
/* 204 */                     ipv6Entries.put(hostnameLower, previous); }
/*     */                 }
/*     */               } }
/*     */           }
/*     */         } }
/* 209 */       return (ipv4Entries.isEmpty()) && (ipv6Entries.isEmpty()) ? HostsFileEntries.EMPTY : new HostsFileEntries(ipv4Entries, ipv6Entries);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/* 214 */         buff.close();
/*     */       } catch (IOException e) {
/* 216 */         logger.warn("Failed to close a reader", e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\HostsFileParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */