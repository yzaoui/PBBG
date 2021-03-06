package com.bitwiserain.pbbg.db.usecase

import com.bitwiserain.pbbg.db.repository.EquipmentTable
import com.bitwiserain.pbbg.db.repository.InventoryTable
import com.bitwiserain.pbbg.domain.model.Equippable
import com.bitwiserain.pbbg.domain.model.Item
import com.bitwiserain.pbbg.domain.model.mine.Pickaxe
import com.bitwiserain.pbbg.domain.usecase.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class EquipmentUCImpl(private val db: Database) : EquipmentUC {
    override fun equip(userId: Int, inventoryItemId: Int): Unit = transaction(db) {
        /* Get item from inventory table */
        val item = InventoryTable.select { InventoryTable.userId.eq(userId) and InventoryTable.id.eq(inventoryItemId) }
            .singleOrNull()
            ?.toItem()

        /* Make sure item exists */
        if (item == null) throw InventoryItemNotFoundException(inventoryItemId)
        /* Make sure item is equippable */
        if (item !is Equippable) throw InventoryItemNotEquippable(inventoryItemId)
        /* Make sure item is not already equipped */
        if (item.equipped) throw InventoryItemAlreadyEquipped(inventoryItemId)

        /* Get all equipped items */
        val equippedItems = InventoryTable.select { InventoryTable.userId.eq(userId) and InventoryTable.equipped.eq(true) }
            .associate { it[InventoryTable.id].value to it.toItem() as Equippable }

        /* Unequip item currently in this equipment slot if any */
        for (equippedItem in equippedItems) {
            if (item is Item.Pickaxe && equippedItem.value is Item.Pickaxe) {
                InventoryTable.update({ (InventoryTable.userId eq userId) and (InventoryTable.id eq equippedItem.key) }) {
                    it[InventoryTable.equipped] = false
                }

                break
            }
        }

        /* Add item to equipment table */
        EquipmentTable.update({ EquipmentTable.userId.eq(userId) }) {
            if (item is Item.Pickaxe) it[EquipmentTable.pickaxe] = Pickaxe.fromItem(item)
        }

        /* Mark item as equipped in inventory table */
        InventoryTable.update({ (InventoryTable.userId eq userId) and (InventoryTable.id eq inventoryItemId) }) {
            it[InventoryTable.equipped] = true
        }
    }

    override fun unequip(userId: Int, inventoryItemId: Int): Unit = transaction(db) {
        /* Get item from inventory table */
        val item = InventoryTable.select { InventoryTable.userId.eq(userId) and InventoryTable.id.eq(inventoryItemId) }
            .singleOrNull()
            ?.toItem()

        /* Make sure item exists */
        if (item == null) throw InventoryItemNotFoundException(inventoryItemId)
        /* Make sure item is equippable */
        if (item !is Equippable) throw InventoryItemNotEquippable(inventoryItemId)
        /* Make sure item is not already equipped */
        if (!item.equipped) throw InventoryItemAlreadyUnequipped(inventoryItemId)

        /* Remove item from equipment table */
        EquipmentTable.update({ EquipmentTable.userId.eq(userId) }) {
            if (item is Item.Pickaxe) it[EquipmentTable.pickaxe] = null
        }

        /* Mark item as unequipped in inventory table */
        InventoryTable.update({ (InventoryTable.userId eq userId) and (InventoryTable.id eq inventoryItemId) }) {
            it[InventoryTable.equipped] = false
        }
    }

    override fun getEquippedPickaxe(userId: Int): Pickaxe? = transaction(db) {
        EquipmentTable.select { EquipmentTable.userId.eq(userId) }
            .map { it[EquipmentTable.pickaxe] }
            .singleOrNull()
    }
}
