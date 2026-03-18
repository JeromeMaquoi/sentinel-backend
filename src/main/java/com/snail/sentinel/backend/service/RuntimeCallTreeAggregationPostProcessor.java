package com.snail.sentinel.backend.service;

import com.snail.sentinel.backend.service.dto.aggregation.AggregatedRuntimeCallTreeMeasurementDTO;
import com.snail.sentinel.backend.service.dto.aggregation.IterationAggregateDTO;
import com.snail.sentinel.backend.service.util.TimeSeriesResampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for post-processing aggregated runtime call tree measurements.
 *
 * Handles:
 * - Time series normalization (normalizing timestamps to 0-based relative time)
 * - Resampling (aligning all iterations to a fixed time grid)
 * - Interpolation (estimating values between measurement points)
 * - Aggregation (computing mean values across iterations)
 *
 * Input from MongoDB aggregation:
 * - timestamps: array of all timestamps (one per measurement point)
 * - values: array of values (one per measurement point)
 * - iterations: array of iteration info (one per measurement point)
 *
 * Output:
 * - Fixed time grid (normalized timestamps)
 * - Per-iteration resampled values
 * - Aggregated values (mean across iterations)
 */
@Service
public class RuntimeCallTreeAggregationPostProcessor {
    private static final Logger log = LoggerFactory.getLogger(RuntimeCallTreeAggregationPostProcessor.class);
    private static final int DEFAULT_GRID_POINTS = 100;
    private static final String MEASUREMENT_TYPE = "runtime_calltree";
    private static final int MINIMUM_DATA_POINTS = 2;

    /**
     * Post-processes a batch of aggregated measurements.
     *
     * @param aggregatedResults List of aggregated measurements from MongoDB
     * @param gridPoints Number of points in the target fixed time grid
     * @return Processed measurements with normalized timestamps and resampled values
     * @throws IllegalArgumentException if gridPoints is invalid
     * @throws NullPointerException if aggregatedResults is null
     */
    public List<AggregatedRuntimeCallTreeMeasurementDTO> processAggregations(
            List<AggregatedRuntimeCallTreeMeasurementDTO> aggregatedResults,
            int gridPoints) {

        Objects.requireNonNull(aggregatedResults, "aggregatedResults cannot be null");
        validateGridPoints(gridPoints);

        log.debug("Processing {} aggregated measurements with {} grid points",
                aggregatedResults.size(), gridPoints);

        return aggregatedResults.stream()
                .map(aggregated -> processAggregation(aggregated, gridPoints))
                .toList();
    }

    /**
     * Post-processes a single aggregated measurement.
     *
     * Steps:
     * 1. Validates input data
     * 2. Groups measurement points by iteration
     * 3. Normalizes timestamps within each iteration
     * 4. Resamples all iterations to a fixed time grid
     * 5. Computes aggregated values across iterations
     *
     * @param aggregated The aggregated measurement from MongoDB
     * @param gridPoints Number of points in the target fixed time grid
     * @return Processed measurement with normalized and resampled data
     * @throws IllegalArgumentException if gridPoints is invalid
     * @throws NullPointerException if aggregated is null
     */
    public AggregatedRuntimeCallTreeMeasurementDTO processAggregation(
            AggregatedRuntimeCallTreeMeasurementDTO aggregated,
            int gridPoints) {

        Objects.requireNonNull(aggregated, "aggregated measurement cannot be null");
        validateGridPoints(gridPoints);

        setMetadataFields(aggregated);

        // Validate and extract input data
        if (!isValidAggregationData(aggregated)) {
            log.debug("Skipping processing - insufficient or invalid data for callstack: {}",
                    aggregated.getCallstack());
            return aggregated;
        }

        List<Long> timestamps = aggregated.getTimestamps();
        List<Double> values = aggregated.getValues();
        List<?> rawIterations = aggregated.getIterations();

        // Ensure consistent array lengths
        int dataLength = getConsistentDataLength(timestamps, values, rawIterations);

        if (dataLength < MINIMUM_DATA_POINTS) {
            log.debug("Insufficient data points ({}) for resampling, skipping processing", dataLength);
            return aggregated;
        }

        // Process time series data
        Map<String, List<MeasurementPoint>> pointsByIteration =
                groupMeasurementsByIteration(timestamps, values, rawIterations, dataLength);

        List<TimeSeriesResampler.TimeSeries> timeSeriesList =
                buildTimeSeriesFromGroups(pointsByIteration);

        TimeSeriesResampler.ResamplingResult resamplingResult =
                TimeSeriesResampler.resampleToFixedGrid(timeSeriesList, gridPoints);

        // Populate results
        populateResampledData(aggregated, resamplingResult, pointsByIteration.keySet());

        log.debug("Successfully processed aggregation with {} iterations and {} grid points",
                pointsByIteration.size(), gridPoints);

        return aggregated;
    }

    /**
     * Sets metadata fields on the aggregated measurement.
     *
     * @param aggregated The aggregated measurement to update
     */
    private void setMetadataFields(AggregatedRuntimeCallTreeMeasurementDTO aggregated) {
        aggregated.setType(MEASUREMENT_TYPE);
    }

    /**
     * Validates that aggregation data contains required fields.
     *
     * @param aggregated The aggregated measurement to validate
     * @return true if data is valid and sufficient for processing
     */
    private boolean isValidAggregationData(AggregatedRuntimeCallTreeMeasurementDTO aggregated) {
        List<Long> timestamps = aggregated.getTimestamps();
        List<Double> values = aggregated.getValues();
        List<?> iterations = aggregated.getIterations();

        return timestamps != null && !timestamps.isEmpty() &&
                values != null && !values.isEmpty() &&
                iterations != null && !iterations.isEmpty();
    }

    /**
     * Gets the consistent length for parallel arrays.
     *
     * @param timestamps The timestamps array
     * @param values The values array
     * @param iterations The iterations array
     * @return The minimum length of the three arrays
     */
    private int getConsistentDataLength(List<Long> timestamps, List<Double> values, List<?> iterations) {
        return Math.min(timestamps.size(), Math.min(values.size(), iterations.size()));
    }

    /**
     * Validates grid points parameter.
     *
     * @param gridPoints The number of grid points
     * @throws IllegalArgumentException if gridPoints is not positive
     */
    private void validateGridPoints(int gridPoints) {
        if (gridPoints <= 0) {
            throw new IllegalArgumentException("gridPoints must be positive, got: " + gridPoints);
        }
    }

    /**
     * Groups measurement points by their iteration.
     *
     * @param timestamps List of timestamps
     * @param values List of values
     * @param rawIterations List of iteration objects
     * @param dataLength Number of data points to process
     * @return Map of iteration key to measurement points
     */
    private Map<String, List<MeasurementPoint>> groupMeasurementsByIteration(
            List<Long> timestamps,
            List<Double> values,
            List<?> rawIterations,
            int dataLength) {

        Map<String, List<MeasurementPoint>> grouped = new LinkedHashMap<>();

        for (int i = 0; i < dataLength; i++) {
            String iterationKey = createIterationKey(rawIterations.get(i));
            MeasurementPoint point = new MeasurementPoint(
                    timestamps.get(i),
                    values.get(i) != null ? values.get(i) : 0.0
            );
            grouped.computeIfAbsent(iterationKey, k -> new ArrayList<>()).add(point);
        }

        return grouped;
    }

    /**
     * Builds time series from grouped measurement points.
     *
     * @param pointsByIteration Map of iteration key to measurement points
     * @return List of time series for each iteration
     */
    private List<TimeSeriesResampler.TimeSeries> buildTimeSeriesFromGroups(
            Map<String, List<MeasurementPoint>> pointsByIteration) {

        List<TimeSeriesResampler.TimeSeries> seriesList = new ArrayList<>();

        for (List<MeasurementPoint> iterationPoints : pointsByIteration.values()) {
            // Sort by timestamp for interpolation
            iterationPoints.sort(Comparator.comparingLong(p -> p.timestamp));

            List<Long> timestamps = new ArrayList<>();
            List<Double> values = new ArrayList<>();

            for (MeasurementPoint point : iterationPoints) {
                timestamps.add(point.timestamp);
                values.add(point.value);
            }

            seriesList.add(new TimeSeriesResampler.TimeSeries(timestamps, values));
            log.trace("Built time series with {} points", iterationPoints.size());
        }

        return seriesList;
    }

