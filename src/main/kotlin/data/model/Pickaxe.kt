package data.model

enum class Pickaxe(val type: String, val tiles: Set<Pair<Int, Int>>) {
    PLUS("Plus-shaped", setOf(
        0 to -1,
        -1 to 0,
        0 to 0,
        1 to 0,
        0 to 1
    )),
    CROSS("Cross-shaped", setOf(
        -1 to -1,
        1 to -1,
        0 to 0,
        -1 to 1,
        1 to 1
    )),
    RECTANGLE("Open rectangle", setOf(
        -1 to -1,
        0 to -1,
        1 to -1,
        -1 to 0,
        1 to 0,
        -1 to 1,
        0 to 1,
        1 to 1
    ))
}