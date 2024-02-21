#!/bin/bash

LOGFILE="$PLUGINS_DIRECTORY/totalTime.txt"

log_and_print_output_with_date () {
    echo -e `date "+%Y/%m/%d %H:%M:%S"`" - $1\n\n"
    echo `date "+%Y/%m/%d %H:%M:%S"`" - $1" >> $LOGFILE
}

log_title_output() {
    local title="$1"
    local len=${#title}

    local bar_line=$(printf '%*s' "$len" | tr ' ' '=')
    echo "$bar_line" >> $LOGFILE
    echo "$title" >> $LOGFILE
    echo "$bar_line" >> $LOGFILE
}

log_iteration_output() {
    echo -e "        => "`date "+%Y/%m/%d %H:%M:%S"`" - Test for iteration $1 done!\n\n"
    echo "        => "`date "+%Y/%m/%d %H:%M:%S"`" - Test for iteration $1 done!" >> $LOGFILE
}

:'log_output_without_date() {
    echo "$1" >> $LOGFILE
}

parse_and_log_test_results() {
    local log_file="$1"
    local project_type="$2"
    if [ "$project_type" == "gradle" ]; then
        local completed=$(grep -oP '\d+(?= tests completed)' "$log_file")
        local failed=$(grep -oP '\d+(?= failed)' "$log_file")
        local skipped=$(grep -oP '\d+(?= skipped)' "$log_file")

        log_output_without_date "        --------------------------"
        log_output_without_date "        Total tests completed: $completed"
        log_output_without_date "        Total test failed: $failed"
        log_output_without_date "        Total tests skipped: $skipped"
        log_output_without_date "        --------------------------"
    elif [ "$project_type" == "maven" ]; then
        local results_block=$(grep -A 1000 'Results:' "$log_file" | sed -n '/Results:/,/^$/p')
        echo $results_block
        local total=$(echo "$results_block" | grep 'Tests run' | awk '{print $3}')
        echo "total = $total"
    fi
}''
