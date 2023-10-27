/*    */ package rdp.gold.brute;
/*    */ 
/*    */ import java.text.NumberFormat;
/*    */ 
/*    */ public class Memory {
/*    */   public static String getMemoryUsage() {
/*  7 */     Runtime runtime = Runtime.getRuntime();
/*    */     
/*  9 */     long maxMemory = runtime.maxMemory();
/* 10 */     long allocatedMemory = runtime.totalMemory();
/* 11 */     long freeMemory = runtime.freeMemory();
/*    */     
/* 13 */     NumberFormat format = NumberFormat.getInstance();
/*    */     
/* 15 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 17 */     sb.append("free memory: " + format.format(freeMemory / 1024L) + "\r\n");
/* 18 */     sb.append("allocated memory: " + format.format(allocatedMemory / 1024L) + "\r\n");
/* 19 */     sb.append("max memory: " + format.format(maxMemory / 1024L) + "\r\n");
/* 20 */     sb.append("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024L) + "\r\n");
/*    */     
/* 22 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\Memory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */