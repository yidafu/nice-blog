#!/bin/bash

# JVM Platform Test Runner
# This script runs all JVM platform tests for the nice-blog project

set -e  # Exit on error

echo "========================================="
echo "  Running JVM Platform Tests"
echo "========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Gradle wrapper exists
if [ ! -f "./gradlew" ]; then
    echo -e "${RED}Error: gradlew not found in current directory${NC}"
    exit 1
fi

# Function to run tests for a module
run_module_tests() {
    local module=$1
    echo -e "${YELLOW}Testing module: ${module}${NC}"
    echo "-------------------------------------------"

    if ./gradlew :${module}:jvmTest --console=plain; then
        echo -e "${GREEN}✓ ${module} tests passed${NC}"
        echo ""
        return 0
    else
        echo -e "${RED}✗ ${module} tests failed${NC}"
        echo ""
        return 1
    fi
}

# Main execution
echo "Starting JVM tests for all modules..."
echo ""

FAILED_MODULES=()
PASSED_MODULES=()

# Test nicemaker module
if run_module_tests "nicemaker"; then
    PASSED_MODULES+=("nicemaker")
else
    FAILED_MODULES+=("nicemaker")
fi

# Test themes module
if run_module_tests "themes"; then
    PASSED_MODULES+=("themes")
else
    FAILED_MODULES+=("themes")
fi

# Summary
echo "========================================="
echo "  Test Summary"
echo "========================================="
echo ""

if [ ${#PASSED_MODULES[@]} -gt 0 ]; then
    echo -e "${GREEN}Passed modules (${#PASSED_MODULES[@]}):${NC}"
    for module in "${PASSED_MODULES[@]}"; do
        echo -e "  ${GREEN}✓${NC} $module"
    done
    echo ""
fi

if [ ${#FAILED_MODULES[@]} -gt 0 ]; then
    echo -e "${RED}Failed modules (${#FAILED_MODULES[@]}):${NC}"
    for module in "${FAILED_MODULES[@]}"; do
        echo -e "  ${RED}✗${NC} $module"
    done
    echo ""
    echo -e "${RED}Tests failed!${NC}"
    exit 1
else
    echo -e "${GREEN}All tests passed!${NC}"
    exit 0
fi

