package dev.toastmc.client.module.misc

import dev.toastmc.client.ToastClient.Companion.EVENT_BUS
import dev.toastmc.client.event.CloseScreenInPortalEvent
import dev.toastmc.client.module.Category
import dev.toastmc.client.module.Module
import dev.toastmc.client.module.ModuleManifest
import dev.toastmc.client.util.mc
import me.zero.alpine.listener.EventHandler
import me.zero.alpine.listener.EventHook
import me.zero.alpine.listener.Listener

@ModuleManifest(
        label = "PortalChat",
        description = "Open GUIS in Portals",
        category = Category.MISC
)
class PortalChat : Module() {

    override fun onEnable() {
        EVENT_BUS.subscribe(closeScreenEvent)
    }

    override fun onDisable() {
        EVENT_BUS.unsubscribe(closeScreenEvent)
    }

    @EventHandler
    private val closeScreenEvent =  Listener(EventHook<CloseScreenInPortalEvent> {
        if (mc.player == null) return@EventHook
        it.cancel()
    })
}