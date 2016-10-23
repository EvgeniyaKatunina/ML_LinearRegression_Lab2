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

    data class DataObject(val area: Double, val rooms: Double, val price: Double)

    fun printlnResult(result: Vector) {
        val (a, r, c) = result.components
        println("price = ${a / maxArea} * area ${r / maxRooms} * rooms + $c")
    }
}

object GradientDescentLauncher {

    @JvmStatic fun main(args: Array<String>) {
        Main.printlnResult(doGradientDescent(Main.data))
    }

}

object GeneticAlgorithmLauncher {

    @JvmStatic fun main(args: Array<String>) {
        Main.printlnResult(getCoefficients())
    }

}
