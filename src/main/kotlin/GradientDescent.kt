import utils.Vector

/**
 * Created by Евгения on 23.10.2016.
 */


val epsilon = 0.01
val initialCoeffs = Vector(0.0, 0.0, 0.0)

fun doGradientDescent(dataObjects: List<Main.DataObject>): Vector {
    var currentCoeffs = initialCoeffs
    var counter = 1
    do {
        val previousCoeffs = currentCoeffs
        val step = 0.0001
        currentCoeffs = previousCoeffs - Vector(listOf(
                dataObjects.map { step * getGradientComponent(it.area, currentCoeffs, it) }.average(),
                dataObjects.map { step * getGradientComponent(it.rooms, currentCoeffs, it) }.average(),
                dataObjects.map { step * getGradientComponent(1.0, currentCoeffs, it) }.average()))
        counter++
        val error = (currentCoeffs - previousCoeffs).norm()
        //if (counter % 10 == 0) println("Current error is $error.")
        println((mse(currentCoeffs)))
    } while (error >= epsilon)
    return currentCoeffs
}

/**
 *  Q(w0, w1, w2, X) = 1/l * sum by i from 1 to  l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price)^2
 *  dQ/dw2 = 2/l * sum by i from 1 to l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price) * x_i_area
 *  dQ/dw1 = 2/l * sum by i from 1 to l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price) * x_i_rooms
 *  dQ/dw0 = 2/l * sum by i from 1 to l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price)
 *  This function calculates one summand for dQ/dw_number, the average must be calculated
 *  in the caller's method.
 */
fun getGradientComponent(feature: Double, coeffs: Vector, dataObject: Main.DataObject): Double {
    return 2 * (
            coeffs[0] * dataObject.area +
                    coeffs[1] * dataObject.rooms +
                    coeffs[2] * 1.0 -
                    dataObject.price) * feature
}

/**
 * Q(w0, w1, w2, X) = 1/l * sum by i from 1 to  l (w2 * x_i_area + w1 * x_i_rooms + w0 - y_i_price)^2
 */
fun mse(coeffs: Vector): Double {
    return -1 * data.sumByDouble {
        Math.pow(coeffs[0] * it.area + coeffs[1] * it.rooms + coeffs[2] * 1.0 - it.price, 2.0)
    } / data.size
}