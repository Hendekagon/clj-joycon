(ns joycon.vibration
  "

  Functions for sending vibrations (rumbles)
  to your joycons


  "
  (:require [joycon.core :as jc]))

; https://github.com/dekuNukem/Nintendo_Switch_Reverse_Engineering/blob/master/rumble_data_table.md

(def frequency-table [
 [0x00 0x00 0x01 40.875885 41]
 [0x00 0x00 0x02 41.77095  42]
 [0x00 0x00 0x03 42.685616 43]
 [0x00 0x00 0x04 43.620308 44]
 [0x00 0x00 0x05 44.57547  45]
 [0x00 0x00 0x06 45.551544 46]
 [0x00 0x00 0x07 46.548996 47]
 [0x00 0x00 0x08 47.568283 48]
 [0x00 0x00 0x09 48.609894 49]
 [0x00 0x00 0x0A 49.674313 50]
 [0x00 0x00 0x0B 50.762039 51]
 [0x00 0x00 0x0C 51.873581 52]
 [0x00 0x00 0x0D 53.009464 53]
 [0x00 0x00 0x0E 54.170223 54]
 [0x00 0x00 0x0F 55.356396 55]
 [0x00 0x00 0x10 56.568542 57]
 [0x00 0x00 0x11 57.807232 58]
 [0x00 0x00 0x12 59.073048 59]
 [0x00 0x00 0x13 60.366577 60]
 [0x00 0x00 0x14 61.688435 62]
 [0x00 0x00 0x15 63.039234 63]
 [0x00 0x00 0x16 64.419617 64]
 [0x00 0x00 0x17 65.830215 66]
 [0x00 0x00 0x18 67.271713 67]
 [0x00 0x00 0x19 68.744774 69]
 [0x00 0x00 0x1A 70.250084 70]
 [0x00 0x00 0x1B 71.788361 72]
 [0x00 0x00 0x1C 73.360321 73]
 [0x00 0x00 0x1D 74.966705 75]
 [0x00 0x00 0x1E 76.608261 77]
 [0x00 0x00 0x1F 78.285767 78]
 [0x00 0x00 0x20 80        80]
 [0x04 0x00 0x21 81.75177  82]
 [0x08 0x00 0x22 83.541901 84]
 [0x0C 0x00 0x23 85.371231 85]
 [0x10 0x00 0x24 87.240616 87]
 [0x14 0x00 0x25 89.15094  89]
 [0x18 0x00 0x26 91.103088 91]
 [0x1C 0x00 0x27 93.097992 93]
 [0x20 0x00 0x28 95.136566   95]
 [0x24 0x00 0x29 97.219788   97]
 [0x28 0x00 0x2A 99.348625   99]
 [0x2C 0x00 0x2B 101.524078 102]
 [0x30 0x00 0x2C 103.747162 104]
 [0x34 0x00 0x2D 106.018929 106]
 [0x38 0x00 0x2E 108.340446 108]
 [0x3C 0x00 0x2F 110.712791 111]
 [0x40 0x00 0x30 113.137085 113]
 [0x44 0x00 0x31 115.614464 116]
 [0x48 0x00 0x32 118.146095 118]
 [0x4C 0x00 0x33 120.733154 121]
 [0x50 0x00 0x34 123.376869 123]
 [0x54 0x00 0x35 126.078468 126]
 [0x58 0x00 0x36 128.839233 129]
 [0x5C 0x00 0x37 131.660431 132]
 [0x60 0x00 0x38 134.543427 135]
 [0x64 0x00 0x39 137.489548 137]
 [0x68 0x00 0x3A 140.500168 141]
 [0x6C 0x00 0x3B 143.576721 144]
 [0x70 0x00 0x3C 146.720642 147]
 [0x74 0x00 0x3D 149.933411 150]
 [0x78 0x00 0x3E 153.216522 153]
 [0x7C 0x00 0x3F 156.571533 157]
 [0x80 0x00 0x40 160        160]
 [0x84 0x00 0x41 163.50354  164]
 [0x88 0x00 0x42 167.083801 167]
 [0x8C 0x00 0x43 170.742462 171]
 [0x90 0x00 0x44 174.481232 174]
 [0x94 0x00 0x45 178.30188  178]
 [0x98 0x00 0x46 182.206177 182]
 [0x9C 0x00 0x47 186.195984 186]
 [0xA0 0x00 0x48 190.273132 190]
 [0xA4 0x00 0x49 194.439575 194]
 [0xA8 0x00 0x4A 198.69725  199]
 [0xAC 0x00 0x4B 203.048157 203]
 [0xB0 0x00 0x4C 207.494324 207]
 [0xB4 0x00 0x4D 212.037857 212]
 [0xB8 0x00 0x4E 216.680893 217]
 [0xBC 0x00 0x4F 221.425583 221]
 [0xC0 0x00 0x50 226.27417  226]
 [0xC4 0x00 0x51 231.228928 231]
 [0xC8 0x00 0x52 236.292191 236]
 [0xCC 0x00 0x53 241.466309 241]
 [0xD0 0x00 0x54 246.753738 247]
 [0xD4 0x00 0x55 252.156937 252]
 [0xD8 0x00 0x56 257.678467 258]
 [0xDC 0x00 0x57 263.320862 263]
 [0xE0 0x00 0x58 269.086853 269]
 [0xE4 0x00 0x59 274.979095 275]
 [0xE8 0x00 0x5A 281.000336 281]
 [0xEC 0x00 0x5B 287.153442 287]
 [0xF0 0x00 0x5C 293.441284 293]
 [0xF4 0x00 0x5D 299.866821 300]
 [0xF8 0x00 0x5E 306.433044 306]
 [0xFC 0x00 0x5F 313.143066 313]
 [0x00 0x01 0x60 320        320]
 [0x04 0x01 0x61 327.00708  327]
 [0x08 0x01 0x62 334.167603 334]
 [0x0C 0x01 0x63 341.484924 341]
 [0x10 0x01 0x64 348.962463 349]
 [0x14 0x01 0x65 356.60376  357]
 [0x18 0x01 0x66 364.412354 364]
 [0x1C 0x01 0x67 372.391968 372]
 [0x20 0x01 0x68 380.546265 381]
 [0x24 0x01 0x69 388.87915  389]
 [0x28 0x01 0x6A 397.394501 397]
 [0x2C 0x01 0x6B 406.096313 406]
 [0x30 0x01 0x6C 414.988647 415]
 [0x34 0x01 0x6D 424.075714 424]
 [0x38 0x01 0x6E 433.361786 433]
 [0x3C 0x01 0x6F 442.851166 443]
 [0x40 0x01 0x70 452.54834 453]
 [0x44 0x01 0x71 462.457855 462]
 [0x48 0x01 0x72 472.584381 473]
 [0x4C 0x01 0x73 482.932617 483]
 [0x50 0x01 0x74 493.507477 494]
 [0x54 0x01 0x75 504.313873 504]
 [0x58 0x01 0x76 515.356934 515]
 [0x5C 0x01 0x77 526.641724 527]
 [0x60 0x01 0x78 538.173706 538]
 [0x64 0x01 0x79 549.958191 550]
 [0x68 0x01 0x7A 562.000671 562]
 [0x6C 0x01 0x7B 574.306885 574]
 [0x70 0x01 0x7C 586.882568 587]
 [0x74 0x01 0x7D 599.733643 600]
 [0x78 0x01 0x7E 612.866089 613]
 [0x7C 0x01 0x7F 626.286133 626]
 [0x80 0x01 0x00 640        640]
 [0x84 0x01 0x00 654.01416  654]
 [0x88 0x01 0x00 668.335205 668]
 [0x8C 0x01 0x00 682.969849 683]
 [0x90 0x01 0x00 697.924927 698]
 [0x94 0x01 0x00 713.20752  713]
 [0x98 0x01 0x00 728.824707 729]
 [0x9C 0x01 0x00 744.783936 745]
 [0xA0 0x01 0x00 761.092529 761]
 [0xA4 0x01 0x00 777.758301 778]
 [0xA8 0x01 0x00 794.789001 795]
 [0xAC 0x01 0x00 812.192627 812]
 [0xB0 0x01 0x00 829.977295 830]
 [0xB4 0x01 0x00 848.151428 848]
 [0xB8 0x01 0x00 866.723572 867]
 [0xBC 0x01 0x00 885.702332 886]
 [0xC0 0x01 0x00 905.09668  905]
 [0xC4 0x01 0x00 924.91571  925]
 [0xC8 0x01 0x00 945.168762 945]
 [0xCC 0x01 0x00 965.865234 966]
 [0xD0 0x01 0x00 987.014954 987]
 [0xD4 0x01 0x00 1008.627747 1009]
 [0xD8 0x01 0x00 1030.713867 1031]
 [0xDC 0x01 0x00 1053.283447 1053]
 [0xE0 0x01 0x00 1076.347412 1076]
 [0xE4 0x01 0x00 1099.916382 1100]
 [0xE8 0x01 0x00 1124.001343 1124]
 [0xEC 0x01 0x00 1148.61377  1149]
 [0xF0 0x01 0x00 1173.765137 1174]
 [0xF4 0x01 0x00 1199.467285 1199]
 [0xF8 0x01 0x00 1225.732178 1226]
 [0xFC 0x01 0x00 1252.572266 1253]])

(def amplitude-table [
 [0x00 0x00 0x40 0.0 0.0]
 [0x02 0x80 0x40 0.007843 0.01]
 [0x04 0x00 0x41 0.011823 0.012]
 [0x06 0x80 0x41 0.014061 0.014]
 [0x08 0x00 0x42 0.01672 0.017]
 [0x0A 0x80 0x42 0.019885 0.02]
 [0x0C 0x00 0x43 0.023648 0.024]
 [0x0E 0x80 0x43 0.028123 0.028]
 [0x10 0x00 0x44 0.033442 0.033]
 [0x12 0x80 0x44 0.039771 0.04]
 [0x14 0x00 0x45 0.047296 0.047]
 [0x16 0x80 0x45 0.056246 0.056]
 [0x18 0x00 0x46 0.066886 0.067]
 [0x1A 0x80 0x46 0.079542 0.08]
 [0x1C 0x00 0x47 0.094592 0.095]
 [0x1E 0x80 0x47 0.112491 0.112]
 [0x20 0x00 0x48 0.117471 0.117]
 [0x22 0x80 0x48 0.122671 0.123]
 [0x24 0x00 0x49 0.128102 0.128]
 [0x26 0x80 0x49 0.133774 0.134]
 [0x28 0x00 0x4A 0.139697 0.14]
 [0x2A 0x80 0x4A 0.145882 0.146]
 [0x2C 0x00 0x4B 0.152341 0.152]
 [0x2E 0x80 0x4B 0.159085 0.159]
 [0x30 0x00 0x4C 0.166129 0.166]
 [0x32 0x80 0x4C 0.173484 0.173]
 [0x34 0x00 0x4D 0.181166 0.181]
 [0x36 0x80 0x4D 0.189185 0.189]
 [0x38 0x00 0x4E 0.197561 0.198]
 [0x3A 0x80 0x4E 0.206308 0.206]
 [0x3C 0x00 0x4F 0.215442 0.215]
 [0x3E 0x80 0x4F 0.224982 0.225]
 [0x40 0x00 0x50 0.229908 0.23]
 [0x42 0x80 0x50 0.234943 0.235]
 [0x44 0x00 0x51 0.240087 0.24]
 [0x46 0x80 0x51 0.245345 0.245]
 [0x48 0x00 0x52 0.250715 0.251]
 [0x4A 0x80 0x52 0.256206 0.256]
 [0x4C 0x00 0x53 0.261816 0.262]
 [0x4E 0x80 0x53 0.267549 0.268]
 [0x50 0x00 0x54 0.273407 0.273]
 [0x52 0x80 0x54 0.279394 0.279]
 [0x54 0x00 0x55 0.285514 0.286]
 [0x56 0x80 0x55 0.291765 0.292]
 [0x58 0x00 0x56 0.298154 0.298]
 [0x5A 0x80 0x56 0.304681 0.305]
 [0x5C 0x00 0x57 0.311353 0.311]
 [0x5E 0x80 0x57 0.318171 0.318]
 [0x60 0x00 0x58 0.325138 0.325]
 [0x62 0x80 0x58 0.332258 0.332]
 [0x64 0x00 0x59 0.339534 0.34]
 [0x66 0x80 0x59 0.346969 0.347]
 [0x68 0x00 0x5A 0.354566 0.355]
 [0x6A 0x80 0x5A 0.362331 0.362]
 [0x6C 0x00 0x5B 0.370265 0.37]
 [0x6E 0x80 0x5B 0.378372 0.378]
 [0x70 0x00 0x5C 0.386657 0.387]
 [0x72 0x80 0x5C 0.395124 0.395]
 [0x74 0x00 0x5D 0.403777 0.404]
 [0x76 0x80 0x5D 0.412619 0.413]
 [0x78 0x00 0x5E 0.421652 0.422]
 [0x7A 0x80 0x5E 0.430885 0.431]
 [0x7C 0x00 0x5F 0.440321 0.44]
 [0x7E 0x80 0x5F 0.449964 0.45]
 [0x80 0x00 0x60 0.459817 0.46]
 [0x82 0x80 0x60 0.469885 0.47]
 [0x84 0x00 0x61 0.480174 0.48]
 [0x86 0x80 0x61 0.490689 0.491]
 [0x88 0x00 0x62 0.501433 0.501]
 [0x8A 0x80 0x62 0.512413 0.512]
 [0x8C 0x00 0x63 0.523633 0.524]
 [0x8E 0x80 0x63 0.5351 0.535]
 [0x90 0x00 0x64 0.546816 0.547]
 [0x92 0x80 0x64 0.55879 0.559]
 [0x94 0x00 0x65 0.571027 0.571]
 [0x96 0x80 0x65 0.58353 0.584]
 [0x98 0x00 0x66 0.596307 0.596]
 [0x9A 0x80 0x66 0.609365 0.609]
 [0x9C 0x00 0x67 0.622708 0.623]
 [0x9E 0x80 0x67 0.636344 0.636]
 [0xA0 0x00 0x68 0.650279 0.65]
 [0xA2 0x80 0x68 0.664518 0.665]
 [0xA4 0x00 0x69 0.679069 0.679]
 [0xA6 0x80 0x69 0.693939 0.694]
 [0xA8 0x00 0x6A 0.709133 0.709]
 [0xAA 0x80 0x6A 0.724662 0.725]
 [0xAC 0x00 0x6B 0.740529 0.741]
 [0xAE 0x80 0x6B 0.756745 0.757]
 [0xB0 0x00 0x6C 0.773316 0.773]
 [0xB2 0x80 0x6C 0.790249 0.79]
 [0xB4 0x00 0x6D 0.807554 0.808]
 [0xB6 0x80 0x6D 0.825237 0.825]
 [0xB8 0x00 0x6E 0.843307 0.843]
 [0xBA 0x80 0x6E 0.861772 0.862]
 [0xBC 0x00 0x6F 0.880643 0.881]
 [0xBE 0x80 0x6F 0.899928 0.9]
 [0xC0 0x00 0x70 0.919633 0.92]
 [0xC2 0x80 0x70 0.939771 0.94]
 [0xC4 0x00 0x71 0.960348 0.96]
 [0xC6 0x80 0x71 0.981378 0.981]
 [0xC8 0x00 0x72 1.002867 1.003]])

(def rounded-frequencies
  (vec (map last frequency-table)))

(def rounded-amplitudes
  (vec (map last amplitude-table)))

(def bytes-by-frequency
  (into {}
    (map (fn [[hfb0 hfb1 lfb2 f rf]] [rf [hfb0 hfb1 lfb2]]) frequency-table)))

(def bytes-by-amplitude
  (into {}
    (map (fn [[hab1 lab3 lab4 a ra]] [ra [hab1 lab3 lab4]]) amplitude-table)))

(defn clamp [x l h]
  (if (< x l)
    l
    (if (> x h) h x)))

(defn hex [x] (format "%02x" x))

(def l2 (Math/log 2))

; https://github.com/dekuNukem/Nintendo_Switch_Reverse_Engineering/blob/master/rumble_data_table.md
(defn encode-frequency [f]
  (let [ef (Math/round (* 32 (Math/log (* (clamp f 41 1253) 0.1)) l2))
        hf (* (- ef 0x60) 4)
        lf (- ef 0x40)]
   [(bit-and hf 2r1111111100000000) (bit-and hf 2r0000000011111111) lf]))


(defn encode-amplitude [a]
  (let [
         ca (clamp a 0 1)
         ha (cond
              (< ca 0.117) (- (/ (- (* l2 32 (Math/log (* 1000 ca))) 0x60) (- 5 (* l2 (Math/log ca)))) 1)
              (< ca 0.23) (- (- (* 32 l2 (Math/log ca)) 0x60) 0x5c)
              true (- (* 2 (- (* 32 l2 (Math/log ca)) 0x60)) 0xf6))
         la (* 0.5 (Math/round ha))
         p  (> (mod la 2) 0)
         la (int (if p (- la 1) p))
         la (int (+ 0x40 (bit-shift-right la 1)))
         la (int (if p (bit-or la 0x8000) la))
       ]
     [(int ha) (bit-and (bit-shift-right la 8) 0xff) (bit-and la 0xff)]))


; https://github.com/dekuNukem/Nintendo_Switch_Reverse_Engineering/blob/master/bluetooth_hid_notes.md#rumble-data
; [00 01 40 40 00 01 40 40] (320Hz 0.0f 160Hz 0.0f)
; A timing byte, then 4 bytes of rumble data for left Joy-Con, followed by 4 bytes for right Joy-Con.
; The rumble data structure contains 2 bytes High Band data, 2 byte Low Band data
(defn rumble-data
  ([[lf la] [rf ra]]
    (let [
          [lhfb0 lhfb1 llfb2] (bytes-by-frequency lf)
          [lhab1 llab3 llab4] (bytes-by-amplitude la)
          [rhfb0 rhfb1 rlfb2] (bytes-by-frequency rf)
          [rhab1 rlab3 rlab4] (bytes-by-amplitude ra)
         ]
         [lhfb1 (+ lhab1 lhfb0)   (+ llfb2 llab4) llab3

          rhfb1 (+ rhab1 rhfb0)   (+ rlfb2 rlab4) rlab3])))

(defn vibration-data
  ([fa]
    (vibration-data fa fa))
  ([l r]
    (into (into [0x01 0] (rumble-data l r)) jc/zer06)))

(defn send-vibrations [j tfa]
  (let [q (vibration-data [41 0.0])]
    (doseq [[t vd] (map (fn [[t f a]] [t (vibration-data [f a])]) tfa)]
     (do
       (Thread/sleep t)
       (jc/with-set-output-report j vd)
       ;(Thread/sleep t)
       ;(jc/with-set-output-report j q)
       ))))