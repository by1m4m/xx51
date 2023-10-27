/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.channel.AddressedEnvelope;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.SocketAddress;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DnsMessageUtil
/*     */ {
/*     */   static StringBuilder appendQuery(StringBuilder buf, DnsQuery query)
/*     */   {
/*  29 */     appendQueryHeader(buf, query);
/*  30 */     appendAllRecords(buf, query);
/*  31 */     return buf;
/*     */   }
/*     */   
/*     */   static StringBuilder appendResponse(StringBuilder buf, DnsResponse response) {
/*  35 */     appendResponseHeader(buf, response);
/*  36 */     appendAllRecords(buf, response);
/*  37 */     return buf; }
/*     */   
/*     */   static StringBuilder appendRecordClass(StringBuilder buf, int dnsClass) { String name;
/*     */     String name;
/*     */     String name;
/*  42 */     String name; String name; String name; String name; switch (dnsClass &= 0xFFFF) {
/*     */     case 1: 
/*  44 */       name = "IN";
/*  45 */       break;
/*     */     case 2: 
/*  47 */       name = "CSNET";
/*  48 */       break;
/*     */     case 3: 
/*  50 */       name = "CHAOS";
/*  51 */       break;
/*     */     case 4: 
/*  53 */       name = "HESIOD";
/*  54 */       break;
/*     */     case 254: 
/*  56 */       name = "NONE";
/*  57 */       break;
/*     */     case 255: 
/*  59 */       name = "ANY";
/*  60 */       break;
/*     */     default: 
/*  62 */       name = null;
/*     */     }
/*     */     
/*     */     
/*  66 */     if (name != null) {
/*  67 */       buf.append(name);
/*     */     } else {
/*  69 */       buf.append("UNKNOWN(").append(dnsClass).append(')');
/*     */     }
/*     */     
/*  72 */     return buf;
/*     */   }
/*     */   
/*     */   private static void appendQueryHeader(StringBuilder buf, DnsQuery msg)
/*     */   {
/*  77 */     buf.append(StringUtil.simpleClassName(msg)).append('(');
/*     */     
/*  79 */     appendAddresses(buf, msg)
/*  80 */       .append(msg.id())
/*  81 */       .append(", ")
/*  82 */       .append(msg.opCode());
/*     */     
/*  84 */     if (msg.isRecursionDesired()) {
/*  85 */       buf.append(", RD");
/*     */     }
/*  87 */     if (msg.z() != 0)
/*     */     {
/*  89 */       buf.append(", Z: ").append(msg.z());
/*     */     }
/*  91 */     buf.append(')');
/*     */   }
/*     */   
/*     */   private static void appendResponseHeader(StringBuilder buf, DnsResponse msg)
/*     */   {
/*  96 */     buf.append(StringUtil.simpleClassName(msg)).append('(');
/*     */     
/*  98 */     appendAddresses(buf, msg)
/*  99 */       .append(msg.id())
/* 100 */       .append(", ")
/* 101 */       .append(msg.opCode())
/* 102 */       .append(", ")
/* 103 */       .append(msg.code())
/* 104 */       .append(',');
/*     */     
/* 106 */     boolean hasComma = true;
/* 107 */     if (msg.isRecursionDesired()) {
/* 108 */       hasComma = false;
/* 109 */       buf.append(" RD");
/*     */     }
/* 111 */     if (msg.isAuthoritativeAnswer()) {
/* 112 */       hasComma = false;
/* 113 */       buf.append(" AA");
/*     */     }
/* 115 */     if (msg.isTruncated()) {
/* 116 */       hasComma = false;
/* 117 */       buf.append(" TC");
/*     */     }
/* 119 */     if (msg.isRecursionAvailable()) {
/* 120 */       hasComma = false;
/* 121 */       buf.append(" RA");
/*     */     }
/* 123 */     if (msg.z() != 0) {
/* 124 */       if (!hasComma) {
/* 125 */         buf.append(',');
/*     */       }
/*     */       
/* 128 */       buf.append(" Z: ").append(msg.z());
/*     */     }
/*     */     
/* 131 */     if (hasComma) {
/* 132 */       buf.setCharAt(buf.length() - 1, ')');
/*     */     } else {
/* 134 */       buf.append(')');
/*     */     }
/*     */   }
/*     */   
/*     */   private static StringBuilder appendAddresses(StringBuilder buf, DnsMessage msg)
/*     */   {
/* 140 */     if (!(msg instanceof AddressedEnvelope)) {
/* 141 */       return buf;
/*     */     }
/*     */     
/*     */ 
/* 145 */     AddressedEnvelope<?, SocketAddress> envelope = (AddressedEnvelope)msg;
/*     */     
/* 147 */     SocketAddress addr = envelope.sender();
/* 148 */     if (addr != null)
/*     */     {
/*     */ 
/* 151 */       buf.append("from: ").append(addr).append(", ");
/*     */     }
/*     */     
/* 154 */     addr = envelope.recipient();
/* 155 */     if (addr != null)
/*     */     {
/*     */ 
/* 158 */       buf.append("to: ").append(addr).append(", ");
/*     */     }
/*     */     
/* 161 */     return buf;
/*     */   }
/*     */   
/*     */   private static void appendAllRecords(StringBuilder buf, DnsMessage msg) {
/* 165 */     appendRecords(buf, msg, DnsSection.QUESTION);
/* 166 */     appendRecords(buf, msg, DnsSection.ANSWER);
/* 167 */     appendRecords(buf, msg, DnsSection.AUTHORITY);
/* 168 */     appendRecords(buf, msg, DnsSection.ADDITIONAL);
/*     */   }
/*     */   
/*     */   private static void appendRecords(StringBuilder buf, DnsMessage message, DnsSection section) {
/* 172 */     int count = message.count(section);
/* 173 */     for (int i = 0; i < count; i++)
/*     */     {
/*     */ 
/* 176 */       buf.append(StringUtil.NEWLINE).append('\t').append(message.recordAt(section, i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\dns\DnsMessageUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */