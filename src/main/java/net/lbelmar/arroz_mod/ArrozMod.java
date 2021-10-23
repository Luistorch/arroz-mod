// -----------------------------------------------------------
// Author: Luis Belloch
// DateOfCreation: 21/10/2021
// Description: Main class of the mod
// -----------------------------------------------------------
package net.lbelmar.arroz_mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.lbelmar.arroz_mod.Blocks.AjoPlantBlock;
import net.lbelmar.arroz_mod.Blocks.ArrozCropBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.Material;
import net.minecraft.block.PlantBlock;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeIds;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFlowerFeature;
import net.minecraft.world.gen.feature.FlowerFeature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.OceanRuinFeature.BiomeType;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.GenerationStep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrozMod implements ModInitializer {
	public static final String MOD_ID = "arroz_mod"; 
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	
	// BLOCK
	public static final CropBlock ARROZ_CROP_BLOCK = new ArrozCropBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final CropBlock GARROFON_CROP_BLOCK = new ArrozCropBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final PlantBlock AJO_PLANT_BLOCK = new AjoPlantBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().noCollision().breakInstantly().sounds(BlockSoundGroup.CROP));

	// BLOCK ITEMS
	public static final Item ARROZ_GRANO = new AliasedBlockItem(ArrozMod.ARROZ_CROP_BLOCK, new Item.Settings().group(ItemGroup.MISC));
	public static final Item GARROFON_GRANO = new AliasedBlockItem(ArrozMod.GARROFON_CROP_BLOCK, new Item.Settings().group(ItemGroup.MISC));

	// ITEMS
	public static final Item ACEITE = new Item(new Item.Settings().group(ItemGroup.MISC));
	public static final Item AJO_CABEZA = new Item(new Item.Settings().group(ItemGroup.MISC));
	public static final Item AJO_DIENTE = new Item(new Item.Settings().group(ItemGroup.MISC));
	
	// FEATURES 
	public static final FlowerFeature<RandomPatchFeatureConfig> AJO_FEATURE = new DefaultFlowerFeature(RandomPatchFeatureConfig.CODEC);
	//BlockStateProvider stateProvider, BlockPlacer blockPlacer, Set<Block> whitelist, Set<BlockState> blacklist, int tries, int spreadX, int spreadY, int spreadZ, boolean canReplace, boolean project, boolean needsWater
	public static final ConfiguredFeature<?, ?> AJO_CONFIGURED_FEATURE = AJO_FEATURE.configure(new RandomPatchFeatureConfig(AJO_PLANT_BLOCK, BlockPlacer.TYPE_CODEC.));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
		
		// ITEMS
		LOGGER.info("Registering items.");
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "arroz_grano"), ARROZ_GRANO);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "garrofon_grano"), GARROFON_GRANO);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "aceite"), ACEITE);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ajo_cabeza"), AJO_CABEZA);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "ajo_diente"), AJO_DIENTE);

		// BLOCK
		LOGGER.info("Registering blocks.");
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID,"arroz_crop_block"), ARROZ_CROP_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID,"garrofon_crop_block"), GARROFON_CROP_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier(MOD_ID,"ajo_plant_block"), AJO_PLANT_BLOCK);

		// FEATURES
		LOGGER.info("Registering features.");
		Registry.register(Registry.FEATURE, new Identifier(MOD_ID, "ajo_patch_feature"), AJO_FEATURE);
		
		// FEATURE CONFIGURATION
		RegistryKey<ConfiguredFeature<?, ?>> ajoFeature = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY,
        new Identifier("arroz_mod", "ajo_patch_feature"));
    	Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, ajoFeature.getValue(), AJO_CONFIGURED_FEATURE);

		// BIOME FEATURES
		BiomeModifications.addFeature(BiomeSelectors.categories(Biome.Category.FOREST), GenerationStep.Feature.VEGETAL_DECORATION, ajoFeature);
		
	}
}