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

log_output_without_date() {
    echo "$1" >> $LOGFILE
}

log_configuration() {
    log_output_without_date "+++++++++++++++++++"
    log_output_without_date "Execution args:"
    log_output_without_date "REPO_DIRECTORY : $REPO_DIRECTORY"
    log_output_without_date "PLUGINS_DIRECTORY : $PLUGINS_DIRECTORY"
    log_output_without_date "BATCH_SIZE : $BATCH_SIZE"
    log_output_without_date "NB_ITERATION : $NB_ITERATION"
    log_output_without_date "REMOVE_REPO : $REMOVE_REPO"
    log_output_without_date "CLONE : $CLONE"
    log_output_without_date "CK : $CK"
    log_output_without_date "JOULAR : $JOULAR"
    log_output_without_date "NB_WARMUP : $NB_WARMUP"
    log_output_without_date "+++++++++++++++++++"
}

log_iteration_output() {
    echo -e "        => "`date "+%Y/%m/%d %H:%M:%S"`" - Test for iteration $1 done!\n\n"
    echo "        => "`date "+%Y/%m/%d %H:%M:%S"`" - Test for iteration $1 done!" >> "$LOGFILE"
}

log_sleep_iteration() {
    echo "        => Sleep $1 seconds before next iteration" >> "$LOGFILE"
}
