package utils

/**
 * Created by Евгения on 02.10.2016.
 */

data class Vector(val components: List<Double>) : Cloneable {

    constructor(vararg components: Double): this(listOf(*components.toTypedArray()))

    val dimensions: Int get() = components.size

    operator fun get(component: Int): Double = components[component]

    operator fun plus(that: Vector): Vector {
        return Vector(components.zip(that.components).map { it.first + it.second })
    }

    operator fun minus(that: Vector): Vector = plus(that.times(-1.0))

    operator fun times(a: Double) = Vector(components.map { it * a })

    /**
     * Calculates scalar product with another vector.
     * @param v Another vector.
     * @return A number -- (this, v), which is scalar product.
     */
    operator fun times(v: Vector): Double {
        if (dimensions != v.dimensions)
            throw IllegalArgumentException()
        var result = 0.0
        for (i in 0..dimensions - 1) {
            result += get(i) * v[i]
        }
        return result
    }

    /**
     * Checks if all the components of this and another vector differ not more than by epsilon.
     * @param that A vector to compare with;
     * @param epsilon Permitted difference.
     * @return For all i: |this_i - that_i| <= epsilon
     */
    fun similar(that: Vector, epsilon: Double): Boolean {
        if (dimensions != that.dimensions)
            return false
        for (i in 0..dimensions - 1) {
            if (Math.abs(get(i) - that[i]) >= epsilon)
                return false
        }
        return true
    }

    /**
     * Calculates the Euclidean norm of vector.
     * @return |a| = sqrt(Sum {1..n} a_i^2)
     */
    fun norm(): Double {
        return Vector.distance(this, Vector.zero(dimensions))
    }

    override fun toString(): String {
        val result = StringBuilder("(")
        for (i in 0..dimensions - 1) {
            result.append(get(i))
            if (i != dimensions - 1)
                result.append(", ")
        }
        result.append(")")
        return result.toString()
    }

    companion object {
        /**
         * Calculates the distance between two vectors, |a - b|.
         * @return |a - b| = sqrt(Sum {1..n} (b_i - a_i)^2)
         */
        fun distance(a: Vector, b: Vector): Double {
            if (a.dimensions != b.dimensions) {
                throw IllegalArgumentException("a and b should be of the same dimension.")
            }
            var quadsSum = 0.0
            for (i in 0..a.dimensions - 1) {
                val componentDiff = b.get(i) - a.get(i)
                quadsSum += componentDiff * componentDiff
            }
            return Math.sqrt(quadsSum)
        }

        /**
         * Creates an n-dimensional zero vector
         * @param dimensions n - number of components
         * @return (0, 0,..., 0) { n components }
         */
        public fun zero(dimensions: Int) = Vector((0..dimensions-1).map { 0.0 })
    }
}