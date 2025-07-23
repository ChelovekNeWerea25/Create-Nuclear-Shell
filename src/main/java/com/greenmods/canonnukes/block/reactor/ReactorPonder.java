package com.greenmods.canonnukes.block.reactor;

import com.greenmods.canonnukes.block.ModBlock;
import com.greenmods.canonnukes.item.ModItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class ReactorPonder {

    public static void ponder(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.configureBasePlate(0, 0, 7);
        scene.title("reactor", "Generating Electricity the Nuclear Way");
        scene.showBasePlate();
        scene.idle(20);

        // --- Шаг 1: Каркас снизу ---
        scene.world().showSection(util.select().fromTo(2, 1, 2, 4, 1, 4), Direction.UP);
        scene.overlay().showText(60)
                .text("Build a steel frame for support")
                .placeNearTarget()
                .pointAt(new Vec3(3.5, 1.5, 3.5));
        scene.idle(70);
        scene.addKeyframe();

        // --- Шаг 2: Внутренности ядра ---
        scene.world().showSection(util.select().fromTo(2, 2, 2, 4, 2, 4), Direction.UP);
        scene.overlay().showText(70)
                .text("Insert internal blocks for the reactor core")
                .placeNearTarget()
                .pointAt(new Vec3(3.5, 2.5, 3.5));
        scene.idle(80);
        scene.addKeyframe();

        // --- Шаг 3: Специальные блоки ---
        showBlockHint(scene, util,
                "Controller: manages the whole structure",
                PonderPalette.GREEN, new Vec3(3.5, 2.5, 2.5), 3, 2, 2);

        scene.rotateCameraY(90);
        scene.idle(10);

        showBlockHint(scene, util,
                "Input port: insert uranium here",
                PonderPalette.BLUE, new Vec3(4.5, 2.5, 3.5), 4, 2, 3);
        scene.rotateCameraY(90);
        scene.idle(10);

        showBlockHint(scene, util,
                "Rotation output port",
                PonderPalette.BLUE, new Vec3(3.5, 2.5, 4.5), 3, 2, 4);
        scene.rotateCameraY(90);
        scene.idle(10);

        showBlockHint(scene, util,
                "Output port: exports processed fuel",
                PonderPalette.RED, new Vec3(2.5, 2.5, 3.5), 2, 2, 3);
        scene.rotateCameraY(90);
        scene.idle(10);

        // --- Шаг 4: Верхняя крышка ---
        scene.world().showSection(util.select().fromTo(2, 3, 2, 4, 3, 4), Direction.UP);
        scene.overlay().showText(70)
                .text("Seal the structure with a top layer")
                .placeNearTarget()
                .pointAt(new Vec3(3.5, 3.5, 3.5));
        scene.idle(80);
        scene.addKeyframe();

        // --- Шаг 5: Завершение сборки ---
        scene.overlay().showText(90)
                .text("Reactor is ready! Connect Create shafts.")
                .placeNearTarget()
                .pointAt(new Vec3(3.5, 2.5, 3.5));
        scene.idle(60);

        // --- Симуляция активации контроллера ---
        scene.overlay().showOutline(PonderPalette.GREEN, null, util.select().position(3, 2, 2), 60);
        scene.overlay().showControls(new Vec3(3.5, 2.5, 2.5), Pointing.UP, 40)
                .rightClick()
                .withItem(ModItems.NUKE_ICON.asStack());
        scene.idle(20);

        scene.world().modifyBlock(util.grid().at(3, 2, 2), state ->
                state.setValue(ReactorBlock.ASSEMBLED, true), false);
        scene.idle(20);

        scene.overlay().showText(70)
                .text("Structure successfully assembled!")
                .placeNearTarget()
                .pointAt(new Vec3(3.5, 2.5, 2.5));
        scene.idle(60);

        scene.markAsFinished();
    }

    // Вспомогательная функция для подсветки и текста
    private static void showBlockHint(CreateSceneBuilder scene, SceneBuildingUtil util, String text, PonderPalette color, Vec3 point, int x, int y, int z) {
        scene.overlay().showText(60)
                .text(text)
                .placeNearTarget()
                .pointAt(point);
        scene.overlay().showOutline(color, null, util.select().position(x, y, z), 60);
        scene.idle(60);
        scene.addKeyframe();
    }
}
