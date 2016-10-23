import utils.Vector
import java.io.File

/**
 * Created by Евгения on 02.10.2016.
 */

object Main {

    val lines = File("prices.txt").readLines().drop(1)

    val areas = Vector(lines.map { it.split(",")[0].toDouble() })
    val maxArea = areas.components.max()!!.toDouble()

    val rooms = Vector(lines.map { it.split(",")[1].toDouble() })
    val maxRooms = rooms.components.max()!!.toDouble()

    val data = lines.map { it ->
        val (area, rooms, price) = it.split(",").map(String::toDouble)
        DataObject(area / maxArea, rooms / maxRooms, price)
    }

    val epsilon = 0.01
    val initialCoeffs = Vector(0.0, 0.0, 0.0)

    @JvmStatic fun main(args: Array<String>) {

        val x = doGradientDescent(data)
        val (a, r, c) = x.components
        println("price = ${a / maxArea} * area ${r / maxRooms} * rooms + $c")
    }

    fun doGradientDescent(dataObjects: List<DataObject>): Vector {
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
            if (counter % 10 == 0) println("Current error is $error.")
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
    fun getGradientComponent(feature: Double, coeffs: Vector, dataObject: DataObject): Double {
        return 2 * (
                coeffs[0] * dataObject.area +
                        coeffs[1] * dataObject.rooms +
                        coeffs[2] * 1.0 -
                        dataObject.price) * feature
    }

    data class DataObject(val area: Double, val rooms: Double, val price: Double)
}
