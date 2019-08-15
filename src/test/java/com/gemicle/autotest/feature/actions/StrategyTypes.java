package com.gemicle.autotest.feature.actions;

import java.sql.SQLException;

public enum StrategyTypes implements StrategyCalculation {
    AVERAGE("average") {
        @Override
        public int calculate(String host, int time) throws SQLException {
            DBActions db = new DBActions();
            return db.getAverageAndUpdateData(host, time);
        }
    };

    public final String strategyName;

    StrategyTypes(String calculationName) {
        this.strategyName = calculationName;
    }
}
