import utils.Vector
import java.io.File
import java.util.*

/**
 * Created by Евгения on 02.10.2016.
 */

object Main {

    val lines = File("prices.txt").readLines().drop(1)

    val areas = Vector(lines.map { it.split(",")[0].toDouble() })
    val maxArea = areas.components.max()!!.toDouble()

    val rooms = Vector(lines.map { it.split(",")[1].toDouble() })
    val maxRooms = rooms.components.max()!!.toDouble()

    val prices = Vector(lines.map { it.split(",")[2].toDouble() })

    val data = lines.map { it ->
        val (area, rooms, price) = it.split(",").map(String::toDouble)
        DataObject(area / maxArea, rooms / maxRooms, price)
    }

    val epsilon = 0.01
    val weightVector = Vector(0.0, 0.0, 0.0)

    @JvmStatic fun main(args: Array<String>) {

        val x = doGradientDescent(data)
        val (a, r, f) = x.components
        println("${a / maxArea}, ${r / maxRooms}, $f")
    }

    fun getWeight(answers: Vector, featureValues: Vector): Double {
        return (answers * featureValues) / (featureValues * featureValues)
    }

    val lambda = 1 / 20.0

    fun doGradientDescent(dataObjects: List<DataObject>): Vector {
        val weights = weightVector
        var weightTPrev = weights
        var weightT = weights
        val random = Random()
        var counter = 1
        do {
            weightTPrev = weightT
            val step = 0.0001
            weightT = weightTPrev - Vector(listOf(
                    dataObjects.map { getGradientComponent(step, it.area, weightT, it) }.average(),
                    dataObjects.map { getGradientComponent(step, it.rooms, weightT, it) }.average(),
                    dataObjects.map { getGradientComponent(step, 1.0, weightT, it) }.average()))
            counter++
            val error = (weightT - weightTPrev).norm()
            if (counter % 10 == 0) println(error)
        } while (error >= epsilon)
        return weightT
    }

    fun getGradientComponent(step: Double, feature: Double, weights: Vector, dataObject: DataObject): Double {
        return 2 * step * feature * (
                weights[0] * dataObject.area +
                        weights[1] * dataObject.rooms +
                        weights[2] * 1.0 -
                        dataObject.price)
    }


    data class DataObject(val area: Double, val rooms: Double, val price: Double) {
        val featuresVector = Vector(listOf(area, rooms))
    }
}
