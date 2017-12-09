/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.WaterHouse;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;

@RegisterPlugin
public class WaterHouseRasterizer implements WorldRasterizerPlugin {
    Block water;

    @Override
    public void initialize() {
        water = CoreRegistry.get(BlockManager.class).getBlock("Core:Water");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        WaterHouseFacet houseFacet = chunkRegion.getFacet(WaterHouseFacet.class);
        for (Vector3i block : houseFacet.getWorldRegion()) if(houseFacet.getWorld(block)) {
            Vector3i centerHousePosition = new Vector3i();
            int extent = 10;
            centerHousePosition.add(0, extent, 0);
            Region3i walls = Region3i.createFromCenterExtents(centerHousePosition, extent);
            Region3i inside = Region3i.createFromCenterExtents(centerHousePosition, extent - 1);
            for (Vector3i newBlockPosition : walls) {
                if (chunkRegion.getRegion().encompasses(newBlockPosition)
                        && !inside.encompasses(newBlockPosition)) {
                    chunk.setBlock(ChunkMath.calcBlockPos(newBlockPosition), water);
                }
            }
        }
    }
}