    /**
     * Populates the aggregated measurement with resampled data.
     *
     * @param aggregated The aggregated measurement to populate
     * @param result The resampling result
     * @param iterationKeys The iteration keys in order
     */
    private void populateResampledData(
            AggregatedRuntimeCallTreeMeasurementDTO aggregated,
            TimeSeriesResampler.ResamplingResult result,
            Set<String> iterationKeys) {

        // Set normalized timestamps
        aggregated.setTimestamps(result.getFixedTimestamps());

        // Create per-iteration aggregates
        List<IterationAggregateDTO> iterations = new ArrayList<>();
        List<String> keys = new ArrayList<>(iterationKeys);

        for (int i = 0; i < result.getResampledValues().size(); i++) {
            String description = keys.get(i);
            List<Double> resampledValues = result.getResampledValues().get(i);
            iterations.add(new IterationAggregateDTO(description, resampledValues));
        }

        aggregated.setIterations(iterations);

        // Compute and set aggregated values
        aggregated.setValues(computeAggregatedValues(result.getResampledValues()));
    }

    /**
     * Creates a unique, stable key for an iteration.
     *
     * @param iterationObj The iteration object (typically a Map or DTO)
     * @return A unique string key for the iteration
     */
    private String createIterationKey(Object iterationObj) {
        if (iterationObj == null) {
            return "unknown";
        }

        if (iterationObj instanceof Map<?, ?> map) {
            Integer pid = extractIntegerFromMap(map);
            Long startTs = extractLongFromMap(map);

            if (pid != null && startTs != null) {
                return String.format("pid:%d_start:%d", pid, startTs);
            }
        }

        // Fallback to string representation for unknown types
        return iterationObj.toString();
    }

    /**
     * Safely extracts a pid (Integer) from an iteration map.
     *
     * @param map The map to extract from
     * @return The extracted Integer, or null if not found or invalid type
     */
    private Integer extractIntegerFromMap(Map<?, ?> map) {
        Object obj = map.get("pid");

        if (obj instanceof Integer integer) {
            return integer;
        } else if (obj instanceof Number number) {
            return number.intValue();
        }

        return null;
    }

    /**
     * Safely extracts a startTimestamp (Long) from an iteration map.
     *
     * @param map The map to extract from
     * @return The extracted Long, or null if not found or invalid type
     */
    private Long extractLongFromMap(Map<?, ?> map) {
        Object obj = map.get("startTimestamp");

        if (obj instanceof Long longVal) {
            return longVal;
        } else if (obj instanceof Number number) {
            return number.longValue();
        }

        return null;
    }

    /**
     * Computes aggregated values across all iterations.
     *
     * Calculates the mean value at each grid point across all iterations.
     *
     * @param resampledValues List of value lists, one per iteration
     * @return Aggregated values (mean at each grid point)
     */
    private List<Double> computeAggregatedValues(List<List<Double>> resampledValues) {
        if (resampledValues == null || resampledValues.isEmpty()) {
            return new ArrayList<>();
        }

        int gridSize = resampledValues.get(0).size();
        List<Double> aggregated = new ArrayList<>(gridSize);

        for (int gridPoint = 0; gridPoint < gridSize; gridPoint++) {
            double sum = 0.0;
            int count = 0;

            for (List<Double> iterationValues : resampledValues) {
                if (gridPoint < iterationValues.size()) {
                    sum += iterationValues.get(gridPoint);
                    count++;
                }
            }

            double meanValue = (count > 0) ? (sum / count) : 0.0;
            aggregated.add(meanValue);
        }

        return aggregated;
    }

    /**
     * Immutable representation of a single measurement point in a time series.
     *
     * Holds the timestamp and value for a single measurement, to be grouped
     * by iteration before resampling.
     */
    private static final class MeasurementPoint {
        private final long timestamp;
        private final double value;

        /**
         * Creates a new measurement point.
         *
         * @param timestamp The time at which the measurement was taken
         * @param value The measured value at this timestamp
         */
        MeasurementPoint(long timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public long timestamp() {
            return timestamp;
        }

        public double value() {
            return value;
        }

        @Override
        public String toString() {
            return String.format("MeasurementPoint{timestamp=%d, value=%f}", timestamp, value);
        }
    }
}











