package com.jakewharton.u2020;

final class Modules {

    static Object[] list(U2020App app) {
        return new Object[] { new U2020Module(app), new InternalReleaseU2020Module() };
    }

    private Modules() {
        // No instances.
    }
}
