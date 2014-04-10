# kepler

A Clojure library that calculates the positions of planetary bodies using the keplerian elements. 

## Usage

The vars named after planets (Mercury, Venus, etc) contain the coefficients valid for 1850 CE to 2050 CE.
The vars named after planets + "3K" (e.g. Mercury3k, Venus3k, etc) contain the coefficients valid for 3000 BCE to 3000 CE.

(ephemeris Mercury (t/now))
returns the keplerian elements for the position of Mercury at the current time.

(xecl (ephemeris Mercury (t/now)))
(yecl (ephemeris Mercury (t/now)))
(zecl (ephemeris Mercury (t/now)))
return the x, y, and z coordinates in the ecliptic for Mercury at the current time.

(xecl-lunar (t/now))
(yecl-lunar (t/now))
(zecl-lunar (t/now))
return the x, y, and z coordinates in the ecliptic for the Moon at the current time

(phaseFraction (t/now)) returns a fraction between 0 and 1 representing the phase of the moon, where 0 is the new moon and 0.5 is the full moon. 

## License

Copyright © 2014

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
