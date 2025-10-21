#!/bin/bash

# Blog Generator Script
# Generate static blog from example-blog using NiceMaker

set -e  # Exit on error

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "========================================="
echo "  NiceMaker Blog Generator"
echo "========================================="
echo ""

# Configuration
CONFIG_FILE="${1:-nice.yaml}"
BLOG_SOURCE="${2:-example-blog}"
OUTPUT_DIR="${3:-nicemaker/output}"

# Convert CONFIG_FILE to absolute path
if [[ "$CONFIG_FILE" != /* ]]; then
    CONFIG_FILE="$(pwd)/$CONFIG_FILE"
fi

# Check if config file exists
if [ ! -f "$CONFIG_FILE" ]; then
    echo -e "${RED}Error: Configuration file '$CONFIG_FILE' not found${NC}"
    echo "Usage: $0 [config_file] [blog_source] [output_dir]"
    echo ""
    echo "Examples:"
    echo "  $0                                    # Use default: nice.yaml, example-blog"
    echo "  $0 nice.yaml sample-blog              # Use sample-blog as source"
    echo "  $0 nice.example.yaml example-blog     # Use nice.example.yaml config"
    exit 1
fi

# Check if blog source exists
if [ ! -d "$BLOG_SOURCE" ]; then
    echo -e "${YELLOW}Warning: Blog source directory '$BLOG_SOURCE' not found${NC}"
    echo "Will try to clone from Git repository specified in config..."
    echo ""
fi

echo -e "${BLUE}Configuration:${NC}"
echo "  Config file:  $CONFIG_FILE"
echo "  Blog source:  $BLOG_SOURCE"
echo "  Output dir:   $OUTPUT_DIR"
echo ""

# Step 1: Build the project
echo -e "${YELLOW}Step 1: Building NiceMaker...${NC}"
echo "-------------------------------------------"
if ./gradlew :nicemaker:jvmJar --console=plain --no-daemon; then
    echo -e "${GREEN}✓ Build successful${NC}"
    echo ""
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi

# Step 2: Generate the blog
echo -e "${YELLOW}Step 2: Generating blog...${NC}"
echo "-------------------------------------------"

# Run the generator (使用 jvmRun 而不是 run)
if ./gradlew :nicemaker:jvmRun --args="build -c $CONFIG_FILE" --console=plain; then
    echo ""
    echo -e "${GREEN}✓ Blog generated successfully${NC}"
    echo ""
else
    echo ""
    echo -e "${RED}✗ Blog generation failed${NC}"
    exit 1
fi

# Step 3: Show output information
echo "========================================="
echo "  Generation Complete!"
echo "========================================="
echo ""
echo -e "${GREEN}Blog has been generated to: ${NC}${OUTPUT_DIR}"
echo ""
echo "Next steps:"
echo "  1. View the blog: open ${OUTPUT_DIR}/index.html"
echo "  2. Start dev server: $0 --serve"
echo "  3. Deploy to production"
echo ""

# Check if output exists and show statistics
if [ -d "$OUTPUT_DIR" ]; then
    HTML_COUNT=$(find "$OUTPUT_DIR" -name "*.html" 2>/dev/null | wc -l)
    echo -e "${BLUE}Statistics:${NC}"
    echo "  HTML files: $HTML_COUNT"

    if [ -f "$OUTPUT_DIR/sitemap.xml" ]; then
        echo "  Sitemap: ✓"
    fi

    echo ""
fi

# Offer to open the blog
read -p "Do you want to open the blog in browser? (y/N) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    if command -v open &> /dev/null; then
        open "${OUTPUT_DIR}/index.html"
    elif command -v xdg-open &> /dev/null; then
        xdg-open "${OUTPUT_DIR}/index.html"
    else
        echo "Please open ${OUTPUT_DIR}/index.html in your browser"
    fi
fi

