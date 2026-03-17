package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;

public class AutoWalk extends Module {
   private Setting.Mode direction = new Setting.Mode("Direction", new String[]{"Forward", "Back", "Left", "Right"});

   public AutoWalk() {
      super("AutoWalk", "Auto walk", Module.Category.MISC);
      this.addSettings(new Setting[]{this.direction});
   }

   public void onEnable() {
      this.updateKeyBind();
   }

   public void onDisable() {
      mc.field_71474_y.field_74351_w.func_225593_a_(false);
      mc.field_71474_y.field_74368_y.func_225593_a_(false);
      mc.field_71474_y.field_74370_x.func_225593_a_(false);
      mc.field_71474_y.field_74366_z.func_225593_a_(false);
   }

   public void onTick() {
      this.updateKeyBind();
   }

   private void updateKeyBind() {
      String dir = this.direction.getValue();
      mc.field_71474_y.field_74351_w.func_225593_a_(dir.equals("Forward"));
      mc.field_71474_y.field_74368_y.func_225593_a_(dir.equals("Back"));
      mc.field_71474_y.field_74370_x.func_225593_a_(dir.equals("Left"));
      mc.field_71474_y.field_74366_z.func_225593_a_(dir.equals("Right"));
   }
}
