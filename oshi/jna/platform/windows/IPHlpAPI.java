/*     */ package oshi.jna.platform.windows;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.Structure.ByReference;
/*     */ import com.sun.jna.platform.win32.Guid.GUID;
/*     */ import com.sun.jna.platform.win32.WinDef.UCHAR;
/*     */ import com.sun.jna.platform.win32.WinDef.UINT;
/*     */ import com.sun.jna.platform.win32.WinDef.ULONG;
/*     */ import com.sun.jna.platform.win32.WinDef.ULONGByReference;
/*     */ import java.util.Arrays;
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
/*     */ public abstract interface IPHlpAPI
/*     */   extends Library
/*     */ {
/*  41 */   public static final IPHlpAPI INSTANCE = (IPHlpAPI)Native.loadLibrary("IPHlpAPI", IPHlpAPI.class);
/*     */   public static final int IF_MAX_STRING_SIZE = 256;
/*     */   public static final int IF_MAX_PHYS_ADDRESS_LENGTH = 32;
/*     */   public static final int MAX_INTERFACE_NAME_LEN = 256;
/*     */   public static final int MAXLEN_IFDESCR = 256;
/*     */   public static final int MAXLEN_PHYSADDR = 8;
/*     */   public static final int MAX_HOSTNAME_LEN = 128;
/*     */   public static final int MAX_DOMAIN_NAME_LEN = 128;
/*     */   public static final int MAX_SCOPE_ID_LEN = 256;
/*     */   public static final int ERROR_BUFFER_OVERFLOW = 111;
/*     */   
/*     */   public abstract int GetIfEntry(MIB_IFROW paramMIB_IFROW);
/*     */   
/*     */   public static class MIB_IFROW extends Structure {
/*  55 */     public char[] wszName = new char['Ā'];
/*     */     public int dwIndex;
/*     */     public int dwType;
/*     */     public int dwMtu;
/*     */     public int dwSpeed;
/*     */     public int dwPhysAddrLen;
/*  61 */     public byte[] bPhysAddr = new byte[8];
/*     */     public int dwAdminStatus;
/*     */     public int dwOperStatus;
/*     */     public int dwLastChange;
/*     */     public int dwInOctets;
/*     */     public int dwInUcastPkts;
/*     */     public int dwInNUcastPkts;
/*     */     public int dwInDiscards;
/*     */     public int dwInErrors;
/*     */     public int dwInUnknownProtos;
/*     */     public int dwOutOctets;
/*     */     public int dwOutUcastPkts;
/*     */     public int dwOutNUcastPkts;
/*     */     public int dwOutDiscards;
/*     */     public int dwOutErrors;
/*     */     public int dwOutQLen;
/*     */     public int dwDescrLen;
/*  78 */     public byte[] bDescr = new byte['Ā'];
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/*  82 */       return Arrays.asList(new String[] { "wszName", "dwIndex", "dwType", "dwMtu", "dwSpeed", "dwPhysAddrLen", "bPhysAddr", "dwAdminStatus", "dwOperStatus", "dwLastChange", "dwInOctets", "dwInUcastPkts", "dwInNUcastPkts", "dwInDiscards", "dwInErrors", "dwInUnknownProtos", "dwOutOctets", "dwOutUcastPkts", "dwOutNUcastPkts", "dwOutDiscards", "dwOutErrors", "dwOutQLen", "dwDescrLen", "bDescr" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class MIB_IFROW2
/*     */     extends Structure
/*     */   {
/*     */     public long InterfaceLuid;
/*     */     
/*     */     public WinDef.ULONG InterfaceIndex;
/*     */     public Guid.GUID InterfaceGuid;
/*  94 */     public char[] Alias = new char['ā'];
/*  95 */     public char[] Description = new char['ā'];
/*     */     public WinDef.ULONG PhysicalAddressLength;
/*  97 */     public WinDef.UCHAR[] PhysicalAddress = new WinDef.UCHAR[32];
/*  98 */     public WinDef.UCHAR[] PermanentPhysicalAddress = new WinDef.UCHAR[32];
/*     */     
/*     */     public WinDef.ULONG Mtu;
/*     */     
/*     */     public WinDef.ULONG Type;
/*     */     
/*     */     public int TunnelType;
/*     */     public int MediaType;
/*     */     public int PhysicalMediumType;
/*     */     public int AccessType;
/*     */     public int DirectionType;
/*     */     public byte InterfaceAndOperStatusFlags;
/*     */     public int OperStatus;
/*     */     public int AdminStatus;
/*     */     public int MediaConnectState;
/*     */     public Guid.GUID NetworkGuid;
/*     */     public int ConnectionType;
/*     */     public long TransmitLinkSpeed;
/*     */     public long ReceiveLinkSpeed;
/*     */     public long InOctets;
/*     */     public long InUcastPkts;
/*     */     public long InNUcastPkts;
/*     */     public long InDiscards;
/*     */     public long InErrors;
/*     */     public long InUnknownProtos;
/*     */     public long InUcastOctets;
/*     */     public long InMulticastOctets;
/*     */     public long InBroadcastOctets;
/*     */     public long OutOctets;
/*     */     public long OutUcastPkts;
/*     */     public long OutNUcastPkts;
/*     */     public long OutDiscards;
/*     */     public long OutErrors;
/*     */     public long OutUcastOctets;
/*     */     public long OutMulticastOctets;
/*     */     public long OutBroadcastOctets;
/*     */     public long OutQLen;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 138 */       return Arrays.asList(new String[] { "InterfaceLuid", "InterfaceIndex", "InterfaceGuid", "Alias", "Description", "PhysicalAddressLength", "PhysicalAddress", "PermanentPhysicalAddress", "Mtu", "Type", "TunnelType", "MediaType", "PhysicalMediumType", "AccessType", "DirectionType", "InterfaceAndOperStatusFlags", "OperStatus", "AdminStatus", "MediaConnectState", "NetworkGuid", "ConnectionType", "TransmitLinkSpeed", "ReceiveLinkSpeed", "InOctets", "InUcastPkts", "InNUcastPkts", "InDiscards", "InErrors", "InUnknownProtos", "InUcastOctets", "InMulticastOctets", "InBroadcastOctets", "OutOctets", "OutUcastPkts", "OutNUcastPkts", "OutDiscards", "OutErrors", "OutUcastOctets", "OutMulticastOctets", "OutBroadcastOctets", "OutQLen" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int GetIfEntry2(MIB_IFROW2 paramMIB_IFROW2);
/*     */   
/*     */   public abstract int GetNetworkParams(FIXED_INFO paramFIXED_INFO, WinDef.ULONGByReference paramULONGByReference);
/*     */   
/*     */   public static class IP_ADDRESS_STRING
/*     */     extends Structure
/*     */   {
/* 150 */     public byte[] String = new byte[16];
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 154 */       return Arrays.asList(new String[] { "String" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class IP_ADDR_STRING
/*     */     extends Structure
/*     */   {
/*     */     public ByReference Next;
/*     */     
/*     */     public IPHlpAPI.IP_ADDRESS_STRING IpAddress;
/*     */     public IPHlpAPI.IP_ADDRESS_STRING IpMask;
/*     */     public int Context;
/*     */     
/* 169 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "Next", "IpAddress", "IpMask", "Context" }); }
/*     */     
/*     */     public static class ByReference extends IPHlpAPI.IP_ADDR_STRING implements Structure.ByReference
/*     */     {} }
/*     */   
/* 174 */   public static class FIXED_INFO extends Structure { public byte[] HostName = new byte[''];
/* 175 */     public byte[] DomainName = new byte[''];
/*     */     public IPHlpAPI.IP_ADDR_STRING.ByReference CurrentDnsServer;
/*     */     public IPHlpAPI.IP_ADDR_STRING DnsServerList;
/*     */     public WinDef.UINT NodeType;
/* 179 */     public byte[] ScopeId = new byte['Ą'];
/*     */     public WinDef.UINT EnableRouting;
/*     */     public WinDef.UINT EnableProxy;
/*     */     public WinDef.UINT EnableDns;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 186 */       return Arrays.asList(new String[] { "HostName", "DomainName", "CurrentDnsServer", "DnsServerList", "NodeType", "ScopeId", "EnableRouting", "EnableProxy", "EnableDns" });
/*     */     }
/*     */     
/*     */     public FIXED_INFO(Pointer p)
/*     */     {
/* 191 */       super();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\windows\IPHlpAPI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */