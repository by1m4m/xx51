/*     */ package com.sun.jna.platform;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.DataBuffer;
/*     */ import java.awt.image.DataBufferByte;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.MultiPixelPackedSampleModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.SampleModel;
/*     */ import java.awt.image.SinglePixelPackedSampleModel;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RasterRangesUtils
/*     */ {
/*  42 */   private static final int[] subColMasks = { 128, 64, 32, 16, 8, 4, 2, 1 };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static final Comparator<Object> COMPARATOR = new Comparator() {
/*     */     public int compare(Object o1, Object o2) {
/*  49 */       return ((Rectangle)o1).x - ((Rectangle)o2).x;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean outputOccupiedRanges(Raster raster, RangesOutput out)
/*     */   {
/*  77 */     Rectangle bounds = raster.getBounds();
/*  78 */     SampleModel sampleModel = raster.getSampleModel();
/*  79 */     boolean hasAlpha = sampleModel.getNumBands() == 4;
/*     */     
/*     */ 
/*  82 */     if ((raster.getParent() == null) && (bounds.x == 0) && (bounds.y == 0))
/*     */     {
/*     */ 
/*  85 */       DataBuffer data = raster.getDataBuffer();
/*  86 */       if (data.getNumBanks() == 1)
/*     */       {
/*     */ 
/*  89 */         if ((sampleModel instanceof MultiPixelPackedSampleModel)) {
/*  90 */           MultiPixelPackedSampleModel packedSampleModel = (MultiPixelPackedSampleModel)sampleModel;
/*  91 */           if (packedSampleModel.getPixelBitStride() == 1)
/*     */           {
/*  93 */             return outputOccupiedRangesOfBinaryPixels(((DataBufferByte)data).getData(), bounds.width, bounds.height, out);
/*     */           }
/*  95 */         } else if (((sampleModel instanceof SinglePixelPackedSampleModel)) && 
/*  96 */           (sampleModel.getDataType() == 3))
/*     */         {
/*  98 */           return outputOccupiedRanges(((DataBufferInt)data).getData(), bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 106 */     int[] pixels = raster.getPixels(0, 0, bounds.width, bounds.height, (int[])null);
/* 107 */     return outputOccupiedRanges(pixels, bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean outputOccupiedRangesOfBinaryPixels(byte[] binaryBits, int w, int h, RangesOutput out)
/*     */   {
/* 119 */     Set<Rectangle> rects = new HashSet();
/* 120 */     Set<Rectangle> prevLine = Collections.EMPTY_SET;
/* 121 */     int scanlineBytes = binaryBits.length / h;
/* 122 */     for (int row = 0; row < h; row++) {
/* 123 */       Set<Rectangle> curLine = new TreeSet(COMPARATOR);
/* 124 */       int rowOffsetBytes = row * scanlineBytes;
/* 125 */       int startCol = -1;
/*     */       
/* 127 */       for (int byteCol = 0; byteCol < scanlineBytes; byteCol++) {
/* 128 */         int firstByteCol = byteCol << 3;
/* 129 */         byte byteColBits = binaryBits[(rowOffsetBytes + byteCol)];
/* 130 */         if (byteColBits == 0)
/*     */         {
/* 132 */           if (startCol >= 0)
/*     */           {
/* 134 */             curLine.add(new Rectangle(startCol, row, firstByteCol - startCol, 1));
/* 135 */             startCol = -1;
/*     */           }
/* 137 */         } else if (byteColBits == 255)
/*     */         {
/* 139 */           if (startCol < 0)
/*     */           {
/* 141 */             startCol = firstByteCol;
/*     */           }
/*     */         }
/*     */         else {
/* 145 */           for (int subCol = 0; subCol < 8; subCol++) {
/* 146 */             int col = firstByteCol | subCol;
/* 147 */             if ((byteColBits & subColMasks[subCol]) != 0) {
/* 148 */               if (startCol < 0)
/*     */               {
/* 150 */                 startCol = col;
/*     */               }
/*     */             }
/* 153 */             else if (startCol >= 0)
/*     */             {
/* 155 */               curLine.add(new Rectangle(startCol, row, col - startCol, 1));
/* 156 */               startCol = -1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 162 */       if (startCol >= 0)
/*     */       {
/* 164 */         curLine.add(new Rectangle(startCol, row, w - startCol, 1));
/*     */       }
/* 166 */       Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
/* 167 */       rects.addAll(unmerged);
/* 168 */       prevLine = curLine;
/*     */     }
/*     */     
/* 171 */     rects.addAll(prevLine);
/* 172 */     for (Iterator<Rectangle> i = rects.iterator(); i.hasNext();) {
/* 173 */       Rectangle r = (Rectangle)i.next();
/* 174 */       if (!out.outputRange(r.x, r.y, r.width, r.height)) {
/* 175 */         return false;
/*     */       }
/*     */     }
/* 178 */     return true;
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
/*     */   public static boolean outputOccupiedRanges(int[] pixels, int w, int h, int occupationMask, RangesOutput out)
/*     */   {
/* 192 */     Set<Rectangle> rects = new HashSet();
/* 193 */     Set<Rectangle> prevLine = Collections.EMPTY_SET;
/* 194 */     for (int row = 0; row < h; row++) {
/* 195 */       Set<Rectangle> curLine = new TreeSet(COMPARATOR);
/* 196 */       int idxOffset = row * w;
/* 197 */       int startCol = -1;
/*     */       
/* 199 */       for (int col = 0; col < w; col++) {
/* 200 */         if ((pixels[(idxOffset + col)] & occupationMask) != 0) {
/* 201 */           if (startCol < 0) {
/* 202 */             startCol = col;
/*     */           }
/*     */         }
/* 205 */         else if (startCol >= 0)
/*     */         {
/* 207 */           curLine.add(new Rectangle(startCol, row, col - startCol, 1));
/* 208 */           startCol = -1;
/*     */         }
/*     */       }
/*     */       
/* 212 */       if (startCol >= 0)
/*     */       {
/* 214 */         curLine.add(new Rectangle(startCol, row, w - startCol, 1));
/*     */       }
/* 216 */       Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
/* 217 */       rects.addAll(unmerged);
/* 218 */       prevLine = curLine;
/*     */     }
/*     */     
/* 221 */     rects.addAll(prevLine);
/* 222 */     for (Iterator<Rectangle> i = rects.iterator(); i.hasNext();) {
/* 223 */       Rectangle r = (Rectangle)i.next();
/* 224 */       if (!out.outputRange(r.x, r.y, r.width, r.height)) {
/* 225 */         return false;
/*     */       }
/*     */     }
/* 228 */     return true;
/*     */   }
/*     */   
/*     */   private static Set<Rectangle> mergeRects(Set<Rectangle> prev, Set<Rectangle> current) {
/* 232 */     Set<Rectangle> unmerged = new HashSet(prev);
/* 233 */     if ((!prev.isEmpty()) && (!current.isEmpty())) {
/* 234 */       Rectangle[] pr = (Rectangle[])prev.toArray(new Rectangle[prev.size()]);
/* 235 */       Rectangle[] cr = (Rectangle[])current.toArray(new Rectangle[current.size()]);
/* 236 */       int ipr = 0;
/* 237 */       int icr = 0;
/* 238 */       while ((ipr < pr.length) && (icr < cr.length)) {
/* 239 */         while (cr[icr].x < pr[ipr].x) {
/* 240 */           icr++; if (icr == cr.length) {
/* 241 */             return unmerged;
/*     */           }
/*     */         }
/* 244 */         if ((cr[icr].x == pr[ipr].x) && (cr[icr].width == pr[ipr].width)) {
/* 245 */           unmerged.remove(pr[ipr]);
/* 246 */           cr[icr].y = pr[ipr].y;
/* 247 */           pr[ipr].height += 1;
/* 248 */           icr++;
/*     */         }
/*     */         else {
/* 251 */           ipr++;
/*     */         }
/*     */       }
/*     */     }
/* 255 */     return unmerged;
/*     */   }
/*     */   
/*     */   public static abstract interface RangesOutput
/*     */   {
/*     */     public abstract boolean outputRange(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\RasterRangesUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */