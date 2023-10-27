/*     */ package oshi.hardware.platform.windows;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import oshi.hardware.Sensors;
/*     */ import oshi.util.platform.windows.WmiUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WindowsSensors
/*     */   implements Sensors
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  32 */   private String tempIdentifierStr = null;
/*     */   
/*     */ 
/*  35 */   private String wmiTempNamespace = null;
/*     */   
/*  37 */   private String wmiTempClass = null;
/*     */   
/*  39 */   private String wmiTempProperty = null;
/*     */   
/*     */ 
/*  42 */   private boolean fanSpeedWMI = true;
/*     */   
/*     */ 
/*  45 */   private String voltIdentifierStr = null;
/*     */   
/*     */ 
/*  48 */   private String wmiVoltNamespace = null;
/*     */   
/*  50 */   private String wmiVoltClass = null;
/*     */   
/*  52 */   private String wmiVoltProperty = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCpuTemperature()
/*     */   {
/*  60 */     double tempC = 0.0D;
/*     */     
/*     */ 
/*  63 */     if (this.tempIdentifierStr != null) {
/*  64 */       Map<String, List<Float>> vals = WmiUtil.selectFloatsFrom("root\\OpenHardwareMonitor", "Sensor", "Value", "WHERE Parent=\"" + this.tempIdentifierStr + "\" AND SensorType=\"Temperature\"");
/*     */       
/*  66 */       if (!((List)vals.get("Value")).isEmpty()) {
/*  67 */         double sum = 0.0D;
/*  68 */         for (Iterator localIterator = ((List)vals.get("Value")).iterator(); localIterator.hasNext();) { double val = ((Float)localIterator.next()).floatValue();
/*  69 */           sum += val;
/*     */         }
/*  71 */         tempC = sum / ((List)vals.get("Value")).size();
/*     */       }
/*  73 */       return tempC;
/*     */     }
/*     */     
/*     */ 
/*     */     long tempK;
/*     */     
/*  79 */     if (this.wmiTempClass == null) {
/*  80 */       this.wmiTempNamespace = "root\\cimv2";
/*  81 */       this.wmiTempClass = "Win32_Temperature";
/*  82 */       this.wmiTempProperty = "CurrentReading";
/*  83 */       long tempK = WmiUtil.selectUint32From(this.wmiTempNamespace, this.wmiTempClass, this.wmiTempProperty, null).longValue();
/*  84 */       if (tempK == 0L) {
/*  85 */         this.wmiTempClass = "Win32_TemperatureProbe";
/*  86 */         tempK = WmiUtil.selectUint32From(this.wmiTempNamespace, this.wmiTempClass, this.wmiTempProperty, null).longValue();
/*     */       }
/*  88 */       if (tempK == 0L) {
/*  89 */         this.wmiTempClass = "Win32_PerfFormattedData_Counters_ThermalZoneInformation";
/*  90 */         this.wmiTempProperty = "Temperature";
/*  91 */         tempK = WmiUtil.selectUint32From(this.wmiTempNamespace, this.wmiTempClass, this.wmiTempProperty, null).longValue();
/*     */       }
/*  93 */       if (tempK == 0L) {
/*  94 */         this.wmiTempNamespace = "root\\wmi";
/*  95 */         this.wmiTempClass = "MSAcpi_ThermalZoneTemperature";
/*  96 */         this.wmiTempProperty = "CurrentTemperature";
/*  97 */         tempK = WmiUtil.selectUint32From(this.wmiTempNamespace, this.wmiTempClass, this.wmiTempProperty, null).longValue();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 102 */       tempK = WmiUtil.selectUint32From(this.wmiTempNamespace, this.wmiTempClass, this.wmiTempProperty, null).longValue();
/*     */     }
/*     */     
/* 105 */     if (tempK > 2732L) {
/* 106 */       tempC = tempK / 10.0D - 273.15D;
/* 107 */     } else if (tempK > 274L) {
/* 108 */       tempC = tempK - 273.0D;
/*     */     }
/* 110 */     if (tempC <= 0.0D)
/*     */     {
/*     */ 
/* 113 */       String cpuIdentifier = WmiUtil.selectStringFrom("root\\OpenHardwareMonitor", "Hardware", "Identifier", "WHERE HardwareType=\"CPU\"");
/*     */       
/* 115 */       this.tempIdentifierStr = (cpuIdentifier.length() > 0 ? cpuIdentifier : null);
/*     */       
/* 117 */       if (this.tempIdentifierStr != null) {
/* 118 */         return getCpuTemperature();
/*     */       }
/* 120 */       tempC = 0.0D;
/*     */     }
/* 122 */     return tempC;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getFanSpeeds()
/*     */   {
/* 131 */     int[] fanSpeeds = new int[1];
/*     */     
/* 133 */     if (!this.fanSpeedWMI) {
/* 134 */       Map<String, List<Float>> vals = WmiUtil.selectFloatsFrom("root\\OpenHardwareMonitor", "Sensor", "Value", "WHERE Parent=\"" + this.tempIdentifierStr + "\" AND SensorType=\"Fan\"");
/*     */       
/* 136 */       if (!((List)vals.get("Value")).isEmpty()) {
/* 137 */         fanSpeeds = new int[((List)vals.get("Value")).size()];
/* 138 */         for (int i = 0; i < ((List)vals.get("Value")).size(); i++) {
/* 139 */           fanSpeeds[i] = ((Float)((List)vals.get("Value")).get(i)).intValue();
/*     */         }
/*     */       }
/* 142 */       return fanSpeeds;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 147 */     int rpm = WmiUtil.selectUint32From(null, "Win32_Fan", "DesiredSpeed", null).intValue();
/*     */     
/* 149 */     if (rpm > 0) {
/* 150 */       fanSpeeds[0] = rpm;
/*     */     }
/*     */     else {
/* 153 */       this.fanSpeedWMI = false;
/* 154 */       return getFanSpeeds();
/*     */     }
/* 156 */     return fanSpeeds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getCpuVoltage()
/*     */   {
/* 165 */     double volts = 0.0D;
/*     */     
/* 167 */     if (this.voltIdentifierStr != null) {
/* 168 */       return WmiUtil.selectFloatFrom("root\\OpenHardwareMonitor", "Sensor", "Value", "WHERE Parent=\"" + this.voltIdentifierStr + "\" AND SensorType=\"Voltage\"").floatValue();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     int decivolts;
/*     */     
/*     */ 
/* 176 */     if (this.wmiVoltClass == null) {
/* 177 */       this.wmiVoltNamespace = "root\\cimv2";
/* 178 */       this.wmiVoltClass = "Win32_Processor";
/* 179 */       this.wmiVoltProperty = "CurrentVoltage";
/*     */       
/* 181 */       int decivolts = WmiUtil.selectUint32From(this.wmiVoltNamespace, this.wmiVoltClass, this.wmiVoltProperty, null).intValue();
/*     */       
/*     */ 
/*     */ 
/* 185 */       if (((decivolts & 0x80) == 0) && (decivolts > 0)) {
/* 186 */         this.wmiVoltProperty = "VoltageCaps";
/*     */         
/*     */ 
/*     */ 
/* 190 */         decivolts = WmiUtil.selectUint32From(this.wmiVoltNamespace, this.wmiVoltClass, this.wmiVoltProperty, null).intValue();
/*     */       }
/*     */       
/*     */     }
/*     */     else
/*     */     {
/* 196 */       decivolts = WmiUtil.selectUint32From(this.wmiVoltNamespace, this.wmiVoltClass, this.wmiVoltProperty, null).intValue();
/*     */     }
/*     */     
/* 199 */     if (decivolts > 0) {
/* 200 */       if ("VoltageCaps".equals(this.wmiVoltProperty))
/*     */       {
/* 202 */         if ((decivolts & 0x1) > 0) {
/* 203 */           volts = 5.0D;
/* 204 */         } else if ((decivolts & 0x2) > 0) {
/* 205 */           volts = 3.3D;
/* 206 */         } else if ((decivolts & 0x4) > 0) {
/* 207 */           volts = 2.9D;
/*     */         }
/*     */       }
/*     */       else {
/* 211 */         volts = (decivolts & 0x7F) / 10.0D;
/*     */       }
/*     */     }
/* 214 */     if (volts <= 0.0D)
/*     */     {
/*     */ 
/* 217 */       Map<String, List<String>> voltIdentifiers = WmiUtil.selectStringsFrom("root\\OpenHardwareMonitor", "Hardware", "Identifier", "WHERE SensorType=\"Voltage\"");
/*     */       
/*     */ 
/*     */ 
/* 221 */       for (String id : (List)voltIdentifiers.get("Identifier")) {
/* 222 */         if (id.toLowerCase().contains("cpu")) {
/* 223 */           this.voltIdentifierStr = id;
/* 224 */           break;
/*     */         }
/*     */       }
/*     */       
/* 228 */       if ((this.voltIdentifierStr == null) && (!((List)voltIdentifiers.get("Identifier")).isEmpty())) {
/* 229 */         this.voltIdentifierStr = ((String)((List)voltIdentifiers.get("Identifier")).get(0));
/*     */       }
/*     */       
/* 232 */       if (this.voltIdentifierStr != null) {
/* 233 */         return getCpuVoltage();
/*     */       }
/*     */     }
/* 236 */     return volts;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsSensors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */