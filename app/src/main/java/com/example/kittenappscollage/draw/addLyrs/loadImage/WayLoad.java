package com.example.kittenappscollage.draw.addLyrs.loadImage;

public class WayLoad {

    private StrategyLoadImage wStrategy;

    public WayLoad setStrategy(StrategyLoadImage strategy){
        wStrategy = strategy;
        return this;
    }

    public WayLoad setWayProject(Object way){
        wStrategy.way(way);
        return this;
    }
}
