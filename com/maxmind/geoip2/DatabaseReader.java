/*     */ package com.maxmind.geoip2;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.InjectableValues.Std;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.maxmind.db.Metadata;
/*     */ import com.maxmind.db.Reader;
/*     */ import com.maxmind.db.Reader.FileMode;
/*     */ import com.maxmind.geoip2.exception.AddressNotFoundException;
/*     */ import com.maxmind.geoip2.exception.GeoIp2Exception;
/*     */ import com.maxmind.geoip2.model.AnonymousIpResponse;
/*     */ import com.maxmind.geoip2.model.CityResponse;
/*     */ import com.maxmind.geoip2.model.ConnectionTypeResponse;
/*     */ import com.maxmind.geoip2.model.CountryResponse;
/*     */ import com.maxmind.geoip2.model.DomainResponse;
/*     */ import com.maxmind.geoip2.model.IspResponse;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DatabaseReader
/*     */   implements DatabaseProvider, Closeable
/*     */ {
/*     */   private final Reader reader;
/*     */   private final ObjectMapper om;
/*     */   
/*     */   private DatabaseReader(Builder builder)
/*     */     throws IOException
/*     */   {
/*  38 */     if (builder.stream != null) {
/*  39 */       this.reader = new Reader(builder.stream);
/*  40 */     } else if (builder.database != null) {
/*  41 */       this.reader = new Reader(builder.database, builder.mode);
/*     */     }
/*     */     else
/*     */     {
/*  45 */       throw new IllegalArgumentException("Unsupported Builder configuration: expected either File or URL");
/*     */     }
/*     */     
/*  48 */     this.om = new ObjectMapper();
/*  49 */     this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
/*     */     
/*  51 */     this.om.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
/*     */     
/*  53 */     InjectableValues inject = new InjectableValues.Std().addValue("locales", builder.locales);
/*     */     
/*  55 */     this.om.setInjectableValues(inject);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     final File database;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final InputStream stream;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  75 */     List<String> locales = Collections.singletonList("en");
/*  76 */     Reader.FileMode mode = Reader.FileMode.MEMORY_MAPPED;
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder(InputStream stream)
/*     */     {
/*  82 */       this.stream = stream;
/*  83 */       this.database = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder(File database)
/*     */     {
/*  90 */       this.database = database;
/*  91 */       this.stream = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder locales(List<String> val)
/*     */     {
/* 100 */       this.locales = val;
/* 101 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder fileMode(Reader.FileMode val)
/*     */     {
/* 112 */       if ((this.stream != null) && (Reader.FileMode.MEMORY != val)) {
/* 113 */         throw new IllegalArgumentException("Only FileMode.MEMORY is supported when using an InputStream.");
/*     */       }
/*     */       
/* 116 */       this.mode = val;
/* 117 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public DatabaseReader build()
/*     */       throws IOException
/*     */     {
/* 126 */       return new DatabaseReader(this, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T get(InetAddress ipAddress, Class<T> cls, boolean hasTraits, String type)
/*     */     throws IOException, AddressNotFoundException
/*     */   {
/* 139 */     String databaseType = getMetadata().getDatabaseType();
/* 140 */     if (!databaseType.contains(type))
/*     */     {
/* 142 */       String caller = Thread.currentThread().getStackTrace()[2].getMethodName();
/* 143 */       throw new UnsupportedOperationException("Invalid attempt to open a " + databaseType + " database using the " + caller + " method");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 148 */     ObjectNode node = (ObjectNode)this.reader.get(ipAddress);
/*     */     
/*     */ 
/*     */ 
/* 152 */     if (node == null)
/*     */     {
/* 154 */       throw new AddressNotFoundException("The address " + ipAddress.getHostAddress() + " is not in the database.");
/*     */     }
/*     */     ObjectNode ipNode;
/*     */     ObjectNode ipNode;
/* 158 */     if (hasTraits) {
/* 159 */       if (!node.has("traits")) {
/* 160 */         node.set("traits", this.om.createObjectNode());
/*     */       }
/* 162 */       ipNode = (ObjectNode)node.get("traits");
/*     */     } else {
/* 164 */       ipNode = node;
/*     */     }
/* 166 */     ipNode.put("ip_address", ipAddress.getHostAddress());
/*     */     
/* 168 */     return (T)this.om.treeToValue(node, cls);
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
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 187 */     this.reader.close();
/*     */   }
/*     */   
/*     */   public CountryResponse country(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 193 */     return (CountryResponse)get(ipAddress, CountryResponse.class, true, "Country");
/*     */   }
/*     */   
/*     */   public CityResponse city(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 199 */     return (CityResponse)get(ipAddress, CityResponse.class, true, "City");
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
/*     */   public AnonymousIpResponse anonymousIp(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 213 */     return (AnonymousIpResponse)get(ipAddress, AnonymousIpResponse.class, false, "GeoIP2-Anonymous-IP");
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
/*     */   public ConnectionTypeResponse connectionType(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 227 */     return (ConnectionTypeResponse)get(ipAddress, ConnectionTypeResponse.class, false, "GeoIP2-Connection-Type");
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
/*     */   public DomainResponse domain(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 243 */     return (DomainResponse)get(ipAddress, DomainResponse.class, false, "GeoIP2-Domain");
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
/*     */   public IspResponse isp(InetAddress ipAddress)
/*     */     throws IOException, GeoIp2Exception
/*     */   {
/* 257 */     return (IspResponse)get(ipAddress, IspResponse.class, false, "GeoIP2-ISP");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Metadata getMetadata()
/*     */   {
/* 264 */     return this.reader.getMetadata();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\DatabaseReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */