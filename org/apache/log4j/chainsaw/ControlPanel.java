/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.Document;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
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
/*     */ class ControlPanel
/*     */   extends JPanel
/*     */ {
/*  42 */   private static final Logger LOG = Logger.getLogger(ControlPanel.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ControlPanel(final MyTableModel aModel)
/*     */   {
/*  51 */     setBorder(BorderFactory.createTitledBorder("Controls: "));
/*  52 */     GridBagLayout gridbag = new GridBagLayout();
/*  53 */     GridBagConstraints c = new GridBagConstraints();
/*  54 */     setLayout(gridbag);
/*     */     
/*     */ 
/*  57 */     c.ipadx = 5;
/*  58 */     c.ipady = 5;
/*     */     
/*     */ 
/*  61 */     c.gridx = 0;
/*  62 */     c.anchor = 13;
/*     */     
/*  64 */     c.gridy = 0;
/*  65 */     JLabel label = new JLabel("Filter Level:");
/*  66 */     gridbag.setConstraints(label, c);
/*  67 */     add(label);
/*     */     
/*  69 */     c.gridy += 1;
/*  70 */     label = new JLabel("Filter Thread:");
/*  71 */     gridbag.setConstraints(label, c);
/*  72 */     add(label);
/*     */     
/*  74 */     c.gridy += 1;
/*  75 */     label = new JLabel("Filter Logger:");
/*  76 */     gridbag.setConstraints(label, c);
/*  77 */     add(label);
/*     */     
/*  79 */     c.gridy += 1;
/*  80 */     label = new JLabel("Filter NDC:");
/*  81 */     gridbag.setConstraints(label, c);
/*  82 */     add(label);
/*     */     
/*  84 */     c.gridy += 1;
/*  85 */     label = new JLabel("Filter Message:");
/*  86 */     gridbag.setConstraints(label, c);
/*  87 */     add(label);
/*     */     
/*     */ 
/*  90 */     c.weightx = 1.0D;
/*     */     
/*  92 */     c.gridx = 1;
/*  93 */     c.anchor = 17;
/*     */     
/*  95 */     c.gridy = 0;
/*  96 */     Level[] allPriorities = { Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG, Level.TRACE };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     final JComboBox priorities = new JComboBox(allPriorities);
/* 104 */     Level lowest = allPriorities[(allPriorities.length - 1)];
/* 105 */     priorities.setSelectedItem(lowest);
/* 106 */     aModel.setPriorityFilter(lowest);
/* 107 */     gridbag.setConstraints(priorities, c);
/* 108 */     add(priorities);
/* 109 */     priorities.setEditable(false);
/* 110 */     priorities.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent aEvent) {
/* 112 */         aModel.setPriorityFilter((Priority)priorities.getSelectedItem());
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 117 */     });
/* 118 */     c.fill = 2;
/* 119 */     c.gridy += 1;
/* 120 */     final JTextField threadField = new JTextField("");
/* 121 */     threadField.getDocument().addDocumentListener(new DocumentListener() {
/*     */       public void insertUpdate(DocumentEvent aEvent) {
/* 123 */         aModel.setThreadFilter(threadField.getText());
/*     */       }
/*     */       
/* 126 */       public void removeUpdate(DocumentEvent aEvente) { aModel.setThreadFilter(threadField.getText()); }
/*     */       
/*     */       public void changedUpdate(DocumentEvent aEvent) {
/* 129 */         aModel.setThreadFilter(threadField.getText());
/*     */       }
/* 131 */     });
/* 132 */     gridbag.setConstraints(threadField, c);
/* 133 */     add(threadField);
/*     */     
/* 135 */     c.gridy += 1;
/* 136 */     final JTextField catField = new JTextField("");
/* 137 */     catField.getDocument().addDocumentListener(new DocumentListener() {
/*     */       public void insertUpdate(DocumentEvent aEvent) {
/* 139 */         aModel.setCategoryFilter(catField.getText());
/*     */       }
/*     */       
/* 142 */       public void removeUpdate(DocumentEvent aEvent) { aModel.setCategoryFilter(catField.getText()); }
/*     */       
/*     */       public void changedUpdate(DocumentEvent aEvent) {
/* 145 */         aModel.setCategoryFilter(catField.getText());
/*     */       }
/* 147 */     });
/* 148 */     gridbag.setConstraints(catField, c);
/* 149 */     add(catField);
/*     */     
/* 151 */     c.gridy += 1;
/* 152 */     final JTextField ndcField = new JTextField("");
/* 153 */     ndcField.getDocument().addDocumentListener(new DocumentListener() {
/*     */       public void insertUpdate(DocumentEvent aEvent) {
/* 155 */         aModel.setNDCFilter(ndcField.getText());
/*     */       }
/*     */       
/* 158 */       public void removeUpdate(DocumentEvent aEvent) { aModel.setNDCFilter(ndcField.getText()); }
/*     */       
/*     */       public void changedUpdate(DocumentEvent aEvent) {
/* 161 */         aModel.setNDCFilter(ndcField.getText());
/*     */       }
/* 163 */     });
/* 164 */     gridbag.setConstraints(ndcField, c);
/* 165 */     add(ndcField);
/*     */     
/* 167 */     c.gridy += 1;
/* 168 */     final JTextField msgField = new JTextField("");
/* 169 */     msgField.getDocument().addDocumentListener(new DocumentListener() {
/*     */       public void insertUpdate(DocumentEvent aEvent) {
/* 171 */         aModel.setMessageFilter(msgField.getText());
/*     */       }
/*     */       
/* 174 */       public void removeUpdate(DocumentEvent aEvent) { aModel.setMessageFilter(msgField.getText()); }
/*     */       
/*     */       public void changedUpdate(DocumentEvent aEvent) {
/* 177 */         aModel.setMessageFilter(msgField.getText());
/*     */       }
/*     */       
/*     */ 
/* 181 */     });
/* 182 */     gridbag.setConstraints(msgField, c);
/* 183 */     add(msgField);
/*     */     
/*     */ 
/* 186 */     c.weightx = 0.0D;
/* 187 */     c.fill = 2;
/* 188 */     c.anchor = 13;
/* 189 */     c.gridx = 2;
/*     */     
/* 191 */     c.gridy = 0;
/* 192 */     JButton exitButton = new JButton("Exit");
/* 193 */     exitButton.setMnemonic('x');
/* 194 */     exitButton.addActionListener(ExitAction.INSTANCE);
/* 195 */     gridbag.setConstraints(exitButton, c);
/* 196 */     add(exitButton);
/*     */     
/* 198 */     c.gridy += 1;
/* 199 */     JButton clearButton = new JButton("Clear");
/* 200 */     clearButton.setMnemonic('c');
/* 201 */     clearButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent aEvent) {
/* 203 */         aModel.clear();
/*     */       }
/* 205 */     });
/* 206 */     gridbag.setConstraints(clearButton, c);
/* 207 */     add(clearButton);
/*     */     
/* 209 */     c.gridy += 1;
/* 210 */     final JButton toggleButton = new JButton("Pause");
/* 211 */     toggleButton.setMnemonic('p');
/* 212 */     toggleButton.addActionListener(new ActionListener() {
/*     */       public void actionPerformed(ActionEvent aEvent) {
/* 214 */         aModel.toggle();
/* 215 */         toggleButton.setText(aModel.isPaused() ? "Resume" : "Pause");
/*     */       }
/*     */       
/* 218 */     });
/* 219 */     gridbag.setConstraints(toggleButton, c);
/* 220 */     add(toggleButton);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\chainsaw\ControlPanel.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */