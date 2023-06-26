package dev.dhyces.sleepycampfires.modhelper.services;

import dev.dhyces.sleepycampfires.modhelper.services.helpers.PlatformHelper;

import java.util.ServiceLoader;

public final class Services {

    public static final PlatformHelper PLATFORM_HELPER = loadService(PlatformHelper.class);

    static <T> T loadService(Class<T> t) {
        return ServiceLoader.load(t).findFirst().get();
    }
}
