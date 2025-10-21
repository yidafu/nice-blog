#!/bin/bash

# Blog Development Server
# Serve generated blog for local preview

set -e  # Exit on error

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "========================================="
echo "  NiceMaker Development Server"
echo "========================================="
echo ""

# Configuration
CONFIG_FILE="${1:-nice.yaml}"
PORT="${2:-8080}"

if [ ! -f "$CONFIG_FILE" ]; then
    echo -e "${YELLOW}Warning: Configuration file '$CONFIG_FILE' not found${NC}"
    echo "Using default configuration..."
    echo ""
fi

echo -e "${BLUE}Starting development server...${NC}"
echo "  Config: $CONFIG_FILE"
echo "  Port:   $PORT"
echo ""
echo -e "${GREEN}Server will be available at: http://localhost:$PORT${NC}"
echo ""
echo "Press Ctrl+C to stop the server"
echo ""

# Run the server
./gradlew :nicemaker:run --args="serve -c $CONFIG_FILE -p $PORT" --console=plain

