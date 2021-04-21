package tech.challenge

import org.orekit.bodies.OneAxisEllipsoid
import org.orekit.frames.FramesFactory
import org.orekit.utils.Constants
import org.orekit.utils.IERSConventions

val earth = OneAxisEllipsoid(
    Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
    Constants.WGS84_EARTH_FLATTENING,
    FramesFactory.getITRF(IERSConventions.IERS_2010, true)
)