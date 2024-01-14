(ns company.db)

(def galvanizacoes
  [{:label "cold" :data :a-frio}
   {:label "hot" :data :a-quente}
   {:label "none" :data :nenhuma :checked true}])

(def paineis
  [{:data {:marca "AMERISOLAR", :modelo "AS-7M144-HC", :potencia 545, :altura 2256, :largura 1133}, :label "AS-7M144-HC"}
   {:data {:marca "BEDIN", :modelo "BS", :potencia 545, :altura 2256, :largura 1133}, :label "BS"}
   {:data {:marca "BYD", :modelo "P6K-36-5BB", :potencia 335, :altura 1960, :largura 990}, :label "P6K-36-5BB"}
   {:data {:marca "BYD", :modelo "PHK-36-SERIE-5BB", :potencia 340, :altura 1992, :largura 992}, :label "PHK-36-SERIE-5BB"}
   {:data {:marca "CANADIAN", :modelo "CS3W", :potencia 450, :altura 2108, :largura 1048}, :label "CS3W"}
   {:data {:marca "CANADIAN", :modelo "HiKu7 Mono PERC", :potencia 590, :altura 2172, :largura 1303}, :label "HiKu7 Mono PERC"}
   {:data {:marca "DAH", :modelo "DHM-72X10", :potencia 525, :altura 2256, :largura 1133}, :label "DHM-72X10"}
   #_{:data {:marca "DAH", :modelo "DHM-72X10", :potencia 545, :altura 2279, :largura 1134}, :label "DHM-72X10"}
   {:data {:marca "JINKO", :modelo "JKM540M-7TL4-V", :potencia 540, :altura 2230, :largura 1086}, :label "JKM540M-7TL4-V"}
   {:data {:marca "RISEN", :modelo "RSM150-8-480M-505M", :potencia 505, :altura 2220, :largura 1062}, :label "RSM150-8-480M-505M"}
   {:data {:marca "RISEN", :modelo "RSM120-8-580M-605M", :potencia 590, :altura 2172, :largura 1303}, :label "RSM120-8-580M-605M"}
   {:data {:marca "JINKO", :modelo "JKM530M-72HL4-TV", :potencia 530, :altura 2274, :largura 1134}, :label "JKM530M-72HL4-TV"}
   {:data {:marca "JINKO", :modelo "JKM540M-72HL4", :potencia 540, :altura 2274, :largura 1134}, :label "JKM540M-72HL4"}
   {:data {:marca "AE", :modelo "HM6L-60", :potencia 460, :altura 1910, :largura 1133}, :label "HM6L-60"}
   {:data {:marca "TRINA", :modelo "TSM-DE18M", :potencia 510, :altura 2187, :largura 1061}, :label "TSM-DE18M"}
   {:data {:marca "JINKO", :modelo "JKM470M-7RL3", :potencia 470, :altura 2182, :largura 1029}, :label "JKM470M-7RL3"}
   {:data {:marca "JINKO", :modelo "JKM440-460M-60HL4", :potencia 460, :altura 1903, :largura 1134}, :label "JKM440-460M-60HL4"}
   {:data {:marca "BALFAR", :modelo "BF400", :potencia 400, :altura 1986, :largura 1004}, :label "BF400"}
   {:data {:marca "LEAPTON", :modelo "LP210-M-66-MH 650-670W", :potencia 665, :altura 2384, :largura 1303}, :label "LP210-M-66-MH 650-670W"}
   {:data {:marca "BALFAR", :modelo "BS72M", :potencia 400, :altura 1986, :largura 1004}, :label "BS72M"}
   {:data {:marca "JINKO", :modelo "JKM460M-60HL4-V", :potencia 460, :altura 1903, :largura 1134}, :label "JKM460M-60HL4-V"}])

