package com.bitwiserain.pbbg.domain.model

/**
 * Representation of player's inventory.
 */
data class Inventory(
    val items: Map<Int, Item>,
    val equipment: Equipment
)
