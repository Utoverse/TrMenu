package cc.trixey.mc.trmenu.test

import cc.trixey.mc.trmenu.legacy.invero.InveroManager
import cc.trixey.mc.trmenu.legacy.invero.nms.WindowProperty
import cc.trixey.mc.trmenu.legacy.invero.type.InvChest
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.command
import taboolib.common.platform.function.submitAsync

/**
 * @author Arasple
 * @since 2022/10/22
 *
 * TODO # Invero 开发目标：
 * - [√] 基本的容器特征，标题改变，物品应用，交互检测和事件
 * - [ ] 妥善的玩家背包处理方式 ***
 * - [ ] 输入与输出的支持
 * - [ ] 特殊容器的特征支持 *可缓
 *
 */
private val testPool = InveroManager.registerPool("Invero")
private val testInvero = testPool.createInvero(WindowProperty.GENERIC_9X6) as InvChest

//@Awake(value = LifeCycle.ACTIVE)
fun registerTestingCommand() {
    command("inveorLegacy") {
        literal("list") {
            execute<ProxyCommandSender> { sender, _, _ ->
                sender.sendMessage("Registeredpool Lists:")
                testPool.forInveros {
                    sender.sendMessage("$it: ${it.property} / @${it.pool.index} -- ${it.view.viewer}")
                }
            }
        }
        literal("testInsertable") {
            execute<Player> { player, _, _ ->
                val chest = testPool.createInvero(WindowProperty.GENERIC_9X6, "Insertable Testing", player.uniqueId) {

                    onPostOpen {
                        if (player.isOp) {
                            player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 1f)
                        } else {
                            it.isCancelled = true
                        }
                    }

                    onOpen {
                        val playerContents = it.view.playerContents.contents
                        it.setPlayerContents(playerContents)
                    }

                    onInteract {
                        if (it.slot in it.property.slotsHotbar) {
                            it.isCancelled = true
                            it.player.sendMessage("Cancelled hotbar click")
                        }

                        it.player.sendMessage(
                            """
                                    ————————————————————————————————————————
                                    
                                    Interact: ${it.type}
                                    Slot: ${it.slot}
                                    
                                    ItemCursor: ${it.carriedItem}
                                    ItemSlot: ${it.slotItem}
                                    ItemBukkitCursor: ${player.itemOnCursor}
                                    
                                    Action: ${it.action}
                                """.trimIndent()
                        )

                        println(
                            """
                                    -------------------------------------
                                    Contents: ${contents.size}
                                    ${contents.filterNotNull()}
                                """.trimIndent()
                        )
                    }

                    onClose {
                        it.release()
                        it.player.updateInventory()
                        it.player.sendMessage("Closed")
                    }

                } as InvChest

                chest.open()
            }
        }
        literal("testBasic") {
            execute { player, _, _ ->
                testInvero.open()

                // Item host test
                repeat(50) {
                    submitAsync(delay = (20 + it * 1).toLong()) {
                        val itemStack = ItemStack(Material.values().random(), (1..64).random())
                        testInvero.setItem(it, itemStack)
                    }
                }
                // Title change test
                var current = ""
                val titles = mutableListOf<String>()
                var index = 0

                "Invero Animated Title".windowed(1, 1).forEachIndexed { _, s ->
                    current += s
                    titles.add(current)
                }
                titles.addAll(titles.reversed())

                submitAsync(period = 1) {
                    if (InveroManager.findViewingInvero(player) == null) {
                        testInvero.clear()
                        testInvero.title = ""
                        index = 0
                        cancel()
                    }
                    testInvero.title = titles[index++]
                    if (index == titles.size - 1) index = 0
                }
            }
        }
    }
}