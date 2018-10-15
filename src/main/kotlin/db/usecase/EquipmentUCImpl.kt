package com.bitwiserain.pbbg.db.usecase

import com.bitwiserain.pbbg.db.repository.EquipmentTable
import com.bitwiserain.pbbg.db.repository.InventoryTable
import com.bitwiserain.pbbg.db.repository.UserTable
import com.bitwiserain.pbbg.domain.model.Equippable
import com.bitwiserain.pbbg.domain.model.Item
import com.bitwiserain.pbbg.domain.model.Pickaxe
import com.bitwiserain.pbbg.domain.usecase.EquipmentUC
import com.bitwiserain.pbbg.domain.usecase.InventoryItemNotEquippable
import com.bitwiserain.pbbg.domain.usecase.InventoryItemNotFoundException
import com.bitwiserain.pbbg.domain.usecase.InventoryUC
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Random

class EquipmentUCImpl(private val db: Database, private val inventoryUC: InventoryUC) : EquipmentUC {
    override fun equip(userId: Int, inventoryItemId: Int) {
        TODO("not implemented")
    }

    override fun unequip(userId: Int, inventoryItemId: Int): Unit = transaction(db) {
        /* Get item from inventory table */
        val item = InventoryTable.select { InventoryTable.userId.eq(userId) and InventoryTable.id.eq(inventoryItemId) }
            .singleOrNull()
            ?.toItem()

        /* Make sure item exists and is equippable */
        if (item == null) throw InventoryItemNotFoundException(inventoryItemId)
        if (item !is Equippable) throw InventoryItemNotEquippable(inventoryItemId)

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

    override fun generatePickaxe(userId: Int): Item.Pickaxe? = transaction(db) {
        // TODO: Do something if this user already has a pickaxe
        val pickaxeEnum = Pickaxe.values()[Random().nextInt(Pickaxe.values().size)]
        val pickaxeItem = pickaxeEnum.toItem(equipped = true)

        inventoryUC.storeInInventory(userId, pickaxeItem)

        EquipmentTable.insert {
            it[EquipmentTable.userId] = EntityID(userId, UserTable)
            it[EquipmentTable.pickaxe] = pickaxeEnum
        }

        pickaxeItem
    }
}
