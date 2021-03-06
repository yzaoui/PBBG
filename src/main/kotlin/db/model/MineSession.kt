package com.bitwiserain.pbbg.db.model

/**
 * Holds the metadata of an open mine session.
 *
 * @property id Unique identifier of this mine session.
 * @property width The width of this mine session.
 * @property height The height of this mine session.
 */
data class MineSession(
    val id: Int,
    val width: Int,
    val height: Int
)
