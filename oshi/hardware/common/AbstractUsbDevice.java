/*     */ package oshi.hardware.common;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import oshi.hardware.UsbDevice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUsbDevice
/*     */   implements UsbDevice
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*     */   protected String name;
/*     */   protected String vendor;
/*     */   protected String vendorId;
/*     */   protected String productId;
/*     */   protected String serialNumber;
/*     */   protected UsbDevice[] connectedDevices;
/*     */   
/*     */   public AbstractUsbDevice(String name, String vendor, String vendorId, String productId, String serialNumber, UsbDevice[] connectedDevices)
/*     */   {
/*  48 */     this.name = name;
/*  49 */     this.vendor = vendor;
/*  50 */     this.vendorId = vendorId;
/*  51 */     this.productId = productId;
/*  52 */     this.serialNumber = serialNumber;
/*  53 */     this.connectedDevices = ((UsbDevice[])Arrays.copyOf(connectedDevices, connectedDevices.length));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  61 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVendor()
/*     */   {
/*  69 */     return this.vendor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVendorId()
/*     */   {
/*  77 */     return this.vendorId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProductId()
/*     */   {
/*  85 */     return this.productId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSerialNumber()
/*     */   {
/*  93 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UsbDevice[] getConnectedDevices()
/*     */   {
/* 101 */     return (UsbDevice[])Arrays.copyOf(this.connectedDevices, this.connectedDevices.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(UsbDevice usb)
/*     */   {
/* 110 */     return getName().compareTo(usb.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return indentUsb(this, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String indentUsb(UsbDevice usbDevice, int indent)
/*     */   {
/* 130 */     String indentFmt = indent > 2 ? String.format("%%%ds|-- ", new Object[] { Integer.valueOf(indent - 4) }) : String.format("%%%ds", new Object[] { Integer.valueOf(indent) });
/* 131 */     StringBuilder sb = new StringBuilder(String.format(indentFmt, new Object[] { "" }));
/* 132 */     sb.append(usbDevice.getName());
/* 133 */     if (usbDevice.getVendor().length() > 0) {
/* 134 */       sb.append(" (").append(usbDevice.getVendor()).append(')');
/*     */     }
/* 136 */     if (usbDevice.getSerialNumber().length() > 0) {
/* 137 */       sb.append(" [s/n: ").append(usbDevice.getSerialNumber()).append(']');
/*     */     }
/* 139 */     for (UsbDevice connected : usbDevice.getConnectedDevices()) {
/* 140 */       sb.append('\n').append(indentUsb(connected, indent + 4));
/*     */     }
/* 142 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractUsbDevice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */