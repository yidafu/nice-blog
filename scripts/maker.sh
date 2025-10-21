#!/bin/bash

# NiceMaker CLI Wrapper
# Convenient wrapper for running NiceMaker commands

set -e

# Colors
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Show help if no arguments
if [ $# -eq 0 ]; then
    echo "NiceMaker CLI Wrapper"
    echo ""
    echo "Usage:"
    echo "  $0 build [-c config.yaml]           Generate static blog"
    echo "  $0 serve [port|dir] [dir]           Start development server"
    echo "  $0 help                              Show detailed help"
    echo ""
    echo "Examples:"
    echo "  $0 build                             Generate with nice.yaml"
    echo "  $0 build -c nice.example.yaml        Generate with custom config"
    echo "  $0 serve                             Start server (port 3000, default output)"
    echo "  $0 serve 8080                        Start server on port 8080"
    echo "  $0 serve ./nicemaker/output          Start server with custom output dir"
    echo "  $0 serve 8080 ./custom-output        Start server on port 8080 with custom dir"
    echo ""
    exit 0
fi

COMMAND=$1
shift

echo -e "${BLUE}Running NiceMaker CLI...${NC}"
echo ""

# Ensure project is built (for KMP, we use jvmJar)
if [ ! -f "nicemaker/build/libs/nicemaker-jvm.jar" ]; then
    echo -e "${YELLOW}Building NiceMaker first...${NC}"
    ./gradlew :nicemaker:jvmJar --console=plain --no-daemon
    echo ""
fi

# Run the command
case "$COMMAND" in
    build)
        ./gradlew :nicemaker:jvmRun --args="build $*" --console=plain
        ;;
    serve)
        # 智能解析参数：支持 port 和 directory
        PORT=""
        OUTPUT_DIR=""

        # 解析第一个参数
        if [[ "$1" =~ ^[0-9]+$ ]]; then
            # 第一个参数是数字，作为端口
            PORT="$1"
            OUTPUT_DIR="$2"
        elif [ -n "$1" ]; then
            # 第一个参数不是数字，作为目录
            OUTPUT_DIR="$1"
        fi

        # 构建参数字符串
        SERVE_ARGS=""
        [ -n "$PORT" ] && SERVE_ARGS="$SERVE_ARGS -p $PORT"
        [ -n "$OUTPUT_DIR" ] && SERVE_ARGS="$SERVE_ARGS -d $OUTPUT_DIR"

        echo -e "${YELLOW}Starting server with args:$SERVE_ARGS${NC}"
        ./gradlew :nicemaker:jvmRun --args="serve$SERVE_ARGS" --console=plain
        ;;
    help|--help|-h)
        ./gradlew :nicemaker:jvmRun --args="--help" --console=plain
        ;;
    *)
        echo "Unknown command: $COMMAND"
        echo "Run '$0' without arguments to see usage"
        exit 1
        ;;
esac

