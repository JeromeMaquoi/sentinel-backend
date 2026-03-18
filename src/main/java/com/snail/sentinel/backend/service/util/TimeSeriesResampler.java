package com.snail.sentinel.backend.service.util;

import java.util.*;

/**
 * Utility class for resampling time series data to a fixed grid.
 * Handles normalization of timestamps and linear interpolation of values.
 */
public final class TimeSeriesResampler {

    private TimeSeriesResampler() {
        // Utility class, no instantiation
    }

    /**
     * Represents a single point in a time series
     */
    public static class TimeSeriesPoint {
        public final long timestamp;
        public final double value;

        public TimeSeriesPoint(long timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }
    }

    /**
     * Resamples multiple time series to a fixed time grid.
     *
     * @param allSeriesData List of time series, each containing timestamps and values
     * @param numberOfGridPoints Number of points in the target fixed grid
     * @return A resampling result containing the fixed timestamps and resampled values for each series
     */
    public static ResamplingResult resampleToFixedGrid(List<TimeSeries> allSeriesData, int numberOfGridPoints) {
        if (allSeriesData == null || allSeriesData.isEmpty()) {
            return new ResamplingResult(new ArrayList<>(), new ArrayList<>());
        }

        // Normalize all series to 0-based timestamps
        List<TimeSeries> normalizedSeries = new ArrayList<>();
        for (TimeSeries series : allSeriesData) {
            normalizedSeries.add(normalizeSeries(series));
        }

        // Find global min and max timestamps across all series
        long globalMinTime = Long.MAX_VALUE;
        long globalMaxTime = Long.MIN_VALUE;

        for (TimeSeries series : normalizedSeries) {
            if (!series.timestamps.isEmpty()) {
                globalMinTime = Math.min(globalMinTime, series.timestamps.get(0));
                globalMaxTime = Math.max(globalMaxTime, series.timestamps.get(series.timestamps.size() - 1));
            }
        }

        if (globalMinTime == Long.MAX_VALUE || globalMaxTime == Long.MIN_VALUE) {
            return new ResamplingResult(new ArrayList<>(), new ArrayList<>());
        }

        // Create fixed time grid
        List<Long> fixedTimestamps = createFixedTimeGrid(globalMinTime, globalMaxTime, numberOfGridPoints);

        // Resample each series to the fixed grid
        List<List<Double>> resampledValues = new ArrayList<>();
        for (TimeSeries series : normalizedSeries) {
            List<Double> resampledSeries = resampleSeriesLinear(series, fixedTimestamps);
            resampledValues.add(resampledSeries);
        }

        return new ResamplingResult(fixedTimestamps, resampledValues);
    }

    /**
     * Normalizes a time series by subtracting the minimum timestamp from all timestamps.
     *
     * @param series The time series to normalize
     * @return A new TimeSeries with normalized (0-based) timestamps
     */
    private static TimeSeries normalizeSeries(TimeSeries series) {
        if (series.timestamps.isEmpty()) {
            return new TimeSeries(new ArrayList<>(), new ArrayList<>());
        }

        long minTimestamp = series.timestamps.stream().mapToLong(Long::longValue).min().orElse(0);
        List<Long> normalizedTimestamps = new ArrayList<>();
        for (long ts : series.timestamps) {
            normalizedTimestamps.add(ts - minTimestamp);
        }

        return new TimeSeries(normalizedTimestamps, new ArrayList<>(series.values));
    }

    /**
     * Creates a fixed time grid from min to max time with the specified number of points.
     *
     * @param minTime Minimum time
     * @param maxTime Maximum time
     * @param numberOfPoints Number of points in the grid
     * @return List of timestamps representing the fixed time grid
     */
    private static List<Long> createFixedTimeGrid(long minTime, long maxTime, int numberOfPoints) {
        List<Long> grid = new ArrayList<>();
        if (numberOfPoints <= 1) {
            grid.add(minTime);
            return grid;
        }

        double step = (double) (maxTime - minTime) / (numberOfPoints - 1);
        for (int i = 0; i < numberOfPoints; i++) {
            long timePoint = Math.round(minTime + i * step);
            grid.add(timePoint);
        }

        return grid;
    }

