/*     */ package oshi.util.platform.windows;
/*     */ 
/*     */ import com.sun.jna.NativeLong;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.platform.win32.COM.COMUtils;
/*     */ import com.sun.jna.platform.win32.OleAuto;
/*     */ import com.sun.jna.platform.win32.Variant.VARIANT.ByReference;
/*     */ import com.sun.jna.platform.win32.Variant.VARIANT._VARIANT;
/*     */ import com.sun.jna.platform.win32.Variant.VARIANT._VARIANT.__VARIANT;
/*     */ import com.sun.jna.platform.win32.WTypes.BSTR;
/*     */ import com.sun.jna.platform.win32.WinDef.BOOL;
/*     */ import com.sun.jna.platform.win32.WinDef.LONG;
/*     */ import com.sun.jna.platform.win32.WinNT.HRESULT;
/*     */ import com.sun.jna.ptr.LongByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.windows.COM.EnumWbemClassObject;
/*     */ import oshi.jna.platform.windows.COM.WbemClassObject;
/*     */ import oshi.jna.platform.windows.COM.WbemLocator;
/*     */ import oshi.jna.platform.windows.COM.WbemServices;
/*     */ import oshi.jna.platform.windows.Ole32;
/*     */ import oshi.util.ParseUtil;
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
/*     */ public class WmiUtil
/*     */ {
/*  71 */   private static final Logger LOG = LoggerFactory.getLogger(WmiUtil.class);
/*     */   
/*     */   public static final String DEFAULT_NAMESPACE = "ROOT\\CIMV2";
/*     */   
/*  75 */   private static boolean comInitialized = false;
/*  76 */   private static boolean securityInitialized = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static enum ValueType
/*     */   {
/*  83 */     STRING,  UINT32,  FLOAT,  DATETIME,  BOOLEAN,  UINT64,  UINT16, 
/*     */     
/*  85 */     PROCESS_GETOWNER,  PROCESS_GETOWNERSID;
/*     */     
/*     */ 
/*     */     private ValueType() {}
/*     */   }
/*     */   
/*  91 */   private static final ValueType[] STRING_TYPE = { ValueType.STRING };
/*  92 */   private static final ValueType[] UINT32_TYPE = { ValueType.UINT32 };
/*  93 */   private static final ValueType[] FLOAT_TYPE = { ValueType.FLOAT };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean hasNamespace(String namespace)
/*     */   {
/* 104 */     Map<String, List<String>> nsMap = selectStringsFrom("ROOT", "__NAMESPACE", "Name", null);
/* 105 */     for (String s : (List)nsMap.get("Name")) {
/* 106 */       if (s.equals(namespace)) {
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
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
/*     */   public static Long selectUint32From(String namespace, String wmiClass, String property, String whereClause)
/*     */   {
/* 127 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property, wmiClass, whereClause, UINT32_TYPE);
/*     */     
/* 129 */     if ((result.containsKey(property)) && (!((List)result.get(property)).isEmpty())) {
/* 130 */       return (Long)((List)result.get(property)).get(0);
/*     */     }
/* 132 */     return Long.valueOf(0L);
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
/*     */ 
/*     */ 
/*     */   public static Map<String, List<Long>> selectUint32sFrom(String namespace, String wmiClass, String properties, String whereClause)
/*     */   {
/* 152 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties, wmiClass, whereClause, UINT32_TYPE);
/*     */     
/* 154 */     HashMap<String, List<Long>> longMap = new HashMap();
/* 155 */     for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
/* 156 */       ArrayList<Long> longList = new ArrayList();
/* 157 */       for (Object obj : (List)entry.getValue()) {
/* 158 */         longList.add((Long)obj);
/*     */       }
/* 160 */       longMap.put(entry.getKey(), longList);
/*     */     }
/* 162 */     return longMap;
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
/*     */   public static Float selectFloatFrom(String namespace, String wmiClass, String property, String whereClause)
/*     */   {
/* 179 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property, wmiClass, whereClause, FLOAT_TYPE);
/*     */     
/* 181 */     if ((result.containsKey(property)) && (!((List)result.get(property)).isEmpty())) {
/* 182 */       return (Float)((List)result.get(property)).get(0);
/*     */     }
/* 184 */     return Float.valueOf(0.0F);
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
/*     */ 
/*     */ 
/*     */   public static Map<String, List<Float>> selectFloatsFrom(String namespace, String wmiClass, String properties, String whereClause)
/*     */   {
/* 204 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties, wmiClass, whereClause, FLOAT_TYPE);
/*     */     
/* 206 */     HashMap<String, List<Float>> floatMap = new HashMap();
/* 207 */     for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
/* 208 */       ArrayList<Float> floatList = new ArrayList();
/* 209 */       for (Object obj : (List)entry.getValue()) {
/* 210 */         floatList.add((Float)obj);
/*     */       }
/* 212 */       floatMap.put(entry.getKey(), floatList);
/*     */     }
/* 214 */     return floatMap;
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
/*     */   public static String selectStringFrom(String namespace, String wmiClass, String property, String whereClause)
/*     */   {
/* 231 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, property, wmiClass, whereClause, STRING_TYPE);
/*     */     
/* 233 */     if ((result.containsKey(property)) && (!((List)result.get(property)).isEmpty())) {
/* 234 */       return (String)((List)result.get(property)).get(0);
/*     */     }
/* 236 */     return "";
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
/*     */ 
/*     */ 
/*     */   public static Map<String, List<String>> selectStringsFrom(String namespace, String wmiClass, String properties, String whereClause)
/*     */   {
/* 256 */     Map<String, List<Object>> result = queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties, wmiClass, whereClause, STRING_TYPE);
/*     */     
/* 258 */     HashMap<String, List<String>> strMap = new HashMap();
/* 259 */     for (Map.Entry<String, List<Object>> entry : result.entrySet()) {
/* 260 */       ArrayList<String> strList = new ArrayList();
/* 261 */       for (Object obj : (List)entry.getValue()) {
/* 262 */         strList.add((String)obj);
/*     */       }
/* 264 */       strMap.put(entry.getKey(), strList);
/*     */     }
/* 266 */     return strMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Map<String, List<Object>> selectObjectsFrom(String namespace, String wmiClass, String properties, String whereClause, ValueType[] propertyTypes)
/*     */   {
/* 292 */     return queryWMI(namespace == null ? DEFAULT_NAMESPACE : namespace, properties, wmiClass, whereClause, propertyTypes);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Map<String, List<Object>> queryWMI(String namespace, String properties, String wmiClass, String whereClause, ValueType[] propertyTypes)
/*     */   {
/* 317 */     Map<String, List<Object>> values = new HashMap();
/* 318 */     String[] props = properties.split(",");
/* 319 */     for (int i = 0; i < props.length; i++) {
/* 320 */       if ("__PATH".equals(props[i]))
/*     */       {
/* 322 */         values.put(propertyTypes[i].name(), new ArrayList());
/*     */       }
/*     */       else {
/* 325 */         values.put(props[i], new ArrayList());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 330 */     if (!initCOM()) {
/* 331 */       unInitCOM();
/* 332 */       return values;
/*     */     }
/*     */     
/* 335 */     PointerByReference pSvc = new PointerByReference();
/* 336 */     if (!connectServer(namespace, pSvc)) {
/* 337 */       unInitCOM();
/* 338 */       return values;
/*     */     }
/* 340 */     WbemServices svc = new WbemServices(pSvc.getValue());
/*     */     
/* 342 */     PointerByReference pEnumerator = new PointerByReference();
/* 343 */     if (!selectProperties(svc, pEnumerator, properties, wmiClass, whereClause)) {
/* 344 */       svc.Release();
/* 345 */       unInitCOM();
/* 346 */       return values;
/*     */     }
/* 348 */     EnumWbemClassObject enumerator = new EnumWbemClassObject(pEnumerator.getValue());
/*     */     
/* 350 */     enumerateProperties(values, enumerator, props, propertyTypes, svc);
/*     */     
/*     */ 
/* 353 */     enumerator.Release();
/* 354 */     svc.Release();
/* 355 */     unInitCOM();
/* 356 */     return values;
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
/*     */   private static boolean initCOM()
/*     */   {
/* 373 */     WinNT.HRESULT hres = Ole32.INSTANCE.CoInitializeEx(null, 0);
/* 374 */     if (COMUtils.FAILED(hres)) {
/* 375 */       if (hres.intValue() == -2147417850)
/*     */       {
/* 377 */         LOG.debug("COM already initialized.");
/* 378 */         securityInitialized = true;
/* 379 */         return true;
/*     */       }
/* 381 */       LOG.error(String.format("Failed to initialize COM library. Error code = 0x%08x", new Object[] { Integer.valueOf(hres.intValue()) }));
/* 382 */       return false;
/*     */     }
/* 384 */     comInitialized = true;
/* 385 */     if (securityInitialized)
/*     */     {
/* 387 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 391 */     hres = Ole32.INSTANCE.CoInitializeSecurity(null, new NativeLong(-1L), null, null, 0, 3, null, 0, null);
/*     */     
/* 393 */     if ((COMUtils.FAILED(hres)) && (hres.intValue() != -2147417831)) {
/* 394 */       LOG.error(String.format("Failed to initialize security. Error code = 0x%08x", new Object[] { Integer.valueOf(hres.intValue()) }));
/* 395 */       Ole32.INSTANCE.CoUninitialize();
/* 396 */       return false;
/*     */     }
/* 398 */     securityInitialized = true;
/* 399 */     return true;
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
/*     */   private static boolean connectServer(String namespace, PointerByReference pSvc)
/*     */   {
/* 416 */     WbemLocator loc = WbemLocator.create();
/* 417 */     if (loc == null) {
/* 418 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 424 */     WinNT.HRESULT hres = loc.ConnectServer(new WTypes.BSTR(namespace), null, null, null, null, null, null, pSvc);
/* 425 */     if (COMUtils.FAILED(hres)) {
/* 426 */       LOG.error(String.format("Could not connect to namespace %s. Error code = 0x%08x", new Object[] { namespace, 
/* 427 */         Integer.valueOf(hres.intValue()) }));
/* 428 */       loc.Release();
/* 429 */       unInitCOM();
/* 430 */       return false;
/*     */     }
/* 432 */     LOG.debug("Connected to {} WMI namespace", namespace);
/* 433 */     loc.Release();
/*     */     
/*     */ 
/*     */ 
/* 437 */     hres = Ole32.INSTANCE.CoSetProxyBlanket(pSvc.getValue(), 10, 0, null, 3, 3, null, 0);
/*     */     
/* 439 */     if (COMUtils.FAILED(hres)) {
/* 440 */       LOG.error(String.format("Could not set proxy blanket. Error code = 0x%08x", new Object[] { Integer.valueOf(hres.intValue()) }));
/* 441 */       new WbemServices(pSvc.getValue()).Release();
/* 442 */       unInitCOM();
/* 443 */       return false;
/*     */     }
/* 445 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean selectProperties(WbemServices svc, PointerByReference pEnumerator, String properties, String wmiClass, String whereClause)
/*     */   {
/* 470 */     String query = String.format("SELECT %s FROM %s %s", new Object[] { properties, wmiClass, whereClause != null ? whereClause : "" });
/*     */     
/* 472 */     LOG.debug("Query: {}", query);
/* 473 */     WinNT.HRESULT hres = svc.ExecQuery(new WTypes.BSTR("WQL"), new WTypes.BSTR(query), new NativeLong(48L), null, pEnumerator);
/*     */     
/*     */ 
/*     */ 
/* 477 */     if (COMUtils.FAILED(hres)) {
/* 478 */       LOG.error(String.format("Query '%s' failed. Error code = 0x%08x", new Object[] { query, Integer.valueOf(hres.intValue()) }));
/* 479 */       svc.Release();
/* 480 */       unInitCOM();
/* 481 */       return false;
/*     */     }
/* 483 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void enumerateProperties(Map<String, List<Object>> values, EnumWbemClassObject enumerator, String[] properties, ValueType[] propertyTypes, WbemServices svc)
/*     */   {
/* 506 */     if ((propertyTypes.length > 1) && (properties.length != propertyTypes.length)) {
/* 507 */       throw new IllegalArgumentException("Property type array size must be 1 or equal to properties array size.");
/*     */     }
/*     */     
/*     */ 
/* 511 */     PointerByReference pclsObj = new PointerByReference();
/* 512 */     LongByReference uReturn = new LongByReference(0L);
/* 513 */     while (enumerator.getPointer() != Pointer.NULL) {
/* 514 */       WinNT.HRESULT hres = enumerator.Next(new NativeLong(-1L), new NativeLong(1L), pclsObj, uReturn);
/*     */       
/*     */ 
/* 517 */       if ((0L == uReturn.getValue()) || (COMUtils.FAILED(hres)))
/*     */       {
/*     */ 
/* 520 */         return;
/*     */       }
/* 522 */       Variant.VARIANT.ByReference vtProp = new Variant.VARIANT.ByReference();
/*     */       
/*     */ 
/* 525 */       WbemClassObject clsObj = new WbemClassObject(pclsObj.getValue());
/* 526 */       for (int p = 0; p < properties.length; p++) {
/* 527 */         String property = properties[p];
/* 528 */         hres = clsObj.Get(new WTypes.BSTR(property), new NativeLong(0L), vtProp, null, null);
/*     */         
/* 530 */         ValueType propertyType = propertyTypes.length > 1 ? propertyTypes[p] : propertyTypes[0];
/* 531 */         switch (propertyType)
/*     */         {
/*     */         case STRING: 
/* 534 */           ((List)values.get(property)).add(vtProp.getValue() == null ? "unknown" : vtProp.stringValue());
/* 535 */           break;
/*     */         
/*     */ 
/*     */         case UINT16: 
/*     */         case UINT32: 
/*     */         case UINT64: 
/* 541 */           ((List)values.get(property)).add(Long.valueOf(vtProp.getValue() == null ? 0L : vtProp._variant.__variant.lVal.longValue()));
/* 542 */           break;
/*     */         case FLOAT: 
/* 544 */           ((List)values.get(property)).add(Float.valueOf(vtProp.getValue() == null ? 0.0F : vtProp.floatValue()));
/* 545 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case DATETIME: 
/* 550 */           ((List)values.get(property)).add(Long.valueOf(vtProp.getValue() == null ? 0L : ParseUtil.cimDateTimeToMillis(vtProp.stringValue())));
/* 551 */           break;
/*     */         
/*     */         case BOOLEAN: 
/* 554 */           ((List)values.get(property)).add(vtProp.getValue() == null ? Long.valueOf(0L) : Boolean.valueOf(vtProp._variant.__variant.boolVal.booleanValue()));
/* 555 */           break;
/*     */         
/*     */         case PROCESS_GETOWNER: 
/* 558 */           String owner = String.join("\\", 
/* 559 */             execMethod(svc, vtProp.stringValue(), "GetOwner", new String[] { "Domain", "User" }));
/* 560 */           ((List)values.get(propertyType.name())).add("\\".equals(owner) ? "N/A" : owner);
/* 561 */           break;
/*     */         
/*     */         case PROCESS_GETOWNERSID: 
/* 564 */           String[] ownerSid = execMethod(svc, vtProp.stringValue(), "GetOwnerSid", new String[] { "Sid" });
/* 565 */           ((List)values.get(propertyType.name())).add(ownerSid.length < 1 ? "" : ownerSid[0]);
/* 566 */           break;
/*     */         
/*     */ 
/*     */         default: 
/* 570 */           throw new IllegalArgumentException("Unimplemented enum type: " + propertyType.toString());
/*     */         }
/* 572 */         OleAuto.INSTANCE.VariantClear(vtProp.getPointer());
/*     */       }
/*     */       
/* 575 */       clsObj.Release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void unInitCOM()
/*     */   {
/* 584 */     if (comInitialized) {
/* 585 */       Ole32.INSTANCE.CoUninitialize();
/* 586 */       comInitialized = false;
/*     */     }
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
/*     */   private static String[] execMethod(WbemServices svc, String clsObj, String method, String... properties)
/*     */   {
/* 605 */     List<String> result = new ArrayList();
/* 606 */     PointerByReference ppOutParams = new PointerByReference();
/* 607 */     WinNT.HRESULT hres = svc.ExecMethod(new WTypes.BSTR(clsObj), new WTypes.BSTR(method), new NativeLong(0L), null, null, ppOutParams, null);
/*     */     
/* 609 */     if (COMUtils.FAILED(hres)) {
/* 610 */       return new String[0];
/*     */     }
/* 612 */     WbemClassObject obj = new WbemClassObject(ppOutParams.getValue());
/* 613 */     Variant.VARIANT.ByReference vtProp = new Variant.VARIANT.ByReference();
/* 614 */     for (String prop : properties) {
/* 615 */       hres = obj.Get(new WTypes.BSTR(prop), new NativeLong(0L), vtProp, null, null);
/* 616 */       if (!COMUtils.FAILED(hres)) {
/* 617 */         result.add(vtProp.getValue() == null ? "" : vtProp.stringValue());
/*     */       }
/*     */     }
/* 620 */     obj.Release();
/* 621 */     return (String[])result.toArray(new String[result.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\platform\windows\WmiUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */