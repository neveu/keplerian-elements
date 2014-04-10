;;; "Keplerian Elements for Approximate Positions of the
;;;  Major Planets" by E.M. Standish (JPL/Caltech) available from
;;;  the JPL Solar System Dynamics web site (http://ssd.jpl.nasa.gov/).
;;;  

(require '[clj-time.core :as t])
(require '[clj-time.format :as time-format])

(defn keplerian-element
  ([a0  e0  I0  L0  wbar0  omega0
    dadt  dedt  dIdt  dLdt  dwbardt  domegadt]
     (keplerian-element a0  e0  I0  L0  wbar0  omega0
                        dadt  dedt  dIdt  dLdt  dwbardt  domegadt 0 0 0 0))
  ([a0  e0  I0  L0  wbar0  omega0
    dadt  dedt  dIdt  dLdt  dwbardt  domegadt
    b c s f]
     {:a0 a0 :e0 e0 :I0 I0 :L0 L0 :wbar0 wbar0 :omega0 omega0
      :dadt dadt :dedt dedt :dIdt dIdt :dLdt dLdt :dwbardt dwbardt :domegadt domegadt
      :b b :c c :f f :s s}))

;;; data from http://ssd.jpl.nasa.gov/txt/p_elem_t2.txt
(def Sun (keplerian-element 0 0 0 0 0 0 0 0 0 0 0 0))

(def Mercury
  (keplerian-element 0.38709927 0.20563593 7.00497902 252.25032350 77.45779628 48.33076593
                     0.00000037 0.00001906 -0.00594749 149472.67411175 0.16047689 -0.12534081))

(def Venus
  (keplerian-element 0.72333566 0.00677672 3.39467605 181.97909950 131.60246718 76.67984255
                     0.00000390 -0.00004107 -0.00078890 58517.81538729 0.00268329 -0.27769418))

(def EmBary
  (keplerian-element 1.00000261 0.01671123 -0.00001531 100.46457166    102.93768193 0.0
                     0.00000562 -0.00004392 -0.01294668 35999.37244981 0.32327364   0.0))

(def Mars
  (keplerian-element 1.52371034 0.09339410 1.84969142 -4.55343205 -23.94362959 49.55953891
                     0.00001847 0.00007882 -0.00813131 19140.30268499 0.44441088 -0.29257343))

(def Jupiter
  (keplerian-element 5.20288700 0.04838624 1.30439695 34.39644051 14.72847983 100.47390909
                     -0.00011607 -0.00013253 -0.00183714 3034.74612775 0.21252668 0.20469106))

(def Saturn
  (keplerian-element 9.53667594 0.05386179 2.48599187 49.95424423 92.59887831 113.66242448
                     -0.00125060 -0.00050991 0.00193609 1222.49362201 -0.41897216 -0.28867794))

;;; Table 2a. Keplerian elements and their rates, with respect to the
;;; mean ecliptic and equinox of J2000, valid for the time-interval
;;; 3000 BC -- 3000 AD.  NOTE: the computation of M for Jupiter
;;; through Pluto *must* be augmented by the additional terms
;;; http://ssd.jpl.nasa.gov/txt/p_elem_t2.txt

(def Mercury3k
  (keplerian-element 0.38709843 0.20563661 7.00559432 252.25166724 77.45771895 48.33961819 
                     0.00000000 0.00002123 -0.00590158 149472.67486623 0.15940013 -0.12214182))

(def Venus3k
  (keplerian-element 0.72332102 0.00676399 3.39777545 181.97970850 131.76755713 76.67261496 
                     -0.00000026 -0.00005107 0.00043494 58517.81560260 0.05679648 -0.27274174))

(def EmBary3k
  (keplerian-element 1.00000018 0.01673163 -0.00054346 100.46691572 102.93005885 -5.11260389 
                     -0.00000003 -0.00003661 -0.01337178 35999.37306329 0.31795260 -0.24123856))

(def Mars3k
  (keplerian-element 1.52371243 0.09336511 1.85181869 -4.56813164 -23.91744784 49.71320984 
                     0.00000097 0.00009149 -0.00724757 19140.29934243 0.45223625 -0.26852431))

(def Jupiter3k
  (keplerian-element 5.20248019 0.04853590 1.29861416 34.33479152 14.27495244 100.29282654 
                     -0.00002864 0.00018026 -0.00322699 3034.90371757 0.18199196 0.13024619
                     -0.00012452    0.06064060   -0.35635438   38.35125000))

(def Saturn3k
  (keplerian-element 9.54149883 0.05550825 2.49424102 50.07571329 92.86136063 113.63998702 
                     -0.00003065 -0.00032044 0.00451969 1222.11494724 0.54179478 -0.25015002
                     0.00025899   -0.13434469    0.87320147   38.35125000))

(def Uranus3k
  (keplerian-element 19.18797948 0.04685740 0.77298127 314.20276625 172.43404441 73.96250215 
                     -0.00020455 -0.00001550 -0.00180155 428.49512595 0.09266985 0.05739699
                      0.00058331   -0.97731848    0.17689245    7.67025000))

(def Neptune3k
  (keplerian-element 30.06952752 0.00895439 1.77005520 304.22289287 46.68158724 131.78635853 
                     0.00006447 0.00000818 0.00022400 218.46515314 0.01009938 -0.00606302
                     -0.00041348    0.68346318   -0.10162547    7.67025000))


(defn in-centuries [in]
  (/ (t/in-days in) 36524.2))

(defn j2000 [datetime]
  (t/interval (t/date-time 2000) datetime))

(in-centuries (j2000 (t/now)))

(defn ft [t [x0 dxdt]]
  "x0 + t*dxdt"
  (+ x0 (* t dxdt)))

(defn aeILwo [el t]
  "Calculate the elements as a function of time, t in centuries since 1/1/2000"
  (map (partial ft t)
       [[(:a0 el) (:dadt el)]
        [(:e0 el) (:dedt el)]
        [(:I0 el) (:dIdt el)]
        [(:L0 el) (:dLdt el)]
        [(:wbar0 el) (:dwbardt el)]
        [(:omega0 el) (:domegadt el)]]))

(defn +-180 [n]
  "maps angle n to +/-180"
	(let [n (mod n 360)]
	  (if (< n 180)
	      n
	    (- n 360))))

(defn radians [deg]
  (* deg (/ Math/PI 180.0)))

(defn cosd [deg]
  (Math/cos (radians deg)))

(defn sind [deg]
  (Math/sin (radians deg)))

(defn tand [deg]
  (Math/tan (radians deg)))

(defn solve-for-E [M e e* [En dE]]
    (let [dM (- M (- En (* e* (sind En))))
          dE (/ dM (- 1 (* e (cosd En))))]
      [(+ En dE) dE]))

(defn E-dE [M e e* tol]
  (let  [E0 (+ M (* e* (sind M)))]
    (first (drop-while (fn [[En dE]] (> (Math/abs dE) tol))
                       (iterate (partial solve-for-E M e e*)
                                [E0 1])))))

(defn outer-solar-system-correction [el t]
  (let [[b c f s][(:b el)(:c el)(:f el)(:s el)]]
    (+ (* b t t)
       (* c (cosd (* f t)))
       (* s (sind (* f t))))))

(defn ephemeris
  ([el t tol]
     "Take keplerian element spec el= {:a0 a0 :e0 e0 :I0 I0 :L0
L0 :wbar0 wbar0 :omega0 omega0 :dadt dadt :dedt dedt :dIdt dIdt :dLdt
dLdt :dwbardt dwbardt :domegadt domegadt} and time t in centuries
after 1/1/2000 and error tolerance tol. Returns {:E E :a a :e e :w
w :omega omega :I I}"
     (let [[a e I L wbar omega] (aeILwo el t)
           M (+-180 (+ (- L wbar) (outer-solar-system-correction el t)))
           e* (* 57.29578 e)
           [E dE] (E-dE M e e* tol)]
       {:E E :a a :e e :w (- wbar omega) :omega omega :I I}))

  ([el datetime]
     (ephemeris el (in-centuries (j2000 datetime)) 1e-4)))

(ephemeris Mercury3k (t/now))
(ephemeris Jupiter (t/now))
(ephemeris Jupiter3k (t/now))

(defn xp [eph]
  (* (:a eph) (- (cosd (:E eph)) (:e eph))))

(defn yp [eph]
  (* (:a eph)
     (Math/sqrt (- 1 (* (:e eph) (:e eph))))
     (sind (:E eph))))

(xp (ephemeris Mercury (t/now)))
(yp (ephemeris Mercury (t/now)))

(defn xecl [eph]
  "Calculate the x-coordinate of the body in the ecliptic"
  (let [w (:w eph)
        omega (:omega eph)
        I (:I eph)
        x (xp eph)
        y (yp eph)]
    (+ (* x (- (* (cosd w) (cosd omega))
               (* (sind w)(sind omega)(cosd I))))
       (* y (- (- (* (sind w)(cosd omega)))
               (* (cosd w)(sind omega)(cosd I)))))))

(defn yecl [eph]
  "Calculate the y-coordinate of the body in the ecliptic"
  (let [w (:w eph)
        omega (:omega eph)
        I (:I eph)
        x (xp eph)
        y (yp eph)]
    (+ (* x (+ (* (cosd w) (sind omega))
               (* (sind w)(cosd omega)(cosd I))))
       (* y (+ (- (* (sind w)(sind omega)))
               (* (cosd w)(cosd omega)(cosd I)))))))

(defn zecl [eph]
  "Calculate the z-coordinate of the body in the ecliptic"
  (let [w (:w eph)
        I (:I eph)
        x (xp eph)
        y (yp eph)]
    (+ (* (sind w)(sind I) x)
       (* (cosd w)(sind I) y))))
          
(xecl (ephemeris Mercury (t/now)))
(yecl (ephemeris Mercury (t/now)))
(zecl (ephemeris Mercury (t/now)))


;;; Lunar ephemeris

(defn lambda [t] (+ 218.32
                    (* 481267.883  t)
                    (* 6.29  (sind (+ (* 477198.85 t) 134.9)))
                    (* -1.27 (sind (+ (* 413335.38 t) 259.2)))
                    (* 0.66  (sind (+ (* 890534.23 t) 235.7)))
                    (* 0.21  (sind (+ (* 954397.70 t) 269.9)))
                    (* -0.19 (sind (+ (* 35999.05 t)  357.5)))
                    (* -0.11 (sind (+ (* 966404.05 t) 186.6)))))

(defn beta [t] (+ (* 5.13  (sind (+ (* 483202.03 t) 93.3)))
                  (* 0.28  (sind (+ (* 960400.87 t) 228.2)))
                  (* -0.28 (sind (+ (* 6003.18 t) 318.3)))
                  (* -0.17 (sind (+ (* 407332.20 t) 217.6)))))

(defn pi [t] (+ 0.9508
                (* 0.0518 (cosd (+ (* 477198.85 t) 134.9)))
                (* 0.0095 (cosd (+ (* 413335.38 t) 259.2)))
                (* 0.0078 (cosd (+ (* 890534.23 t) 235.7)))
                (* 0.0028 (cosd (+ (* 954397.70 t) 269.9)))))

(defn poly [a0 a1 a2 t]
  (+ a0 (* a1 t) (* a2 t t)))

(defn a [t]
  (poly 0 1.396971 0.0003086 t))

(defn b [t]
  (poly 0 0.013056 0.0000092 t))

(defn c [t]
  (poly 5.12362 -1.155358 -0.0001964 t))

(defn distance [t]
  (/ 6378.140 (sind (pi t))))

(declare beta0)

(defn lambda0 [t]
  (+ (lambda t)
     (- (a t))
     (* (b t)
        (cosd (+ (lambda t)(c t)))
        (tand (beta0 t)))))

(defn beta0 [t]
  (- (beta t)
     (* (b t)
        (sind (+ (lambda t) (c t))))))

(defn xeq [t]
  (* (distance t)(cosd (beta0 t))(cosd (lambda0 t))))

(defn ycommon [t obliq]
  (* (distance t)
     (- (* (cosd (beta0 t))
           (sind (lambda0 t))
           (cosd obliq))
        (* (sind (beta0 t))
           (sind obliq)))))

(defn zcommon [t obliq]
  (* (distance t)
     (+ (* (cosd (beta0 t))
           (sind (lambda0 t))
           (sind obliq))
        (* (sind (beta0 t))
           (cosd obliq)))))

(def obliquityJ2000 23.43928)

(defn yeq [t]
  (ycommon t obliquityJ2000))

(defn zeq [t]
  (zcommon t obliquityJ2000))

(defn xecl-lunar [t]
  (xeq (in-centuries (j2000 t))))

(defn yecl-lunar [t]
  (ycommon (in-centuries (j2000 t)) 0))

(defn zecl-lunar [t]
  (zcommon (in-centuries (j2000 t)) 0))

(xecl-lunar (t/now))
(yecl-lunar (t/now))
(zecl-lunar (t/now))

(defn newMoonEpoch [datetime]
  (t/interval (t/date-time 1970 1 7) datetime))

(newMoonEpoch (t/now))

(defn phaseFraction [datetime]
  (let [lp 2551443.0] 
    (/ (mod (t/in-secs (newMoonEpoch datetime))
            lp)
       lp)))

(defn phaseImageIndex [datetime numImages]
  (mod (int (Math/floor (* (phaseFraction datetime)
                           numImages)))
       numImages))




