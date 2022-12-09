package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.bukkit.Material;

public class MaterialsParser {
    public MaterialsParser() {
    }

    public static Material[] toMaterial(String[] stringList) {
        List<Material> materials = new ArrayList();
        String[] arr$ = stringList;
        int len$ = stringList.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String blockString = arr$[i$];
            Pattern whitespace = Pattern.compile("\\s");
            if (whitespace.matcher(blockString).find()) {
                blockString = whitespace.matcher(blockString).replaceAll("");
            }

            Pattern onlyNumbers = Pattern.compile("[^0-9]");
            Material material = Material.matchMaterial(blockString);
            if (material == null) {
                String tempId = onlyNumbers.matcher(blockString).replaceAll("");
                if (!tempId.isEmpty()) {
                    material = Material.getMaterial(tempId);
                }

                if (material == null) {
                    Pattern onlyLetters = Pattern.compile("[^a-zA-Z_]");
                    material = Material.matchMaterial(onlyLetters.matcher(blockString).replaceAll(""));
                }
            }

            if (material != null) {
                materials.add(material);
            }
        }

        return (Material[])materials.toArray(new Material[materials.size()]);
    }

    public static String[] toStringArray(Material[] materials) {
        List<String> items = new ArrayList();
        Material[] arr$ = materials;
        int len$ = materials.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Material mat = arr$[i$];
            items.add(mat.name());
        }

        return (String[])items.toArray(new String[items.size()]);
    }
}
