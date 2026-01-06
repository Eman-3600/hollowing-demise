package net.eman3600.hdemise.mixin_interfaces;

public interface PlayerEntityAccess {
    int hdemise$getTicksSinceLastAttack();

    static boolean isInMetronomeWindow(int ticksSinceLastAttack, float attackCooldown, boolean lenient) {
        float window = (ticksSinceLastAttack - attackCooldown);

        return window >= (-.5f - (lenient ? 1 : 0)) && window < 3.5f;
    }
}
