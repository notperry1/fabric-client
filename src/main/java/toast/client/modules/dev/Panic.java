package toast.client.modules.dev;

import toast.client.ToastClient;
import toast.client.modules.Module;
import toast.client.modules.ModuleManager;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Panic extends Module {
    public Panic() {
        super("Panic", Category.DEV, GLFW.GLFW_KEY_P);
    }

    private static boolean isPanicking = false;

    private List<Module> wasEnabled = new ArrayList<>();

    public void onEnable() {
        if(mc.currentScreen != null) return;
        isPanicking = true;
        for (Module module : ModuleManager.modules) {
            if(module.isEnabled() && !module.getClass().equals(this.getClass())) {
                module.setToggled(false);
                wasEnabled.add(module);
            }
        }
        if(mc.inGameHud != null) {
            List<String> msgs = mc.inGameHud.getChatHud().getMessageHistory();//doesnt work no idea why doesnt delete shit
            List<Integer> toDelete = new ArrayList<>();
            for (int i = msgs.size() - 1; i >= 0; i--) {
                if (msgs.get(i).contains(ToastClient.cleanPrefix)) {
                    toDelete.add(i);
                }
            }
            for (Integer msgid : toDelete) {
                mc.inGameHud.getChatHud().removeMessage(msgid);
            }
            mc.updateWindowTitle();
        }
    }

    public void onDisable() {
        isPanicking = false;
        for (Module module : ModuleManager.modules) {
            if(wasEnabled.contains(module)) {
                module.setToggled(true);
                wasEnabled.remove(module);
            }
        }
        if (mc.currentScreen != null) {
            mc.updateWindowTitle();
        }
    }

    public static boolean IsPanicking() {
        return isPanicking;
    }
}
