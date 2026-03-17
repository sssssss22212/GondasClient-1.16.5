package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.gondas.client.util.Render3D;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobESP extends Module {
   private Setting.Boolean monsters = new Setting.Boolean("Monsters", true);
   private Setting.Boolean animals = new Setting.Boolean("Animals", true);
   private Setting.Double range = new Setting.Double("Range", 50.0D, 10.0D, 100.0D);

   public MobESP() {
      super("MobESP", "Highlight mobs", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.monsters, this.animals, this.range});
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         Iterator var2 = mc.field_71441_e.func_217416_b().iterator();

         while(true) {
            Entity e;
            float r;
            float g;
            float b;
            while(true) {
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           return;
                        }

                        e = (Entity)var2.next();
                     } while(!(e instanceof LivingEntity));
                  } while(e == mc.field_71439_g);
               } while((double)mc.field_71439_g.func_70032_d(e) > this.range.getValue());

               r = 1.0F;
               g = 0.0F;
               b = 0.0F;
               if (e instanceof MonsterEntity && this.monsters.getValue()) {
                  r = 1.0F;
                  g = 0.0F;
                  b = 0.0F;
                  break;
               }

               if (e instanceof AnimalEntity && this.animals.getValue()) {
                  r = 0.0F;
                  g = 1.0F;
                  b = 0.0F;
                  break;
               }
            }

            Vector3d pos = new Vector3d(e.func_226277_ct_(), e.func_226278_cu_(), e.func_226281_cx_());
            Render3D.drawBox(pos, (double)e.func_213311_cf(), (double)e.func_213302_cg(), r, g, b, 0.5F);
         }
      }
   }
}
