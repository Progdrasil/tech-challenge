package tech.challenge

import com.github.michaelbull.result.*
import org.hipparchus.ode.events.Action
import org.orekit.frames.TopocentricFrame
import org.orekit.propagation.analytical.tle.TLEPropagator
import org.orekit.propagation.events.ElevationExtremumDetector
import org.orekit.time.AbsoluteDate

fun calculateOverheadPasses(bundle: Bundle): Result<Map<String, List<OverheadPass>>, Error> = binding {
    val tles = getTles(bundle.tles).bind()
    val frames = getFrames(bundle.targets)

    frames.associate { site -> site.name to tles.map { site.calculateOverheadPass(it) } }
}

fun getTles(data: List<String>) = data.map(TitledTLE::parse).combine()

fun getFrames(data: List<Site>) = data.map(Site::toFrame)

fun TopocentricFrame.calculateOverheadPass(tle: TitledTLE): OverheadPass {
    val propagator = TLEPropagator.selectExtrapolator(tle.tle)
    var startDate: AbsoluteDate? = null
    var endDate: AbsoluteDate? = null
    // Magic values from Documentation
    val detector = ElevationExtremumDetector(60.0, 0.001, this)
        .withHandler { s, detector, increasing ->
            if (increasing) {
                startDate = s.date
                Action.CONTINUE
            } else {
                endDate = s.date
                Action.STOP
            }
        }
    propagator.addEventDetector(detector)
    // seemed like a reasonable amount, there must be a better way though -.-
    propagator.propagate(tle.tle.date.shiftedBy(600000.0))

    return OverheadPass(
        startDate?.toString() ?: "Unknown",
        endDate?.toString() ?: "Unknown",
        tle.title ?: "Unknown satellite"
    )
}