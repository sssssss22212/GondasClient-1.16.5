package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class Spammer extends Module {
   private Setting.Mode message = new Setting.Mode("Message", new String[]{"Hello!", "GG", "Gondas Client on top!", "Custom"});
   private Setting.Int delay = new Setting.Int("Delay", 100, 20, 500);
   private Setting.Str customMsg = new Setting.Str("Custom", "Your message here");
   private int timer = 0;

   public Spammer() {
      super("Spammer", "Auto send messages", Module.Category.MISC);
      this.addSettings(new Setting[]{this.message, this.delay, this.customMsg});
   }

   public void onTick() {
      if (mc.field_71439_g != null) {
         ++this.timer;
         if (this.timer >= this.delay.getValue()) {
            this.timer = 0;
            String msg = this.message.getValue();
            if (msg.equals("Custom")) {
               msg = this.customMsg.getValue();
            }

            mc.field_71439_g.func_71165_d(msg);
         }

      }
   }
}
