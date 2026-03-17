package com.gondas.client.module.impl;

import com.gondas.client.module.Module;
import com.gondas.client.setting.Setting;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemESP extends Module {
   private Setting.Double range = new Setting.Double("Range", 50.0D, 10.0D, 100.0D);
   private Setting.Boolean names = new Setting.Boolean("Names", true);
   private Setting.Boolean russianNames = new Setting.Boolean("RussianNames", true);
   private Setting.Boolean showCount = new Setting.Boolean("ShowCount", true);
   private Setting.Double lineWidth = new Setting.Double("LineWidth", 2.0D, 1.0D, 5.0D);
   private Setting.Boolean filled = new Setting.Boolean("Filled", true);
   private Setting.Boolean showIcon = new Setting.Boolean("ShowIcon", true);
   private Setting.Double nameScale = new Setting.Double("NameScale", 1.0D, 0.5D, 2.0D);

   public ItemESP() {
      super("ItemESP", "Highlight dropped items with names", Module.Category.RENDER);
      this.addSettings(new Setting[]{this.range, this.names, this.russianNames, this.showCount, this.lineWidth, this.filled, this.showIcon, this.nameScale});
   }

   @SubscribeEvent
   public void onRender(RenderWorldLastEvent event) {
      if (mc.field_71441_e != null && mc.field_71439_g != null) {
         MatrixStack matrix = event.getMatrixStack();
         Vector3d camPos = mc.field_71460_t.func_215316_n().func_216785_c();
         RenderSystem.enableBlend();
         RenderSystem.disableDepthTest();
         RenderSystem.disableLighting();
         RenderSystem.disableTexture();
         RenderSystem.lineWidth((float)this.lineWidth.getValue());
         Iterator var4 = mc.field_71441_e.func_217416_b().iterator();

         while(true) {
            Entity e;
            double dist;
            do {
               do {
                  if (!var4.hasNext()) {
                     if (this.names.getValue()) {
                        this.renderNames(matrix, camPos);
                     }

                     RenderSystem.enableDepthTest();
                     RenderSystem.enableTexture();
                     RenderSystem.disableBlend();
                     return;
                  }

                  e = (Entity)var4.next();
               } while(!(e instanceof ItemEntity));

               dist = (double)mc.field_71439_g.func_70032_d(e);
            } while(dist > this.range.getValue());

            ItemEntity item = (ItemEntity)e;
            ItemStack stack = item.func_92059_d();
            float r = 1.0F;
            float g = 1.0F;
            float b = 0.0F;
            String itemName = stack.func_200301_q().getString().toLowerCase();
            if (!itemName.contains("diamond") && !itemName.contains("алмаз")) {
               if (!itemName.contains("gold") && !itemName.contains("золот")) {
                  if (!itemName.contains("emerald") && !itemName.contains("изумруд")) {
                     if (!itemName.contains("netherite") && !itemName.contains("незерит")) {
                        if (!itemName.contains("iron") && !itemName.contains("желез")) {
                           if (!stack.func_77948_v() && !itemName.contains("enchanted")) {
                              if (itemName.contains("rare") || itemName.contains("редк")) {
                                 r = 1.0F;
                                 g = 0.5F;
                                 b = 0.0F;
                              }
                           } else {
                              r = 0.8F;
                              g = 0.0F;
                              b = 1.0F;
                           }
                        } else {
                           r = 0.9F;
                           g = 0.9F;
                           b = 0.9F;
                        }
                     } else {
                        r = 0.5F;
                        g = 0.3F;
                        b = 0.3F;
                     }
                  } else {
                     r = 0.0F;
                     g = 1.0F;
                     b = 0.4F;
                  }
               } else {
                  r = 1.0F;
                  g = 0.85F;
                  b = 0.0F;
               }
            } else {
               r = 0.0F;
               g = 0.8F;
               b = 1.0F;
            }

            double x = e.func_226277_ct_() - camPos.field_72450_a;
            double y = e.func_226278_cu_() - camPos.field_72448_b;
            double z = e.func_226281_cx_() - camPos.field_72449_c;
            matrix.func_227860_a_();
            Matrix4f matrix4f = matrix.func_227866_c_().func_227870_a_();
            Tessellator tessellator = Tessellator.func_178181_a();
            BufferBuilder buffer = tessellator.func_178180_c();
            if (this.filled.getValue()) {
               buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
               buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 0.3F).func_181675_d();
               tessellator.func_78381_a();
            }

            buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z - 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x + 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            buffer.func_227888_a_(matrix4f, (float)x - 0.25F, (float)y + 0.5F, (float)z + 0.25F).func_227885_a_(r, g, b, 1.0F).func_181675_d();
            tessellator.func_78381_a();
            matrix.func_227865_b_();
         }
      }
   }

   private void renderNames(MatrixStack matrix, Vector3d camPos) {
      Iterator var3 = mc.field_71441_e.func_217416_b().iterator();

      while(var3.hasNext()) {
         Entity e = (Entity)var3.next();
         if (e instanceof ItemEntity) {
            double dist = (double)mc.field_71439_g.func_70032_d(e);
            if (!(dist > this.range.getValue())) {
               ItemEntity item = (ItemEntity)e;
               ItemStack stack = item.func_92059_d();
               String name = stack.func_200301_q().getString();
               if (this.russianNames.getValue()) {
                  name = this.translateToRussian(name, stack);
               }

               if (this.showCount.getValue() && stack.func_190916_E() > 1) {
                  name = name + " x" + stack.func_190916_E();
               }

               double x = e.func_226277_ct_() - camPos.field_72450_a;
               double y = e.func_226278_cu_() - camPos.field_72448_b + 0.7D;
               double z = e.func_226281_cx_() - camPos.field_72449_c;
               this.renderBillboardText(matrix, name, x, y, z, (float)this.nameScale.getValue());
            }
         }
      }

   }

   private void renderBillboardText(MatrixStack matrix, String text, double x, double y, double z, float scale) {
      matrix.func_227860_a_();
      matrix.func_227861_a_(x, y, z);
      matrix.func_227863_a_(mc.field_71460_t.func_215316_n().func_227995_f_());
      matrix.func_227862_a_(-scale * 0.025F, -scale * 0.025F, scale * 0.025F);
      RenderSystem.enableTexture();
      int width = mc.field_71466_p.func_78256_a(text);
      mc.field_71466_p.func_238421_b_(matrix, text, (float)(-width) / 2.0F, 0.0F, -1);
      matrix.func_227865_b_();
   }

   private String translateToRussian(String name, ItemStack stack) {
      String lower = name.toLowerCase();
      if (lower.contains("diamond")) {
         name = name.replace("Diamond", "Алмаз").replace("diamond", "алмаз");
      }

      if (lower.contains("gold")) {
         name = name.replace("Gold", "Золото").replace("gold", "золото");
      }

      if (lower.contains("iron")) {
         name = name.replace("Iron", "Железо").replace("iron", "железо");
      }

      if (lower.contains("emerald")) {
         name = name.replace("Emerald", "Изумруд").replace("emerald", "изумруд");
      }

      if (lower.contains("netherite")) {
         name = name.replace("Netherite", "Незерит").replace("netherite", "незерит");
      }

      if (lower.contains("stone")) {
         name = name.replace("Stone", "Камень").replace("stone", "камень");
      }

      if (lower.contains("wood")) {
         name = name.replace("Wood", "Дерево").replace("wood", "дерево");
      }

      if (lower.contains("sword")) {
         name = name.replace("Sword", "Меч").replace("sword", "меч");
      }

      if (lower.contains("pickaxe")) {
         name = name.replace("Pickaxe", "Кирка").replace("pickaxe", "кирка");
      }

      if (lower.contains("axe")) {
         name = name.replace("Axe", "Топор").replace("axe", "топор");
      }

      if (lower.contains("shovel")) {
         name = name.replace("Shovel", "Лопата").replace("shovel", "лопата");
      }

      if (lower.contains("hoe")) {
         name = name.replace("Hoe", "Мотыга").replace("hoe", "мотыга");
      }

      if (lower.contains("helmet")) {
         name = name.replace("Helmet", "Шлем").replace("helmet", "шлем");
      }

      if (lower.contains("chestplate")) {
         name = name.replace("Chestplate", "Нагрудник").replace("chestplate", "нагрудник");
      }

      if (lower.contains("leggings")) {
         name = name.replace("Leggings", "Поножи").replace("leggings", "поножи");
      }

      if (lower.contains("boots")) {
         name = name.replace("Boots", "Ботинки").replace("boots", "ботинки");
      }

      return name;
   }
}
