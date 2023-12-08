package utils

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class FieldTest {

    private val field = Field(
        listOf(
            listOf(1, 2),
            listOf(3, 4)
        )
    )

    @Test
    fun constructor() {
        Field(2, 3) { 1 } shouldBe Field(
            listOf(
                listOf(1, 1),
                listOf(1, 1),
                listOf(1, 1),
            )
        )
    }

    @Test
    fun insertAtPosition() {
        field.insertAt(Position.origin, 10) shouldBe Field(
            listOf(
                listOf(3, 4),
                listOf(10, 2),
            )
        )
    }

    @Test
    fun search() {
        field.search(3).first() shouldBe Position(0, 1)
    }

    @Test
    fun insertAtPositions() {
        val positionMap = listOf(Position.origin, Position(1, 1)).associateWith { 5 }
        field.insertAt(positionMap) shouldBe Field(
            listOf(
                listOf(3, 5),
                listOf(5, 2),
            )
        )
    }

    @Test
    fun map() {
        field.map { _, cell ->
            cell * 2
        } shouldBe Field(
            listOf(
                listOf(6, 8),
                listOf(2, 4),
            )
        )
    }

    @Test
    fun highlight() {
        field.highlight { _, cell ->
            cell % 2 == 0
        }
    }

}