    /**
     * Resamples a single time series to a fixed time grid using linear interpolation.
     *
     * @param series The input time series
     * @param fixedTimestamps The target fixed time grid
     * @return List of interpolated values at the fixed time points
     */
    private static List<Double> resampleSeriesLinear(TimeSeries series, List<Long> fixedTimestamps) {
        List<Double> resampled = new ArrayList<>();

        for (long targetTime : fixedTimestamps) {
            double interpolatedValue = interpolateValue(series.timestamps, series.values, targetTime);
            resampled.add(interpolatedValue);
        }

        return resampled;
    }

    /**
     * Interpolates a value at a target time using linear interpolation.
     * If the target time is outside the range, it uses the nearest value.
     *
     * @param timestamps List of timestamps
     * @param values List of values corresponding to timestamps
     * @param targetTime The time point where we want to interpolate
     * @return The interpolated value
     */
    private static double interpolateValue(List<Long> timestamps, List<Double> values, long targetTime) {
        if (timestamps.isEmpty()) {
            return 0.0;
        }

        // Find the two nearest points
        int lowerIndex = -1;
        int upperIndex = -1;

        for (int i = 0; i < timestamps.size(); i++) {
            if (timestamps.get(i) <= targetTime) {
                lowerIndex = i;
            }
            if (timestamps.get(i) >= targetTime && upperIndex == -1) {
                upperIndex = i;
            }
        }

        // If exact match
        if (lowerIndex != -1 && timestamps.get(lowerIndex) == targetTime) {
            return values.get(lowerIndex);
        }

        // If target is before the first point, return the first value
        if (lowerIndex == -1) {
            return values.get(0);
        }

        // If target is after the last point, return the last value
        if (upperIndex == -1) {
            return values.get(values.size() - 1);
        }

        // Linear interpolation between lowerIndex and upperIndex
        long t1 = timestamps.get(lowerIndex);
        long t2 = timestamps.get(upperIndex);
        double v1 = values.get(lowerIndex);
        double v2 = values.get(upperIndex);

        if (t1 == t2) {
            return v1;
        }

        // Linear interpolation formula: v = v1 + (v2 - v1) * (t - t1) / (t2 - t1)
        double ratio = (double) (targetTime - t1) / (t2 - t1);
        return v1 + (v2 - v1) * ratio;
    }

    /**
     * Represents a single time series with timestamps and corresponding values
     */
    public static class TimeSeries {
        private final List<Long> timestamps;
        private final List<Double> values;

        public TimeSeries(List<Long> timestamps, List<Double> values) {
            this.timestamps = new ArrayList<>(timestamps);
            this.values = new ArrayList<>(values);
        }

        public List<Long> getTimestamps() {
            return new ArrayList<>(timestamps);
        }

        public List<Double> getValues() {
            return new ArrayList<>(values);
        }
    }

    /**
     * Result of resampling operation
     */
    public static class ResamplingResult {
        private final List<Long> fixedTimestamps;
        private final List<List<Double>> resampledValues;

        public ResamplingResult(List<Long> fixedTimestamps, List<List<Double>> resampledValues) {
            this.fixedTimestamps = new ArrayList<>(fixedTimestamps);
            this.resampledValues = new ArrayList<>();
            for (List<Double> vals : resampledValues) {
                this.resampledValues.add(new ArrayList<>(vals));
            }
        }

        public List<Long> getFixedTimestamps() {
            return new ArrayList<>(fixedTimestamps);
        }

        public List<List<Double>> getResampledValues() {
            List<List<Double>> copy = new ArrayList<>();
            for (List<Double> vals : resampledValues) {
                copy.add(new ArrayList<>(vals));
            }
            return copy;
        }
    }
}



