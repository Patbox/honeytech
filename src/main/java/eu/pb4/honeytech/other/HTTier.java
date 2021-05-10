package eu.pb4.honeytech.other;

public enum HTTier {
    BASIC(0,0,0, 0.5),
    COPPER(1, 2048, 0.8, 1),
    IRON(2, 4096, 1.3, 2.5),
    GOLD(3, 8192, 1.6, 5);

    public final int level;
    public final double energyCapacity;
    public final double energyMultiplier;
    public final double speed;
    HTTier(int lvl, double energyCapacity, double energyMultiplier, double speed) {
        this.level = lvl;
        this.energyCapacity = energyCapacity;
        this.energyMultiplier = energyMultiplier;
        this.speed = speed;
    }
}
