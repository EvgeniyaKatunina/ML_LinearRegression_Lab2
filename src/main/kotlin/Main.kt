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
                    dataObjects.map { getGradientComponent(step, it.area, currentCoeffs, it) }.average(),
                    dataObjects.map { getGradientComponent(step, it.rooms,currentCoeffs, it) }.average(),
                    dataObjects.map { getGradientComponent(step, 1.0, currentCoeffs, it) }.average()))
            counter++
            val error = (currentCoeffs - previousCoeffs).norm()
            if (counter % 10 == 0) println("Current error is $error.")
        } while (error >= epsilon)
        return currentCoeffs
    }

    fun getGradientComponent(step: Double, feature: Double, coeffs: Vector, dataObject: DataObject): Double {
        return 2 * step * feature * (
                coeffs[0] * dataObject.area +
                        coeffs[1] * dataObject.rooms +
                        coeffs[2] * 1.0 -
                        dataObject.price)
    }


    data class DataObject(val area: Double, val rooms: Double, val price: Double)
}
