package com.gemicle.autotest.feature.actions;

import java.sql.SQLException;

public interface StrategyCalculation {

    int calculate(String host, int time) throws SQLException;
}