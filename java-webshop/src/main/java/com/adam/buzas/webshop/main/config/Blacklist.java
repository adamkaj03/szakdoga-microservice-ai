package com.adam.buzas.webshop.main.config;

import java.util.ArrayList;
import java.util.List;

/**
 * az osztály felelőssége a lejárt vagy kijelenkezés miatt invaliddá vált tokenek tárolása
 */
public class Blacklist {
    private static List<String> invalidTokens = new ArrayList<>();

    /**
     * itt kerül be a listába az invaliddá vált token
     * @param token invalid token
     */
    public static void addTokenToBlacklist(String token){
        invalidTokens.add(token);
    }

    /**
     * itt történik az ellenörzés, hogy az adott token, az fekete listán van-e
     * @param token ellenőrzendő token
     * @return igaz, ha nincs a listán hamis, ha rajta van
     */
    public static boolean isBlacklistContainToken(String token){
        return invalidTokens.contains(token);
    }
}
