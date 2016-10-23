import utils.Vector
import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.thenBy

/**
 * Created by Евгения on 23.10.2016.
 */

val data = Main.data
val initialGenerationSize = 1000
val maxIterationsSize = 100

fun getCoefficients(): Vector {
    val coeffs = TreeSet<Vector>(compareBy(::fitness).thenBy { System.identityHashCode(it) })
    for (i in 1..initialGenerationSize)
        coeffs.add(generateNormalDistributionVector(1e6))
    val beingsToDrop = getNPercents(initialGenerationSize, 50)
    var prevCoeffLast : Double = 0.0
    repeat(maxIterationsSize) {
        coeffs.retainAll(coeffs.drop(beingsToDrop))
        for (x in coeffs.toList()) {
            coeffs.add(x + generateNormalDistributionVector(100000.0))
        }
        println("fitness = ${fitness(coeffs.last())}")
        prevCoeffLast = fitness(coeffs.last())
    }
    return coeffs.last()
}

fun getNPercents(totalAmount: Int, percents: Int): Int = (totalAmount * percents) / 100

val r = Random()

fun generateNormalDistributionVector(dispersion: Double): Vector {
    return Vector(generateNormalDistributionNumber() * dispersion, generateNormalDistributionNumber() * dispersion,
            generateNormalDistributionNumber() * dispersion)
}

fun generateNormalDistributionNumber(): Double {
    var u = r.nextDouble() * 2 - 1
    if (Math.abs(u) == 1.0)
        u = 0.0
    val u2 = Math.pow(u, 2.0)
    var s = (1 - u2) * r.nextDouble() + u2
    if (Math.abs(s) == 1.0)
        s = u2 + 0.99
    return u * Math.sqrt(-2 * Math.log(s) / s)
}

/**
 * Q(w0, w1, w2, X) = 1/l * sum by i from 1 to  l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price)^2
 */
fun fitness(coeffs: Vector): Double {
    return -1 * data.sumByDouble {
        Math.pow(coeffs[0] * it.area + coeffs[1] * it.rooms + coeffs[2] * 1.0 - it.price, 2.0)
    } / data.size
}