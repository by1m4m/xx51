/*     */ package io.netty.handler.codec.haproxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.util.ByteProcessor;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HAProxyMessage
/*     */ {
/*  37 */   private static final HAProxyMessage V1_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private static final HAProxyMessage V2_UNKNOWN_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private static final HAProxyMessage V2_LOCAL_MSG = new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.LOCAL, HAProxyProxiedProtocol.UNKNOWN, null, null, 0, 0);
/*     */   
/*     */   private final HAProxyProtocolVersion protocolVersion;
/*     */   
/*     */   private final HAProxyCommand command;
/*     */   
/*     */   private final HAProxyProxiedProtocol proxiedProtocol;
/*     */   
/*     */   private final String sourceAddress;
/*     */   
/*     */   private final String destinationAddress;
/*     */   
/*     */   private final int sourcePort;
/*     */   private final int destinationPort;
/*     */   private final List<HAProxyTLV> tlvs;
/*     */   
/*     */   private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, String sourcePort, String destinationPort)
/*     */   {
/*  69 */     this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, 
/*     */     
/*  71 */       portStringToInt(sourcePort), portStringToInt(destinationPort));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, int sourcePort, int destinationPort)
/*     */   {
/*  81 */     this(protocolVersion, command, proxiedProtocol, sourceAddress, destinationAddress, sourcePort, destinationPort, 
/*  82 */       Collections.emptyList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HAProxyMessage(HAProxyProtocolVersion protocolVersion, HAProxyCommand command, HAProxyProxiedProtocol proxiedProtocol, String sourceAddress, String destinationAddress, int sourcePort, int destinationPort, List<HAProxyTLV> tlvs)
/*     */   {
/*  93 */     if (proxiedProtocol == null) {
/*  94 */       throw new NullPointerException("proxiedProtocol");
/*     */     }
/*  96 */     HAProxyProxiedProtocol.AddressFamily addrFamily = proxiedProtocol.addressFamily();
/*     */     
/*  98 */     checkAddress(sourceAddress, addrFamily);
/*  99 */     checkAddress(destinationAddress, addrFamily);
/* 100 */     checkPort(sourcePort);
/* 101 */     checkPort(destinationPort);
/*     */     
/* 103 */     this.protocolVersion = protocolVersion;
/* 104 */     this.command = command;
/* 105 */     this.proxiedProtocol = proxiedProtocol;
/* 106 */     this.sourceAddress = sourceAddress;
/* 107 */     this.destinationAddress = destinationAddress;
/* 108 */     this.sourcePort = sourcePort;
/* 109 */     this.destinationPort = destinationPort;
/* 110 */     this.tlvs = Collections.unmodifiableList(tlvs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static HAProxyMessage decodeHeader(ByteBuf header)
/*     */   {
/* 121 */     if (header == null) {
/* 122 */       throw new NullPointerException("header");
/*     */     }
/*     */     
/* 125 */     if (header.readableBytes() < 16)
/*     */     {
/* 127 */       throw new HAProxyProtocolException("incomplete header: " + header.readableBytes() + " bytes (expected: 16+ bytes)");
/*     */     }
/*     */     
/*     */ 
/* 131 */     header.skipBytes(12);
/* 132 */     byte verCmdByte = header.readByte();
/*     */     
/*     */     try
/*     */     {
/* 136 */       ver = HAProxyProtocolVersion.valueOf(verCmdByte);
/*     */     } catch (IllegalArgumentException e) { HAProxyProtocolVersion ver;
/* 138 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     HAProxyProtocolVersion ver;
/* 141 */     if (ver != HAProxyProtocolVersion.V2) {
/* 142 */       throw new HAProxyProtocolException("version 1 unsupported: 0x" + Integer.toHexString(verCmdByte));
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 147 */       cmd = HAProxyCommand.valueOf(verCmdByte);
/*     */     } catch (IllegalArgumentException e) { HAProxyCommand cmd;
/* 149 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     HAProxyCommand cmd;
/* 152 */     if (cmd == HAProxyCommand.LOCAL) {
/* 153 */       return V2_LOCAL_MSG;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 159 */       protAndFam = HAProxyProxiedProtocol.valueOf(header.readByte());
/*     */     } catch (IllegalArgumentException e) { HAProxyProxiedProtocol protAndFam;
/* 161 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     HAProxyProxiedProtocol protAndFam;
/* 164 */     if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
/* 165 */       return V2_UNKNOWN_MSG;
/*     */     }
/*     */     
/* 168 */     int addressInfoLen = header.readUnsignedShort();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 173 */     int srcPort = 0;
/* 174 */     int dstPort = 0;
/*     */     
/* 176 */     HAProxyProxiedProtocol.AddressFamily addressFamily = protAndFam.addressFamily();
/*     */     String srcAddress;
/* 178 */     String dstAddress; if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_UNIX)
/*     */     {
/* 180 */       if ((addressInfoLen < 216) || (header.readableBytes() < 216))
/*     */       {
/*     */ 
/* 183 */         throw new HAProxyProtocolException("incomplete UNIX socket address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 216+ bytes)");
/*     */       }
/* 185 */       int startIdx = header.readerIndex();
/* 186 */       int addressEnd = header.forEachByte(startIdx, 108, ByteProcessor.FIND_NUL);
/* 187 */       int addressLen; int addressLen; if (addressEnd == -1) {
/* 188 */         addressLen = 108;
/*     */       } else {
/* 190 */         addressLen = addressEnd - startIdx;
/*     */       }
/* 192 */       String srcAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
/*     */       
/* 194 */       startIdx += 108;
/*     */       
/* 196 */       addressEnd = header.forEachByte(startIdx, 108, ByteProcessor.FIND_NUL);
/* 197 */       if (addressEnd == -1) {
/* 198 */         addressLen = 108;
/*     */       } else {
/* 200 */         addressLen = addressEnd - startIdx;
/*     */       }
/* 202 */       String dstAddress = header.toString(startIdx, addressLen, CharsetUtil.US_ASCII);
/*     */       
/*     */ 
/* 205 */       header.readerIndex(startIdx + 108);
/*     */     } else { int addressLen;
/* 207 */       if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv4)
/*     */       {
/* 209 */         if ((addressInfoLen < 12) || (header.readableBytes() < 12))
/*     */         {
/*     */ 
/* 212 */           throw new HAProxyProtocolException("incomplete IPv4 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 12+ bytes)");
/*     */         }
/* 214 */         addressLen = 4; } else { int addressLen;
/* 215 */         if (addressFamily == HAProxyProxiedProtocol.AddressFamily.AF_IPv6)
/*     */         {
/* 217 */           if ((addressInfoLen < 36) || (header.readableBytes() < 36))
/*     */           {
/*     */ 
/* 220 */             throw new HAProxyProtocolException("incomplete IPv6 address information: " + Math.min(addressInfoLen, header.readableBytes()) + " bytes (expected: 36+ bytes)");
/*     */           }
/* 222 */           addressLen = 16;
/*     */         } else {
/* 224 */           throw new HAProxyProtocolException("unable to parse address information (unknown address family: " + addressFamily + ')');
/*     */         }
/*     */       }
/*     */       
/*     */       int addressLen;
/* 229 */       srcAddress = ipBytesToString(header, addressLen);
/* 230 */       dstAddress = ipBytesToString(header, addressLen);
/* 231 */       srcPort = header.readUnsignedShort();
/* 232 */       dstPort = header.readUnsignedShort();
/*     */     }
/*     */     
/* 235 */     List<HAProxyTLV> tlvs = readTlvs(header);
/*     */     
/* 237 */     return new HAProxyMessage(ver, cmd, protAndFam, srcAddress, dstAddress, srcPort, dstPort, tlvs);
/*     */   }
/*     */   
/*     */   private static List<HAProxyTLV> readTlvs(ByteBuf header) {
/* 241 */     HAProxyTLV haProxyTLV = readNextTLV(header);
/* 242 */     if (haProxyTLV == null) {
/* 243 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 246 */     List<HAProxyTLV> haProxyTLVs = new ArrayList(4);
/*     */     do
/*     */     {
/* 249 */       haProxyTLVs.add(haProxyTLV);
/* 250 */       if ((haProxyTLV instanceof HAProxySSLTLV)) {
/* 251 */         haProxyTLVs.addAll(((HAProxySSLTLV)haProxyTLV).encapsulatedTLVs());
/*     */       }
/* 253 */     } while ((haProxyTLV = readNextTLV(header)) != null);
/* 254 */     return haProxyTLVs;
/*     */   }
/*     */   
/*     */ 
/*     */   private static HAProxyTLV readNextTLV(ByteBuf header)
/*     */   {
/* 260 */     if (header.readableBytes() < 4) {
/* 261 */       return null;
/*     */     }
/*     */     
/* 264 */     byte typeAsByte = header.readByte();
/* 265 */     HAProxyTLV.Type type = HAProxyTLV.Type.typeForByteValue(typeAsByte);
/*     */     
/* 267 */     int length = header.readUnsignedShort();
/* 268 */     switch (type) {
/*     */     case PP2_TYPE_SSL: 
/* 270 */       ByteBuf rawContent = header.retainedSlice(header.readerIndex(), length);
/* 271 */       ByteBuf byteBuf = header.readSlice(length);
/* 272 */       byte client = byteBuf.readByte();
/* 273 */       int verify = byteBuf.readInt();
/*     */       
/* 275 */       if (byteBuf.readableBytes() >= 4)
/*     */       {
/* 277 */         List<HAProxyTLV> encapsulatedTlvs = new ArrayList(4);
/*     */         do {
/* 279 */           HAProxyTLV haProxyTLV = readNextTLV(byteBuf);
/* 280 */           if (haProxyTLV == null) {
/*     */             break;
/*     */           }
/* 283 */           encapsulatedTlvs.add(haProxyTLV);
/* 284 */         } while (byteBuf.readableBytes() >= 4);
/*     */         
/* 286 */         return new HAProxySSLTLV(verify, client, encapsulatedTlvs, rawContent);
/*     */       }
/* 288 */       return new HAProxySSLTLV(verify, client, Collections.emptyList(), rawContent);
/*     */     
/*     */     case PP2_TYPE_ALPN: 
/*     */     case PP2_TYPE_AUTHORITY: 
/*     */     case PP2_TYPE_SSL_VERSION: 
/*     */     case PP2_TYPE_SSL_CN: 
/*     */     case PP2_TYPE_NETNS: 
/*     */     case OTHER: 
/* 296 */       return new HAProxyTLV(type, typeAsByte, header.readRetainedSlice(length));
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static HAProxyMessage decodeHeader(String header)
/*     */   {
/* 310 */     if (header == null) {
/* 311 */       throw new HAProxyProtocolException("header");
/*     */     }
/*     */     
/* 314 */     String[] parts = header.split(" ");
/* 315 */     int numParts = parts.length;
/*     */     
/* 317 */     if (numParts < 2) {
/* 318 */       throw new HAProxyProtocolException("invalid header: " + header + " (expected: 'PROXY' and proxied protocol values)");
/*     */     }
/*     */     
/*     */ 
/* 322 */     if (!"PROXY".equals(parts[0])) {
/* 323 */       throw new HAProxyProtocolException("unknown identifier: " + parts[0]);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 328 */       protAndFam = HAProxyProxiedProtocol.valueOf(parts[1]);
/*     */     } catch (IllegalArgumentException e) { HAProxyProxiedProtocol protAndFam;
/* 330 */       throw new HAProxyProtocolException(e);
/*     */     }
/*     */     HAProxyProxiedProtocol protAndFam;
/* 333 */     if ((protAndFam != HAProxyProxiedProtocol.TCP4) && (protAndFam != HAProxyProxiedProtocol.TCP6) && (protAndFam != HAProxyProxiedProtocol.UNKNOWN))
/*     */     {
/*     */ 
/* 336 */       throw new HAProxyProtocolException("unsupported v1 proxied protocol: " + parts[1]);
/*     */     }
/*     */     
/* 339 */     if (protAndFam == HAProxyProxiedProtocol.UNKNOWN) {
/* 340 */       return V1_UNKNOWN_MSG;
/*     */     }
/*     */     
/* 343 */     if (numParts != 6) {
/* 344 */       throw new HAProxyProtocolException("invalid TCP4/6 header: " + header + " (expected: 6 parts)");
/*     */     }
/*     */     
/* 347 */     return new HAProxyMessage(HAProxyProtocolVersion.V1, HAProxyCommand.PROXY, protAndFam, parts[2], parts[3], parts[4], parts[5]);
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
/*     */   private static String ipBytesToString(ByteBuf header, int addressLen)
/*     */   {
/* 360 */     StringBuilder sb = new StringBuilder();
/* 361 */     if (addressLen == 4) {
/* 362 */       sb.append(header.readByte() & 0xFF);
/* 363 */       sb.append('.');
/* 364 */       sb.append(header.readByte() & 0xFF);
/* 365 */       sb.append('.');
/* 366 */       sb.append(header.readByte() & 0xFF);
/* 367 */       sb.append('.');
/* 368 */       sb.append(header.readByte() & 0xFF);
/*     */     } else {
/* 370 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 371 */       sb.append(':');
/* 372 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 373 */       sb.append(':');
/* 374 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 375 */       sb.append(':');
/* 376 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 377 */       sb.append(':');
/* 378 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 379 */       sb.append(':');
/* 380 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 381 */       sb.append(':');
/* 382 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/* 383 */       sb.append(':');
/* 384 */       sb.append(Integer.toHexString(header.readUnsignedShort()));
/*     */     }
/* 386 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int portStringToInt(String value)
/*     */   {
/*     */     try
/*     */     {
/* 399 */       port = Integer.parseInt(value);
/*     */     } catch (NumberFormatException e) { int port;
/* 401 */       throw new HAProxyProtocolException("invalid port: " + value, e);
/*     */     }
/*     */     int port;
/* 404 */     if ((port <= 0) || (port > 65535)) {
/* 405 */       throw new HAProxyProtocolException("invalid port: " + value + " (expected: 1 ~ 65535)");
/*     */     }
/*     */     
/* 408 */     return port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void checkAddress(String address, HAProxyProxiedProtocol.AddressFamily addrFamily)
/*     */   {
/* 419 */     if (addrFamily == null) {
/* 420 */       throw new NullPointerException("addrFamily");
/*     */     }
/*     */     
/* 423 */     switch (addrFamily) {
/*     */     case AF_UNSPEC: 
/* 425 */       if (address != null) {
/* 426 */         throw new HAProxyProtocolException("unable to validate an AF_UNSPEC address: " + address);
/*     */       }
/* 428 */       return;
/*     */     case AF_UNIX: 
/* 430 */       return;
/*     */     }
/*     */     
/* 433 */     if (address == null) {
/* 434 */       throw new NullPointerException("address");
/*     */     }
/*     */     
/* 437 */     switch (addrFamily) {
/*     */     case AF_IPv4: 
/* 439 */       if (!NetUtil.isValidIpV4Address(address)) {
/* 440 */         throw new HAProxyProtocolException("invalid IPv4 address: " + address);
/*     */       }
/*     */       break;
/*     */     case AF_IPv6: 
/* 444 */       if (!NetUtil.isValidIpV6Address(address)) {
/* 445 */         throw new HAProxyProtocolException("invalid IPv6 address: " + address);
/*     */       }
/*     */       break;
/*     */     default: 
/* 449 */       throw new Error();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void checkPort(int port)
/*     */   {
/* 460 */     if ((port < 0) || (port > 65535)) {
/* 461 */       throw new HAProxyProtocolException("invalid port: " + port + " (expected: 1 ~ 65535)");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyProtocolVersion protocolVersion()
/*     */   {
/* 469 */     return this.protocolVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyCommand command()
/*     */   {
/* 476 */     return this.command;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HAProxyProxiedProtocol proxiedProtocol()
/*     */   {
/* 483 */     return this.proxiedProtocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String sourceAddress()
/*     */   {
/* 490 */     return this.sourceAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String destinationAddress()
/*     */   {
/* 497 */     return this.destinationAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int sourcePort()
/*     */   {
/* 504 */     return this.sourcePort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int destinationPort()
/*     */   {
/* 511 */     return this.destinationPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HAProxyTLV> tlvs()
/*     */   {
/* 520 */     return this.tlvs;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\haproxy\HAProxyMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */