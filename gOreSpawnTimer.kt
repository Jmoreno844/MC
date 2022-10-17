
package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.script.api.global.Chat
import net.ccbluex.liquidbounce.utils.extensions.getBlock
import net.ccbluex.liquidbounce.utils.extensions.getVec
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.minecraft.block.Block
import net.minecraft.block.BlockOre
import net.minecraft.block.state.IBlockState
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.network.play.server.S13PacketDestroyEntities
import net.minecraft.network.play.server.S23PacketBlockChange
import net.minecraft.network.play.server.S24PacketBlockAction
import net.minecraft.network.play.server.S25PacketBlockBreakAnim
import net.minecraft.util.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Tick
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.TimeUnit
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import java.awt.Color

@ModuleInfo(name = "gOreSpawnTimer",category = ModuleCategory.RENDER)
class OreSpawnTimer : Module() {
    var contador: Int = 0
    var corrido = 0
    var cooldown = false
    var marcado = mutableListOf<marcador>()
    public var breakers = mutableListOf<Int>()
    var ticks = 0
    var toro = 17
    var thierro = 22
    var tdiamante = 18
    var exists = false

    fun esperar(){
        ticks=0
        cooldown = true
        if(ticks==1){
            cooldown==false
        }
    }
    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        ticks++
        corrido = 0


        if (ticks % 20 == 1) {

            for (x in marcado){

                x.tiempo--
                if (x.tiempo<=0){
                    corrido++
                }

            }}
        while (corrido > 0){
            marcado.removeAt(0)
            corrido--
        }

    }
    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet

        if (packet is C07PacketPlayerDigging) {

            if(packet.status == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {

                if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:gold_ore}"||
                    mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:iron_ore}"||
                    mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:diamond_ore}"){
                    for(x in marcado){
                        if((x.tiempo==18 && x.posicion == packet.position && x.tick == ticks)){
                            exists = true
                        }
                    }
                    if(exists==false){
                        if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:gold_ore}"){
                            marcado.add(marcador(toro,packet.position,ticks))}
                        else if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:iron_ore}"){
                            marcado.add(marcador(thierro,packet.position,ticks))}
                        else if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:diamond_ore}"){
                            marcado.add(marcador(tdiamante,packet.position,ticks))}
                    }
                    exists=false
                }
            }
        }
        if (packet is S25PacketBlockBreakAnim) {


            if(packet.progress >= 10){

                if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:gold_ore}"||
                    mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:iron_ore}"||
                    mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:diamond_ore}"){
                    for(x in marcado){
                        if((x.tiempo==18 && x.posicion == packet.position && x.tick == ticks)){
                            exists = true
                        }
                    }
                    if(exists==false){
                        if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:gold_ore}"){
                            marcado.add(marcador(toro,packet.position,ticks))}
                        else if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:iron_ore}"){
                            marcado.add(marcador(thierro,packet.position,ticks))}
                        else if(mc.theWorld.getBlockState(packet.position).block.toString() == "Block{minecraft:diamond_ore}"){
                            marcado.add(marcador(tdiamante,packet.position,ticks))}
                    }
                    exists=false



                }
            }
        }
    }

    @EventTarget
    fun onRender3D(event: Render3DEvent) {
        for (block in marcado){
            if (block.tiempo in 6 downTo 5){
                RenderUtils.drawBlockBox(block.posicion,Color.GREEN,true)
            }
            else if (block.tiempo in 4 downTo 3){
                RenderUtils.drawBlockBox(block.posicion,Color.YELLOW,true)
            }
            else if (block.tiempo in 2 downTo 1){
                RenderUtils.drawBlockBox(block.posicion,Color.RED,true)
            }
        }


    }
}

class marcador constructor(ctiempo: Int, cposicion: BlockPos, ctick :Int) {
    var tiempo: Int = ctiempo
    var posicion: BlockPos = cposicion
    var tick: Int = ctick


}
